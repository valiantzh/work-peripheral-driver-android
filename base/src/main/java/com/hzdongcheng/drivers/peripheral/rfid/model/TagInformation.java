package com.hzdongcheng.drivers.peripheral.rfid.model;

/**
 * @ClassName: TagInformation
 * @Description: RFID标签信息
 * @author Yihang
 * @date 2018-09-29
 */

public class TagInformation {

    private String EPCcode = ""; //EPC编码
    private int antennaNo = 0; //天线号
    private int startTime = 0; //开始时间 毫秒计数

    public String getEPCcode() {
        return EPCcode;
    }

    public void setEPCcode(String EPCcode) {
        this.EPCcode = EPCcode;
    }

    public int getAntennaNo() {
        return antennaNo;
    }

    public void setAntennaNo(int antennaNo) {
        this.antennaNo = antennaNo;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public String toString() {
        return "\nEPC="+EPCcode+"\nAntennaNo="+antennaNo+"\nStartTime="+startTime+"\n\n";
    }
}
