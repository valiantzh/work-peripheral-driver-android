package com.hzdongcheng.drivers.peripheral.plc.model;

import java.util.Arrays;

/**
 * 盘库结果
 * Created by Administrator on 2018/4/15.
 */

public class CheckResult {
    private byte checkStatus;//盘库是否完成
    private int errorCode;//盘库结果，0 无异常
    private int checkToken;//
    private byte[][] goodsStatus;
    public CheckResult(){
    }

    public void setCheckStatus(byte checkStatus) {
        this.checkStatus = checkStatus;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setGoodsStatus(byte[][] goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    /**
     *
     * @return 0未盘库 1-盘库中 2-盘库完成 3-盘库中断
     */
   public byte getCheckStatus() {
        return checkStatus;
   }

    /**
     *
     * @param ofLayer 0~
     * @param ofCol  1~
     * @return 0- 无物 1-有物 9-未知
     */
    public byte getGoodsStatus(int ofLayer, int ofCol){
        int layerNum = getLayerNum();
        ofCol = ofCol-1;//1~
        if(ofLayer>=0 && ofCol>=0){
            if(layerNum>0&& ofLayer<layerNum){
                int colNum = getColNum(ofLayer);
                if(colNum>0 && ofCol< colNum){
                    return goodsStatus[ofLayer][ofCol];
                }
            }
        }

        return 9;
    }

    public int getLayerNum(){
        return this.goodsStatus==null?0:this.goodsStatus.length;
    }
    public int getColNum(int ofLayer){
        int layerNum = getLayerNum();
        if(layerNum>0&&ofLayer>=0 &&ofLayer<layerNum){
            return this.goodsStatus[ofLayer].length;
        }

        return 0;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getCheckToken() {
        return checkToken;
    }

    public void setCheckToken(int checkToken) {
        this.checkToken = checkToken;
    }

    public byte[][] getGoodsStatus() {
        return goodsStatus;
    }


    @Override
    public String toString() {
        return "CheckResult{" +
                "checkStatus=" + checkStatus +
                ", errorCode=" + errorCode +
                ", goodsStatus=" + Arrays.toString(goodsStatus) +
                '}';
    }
}
