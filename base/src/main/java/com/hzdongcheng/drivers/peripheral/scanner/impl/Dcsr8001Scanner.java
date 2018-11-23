package com.hzdongcheng.drivers.peripheral.scanner.impl;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;

import com.hzdongcheng.drivers.peripheral.scanner.AbstractScanner;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;

public class Dcsr8001Scanner extends AbstractScanner {

    public Dcsr8001Scanner(boolean normallyMode) {
        super(normallyMode);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected String _searchVersion() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        // {0x23 0x73 0x79 0x6E 0x6F 0x20 0x3D 0x30 0x31 0x39};
        byte szRequestData[] = new byte[10];
        szRequestData[0] = 0x23;
        szRequestData[1] = 0x73;
        szRequestData[2] = 0x79;
        szRequestData[3] = 0x6E;
        szRequestData[4] = 0x6F;
        szRequestData[5] = 0x20;
        szRequestData[6] = 0x3D;
        szRequestData[7] = 0x30;
        szRequestData[8] = 0x31;
        szRequestData[9] = 0x39;

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[1];
        byte[] retBuffer = new byte[26];

        do {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
        } while (recvBuff[0] != 0x44);

        serialPortBase.recvFixLength(retBuffer, retBuffer.length);
        if (retBuffer[0] == 0x43 && retBuffer[25] == 0x0A) {
            return "D" + StringUtils.byteArrayToString(retBuffer, 0, 25);
        } else
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);
    }

    // 开启蜂鸣音
    protected int _buzzing_on() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        byte szRequestData[] = new byte[10];
        szRequestData[0] = 0x7E;
        szRequestData[1] = 0x00;
        szRequestData[2] = 0x08;
        szRequestData[3] = 0x01;
        szRequestData[4] = 0x00;
        szRequestData[5] = 0x13;
        szRequestData[6] = 0x04;
        szRequestData[7] = (byte) 0xAB;
        szRequestData[8] = (byte) 0xCD;

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        checkResponse();
        return 0;
    }

    // 关闭蜂鸣音
    protected int _buzzing_off() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        byte szRequestData[] = new byte[10];
        szRequestData[0] = 0x7E;
        szRequestData[1] = 0x00;
        szRequestData[2] = 0x08;
        szRequestData[3] = 0x01;
        szRequestData[4] = 0x00;
        szRequestData[5] = 0x13;
        szRequestData[6] = 0x02;
        szRequestData[7] = (byte) 0xAB;
        szRequestData[8] = (byte) 0xCD;

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        checkResponse();
        return 0;
    }

    @Override
    protected int _switchMode(boolean normallyMode) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        byte szRequestData[];
        if (normallyMode) { // 常亮模式23 73 79 6E 6F 20 3D 31 33 30
            szRequestData = new byte[]{0x7E, 0x00, 0x08, 0x01, 0x00, 0x10, 0x08, (byte) 0xAB, (byte) 0xCD};// 模式
            serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        } else {
            // szRequestData = new byte[] { 0x23, 0x73, 0x79, 0x6E, 0x6F, 0x20, 0x3D, 0x31,
            // 0x33, 0x34 };
            szRequestData = new byte[]{0x7E, 0x00, 0x08, 0x01, 0x00, 0x10, 0x04, (byte) 0xAB, (byte) 0xCD};// 模式
            serialPortBase.sendFixLength(szRequestData, szRequestData.length);
        }

        checkResponse();

        this.normallyMode = normallyMode;
        return 0;
    }

    byte[] currect = new byte[]{0x00, 0x00, 0x01, 0x00, 0x33, 0x31};

    private void checkResponse()
            throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, ResponseCodeException {
        byte[] recvBuff = new byte[1];
        byte[] retBuffer = new byte[6];
        while (true) {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
            if (recvBuff[0] == 0x02) {
                serialPortBase.recvFixLength(retBuffer, retBuffer.length);
                break;
            }
        }

        if (!Arrays.equals(currect, retBuffer))
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);


    }

    Boolean recentOne = null;
    Boolean recentTwo = null;
    byte[] allOn = new byte[]{0x7E, 0x00, 0x08, 0x28, 0x00, 0x2E, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x05, 0x04, 0x20,
            0x01, 0x04, 0x20, 0x05, 0x04, 0x20, 0x01, 0x04, 0x20, 0x01, 0x01, 0x04, 0x20, 0x01, 0x04, 0x20, 0x01, 0x04,
            0x20, 0x05, 0x04, 0x20, 0x05, 0x04, 0x20, 0x01, 0x01, 0x01, 0x04, 0x20, 0x01, 0x01, (byte) 0xAB,
            (byte) 0xCD};
    byte[] allOff = new byte[]{0x7E, 0x00, 0x08, 0x28, 0x00, 0x2E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x20,
            0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x04,
            0x20, 0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x00, 0x00, 0x04, 0x20, 0x00, 0x00, (byte) 0xAB,
            (byte) 0xCD};

    byte[] trueFalse = new byte[]{0x7E, 0x00, 0x08, 0x28, 0x00, 0x2E, 0x0D, 0x0D, 0x0D, 0x0D, 0x0D, 0x05, 0x04, 0x20,
            0x01, 0x04, 0x20, 0x05, 0x04, 0x20, 0x01, 0x04, 0x20, 0x00, 0x01, 0x04, 0x20, 0x01, 0x04, 0x20, 0x01, 0x04,
            0x20, 0x05, 0x04, 0x20, 0x05, 0x04, 0x20, 0x01, 0x01, 0x01, 0x04, 0x20, 0x00, 0x00, (byte) 0xAB,
            (byte) 0xCD};
    byte[] falseTrue = new byte[]{0x7E, 0x00, 0x08, 0x28, 0x00, 0x2E, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 0x20,
            0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x01, 0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x04,
            0x20, 0x00, 0x04, 0x20, 0x00, 0x04, 0x20, 0x00, 0x00, 0x00, 0x04, 0x20, 0x01, 0x01, (byte) 0xAB,
            (byte) 0xCD};

    @Override
    protected int _oneDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

        byte szRequestData[];
        if (onOff) {
            if (recentTwo != null && recentTwo) {
                szRequestData = allOn;
            } else
                szRequestData = trueFalse;
        } else {
            if (recentTwo != null && !recentTwo) {
                szRequestData = allOff;
            } else
                szRequestData = falseTrue;
        }
        recentOne = onOff;
        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        checkResponse();

        return 0;
    }

    @Override
    protected int _twoDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        // TODO Auto-generated method stub
        byte szRequestData[];
        if (onOff) {
            if (recentOne != null && recentOne) {
                szRequestData = allOn;
            } else
                szRequestData = falseTrue;
        } else {
            if (recentOne != null && !recentOne) {
                szRequestData = allOff;
            } else
                szRequestData = trueFalse;
        }
        recentTwo = onOff;

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        checkResponse();

        return 0;
    }

    @Override
    protected int _enableScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        // TODO Auto-generated method stub
        byte szRequestData[];
        if (onOff) {
            szRequestData = new byte[]{0x23, 0x73, 0x79, 0x6E, 0x6F, 0x20, 0x3D, 0x31, 0x37, 0x38};
        } else {
            szRequestData = new byte[]{0x23, 0x73, 0x79, 0x6E, 0x6F, 0x20, 0x3D, 0x31, 0x37, 0x39};
        }

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[3];
        // 06 06
        serialPortBase.recvFixLength(recvBuff, recvBuff.length);

        if (recvBuff[0] != 0x30)
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        return 0;
    }

    @Override
    protected int _startScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
//		byte szRequestData[];// 7E 00 06 01 00 00 55 AB CD
//		szRequestData = new byte[] { 0x7E, 0x00, 0x06, 0x01, 0x00, 0x00, 0x55, (byte) 0xAB, (byte) 0xCD };
//		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
        this._buzzing_on();
        return 0;
    }

    @Override
    protected int _stopScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
//		byte szRequestData[];// 7E 00 06 01 00 00 55 AB CD
//		szRequestData = new byte[] { 0x7E, 0x00, 0x06, 0x01, 0x00, 0x00, (byte) 0xAA, (byte) 0xAB, (byte) 0xCD };
//		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
        this._buzzing_off();
        return 0;
    }

}
