package com.hzdongcheng.drivers.peripheral.plc.model;

/**
 * 货格状态
 * Created by Administrator on 2018/4/13.
 */

public class ShelfBoxStatus implements java.io.Serializable{
    /**
     * 物品状态. 0:无物 1:有物 2: 无物光幕被挡 3：有物光幕被挡 9:未知
     */
    private byte goodsStatus;

    /**
     * 箱门开关状态. 0:关闭 1:打开 9:未知
     */
    private byte openStatus;

    /**
     *  货格是否在取货口
     */
    private boolean isReachDoor;
    /**
     * 格口内部门编号
     */
    private int doorNo;

    public ShelfBoxStatus(){
        goodsStatus = 9;
        openStatus = 9;
        isReachDoor = false;
        doorNo = -1;
    }

    public byte getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(byte goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public byte getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(byte openStatus) {
        this.openStatus = openStatus;
    }

    public boolean isReachDoor() {
        return isReachDoor;
    }

    public void setReachDoor(boolean reachDoor) {
        isReachDoor = reachDoor;
    }

    public int getDoorNo() {
        return doorNo;
    }

    public void setDoorNo(int doorNo) {
        this.doorNo = doorNo;
    }
}
