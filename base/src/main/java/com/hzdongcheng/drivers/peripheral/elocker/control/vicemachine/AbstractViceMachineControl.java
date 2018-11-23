/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  AbstractGeneralViceMachineControl.java   
 * @Package com.hzdongcheng.components.driver.elocker.vicemachine   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午2:51:13   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine;

import com.hzdongcheng.drivers.common.Constants;
import com.hzdongcheng.drivers.peripheral.elocker.control.AbstractELockerControlBase;
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
* @ClassName: AbstractGeneralViceMachineControl 
* @Description: TODO(副机控制抽象类,提供默认实现) 
* @author Jxing 
* @date 2017年4月12日 下午2:51:13 
* @version 1.0 
*/
public abstract class AbstractViceMachineControl extends AbstractELockerControlBase implements IViceMachineControl {

	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param serialPort 
	*/
	protected AbstractViceMachineControl(SerialPortBase serialPort) {
		super(serialPort);
	}

	/* (non Javadoc) 
	* <p>Title: getDeviceCode</p> 
	* <p>Description: </p> 
	* @param boardID
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.elocker.control.vicemachine.IViceMachineControl#getDeviceCode(byte) 
	*/
	@Override
	public String getDeviceCode(byte boardID) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		byte[] szRequestData = new byte[8];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x08;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'G';
		szRequestData[5] = 0;
		szRequestData[6] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[7] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		if (0 != recvBuff[4] || 30 != recvBuff[5])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"getDeviceCode request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");
		if (-1 == recvBuff[6]) {
			return "";
		}

		byte[] DeviceCodeData = new byte[30];
		System.arraycopy(recvBuff, 6, DeviceCodeData, 0, 30);

		return new String(DeviceCodeData).trim();
	}

	/* (non Javadoc) 
	* <p>Title: setDeviceCode</p> 
	* <p>Description: </p> 
	* @param boardID
	* @param code
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.elocker.control.vicemachine.IViceMachineControl#setDeviceCode(byte, java.lang.String) 
	*/
	@Override
	public void setDeviceCode(byte boardID, String code) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {

		byte[] szRequestData = new byte[38];
		szRequestData[0] = 0x02;
		szRequestData[1] = 38;
		szRequestData[2] = (byte) boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'S';
		szRequestData[5] = 30;

		byte[] DeviceCodeData = code.trim().getBytes();
		System.arraycopy(DeviceCodeData, 0, szRequestData, 6,
				DeviceCodeData.length);

		szRequestData[36] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[37] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"setDeviceCode request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");
		    
	}

	/* (non Javadoc) 
	* <p>Title: openBox</p> 
	* <p>Description: </p> 
	* @param boardID
	* @param boxID
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.elocker.control.vicemachine.IViceMachineControl#openBox(byte, byte) 
	*/
	@Override
	public BoxStatus openBox(byte boardID, byte boxID) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		BoxStatus boxStatus = new BoxStatus();

		// 开箱命令
		byte[] szRequestData = new byte[9];
		szRequestData[0] = 0x02;
		szRequestData[1] = 0x09;
		szRequestData[2] = boardID;
		szRequestData[3] = getFrameNOAndIncrement();
		szRequestData[4] = 'O';
		szRequestData[5] = 1;
		szRequestData[6] = boxID;
		szRequestData[7] = getBCC(szRequestData, (byte) (szRequestData[1] - 2));
		szRequestData[8] = 0x03;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);

		if (0 == recvBuff[4]) {
			boxStatus.setGoodsStatus((byte) (recvBuff[6] > 0 ? 1 : 0));
			boxStatus.setOpenStatus((byte) 9);
		} else {
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"Open box request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");
		}

		return boxStatus;
	}


	/* (non Javadoc) 
	* <p>Title: queryStatus</p> 
	* <p>Description: </p> 
	* @param boardID
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.elocker.control.vicemachine.IViceMachineControl#queryStatus(byte) 
	*/
	@Override
	public ViceMachineStatus queryStatus(byte boardID) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {

		ViceMachineStatus viceMachineStatus = new ViceMachineStatus();
		BoxStatus[] boxStatus = new BoxStatus[40];
		
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

		if (0 != recvBuff[4])
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"queryStatus request failed, the response code wrong and code is "
							+ recvBuff[4] + ".");

		if (recvBuff[5] < 6 || recvBuff[5] % 2 != 0 || recvBuff[5] > 14)
			throw new ProtocolParsingException(
					SerialPortErrorCode.ERR_RESPONSECODE);

		// 最大箱门数量
		byte totalBoxNums = (byte) ((recvBuff[5] - 4) / 2 * 8);
		viceMachineStatus.setBoxNums(totalBoxNums);
		int[] szByte = new int[] { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40,
				0x80 };

		// 物品状态
		for (byte n = 0, j = 0, k = 0; n < totalBoxNums; n++) {
			byte uArticle = (byte) ((recvBuff[6 + k] & szByte[j]) >> j);
			j++;
			boxStatus[n] = new BoxStatus();
			boxStatus[n].setGoodsStatus(uArticle == 0 ? (byte) MainMachineStatus.ZERO_STATUS
							: (byte) MainMachineStatus.ONE_STATUS);
			if (j == 8) {
				k++;
				j = 0;
			}
		}
		// 箱门开关状态
		for (byte n = 0, j = 0, k = 0; n < totalBoxNums; n++) {
			byte uOpen = (byte) ((recvBuff[6 + totalBoxNums / 8 + k] & szByte[j]) >> j);
			j++;
			boxStatus[n].setOpenStatus(uOpen == 1 ? (byte) MainMachineStatus.ZERO_STATUS
					: (byte) MainMachineStatus.ONE_STATUS);
			if (j == 8) {
				k++;
				j = 0;
			}
		}
		{
			// 防撬
			viceMachineStatus
					.setTamperStatus(recvBuff[6 + (totalBoxNums / 8) * 2]);
			// 震动
			viceMachineStatus
					.setShockStatus(recvBuff[6 + (totalBoxNums / 8) * 2 + 1]);
		}
		viceMachineStatus.setBoxStatusArray(boxStatus);
		return viceMachineStatus;
	}


	/* (non Javadoc) 
	* <p>Title: queryStatus</p> 
	* <p>Description: </p> 
	* @param boardID
	* @param boxID
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.elocker.control.vicemachine.IViceMachineControl#queryStatus(byte, byte) 
	*/
	@Override
	public BoxStatus queryStatus(byte boardID, byte boxID) throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		BoxStatus boxStatus = new BoxStatus();
		ViceMachineStatus viceMachineStatus = queryStatus(boardID);
		
		if (Math.abs(boxID) == 0 || Math.abs(boxID) > 40){
			boxID = 1;
		}
		
		boxStatus.setGoodsStatus(viceMachineStatus.getBoxStatusArray(boxID-1)
				.getGoodsStatus());
		boxStatus.setOpenStatus(viceMachineStatus.getBoxStatusArray(boxID-1)
				.getOpenStatus());

		return boxStatus;
		
	}
}
