/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  SaleViceMachineControlImpl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.vicemachine.sale.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:11:52   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.sale.impl;

import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.sale.ISaleViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
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
 * @ClassName: SaleViceMachineControlImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jxing
 * @date 2017年4月12日 下午3:11:52
 * @version 1.0
 */
public class SaleViceMachineControlImpl extends AbstractViceMachineControl
		implements ISaleViceMachineControl {

	public SaleViceMachineControlImpl(SerialPortBase serialPort) {
		super(serialPort);
	}
	
	/**
	 * @throws TerminalException 
	 * @throws SendTimeoutException 
	 * @throws SerialErrorException 
	 * @throws NotOpenSerialException 
	 * 
	* @Title: saleGoods 
	* @Description: TODO(售卖机出货  deskID：分机号  locationID：货品位置) 
	* @param @return 设定文件 
	* @return int 返回类型  0:成功  1：失败 2：超出 3：应答
	* @throws
	 */
	@Override
	public BoxStatus openBox(byte boardID, byte boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		BoxStatus boxStatus=new BoxStatus();
		
		byte szRequestData[] = new byte[6];
		szRequestData[0] = 0x28;
		szRequestData[1] = 0x11;
		szRequestData[2] = 0x01;
		szRequestData[3] = (byte) (0x30 + boardID);
		szRequestData[4] = (byte) (0x30 + boxID);
		szRequestData[5] = 0x29;

		serialPort.sendFixLength(szRequestData, szRequestData.length);

		// 应答指令： 0x28 0x11 0x30+x2 0x4F 0x4B 0x29
		// 超出指令： 0x28 0x11 0x30+x2 0x4E 0x4F 0x29
		// 成功指令： 0x28 0x11 0x06 0x30+x2 0x31 0x29
		// 失败指令： 0x28 0x11 0x06 0x30+x2 0x30 0x29
		byte[] recvBuff = new byte[6];
		serialPort.recvFixLength(recvBuff, recvBuff.length);

		if (recvBuff[2] != (byte) (0x30 + boardID))
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"saleGoods1 request failed, the response code wrong and code is "
							+ recvBuff[2] + ".");
		if (recvBuff[3] == 0x4E && recvBuff[4] == 0x4F)
			boxStatus.setSaleReturnStatus(2);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		recvBuff = new byte[6];
		serialPort.recvFixLength(recvBuff, recvBuff.length);
		if (recvBuff[3] != (byte) (0x30 + boardID))
			throw new ResponseCodeException(
					SerialPortErrorCode.ERR_RESPONSECODE,
					"saleGoods2 request failed, the response code wrong and code is "
							+ recvBuff[3] + ".");
		if (recvBuff[4] == 0x31)
			boxStatus.setSaleReturnStatus(0);
		else if (recvBuff[4] == 0x30)
			boxStatus.setSaleReturnStatus(1);
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
	

}
