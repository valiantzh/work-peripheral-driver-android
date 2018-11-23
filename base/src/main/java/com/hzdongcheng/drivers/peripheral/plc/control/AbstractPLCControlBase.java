package com.hzdongcheng.drivers.peripheral.plc.control;

import com.hzdongcheng.drivers.base.modbus.ModbusBase;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;

/**
 * PLC控制基类
 * Created by Administrator on 2018/4/12.
 */

public abstract class AbstractPLCControlBase {
    protected ModbusBase modbusBase;
    protected AbstractPLCControlBase(ModbusBase modbusBase){
        this.modbusBase = modbusBase;
        DriversErrorCode.loadResource();
    }
    /**
     *
     * @Title: getBCC
     * @Description: TODO(计算校验码)
     * @param @param array
     * @param @param len
     * @param @return 设定文件
     * @return byte 返回类型
     * @throws
     */
    protected byte getBCC(final byte[] array, byte len){

        byte bcc = 0;

        for (byte i = 0; i < len; i++)
            bcc ^= array[i];

        return bcc;
    }
}
