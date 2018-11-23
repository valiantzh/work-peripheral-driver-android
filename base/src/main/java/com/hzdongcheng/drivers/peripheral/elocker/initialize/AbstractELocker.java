/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  AbstractELockerControl.java   
 * @Package com.hzdongcheng.components.driver.elocker.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:24:16   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.initialize;

import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;

/** 
* @ClassName: AbstractELocker
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jxing 
* @date 2017年4月12日 下午3:24:16
* @version 1.0 
*/
public abstract class AbstractELocker implements IELocker{

	protected final SerialPortBase serialPort = new SerialPortBase();
	
	public AbstractELocker(){
		ErrorCode.loadResource();
	}
	
	/**
	 * @return the serialPort
	 */
	public SerialPortBase getSerialPort() {
		return serialPort;
	}
}
