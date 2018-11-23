/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  FreshControlImpl.java   
 * @Package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.fresh.impl
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年5月10日 下午3:18:44   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.fresh.impl;

import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.fresh.IFreshViceMachinControl;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;

/**
 * @ClassName: FreshControlImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jxing
 * @date 2017年5月10日 下午3:18:44
 * @version 1.0
 */
public class FreshViceMachinControlImpl extends AbstractViceMachineControl implements IFreshViceMachinControl {

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
	public FreshViceMachinControlImpl(SerialPortBase serialPort) {
		super(serialPort);
	}
}
