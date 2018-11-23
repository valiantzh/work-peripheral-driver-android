package com.hzdongcheng.drivers.base.serialport.exception;

/**
 * 
* @ClassName: ResponseTimeoutException 
* @Description: TODO(应答超时异常类) 
* @author Jxing 
* @date 2016年12月23日 下午12:59:42 
* @version 1.0
 */
public class RecvTimeoutException extends SerialPortException{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 46789295955664613L;

	public RecvTimeoutException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public RecvTimeoutException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public RecvTimeoutException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
