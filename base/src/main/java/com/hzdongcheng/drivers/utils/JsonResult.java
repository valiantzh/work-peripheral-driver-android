package com.hzdongcheng.drivers.utils;

public class JsonResult {
	private int errorCode; //错误代码
    private String errorMessage; //错误描述
    private String data;       //数据
    public JsonResult(){
    	
    }
	public  JsonResult(int errorCode,String errorDesc,String data) {
        this.errorCode = errorCode;
        this.errorMessage = errorDesc;
        this.data = data;
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
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
}
