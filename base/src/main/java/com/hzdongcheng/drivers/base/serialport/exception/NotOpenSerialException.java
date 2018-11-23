package com.hzdongcheng.drivers.base.serialport.exception;

/**
 * 
* @ClassName: NotOpenSerialException 
* @Description: TODO(串口打开失败异常类) 
* @author Jxing 
* @date 2016年12月23日 下午12:58:44 
* @version 1.0
 */
public class NotOpenSerialException extends SerialPortException{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = -5893196105093651029L;

	public NotOpenSerialException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotOpenSerialException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public NotOpenSerialException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
