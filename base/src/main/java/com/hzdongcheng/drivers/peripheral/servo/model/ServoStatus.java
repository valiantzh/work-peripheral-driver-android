package com.hzdongcheng.drivers.peripheral.servo.model;

import com.hzdongcheng.drivers.common.Constants;

/**
 * 伺服状态
 * Created by Administrator on 2018/4/12.
 */

public class ServoStatus {

    public ServoStatus(){
        setEnableStatus(Constants.UNKNOWN_STATUS);
        setOriginStatus(Constants.UNKNOWN_STATUS);
        setRunStatus(Constants.UNKNOWN_STATUS);
        setRunForward(Constants.UNKNOWN_STATUS);
        setRunMode(Constants.UNKNOWN_STATUS);
        setRunSpeed(0);
    }
    /**
     * 伺服使能状态. 0:伺服无使能 1:伺服有使能 9:未知
     */
    private byte enableStatus;

    /**
     * 伺服原点状态. 0:伺服无原点 1:伺服有原点 9:未知
     */
    private byte originStatus;

    /**
     * 伺服运行状态. 0:伺服停止 1:伺服运行中 9:未知
     */
    private byte runStatus;

    /**
     * 伺服运行方向状态. 0:正向 1:反向 9:未知
     */
    private byte runForward;

    /**
     * 伺服运行模式状态. 0:高速 1:低速 9:未知
     */
    private byte runMode;

    /**
     * 伺服手动运行速度（转/分钟）
     */
    private int runSpeed;
    /**
     * 标定类型 1-系统基准点标定中 2-基准脉冲数标定中 3-格口位置标定中
     */
    private int correctingType;//标定类型

    private int correctPulse;//校准脉冲数
    private int correctCoil;//数校准圈数
    private int currentPos;//当前位置
    private int targetPos;//目标位置
    private int servoErrorCode;//报警代码（伺服）
    private int totalPulse;//伺服转过脉冲数
    private int totalCoil;//伺服转过圈数
    private int urgentStopReset;//急停复位
    private int errorReset;//报警复位
    private int runErrorCode1;//Ι类报警代码
    private int runErrorCode2;//Π类报警代码
    private int busErrorCode;//业务错误提示代码

    private char[] cmdChar;//命令字

    public byte getEnableStatus() {
        return enableStatus;
    }

    public void setEnableStatus(byte enableStatus) {
        this.enableStatus = enableStatus;
    }

    public byte getOriginStatus() {
        return originStatus;
    }

    public void setOriginStatus(byte originStatus) {
        this.originStatus = originStatus;
    }

    public byte getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(byte runStatus) {
        this.runStatus = runStatus;
    }

    public byte getRunForward() {
        return runForward;
    }

    public void setRunForward(byte runForward) {
        this.runForward = runForward;
    }

    public byte getRunMode() {
        return runMode;
    }

    public void setRunMode(byte runMode) {
        this.runMode = runMode;
    }

    public void setRunSpeed(int runSpeed) {
        this.runSpeed = runSpeed;
    }

    public int getRunSpeed() {
        return runSpeed;
    }

    public int getCorrectingType() {
        return correctingType;
    }

    public void setCorrectingType(int correctingType) {
        this.correctingType = correctingType;
    }

    public int getCorrectPulse() {
        return correctPulse;
    }

    public void setCorrectPulse(int correctPulse) {
        this.correctPulse = correctPulse;
    }

    public int getCorrectCoil() {
        return correctCoil;
    }

    public void setCorrectCoil(int correctCoil) {
        this.correctCoil = correctCoil;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public int getTargetPos() {
        return targetPos;
    }

    public void setTargetPos(int targetPos) {
        this.targetPos = targetPos;
    }

    public int getServoErrorCode() {
        return servoErrorCode;
    }

    public void setServoErrorCode(int servoErrorCode) {
        this.servoErrorCode = servoErrorCode;
    }

    public int getTotalPulse() {
        return totalPulse;
    }

    public void setTotalPulse(int totalPulse) {
        this.totalPulse = totalPulse;
    }

    public int getTotalCoil() {
        return totalCoil;
    }

    public void setTotalCoil(int totalCoil) {
        this.totalCoil = totalCoil;
    }

    public int getUrgentStopReset() {
        return urgentStopReset;
    }

    public void setUrgentStopReset(int urgentStopReset) {
        this.urgentStopReset = urgentStopReset;
    }

    public int getErrorReset() {
        return errorReset;
    }

    public void setErrorReset(int errorReset) {
        this.errorReset = errorReset;
    }

    public int getRunErrorCode1() {
        return runErrorCode1;
    }

    public void setRunErrorCode1(int runErrorCode1) {
        this.runErrorCode1 = runErrorCode1;
    }

    public int getRunErrorCode2() {
        return runErrorCode2;
    }

    public void setRunErrorCode2(int runErrorCode2) {
        this.runErrorCode2 = runErrorCode2;
    }

    public int getBusErrorCode() {
        return busErrorCode;
    }

    public void setBusErrorCode(int busErrorCode) {
        this.busErrorCode = busErrorCode;
    }

    public char[] getCmdChar() {
        return cmdChar;
    }

    public void setCmdChar(char[] cmdChar) {
        this.cmdChar = cmdChar;
    }
}
