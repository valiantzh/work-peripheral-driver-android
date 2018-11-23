package com.hzdongcheng.drivers.base.modbus;

import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;


/**
 * modbus 通讯设备接口
 * Created by Administrator on 2018/4/2.
 */

public interface IModbusDevice {
    //连接串口
    void connect(String portName) throws ModbusException;
    //关闭连接
    void disconnect() throws ModbusException;
}
