package com.hzdongcheng.drivers.peripheral.plc.control.rotate;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.plc.model.CheckResult;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxStatus;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;
import com.hzdongcheng.drivers.peripheral.servo.IServoControl;

/**
 *
 * 旋转货柜控制接口
 * 旋转货柜功能描述：一个货柜有多层，每层的货格类型相同；1~多个存取口；多个货格共用一个存取口；存取口固定，货架在动
 * 旋转初始化
 * 旋转重置
 * 旋转到指定位置
 * 打开存取门
 * 打开维修门
 * 旋转盘点
 *
 * Created by Administrator on 2018/4/12.
 */

public interface IRotateControl extends IServoControl{

    /**
     * 副机初始化
     * @param slaveID  副机编号
     * @return
     */
    void initialize(byte slaveID) throws DriversException;

    /**
     * 复位电机
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    boolean resetServo(byte slaveID)throws DriversException;


    /**
     * 移动指定货格到存取口
     * @param slaveID  副机编号
     * @param ofLayer 所在层 0开始
     * @param ofCol   所在列 1开始
     * @return
     */
    ShelfBoxStatus move2Access(byte slaveID, byte ofLayer, byte ofCol) throws  DriversException;

    /**
     * 打开指定存取门
     * @param slaveID  副机编号
     * @param doorNo  门编号 0开始
     * @param isCheck true 未对正，不开门 false -强制开门
     * @return 0-门关  1-门开
     */
    int openAccessDoor(byte slaveID, byte doorNo, boolean isCheck) throws  DriversException;

    /**
     * 移动指定货格到存取口并开门
     * @param slaveID  副机编号
     * @param ofLayer 所在层 0开始
     * @param ofCol   所在列 1开始
     * @return
     */
    ShelfBoxStatus move2AccessAndOpen(byte slaveID, byte ofLayer, byte ofCol) throws  DriversException;

    /**
     * 关闭指定存取门
     * @param slaveID  副机编号
     * @param doorNo 0开始
     * @return
     * @throws DriversException
     */
    int closeAccessDoor(byte slaveID, byte doorNo) throws  DriversException;
    /**
     * 打开指定维修门口
     * @param slaveID  副机编号
     * @param doorNo  门编号 0开始
     * @return
     */
    int openRepairDoor(byte slaveID, byte doorNo) throws  DriversException;

    /**
     * 打开所有门
     * @param slaveID  副机编号
     * @return
     */
    int openAllDoor(byte slaveID) throws  DriversException;

    /**
     * 发送盘库任务
     * @param slaveID  副机编号
     * @param checkToken 盘库令牌
     * @return
     */
    int sendCheckTask(byte slaveID, int checkToken) throws DriversException;

    /**
     * 查询盘库结果
     * @param slaveID  副机编号
     * @param checkToken
     * @return
     * @throws DriversException
     */
    CheckResult queryCheckResult(byte slaveID, int checkToken) throws DriversException;

    /**
     * 查询货格状态
     * @param slaveID  副机编号
     * @param ofLayer 0~
     * @param ofCol 1~
     * @return
     * @throws DriversException
     */
    ShelfBoxStatus queryShelfBoxStatus(byte slaveID, byte ofLayer, byte ofCol) throws  DriversException;

    /**
     * 查询副机状态
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    SlaveDeskStatus querySlaveDeskStatus(byte slaveID) throws  DriversException;
}
