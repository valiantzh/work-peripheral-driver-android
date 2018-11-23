package com.hzdongcheng.drivers.base.serialport.exception;

/**
 * 
* @ClassName: SendTimeoutException 
* @Description: TODO(串口数据发送超时异常类) 
* @author Jxing 
* @date 2016年12月23日 下午12:59:52 
* @version 1.0
 */
public class SendTimeoutException extends SerialPortException{

	/**
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	 */
	private static final long serialVersionUID = 1703600512313231866L;

	public SendTimeoutException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public SendTimeoutException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public SendTimeoutException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}

}
