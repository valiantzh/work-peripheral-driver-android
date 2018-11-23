// IServoController.aidl
package com.hzdongcheng.drivers.rotation;
import com.hzdongcheng.drivers.bean.Result;
// Declare any non-default types here with import statements

interface IServoController {
    /**
     *
     * @Title: servoEnable
     * @Description: 伺服电机使能开关
     * @param slaveID 副机编号
     * @param @param no
     * @return
     */
    Result toggleServoEnable(byte slaveID,boolean no);

    /**
     *
     * @Title: returnServoOrigin
     * @Description: 伺服回原点
     * @param slaveID 副机编号
     * @return
     */
    Result returnServoOrigin(byte slaveID);

    /**
     *
     * @Title: runServoByJog
     * @Description: 伺服点动开始
     * @param slaveID 副机编号
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 运动速度  （低速-转/分钟）
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return
     */
    Result runServoByJog(byte slaveID, int aspect, int speed, int type);

    /**
     *
     * @Title: stopServoRun
     * @Description: 伺服点动结束
     * @param slaveID 副机编号
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return
     */
    Result stopServoByJog(byte slaveID, int type);

    /**
     * 伺服跑圈
     * @param slaveID 副机编号
     * @param iCoil 圈数
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 伺服速度（高速-转/分钟）
     * @return
     */
    Result runServoByCoil(byte slaveID,int iCoil, int aspect, int speed);

    /**
     *
     * @Title: runServoByNext
     * @Description: 伺服运动到下一个位置
     * @param slaveID 副机编号
     * @param type type  类型 0-大格口  1-小格口
     * @param speed 运动速度  （转/分钟）
     * @return
     */
    Result runServoByNext(byte slaveID, int type, int speed);

    /**
     *
     * @Title: runServoByLast
     * @Description: 伺服运动到上一个位置
     * @param slaveID 副机编号
     * @param type  类型 0-大格口  1-小格口
     * @param speed 运动速度  （转/分钟）
     * @return
     */
    Result runServoByLast(byte slaveID, int type, int speed);

    /**
     * 紧急停车
     * @param slaveID
     * @return
     */
    Result urgentStopServo(byte slaveID);

    /**
     * 急停复位
     * @param slaveID 副机编号
     * @return
     */
    Result resetServo4Stop(byte slaveID);

    /**
     * 报警复位
     * @param slaveID 副机编号
     * @return
     */
    Result resetServo4Error(byte slaveID);

    /**
     * 设置基准脉冲-基准点
     * @param slaveID 副机编号
     * @param iPulse
     * @return
     */
    Result setPulse4Base(byte slaveID, int iPulse);

    /**
     * 设置基准脉冲-1圈
     * @param slaveID 副机编号
     * @param iPulse
     * @return
     */
    Result setPulse4Coil(byte slaveID, int iPulse);

    /**
     * 设置基准脉冲-当前位置
     * @param slaveID 副机编号
     * @param iPulse
     * @return
     */
    Result setPulse4Position(byte slaveID, int iPulse);
    /**
     *
     * @Title: getServoStatus
     * @Description: 获取伺服状态
     * @param slaveID 副机编号
     * @return
     */
    Result getServoStatus(byte slaveID);
}
