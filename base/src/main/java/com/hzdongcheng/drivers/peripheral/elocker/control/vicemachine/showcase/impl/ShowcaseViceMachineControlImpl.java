/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ShowcaseViceMachineControlImpl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.vicemachine.showcase.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:12:32   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.showcase.impl;

import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.showcase.IShowcaseViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;

/** 
* @ClassName: ShowcaseViceMachineControlImpl 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jxing 
* @date 2017年4月12日 下午3:12:32 
* @version 1.0 
*/
public class ShowcaseViceMachineControlImpl extends AbstractViceMachineControl implements IShowcaseViceMachineControl {

	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param serialPort 
	*/
	public ShowcaseViceMachineControlImpl(SerialPortBase serialPort) {
		super(serialPort);
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
