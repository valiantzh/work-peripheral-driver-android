package com.hzdongcheng.drivers.base.serialport.exception;

/**
 * 
* @ClassName: NotFoundSerialLibraryException 
* @Description: TODO(无法找到串口库异常类) 
* @author Jxing 
* @date 2016年12月23日 下午12:58:14 
* @version 1.0
 */
public class NotFoundSerialLibraryException extends SerialPortException{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = 4996130248151405267L;

	public NotFoundSerialLibraryException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public NotFoundSerialLibraryException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public NotFoundSerialLibraryException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
