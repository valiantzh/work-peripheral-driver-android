package com.hzdongcheng.drivers.peripheral.scanner.impl;

import com.hzdongcheng.drivers.peripheral.scanner.AbstractScanner;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;
import com.hzdongcheng.components.toolkits.utils.TypeConvertUtils;

import java.util.Arrays;

/**
 * 
 * @ClassName: NewWorldScanner 
 * @Description: NewWorld扫描枪实现类
 * @author Administrator 
 * @date 2018年1月26 下午4:46:28 
 * @version 1.0
 */
public class NewWorldScanner extends AbstractScanner {

    public NewWorldScanner(boolean normallyMode) {
        super(normallyMode);
    }

    @Override
    protected String _searchVersion() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        // TODO Auto-generated method stub
        // 7e 00 00 02 33 47 89
        byte szRequestData[] = new byte[7];
        szRequestData[0] = 0x7e;
        szRequestData[1] = 0x00;
        szRequestData[2] = 0x00;
        szRequestData[3] = 0x02;
        szRequestData[4] = 0x33;
        szRequestData[5] = 0x47;
        szRequestData[6] = (byte) 0x89;

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[1];
        byte[] retBuffer = new byte[15];

        do {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
        } while (recvBuff[0] != 0x02);

        serialPortBase.recvFixLength(retBuffer, retBuffer.length);
        if (retBuffer[0] == 0x00 && retBuffer[1] == 0x00 && retBuffer[2] == 0x0B && retBuffer[3] == 0x34 && retBuffer[4] == 0x20) {
            return StringUtils.byteArrayToString(retBuffer, 5, 9);
        } else
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);
    }

    @Override
    protected int _switchMode(boolean normallyMode) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException {

        byte szRequestData[];
        byte correct[];
        if (normallyMode) { // 常量模式
            // szRequestData = new byte[] { 0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30,
            // 0x31, 0x30, 0x3B, 0x30, 0x33,
            // 0x30, 0x32, 0x30, 0x31, 0x30, 0x3b, 0x30, 0x32, 0x30, 0x30, 0x30, 0x31, 0x30,
            // 0x3b, 0x30, 0x30,
            // 0x30, 0x36, 0x30, 0x30, 0x30, 0x3b };// 结束
            szRequestData = new byte[]{0x7e, 0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x53, 0x43, 0x4e, 0x4d, 0x4f, 0x44,
                    0x32, 0x3b, 0x49, 0x4c, 0x4c, 0x53, 0x43, 0x4e, 0x32, 0x3b, 0x03};// 结束
            correct = new byte[]{0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x53, 0x43, 0x4E, 0x4D, 0x4F, 0x44, 0x32, 0x06,
                    0x3B, 0x49, 0x4C, 0x4C, 0x53, 0x43, 0x4E, 0x32, 0x06, 0x3B, 0x03};
        } else {
            // szRequestData = new byte[] { 0x6e, 0x6c, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30,
            // 0x31, 0x30, 0x3b, 0x30, 0x33,
            // 0x30, 0x32, 0x30, 0x30, 0x30, 0x3b, 0x30, 0x33, 0x31, 0x33, 0x30, 0x30, 0x30,
            // 0x3d, 0x31, 0x35,
            // 0x30, 0x30, 0x30, 0x3b, 0x30, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30, 0x3b };
            szRequestData = new byte[]{0x7e, 0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x53, 0x43, 0x4e, 0x4d, 0x4f, 0x44,
                    0x30, 0x3b, 0x49, 0x4c, 0x4c, 0x53, 0x43, 0x4e, 0x30, 0x3b, 0x03};
            correct = new byte[]{0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x53, 0x43, 0x4E, 0x4D, 0x4F, 0x44, 0x30, 0x06,
                    0x3B, 0x49, 0x4C, 0x4C, 0x53, 0x43, 0x4E, 0x30, 0x06, 0x3B, 0x03};
        }
        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[1];
        byte[] retBuffer = new byte[correct.length];

        while (true) {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
            if (recvBuff[0] == 0x02) {
                serialPortBase.recvFixLength(retBuffer, retBuffer.length);
                break;
            }
        }

        if (!Arrays.equals(correct, retBuffer))
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        this.normallyMode = normallyMode;
        return 0;
    }

    /*
     * (non Javadoc) <p>Title: oneDScanningOnOff</p> <p>Description: </p>
     *
     * @param onOff
     *
     * @return
     *
     * @see
     * com.hzdongcheng.components.driver.scanner.IScanner#oneDScanningOnOff(boolean)
     */
    @Override
    protected int _oneDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

        byte szRequestData[];
        byte correct[];
        if (onOff) {
            // szRequestData = new byte[] { 0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30,
            // 0x31, 0x30, 0x3B, 0x30, 0x30,
            // 0x30, 0x31, 0x30, 0x34, 0x30, 0x3B, 0x30, 0x30, 0x30, 0x36, 0x30, 0x30, 0x30,
            // 0x3B };
            szRequestData = new byte[]{0x7e, 0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4c, 0x4c, 0x31, 0x44, 0x43,
                    0x31, 0x3b, 0x03};
            correct = new byte[]{0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4C, 0x4C, 0x31, 0x44, 0x43, 0x31, 0x06,
                    0x3B, 0x03};
        } else {
            // szRequestData = new byte[] { 0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30,
            // 0x31, 0x30, 0x3B, 0x30, 0x30,
            // 0x30, 0x31, 0x30, 0x33, 0x30, 0x3B, 0x30, 0x30, 0x30, 0x36, 0x30, 0x30, 0x30,
            // 0x3B };
            szRequestData = new byte[]{0x7e, 0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4c, 0x4c, 0x31, 0x44, 0x43,
                    0x30, 0x3b, 0x03};
            correct = new byte[]{0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4C, 0x4C, 0x31, 0x44, 0x43, 0x30, 0x06,
                    0x3B, 0x03};
        }

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[1];
        byte[] retBuffer = new byte[correct.length];

        while (true) {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
            if (recvBuff[0] == 0x02) {
                serialPortBase.recvFixLength(retBuffer, retBuffer.length);
                break;
            }
        }

        if (!Arrays.equals(correct, retBuffer))
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        return 0;
    }

    /*
     * (non Javadoc) <p>Title: twoDScanningOnOff</p> <p>Description: </p>
     *
     * @param onOff
     *
     * @return
     *
     * @see
     * com.hzdongcheng.components.driver.scanner.IScanner#twoDScanningOnOff(boolean)
     */
    @Override
    protected int _twoDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        byte szRequestData[];
        byte correct[];
        if (onOff) {
            // szRequestData = new byte[] { 0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30,
            // 0x31, 0x30, 0x3B, 0x30, 0x30,
            // 0x30, 0x31, 0x30, 0x36, 0x30, 0x3B, 0x30, 0x30, 0x30, 0x36, 0x30, 0x30, 0x30,
            // 0x3B };
            szRequestData = new byte[]{0x7e, 0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4c, 0x4c, 0x32, 0x44, 0x43,
                    0x31, 0x3b, 0x03};
            correct = new byte[]{0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4C, 0x4C, 0x32, 0x44, 0x43, 0x31, 0x06,
                    0x3B, 0x03};
        } else {
            // szRequestData = new byte[] { 0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30,
            // 0x31, 0x30, 0x3B, 0x30, 0x30,
            // 0x30, 0x31, 0x30, 0x35, 0x30, 0x3B, 0x30, 0x30, 0x30, 0x36, 0x30, 0x30, 0x30,
            // 0x3B };
            szRequestData = new byte[]{0x7e, 0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4c, 0x4c, 0x32, 0x44, 0x43,
                    0x30, 0x3b, 0x03};
            correct = new byte[]{0x01, 0x30, 0x30, 0x30, 0x30, 0x23, 0x41, 0x4C, 0x4C, 0x32, 0x44, 0x43, 0x30, 0x06,
                    0x3B, 0x03};
        }

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[1];
        byte[] retBuffer = new byte[correct.length];

        while (true) {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
            if (recvBuff[0] == 0x02) {
                serialPortBase.recvFixLength(retBuffer, retBuffer.length);
                break;
            }
        }

        if (!Arrays.equals(correct, retBuffer))
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        return 0;
    }

    /*
     * (non Javadoc) <p>Title: enableScanningOnOff</p> <p>Description: </p>
     *
     * @param onOff
     *
     * @return
     *
     * @see com.hzdongcheng.components.driver.scanner.IScanner#enableScanningOnOff(
     * boolean)
     */
    @Override
    protected int _enableScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
            SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
        byte szRequestData[];
        if (onOff) {
            szRequestData = new byte[]{0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30, 0x31, 0x30, 0x3B, 0x30, 0x30,
                    0x30, 0x31, 0x30, 0x32, 0x30, 0x3B, 0x30, 0x30, 0x30, 0x36, 0x30, 0x30, 0x30, 0x3B};
        } else {
            szRequestData = new byte[]{0x6E, 0x6C, 0x73, 0x30, 0x30, 0x30, 0x36, 0x30, 0x31, 0x30, 0x3B, 0x30, 0x30,
                    0x30, 0x31, 0x30, 0x31, 0x30, 0x3B, 0x30, 0x30, 0x30, 0x36, 0x30, 0x30, 0x30, 0x3B};
        }

        serialPortBase.sendFixLength(szRequestData, szRequestData.length);

        byte[] recvBuff = new byte[1];
        while (true) {
            serialPortBase.recvFixLength(recvBuff, recvBuff.length);
            if (recvBuff[0] == 0x06 || recvBuff[0] == 0x15) {
                break;
            }
        }

        if (recvBuff[0] != 0x06)
            throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        return 0;
    }

    @Override
    protected int _startScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException {

        // byte szRequestData[] = new byte[2];
        // szRequestData[0] = 0x1B;
        // szRequestData[1] = 0x31;
        // serialPortBase.sendFixLength(szRequestData, szRequestData.length);
        //
        // byte[] recvBuff = new byte[1];
        // while(true) {
        // serialPortBase.recvFixLength(recvBuff, recvBuff.length);
        // if(recvBuff[0]==0x06||recvBuff[0]==0x15) {
        // break;
        // }
        // }
        //
        // if (recvBuff[0] != 0x06)
        // throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        return 0;
    }

    @Override
    protected int _stopScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
            RecvTimeoutException, ResponseCodeException {

        // byte szRequestData[] = new byte[2];
        // szRequestData[0] = 0x1B;
        // szRequestData[1] = 0x30;
        // serialPortBase.sendFixLength(szRequestData, szRequestData.length);
        //
        // byte[] recvBuff = new byte[1];
        //
        // while(true) {
        // serialPortBase.recvFixLength(recvBuff, recvBuff.length);
        // if(recvBuff[0]==0x06||recvBuff[0]==0x15) {
        // break;
        // }
        // }
        //
        // if (recvBuff[0] != 0x06)
        // throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);

        return 0;
    }

    /**
     * @throws SendTimeoutException
     * @throws ProtocolParsingException
     * @throws NotOpenSerialException
     * @Title: recv @Description: TODO(这里用一句话描述这个方法的作用) @param @param
     * pszBuff @param @param len @param @throws SystemException 设定文件 @return
     * void 返回类型 @throws
     */
    private byte[] recv(byte[] pszBuff, int len) throws NotOpenSerialException, SerialPortErrorException,
            RecvTimeoutException, ProtocolParsingException, SendTimeoutException {

        serialPortBase.recvFixLength(pszBuff, len);

        if (pszBuff[4] != 0x34)
            throw new ProtocolParsingException(SerialPortErrorCode.ERR_RESPONSECODE);

        // 数据总长度
        int uTotalBytes = TypeConvertUtils.byteToInt(pszBuff[2]) * 256 + TypeConvertUtils.byteToInt(pszBuff[3]);
        byte[] bodyArray = new byte[uTotalBytes - 2];
        serialPortBase.recvFixLength(bodyArray, uTotalBytes - 2);

        return bodyArray;

    }
}
