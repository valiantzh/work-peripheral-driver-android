package com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl;

import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.base.modbus.ModbusBase;
import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.modbus.helper.CmdChar;
import com.hzdongcheng.drivers.base.modbus.helper.ModbusHelper;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.AbstractRotateControl;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.IRotateControl;
import com.hzdongcheng.drivers.peripheral.plc.model.CheckResult;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxStatus;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;
import com.hzdongcheng.drivers.peripheral.servo.model.ServoStatus;


/**
 * 东城3C智能仓旋转控制实现
 * slaveID  副机编号 1~
 * Created by Administrator on 2018/4/14.
 */

public class Dm3ccRotateControlImpl extends AbstractRotateControl implements IRotateControl {
    private final static int DEFAULT_SERVO_SPEED_HIGH = 3000;
    private final Log4jUtils log = Log4jUtils.createInstanse(this.getClass());
    private static ModbusHelper modbusHelper = ModbusHelper.getInstance();
    private final static int QUERY_STATUS_GAP_MS = 550;//状态查询间隔 ms

    public Dm3ccRotateControlImpl(ModbusBase modbusBase) {
        super(modbusBase);

    }

    //#region 状态检查
    /**
     * 检查系统状态
     * 1、有无原点，有误使能开关
     * 2、伺服报警
     * 3、PLC系统异常
     * @return
     */
    private void checkSystemState(byte slaveID) throws DriversException{
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        StringBuffer errorMsg = new StringBuffer();
        int servoError = cmdCharHelper.getServoErrorCode();
        if(servoError !=0 ){
            errorMsg.append("[ServoError:").append(servoError).append("]");
        }
        if(0 == cmdCharHelper.getEnableStatus()){//伺服无使能
            throw new DriversException(DriversErrorCode.ERR_PD_SERVO_DISABLE, errorMsg.toString());
        }
        int runErrorCode2 = cmdCharHelper.getRunErrorCode2();
        int runErrorCode1 = cmdCharHelper.getRunErrorCode1();
        if(runErrorCode2 != 0 || runErrorCode1 != 0){
            if(runErrorCode2 != 0){
                errorMsg.append("[ErrorII:").append(runErrorCode2).append("]");
            }
            if(runErrorCode1 != 0){
                errorMsg.append("[ErrorI:").append(runErrorCode1).append("]");
            }
            if(1 == cmdCharHelper.getRunErrorCode(0)){//12.0 系统紧急停止中
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_URGENTSTOPPING, errorMsg.toString());
            }
            if(1 == cmdCharHelper.getRunErrorCode(2)){//12.2 伺服报警
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_EXISTSALARM, errorMsg.toString());
            }
            if(1 == cmdCharHelper.getRunErrorCode(16)){//13.0 系统无原点
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_LOST_ORIGIN, errorMsg.toString());
            }
            if(1 == cmdCharHelper.getRunErrorCode(17)){//13.1 系统无基准点
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_NO_REFERENCEPOIINT, errorMsg.toString());
            }
            if(1 == cmdCharHelper.getRunErrorCode(22)){//13.6 PLC系统异常
                throw new DriversException(DriversErrorCode.ERR_PD_PLC_SYSALARM, errorMsg.toString());
            }

            if(1 == cmdCharHelper.getBusErrorCode(7)){//14.7 1，单侧圈数超限（请求回原点）
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_RUN_OVER_LIMIT, errorMsg.toString());
            }
        }
    }

    /**
     * 检查运动环境
     * 1、运动中，不允许动
     * 2、光幕被挡，不允许动
     * 3、自动门未全关闭，不允许动
     * 4、手动门未全关闭，不允许动
     * 5、盘库中，不允许动
     */
    private void checkMoveState(byte slaveID) throws DriversException{
        checkMoveState(slaveID, false);
    }
    private void checkMoveState(byte slaveID, boolean checkTask) throws DriversException{
        checkSystemState(slaveID);
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        StringBuffer errorMsg = new StringBuffer();
        if(1== cmdCharHelper.getRunStatus()){//运动中，不允许动
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_FORBIDMOVE4RUNNING);
        }
        if(1 == cmdCharHelper.getRasterStatus()){//14.0 1,光幕被挡
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_RASTERTHING, errorMsg.toString());
        }
        if(1 == cmdCharHelper.getBusErrorCode(2)){//14.2 1，自动门未全关闭
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_FORBIDMOVE4AUTODOOROPEN, errorMsg.toString());
        }
        if(1 == cmdCharHelper.getBusErrorCode(3)){//14.3 1,手动门未全关闭
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_FORBIDMOVE4MANUALDOOROPEN, errorMsg.toString());
        }
        if(1 == cmdCharHelper.getCheckStatus()){//盘库中
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_FORBIDMOVE4CHECKING, errorMsg.toString());
        }
        if(checkTask){//盘库
            if(1== cmdCharHelper.getRunErrorCode(19)){//13.3 市电断路 UPS供电
                throw new DriversException(DriversErrorCode.ERR_PD_PLC_FORBIDMOVE4CHECKING, errorMsg.toString());
            }
        }
    }

    /**
     * 检查开门环境
     *  1、运动中，不允许开门
     *  2、未对正任何格口，不允许开门
     * @throws DriversException
     */
    private void checkOpenDoorState(byte slaveID)throws DriversException{
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        if(1== cmdCharHelper.getRunStatus()){//运动中，不允许开门
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_FORBIDOPEN4RUNNING);
        }
        if(1 == cmdCharHelper.getBusErrorCode(4)){//14.4 未对正任何格口
            throw new DriversException(DriversErrorCode.ERR_PD_PLC_NOALIGNBOX);
        }
    }
    //#endregion

    //#region 业务功能控制
    /**
     * 初始化
     * @param slaveID  副机编号
     * @return
     */
    @Override
    public void initialize(byte slaveID) throws DriversException {
        long start = System.currentTimeMillis();
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        Dm3ccCmdCharHelper.setServoSpeed(slaveID,DEFAULT_SERVO_SPEED_HIGH);

        //1、检查有无告警
        if(1 == cmdCharHelper.getErrorReset()){
            resetServo4Error(slaveID);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cmdCharHelper.setRCmdChar(slaveID,  _readFast(slaveID));
            if(1 == cmdCharHelper.getErrorReset()){//复位失败
                String errorMsg = "ServoErrorCode="+cmdCharHelper.getServoErrorCode()+"";
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_RESET_FAIL,errorMsg);
            }
        }
        //2、急停复位
        if(1== cmdCharHelper.getUrgentStopReset()){
            resetServo4Stop(slaveID);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cmdCharHelper.setRCmdChar(slaveID,  _readFast(slaveID));
            if(1== cmdCharHelper.getUrgentStopReset()){
                String errorMsg = "Reset urgent stop fail";
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_RESET_FAIL, errorMsg);
            }
        }
        //3、检查原点
        if(0 == cmdCharHelper.getOriginStatus()){
            //回原点
            returnServoOrigin(slaveID);
            //查原点状态
            do{
                cmdCharHelper.setRCmdChar(slaveID,  _readFast(slaveID));
                if(1 == cmdCharHelper.getOriginStatus()){//有原点
                    break;
                }else if(2 == cmdCharHelper.getOriginStatus()){//回原点中
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }else{
                    String errorMsg = "ServoErrorCode="+cmdCharHelper.getServoErrorCode()+"";
                    throw new DriversException(DriversErrorCode.ERR_PD_SERVO_BACK_ORIGIN_FAIL,errorMsg);
                }

                if(1 == cmdCharHelper.getRunErrorCode(1)){//12.1 原点回归超时
                    throw new DriversException(DriversErrorCode.ERR_PD_SERVO_BACK_ORIGIN_TIMEOUT);
                }
            }while(true);
        }
        if(0 == cmdCharHelper.getEnableStatus()) {//伺服无使能
            toggleServoEnable(slaveID, true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
        checkSystemState(slaveID);
        log.debug("slaveID="+slaveID+",initialize  "+(System.currentTimeMillis() - start)+" ms");
    }
    /**
     * 电机复位
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    @Override
    public boolean resetServo(byte slaveID)throws DriversException{
        long start = System.currentTimeMillis();
        Dm3ccCmdCharHelper.setServoSpeed(slaveID,DEFAULT_SERVO_SPEED_HIGH);
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        //1、检查有无告警
        if(1 == cmdCharHelper.getErrorReset()){
            resetServo4Error(slaveID);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cmdCharHelper.setRCmdChar(slaveID, _readFast(slaveID));
            if(1 == cmdCharHelper.getErrorReset()){//复位失败
                String errorMsg = "ServoErrorCode="+cmdCharHelper.getServoErrorCode()+"";
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_RESET_FAIL,errorMsg);
            }
        }
        //2、急停复位
        if(1== cmdCharHelper.getUrgentStopReset()){
            resetServo4Stop(slaveID);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cmdCharHelper.setRCmdChar(slaveID, _readFast(slaveID));
            if(1== cmdCharHelper.getUrgentStopReset()){
                String errorMsg = "Reset urgent stop fail";
                throw new DriversException(DriversErrorCode.ERR_PD_SERVO_RESET_FAIL, errorMsg);
            }
        }
        //3、检查原点
        if(0 == cmdCharHelper.getOriginStatus()
                || 1== cmdCharHelper.getBusErrorCode(7)){//14.7 1，单侧圈数超限（请求回原点）
            //回原点
            returnServoOrigin(slaveID);
            //查原点状态
            do{
                cmdCharHelper.setRCmdChar(slaveID,  _readFast(slaveID));
                if(1 == cmdCharHelper.getOriginStatus()){//有原点
                    break;
                }else if(2 == cmdCharHelper.getOriginStatus()){//回原点中
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }else{
                    String errorMsg = "ServoErrorCode="+cmdCharHelper.getServoErrorCode()+"";
                    throw new DriversException(DriversErrorCode.ERR_PD_SERVO_BACK_ORIGIN_FAIL,errorMsg);
                }
                if(1 == cmdCharHelper.getRunErrorCode(1)){//12.1 原点回归超时
                    throw new DriversException(DriversErrorCode.ERR_PD_SERVO_BACK_ORIGIN_TIMEOUT);
                }
            }while(true);
        }
        if(0 == cmdCharHelper.getEnableStatus()) {//伺服无使能
            toggleServoEnable(slaveID, true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {}
        }
        checkSystemState(slaveID);
        log.debug("slaveID="+slaveID+",resetServo  "+(System.currentTimeMillis() - start)+" ms");
        return true;
    }
    private ShelfBoxStatus _move2Access(byte slaveID, byte ofLayer, byte ofCol) throws DriversException {
        long start = System.currentTimeMillis();
        ShelfBoxStatus boxStatus = new ShelfBoxStatus();
        //查当前格口位置
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        if(cmdCharHelper._getInsideTargetPos(slaveID, ofLayer, ofCol ) == cmdCharHelper.getCurrentPos()){
            //目标位置与当前位置一致, 检查门状态
            int iDoorNo = cmdCharHelper._getInsideDoorNo(slaveID, ofLayer);
            int iLayer = cmdCharHelper._getInsideLayerNo(slaveID,ofLayer);
            boxStatus.setOpenStatus(cmdCharHelper.getDoorStatus(iDoorNo));
            boxStatus.setGoodsStatus(cmdCharHelper.getGoodsStatus(iLayer, ofCol));
            boxStatus.setDoorNo(iDoorNo);
            boxStatus.setReachDoor(true);
            return boxStatus;
        }
        checkMoveState(slaveID);
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        //自动 高速
        int speed = Dm3ccCmdCharHelper.getServoSpeed(slaveID);
        cmdChar.modCmdCharBit(3, 3, 1);//3.3  0，手动；1，自动
        cmdChar.modCmdCharBit(4, 1, 0);//4.1  0，伺服高速
        cmdChar.modCmdChar(17,((speed>>8)&0xff), ((speed)&0xff));//17  伺服手动运行速度指令2（高速指令）
        cmdChar.modCmdCharBit(7, 0, Dm3ccCmdCharHelper._getInsideBoxType(slaveID, ofLayer));//7.0  0，大格口定位；1，小格口定位(自动）
        cmdChar.modCmdChar(8,(byte)(0), ofCol);//8  目标位置
        int rc = 0;
        try {
            cmdChar.modCmdCharBit(7, 4, 0);//7.4  先置0
            rc = _write(slaveID, cmdChar,"_move2Access0");//先设置速度值
            if(rc == 0){
                cmdChar.modCmdCharBit(7, 4, 1);//7.4  0-1，执行（上升沿执行一次）(自动）
                rc = _write(slaveID, cmdChar,"_move2Access1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        } finally {
            log.debug("slaveID="+slaveID+",move2Access:ofLayer="+ofLayer+",ofCol="+ofCol+" "+(System.currentTimeMillis() - start)+" ms,rc="+rc+".speed="+speed+",ofLayer="+ofLayer+","+ofCol);
        }
        return boxStatus;
    }


    /**
     * 移动指定货格到存取口
     * @param slaveID  副机编号
     * @param ofLayer 所在层 0开始
     * @param ofCol   所在列 1开始
     * @return
     */
    @Override
    public ShelfBoxStatus move2Access(byte slaveID, byte ofLayer, byte ofCol) throws DriversException {
        long start = System.currentTimeMillis();
        ShelfBoxStatus boxStatus = _move2Access(slaveID, ofLayer, ofCol);
        if(boxStatus.isReachDoor()){
            //到位
            return boxStatus;
        }else {
            //等待到位
            boxStatus = _waitReachDoor(slaveID, ofLayer, ofCol, false);
        }
        log.debug("slaveID="+slaveID+",move2Access2 :ofLayer="+ofLayer+",ofCol="+ofCol+" "+(System.currentTimeMillis() - start)+" ms"+",ofLayer="+ofLayer+","+ofCol);
        return boxStatus;
    }

    //等待到达取货口
    private ShelfBoxStatus _waitReachDoor(byte slaveID, byte ofLayer, byte ofCol, boolean isOpenDoor) throws  DriversException{
        int count = 20;//
        ShelfBoxStatus boxStatus = new ShelfBoxStatus();
        int iLayer = Dm3ccCmdCharHelper._getInsideLayerNo(slaveID, ofLayer);
        int iDoorNo = Dm3ccCmdCharHelper._getInsideDoorNo(slaveID, ofLayer);
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        do{
            if(1== cmdCharHelper.isReachDoor(iLayer, ofCol)){
                if(isOpenDoor){
                    byte isOpen = (byte)openAccessDoor(slaveID,(byte)iDoorNo, true);
                    boxStatus.setOpenStatus(isOpen);
                }else{
                    boxStatus.setOpenStatus(cmdCharHelper.getDoorStatus(iDoorNo));
                }
                boxStatus.setDoorNo(iDoorNo);
                boxStatus.setReachDoor(true);
                boxStatus.setGoodsStatus(cmdCharHelper.getGoodsStatus(iLayer, ofCol));
                break;
            }
            count--;
            if(count < 0){
                throw new DriversException(DriversErrorCode.ERR_PD_RUNBUSINESSTIMEOUT);
            }
            try {
                Thread.sleep(QUERY_STATUS_GAP_MS);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            cmdCharHelper.setRCmdChar(slaveID, _readFast(slaveID));
        }while( true);

        return boxStatus;
    }

    /**
     * 移动指定货格到存取口并开门
     * @param slaveID  副机编号
     * @param ofLayer 所在层 0开始
     * @param ofCol   所在列 1开始
     * @return
     */
    @Override
    public ShelfBoxStatus move2AccessAndOpen(byte slaveID, byte ofLayer, byte ofCol) throws  DriversException{
        long start = System.currentTimeMillis();

        ShelfBoxStatus boxStatus = move2Access(slaveID, ofLayer, ofCol);
        if(boxStatus.isReachDoor() && 1 == boxStatus.getOpenStatus()){
            //到位&门已开
            return boxStatus;
        }else{
            //等待到位，并开门
            boxStatus = _waitReachDoor(slaveID, ofLayer, ofCol, true);
        }

        log.debug("slaveID="+slaveID+",move2AccessAndOpen :ofLayer="+ofLayer+",ofCol="+ofCol+" "+(System.currentTimeMillis() - start)+" ms"+",ofLayer="+ofLayer+","+ofCol);
        return boxStatus;
    }

    /**
     * 打开指定存取门口
     *
     * @param slaveID  副机编号
     * @param doorNo  门编号 0~6
     * @param isCheck true 未对正，不开门 false -强制开门
     * @return 0-门关 1-门开
     */
    @Override
    public int openAccessDoor(byte slaveID, byte doorNo, boolean isCheck) throws DriversException {
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        if(1 == cmdCharHelper.getDoorStatus(doorNo)){
            return 1;
        }
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        int iChar;
        int iBit;
        if(isCheck){//自动 检查是否对正
            checkOpenDoorState(slaveID);
            cmdChar.modCmdCharBit(3, 3, 1);//3.3  0，手动；1，自动
            iChar = 9;
            iBit  = doorNo;//9.0~ 自动状态下，开门控制字
        }else{
            //手动
            cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动
            iChar = 4;
            iBit  = doorNo+8;//4.8~15
        }

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(iChar, iBit, 0);//
            rc = _write(slaveID, cmdChar,"openAccessDoor0");//先置0
            if (rc == 0) {
                cmdChar.modCmdCharBit(iChar, iBit, 1);
                rc = _write(slaveID, cmdChar,"openAccessDoor1");//运行
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cmdCharHelper.setRCmdChar(slaveID, _read(slaveID));
            return cmdCharHelper.getDoorStatus(doorNo);
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        } finally {
            log.debug("slaveID="+slaveID+",openAccessDoor :doorNo="+doorNo+",isCheck="+isCheck+" "+(System.currentTimeMillis() - start)+" ms,rc="+rc+",doorNo="+doorNo);
        }
    }

    /**
     * 关闭指定存取门
     * @param slaveID  副机编号
     * @param doorNo
     * @return
     * @throws DriversException
     */
    @Override
    public int closeAccessDoor(byte slaveID, byte doorNo) throws  DriversException{
        long start = System.currentTimeMillis();
        return 0;
    }

    /**
     * 打开指定维修门
     *
     * @param slaveID  副机编号
     * @param doorNo  门编号
     * @return
     */
    @Override
    public int openRepairDoor(byte slaveID, byte doorNo) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        //手动
        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(4, 15, 0);
            rc = _write(slaveID, cmdChar,"openRepairDoor0");//先置0
            if(rc == 0){
                cmdChar.modCmdCharBit(4, 15, 1);//4.15  维修门开
                rc = _write(slaveID, cmdChar,"openRepairDoor1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        } finally {
            log.debug("slaveID="+slaveID+",openRepairDoor: doorNo="+doorNo+" "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }
    /**
     * 打开所有门
     * @param slaveID  副机编号
     * @return
     */
    @Override
    public int openAllDoor(byte slaveID) throws  DriversException{
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        //手动
        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(4, 8, 0);
            cmdChar.modCmdCharBit(4, 9, 0);
            cmdChar.modCmdCharBit(4, 10, 0);
            cmdChar.modCmdCharBit(4, 11, 0);
            cmdChar.modCmdCharBit(4, 12, 0);
            cmdChar.modCmdCharBit(4, 13, 0);
            cmdChar.modCmdCharBit(4, 14, 0);
            cmdChar.modCmdCharBit(4, 15, 0);
            rc = _write(slaveID, cmdChar,"openAllDoor0");//先置0
            if(rc == 0){
                cmdChar.modCmdCharBit(4, 8, 1);
                cmdChar.modCmdCharBit(4, 9, 1);
                cmdChar.modCmdCharBit(4, 10, 1);
                cmdChar.modCmdCharBit(4, 11, 1);
                cmdChar.modCmdCharBit(4, 12, 1);
                cmdChar.modCmdCharBit(4, 13, 1);
                cmdChar.modCmdCharBit(4, 14, 1);
                cmdChar.modCmdCharBit(4, 15, 1);
                rc = _write(slaveID, cmdChar,"openAllDoor1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",openAllDoor  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }
    /**
     * 发送盘库任务
     * @param slaveID  副机编号
     * @param checkToken 盘库令牌
     * @return
     */
    @Override
    public int sendCheckTask(byte slaveID, int checkToken) throws DriversException{
        long start = System.currentTimeMillis();

        //运行环境检查
        checkMoveState(slaveID, true);

        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        //自动 高速
        int speed = Dm3ccCmdCharHelper.getServoSpeed(slaveID);
        cmdChar.modCmdCharBit(3, 3, 1);//3.3  0，手动；1，自动
        cmdChar.modCmdCharBit(4, 1, 0);//4.1  0，伺服高速
        cmdChar.modCmdChar(17,((speed>>8)&0xff), ((speed)&0xff));//17  伺服手动运行速度指令2（高速指令）

        int rc = 0;
        try {

            cmdChar.modCmdCharBit(19, 1, 0);//0，未接收到反馈数据
            cmdChar.modCmdChar(20,((checkToken>>8)&0xff),((checkToken)&0xff));//20 本次盘库指令，数据校准代码
            cmdChar.modCmdCharBit(19, 0, 0);
            cmdChar.modCmdCharBit(14, 5, 0);//14.5 1，盘库中等待超时
            cmdChar.modCmdCharBit(20, 5, 0);//20.5 0，未定义；1，盘库被中断
            rc = _write(slaveID, cmdChar,"sendCheckTask0");//先置0
            if(rc == 0){
                cmdChar.modCmdCharBit(19, 0, 1);//19.0  执行盘库（上升沿执行一次）
                rc = _write(slaveID, cmdChar,"sendCheckTask1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",sendCheckTask  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    /**
     * 查询盘库结果
     * @param slaveID  副机编号
     * @param checkToken
     * @return
     * @throws DriversException
     */
    @Override
    public CheckResult queryCheckResult(byte slaveID, int checkToken) throws DriversException{
        long start = System.currentTimeMillis();
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));

        CheckResult  checkResult = new CheckResult();
        checkResult.setCheckStatus(cmdCharHelper.getCheckStatus());
        checkResult.setGoodsStatus(cmdCharHelper.getCheckGoodsStatus());
        checkResult.setCheckToken(checkToken);
        if(checkResult.getCheckStatus() == 2){//盘库完成
            CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
            //自动
            cmdChar.modCmdCharBit(3, 3, 1);//3.3  0，手动；1，自动

            try {
                cmdChar.modCmdCharBit(19, 1, 1);//19.1  未接收到反馈数据；1，接收到了
                _write(slaveID, cmdChar,"queryCheckResult");//运行
            } catch (ModbusException e) {
                //e.printStackTrace();
                throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
            }
        }
        log.debug("queryCheckResult  "+(System.currentTimeMillis() - start)+" ms");
        return checkResult;
    }

    /**
     * 查询货格状态
     *
     * @param slaveID  副机编号
     * @param ofLayer 0~
     * @param ofCol 1~
     * @return
     * @throws DriversException
     */
    @Override
    public ShelfBoxStatus queryShelfBoxStatus(byte slaveID, byte ofLayer, byte ofCol) throws DriversException {
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        int iLayer = cmdCharHelper._getInsideLayerNo(slaveID, ofLayer);//内部层号
        int doorNo = cmdCharHelper._getInsideDoorNo(slaveID, ofLayer);
        ShelfBoxStatus boxStatus = new ShelfBoxStatus();
        boxStatus.setOpenStatus(cmdCharHelper.getDoorStatus(doorNo));
        boxStatus.setGoodsStatus(cmdCharHelper.getGoodsStatus(iLayer, ofCol));
        boxStatus.setDoorNo(doorNo);
        boxStatus.setReachDoor(cmdCharHelper.isReachDoor(iLayer, ofCol)==1?true:false);

        return boxStatus;
    }

    /**
     * 查询所有货格状态
     *
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    @Override
    public SlaveDeskStatus querySlaveDeskStatus(byte slaveID) throws DriversException {
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        SlaveDeskStatus slaveDeskStatus = new SlaveDeskStatus();
        try{
            checkSystemState(slaveID);
        }catch (DriversException e){
            slaveDeskStatus.setSlaveStatus(1);
            slaveDeskStatus.setErrorCode(e.getErrorCode());
            slaveDeskStatus.setErrorMsg(e.getErrorTitle()+","+e.getMessage());
        }

        slaveDeskStatus.setCurrentPos(cmdCharHelper.getCurrentPos());
        slaveDeskStatus.setRunStatus(cmdCharHelper.getRunStatus());
        slaveDeskStatus.setRasterStatus(cmdCharHelper.getRasterStatus());//14.0 1,光幕被挡
        slaveDeskStatus.setRepairDoorStatus(cmdCharHelper.getBusErrorCode(1));//14.1 1，维护门未关闭
        slaveDeskStatus.setAutoDoorStatus(cmdCharHelper.getBusErrorCode(2));//14.2 1，自动门未全关闭
        slaveDeskStatus.setManualDoorStatus(cmdCharHelper.getBusErrorCode(3));//14.3 1,手动门未全关闭
        slaveDeskStatus.setShockStatus(cmdCharHelper.getRunErrorCode(20));//13.4 震动报警
        slaveDeskStatus.setPowerSupply(cmdCharHelper.getRunErrorCode(19));//13.3 市电断路 UPS供电
        int iCol = 0;
        int currentPos = cmdCharHelper.getCurrentPos();
        if(currentPos> Dm3ccCmdCharHelper.GAP_BOX_NO){
            iCol = currentPos- Dm3ccCmdCharHelper.GAP_BOX_NO;
        }else{
            iCol = currentPos;
        }
        for(int i = 0; i< Dm3ccCmdCharHelper.MAX_LAYER_NUM; i++){
            int ofLayer = Dm3ccCmdCharHelper._getOutsideLayerNo(slaveID, i);
            if(ofLayer >= 0){
                ShelfBoxStatus boxStatus = new ShelfBoxStatus();
                boxStatus.setOpenStatus(cmdCharHelper.getDoorStatus(i));
                boxStatus.setGoodsStatus(cmdCharHelper.getGoodsStatus(i, iCol));
                boxStatus.setDoorNo(i);
                boxStatus.setReachDoor(cmdCharHelper.isReachDoor(i, iCol)==1?true:false);
                slaveDeskStatus.addBoxStatus(ofLayer, boxStatus);
            }

        }

        return slaveDeskStatus;
    }
    //#endregion

    //#region 伺服电机控制
    /**
     * @param no
     * @param slaveID  副机编号
     * @return int
     * @throws DriversException
     * @Title: servoEnable
     * @Description: 伺服电机使能开关
     */
    @Override
    public int toggleServoEnable(byte slaveID,boolean no) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar =Dm3ccCmdCharHelper.buildCmdCharPre();
        int rc = 0;
        try {
            cmdChar.modCmdCharBit(3, 4, 0);//3.4 去使能
            rc = _write(slaveID, cmdChar,"toggleServoEnable0");
            if(no){
                cmdChar.modCmdCharBit(3, 4,1);//3.4  伺服使能
                rc = _write(slaveID, cmdChar,"toggleServoEnable1");
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",returnServoOrigin  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    /**
     * @param slaveID  副机编号
     * @return int
     * @throws DriversException
     * @Title: returnServoOrigin
     * @Description: 伺服回原点
     */
    @Override
    public int returnServoOrigin(byte slaveID) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar =Dm3ccCmdCharHelper.buildCmdCharPre();
        int rc = 0;
        try {
            cmdChar.modCmdCharBit(3, 4,1);//3.4  伺服使能
            cmdChar.modCmdCharBit(4, 3, 0);
            rc = _write(slaveID, cmdChar,"returnServoOrigin0");
            if(rc == 0){
                cmdChar.modCmdCharBit(4, 3, 1);//4.3 上升沿回原点
                rc = _write(slaveID, cmdChar,"returnServoOrigin1");
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",returnServoOrigin  "+(System.currentTimeMillis() - start)+" ms");
        }

        return rc;
    }

    /**
     * @param slaveID  副机编号
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 运动速度
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @throws DriversException
     * @Title: startServoRun
     * @Description: 伺服点动
     */
    @Override
    public int runServoByJog(byte slaveID, int aspect, int speed, int type) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动
        if(aspect == 0){
            cmdChar.modCmdCharBit(4, 0, 0);//4.0  0，伺服手动正向
        }else{
            cmdChar.modCmdCharBit(4, 0, 1);//4.0  1，伺服手动反向
        }
        cmdChar.modCmdCharBit(4, 1, 1);//4.1  1，伺服手动低速

        cmdChar.modCmdChar(18,((speed>>8)&0xff), ((speed)&0xff));//18  伺服手动运行速度指令2（低速指令）
        //标定状态保持：1-基准点标定 2-基准脉冲数标定 3-格口位置标定
        if(1==type){//
            cmdChar.modCmdCharBit(3, 5, 1);//3.5  1，系统基准点标定
        }else if(2 == type){
            cmdChar.modCmdCharBit(3, 6, 1);//3.6  1，系统基准脉冲数标定
        }else if(3== type){
            cmdChar.modCmdCharBit(3, 7, 1);//3.7  1，系统格口位置标定
        }
        int rc = 0;
        try {
            cmdChar.modCmdCharBit(4, 2, 0);//4.2  先置0
            rc = _write(slaveID, cmdChar,"runServoByJog0");//先设置速度值

            if(rc == 0){
                cmdChar.modCmdCharBit(4, 2, 1);//4.2  1，伺服手动运行
                rc = _write(slaveID, cmdChar,"runServoByJog1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",runServoByJog  "+(System.currentTimeMillis() - start)+" ms");
        }

        return rc;
    }

    /**
     * @param slaveID  副机编号
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return int
     * @throws DriversException
     * @Title: stopServoRun
     * @Description: 伺服点动结束
     */
    @Override
    public int stopServoByJog(byte slaveID, int type) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动
        cmdChar.modCmdCharBit(4, 2, 0);//4.2  1，伺服手动运行
        //标定状态保持：1-基准点标定 2-基准脉冲数标定 3-格口位置标定
        if(1==type){//
            cmdChar.modCmdCharBit(3, 5, 1);//3.5  1，系统基准点标定
        }else if(2 == type){
            cmdChar.modCmdCharBit(3, 6, 1);//3.6  1，系统基准脉冲数标定
        }else if(3== type){
            cmdChar.modCmdCharBit(3, 7, 1);//3.7  1，系统格口位置标定
        }
        int rc = 0;
        try {
            rc = _write(slaveID, cmdChar,"stopServoByJog");
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",stopServoByJog  "+(System.currentTimeMillis() - start)+" ms");

        }
        return rc;
    }

    /**
     * 伺服跑圈 系统基准脉冲数标定环境下有效
     *
     * @param slaveID  副机编号
     * @param iCoil   圈数
     * @param aspect  运动方向 0-正向  1-反向
     * @param speed   伺服速度（高速-转/分钟）
     * @return
     * @throws DriversException
     */
    @Override
    public int runServoByCoil(byte slaveID, int iCoil, int aspect, int speed) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();

        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动
        if(aspect == 0){
            cmdChar.modCmdCharBit(4, 0, 0);//4.0  0，伺服手动正向
        }else{
            cmdChar.modCmdCharBit(4, 0, 1);//4.0  1，伺服手动反向
        }
        cmdChar.modCmdCharBit(4, 1, 0);//4.1  0，伺服手动高速
        cmdChar.modCmdChar(17,((speed>>8)&0xff), ((speed)&0xff));//17  伺服手动运行速度指令2（高速指令）
        cmdChar.modCmdCharBit(3, 6, 1);//3.6  1，系统基准脉冲数标定
        cmdChar.modCmdChar(6,((iCoil>>8)&0xff), (iCoil&0xff));//6 基准脉冲数校准圈数
        int rc = 0;
        try {
            cmdChar.modCmdCharBit(3, 15, 0);//3.15  1，基准脉冲数标定执行跑圈 先回0

            rc = _write(slaveID, cmdChar,"runServoByCoil0");//先设置，后运行
            if(rc == 0){
                //CmdChar cmdCharQry = modbusHelper.read(slaveID, Dm3ccCmdCharHelper.START_ADDRESS_READ, Dm3ccCmdCharHelper.CMD_CHAR_NUM);
                cmdChar.modCmdCharBit(3, 14, 1);//3.14  0，校准圈数无效（10圈），1，有效
                cmdChar.modCmdCharBit(3, 15, 1);//3.15  1，基准脉冲数标定执行跑圈
                rc = _write(slaveID, cmdChar,"runServoByCoil1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",runServoByCoil  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

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
    public int runServoByNext(byte slaveID, int type, int speed)throws DriversException{
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();

        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动
        if(type == 0){
            cmdChar.modCmdCharBit(4, 4, 0);//4.4  0，大格口定位；
        }else{
            cmdChar.modCmdCharBit(4, 4, 1);//4.4  1，小格口定位
        }
        cmdChar.modCmdCharBit(4, 1, 0);//4.1  0，伺服手动高速
        cmdChar.modCmdChar(17,((speed>>8)&0xff), ((speed)&0xff));//17  伺服手动运行速度指令2（高速指令）

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(4, 6, 1);//4.6  全零对正，1，下个格口
            cmdChar.modCmdCharBit(4, 7, 0);//4.7  0-1，运行，先置0
            rc = _write(slaveID, cmdChar,"runServoByNext0");//先设置速度值
            if(rc == 0){
                cmdChar.modCmdCharBit(4, 7, 1);//4.7  1 伺服手动运行
                rc = _write(slaveID, cmdChar,"runServoByNext1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",runServoByNext  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

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
    public int runServoByLast(byte slaveID, int type, int speed)throws DriversException{
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();

        cmdChar.modCmdCharBit(3, 3, 0);//3.3  0，手动；1，自动
        if(type == 0){
            cmdChar.modCmdCharBit(4, 4, 0);//4.4  0，大格口定位；
        }else{
            cmdChar.modCmdCharBit(4, 4, 1);//4.4  1，小格口定位
        }
        cmdChar.modCmdCharBit(4, 1, 0);//4.1  0，伺服手动高速
        cmdChar.modCmdChar(17,((speed>>8)&0xff), ((speed)&0xff));//17  伺服手动运行速度指令2（高速指令）

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(4, 5, 1);//4.5  全零对正，1，上个格口
            cmdChar.modCmdCharBit(4, 7, 0);//4.7  0-1，运行，先置0
            rc = _write(slaveID, cmdChar,"runServoByLast0");//先设置速度值
            if(rc == 0){
                cmdChar.modCmdCharBit(4, 7, 1);//4.7  1 伺服手动运行
                rc = _write(slaveID, cmdChar,"runServoByLast1");//运行
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",runServoByLast  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }
    /**
     * 紧急停车
     *
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    @Override
    public int urgentStopServo(byte slaveID) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 0, 1);//3.0  0，正常；为1，急停，就地停车。
        int rc = 0;
        try {
            rc = _write(slaveID, cmdChar,"urgentStopServo");
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",urgentStopServo  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    /**
     * 急停复位
     *
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    @Override
    public int resetServo4Stop(byte slaveID) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 0, 0);//3.0  0，正常；为1，急停，就地停车。
        cmdChar.modCmdCharBit(3, 1, 1);//3.1  0，正常；1，急停复位。
        cmdChar.modCmdCharBit(3, 2, 1);//3.2  0，正常；1，报警复位
        int rc = 0;
        try {
            rc = _write(slaveID, cmdChar,"resetServo4Stop");

            toggleServoEnable(slaveID, true);//加使能
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",resetServo4Stop  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    /**
     * 报警复位
     *
     * @param slaveID  副机编号
     * @return
     * @throws DriversException
     */
    @Override
    public int resetServo4Error(byte slaveID) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 2, 1);//3.2  0，正常；1，报警复位
        int rc = 0;
        try {
            rc = _write(slaveID, cmdChar,"resetServo4Error");

            toggleServoEnable(slaveID, true);//加使能
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",resetServo4Error  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    /**
     * 设置伺服运行速度-业务运行速度
     * @param slaveID  副机编号
     * @param iSpeed 100~3000
     * @return
     * @throws DriversException
     */
    @Override
    public int setServoSpeed(byte slaveID, int iSpeed)throws DriversException{
        return 0;
    }
    /**
     * 设置基准脉冲-基准点
     *
     * @param slaveID  副机编号
     * @param iPulse
     * @return
     * @throws DriversException
     */
    @Override
    public int setPulse4Base(byte slaveID, int iPulse) throws DriversException {
        long start = System.currentTimeMillis();
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        CmdChar cmdChar = cmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 5, 1);//3.5  1，系统基准点标定
        cmdChar.modCmdChar(5,((iPulse>>8)&0xff), ((iPulse)&0xff));//5  校准脉冲数设置（偏差值，有符号）

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(3, 8, 0);//3.8  1，系统基准点确定
            rc = _write(slaveID, cmdChar,"setPulse4Base0");
            if(rc == 0){
                cmdChar.modCmdCharBit(3, 8, 1);//3.8  1，系统基准点确定
                rc = _write(slaveID, cmdChar,"setPulse4Base1");
            }

        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",setPulse4Base  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    /**
     * 设置基准脉冲-1圈
     *
     * @param slaveID  副机编号
     * @param iPulse
     * @return
     * @throws DriversException
     */
    @Override
    public int setPulse4Coil(byte slaveID, int iPulse) throws DriversException {
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 6, 1);//3.6  1，系统基准脉冲数标定
        cmdChar.modCmdChar(5,((iPulse>>8)&0xff), ((iPulse)&0xff));//5  校准脉冲数设置（偏差值，有符号）

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(3, 9, 0);//3.9  1，系统基准脉冲数确定
            rc = _write(slaveID, cmdChar,"setPulse4Coil0");
            if(rc == 0){
                cmdChar.modCmdCharBit(3, 9, 1);//3.9  1，系统基准脉冲数确定
                rc = _write(slaveID, cmdChar,"setPulse4Coil1");
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",setPulse4Coil  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }
    /**
     * 设置基准脉冲-当前位置
     * @param slaveID  副机编号
     * @param iPulse
     * @return
     * @throws DriversException
     */
    @Override
    public int setPulse4Position(byte slaveID, int iPulse)throws DriversException{
        long start = System.currentTimeMillis();
        CmdChar cmdChar = Dm3ccCmdCharHelper.buildCmdCharPre();
        cmdChar.modCmdCharBit(3, 7, 1);//3.7  1，格口位置标定
        cmdChar.modCmdChar(5,((iPulse>>8)&0xff), ((iPulse)&0xff));//5  校准脉冲数设置（偏差值，有符号）

        int rc = 0;
        try {
            cmdChar.modCmdCharBit(3, 10, 0);//3.10  1，系统格口位置确定
            rc = _write(slaveID, cmdChar, "setPulse4Position0");
            if(rc == 0){
                cmdChar.modCmdCharBit(3, 10, 1);//3.10  1，系统格口位置确定
                rc = _write(slaveID, cmdChar, "setPulse4Position1");
            }
        } catch (ModbusException e) {
            //e.printStackTrace();
            throw new DriversException(DriversErrorCode.ERR_PD_COMMUNICATION, e.getMessage());
        }finally {
            log.debug("slaveID="+slaveID+",setPulse4Position  "+(System.currentTimeMillis() - start)+" ms");
        }
        return rc;
    }

    private ServoStatus _getServoStatus(Dm3ccCmdCharHelper cmdCharHelper) throws DriversException{
        ServoStatus servoStatus = new ServoStatus();
        servoStatus.setEnableStatus(cmdCharHelper.getEnableStatus());
        servoStatus.setOriginStatus(cmdCharHelper.getOriginStatus());
        servoStatus.setRunForward(cmdCharHelper.getRunForward());
        servoStatus.setRunMode(cmdCharHelper.getRunMode());
        servoStatus.setRunStatus(cmdCharHelper.getRunStatus());
        servoStatus.setCorrectCoil(cmdCharHelper.getCorrectCoil());
        servoStatus.setCorrectingType(cmdCharHelper.getCorrectingStatus());
        servoStatus.setCorrectPulse(cmdCharHelper.getCorrectPulse());
        servoStatus.setCurrentPos(cmdCharHelper.getCurrentPos());
        servoStatus.setTargetPos(cmdCharHelper.getTargetPos());
        servoStatus.setTotalCoil(cmdCharHelper.getTotalCoil());
        servoStatus.setTotalPulse(cmdCharHelper.getTotalPulse());
        servoStatus.setServoErrorCode(cmdCharHelper.getServoErrorCode());

        servoStatus.setUrgentStopReset(cmdCharHelper.getUrgentStopReset());
        servoStatus.setErrorReset(cmdCharHelper.getErrorReset());
        servoStatus.setRunErrorCode1(cmdCharHelper.getRunErrorCode1());
        servoStatus.setRunErrorCode2(cmdCharHelper.getRunErrorCode2());
        servoStatus.setBusErrorCode(cmdCharHelper.getBusErrorCode());

        servoStatus.setCmdChar(cmdCharHelper.getCmdChar());
        return servoStatus;
    }
    /**
     * @param slaveID  副机编号 1~
     * @return String
     * @throws DriversException
     * @Title: getServoStatus
     * @Description: 伺服回原点
     */
    @Override
    public ServoStatus getServoStatus(byte slaveID) throws DriversException {
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, _readFast(slaveID));
        return _getServoStatus(cmdCharHelper);
    }
    //#endregion

    /**
     * @param slaveID  副机编号 1~
     * @return
     */
    private CmdChar _readFast(byte slaveID){//读缓存数据
        return modbusHelper.readFast(slaveID, Dm3ccCmdCharHelper.START_ADDRESS_READ, Dm3ccCmdCharHelper.CMD_CHAR_NUM);
    }
    /**
     * @param slaveID  副机编号 1~
     * @return
     */
    private CmdChar _read(byte slaveID){//读实时数据-更耗时
        return modbusHelper.read(slaveID,Dm3ccCmdCharHelper.START_ADDRESS_READ, Dm3ccCmdCharHelper.CMD_CHAR_NUM);
    }

    /**
     *
     * @param slaveID 副机编号 1~
     * @param wCmdChar
     * @return
     * @throws ModbusException
     */
    private int _write(byte slaveID, CmdChar wCmdChar, String funcName)throws ModbusException {
        long start = System.currentTimeMillis();
        int rc = modbusHelper.write(slaveID, wCmdChar);
        log.debug("funcName="+funcName+",slaveID="+slaveID+",write["+wCmdChar.getCmdCharString()+"],rc="+rc+","+(System.currentTimeMillis() - start)+" ms");
        return rc;
    }
}
