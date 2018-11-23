/**   
 * Copyright © 2018.
 * 
 * @Package: com.dcdzsoft.drivers.printer.model 
 * @author: Administrator   
 * @date: 2018年2月6日 下午1:41:14 
 */
package com.hzdongcheng.drivers.peripheral.printer.model;

import com.hzdongcheng.drivers.common.Constants;

/** 
 * @ClassName: PrinterStatus 
 * @Description: 打印机状态
 * @author: Administrator
 * @date: 2018年2月6日 下午1:41:14  
 */
public class PrinterStatus {
	public PrinterStatus(){
		this.setCutterStatus(Constants.UNKNOWN_STATUS);
		this.setPater1Status(Constants.UNKNOWN_STATUS);
		this.setPater2Status(Constants.UNKNOWN_STATUS);
		this.setPater3Status(Constants.UNKNOWN_STATUS);
	}
	/**
	 * 切刀锁状态 1-锁打开 0-锁关闭
	 */
	private byte cutterStatus;
	
	/**
	 * 纸将尽状态：1-纸将尽 0-纸充足
	 */
	private byte pater1Status;
	
	/**
	 * 纸未取状态： 1-未取 0-已取
	 */
	private byte pater2Status;
	/**
	 * 缺纸状态 1-缺纸 0-有纸
	 */
	private byte pater3Status;
	/**
	 * 切刀锁状态 1-锁打开 0-锁关闭
	 * @return the cutterStatus
	 */
	public byte getCutterStatus() {
		return cutterStatus;
	}
	/**
	 * @param cutterStatus the cutterStatus to set
	 */
	public void setCutterStatus(byte cutterStatus) {
		this.cutterStatus = cutterStatus;
	}
	/**
	 * 纸将尽状态：1-纸将尽 0-纸充足
	 * @return the pater1Status
	 */
	public byte getPater1Status() {
		return pater1Status;
	}
	/**
	 * @param pater1Status the pater1Status to set
	 */
	public void setPater1Status(byte pater1Status) {
		this.pater1Status = pater1Status;
	}
	/**
	 * 纸未取状态： 1-未取 0-已取
	 * @return the pater2Status
	 */
	public byte getPater2Status() {
		return pater2Status;
	}
	/**
	 * 纸未取状态： 1-未取 0-已取
	 * @param pater2Status the pater2Status to set
	 */
	public void setPater2Status(byte pater2Status) {
		this.pater2Status = pater2Status;
	}
	/**
	 * 缺纸状态 1-缺纸 0-有纸
	 * @return the pater3Status
	 */
	public byte getPater3Status() {
		return pater3Status;
	}
	/**
	 * 缺纸状态 1-缺纸 0-有纸
	 * @param pater3Status the pater3Status to set
	 */
	public void setPater3Status(byte pater3Status) {
		this.pater3Status = pater3Status;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PrinterStatus [cutterStatus=" + cutterStatus + ", pater1Status=" + pater1Status + ", pater2Status="
				+ pater2Status + ", pater3Status=" + pater3Status + "]";
	}
	
	
}
