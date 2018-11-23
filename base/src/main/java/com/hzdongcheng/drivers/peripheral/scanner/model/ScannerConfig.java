package com.hzdongcheng.drivers.peripheral.scanner.model;

import com.hzdongcheng.drivers.common.DriverConfig;

/**
 * 
 * @ClassName: ScannerConfig 
 * @Description: 扫描设备配置类
 * @author: Administrator
 * @date: 2018年2月8日 下午2:08:58
 */
public class ScannerConfig extends DriverConfig{
	public static final String CONFIG_NAME = "Scanner";
	//扫描枪厂家
	public static final String SCANNER_VENDOR_NEWLAND = "Newland";
    public static final String SCANNER_VENDOR_HONEYWELL = "Honeywell";
    public static final String SCANNER_VENDOR_DCSR = "Dcsr";
    public static final String SCANNER_VENDOR_DATALOGIC = "Datalogic";
    public static final String SCANNER_VENDOR_DEWO = "Dewo";
    public static final String SCANNER_VENDOR_MOTO = "MOTO";

    private boolean isNormallyOn; //是否常亮模式

	/**
	 * @return the isNormallyOn
	 */
	public boolean isNormallyOn() {
		return isNormallyOn;
	}

	/**
	 * @param isNormallyOn the isNormallyOn to set
	 */
	public void setNormallyOn(boolean isNormallyOn) {
		this.isNormallyOn = isNormallyOn;
	}

}
