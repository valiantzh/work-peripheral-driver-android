package com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl;

import com.hzdongcheng.drivers.base.modbus.helper.CmdChar;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * DM3CC命令解析帮助类,实现：
 * 1、构造发送的命令字；
 * 2、解析收到的命令字
 *
 * Created by Administrator on 2018/4/16.
 */

public class Dm3ccCmdCharHelper {
    public final static int START_ADDRESS_READ  = 39207;//39168;//H 99 27
    public final static int START_ADDRESS_WRITE = 39168;//H 99 00
    public final static int CMD_CHAR_NUM = 40;
    public final static int MASTER_ID = 1;//主柜编号



    final static int MIN_SERVO_SPEED_LOW = 10;
    final static int MAX_SERVO_SPEED_LOW = 1000;
    final static int MIN_SERVO_SPEED_HIGH = 100;
    final static int MAX_SERVO_SPEED_HIGH = 3000;
    final static int DEFAULT_SERVO_SPEED_HIGH = 2500;//默认伺服速度-高速 转/分钟
    final static int DEFAULT_SERVO_SPEED_LOW = 100;//默认伺服速度-低速 转/分钟

    final static short GAP_BOX_NO = 20;//大格口  1~15 小格口为21~40
    final static int MAX_LAYER_NUM = 6;//最多6层
    final static int INVALID_LAYER_NO = 4;//
    final static int MASTER_INSIDE_SWITCH = 0;//内部层转换 0-不用转换 1-转换、跳过无效层
    final static int MAX_COL_NUM = 20; //最多20列

    static Map<Byte,Integer> servoSpeedMap = new HashMap<Byte,Integer>();

    private CmdChar rCmdChar;
    private byte slaveID;
    public Dm3ccCmdCharHelper(byte slaveID, CmdChar rCmdChar){
        //this.rCmdChar = new CmdChar(START_ADDRESS_READ, CMD_CHAR_NUM);
        this.rCmdChar = rCmdChar;
        this.slaveID = slaveID;
    }
    /**
     * 设置解析收到的命令字（读数据）
     * @param rCmdChar
     */
    public void setRCmdChar(byte slaveID, CmdChar rCmdChar) {
        this.rCmdChar = rCmdChar;
        this.slaveID = slaveID;
    }

    /**
     * 构造发送的命令字(写数据)
     * @return
     */
    public static CmdChar buildCmdCharPre(){
        CmdChar wCmdChar = new CmdChar(START_ADDRESS_WRITE, CMD_CHAR_NUM);

        wCmdChar.modCmdCharBit(3,4);//3.4 加使能
        wCmdChar.modCmdChar(18,((DEFAULT_SERVO_SPEED_LOW>>8)&0xff), ((DEFAULT_SERVO_SPEED_LOW)&0xff));//18  伺服手动运行速度指令2（低速指令）
        wCmdChar.modCmdChar(17,((DEFAULT_SERVO_SPEED_HIGH>>8)&0xff), ((DEFAULT_SERVO_SPEED_HIGH)&0xff));//17  伺服手动运行速度指令2（高速指令）
        //心跳识别
        wCmdChar.modCmdChar(40,(byte)0, (byte)1);
        return wCmdChar;
    }

    //#region 内部转换
    /**
     * 外部层数转换为内部层数
     * @param slaveID  副机编号
     * @param ofLayer
     * @return
     */
    public static int _getInsideLayerNo(byte slaveID, int ofLayer){
        if(MASTER_INSIDE_SWITCH == 1){
            if(slaveID == MASTER_ID){//主柜内部有一层无效  ？？？？？
                if(ofLayer>=INVALID_LAYER_NO){
                    return (ofLayer +1);//跳过无效层
                }
            }
        }


        return ofLayer;
    }
    /**
     * 内部层数转换为外部层数
     * @param slaveID  副机编号
     * @param iLayer
     * @return
     */
    public static int _getOutsideLayerNo(byte slaveID, int iLayer){
        if(MASTER_INSIDE_SWITCH == 1){
            if(slaveID == MASTER_ID){//主柜内部有一层无效  ？？？？？
                if(iLayer ==INVALID_LAYER_NO){
                    return -1;//跳过无效层
                }else  if(iLayer>INVALID_LAYER_NO){
                    return (iLayer -1);//跳过无效层
                }
            }
        }
        return iLayer;
    }

    /**
     * 获取PLC内部定义格口目标位置
     * @param slaveID
     * @param ofLayer
     * @param ofCol
     * @return
     */
    public static int _getInsideTargetPos(byte slaveID, int ofLayer, int ofCol){
        int iLayer = _getInsideLayerNo(slaveID, ofLayer);//内部层号
        if(iLayer== 0 || iLayer==5) {//第1层 和第6层为大格口
            return ofCol;//大格口 1~15
        }else{
            return 20+ofCol;//小格口 21~40
        }
    }

    /**
     * 获取PLC内部定义的格口类型定位：
     * @param slaveID
     * @param ofLayer
     * @return 0，大格口定位；1，小格口定位
     */
    public static int _getInsideBoxType(byte slaveID, int ofLayer){
        int iLayer = _getInsideLayerNo(slaveID, ofLayer);//内部层号
        if(iLayer== 0 || iLayer==5) {//第1层 和第6层为大格口
            return 0;//7.0  0，大格口定位；1，小格口定位(自动）
        }else{
            return 1;
        }
    }

    /**
     * 获取PLC内部定义的门编号
     * @param slaveID
     * @param ofLayer
     * @return
     */
    public static int _getInsideDoorNo(byte slaveID, int ofLayer){
        return _getInsideLayerNo(slaveID, ofLayer);//内部层号
    }
    //#endregion


    /**
     * 获取伺服运行速度
     * @param slaveID  副机编号
     * @return
     */
    public static int getServoSpeed(byte slaveID){
        Integer speed = servoSpeedMap.get(slaveID);
        int servoSpeed = 0;
        if(speed == null){
            servoSpeed = DEFAULT_SERVO_SPEED_HIGH;
        }else{
            servoSpeed = speed.intValue();
        }
        if(servoSpeed< MIN_SERVO_SPEED_HIGH){
            servoSpeed = MIN_SERVO_SPEED_HIGH;
        }else if(servoSpeed>MAX_SERVO_SPEED_HIGH){
            servoSpeed = MAX_SERVO_SPEED_HIGH;
        }
        return servoSpeed;
    }

    /**
     * 设置伺服运行速度
     * @param slaveID  副机编号
     * @param servoSpeed
     */
    public static void setServoSpeed(byte slaveID,int servoSpeed) {
        if(servoSpeed< MIN_SERVO_SPEED_HIGH){
            servoSpeed = MIN_SERVO_SPEED_HIGH;
        }else if(servoSpeed>MAX_SERVO_SPEED_HIGH){
            servoSpeed = MAX_SERVO_SPEED_HIGH;
        }
        servoSpeedMap.put(slaveID, servoSpeed);
    }

    //#region 命令解析
    public char[] getCmdChar(){
        return rCmdChar.getCmdChars();
    }

    public byte getBiteValue(int iChar, int iBit){
        return rCmdChar ==null?9: rCmdChar.getBitValue(iChar, iBit);
    }

    /**
     *4.0  紧急停车中
     * @return
     */
    public byte getUrgentStopStatus() {
        urgentStopStatus =  rCmdChar ==null?9: rCmdChar.getBitValue(4, 0);
        return urgentStopStatus;
    }

    /**
     * 4.1   急停复位
     * @return
     */
    public byte getUrgentStopReset() {
        urgentStopReset = rCmdChar ==null?9: rCmdChar.getBitValue(4, 1);
        return urgentStopReset;
    }

    /**
     * 4.2 报警复位
     * @return
     */
    public byte getErrorReset() {
        errorReset = rCmdChar ==null?9: rCmdChar.getBitValue(4, 2);
        return errorReset;
    }

    /**
     * 4.3 手动 自动
     * @return
     */
    public byte getWorkMode() {
        workMode = rCmdChar ==null?9: rCmdChar.getBitValue(4, 3);
        return workMode;
    }

    /**
     * 4.4伺服使能状态 0:伺服无使能 1:伺服有使能
     * @return
     */
    public byte getEnableStatus() {
        enableStatus = rCmdChar ==null?9: rCmdChar.getBitValue(4, 4);
        return enableStatus;
    }


    /**
     *  4.5~4.7
     * @return 1-系统基准点标定中 2-基准脉冲数标定中 3-格口位置标定中
     */
    public byte getCorrectingStatus() {
        if(rCmdChar !=null){
            byte status1 = rCmdChar.getBitValue(4, 5);//4.5  系统基准点标定中
            byte status2 = rCmdChar.getBitValue(4, 6);//4.6  基准脉冲数标定中
            byte status3 = rCmdChar.getBitValue(4, 7);//4.7  格口位置标定中
            if(status1 == 1){
                correctingStatus = 1;
            }else if(status2 == 1){
                correctingStatus = 2;
            }else if(status3 ==1){
                correctingStatus = 3;
            }
        }

        return correctingStatus;
    }

    /**
     * 4.8~4.11
     * @return  1-基准点确认完成 2-基准脉冲数确认完成 3-格口位置确认完成  4-恢复出厂设置完成
     */
    public byte getCorrectedStatus() {
        if(rCmdChar !=null){
            byte status1 = rCmdChar.getBitValue(4, 8);//4.8    基准点确认完成
            byte status2 = rCmdChar.getBitValue(4, 9);//4.9    基准脉冲数确认完成
            byte status3 = rCmdChar.getBitValue(4, 10);//4.10  格口位置确认完成
            byte status4 = rCmdChar.getBitValue(4, 11);//4.11  恢复出厂设置完成
            if(status1 == 1){
                correctedStatus = 1;
            }else if(status2 == 1){
                correctedStatus = 2;
            }else if(status3 ==1){
                correctedStatus = 3;
            }else if(status4==1){
                correctedStatus = 4;
            }
        }

        return correctedStatus;
    }

    /**
     * 5.0 伺服运行方向 0，伺服正向；1，伺服反向
     * @return
     */
    public byte getRunForward() {
        this.runForward = rCmdChar ==null?9: rCmdChar.getBitValue(5, 0);
        return runForward;
    }

    /**
     * 5.1 伺服运行模式状态. 0:高速 1:低速
     * @return
     */
    public byte getRunMode() {
        this.runMode = rCmdChar ==null?9: rCmdChar.getBitValue(5, 1);
        return runMode;
    }

    /**
     * 5.2 伺服运行状态. 0:伺服停止 1:伺服运行中
     * @return
     */
    public byte getRunStatus() {
        this.runStatus = rCmdChar ==null?9: rCmdChar.getBitValue(5, 2);
        return runStatus;
    }

    /**
     * 5.3 伺服原点状态. 0:伺服无原点 1:伺服有原点
     * 5.7              0，未定义；1，伺服回原点中
     * @return 0:伺服无原点 1:伺服有原点 2:回原点中
     */
    public byte getOriginStatus() {
        this.originStatus = rCmdChar ==null?9: rCmdChar.getBitValue(5, 3);
        if(0 == this.originStatus){
            this.originStatus = rCmdChar.getBitValue(5, 3)==1?2:this.originStatus;
        }
        return originStatus;
    }

    /**
     * 5.4 0，手动大格口定位；1，手动小格口定位
     * @return
     */
    public byte getManualBoxType() {
        this.manualBoxType = rCmdChar ==null?9: rCmdChar.getBitValue(5, 4);
        return manualBoxType;
    }

    /**
     * 货格到达存取口 0:不在存取口 1:在
     * @param iLayer
     * @param iCol
     * @return
     */
    public byte isReachDoor(int iLayer,int iCol){
        int targetPos;
        if(0 == getRunStatus()){//伺服停止状态下，查询对正状态
            int isReach = getBiteValue(14, 4);//14.4  1，未对正任何格口
            if(isReach == 0){
                if(iLayer == 0 || iLayer == 5){//大格口
                    targetPos = iCol;
                }else{//小格口
                    targetPos = iCol+20;
                }
                if(targetPos  == getCurrentPos()){
                    return 1;
                }
            }
        }
        return 0;

    }
    /**
     * 5.8~5.15   门状态 0-门关 1门开 9未知
     * @param iDoor 0~
     * @return
     */
    public byte getDoorStatus(int iDoor) {

        if(iDoor>=0){
            doorStatus = rCmdChar ==null?9: rCmdChar.getBitValue(5, 8+iDoor);
        }
        return doorStatus;
    }

    /**
     * 维护门状态 0-门关 1门开 9未知
     * @return
     */
    public byte getRepairDoorStatus(){
        return rCmdChar ==null?9: rCmdChar.getBitValue(5, 15);//5.15
    }

    /**
     * 6 校准脉冲数反馈
     * @return
     */
    public int getCorrectPulse() {
        this.correctPulse = rCmdChar ==null?0: rCmdChar.getShortValue(6);
        return correctPulse;
    }

    /**
     * 7 基准脉冲数校准圈数
     * @return
     */
    public int getCorrectCoil() {
        this.correctCoil = rCmdChar ==null?0: rCmdChar.getShortValue(7);
        return correctCoil;
    }

    /**
     * 8 当前位置
     * @return
     */
    public int getCurrentPos() {
        this.currentPos = rCmdChar ==null?0: rCmdChar.getShortValue(8);
        return currentPos;
    }

    /**
     * 9 目标位置
     * @return
     */
    public int getTargetPos() {
        this.targetPos = rCmdChar ==null?0: rCmdChar.getShortValue(9);
        return targetPos;
    }

    /**
     * 10 物品状态
     * @param ofLayer 0~
     * @param ofCol 1~
     * @return 0:无物 1:有物 2: 无物光幕被挡 3：有物光幕被挡
     */
    public byte getGoodsStatus(int ofLayer, int ofCol) {
        if(rCmdChar !=null){
            if(1 == isReachDoor(ofLayer, ofCol)){
                if(1 == getRasterStatus()){//光幕被挡
                    int status = rCmdChar.getBitValue(10, ofLayer)+2;
                    return (byte)status;
                }else{
                    return rCmdChar.getBitValue(10, ofLayer);
                }

            }
        }
        return 9;
    }

    /**
     * 11 报警代码（伺服）
     * @return
     */
    public char getServoErrorCode() {
        this.servoErrorCode = rCmdChar ==null?0: rCmdChar.getCmdChar(11);
        return servoErrorCode;
    }

    /**
     * 12Ι类报警代码
     * @return
     */
    public char getRunErrorCode1() {
        this.runErrorCode1 = rCmdChar ==null?0: rCmdChar.getCmdChar(12);
        return runErrorCode1;
    }
    /**
     * 13 Π类报警代码
     * @return
     */
    public char getRunErrorCode2() {
        this.runErrorCode2 = rCmdChar ==null?0: rCmdChar.getCmdChar(13);
        return runErrorCode2;
    }
    /**
     * 12Ι类报警代码
     * 13 Π类报警代码
     * @param iBit 0~31
     * @return
     */
    public int getRunErrorCode(int iBit) {
        if(rCmdChar ==null || iBit<0 || iBit>31){
            return 0;
        }
        int iChar =12;
        if(iBit>15){
            iBit = iBit-16;
            iChar = 13;
        }
        return rCmdChar.getBitValue(iChar, iBit);
    }

    /**
     * 14 15 业务错误提示代码
     * @return
     */
    public int getBusErrorCode() {
        this.busErrorCode = rCmdChar ==null?0: rCmdChar.getIntValue(14);
        return busErrorCode;
    }

    /**
     * 14 15 业务错误提示代码
     * @param iBit 0~31
     * @return
     */
    public int getBusErrorCode(int iBit) {
        if(rCmdChar ==null|| iBit<0 || iBit>31){
            return 0;
        }
        int iChar =14;
        if(iBit>15){
            iBit = iBit-16;
            iChar = 15;
        }
        return rCmdChar.getBitValue(iChar, iBit);
    }

    /**
     * 14.0 1,光幕被挡
     * @return
     */
    public int getRasterStatus(){
        return getBusErrorCode(0);
    }

    /**
     * 16 伺服转过脉冲数
     * @return
     */
    public int getTotalPulse() {
        this.totalPulse = rCmdChar ==null?0: rCmdChar.getShortValue(16);
        return totalPulse;
    }

    /**
     * 17 18 伺服转过圈数
     * @return
     */
    public int getTotalCoil() {
        this.totalCoil = rCmdChar ==null?0: rCmdChar.getIntValue(17);
        return totalCoil;
    }

    /**
     * 19 伺服运行速度
     * @return
     */
    public int getRunSpeed() {
        this.runSpeed = rCmdChar ==null?0: rCmdChar.getShortValue(19);
        return runSpeed;
    }

    /**
     * 20 盘库状态 0未盘库 1-盘库中 2-盘库完成 3-盘库中断
     * @return
     */
    public byte getCheckStatus() {
        if(rCmdChar != null){
            if(1== rCmdChar.getBitValue(20, 5)){//20.5  1，盘库被中断
                checkStatus = 3;
            }else if(1== rCmdChar.getBitValue(20, 1)){//20.1 1，盘库结束，数据已输出
                checkStatus = 2;
            }else if(1== rCmdChar.getBitValue(20, 0)){//20.0 1，盘库中
                checkStatus = 1;
            }
        }
        return checkStatus;
    }

    /**
     * 21 盘库令牌
     * @return
     */
    public char getCheckToken() {
        checkToken = rCmdChar ==null?0: rCmdChar.getCharValue(21);
        return checkToken;
    }

    /**
     * 第22~33字  盘点结果物品状态
     * @return
     */
    public byte[][] getCheckGoodsStatus() {
        if(rCmdChar == null){
            return null;
        }
        checkGoodsStatus = new byte[MAX_LAYER_NUM][MAX_COL_NUM];
        for(int i=0; i< MAX_LAYER_NUM ; i++){
            int iChar = 22 + i*2;//双字
            int iData = rCmdChar.getIntValue(iChar);
            int ofLayer = _getOutsideLayerNo(this.slaveID, i);
            if(ofLayer >= 0){//跳过无效层

                if(iData != 0){
                    for(int j=0; j<MAX_COL_NUM; j++ ){
                        byte value = (byte)((iData>>j)&0x1);
                        checkGoodsStatus[ofLayer][j] =value;
                    }
                }
            }
        }
        return checkGoodsStatus;
    }
    //#endregion

    //#region 命令字
    //第4字
    private byte urgentStopStatus;//4.0  紧急停车中
    private byte urgentStopReset;//4.1   急停复位
    private byte errorReset;//4.2
    private byte workMode;//4.3 手动 自动
    private byte enableStatus;//4.4伺服使能状态 0:伺服无使能 1:伺服有使能
    private byte correctingStatus;//4.5~4.7  1-系统基准点标定中 2-基准脉冲数标定中 3-格口位置标定中
    private byte correctedStatus;//4.8~4.11  1-基准点确认完成 2-基准脉冲数确认完成 3-格口位置确认完成  4-恢复出厂设置完成

    //第5字
    private byte runForward;//5.0 伺服运行方向 0，伺服正向；1，伺服反向
    private byte runMode;//5.1 伺服运行模式状态. 0:高速 1:低速
    private byte runStatus;//5.2 伺服运行状态. 0:伺服停止 1:伺服运行中
    private byte originStatus;//5.3 伺服原点状态. 0:伺服无原点 1:伺服有原点
    private byte manualBoxType;//5.4 0，手动大格口定位；1，手动小格口定位
    private byte doorStatus;//5.8~5.15   门状态 0-门关 1门开

    //第6字
    private int correctPulse;//校准脉冲数
    //第7字
    private int correctCoil;//数校准圈数
    //第8字
    private int currentPos;//当前位置
    //第9字
    private int targetPos;//目标位置
    //第10字
    private char goodsStatus;//物品状态
    //第11字
    private char servoErrorCode;//报警代码（伺服）
    //第12字
    private char runErrorCode1;//Ι类报警代码
    //第13字
    private char runErrorCode2;//Π类报警代码
    //第14 15字
    private int busErrorCode;//业务错误提示代码
    //第16字
    private int totalPulse;//伺服转过脉冲数
    //第17 18字
    private int totalCoil;//伺服转过圈数
    //第19字
    private int runSpeed;//伺服运行速度

    //第20字
    private byte checkStatus;//盘库状态 0未盘库 1-盘库中 2-盘库完成 3-盘库中断
    //第21字
    private char checkToken;//
    //第22~33字
    private byte[][] checkGoodsStatus;//盘点结果物品状态
    //#endregion

    @Override
    public String toString() {
        return "Dm3ccCmdCharHelper{" +
                "urgentStopStatus=" + this.getUrgentStopStatus() +
                ", urgentStopReset=" + this.getUrgentStopReset() +
                ", errorReset=" + this.getErrorReset() +
                ", workMode=" + this.getWorkMode() +
                ", enableStatus=" + this.getEnableStatus() +
                ", correctingStatus=" + this.getCorrectingStatus() +
                ", correctedStatus=" + this.getCorrectedStatus() +
                ", runForward=" + this.getRunForward() +
                ", runMode=" + this.getRunMode() +
                ", runStatus=" + this.getRunStatus() +
                ", originStatus=" + this.getOriginStatus() +
                ", manualBoxType=" + this.getManualBoxType() +
                ", doorStatus=" + doorStatus +
                ", correctPulse=" + this.getCorrectPulse() +
                ", correctCoil=" + this.getCorrectCoil() +
                ", currentPos=" + this.getCurrentPos() +
                ", targetPos=" + this.getTargetPos() +
                ", goodsStatus=" + this.goodsStatus +
                ", servoErrorCode=" + this.getServoErrorCode() +
                ", runErrorCode1=" + this.getRunErrorCode1() +
                ", runErrorCode2=" + this.getRunErrorCode2() +
                ", busErrorCode=" + this.getBusErrorCode() +
                ", totalPulse=" + this.getTotalPulse() +
                ", totalCoil=" + this.getTotalCoil() +
                ", runSpeed=" + this.getRunSpeed() +
                ", checkStatus=" + this.getCheckStatus() +
                ", checkToken=" + this.getCheckToken() +
                ", checkGoodsStatus=" + Arrays.toString(this.getCheckGoodsStatus()) +
                '}';
    }
}
