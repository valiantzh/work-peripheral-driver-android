package com.dcdz.drivers.demo.business;

import android.os.RemoteException;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;
import com.hzdongcheng.drivers.peripheral.IObserver;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 通用外设功能代理
 * Created by Administrator on 2018/2/26.
 */

public class Proxy4Peripheral{
    private  Log4jUtils log = Log4jUtils.createInstanse(Proxy4Peripheral.class);
    private ServiceHelper serviceHelper = ServiceHelper.getInstance();

    private static class SingletonHolder{
        private final static Proxy4Peripheral instance = new Proxy4Peripheral();
    }
    public static Proxy4Peripheral getInstance(){
        return SingletonHolder.instance;
    }
    private Proxy4Peripheral(){
    }
    
    public Result getVendorName() throws DcdzSystemException{
        return null;
    }

    
    public Result getAppVersion() throws DcdzSystemException {

        return null;
    }


    //#region 扫描枪业务代理
    public boolean defaultAutoScanMode = true;//true -自动扫码（常亮模式）  false-命令触发模式
    private ScheduledExecutorService serviceScanner = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> futureScanner = null;
    private Runnable futureRunnableScanner = new Runnable() {//命令触发模式调用
        @Override
        public void run() {
            try {
                if(isStartScan){
                    startScan();
                }
            } catch (DcdzSystemException e) {
                e.printStackTrace();
            }
        }
    };
    private boolean isStartScan = false;
    private boolean autoScanMode = true;//true -自动扫码（常亮模式）  false-命令触发模式
    public void initScanner(IMessageListener listener, boolean autoMode) throws DcdzSystemException {
        addScanListener(listener);
        autoScanMode = autoMode;
        toggleBarcode(true);
        if(autoMode){
            toggleMode(true);
        }else{
            toggleMode(false);
            futureScanner = serviceScanner.scheduleWithFixedDelay(futureRunnableScanner, 0, 2, TimeUnit.SECONDS);
        }
        startScan();
    }

    public void closeScanner() throws DcdzSystemException {
        try {
            if(futureScanner != null){
                futureScanner.cancel(true);
                futureScanner = null;
            }
            removeScanListener();
            if(autoScanMode){
                toggleMode(false);//
            }
            stopScan();
        } catch (DcdzSystemException e) {
            log.error("[closeScanner]", e);
        }
    }

    public String toggleMode(boolean on) throws DcdzSystemException{
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.scannerCtrl.switchMode(on);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e) {
            log.error("[toggleMode]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    
    public String toggleBarcode(boolean on) throws DcdzSystemException{
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.scannerCtrl.toggleBarcode(on);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e) {
            log.error("[toggleBarcode]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    
    public String toggleQRCode(boolean on) throws DcdzSystemException{
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res =  serviceHelper.scannerCtrl.toggleQRCode(on);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e) {
            log.error("[toggleQRCode]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    
    public String startScan() throws DcdzSystemException{
        isStartScan = true;
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            //serviceHelper.scannerCtrl.addObserver(scannerObserver);
            Result res =  serviceHelper.scannerCtrl.start();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e) {
            log.error("[startScan]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String stopScan() throws DcdzSystemException{
        isStartScan = false;
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            //serviceHelper.scannerCtrl.removeObserver(scannerObserver);
            Result res = serviceHelper.scannerCtrl.stop();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e) {
            log.error("[stopScan]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    private IMessageListener scannerListener;
    private IObserver scannerObserver = new IObserver.Stub() {
        @Override
        public void onMessage(String arg) throws RemoteException {
            if (scannerListener != null) {
                scannerListener.onMessage(arg);
            }
        }
    };

    protected void addScanListener(IMessageListener listener) throws DcdzSystemException {
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            scannerListener = listener;
            serviceHelper.scannerCtrl.addObserver(scannerObserver);
        }catch (RemoteException e) {
            log.error("[addScanListener]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }

    }

    protected void removeScanListener() throws DcdzSystemException {
        scannerListener = null;
        if (serviceHelper.scannerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            serviceHelper.scannerCtrl.removeObserver(scannerObserver);
        }catch (RemoteException e) {
            log.error("[removeScanListener]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    //#endregion

    //#region 读卡器业务代理
    public boolean startReadCardTask() throws DcdzSystemException{
        if (serviceHelper.cardReaderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            //serviceHelper.cardReaderCtrl.addObserver(cardReaderObserver);
            return serviceHelper.cardReaderCtrl.start();
        } catch (RemoteException e) {
            log.error("[startReadCardTask]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public boolean stopReadCardTask() throws DcdzSystemException{
        if (serviceHelper.cardReaderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            //serviceHelper.cardReaderCtrl.removeObserver(cardReaderObserver);
            return serviceHelper.cardReaderCtrl.stop();
        } catch (RemoteException e) {
            log.error("[stopReadCardTask]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    private IMessageListener cardReaderListener;
    private IObserver cardReaderObserver = new IObserver.Stub() {
        @Override
        public void onMessage(String arg) throws RemoteException {
            if (cardReaderListener != null) {
                cardReaderListener.onMessage(arg);
            }
        }
    };
    
    public void addCardReadListener(IMessageListener listener) throws DcdzSystemException {
        if (serviceHelper.cardReaderCtrl == null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            cardReaderListener = listener;
            serviceHelper.cardReaderCtrl.addObserver(cardReaderObserver);
        } catch (RemoteException e) {
            log.error("[addCardReadListener]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public void removeCardReadListener() throws DcdzSystemException {
        cardReaderListener = null;
        if (serviceHelper.cardReaderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            serviceHelper.cardReaderCtrl.removeObserver(cardReaderObserver);
        } catch (RemoteException e) {
            log.error("[removeCardReadListener]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }

    }
    //#endregion

    //#region 发卡器业务代理
    public String resetCardSender() throws DcdzSystemException{
        if (serviceHelper.cardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res =  serviceHelper.cardSenderCtrl.resetCardSender();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[resetCardSender]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryCardSenderStatus() throws DcdzSystemException{
        if (serviceHelper.cardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.cardSenderCtrl.queryStatus();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryCardSenderStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String moveCard2Front() throws DcdzSystemException{
        if (serviceHelper.cardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.cardSenderCtrl.moveCard2Front();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[moveCard2Front]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String moveCard2ReadPosition() throws DcdzSystemException{
        if (serviceHelper.cardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.cardSenderCtrl.moveCard2ReadPosition();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[moveCard2ReadPosition]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String recycleCard() throws DcdzSystemException{
        if (serviceHelper.cardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.cardSenderCtrl.recycleCard();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[recycleCard]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion

    //#region SIM发卡器业务代理
    public String resetSimCardSender() throws DcdzSystemException{
        if (serviceHelper.simCardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.simCardSenderCtrl.resetCardSender();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[resetSimCardSender]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String querySimCardSenderStatus() throws DcdzSystemException{
        if (serviceHelper.simCardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.simCardSenderCtrl.queryStatus();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[querySimCardSenderStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String moveSimCard2ReadPosition() throws DcdzSystemException{
        if (serviceHelper.simCardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.simCardSenderCtrl.moveCard2ReadPosition();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[moveSimCard2ReadPosition]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String moveSimCard2Front() throws DcdzSystemException{
        if (serviceHelper.simCardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.simCardSenderCtrl.moveCard2Front();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[moveSimCard2Front]", e);
        }
        return null;
    }

    public String recycleSimCard() throws DcdzSystemException{
        if (serviceHelper.simCardSenderCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.simCardSenderCtrl.recycleCard();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[recycleSimCard]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion

    //#region 打印机业务代理
    public String print(String data) throws DcdzSystemException {
        if (serviceHelper.printerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.printerCtrl.print(data);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[print]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryPrinterStatus() throws DcdzSystemException{
        if (serviceHelper.printerCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.printerCtrl.queryStatus();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryPrinterStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion
}
