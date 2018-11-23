package com.hzdongcheng.drivers.peripheral.plc.control.rotate;

import com.hzdongcheng.drivers.base.modbus.ModbusBase;
import com.hzdongcheng.drivers.peripheral.plc.control.AbstractPLCControlBase;
import com.hzdongcheng.drivers.peripheral.servo.IServoControl;

/**
 * Created by Administrator on 2018/4/12.
 */

public abstract class AbstractRotateControl extends AbstractPLCControlBase implements IRotateControl {
    public AbstractRotateControl(ModbusBase modbusBase) {
        super(modbusBase);
    }
}
