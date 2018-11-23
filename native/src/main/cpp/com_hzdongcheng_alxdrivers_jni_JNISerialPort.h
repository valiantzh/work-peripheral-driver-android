/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_hzdongcheng_alxdrivers_jni_JNISerialPort */

#ifndef _Included_com_hzdongcheng_alxdrivers_jni_JNISerialPort
#define _Included_com_hzdongcheng_alxdrivers_jni_JNISerialPort
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllOpen
 * Signature: (Ljava/lang/String;)J
 */
JNIEXPORT jlong JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllOpen
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllClose
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllClose
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllInitialize
 * Signature: (JIBBB)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllInitialize
  (JNIEnv *, jobject, jlong, jint, jbyte, jbyte, jbyte);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSetInOutQueue
 * Signature: (JII)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSetInOutQueue
  (JNIEnv *, jobject, jlong, jint, jint);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSetReadWriteTimeouts
 * Signature: (JII)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSetReadWriteTimeouts
  (JNIEnv *, jobject, jlong, jint, jint);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllClear
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllClear
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllClearError
 * Signature: (J)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllClearError
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllBytesInQueue
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllBytesInQueue
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllOpenEventMode
 * Signature: (JLcom/hzdongcheng/alxdrivers/jni/ISerialEvent;)Z
 */
JNIEXPORT jboolean JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllOpenEventMode
  (JNIEnv *, jobject, jlong, jobject);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllCloseEventMode
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllCloseEventMode
  (JNIEnv *, jobject, jlong);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSend
 * Signature: (J[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSend
  (JNIEnv *, jobject, jlong, jbyteArray, jint);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllSendFixLength
 * Signature: (J[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllSendFixLength
  (JNIEnv *, jobject, jlong, jbyteArray, jint);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllRecv
 * Signature: (J[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllRecv
  (JNIEnv *, jobject, jlong, jbyteArray, jint);

/*
 * Class:     com_hzdongcheng_alxdrivers_jni_JNISerialPort
 * Method:    dllRecvFixLength
 * Signature: (J[BI)I
 */
JNIEXPORT jint JNICALL Java_com_hzdongcheng_alxdrivers_jni_JNISerialPort_dllRecvFixLength
  (JNIEnv *, jobject, jlong, jbyteArray, jint);

#ifdef __cplusplus
}
#endif
#endif