// IDriversCtrlFactory.aidl
package com.hzdongcheng.drivers;

// Declare any non-default types here with import statements

interface IDriversCtrlFactory {
    //获取扫描枪控制接口
    IBinder getScannerCtrl();
    //获取打印机控制接口
    IBinder getPrinterCtrl();
    //获取读卡器控制接口
    IBinder getCardReaderCtrl();
    //获取发卡器控制接口
    IBinder getCardSenderCtrl();
    //获取SIM发卡器控制接口
    IBinder getSimCardSenderCtrl();

    /**
     * 通过名称获取外设驱动控制接口
     */
    IBinder getService(String serviceName);
}
