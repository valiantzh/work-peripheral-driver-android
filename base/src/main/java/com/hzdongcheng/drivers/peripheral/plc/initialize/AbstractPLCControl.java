package com.hzdongcheng.drivers.peripheral.plc.initialize;

import com.hzdongcheng.drivers.base.modbus.ModbusBase;

/**
 * Created by Administrator on 2018/4/12.
 */

public abstract class AbstractPLCControl implements IPLCControl {


    protected final ModbusBase modbusBase = new ModbusBase();

    public AbstractPLCControl(){

    }

    public ModbusBase getModbusBase() {
        return modbusBase;
    }
}
