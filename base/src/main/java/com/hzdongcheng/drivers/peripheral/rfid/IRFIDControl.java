package com.hzdongcheng.drivers.peripheral.rfid;

import com.hzdongcheng.drivers.base.serialport.ISerialPortDevice;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.rfid.model.AntennaParam;
import com.hzdongcheng.drivers.peripheral.rfid.model.TagInformation;

import java.util.List;

public interface IRFIDControl extends ISerialPortDevice {

    /**
     *
     * @Title: cancel
     * @Description: 发送取消操作指令。当需要停止一个连续性操作时需要发送该指令，如停止一次连续性盘点操作。
     * @param
     * @return void 返回类型
     * @throws DriversException
     */
    void cancel() throws DriversException;

    /**
     *
     * @Title: reset
     * @Description: 重置操作指令
     * @param
     * @return void 返回类型
     * @throws DriversException
     */
    void reset() throws DriversException;

    /**
     * @Title: abort
     * @Description: 中止操作指令。该操作会促使其他操作被强制中止，且不会回馈原操作相关的回馈信息。
     * @param
     * @return void 返回类型
     * @throws DriversException
     */
    void abort() throws DriversException;

    /**
     * @Title: pause
     * @Description: 暂停操作指令
     * @param
     * @return void 返回类型
     * @throws DriversException
     */
    void pause() throws DriversException;

    /**
     * @Title: resume
     * @Description: 恢复操作指令
     * @param
     * @return void 返回类型
     * @throws DriversException
     */
    void resume() throws DriversException;

    /**
     * @Title: readTagInfo
     * @Description: 标签盘点操作
     * @param
     * @return List<TagInformation>
     * @throws DriversException
     */
    List<TagInformation> readTagInfo() throws DriversException;

    /**
     * @Title: getAntennaStatus
     * @Description: 获取天线状态指令
     * @param antennaNo 天线号
     * @return 0-关闭 1-打开
     * @throws DriversException
     */
    int getAntennaStatus(int antennaNo) throws DriversException;

    /**
     * @Title: setAntennaStatus
     * @Description: 设置天线状态指令
     * @param antennaNo 天线号
     * @param status 天线状态 0-关闭 1-打开
     * @return true-成功 false-失败
     * @throws DriversException
     */
    boolean setAntennaStatus(int antennaNo, int status) throws DriversException;

    /**
     * @Title: getAntennaParam
     * @Description: 获取天线配置信息指令
     * @param antennaNo 天线号
     * @return AntennaParam 天线配置
     * @throws DriversException
     */
    AntennaParam getAntennaParam(int antennaNo) throws DriversException;

    /**
     * @Title: setAntennaParam
     * @Description: 设置天线配置信息指令
     * @param antennaParam 天线配置
     * @return true-成功 false-失败
     * @throws DriversException
     */
    boolean setAntennaParam(AntennaParam antennaParam) throws DriversException;
}
