package com.hzdongcheng.alxdrivers.jni;

/**
 * Created by tony.zhongli on 2018/1/31.
 */

public abstract class JNISerialPort {
    //打开串口
    protected native long dllOpen(String portName);
    //关闭串口
    protected native void dllClose(long fd);
    //初始化串口
    protected native boolean dllInitialize(long fd, int dwBaudRate, byte uPaity, byte uStopBits, byte uDataBytes);
    //设置输入输出缓冲区大小
    protected native boolean dllSetInOutQueue(long fd, int dwInQueue, int dwOutQueue);
    //设置读写超时时间
    protected native boolean dllSetReadWriteTimeouts(long fd, int dwReadTotalTimeoutConstant, int dwWriteTotalTimeoutConstant);
    //清除缓冲区数据
    protected native boolean dllClear(long fd);
    //清除串口错误
    protected native boolean dllClearError(long fd);
    //查看当前缓冲区有多少字节的数据可以读入
    protected native int dllBytesInQueue(long fd);
    //打开事件监听模式
    protected native boolean dllOpenEventMode(long fd, ISerialEvent event);
    //关闭事件监听模式
    protected native void dllCloseEventMode(long fd);
    //发送数据
    protected native int dllSend(long fd, byte[] szData, int uNumberOfBytesToSend);
    //发送定长数据
    protected native int dllSendFixLength(long fd, byte[] szData, int uNumberOfBytesToSend);
    //接收数据
    protected native int dllRecv(long fd, byte[] szData, int uNumberOfBytesToRecv);
    //接收定长数据
    protected native int dllRecvFixLength(long fd, byte[] szData, int uNumberOfBytesToRecv);
}