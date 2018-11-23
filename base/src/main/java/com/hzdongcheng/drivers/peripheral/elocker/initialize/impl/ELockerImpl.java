/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ELocker.java   
 * @Package com.hzdongcheng.components.driver.elocker.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:01:49   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.initialize.impl;

import com.hzdongcheng.drivers.peripheral.elocker.initialize.AbstractELocker;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;

/** 
* @ClassName: ELocker 
* @Description: TODO(Elocker端口开关类) 
* @author Jxing 
* @date 2017年4月12日 下午3:01:49 
* @version 1.0 
*/
public final class ELockerImpl extends AbstractELocker{

	/* (non Javadoc) 
	* <p>Title: open</p> 
	* <p>Description: </p> 
	* @param portName
	* @throws NotFoundSerialLibraryException
	* @throws SerialPortException 
	* @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#open(java.lang.String)
	*/
	@Override
	public void open(String portName) throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
		//打开串口
		serialPort.open(portName);
		
		try{
			//初始化串口
			serialPort.initialize(SerialPortBase.DCDZ_CBR_115200, SerialPortBase.DCDZ_NOPARITY, 
					SerialPortBase.DCDZ_ONESTOPBIT, SerialPortBase.DCDZ_DATABITS8);
			//设置读超时2秒,写超时1秒
			serialPort.setReadWriteTimeouts(2000, 1000);
			//清楚串口缓冲区数据
			serialPort.clear();
		} catch(SerialPortException e){
			throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
		}
	}

	/* (non Javadoc) 
	* <p>Title: close</p> 
	* <p>Description: </p>  
	* @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#close()
	*/
	@Override
	public void close() {
		serialPort.close();
	}
}
