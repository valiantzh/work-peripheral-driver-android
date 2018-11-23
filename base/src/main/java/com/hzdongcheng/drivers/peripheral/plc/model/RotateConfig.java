package com.hzdongcheng.drivers.peripheral.plc.model;


import com.hzdongcheng.drivers.common.DriverConfig;

import java.util.List;

/**
 * 旋转柜配置
 * Created by Administrator on 2018/4/13.
 */

public class RotateConfig extends DriverConfig {
    public static final String CONFIG_NAME = "Rotate";
    private String portName;
    private String rotateType; //旋转柜类型
    public static final String ROTATE_VENDOR_DCDZDM3CC = "Dcdz_DM3CC";
    private List<ShelfDeskConfig> deskList;

    public int getDeskCount(){
        return deskList==null?0:deskList.size();
    }

    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public String getRotateType() {
        return rotateType;
    }

    public void setRotateType(String rotateType) {
        this.rotateType = rotateType;
    }

    public List<ShelfDeskConfig> getDeskList() {
        return deskList;
    }

    public void setDeskList(List<ShelfDeskConfig> deskList) {
        this.deskList = deskList;
    }
}
