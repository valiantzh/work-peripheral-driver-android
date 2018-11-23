/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  AbstractELockerControl.java   
 * @Package com.dcdzsoft.drivers.peripheral.elocker.impl
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:24:16   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.thermostat.initialize;

import com.hzdongcheng.drivers.base.serialport.SerialPortBase;

/** 
* @ClassName: AbstractELockerControl 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jxing 
* @date 2017年4月12日 下午3:24:16
* @version 1.0 
*/
public abstract class AbstractTherELocker implements ITherELocker{

	protected final SerialPortBase serialPort = new SerialPortBase();

	/**
	 * @return the serialPort
	 */
	public SerialPortBase getSerialPort() {
		return serialPort;
	}
}
