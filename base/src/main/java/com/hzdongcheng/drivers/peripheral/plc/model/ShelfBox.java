package com.hzdongcheng.drivers.peripheral.plc.model;

/**
 * 货格信息
 * Created by Administrator on 2018/4/13.
 */

public class ShelfBox {
    private String boxNo;
    private byte ofDeskNo;
    private byte ofLayerNo;
    private byte ofColNo;
    private String boxType;

    private ShelfBoxStatus status;

    public ShelfBox(){
        status = new ShelfBoxStatus();
    }

    public String getBoxNo() {
        return boxNo;
    }

    public byte getOfDeskNo() {
        return ofDeskNo;
    }

    public byte getOfLayerNo() {
        return ofLayerNo;
    }

    public byte getOfColNo() {
        return ofColNo;
    }

    public ShelfBoxStatus getStatus() {
        return status;
    }

    public void setBoxNo(String boxNo) {
        this.boxNo = boxNo;
    }

    public void setOfDeskNo(byte ofDeskNo) {
        this.ofDeskNo = ofDeskNo;
    }

    public void setOfLayerNo(byte ofLayerNo) {
        this.ofLayerNo = ofLayerNo;
    }

    public void setOfColNo(byte ofColNo) {
        this.ofColNo = ofColNo;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType;
    }
}
