/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  FoodViceMachineControlImpl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.vicemachine.food.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:11:36   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.food.impl;

import com.hzdongcheng.drivers.common.Constants;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.food.IFoodViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.MainMachineStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * @ClassName: FoodViceMachineControlImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jxing
 * @date 2017年4月12日 下午3:11:36
 * @version 1.0
 */
public class FoodViceMachineControlImpl extends AbstractViceMachineControl
		implements IFoodViceMachineControl {

	public FoodViceMachineControlImpl(SerialPortBase serialPort) {
		super(serialPort);
	}

	/*
	 * (non Javadoc) <p>Title: openBox</p> <p>Description: </p>
	 * 
	 * @param boardID
	 * 
	 * @param boxID
	 * 
	 * @see
	 * com.dcdzsoft.drivers.controller.elocker.IViceMachineControl#openBox
	 * (byte, byte)
	 */
	@Override
	public BoxStatus openBox(byte boardID, byte boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		BoxStatus boxStatus = new BoxStatus();
		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'O';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = 0x01;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 == recvBuff[4] && 0 == recvBuff[5]) {
			boxStatus.setOpenStatus(recvBuff[7]);
		} else {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"openBox request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}
		return boxStatus;
	}

	/*
	 * (non Javadoc) <p>Title: queryStatus</p> <p>Description: </p>
	 * 
	 * @param boardID
	 * 
	 * @see
	 * com.dcdzsoft.drivers.controller.elocker.IViceMachineControl#queryStatus
	 * (byte)
	 */
	@Override
	public ViceMachineStatus queryStatus(byte boardID) {
		return null;
	}

	/*
	 * (non Javadoc) <p>Title: queryStatus</p> <p>Description: </p>
	 * 
	 * @param boardID
	 * 
	 * @param boxID
	 * 
	 * @see
	 * com.dcdzsoft.drivers.controller.elocker.IViceMachineControl#queryStatus
	 * (byte, byte)
	 */
	@Override
	public BoxStatus queryStatus(byte boardID, byte boxID) {
		return null;
	}

	@Override
	public void setTemperatureRange(int boardID, int boxID, int temperature)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'c';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = (byte) temperature;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setTemperatureRange request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

	}

	@Override
	public void setAllHeating(int boardID, int heatInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'H';
		szRequestData[5] = 0x01;
		szRequestData[6] = (byte) heatInfo;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setAllHeating request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

	}

	@Override
	public ViceMachineStatus getAllHeating(int boardID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		ViceMachineStatus viceMachineStatus = new ViceMachineStatus();
		BoxStatus[] boxStatus = new BoxStatus[40];

		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'Q';
		szRequestData[5] = 0x00;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getAllHeating request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

		for (int i = 0; i < recvBuff[6]; i++) {
			for (int j = 0; j < 8; j++) {
				int uHeat = (byte) ((recvBuff[7 + i] >> j) & 0x01);
				boxStatus[j * (i + 1)]
						.setHeatStatus(uHeat == 1 ? MainMachineStatus.ONE_STATUS
								: MainMachineStatus.ZERO_STATUS);
			}
		}
		viceMachineStatus.setBoxStatusArray(boxStatus);

		return viceMachineStatus;
	}

	@Override
	public void setBoxHeating(int boardID, int boxID, int heatInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'R';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = (byte) heatInfo;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setBoxHeating request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

	}

	@Override
	public int getBoxHeating(int boardID, int boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'S';
		szRequestData[5] = 0x01;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getBoxHeating request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}
		return recvBuff[7];
	}

	@Override
	public void setRedLantern(int boardID, int boxID, int LanternInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {
		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'L';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = (byte) LanternInfo;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setRedLantern request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

	}

	@Override
	public int getRedLantern(int boardID, int boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'T';
		szRequestData[5] = 0x01;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getRedLantern request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

		return recvBuff[7];
	}

	@Override
	public void setBoxAllControl(int boardID, int boxID, BoxStatus boxStatus)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'K';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = (byte) ((boxStatus.getOpenStatus() == 1 ? 0x01
				: 0x00)
				| (boxStatus.getLedLanternStatus() == 1 ? 0x02 : 0x00)
				| (boxStatus.getRedLanternStatus() == 1 ? 0x04 : 0x00)
				| (boxStatus.getFanStatus() == 1 ? 0x08 : 0x00)
				| (boxStatus.getHeatStatus() == 1 ? 0x16 : 0x00) | (boxStatus
				.getDisinfectStatus() == 1 ? 0x32 : 0x00));
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setBoxAllControl request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

	}

	@Override
	public ViceMachineStatus getBoxAllControl(int boardID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		ViceMachineStatus viceMachineStatus=new ViceMachineStatus();
		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'D';
		szRequestData[5] = 0x01;
		szRequestData[6] = 0x01 | 0x02 | 0x04 | 0x08;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getBoxAllControl request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}
		
		return viceMachineStatus;
	}

	@Override
	public BoxStatus getOneBoxAllControl(int boardID, int boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		BoxStatus boxStatus = new BoxStatus();
		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'A';
		szRequestData[5] = 0x01;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getOneBoxAllControl request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

		return boxStatus;

	}

	@Override
	public void setLEDLantern(int boardID, int boxID, int LEDInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'E';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = (byte) LEDInfo;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setLEDLantern request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}
	}

	@Override
	public int getLEDLantern(int boardID, int boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'e';
		szRequestData[5] = 0x01;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getLEDLantern request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}

		return recvBuff[7];
	}

	@Override
	public void setDisinfect(int boardID, int boxID, int disinfectInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[10];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x0A;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'W';
		szRequestData[5] = 0x02;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = (byte) disinfectInfo;
		szRequestData[8] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[9] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setDisinfect request failed, the response code wrong and code is "
							+ recvBuff[4] + recvBuff[5] + ".");
		}
	}

	@Override
	public int getDisinfect(int boardID, int boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'w';
		szRequestData[5] = 0x01;
		szRequestData[6] = (byte) boxID;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4] || 0 != recvBuff[5]) {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getDisinfect request failed, the response code wrong and code is "
							+ recvBuff[4] +recvBuff[5]+ ".");
		}
		
		return recvBuff[7];
	}

	/**
	 * @throws ProtocolParsingException
	 * @throws ResponseTimeoutException
	 * @throws SerialErrorException
	 * @throws NotOpenSerialException
	 * 
	 * @Title: recv
	 * @Description: TODO(接收驱动板返回的指令)
	 * @param @param buff
	 * @param @param len
	 * @param @throws SystemException 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	protected int recv(byte[] buff, int len) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			ProtocolParsingException {

		byte count = 0;
		byte[] head = new byte[1];

		do {
			serialPort.recvFixLength(head, 1);

			if (head[0] == 0x02)
				break;

			if (++count == 100) {
				throw new ProtocolParsingException(
						SerialPortErrorCode.ERR_PROTOCOLPARSING,
						"Recv data failed, because not found '0x02' protocol head.");
			}

		} while (true);

		byte[] totalLen = new byte[1];
		serialPort.recvFixLength(totalLen, 1);

		if (totalLen[0] < 8 || totalLen[0] > len)
			throw new ProtocolParsingException(
					SerialPortErrorCode.ERR_PROTOCOLPARSING,
					"Recv data failed, because the data length is "
							+ totalLen[0] + ", must between 8 and " + len + ".");
		final byte bodyLen = (byte) (totalLen[0] - 2);

		byte[] szBody = new byte[bodyLen];
		serialPort.recvFixLength(szBody, bodyLen);

		buff[0] = head[0];
		buff[1] = totalLen[0];
		System.arraycopy(szBody, 0, buff, 2, bodyLen);

		if (buff[totalLen[0] - 1] != 0x03
				|| buff[totalLen[0] - 2] != getBCC(buff,
						(byte) (totalLen[0] - 2))) {
			throw new ProtocolParsingException(
					SerialPortErrorCode.ERR_PROTOCOLPARSING,
					"Recv data failed, the tail is not '0x03' or crc failed.");
		}

		return 0;

	}

}
