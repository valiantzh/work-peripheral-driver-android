// IRotationController.aidl
package com.hzdongcheng.drivers.rotation;
import com.hzdongcheng.drivers.bean.Result;
// Declare any non-default types here with import statements

interface IRotationController {
    /**
     * 初始化
     * @param slaveID 副机编号
     * @return
     */
    Result initialize(byte slaveID);

    /**
     * 复位
     * @param slaveID 副机编号
     * @return
     */
    Result reset(byte slaveID);

    /**
     * 移动指定货格到存取口
     * @param slaveID 副机编号
     * @param boxID 副机内格口编号 1~
     * @return
     */
    Result moveShelfBox(byte slaveID, int boxID);

    /**
     * 打开货格进行存取
     * @param slaveID 副机编号
     * @param boxID 副机内格口编号 1~
     * @return
     */
    Result openBox4Access(byte slaveID, int boxID);

    /**
     * 打开指定柜门
     * @param slaveID 副机编号
     * @param doorNo  门编号 0开始
     * @return
     */
    Result openDoor(byte slaveID, byte doorNo);

    /**
     * 打开指定柜门-强制
     * @param slaveID 副机编号
     * @param doorNo  门编号 0开始
     * @return
     */
    Result openDoorForce(byte slaveID, byte doorNo);

    /**
     * 打开所有门
     * @param slaveID 副机编号
     * @return
     */
    Result openAllDoor(byte slaveID);

    /**
     * 打开维护门
     * @param slaveID 副机编号
     * @return
     */
    Result openRepairDoor(byte slaveID);

    /**
     * 关闭指定柜门
     * @param slaveID 副机编号
     * @param doorNo  门编号 0开始
     * @return
     */
    Result closeDoor(byte slaveID, byte doorNo);

    /**
     * 查询货格状态
     * @param slaveID 副机编号
     * @param boxID 副机内格口编号 1~
     * @return
     */
    Result queryBoxStatus(byte slaveID, int boxID);

    /**
     * 查询副机状态
     * @param slaveID 副机编号
     * @return
     */
    Result querySlaveStatus(byte slaveID);

    /**
     * 发送盘库任务
     * @param slaveID 副机编号
     * @param checkToken 盘库令牌
     * @return
     */
    Result sendCheckTask(byte slaveID, int checkToken);

    /**
     * 查询盘库结果
     * @param slaveID 副机编号
     * @param checkToken
     * @return
     */
    Result queryCheckResult(byte slaveID, int checkToken);
}
