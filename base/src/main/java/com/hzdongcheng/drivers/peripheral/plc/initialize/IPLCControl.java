package com.hzdongcheng.drivers.peripheral.plc.initialize;

import com.hzdongcheng.drivers.base.modbus.IModbusDevice;
import com.hzdongcheng.drivers.base.modbus.ModbusBase;

/**
 * Created by Administrator on 2018/4/12.
 */

public interface IPLCControl extends IModbusDevice {
    ModbusBase getModbusBase();
}
