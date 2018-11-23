package com.hzdongcheng.drivers.peripheral.plc.model;

import com.hzdongcheng.drivers.common.BaseConfig;

import java.util.List;

/**
 * 货架层配置
 * Created by Administrator on 2018/4/13.
 */

public class ShelfLayerConfig extends BaseConfig {
    private String layerID ;              //货架层编号
    private String displayName;           //业务层编号
    private String layerType;             //0-小货格  2-大货格  99-外设层
    private List<ShelfBoxConfig> boxList; //每层的货格列表

    public int getBoxCount(){
        return boxList==null?0:boxList.size();
    }
    public String getLayerID() {
        return layerID;
    }

    public void setLayerID(String layerID) {
        this.layerID = layerID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public List<ShelfBoxConfig> getBoxList() {
        return boxList;
    }

    public void setBoxList(List<ShelfBoxConfig> boxList) {
        this.boxList = boxList;
    }
}
