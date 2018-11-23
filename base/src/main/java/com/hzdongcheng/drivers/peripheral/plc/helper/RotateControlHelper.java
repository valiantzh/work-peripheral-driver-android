package com.hzdongcheng.drivers.peripheral.plc.helper;

import com.hzdongcheng.drivers.peripheral.plc.control.rotate.IRotateControl;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl.Dm3ccRotateControlImpl;
import com.hzdongcheng.drivers.peripheral.plc.initialize.IPLCControl;
import com.hzdongcheng.drivers.peripheral.plc.initialize.impl.PLCControlImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/21.
 */

public class RotateControlHelper {
    //
    private final Map<String, Object> mapInterface = new HashMap<String, Object>();
    private RotateControlHelper(){
        final IPLCControl plcControl = new PLCControlImpl();
        final IRotateControl saleRotateControl = new Dm3ccRotateControlImpl(plcControl.getModbusBase());

        //保存到Map
        mapInterface.put("com.hzdongcheng.drivers.peripheral.plc.initialize.IPLCControl",
                plcControl);
        mapInterface.put("com.hzdongcheng.drivers.peripheral.plc.control.rotate.IRotateControl",
                saleRotateControl);
    }
    private static class SingletonHolder {
        private static final RotateControlHelper instance = new RotateControlHelper();
    }
    public static RotateControlHelper getInstance(){
        return SingletonHolder.instance;
    }
    /**
     *
     * @Method Name: queryInterface
     * @Description: 动态查找接口
     * @param  @param name
     * @param  @return
     * @return T
     */
    @SuppressWarnings("unchecked")
    public <T> T queryInterface(Class<T> name){
        String key = name.getName();
        if (!mapInterface.containsKey(key))
            return null;
        return (T)mapInterface.get(key);
    }
}
