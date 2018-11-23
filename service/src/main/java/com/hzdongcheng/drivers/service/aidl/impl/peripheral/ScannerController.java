package com.hzdongcheng.drivers.service.aidl.impl.peripheral;

import android.os.RemoteException;

import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.service.aidl.DriversManager;
import com.hzdongcheng.drivers.peripheral.IObserver;
import com.hzdongcheng.drivers.peripheral.scanner.IScannerController;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;

/**
 * Created by Administrator on 2018/2/11.
 */

public class ScannerController extends IScannerController.Stub {
    DriversManager driversManager = DriversManager.getInstance();
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());

    public ScannerController(){
    }

    @Override
    public Result getVendor() throws RemoteException {
        return null;
    }

    @Override
    public Result getVersion() throws RemoteException {
        return null;
    }

    @Override
    public Result switchMode(boolean on) throws RemoteException {
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][Scan] ==>切换扫描模式");
        Result result;
        try {
            driversManager.getScannerCtrl().switchMode(on);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ProtocolParsingException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ResponseCodeException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }
        
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][Scan] ==>切换扫描模式" + (on ? "常亮" : "触发") + " -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][Scan] ==>切换扫描模式" + (on ? "常亮" : "触发") + " -->Failed" + " --" + result.toString());
        }
        //log4jUtils.info("[Service][Scan] ==>切换扫描模式耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result toggleBarcode(boolean on) throws RemoteException {
        Result result;
        //log4jUtils.info("[Service][Scan] ==> toggleBarcode");
        long start = System.currentTimeMillis();
        try {
            driversManager.getScannerCtrl().oneDScanningOnOff(on);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ProtocolParsingException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ResponseCodeException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][Scan] ==> toggleBarcode" + (on ? "开" : "关") + " -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][Scan] ==> toggleBarcode" + (on ? "开" : "关") + " -->Failed" + " --" + jsonString);
        }
        //log4jUtils.info("[Service][Scan] ==> toggleBarcode,耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result toggleQRCode(boolean on) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][Scan] ==> toggleQRCode");
        try {
            driversManager.getScannerCtrl().twoDScanningOnOff(on);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ProtocolParsingException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ResponseCodeException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][Scan] ==> toggleQRCode" + (on ? "开" : "关") + "  -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][Scan] ==> toggleQRCode" + (on ? "开" : "关") + " -->Failed" + " --" + jsonString);
        }
        //log4jUtils.info("[Service][Scan] ==> toggleQRCode,耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result start() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][Scan] ==> startScan");
        try {
            driversManager.getScannerCtrl().startScan();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ProtocolParsingException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ResponseCodeException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        //result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][Scan] ==> startScan -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][Scan] ==> startScan -->Failed --" + jsonString);
        }
        //log4jUtils.info("[Service][Scan] ==> startScan,耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result stop() throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][Scan] ==> stopScan");

        try {
            driversManager.getScannerCtrl().stopScan();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ProtocolParsingException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (ResponseCodeException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        //result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][Scan] ==> stopScan -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][Scan] ==> stopScan -->Failed --" + jsonString);
        }
        //log4jUtils.info("[Service][Scan] ==>stopScan,耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public boolean addObserver(IObserver observer) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][扫描] ==>添加扫描监听");
        if (observer == null){
            log4jUtils.error("Add Scanner Observer failed. because param is null.");
            return false;
        }
        driversManager.registerCallbacks2Scanner(observer);
        log4jUtils.info("[服务][扫描] ==>添加扫描枪监听耗时 -->" + (System.currentTimeMillis() - start));
        return true;
    }

    @Override
    public boolean removeObserver(IObserver observer) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][扫描] ==>移除扫描监听");
        if (observer != null) {
            driversManager.unregisterCallbacks2Scanner(observer);
        }
        log4jUtils.info("[服务][扫描] ==>移除扫描枪监听耗时 -->" + (System.currentTimeMillis() - start));
        return true;
    }
}
