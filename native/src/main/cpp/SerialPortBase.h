/*
 * SerialPort.h
 *
 *  Created on: 2018/01/31
 */

#ifndef SERIALPORT_H_
#define SERIALPORT_H_

#define DCDZ_NOPARITY            0
#define DCDZ_ODDPARITY           1
#define DCDZ_EVENPARITY          2

#define DCDZ_ONESTOPBIT          0
#define DCDZ_ONE5STOPBITS        1
#define DCDZ_TWOSTOPBITS         2

#define DCDZ_DATABITS7           7
#define DCDZ_DATABITS8           8

class ISerialPortEvent
{
public:
	virtual ~ISerialPortEvent(){};
public:
    virtual void fire_HandleData(const char* szData, unsigned int uBytes) = 0;
    virtual void fire_HandleError(const char* szData) = 0;
};

#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <list>
#include <string.h>
#include <errno.h>
#include <time.h>
#include <pthread.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/file.h>
#include <sys/ioctl.h>

#include <vector>
using namespace std;

#include <android/log.h>

#define LOG_TAG "SerialPort"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__);

class CSerialPort
{
public:
    CSerialPort();
    virtual ~CSerialPort();

public:
    virtual unsigned int AddRef(void);
    virtual unsigned int Release(void);

    virtual bool Open(const char* szName);
    virtual bool IsOpen(void);
    virtual void Close(void);

    virtual bool Initialize(unsigned int dwBaudRate, unsigned char uPaity, unsigned char uStopBits, unsigned char uDataBytes);
    virtual bool SetInOutQueue(unsigned int dwInQueue, unsigned int dwOutQueue);
    virtual bool SetReadWriteTimeouts(unsigned int dwReadTotalTimeoutConstant, unsigned int dwWriteTotalTimeoutConstant);

    virtual bool Clear(void);
    virtual bool ClearError(void);
    virtual const char* ErrorString(void);

    virtual unsigned int BytesInQueue(void);

    //
    virtual bool OpenEventMode(ISerialPortEvent* pEvent);
    //
    virtual void CloseEventMode(void);
    //
    virtual bool Send(const char* szData, unsigned int uNumberOfBytesToSend, unsigned int* lpNumberOfBytesSend);
    //
    virtual bool Recv(char* szData, unsigned int uNumberOfBytesToRecv, unsigned int* lpNumberOfBytesRecv);

    virtual int RecvFixLength(char* szData, unsigned int uNumberOfBytesToRecv) ;

private:
    unsigned int GetLastErrorDesc();

    static void* SerialPortCommThread(void* params);
    void Handle();

private:
    //串口句柄
    int m_hSerialPort;
    //线程ID
    pthread_t m_tid;
    //select
    fd_set rfds;
    //退出标识
    volatile bool m_bExit;
    //
    volatile bool m_bPause;
    //
    ISerialPortEvent* m_pEvent;
    //引用计数
    unsigned int m_uRef;
    //错误描述
    string m_sErrorDesc;
};

#endif /* SERIALPORT_H_ */
