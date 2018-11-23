package com.hzdongcheng.alxdrivers.jni;

/**
 * Created by tony.zhongli on 2018/2/26.
 */

public abstract class JNIModbus {

    /**
     * 创建MasterRTU
     * @param portName  串口
     * @param dwBaudRate
     * @param uPaity  校验位：N-无奇偶校验  E-偶校验 O-奇校验
     * @param uStopBits 停止位长度
     * @param uDataBytes 数据位长度
     * @return
     */
    protected native long CreateMasterRTU(String portName,int dwBaudRate, char uPaity, int uStopBits, int uDataBytes);
    //关闭
    protected native void Close(long fd);

    //读保持寄存器
    protected native int ReadRegisters(long fd,int slaveID,int addr, int nb,char[] szData);
    //写保持寄存器
    protected native int WriteRegister(long fd,int slaveID,int addr, int nb,char[] szData);
}
