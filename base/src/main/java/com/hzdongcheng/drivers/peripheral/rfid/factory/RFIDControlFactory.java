package com.hzdongcheng.drivers.peripheral.rfid.factory;

import com.hzdongcheng.drivers.peripheral.rfid.IRFIDControl;
import com.hzdongcheng.drivers.peripheral.rfid.impl.ZiytekRFIDControl;
import com.hzdongcheng.drivers.peripheral.rfid.model.RFIDConfig;

/**
 *
 * @ClassName: RFIDControlFactory
 * @Description: RFID控制工厂类
 * @author: Yihang
 * @date: 2018/9/28
 */

public class RFIDControlFactory {

    /**
     *
     * @Title: createInstance
     * @Description: 创建RFID实例
     * @param @param classSimpleName
     * @param @return
     * @return IRFIDControl
     * @throws
     * @deprecated
     */
    public static IRFIDControl createInstance(String classSimpleName){
        String className = "com.dcdzsoft.drivers.rfid.impl." + classSimpleName;

        IRFIDControl rfidControl= null;
        try {
            rfidControl = (IRFIDControl)Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return rfidControl;
    }

    /**
     * @param @param
     * @param @return
     * @return IRFIDControl
     * @Method Name: createZiytekRfidControl
     * @Description: 创建Ziytek RFID实例
     * @deprecated
     */
    public static IRFIDControl createZiytekRfidControl() {
        return new ZiytekRFIDControl();
    }

    /**
     *
     * @Title: createInstance
     * @Description: 创建RFID实例
     * @param @param config
     * @param @return
     * @return IRFIDControl
     * @throws
     */
    public static IRFIDControl createInstance(RFIDConfig config) {
        IRFIDControl rfidControl = null;
        switch (config.getVendorName()) {
            case RFIDConfig.RFID_VENDOR_ZIYTEK:
                rfidControl = new ZiytekRFIDControl();
                break;
        }
        return rfidControl;
    }

}
