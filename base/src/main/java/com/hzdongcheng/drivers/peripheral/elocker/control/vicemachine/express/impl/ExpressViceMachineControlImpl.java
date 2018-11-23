/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ExpressViceMachineControlImpl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.vicemachine.express.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:11:13   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.express.impl;

import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.express.IExpressViceMachineControl;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;

/**
 * @ClassName: ExpressViceMachineControlImpl
 * @Description: TODO(普通快递副柜控制实现类)
 * @author Jxing
 * @date 2017年4月12日 下午3:11:13
 * @version 1.0
 */
public class ExpressViceMachineControlImpl extends AbstractViceMachineControl
		implements IExpressViceMachineControl {

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param serialPort
	 */
	public ExpressViceMachineControlImpl(SerialPortBase serialPort) {
		super(serialPort);
	}
}