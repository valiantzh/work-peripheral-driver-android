package com.hzdongcheng.drivers.bean;

/**
 * 货格信息
 * Created by Administrator on 2018/4/13.
 */

public class BoxInfo {
    /**
     * 货格编号
     */
    public String BoxNo;

    /**
     * 货格名称
     */
    public String BoxName;

    /**
     * 所在副机 1~
     */
    public int iDeskNo;

    /**
     * 所在副机内的格口编号 1~
     */
    public int iBoxNo;//
    /**
     * 所在层 0~
     */
    public int ofLayer;
    /**
     * 所在列 1~
     */
    public int ofCol;

    /**
     * 箱门编号 0~
     */
    public int iDoorNo;

    /**
     * 货格类型
     */
    public String BoxType;

    /**
     * 物品状态. 0:无物 1:有物 9:未知
     */
    public int GoodsStatus;

    /**
     * 箱门开关状态. 0:关闭 1:打开 9:未知
     */
    public int OpenStatus;

    /**
     * 门状态 0-正常 1-锁定（超时未关门）
     */
    public int DoorStatus;
}
