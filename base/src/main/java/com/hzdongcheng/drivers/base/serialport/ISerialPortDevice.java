/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ISerialDevice.java   
 * @Package com.hzdongcheng.components.serialport   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午2:13:10   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.base.serialport;

import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;

/** 
* @ClassName: ISerialPortDevice 
* @Description: TODO(串口设备基类) 
* @author Jxing 
* @date 2017年4月12日 下午2:13:10 
* @version 1.0 
*/
public interface ISerialPortDevice {
	//打开串口设备
	void open(String portName) throws NotFoundSerialLibraryException, 
									  InitializeSerialPortException, 
			                          SerialPortErrorException;
	//关闭串口设备
	void close();
	
}
