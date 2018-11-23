/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  InitializeSerialPortException.java   
 * @Package com.hzdongcheng.drivers.base.serialport.exception
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年5月8日 下午5:32:24   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.base.serialport.exception;

/** 
* @ClassName: InitializeSerialPortException 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jxing 
* @date 2017年5月8日 下午5:32:24 
* @version 1.0 
*/
public class InitializeSerialPortException extends SerialPortException {

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 442476029579658839L;

	public InitializeSerialPortException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public InitializeSerialPortException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public InitializeSerialPortException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
