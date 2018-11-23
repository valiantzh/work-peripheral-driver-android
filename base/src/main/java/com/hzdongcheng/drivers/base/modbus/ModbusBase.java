package com.hzdongcheng.drivers.base.modbus;

import com.hzdongcheng.alxdrivers.jni.JNIModbus;
import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;

/**
 * Created by Administrator on 2018/3/22.
 */

public class ModbusBase extends JNIModbus {

    public static final char MODBUS_MODE_RTU   = 0;
    public static final char MODBUS_MODE_ASCII = 1;

    public static final char MODBUS_CONN_PORT = 0;
    public static final char MODBUS_CONN_TCP = 1;

    //标识
    private long _fd = 0;
    private char modbusMode = ModbusBase.MODBUS_MODE_RTU;
    private char modbusConn = ModbusBase.MODBUS_CONN_PORT;
    private int dwBaudRate= SerialPortBase.DCDZ_CBR_9600;
    private char uPaity = SerialPortBase.DCDZ_NOPARITY_C;
    private int uStopBits = SerialPortBase.DCDZ_ONE5STOPBITS;
    private int uDataBytes = SerialPortBase.DCDZ_DATABITS8;

    public ModbusBase(){
    }

    /**
     * 初始化配置
     * @param dwBaudRate
     * @param uPaity N O E
     * @param uStopBits
     * @param uDataBytes
     */
    public void initialize(int dwBaudRate, char uPaity, int uStopBits, int uDataBytes){
        this.dwBaudRate = dwBaudRate;
        this.uPaity     = uPaity;
        this.uStopBits  = uStopBits;
        this.uDataBytes = uDataBytes;
        this.modbusMode = ModbusBase.MODBUS_MODE_RTU;//默认RTU模式，ASCII模式待扩展
    }
    /**
     *
     * @Title: connect
     * @Description: 连接串口
     * @param @param portName
     * @param @return
     * @param @throws ModbusException
     * @return boolean
     * @throws
     */
    public boolean connect(String portName)
            throws ModbusException {

        if (!SerialPortBase.isLoad)
            throw new ModbusException(SerialPortErrorCode.ERR_SP_NOTFOUNDSERIALLIBRARY);

        String port = SerialPortBase.transformSerialPort(portName);

        if(ModbusBase.MODBUS_MODE_RTU == this.modbusMode ){
            _fd = CreateMasterRTU(port, dwBaudRate, uPaity, uStopBits, uDataBytes);
        }
        if (0 == _fd)
            throw new ModbusException(SerialPortErrorCode.ERR_SP_SERIALPORT,
                    "connect port( " + portName + ") failed");

        this.modbusConn = ModbusBase.MODBUS_CONN_PORT;

        return true;
    }

    /**
     *
     * @Title: close
     * @Description: 关闭连接
     * @return void 返回类型
     * @throws
     */
    public void disconnect() {

        if (0 == _fd)
            return;

        Close(_fd);

        _fd = 0;
    }

    /**
     * 读寄存器
     * @param slaveID
     * @param addr
     * @param nb
     * @param szData
     * @return
     * @throws ModbusException
     */
    public int read(int slaveID, int addr, int nb, char[] szData)throws ModbusException{
        if (0 == _fd)
            throw new ModbusException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
        int rc = ReadRegisters(_fd, slaveID, addr, nb, szData);

        return rc;
    }

    /**
     * 写寄存器
     * @param slaveID
     * @param addr
     * @param nb
     * @param szData
     * @return
     * @throws ModbusException
     */
    public int write(int slaveID, int addr, int nb, char[] szData)throws ModbusException{
        if (0 == _fd)
            throw new ModbusException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
        int rc = WriteRegister(_fd, slaveID, addr, nb, szData);
        return rc;
    }
}
