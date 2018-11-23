package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.charge;

import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.IViceMachineControl;

/**
 * Created by Administrator on 2018/8/16.
 */

public interface IChargeViceMachineControl extends IViceMachineControl {
    /**
     * 控制LED指示灯
     * @param boardId
     * @param boxId
     * @param lampId
     * @param action
     * @return
     * @throws NotOpenSerialException
     * @throws SerialPortErrorException
     * @throws RecvTimeoutException
     * @throws SendTimeoutException
     * @throws ProtocolParsingException
     * @throws ResponseCodeException
     */
    int toggleLedLamp(byte boardId,byte boxId,byte lampId,int action) throws NotOpenSerialException,
                                                                             SerialPortErrorException,
                                                                             RecvTimeoutException,
                                                                             SendTimeoutException,
                                                                             ProtocolParsingException,
                                                                             ResponseCodeException;

    /**
     *  控制充电开关
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
    int toggleChargeSwitch(byte boardId,byte boxId,boolean on)throws NotOpenSerialException,
                                                                     SerialPortErrorException,
                                                                     RecvTimeoutException,
                                                                     SendTimeoutException,
                                                                     ProtocolParsingException,
                                                                     ResponseCodeException;

    /**
     * 控制电池锁开关
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
    int toggleBatteryLock(byte boardId,byte boxId,boolean on)throws NotOpenSerialException,
            SerialPortErrorException,
            RecvTimeoutException,
            SendTimeoutException,
            ProtocolParsingException,
            ResponseCodeException;
}
