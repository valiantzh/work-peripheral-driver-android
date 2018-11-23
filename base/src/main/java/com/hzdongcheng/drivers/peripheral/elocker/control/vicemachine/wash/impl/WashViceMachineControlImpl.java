/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  WashViceMachineControlImpl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.vicemachine.wash.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:12:11   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.wash.impl;

import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.wash.IWashViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;

/** 
* @ClassName: WashViceMachineControlImpl 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jxing 
* @date 2017年4月12日 下午3:12:11 
* @version 1.0 
*/
public class WashViceMachineControlImpl extends AbstractViceMachineControl implements IWashViceMachineControl {

	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param serialPort 
	*/
	public WashViceMachineControlImpl(SerialPortBase serialPort) {
		super(serialPort);
		// TODO Auto-generated constructor stub
	}

	/* (non Javadoc) 
	* <p>Title: openBox</p> 
	* <p>Description: </p> 
	* @param boardID
	* @param boxID 
	* @see com.dcdzsoft.drivers.controller.elocker.IViceMachineControl#openBox(byte, byte) 
	*/
	@Override
	public BoxStatus openBox(byte boardID, byte boxID) throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException {
		return null;
	}

	/* (non Javadoc) 
	* <p>Title: queryStatus</p> 
	* <p>Description: </p> 
	* @param boardID 
	* @see com.dcdzsoft.drivers.controller.elocker.IViceMachineControl#queryStatus(byte) 
	*/
	@Override
	public ViceMachineStatus queryStatus(byte boardID) {
		return null;
	}

	/* (non Javadoc) 
	* <p>Title: queryStatus</p> 
	* <p>Description: </p> 
	* @param boardID
	* @param boxID 
	* @see com.dcdzsoft.drivers.controller.elocker.IViceMachineControl#queryStatus(byte, byte) 
	*/
	@Override
	public BoxStatus queryStatus(byte boardID, byte boxID) {
		return null;
	}

}
