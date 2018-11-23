package com.hzdongcheng.drivers.service.aidl.impl.peripheral;

import android.os.RemoteException;

import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.peripheral.printer.IPrinterController;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterStatus;
import com.hzdongcheng.drivers.service.aidl.DriversManager;

/**
 * Created by Administrator on 2018/3/7.
 */

public class PrinterController extends IPrinterController.Stub{
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    DriversManager driversManager = DriversManager.getInstance();
    public PrinterController(){
    }

    @Override
    public Result print(String data) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][PrinterCtrl] ==>print");
        try {
            driversManager.getPrinterCtrl().print(data);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][PrinterCtrl] ==>print :" + data + " -->Success");
        } else {
            log4jUtils.error("[Service][PrinterCtrl] ==>print:" + data + " -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][PrinterCtrl] ==>打印耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryStatus() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][PrinterCtrl] ==>queryStatus");
        try {
            PrinterStatus status = driversManager.getPrinterCtrl().getStatus();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(status));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][PrinterCtrl] ==>queryStatus:"+result.getData()+"  -->Success");
        } else {
            log4jUtils.error("[Service][PrinterCtrl] ==>queryStatus -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][PrinterCtrl] ==>queryStatus耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
}
