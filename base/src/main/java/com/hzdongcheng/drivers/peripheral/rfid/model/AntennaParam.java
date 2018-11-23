package com.hzdongcheng.drivers.peripheral.rfid.model;


/**
 * @ClassName: AntennaParam
 * @Description: RFID天线参数
 * @author Yihang
 * @date 2018-09-29
 */

public class AntennaParam {

    private int index = 0; //天线号
    private int power = 0;  //天线功率 3-32dbm
    private int stayTime = 0;  //驻留时间 ms
    private int period = 0;  //盘讯时间

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getStayTime() {
        return stayTime;
    }

    public void setStayTime(int stayTime) {
        this.stayTime = stayTime;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String toString() {
        return "\nAntennaNo="+index+"\nPower="+power+"\nStayTime="+stayTime+"\nPeriod="+period+"\n\n";
    }
}
