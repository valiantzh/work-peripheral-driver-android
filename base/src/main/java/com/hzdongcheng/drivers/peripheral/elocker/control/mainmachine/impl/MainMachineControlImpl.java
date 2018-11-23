/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  MainMachineControlImpl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.mainmachine.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:33:43   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.impl;

import com.hzdongcheng.drivers.common.Constants;
import com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.AbstractMainMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.MainMachineStatus;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/** 
* @ClassName: MainMachineControlImpl 
* @Description: TODO(主机控制实现类) 
* @author Jxing 
* @date 2017年4月12日 下午3:33:43 
* @version 1.0 
*/
public class MainMachineControlImpl extends AbstractMainMachineControl {

	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param serialPort 
	*/
	public MainMachineControlImpl(SerialPortBase serialPort) {
		super(serialPort);
	}

	/* (non Javadoc) 
	* <p>Title: getDeviceCode</p> 
	* <p>Description: </p> 
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl#getDeviceCode()
	*/
	@Override
	public String getDeviceCode() throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non Javadoc) 
	* <p>Title: setDeviceCode</p> 
	* <p>Description: </p> 
	* @param code
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl#setDeviceCode(java.lang.String)
	*/
	@Override
	public void setDeviceCode(String code) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MainMachineStatus queryMainStatus() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {

		MainMachineStatus mainMachineStatus = new MainMachineStatus();

		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'A';
		szRequestData[5] = 0;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"queryMainStatus request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");
		}

		if (recvBuff[5] != 8) {
			throw new ProtocolParsingException(
					SerialPortErrorCode.ERR_SP_NOTFOUNDSERIALLIBRARY);
		}

		// 获取主机柜所有状态
		// 第一个字节：Bit0：管理，Bit1：备用，Bit2：防拆1，Bit3：防拆2，Bit4：震动，Bit5：电源状态 Bit6:人体靠近检测
		// 第二个字节：湿度
		// 第三、四字节：温度
		// 最后四个字节：温度上下限
		int bit1 = (recvBuff[6] & 0x01) == 0 ? 1 : 0; // 管理开关
		// int bit2 = ( recvBuff[6] & 0x02 ) >> 1; //预留
		int bit3 = (recvBuff[6] & 0x04) >> 2; // 防撬
		// int bit4 = ( recvBuff[6] & 0x08 ) >> 3; //防撬2
		int bit5 = (recvBuff[6] & 0x10) >> 4; // 震动
		int bit6 = (recvBuff[6] & 0x20) >> 5; // 电源状态
		int bit7 = ( recvBuff[6] & 0x40 ) >> 6;//人体靠近检测 add by zxy 20180817
		// int bit8 = ( recvBuff[6] & 0x80 ) >> 7;

		// 湿度
		int sd = recvBuff[7];
		// 温度
		int f = recvBuff[8]; // 0:正数 1:负数
		int wd = f == 0 ? recvBuff[9] : -recvBuff[9];

		// 温度上限
		f = recvBuff[10]; // 0:正数 1:负数
		int wdMax = f == 0 ? recvBuff[11] : -recvBuff[11];

		// 温度下限
		f = recvBuff[12]; // 0:正数 1:负数
		int wdMin = f == 0 ? recvBuff[13] : -recvBuff[13];

		mainMachineStatus.setManagementSwitch(bit1);
		mainMachineStatus.setTamperStatus(bit3);
		mainMachineStatus.setShockStatus(bit5);
		mainMachineStatus.setPowerStatus(bit6);
		mainMachineStatus.setAnearStatus(bit7);
		mainMachineStatus.setWorkingHumidity(sd);
		mainMachineStatus.setWorkingTemperature(wd);
		mainMachineStatus.setWorkingTemperatureMax(wdMax);
		mainMachineStatus.setWorkingTemperatureMin(wdMin);

		return mainMachineStatus;
	}

	@Override
	public void setTemperatureRange(int min, int max)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {
		byte[] szRequestData = new byte[12];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0C;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'S';
		szRequestData[5] = 4;
		szRequestData[6] = (byte) ((max < 0) ? 1 : 0);
		szRequestData[7] = (byte) max;
		szRequestData[8] = (byte) ((min < 0) ? 1 : 0);
		szRequestData[9] = (byte) min;
		szRequestData[10] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[11] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];

		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE,
					"setTemperatureRange request failed, the response code wrong and code is " + recvBuff[4] + ".");

	}

	@Override
	public String getTemperatureRange() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {
		String resultString = "";
		int min = 0;
		int max = 0;

		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'G';
		szRequestData[5] = 0;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getTemperatureRange request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

		if (recvBuff[6] == 1)
			max = -recvBuff[7];
		else
			max = recvBuff[7];

		if (recvBuff[8] == 1)
			min = -recvBuff[9];
		else
			min = recvBuff[9];

		resultString = min + ":" + max;

		return resultString;
	}

	@Override
	public String getCurTemperature() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {
		String resultString = "";
		int SI7005 = 0;
		int DS18B20 = 0;

		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'T';
		szRequestData[5] = 0;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getCurTemperature request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

		if (recvBuff[6] == 1) //
			SI7005 = -recvBuff[7];
		else
			SI7005 = recvBuff[7];

		if (recvBuff[8] == 1) //
			DS18B20 = -recvBuff[9];
		else
			DS18B20 = recvBuff[9];

		resultString = SI7005 + ":" + DS18B20;

		return resultString;
	}

	@Override
	public void weakCurrentManage(int[] channelNO, int[] val)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte channelNO1 = 0;
		for (int i = 7; i >= 0; i--) {
			int j = channelNO[i];
			byte state = (byte) (j > 0 ? 1 : 0);

			state = (byte) (state << i);
			channelNO1 |= state;
		}

		byte channelNO2 = 0;
		for (int i = 7; i >= 0; i--) {
			byte state = (byte) (channelNO[8 + i] > 0 ? 1 : 0);
			state = (byte) (state << i);
			channelNO2 |= state;
		}

		byte val1 = 0;
		for (int i = 7; i >= 0; i--) {
			byte state = (byte) (val[i] > 0 ? 1 : 0);
			state = (byte) (state << i);
			val1 |= state;
		}

		byte val2 = 0;
		for (int i = 7; i >= 0; i--) {
			byte state = (byte) (val[8 + i] > 0 ? 1 : 0);
			state = (byte) (state << i);
			val2 |= state;
		}

		byte[] szRequestData = new byte[12];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0C;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'P';
		szRequestData[5] = 4;
		szRequestData[6] = channelNO1;
		szRequestData[7] = channelNO2;
		szRequestData[8] = val1;
		szRequestData[9] = val2;
		szRequestData[10] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[11] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"weakCurrentManage request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

	}

	@Override
	public void strongCurrentManage(int[] channelNO, int[] val)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte channelNO1 = 0;
		for (int i = 7; i >= 0; i--) {
			int j = channelNO[i];
			byte state = (byte) (j > 0 ? 1 : 0);

			state = (byte) (state << i);
			channelNO1 |= state;
		}

		byte val1 = 0;
		for (int i = 7; i >= 0; i--) {
			byte state = (byte) (val[i] > 0 ? 1 : 0);
			state = (byte) (state << i);
			val1 |= state;
		}

		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0C;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'P';
		szRequestData[5] = 4;
		szRequestData[6] = channelNO1;
		szRequestData[7] = val1;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"strongCurrentManage request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

	}

	@Override
	public void openDeliveryBox() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'L';
		szRequestData[5] = 1;
		szRequestData[6] = 1;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"openDeliveryBox request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

	}

	@Override
	public void closeDeliveryBox() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'L';
		szRequestData[5] = 1;
		szRequestData[6] = 0;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"closeDeliveryBox request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

	}

	@Override
	public BoxStatus queryDeliveryBoxStatus() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {

		BoxStatus boxStatus = new BoxStatus();
		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'O';
		szRequestData[5] = 1;
		szRequestData[6] = 1;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"queryDeliveryBoxStatus request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

		boxStatus.setGoodsStatus(recvBuff[6]);
		boxStatus.setOpenStatus(MainMachineStatus.UNKNOWN_STATUS);
		return boxStatus;
	}

	@Override
	public BoxStatus openPickUpbox() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException {

		BoxStatus boxStatus = new BoxStatus();

		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'F';
		szRequestData[5] = 0;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"queryDeliveryBoxStatus request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

		if (recvBuff[5] < 6 || recvBuff[5] % 2 != 0 || recvBuff[5] > 14)
			throw new ProtocolParsingException(
					SerialPortErrorCode.ERR_SP_NOTFOUNDSERIALLIBRARY);

		// 最大箱门数量
		byte totalBoxNums = (byte) ((recvBuff[5] - 4) / 2 * 8);

		int[] szByte = new int[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40,
				0x80 };

		// 物品状态
		for (byte n = 0, j = 0, k = 0; n < totalBoxNums; n++) {
			byte uArticle = (byte) ((recvBuff[6 + k] & szByte[j]) >> j);
			j++;

			if (n == 0)
				boxStatus
						.setGoodsStatus(uArticle == 0 ? MainMachineStatus.ZERO_STATUS
								: MainMachineStatus.ONE_STATUS);

			if (j == 8) {
				k++;
				j = 0;
			}
		}

		// 箱门开关状态
		for (byte n = 0, j = 0, k = 0; n < totalBoxNums; n++) {
			byte uOpen = (byte) ((recvBuff[6 + totalBoxNums / 8 + k] & szByte[j]) >> j);
			j++;

			if (n == 0)
				boxStatus
						.setOpenStatus(uOpen == 1 ? MainMachineStatus.ZERO_STATUS
								: MainMachineStatus.ONE_STATUS);
			if (j == 8) {
				k++;
				j = 0;
			}
		}

		return boxStatus;
	}

	/* (non Javadoc) 
	* <p>Title: enableKeepAlive</p> 
	* <p>Description: </p> 
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl#enableKeepAlive()
	*/
	@Override
	public void enableKeepAlive() throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'M';
		szRequestData[5] = 1;
		szRequestData[6] = 1;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;
		
		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		
		if (0 != recvBuff[4]) {
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE,
					"enableKeepAlive request failed, the response code wrong and code is " + recvBuff[4] + ".");
		}
	}

	/* (non Javadoc) 
	* <p>Title: disenableKeepAlive</p> 
	* <p>Description: </p> 
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl#disenableKeepAlive()
	*/
	@Override
	public void disenableKeepAlive() throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'M';
		szRequestData[5] = 1;
		szRequestData[6] = 0;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;
		
		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		
		if (0 != recvBuff[4]) {
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE,
					"disenableKeepAlive request failed, the response code wrong and code is " + recvBuff[4] + ".");
		}
	}

	/* (non Javadoc) 
	* <p>Title: keepAlive</p> 
	* <p>Description: </p> 
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl#keepAlive()
	*/
	@Override
	public void keepAlive() throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'L';
		szRequestData[5] = 0;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;
		
		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		
		if (0 != recvBuff[4]) {
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE,
					"keepAlive request failed, the response code wrong and code is " + recvBuff[4] + ".");
		}
	}

	/* (non Javadoc) 
	* <p>Title: restartPower</p> 
	* <p>Description: </p> 
	* @param delayMills
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl#restartPower(int)
	*/
	@Override
	public void restartPower(int delayMills) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		int delaySec = 0;
		
		if (delayMills % 1000 == 0){
			delaySec = delayMills / 1000;
		} else {
			delaySec = ( delayMills / 1000 ) + 1;
		}
		
		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) 0xA0;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'R';
		szRequestData[5] = 1;
		szRequestData[6] = (byte)delaySec;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;
		
		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		
		if (0 != recvBuff[4]) {
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE,
					"restartPower request failed, the response code wrong and code is " + recvBuff[4] + ".");
		}
	}


}
