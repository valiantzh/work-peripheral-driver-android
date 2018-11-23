package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.charge.impl;

import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.AbstractViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.charge.IChargeViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;

/**
 * Created by Administrator on 2018/8/16.
 */

public class ChargeViceMachineControlImpl extends AbstractViceMachineControl implements IChargeViceMachineControl {
    public ChargeViceMachineControlImpl(SerialPortBase serialPort) {
        super(serialPort);
    }

    @Override
    public ViceMachineStatus queryStatus(byte boardID) throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
        return super.queryStatus(boardID);
    }

    @Override
    public BoxStatus queryStatus(byte boardID, byte boxID) throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
        return super.queryStatus(boardID, boxID);
    }

    /**
     * 控制LED指示灯
     *
     * @param boardId
     * @param boxId
     * @param lampId  0-黄色,1-绿色
     * @param action  0 关灯，1 开灯-常亮，2 开灯-闪烁（短） 3 开灯-闪烁（长）
     * @return
     * @throws NotOpenSerialException
     * @throws SerialPortErrorException
     * @throws RecvTimeoutException
     * @throws SendTimeoutException
     * @throws ProtocolParsingException
     * @throws ResponseCodeException
     */
    @Override
    public int toggleLedLamp(byte boardId, byte boxId, byte lampId, int action) throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
        return 0;
    }

    /**
     * 控制充电开关
     *
     * @param boardId
     * @param boxId
     * @param on
     * @return
     * @throws NotOpenSerialException
     * @throws SerialPortErrorException
     * @throws RecvTimeoutException
     * @throws SendTimeoutException
     * @throws ProtocolParsingException
     * @throws ResponseCodeException
     */
    @Override
    public int toggleChargeSwitch(byte boardId, byte boxId, boolean on) throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
        return 0;
    }

    /**
     * 控制电池锁开关
     *
     * @param boardId
     * @param boxId
     * @param on
     * @return
     * @throws NotOpenSerialException
     * @throws SerialPortErrorException
     * @throws RecvTimeoutException
     * @throws SendTimeoutException
     * @throws ProtocolParsingException
     * @throws ResponseCodeException
     */
    @Override
    public int toggleBatteryLock(byte boardId, byte boxId, boolean on) throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, SendTimeoutException, ProtocolParsingException, ResponseCodeException {
        return 0;
    }
}
