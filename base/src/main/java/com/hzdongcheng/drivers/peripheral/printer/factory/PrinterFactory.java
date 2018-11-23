/**   
 * Copyright © 2018.
 * 
 * @Package: com.dcdzsoft.drivers.printer.factory 
 * @author: Administrator   
 * @date: 2018年2月6日 下午3:17:58 
 */
package com.hzdongcheng.drivers.peripheral.printer.factory;

import com.hzdongcheng.drivers.peripheral.printer.IPrinter;
import com.hzdongcheng.drivers.peripheral.printer.impl.WeiHuangPrinter;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterConfig;

/** 
 * @ClassName: PrinterFactory 
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年2月6日 下午3:17:58  
 */
public class PrinterFactory {
	/**
	 * 
	* @Method Name: createInstance 
	* @Description: TODO(通过反射创建打印机实例) 
	* @param  @param classSimpleName
	* @param  @return
	* @return IPrinter
	* @deprecated
	 */
	public static IPrinter createInstance(String classSimpleName){
		
		String className = "com.hzdongcheng.drivers.peripheral.printer.impl." + classSimpleName;

		IPrinter printer = null;
		try {
			printer = (IPrinter)Class.forName(className).newInstance();

		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return printer;
	}
	public static IPrinter createInstance(PrinterConfig config){
		IPrinter printer = null;
		switch(config.getVendorName()){
		case PrinterConfig.PRINTER_VENDOR_WEIHUANG:
			printer = new WeiHuangPrinter();
			break;
		}
		return printer;
	}
}
