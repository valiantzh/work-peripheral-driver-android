package com.hzdongcheng.drivers.exception;

import com.hzdongcheng.drivers.exception.error.DriversErrorCode;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;


/**
 * 
 * @ClassName: DriversException 
 * @Description: 外设驱动异常基类
 * @author: Administrator
 * @date: 2018年1月30日 上午10:51:27
 */
public class DriversException extends DcdzSystemException{

	private static final long serialVersionUID = -2839696558273799862L;

	static {
		DriversErrorCode.loadResource();
	}
	
	public DriversException(int errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
		// TODO Auto-generated constructor stub
	}

	public DriversException(int errorCode, String message) {
		super(errorCode, message);
		// TODO Auto-generated constructor stub
	}

	public DriversException(int errorCode) {
		super(errorCode);
		// TODO Auto-generated constructor stub
	}
	
}
