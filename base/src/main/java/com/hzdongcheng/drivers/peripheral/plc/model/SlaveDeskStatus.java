package com.hzdongcheng.drivers.peripheral.plc.model;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 副机状态
 * Created by Administrator on 2018/4/13.
 */

public class SlaveDeskStatus implements java.io.Serializable{
    private int slaveStatus;//0-正常  1-报警 2-故障
    private int errorCode;//错误码 0-正常
    private String errorMsg;//错误信息
    private int runStatus;//运动状态：0-停止，1- 运动
    private int repairDoorStatus;//维护门状态 0-门关 1-门开
    private int autoDoorStatus;//自动门状态 0-门关 1-门开
    private int manualDoorStatus;//手动门状态 0-门关 1-门开
    private int rasterStatus;//光栅状态 0-正常，1-有物遮挡
    private int shockStatus;//震动状态 0-正常 1-震动报警
    private int powerSupply;//供电方式 0-市电 1-UPS
    private int currentPos;//取货口正对货架的位置
    private Map<String, ShelfBoxStatus> boxStatusMap;//货格状态 layer:status
    private Set<Integer> layerSet;//

    public SlaveDeskStatus(){
        boxStatusMap = new ConcurrentHashMap();
        layerSet = new java.util.HashSet<>();
        slaveStatus = 0;
        errorCode = 0;
        errorMsg = "";
        runStatus = 9;
        repairDoorStatus = 9;
        autoDoorStatus = 9;
        manualDoorStatus = 9;
        rasterStatus = 9;
        shockStatus = 9;
        powerSupply = 9;
    }

    public int getBoxCount(){
        return boxStatusMap==null?0:boxStatusMap.size();
    }
    public int getLayerCount(){
        return layerSet == null?0:layerSet.size();
    }

    public void addBoxStatus(int ofLayer, ShelfBoxStatus boxStatus){
        String key = ""+ofLayer;
        layerSet.add(ofLayer);
        boxStatusMap.put(key,boxStatus);
    }
    public ShelfBoxStatus getBoxStatus(int ofLayer){
        String key = ""+ofLayer;
        return boxStatusMap.get(key);
    }

    public int getSlaveStatus() {
        return slaveStatus;
    }

    public void setSlaveStatus(int slaveStatus) {
        this.slaveStatus = slaveStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }

    public int getRepairDoorStatus() {
        return repairDoorStatus;
    }

    public void setRepairDoorStatus(int repairDoorStatus) {
        this.repairDoorStatus = repairDoorStatus;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public Map<String, ShelfBoxStatus> getBoxStatusMap() {
        return boxStatusMap;
    }

    public void setBoxStatusMap(Map<String, ShelfBoxStatus> boxStatusMap) {
        this.boxStatusMap = boxStatusMap;
    }

    public Set<Integer> getLayerSet() {
        return layerSet;
    }

    public void setLayerSet(Set<Integer> layerSet) {
        this.layerSet = layerSet;
    }

    public int getAutoDoorStatus() {
        return autoDoorStatus;
    }

    public void setAutoDoorStatus(int autoDoorStatus) {
        this.autoDoorStatus = autoDoorStatus;
    }

    public int getManualDoorStatus() {
        return manualDoorStatus;
    }

    public void setManualDoorStatus(int manualDoorStatus) {
        this.manualDoorStatus = manualDoorStatus;
    }

    public int getRasterStatus() {
        return rasterStatus;
    }

    public void setRasterStatus(int rasterStatus) {
        this.rasterStatus = rasterStatus;
    }

    public int getShockStatus() {
        return shockStatus;
    }

    public void setShockStatus(int shockStatus) {
        this.shockStatus = shockStatus;
    }

    public int getPowerSupply() {
        return powerSupply;
    }

    public void setPowerSupply(int powerSupply) {
        this.powerSupply = powerSupply;
    }
}
