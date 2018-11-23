package com.hzdongcheng.alxdrivers.jni;

/**
 * Created by tony.zhongli on 2018/1/31.
 */

public interface ISerialEvent {
    public void fire_HandleData(int ret, byte[] szData);
}
