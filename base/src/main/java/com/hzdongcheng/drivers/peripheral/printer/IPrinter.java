/**   
 * Copyright © 2018.
 * 
 * @Package: com.dcdzsoft.drivers.printer 
 * @author: Administrator   
 * @date: 2018年1月29日 上午8:55:52 
 */
package com.hzdongcheng.drivers.peripheral.printer;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterStatus;
import com.hzdongcheng.drivers.base.serialport.ISerialPortDevice;

/** 
 * @ClassName: IPrinter 
 * @Description: 打印设备接口类
 * @author: Administrator
 * @date: 2018年1月29日 上午8:55:52  
 */
public interface IPrinter extends ISerialPortDevice {
	/**
	 * 
	* @Title: print  
	* @Description: 打印  
	* @param @param data
	* @return void 
	* @throws DriversException
	 */
	void print(String data) throws DriversException;
	
	/**
	 * 
	* @Title: cutPaper  
	* @Description: 切纸
	* @param @param mode 0-半切 1-全切
	* @return void 
	* @throws DriversException
	 */
	void cutPaper(int mode) throws DriversException;
	
	/**
	 * 
	* @Title: onBuzzer  
	* @Description: 控制蜂鸣器发声  
	* @return void 
	* @throws DriversException
	 */
	void onBuzzer()throws DriversException;
	
	/**
	 * 
	* @Title: getStatus  
	* @Description: 获取打印机状态  
	* @param @return
	* @return PrinterStatus 
	* @throws DriversException
	 */
	PrinterStatus getStatus() throws DriversException;

	void reset() throws DriversException;
	
}
