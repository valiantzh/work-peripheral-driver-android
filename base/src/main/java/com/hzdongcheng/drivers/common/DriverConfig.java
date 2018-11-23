package com.hzdongcheng.drivers.common;

/**
 * 
 * @ClassName: DriverConfig 
 * @Description: 驱动配置基类
 * @author: Administrator
 * @date: 2018年2月8日 下午2:21:07
 */
public class DriverConfig extends BaseConfig{
	private String portName = "";         //串口名称
    private String vendorName = "";      //供应商名称
    private boolean isEnabled = false; //是否启用
    private String desc = "";
	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}
	/**
	 * @param portName the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}
	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}
	/**
	 * @param vendorName the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	/**
	 * @return the isEnabled
	 */
	public boolean isEnabled() {
		return isEnabled;
	}
	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
    
}
