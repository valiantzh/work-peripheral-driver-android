// IPrinterCtrl.aidl
package com.hzdongcheng.drivers.peripheral.printer;
import com.hzdongcheng.drivers.bean.Result;

// Declare any non-default types here with import statements

interface IPrinterController {

    /**
     *打印
     */
    Result print(String data);
    /**
    * 获取打印机状态
    * */
    Result queryStatus();
}
