//
// Created by tony.zhongli on 2018/1/31.
//
#include "SerialPort4J.h"

SerialPort4J::SerialPort4J()
{
    //ctor
    serialPort = NULL;
    m_jvm = NULL;
    m_object = NULL;
}

SerialPort4J::~SerialPort4J()
{
    serialPort->Release();
}

SerialPort4J* SerialPort4J::createInstances()
{
    SerialPort4J* serialPort4J = new SerialPort4J();
    if (serialPort4J == NULL)
        return NULL;

    serialPort4J->serialPort = new CSerialPort();
    if (serialPort4J->serialPort == NULL)
    {
        delete serialPort4J;
        serialPort4J = NULL;
    }

    return serialPort4J;
}

void SerialPort4J::Release()
{
    delete this;
}

//打开事件接收模式
//当串口有数据或则错误产生时，会调用ISerialPortEvent接口方法传出
bool SerialPort4J::OpenEventMode(JNIEnv* env, jobject obj)
{
    CAutoMutex autoMutex(&m_Mutex);

    bool bOK = false;

    serialPort->Clear();
    serialPort->ClearError();

    if (JNI_OK != env->GetJavaVM(&m_jvm)){
        return bOK;
    }

    //直接保存obj到DLL中的全局变量是不行的,应该调用以下函数:
    if (m_object != NULL){
        env->DeleteGlobalRef(m_object);
        m_object = NULL;
    }
    m_object = env->NewGlobalRef(obj);
    if (m_object == NULL){
        m_jvm = NULL;
        return bOK;
    }

    //打开事件模式
    bOK = serialPort->OpenEventMode(this);
    if (!bOK)
    {
        env->DeleteGlobalRef(m_object);
        m_object = NULL;
        m_jvm = NULL;
    }

    return bOK;
}

//关闭事件接收模式
void SerialPort4J::CloseEventMode()
{
    CAutoMutex autoMutex(&m_Mutex);

    serialPort->CloseEventMode();

    if (m_object != NULL)
    {
        if (m_jvm != NULL)
        {
            JNIEnv* env = NULL;
            if (JNI_OK == m_jvm->GetEnv((void**)&env, JNI_VERSION_1_6))
            {
                env->DeleteGlobalRef(m_object);
                m_object = NULL;
            }

            m_jvm = NULL;
        }
    }
}

void SerialPort4J::fire_HandleData(const char* szData, unsigned int uBytes)
{
    CAutoMutex autoMutex(&m_Mutex);

    if (m_jvm == NULL || m_object == NULL)
        return;

    JNIEnv *env = NULL;
    m_jvm->AttachCurrentThread((JNIEnv**)&env, NULL);
    if (env == NULL)
        return;

    jclass cls = env->GetObjectClass(m_object);
    jmethodID jmid = env->GetMethodID(cls, "fire_HandleData", "(I[B)V");

    if (jmid != NULL &&  cls != NULL){
        jbyteArray byteArray = env->NewByteArray((jsize)uBytes);
        if (byteArray != NULL)
        {
            // 将char* 转换为byte数组
            env->SetByteArrayRegion(byteArray, 0, (jsize)uBytes, (jbyte*)szData);
            env->CallVoidMethod(m_object, jmid, uBytes, byteArray);
        }
    }

    m_jvm->DetachCurrentThread();
}

void SerialPort4J::fire_HandleError(const char* szData)
{
    CAutoMutex autoMutex(&m_Mutex);

    if (m_jvm == NULL || m_object == NULL)
        return;

    JNIEnv *env = NULL;
    m_jvm->AttachCurrentThread((JNIEnv**)&env, NULL);
    if (env == NULL)
        return;

    jclass cls = env->GetObjectClass(m_object);
    jmethodID jmid = env->GetMethodID(cls, "fire_HandleData", "(I[B)V");

    if (jmid != NULL &&  cls != NULL){
        int uBytes = strlen(szData);
        jbyteArray byteArray = env->NewByteArray((jsize)uBytes);
        if (byteArray != NULL)
        {
            // 将char* 转换为byte数组
            env->SetByteArrayRegion(byteArray, 0, (jsize)uBytes, (jbyte*)szData);
            env->CallVoidMethod(m_object, jmid, -1, byteArray);
        }
    }

    m_jvm->DetachCurrentThread();
}

//打开串口
bool SerialPort4J::Open(const char* szName)
{
    return serialPort->Open(szName);
}
//检查串口是否已经打开
bool SerialPort4J::IsOpen(void)
{
    return serialPort->IsOpen();
}
//关闭串口
void SerialPort4J::Close(void)
{
    CloseEventMode();
    serialPort->Close();
}

//初始化串口配置
bool SerialPort4J::Initialize(unsigned int dwBaudRate, unsigned char uPaity, unsigned char uStopBits, unsigned char uDataBytes)
{
    return serialPort->Initialize(dwBaudRate, uPaity, uStopBits, uDataBytes);
}
//设置缓冲区大小
bool SerialPort4J::SetInOutQueue(unsigned int dwInQueue, unsigned int dwOutQueue)
{
    return serialPort->SetInOutQueue(dwInQueue, dwOutQueue);
}
//设置读写超时
bool SerialPort4J::SetReadWriteTimeouts(unsigned int dwReadTotalTimeoutConstant, unsigned int dwWriteTotalTimeoutConstant)
{
    return serialPort->SetReadWriteTimeouts(dwReadTotalTimeoutConstant, dwWriteTotalTimeoutConstant);
}

//清空收发缓冲区
bool SerialPort4J::Clear(void)
{
    return serialPort->Clear();
}
//清除串口错误信息
bool SerialPort4J::ClearError(void)
{
    return serialPort->ClearError();
}
//获取串口错误信息
const char* SerialPort4J::ErrorString(void)
{
    return serialPort->ErrorString();
}

//得到串口可读字符数量
unsigned int SerialPort4J::BytesInQueue(void)
{
    return serialPort->BytesInQueue();
}


//发送数据
bool SerialPort4J::Send(const char* szData, unsigned int uNumberOfBytesToSend, unsigned int* lpNumberOfBytesSend)
{
    return serialPort->Send(szData, uNumberOfBytesToSend, lpNumberOfBytesSend);
}

int SerialPort4J::SendFixLength(const char* szData, unsigned int uNumberOfBytesToSend)
{
    unsigned int uTotalBytes = uNumberOfBytesToSend;
    unsigned int uIndex = 0;

    int iRet = 0;

    do{
        unsigned int uBytes = 0;
        bool bOK = Send(&szData[uIndex], uTotalBytes - uIndex, &uBytes);
        if (!bOK)
        {
            iRet = -4;
            break;
        }

        uIndex += uBytes;
    }while(uTotalBytes != uIndex);

    return iRet;
}
//接收数据
//在规定的超时时间内，尽可能的接收数据
bool SerialPort4J::Recv(char* szData, unsigned int uNumberOfBytesToRecv, unsigned int* lpNumberOfBytesRecv)
{
    return serialPort->Recv(szData, uNumberOfBytesToRecv, lpNumberOfBytesRecv);
}
//接收指定长度的数据
int SerialPort4J::RecvFixLength(char* szData, unsigned int uNumberOfBytesToRecv)
{
    return serialPort->RecvFixLength(szData, uNumberOfBytesToRecv);
}
