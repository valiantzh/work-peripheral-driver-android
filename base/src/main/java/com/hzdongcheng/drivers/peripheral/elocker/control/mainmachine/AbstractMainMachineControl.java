/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  AbstractMainMachineControl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.mainmachine   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:33:13   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine;

import com.hzdongcheng.drivers.peripheral.elocker.control.AbstractELockerControlBase;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;

/** 
* @ClassName: AbstractMainMachineControl 
* @Description: TODO(主机控制抽象类) 
* @author Jxing 
* @date 2017年4月12日 下午3:33:13 
* @version 1.0 
*/
public abstract class AbstractMainMachineControl extends AbstractELockerControlBase implements IMainMachineControl {

	/** 
	* <p>Title: </p> 
	* <p>Description: </p> 
	* @param serialPort 
	*/
	protected AbstractMainMachineControl(SerialPortBase serialPort) {
		super(serialPort);
	}
}
