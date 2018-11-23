package com.hzdongcheng.drivers.peripheral.elocker.model;

import com.hzdongcheng.drivers.common.BaseConfig;

/**
 * 
 * @ClassName: BoxConfig 
 * @Description: 格口配置
 * @author: Administrator
 * @date: 2018年2月28日 下午2:53:16
 */
public class BoxConfig  extends BaseConfig {
	private String boxID; //逻辑箱号
    private String displayName ; //业务箱号

    public String getBoxID() {
        return boxID;
    }

    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
