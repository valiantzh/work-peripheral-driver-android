package com.hzdongcheng.drivers.peripheral.elocker.model;

import com.hzdongcheng.drivers.common.BaseConfig;

import java.util.List;

/**
 * 
 * @ClassName: DeskConfig 
 * @Description: 副柜配置
 * @author: Administrator
 * @date: 2018年2月28日 下午2:55:39
 */
public class DeskConfig extends BaseConfig {
	private String boardID;               //驱动板号
    private String displayName;          //业务板号
    private String assetsCode;            //资产编码
    private List<BoxConfig> boxList; //箱模型列表

    public List<BoxConfig> getBoxList() {
        return boxList;
    }
    public int getBoxCount()
    {
        if (boxList==null)
            return 0;
        return  boxList.size();
    }

    public void setBoxList(List<BoxConfig> boxList) {
        this.boxList = boxList;
    }

    public String getBoardID() {
        return boardID;
    }

    public void setBoardID(String boardID) {
        this.boardID = boardID;
    }

    public String getAssetsCode() {
        return assetsCode;
    }

    public void setAssetsCode(String assetsCode) {
        this.assetsCode = assetsCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
