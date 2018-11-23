package com.hzdongcheng.drivers.base.serialport.exception;

import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;

/**
 * 
* @ClassName: SerialPortException 
* @Description: TODO(串口异常基类) 
* @author Jxing 
* @date 2016年12月23日 下午1:00:19 
* @version 1.0
 */
public class SerialPortException extends DcdzSystemException{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = -2839696558273799862L;

	static {
		SerialPortErrorCode.loadResource();
	}
	
	public SerialPortException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public SerialPortException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public SerialPortException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	
}
