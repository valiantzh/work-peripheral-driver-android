package com.hzdongcheng.drivers.base.serialport.exception;

/**
 * 
* @ClassName: SerialErrorException 
* @Description: TODO(串口错误异常类) 
* @author Jxing 
* @date 2016年12月23日 下午1:00:09 
* @version 1.0
 */
public class SerialPortErrorException extends SerialPortException{

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
	 */
	private static final long serialVersionUID = 1685175063206365873L;

	public SerialPortErrorException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public SerialPortErrorException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public SerialPortErrorException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
