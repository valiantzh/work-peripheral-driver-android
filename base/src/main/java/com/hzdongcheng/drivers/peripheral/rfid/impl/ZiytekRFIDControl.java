package com.hzdongcheng.drivers.peripheral.rfid.impl;

import android.util.Log;

import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;
import com.hzdongcheng.drivers.peripheral.rfid.AbstractRFIDControl;
import com.hzdongcheng.drivers.peripheral.rfid.model.AntennaParam;
import com.hzdongcheng.drivers.peripheral.rfid.model.TagInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZiytekRFIDControl extends AbstractRFIDControl {

    private final static String TAG = "RFIDControl";

    @Override
    public List<TagInformation> readTagInfo(){

        List<TagInformation> tagInformationList = new ArrayList<>();
        try {
            byte[] checkedData = new byte[]{0x5A, 0x55, 0x08, 0x00, 0x0D, 0x11, 0x00, 0x00, 0x00, 0x00, 0x6A, 0x69};
            byte checkSum = calcSum(checkedData);
            checkedData[checkedData.length-3] = checkSum;
            serialPortBase.sendFixLength(checkedData,checkedData.length);
            byte[] buf = new byte[MAX_RECV_LEN];

            Map<String, TagInformation> epcMaps = new HashMap<>();

            do {
                TagInformation tagInformation = new TagInformation();
                serialPortBase.recv(buf,buf.length);
                if (buf[0] == 0x5a && buf[1] == 0x55) { //验证帧头为0x5a55
                    int totalLength = buf[2]; //获得帧长度
                    int sum = buf[2 + totalLength - 1]; //返回的校验和
                    buf[2 + totalLength - 1] = 0x00; //将返回的校验和设为0以便本地计算校验和
                    if (calcSum(buf,0,totalLength) == sum) { //对比校验和
                        int dataLength = buf[15]; //获得epc编码长度
                        String epc = byteArrayToHexStringNoSpace(buf,17,dataLength); //获得格式化epc编码字符串

                        if (!epc.isEmpty()) { //添加至TagInformation
                            epcMaps.put(epc, tagInformation);
                            tagInformation.setAntennaNo(buf[8]);
                            byte[] startTime = Arrays.copyOfRange(buf,9,13);
                            tagInformation.setStartTime(reverseByteArrayToInt(startTime));
                        }
                    }
                }
            } while (!(buf[2]==0x0f&&buf[5]==0x01)); //唯一判断结束条件 第三位为0x0f 第六位为0x01 TODO Command_End命令经常混在盘点回复指令里导致超时 暂无影响

            for (Map.Entry<String, TagInformation> entry : epcMaps.entrySet()) {
                entry.getValue().setEPCcode(entry.getKey());
                tagInformationList.add(entry.getValue());
            }

        } catch (SerialPortException e) {
            reset(); //报错后复位
            Log.e(TAG,e.getErrorTitle());
            e.printStackTrace();
        }
        return tagInformationList;
    }

    @Override
    public void cancel(){
        try {
            serialPortBase.send(new byte[]{0x5A, 0x55, 0x06, 0x00, 0x0D, 0x01, 0x00, (byte) 0xC3, 0x6A, 0x69},10);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reset(){
        try {
            serialPortBase.send(new byte[]{0x5A, 0x55, 0x06, 0x00, 0x0D, 0x02, 0x00, (byte) 0xC4, 0x6A, 0x69},10);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void abort(){
        try {
            serialPortBase.send(new byte[]{0x5A, 0x55, 0x06, 0x00, 0x0D, 0x03, 0x00, (byte) 0xC5, 0x6A, 0x69},10);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause(){
        try {
            serialPortBase.send(new byte[]{0x5A, 0x55, 0x06, 0x00, 0x0D, 0x04, 0x00, (byte) 0xC6, 0x6A, 0x69},10);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resume(){
        try {
            serialPortBase.send(new byte[]{0x5A, 0x55, 0x06, 0x00, 0x0D, 0x05, 0x00, (byte) 0xC7, 0x6A, 0x69},10);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    public int getAntennaStatus(int antennaNo) throws DriversException{
        int status = 0; //默认关闭
        try {
            byte[] buf = new byte[]{0x5a, 0x55, 0x07, 0x00, 0x0d, 0x1b, 0x00, (byte) antennaNo, 0x00, 0x6a, 0x69};
            byte sum = calcSum(buf);
            buf[buf.length-3] = sum;
            serialPortBase.send(buf,buf.length);
            byte[] szData = new byte[12];
            serialPortBase.recvFixLength(szData,szData.length);
            if (szData[szData.length - 5] == 1) { //判断获取是否成功
                status = szData[szData.length - 4]; //获取天线状态
            } else {
                throw new DriversException(DriversErrorCode.ERR_PD_RFID_GET_ANTENNA_STATUS_FAILED);
            }
        } catch (SerialPortException e) {
            reset();
            Log.e(TAG,e.getErrorTitle());
            e.printStackTrace();
        }
        return status;
    }

    public boolean setAntennaStatus(int antennaNo, int status){
        try {
            byte[] buf = new byte[]{0x5a, 0x55, 0x08, 0x00, 0x0d, 0x1a, 0x00, (byte) antennaNo, (byte) status, 0x00, 0x6a, 0x69};
            byte sum = calcSum(buf);
            buf[buf.length - 3] = sum;
            serialPortBase.send(buf, buf.length);
            byte[] szData = new byte[11];
            serialPortBase.recvFixLength(szData, szData.length);
            return (szData[szData.length - 4] == 1); //返回操作状态
        } catch (SerialPortException e) {
            reset();
            Log.e(TAG, e.getErrorTitle());
            e.printStackTrace();
        }
        return false;
    }

    public AntennaParam getAntennaParam(int antennaNo) throws DriversException{
        AntennaParam antennaParam = new AntennaParam();
        try {
            byte[] buf = new byte[]{0x5a, 0x55, 0x07, 0x00, 0x0d, 0x19, 0x00, (byte) antennaNo, 0x00, 0x6a, 0x69};
            byte sum = calcSum(buf);
            buf[buf.length - 3] = sum;
            serialPortBase.send(buf, buf.length);
            byte[] szData = new byte[23];
            serialPortBase.recvFixLength(szData, szData.length);
            if (szData[7] == 1) { //判断操作是否成功
                byte[] power = Arrays.copyOfRange(szData,8,12); //天线功率
                byte[] stayTime = Arrays.copyOfRange(szData,12,16); //驻留时间
                byte[] period = Arrays.copyOfRange(szData,16,20); //盘讯周期
                antennaParam.setPower(reverseByteArrayToInt(power));
                antennaParam.setStayTime(reverseByteArrayToInt(stayTime));
                antennaParam.setPeriod(reverseByteArrayToInt(period));
                antennaParam.setIndex(antennaNo);
            } else {
                throw new DriversException(DriversErrorCode.ERR_PD_RFID_GET_ANTENNA_PARAM_FAILED);
            }
        } catch (SerialPortException e) {
            reset();
            Log.e(TAG, e.getErrorTitle());
            e.printStackTrace();
        }

        return antennaParam;
    }

    public boolean setAntennaParam(AntennaParam antennaParam) {
        try {
            byte index = (byte) antennaParam.getIndex();
            byte[] power = integerToReverse4ByteArray(antennaParam.getPower()*10); //天线功率填写的是实际要配置的功率乘上 10 后的数值
            byte[] stayTime = integerToReverse4ByteArray(antennaParam.getStayTime());
            byte[] period = integerToReverse4ByteArray(antennaParam.getPeriod());
            byte[] buf = new byte[]{0x5a, 0x55, 0x13, 0x00, 0x0d, 0x18, 0x00, index, power[0], power[1], power[2], power[3], stayTime[0], stayTime[1], stayTime[2], stayTime[3], period[0], period[1], period[2], period[3], 0x00, 0x6a, 0x69};
            byte sum = calcSum(buf);
            buf[buf.length - 3] = sum;
            serialPortBase.send(buf, buf.length);
            byte[] szData = new byte[23];
            serialPortBase.recvFixLength(szData, szData.length);
            return (szData[szData.length - 4] == 1);
        } catch (SerialPortException e) {
            reset();
            Log.e(TAG, e.getErrorTitle());
            e.printStackTrace();
        }
        return false;
    }



}
