package com.hzdongcheng.drivers.peripheral.rfid;

import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.rfid.model.TagInformation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractRFIDControl implements IRFIDControl {

    protected final SerialPortBase serialPortBase = new SerialPortBase();
    // 最大数据接收长度
    protected final int MAX_RECV_LEN = 512;
    // 最大数据发送长度
    protected final int MAX_SEND_LEN = 100;

    @Override
    public void open(String portName) throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
        serialPortBase.open(portName);
        try {
            // 初始化串口
            serialPortBase.initialize(SerialPortBase.DCDZ_CBR_115200, SerialPortBase.DCDZ_NOPARITY,SerialPortBase.DCDZ_ONESTOPBIT,SerialPortBase.DCDZ_DATABITS8);
            // 设置读写缓冲区大小
            serialPortBase.setInOutQueue(MAX_RECV_LEN, MAX_SEND_LEN);
            // 设置读写超时时间
            serialPortBase.setReadWriteTimeouts(2000, 1000);
        } catch (SerialPortException e) {
            close();
            throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
        }
    }

    @Override
    public void close() {
        serialPortBase.close();
    }

    /**
     * @Title: calcSum
     * @Description: 计算校验和
     * @param data 需要计算的byte array
     * @return sum 校验和
     * @throws
     */
    protected byte calcSum(byte[] data){
        byte sum = 0x00;
        for (int i = 0; i < data.length - 2; i++) { //计算从头开始到倒数第三位的加权和
            sum += data[i];
        }
        return sum;
    }

    /**
     * @Title: calcSum
     * @Description: 计算校验和
     * @param data 需要计算的byte array
     * @param offset 首位位移
     * @param count 需计算位数
     * @return sum 校验和
     * @throws
     */
    protected byte calcSum(byte[] data, int offset, int count) {
        byte sum = 0x00;
        for (int i = offset; i < count; i++) {  //计算从第offset位开始长度为count的加权和
            sum += data[i];
        }
        return sum;
    }

    /**
     * @Title: byteArrayToHexStringNoSpace
     * @Description: byte array转化为连续的hex string
     * @param byteArray 需要转换的byte array
     * @return retString 转换后的hex string
     * @throws
     */
    protected static String byteArrayToHexStringNoSpace(byte[] byteArray) {
        String retString = "";
        byte[] var5 = byteArray;
        int var4 = byteArray.length;
        for(int var3 = 0; var3 < var4; ++var3) {
            byte b = var5[var3];
            retString = retString + String.format("%02x", b);
        }
        return retString;
    }

    /**
     * @Title: byteArrayToHexStringNoSpace
     * @Description: byte array转化为连续的hex string
     * @param byteArray 需要转换的byte array
     * @param offset 首位位移
     * @param count 需转换位数
     * @return retString 转换后的hex string
     * @throws
     */
    protected static String byteArrayToHexStringNoSpace(byte[] byteArray, int offset, int count) {
        String retString = "";
        for(int i = 0; i < count; ++i) {
            retString = retString + String.format("%02x", byteArray[offset + i]);
        }
        return retString;
    }

    /**
     * @Title: reverseByteArrayToInt
     * @Description: Little Endian顺序的byte array转换为单个int
     * @param byteArray 需要转换的byte array
     * @return int
     * @throws
     */
    protected int reverseByteArrayToInt(byte[] byteArray) {
        return ByteBuffer.wrap(byteArray).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    /**
     * @Title: integerToReverse4ByteArray
     * @Description: 单个int转换为Little Endian顺序大小为4的byte array
     * @param data 需要转换的int
     * @return byte array
     * @throws
     */
    protected byte[] integerToReverse4ByteArray(int data) {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data).array();
    }
}
