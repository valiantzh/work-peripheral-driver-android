package com.hzdongcheng.drivers.peripheral.plc.initialize.impl;

import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.modbus.helper.ModbusHelper;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.peripheral.plc.initialize.AbstractPLCControl;

/**
 * Created by Administrator on 2018/4/12.
 */

public class PLCControlImpl extends AbstractPLCControl {
    @Override
    public void connect(String portName) throws ModbusException {
        this.modbusBase.initialize(SerialPortBase.DCDZ_CBR_9600,SerialPortBase.DCDZ_EVENPARITY_C,SerialPortBase.DCDZ_ONE5STOPBITS, SerialPortBase.DCDZ_DATABITS8);
        //this.modbusBase.initialize(SerialPortBase.DCDZ_CBR_9600,SerialPortBase.DCDZ_NOPARITY_C,SerialPortBase.DCDZ_ONE5STOPBITS, SerialPortBase.DCDZ_DATABITS8);

        boolean isok =this.modbusBase.connect(portName);
        if(isok){
            ModbusHelper.getInstance().setModbusBase(modbusBase);
            ModbusHelper.getInstance().startMonitorTask();
        }
    }


    @Override
    public void disconnect() throws ModbusException {
        ModbusHelper.getInstance().stopMonitorTask();
        this.modbusBase.disconnect();
    }
}
