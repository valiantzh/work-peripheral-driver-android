/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  IElectricmeter.java   
 * @Package com.hzdongcheng.components.driver.electricmeter   
 * @Description:    TODO(电表接口声明文件)   
 * @author: Jxing     
 * @date:   2017年10月19日 上午9:15:35   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.electricmeter;

import com.hzdongcheng.drivers.base.serialport.ISerialPortDevice;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * 
 * @ClassName: IElectricmeter 
 * @Description: 电表控制接口
 * @author: Administrator
 * @date: 2018年2月11日 上午11:19:00
 */
public interface IElectricmeter extends ISerialPortDevice {
	/**
	 * 
	* @Method Name: readTheMeter 
	* @Description: TODO(抄表)
	* @param  @return
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws RecvTimeoutException
	* @param  @throws SendTimeoutException
	* @param  @throws ProtocolParsingException
	* @param  @throws ResponseCodeException
	* @return String
	 */
	String readTheMeter() throws NotOpenSerialException, 
								 SerialPortErrorException, 
								 RecvTimeoutException,
								 SendTimeoutException,
								 ProtocolParsingException,
								 ResponseCodeException;
}
