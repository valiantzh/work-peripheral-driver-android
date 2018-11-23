package com.hzdongcheng.drivers.peripheral.scanner.event;

import java.util.EventObject;

/**
 * 
 * @ClassName: ScannerEvent 
 * @Description: 扫描枪事件类
 * @author Administrator 
 * @date 2018年1月26日 下午2:32:09 
 */
public class ScannerEvent extends EventObject{
	
	/** 
	* @Fields serialVersionUID : TODO() 
	*/
	private static final long serialVersionUID = 4087621446647410355L;
	
	private int errorCode = 0;
	private String dataString = null;
	
	public ScannerEvent(Object source, int errorCode, String dataString) {
		super(source);
		this.errorCode = errorCode;
		this.dataString = dataString;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the dataString
	 */
	public String getDataString() {
		return dataString;
	}

	/**
	 * @param dataString the dataString to set
	 */
	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

}
