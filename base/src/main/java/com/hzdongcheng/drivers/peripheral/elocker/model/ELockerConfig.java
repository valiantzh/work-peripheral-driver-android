package com.hzdongcheng.drivers.peripheral.elocker.model;

import java.util.List;

import com.hzdongcheng.drivers.common.BaseConfig;

/**
 * 
 * @ClassName: ElockerConfig 
 * @Description: 自提柜配置类
 * @author: Administrator
 * @date: 2018年2月28日 下午2:59:02
 */
public class ELockerConfig extends BaseConfig{
    public static final String CONFIG_NAME = "ELocker";
	private String portName;
    private String elockerType;//组合方式
    private List<DeskConfig> deskList;

    public ELockerConfig() {
    }

    public  int getDeskCount()
    {
        if (deskList == null)
            return 0;
        return deskList.size();
    }
    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
    }

    public List<DeskConfig> getDeskList() {
        return deskList;
    }

    public void setDeskList(List<DeskConfig> deskList) {
        this.deskList = deskList;
    }

    public String getElockerType() {
        return elockerType;
    }

    public void setElockerType(String elockerType) {
        this.elockerType = elockerType;
    }
}
