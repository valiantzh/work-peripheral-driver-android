#include "com_hzdongcheng_alxdrivers_jni_JNIModbus.h"
#include "Modbus4j.h"

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNIModbus
 * Method:    CreateMasterRTU
 * Signature: (Ljava/lang/String;IBBB)J
 */
JNIEXPORT jlong JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNIModbus_CreateMasterRTU
  (JNIEnv* env, jobject object, jstring portName, jint dwBaudRate, jchar uPaity, jint uStopBits, jint uDataBytes){

  Modbus4j* modbus4j = NULL;
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
    modbus4j = Modbus4j::createInstances();

    if (modbus4j != NULL)
    {
      bOK = modbus4j->CreateMasterRTU(szPortName,dwBaudRate,uPaity,uDataBytes,uStopBits);
      if (!bOK)
      {
        modbus4j->Release();
      }
    }

    free(szPortName);

    env->ReleaseByteArrayElements(barr,ba,0);
  }

  env->DeleteLocalRef(strencode);
  env->DeleteLocalRef(clsstring);

  return (jlong)modbus4j;

}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNIModbus
 * Method:    Close
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNIModbus_Close
  (JNIEnv* env, jobject object, jlong fd)
{
  Modbus4j* modbus4j = (Modbus4j*)fd;
  if (modbus4j != NULL){
    modbus4j->Release();
  }
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNIModbus
 * Method:    ReadRegisters
 * Signature: (JII[C)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNIModbus_ReadRegisters
  (JNIEnv* env, jobject object, jlong fd, jint slaveID, jint addr, jint nb, jcharArray szRecvData)
{
  Modbus4j* modbus4j = (Modbus4j*)fd;
  modbus4j->SetSlave(slaveID);

  uint16_t *szData = new uint16_t[nb];
  memset(szData, 0, nb);

  int rc = modbus4j->Read_registers(addr,nb,szData);
  if (rc == 0)
    env->SetCharArrayRegion(szRecvData, 0, nb, (jchar*)szData);

  delete [] szData;

  return rc;
}

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNIModbus
 * Method:    WriteRegister
 * Signature: (JII[C)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNIModbus_WriteRegister
(JNIEnv* env, jobject object, jlong fd, jint slaveID,jint addr, jint nb, jcharArray szData)
{
  Modbus4j* modbus4j = (Modbus4j*)fd;

  modbus4j->SetSlave(slaveID);

  jboolean isCopy = JNI_FALSE;
  jchar* jArray = env->GetCharArrayElements(szData, &isCopy);

  int rc = modbus4j->Secure_write_registers(addr,nb,(const uint16_t *)jArray);

  env->ReleaseCharArrayElements(szData,jArray,0);

  return rc;
}
