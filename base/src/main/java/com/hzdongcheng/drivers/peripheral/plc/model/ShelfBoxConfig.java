package com.hzdongcheng.drivers.peripheral.plc.model;

import com.hzdongcheng.drivers.common.BaseConfig;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ShelfBoxConfig extends BaseConfig {
    private String boxID ;              //逻辑编号（层内货格编号）
    private String displayName;         //业务货格编号

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
