package com.hzdongcheng.drivers.peripheral.plc.model;

import com.hzdongcheng.drivers.common.BaseConfig;

import java.util.List;

/**
 * 货架配置
 * Created by Administrator on 2018/4/13.
 */

public class ShelfDeskConfig extends BaseConfig {
    private String slaveID ;              //副柜编号
    private int servoSpeed;               //伺服速度
    private String displayName;           //业务副柜编号
    private String assetsCode;            //资产编码
    private List<ShelfLayerConfig> layerList;//货架层列表

    public int getLayerCount(){
        return layerList==null?0:layerList.size();
    }

    public String getSlaveID() {
        return slaveID;
    }

    public void setSlaveID(String slaveID) {
        this.slaveID = slaveID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAssetsCode() {
        return assetsCode;
    }

    public void setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
    }

    public List<ShelfLayerConfig> getLayerList() {
        return layerList;
    }

    public void setLayerList(List<ShelfLayerConfig> layerList) {
        this.layerList = layerList;
    }

    public int getServoSpeed() {
        return servoSpeed;
    }

    public void setServoSpeed(int servoSpeed) {
        this.servoSpeed = servoSpeed;
    }
}
