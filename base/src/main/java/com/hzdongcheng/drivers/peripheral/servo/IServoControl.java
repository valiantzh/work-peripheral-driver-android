package com.hzdongcheng.drivers.peripheral.servo;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.servo.model.ServoStatus;

/**
 * 伺服电机控制接口
 * Created by Administrator on 2018/4/12.
 */

public interface IServoControl {

    /**
     *
     * @Title: servoEnable
     * @Description: 伺服电机使能开关
     * @param slaveID  副机编号
     * @param @param no
     * @return int
     * @throws DriversException
     */
    int toggleServoEnable(byte slaveID,boolean no) throws DriversException;

    /**
     *
     * @Title: returnServoOrigin
     * @Description: 伺服回原点
     * @param slaveID  副机编号
     * @return int
     * @throws DriversException
     */
    int returnServoOrigin(byte slaveID) throws DriversException;

    /**
     *
     * @Title: runServoByJog
     * @Description: 伺服点动开始
     * @param slaveID  副机编号
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 运动速度  （低速-转/分钟）
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return int
     * @throws DriversException
     */
    int runServoByJog(byte slaveID, int aspect, int speed, int type)throws DriversException;

    /**
     *
     * @Title: stopServoRun
     * @Description: 伺服点动结束
     * @param slaveID  副机编号
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return int
     * @throws DriversException
     */
    int stopServoByJog(byte slaveID, int type)throws DriversException;

    /**
     * 伺服跑圈
     * @param slaveID  副机编号
     * @param iCoil 圈数
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 伺服速度（高速-转/分钟）
     * @return
     * @throws DriversException
     */
    int runServoByCoil(byte slaveID,int iCoil, int aspect, int speed)throws DriversException;

    /**
     *
     * @Title: runServoByNext
     * @Description: 伺服运动到下一个位置
     * @param slaveID  副机编号
     * @param type type  类型 0-大格口  1-小格口
     * @param speed 运动速度  （转/分钟）
     * @return int
     * @throws DriversException
     */
    int runServoByNext(byte slaveID, int type, int speed)throws DriversException;

    /**
     *
     * @Title: runServoByLast
     * @Description: 伺服运动到上一个位置
     * @param slaveID  副机编号
     * @param type  类型 0-大格口  1-小格口
     * @param speed 运动速度  （转/分钟）
     * @return int
     * @throws DriversException
     */
    int runServoByLast(byte slaveID, int type, int speed)throws DriversException;

    /**
     * 紧急停车
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    int urgentStopServo(byte slaveID)throws DriversException;

    /**
     * 急停复位
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    int resetServo4Stop(byte slaveID)throws DriversException;

    /**
     * 报警复位
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    int resetServo4Error(byte slaveID)throws DriversException;

    /**
     * 设置伺服运行速度-业务运行速度
     * @param slaveID  副机编号
     * @param iSpeed 100~3000
     * @return
     * @throws DriversException
     */
    int setServoSpeed(byte slaveID, int iSpeed)throws DriversException;

    /**
     * 设置基准脉冲-基准点
     * @param slaveID  副机编号
     * @param iPulse
     * @return
     * @throws DriversException
     */
    int setPulse4Base(byte slaveID, int iPulse)throws DriversException;

    /**
     * 设置基准脉冲-1圈
     * @param slaveID  副机编号
     * @param iPulse
     * @return
     * @throws DriversException
     */
    int setPulse4Coil(byte slaveID, int iPulse)throws DriversException;

    /**
     * 设置基准脉冲-当前位置
     * @param slaveID  副机编号
     * @param iPulse
     * @return
     * @throws DriversException
     */
    int setPulse4Position(byte slaveID, int iPulse)throws DriversException;


    /**
     *
     * @Title: getServoStatus
     * @Description: 获取伺服状态
     * @param slaveID  副机编号
     * @return ServoStatus
     * @throws DriversException
     */
    ServoStatus getServoStatus(byte slaveID)throws DriversException;
}
