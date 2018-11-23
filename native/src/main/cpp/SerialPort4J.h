//
// Created by tony.zhongli on 2018/1/31.
//

#ifndef ALXDRIVERS_SERIALPORT4J_H
#define ALXDRIVERS_SERIALPORT4J_H

#include <unistd.h>
#include <jni.h>
#include "SerialPortBase.h"

class CMutex
{
public:
    CMutex()
    {
        pthread_mutex_init(&m_hMutex, NULL);
    }

    ~CMutex()
    {
        pthread_mutex_destroy(&m_hMutex);
    }
public:
    /*static CMutex* createInstance()
    {
        return new CMutex();
    }*/

    void Release()
    {
        delete this;
    }

    bool Wait()
    {
        return pthread_mutex_lock(&m_hMutex) == 0 ? true : false;
    }

    bool WakeUp()
    {
        return pthread_mutex_unlock(&m_hMutex) == 0 ? true : false;
    }

private:
    pthread_mutex_t m_hMutex;
};

class CAutoMutex
{
public:
    CAutoMutex(CMutex* mutex)
    {
        m_Mutex = mutex;
        m_Mutex->Wait();
    }
    ~CAutoMutex()
    {
        m_Mutex->WakeUp();
    }
private:
    CMutex* m_Mutex;
};


class SerialPort4J : public ISerialPortEvent
{
protected:
    SerialPort4J();
    virtual ~SerialPort4J();

public:
    static SerialPort4J* createInstances();

protected:
    void fire_HandleData(const char* szData, unsigned int uBytes);
    void fire_HandleError(const char* szData);

public:
    void Release();

    //打开串口
    bool Open(const char* szName);
    //检查串口是否已经打开
    bool IsOpen(void);
    //关闭串口
    void Close(void);

    //初始化串口配置
    bool Initialize(unsigned int dwBaudRate, unsigned char uPaity, unsigned char uStopBits, unsigned char uDataBytes);
    //设置缓冲区大小
    bool SetInOutQueue(unsigned int dwInQueue, unsigned int dwOutQueue);
    //设置读写超时
    bool SetReadWriteTimeouts(unsigned int dwReadTotalTimeoutConstant, unsigned int dwWriteTotalTimeoutConstant);

    //清空收发缓冲区
    bool Clear(void);
    //清除串口错误信息
    bool ClearError(void);
    //获取串口错误信息
    const char* ErrorString(void);

    //得到串口可读字符数量
    unsigned int BytesInQueue(void);

    //打开事件接收模式
    //当串口有数据或则错误产生时，会调用ISerialPortEvent接口方法传出
    bool OpenEventMode(JNIEnv* env, jobject obj);
    //关闭事件接收模式
    void CloseEventMode();

    //发送数据
    bool Send(const char* szData, unsigned int uNumberOfBytesToSend, unsigned int* lpNumberOfBytesSend);

    //发送数据
    int SendFixLength(const char* szData, unsigned int uNumberOfBytesToSend);
    //接收数据
    //在规定的超时时间内，尽可能的接收数据
    bool Recv(char* szData, unsigned int uNumberOfBytesToRecv, unsigned int* lpNumberOfBytesRecv);
    //接收指定长度的数据
    int RecvFixLength(char* szData, unsigned int uNumberOfBytesToRecv);

private:
    CSerialPort* serialPort;
    JavaVM *m_jvm;
    jobject m_object;

    CMutex m_Mutex;
};


#endif //ALXDRIVERS_SERIALPORT4J_H
