#include "com_hzdongcheng_alxdrivers_jni_JNISerialPort.h"
#include "SerialPort4J.h"

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllOpen
 * Signature: (Ljava/lang/String;)I
 */
/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllOpen
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jlong JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllOpen
        (JNIEnv* env, jobject object, jstring portName)
{
    SerialPort4J* serialPort = NULL;
    bool bOK = false;

    char* szPortName = NULL;
    jclass clsstring  = env->FindClass("java/lang/String");

    jstring strencode = env->NewStringUTF("GB2312");
    jmethodID mid     = env->GetMethodID(clsstring, "getBytes", "(Ljava/lang/String;)[B");
    jbyteArray barr   = (jbyteArray)env->CallObjectMethod(portName, mid, strencode);
    jsize alen        = env->GetArrayLength(barr);
    jbyte* ba         = env->GetByteArrayElements(barr,JNI_FALSE);

    if(alen > 0 && ba != NULL)
    {
        szPortName = (char*)malloc(alen+1);
        memcpy(szPortName, ba, alen);
        szPortName[alen] = 0;

        //open serial port
        serialPort = SerialPort4J::createInstances();

        if (serialPort != NULL)
        {
            bOK = serialPort->Open(szPortName);
            if (!bOK)
            {
                serialPort->Release();
                serialPort = NULL;
            }
        }

        free(szPortName);

        env->ReleaseByteArrayElements(barr,ba,0);
    }

    env->DeleteLocalRef(strencode);
    env->DeleteLocalRef(clsstring);

    return (jlong)serialPort;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllClose
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllClose
        (JNIEnv* env, jobject object, jlong fd)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    if (serialPort != NULL){
        serialPort->Close();
        serialPort->Release();
    }
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllInitialize
 * Signature: (IIBBB)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllInitialize
        (JNIEnv* env, jobject object, jlong fd, jint dwBaudRate, jbyte uPaity, jbyte uStopBits, jbyte uDataBytes)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    bool bOK = serialPort->Initialize(dwBaudRate, uPaity, uStopBits, uDataBytes);
    return bOK ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSetInOutQueue
 * Signature: (III)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSetInOutQueue
        (JNIEnv* env, jobject object, jlong fd, jint dwInQueue, jint dwOutQueue)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    bool bOK = serialPort->SetInOutQueue(dwInQueue, dwOutQueue);
    return bOK ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSetReadWriteTimeouts
 * Signature: (III)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSetReadWriteTimeouts
        (JNIEnv* env, jobject object, jlong fd, jint dwReadTotalTimeoutConstant, jint dwWriteTotalTimeoutConstant)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    bool bOK = serialPort->SetReadWriteTimeouts(dwReadTotalTimeoutConstant, dwWriteTotalTimeoutConstant);
    return bOK ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllClear
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllClear
        (JNIEnv* env, jobject object, jlong fd)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    bool bOK = serialPort->Clear();
    return bOK ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllClearError
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllClearError
        (JNIEnv* env, jobject object, jlong fd)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    bool bOK = serialPort->ClearError();
    return bOK ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllBytesInQueue
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllBytesInQueue
        (JNIEnv* env, jobject object, jlong fd)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    return serialPort->BytesInQueue();
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllOpenEventMode
 * Signature: (ILcom/hzdongcheng/alxdrivers/jni/ISerialEvent;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllOpenEventMode
        (JNIEnv* env, jobject object, jlong fd, jobject callback)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    bool bOK = serialPort->OpenEventMode(env, callback);
    return bOK ? JNI_TRUE : JNI_FALSE;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllCloseEventMode
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllCloseEventMode
        (JNIEnv* env, jobject object, jlong fd)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;
    serialPort->CloseEventMode();
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSend
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSend
        (JNIEnv* env, jobject object, jlong fd, jbyteArray szData, jint len)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;

    jboolean isCopy = JNI_FALSE;
    jbyte* jBytes = env->GetByteArrayElements(szData, &isCopy);

    unsigned int uNumberOfBytesSend = 0;
    bool bOK = serialPort->Send((const char*)jBytes, len, &uNumberOfBytesSend);

    env->ReleaseByteArrayElements(szData,jBytes,0);

    return bOK ? uNumberOfBytesSend : -4;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSendFixLength
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSendFixLength
        (JNIEnv* env, jobject object, jlong fd, jbyteArray szData, jint len)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;

    jboolean isCopy = JNI_FALSE;
    jbyte* jArray = env->GetByteArrayElements(szData, &isCopy);

    int ret = serialPort->SendFixLength((const char*)jArray, len);

    env->ReleaseByteArrayElements(szData,jArray,0);

    return ret;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllRecv
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllRecv
        (JNIEnv* env, jobject object, jlong fd, jbyteArray szRecvData, jint len)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;

    char* szData = new char[len];
    memset(szData, 0, len);

    unsigned int uNumberOfBytesRecv = 0;
    bool bOK = serialPort->Recv(szData, len, &uNumberOfBytesRecv);

    if(bOK)
        env->SetByteArrayRegion(szRecvData, 0, uNumberOfBytesRecv, (jbyte*)szData);

    delete [] szData;

    return bOK ? uNumberOfBytesRecv : -4;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllRecvFixLength
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllRecvFixLength
        (JNIEnv* env, jobject object, jlong fd, jbyteArray szRecvData, jint len)
{
    SerialPort4J* serialPort = (SerialPort4J*)fd;

    char* szData = new char[len];
    memset(szData, 0, len);

    int ret = serialPort->RecvFixLength(szData, len);
    if (ret == 0)
        env->SetByteArrayRegion(szRecvData, 0, len, (jbyte*)szData);

    delete [] szData;

    return ret;
}