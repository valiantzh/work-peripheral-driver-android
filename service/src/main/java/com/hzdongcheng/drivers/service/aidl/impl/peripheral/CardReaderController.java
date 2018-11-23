package com.hzdongcheng.drivers.service.aidl.impl.peripheral;

import android.os.RemoteException;

import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.service.aidl.DriversManager;
import com.hzdongcheng.drivers.peripheral.IObserver;
import com.hzdongcheng.drivers.peripheral.cardreader.ICardReaderController;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;


/**
 * Created by Administrator on 2018/3/8.
 */

public class CardReaderController extends ICardReaderController.Stub {
    DriversManager driversManager = DriversManager.getInstance();
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    public CardReaderController(){
    }

    @Override
    public Result writeToBlock(byte sectorNo, byte blockNo, String keyA, String keyB, String data) throws RemoteException {
        return null;
    }

    @Override
    public Result readFromBlock(byte sectorNo, byte blockNo, String keyA, String keyB) throws RemoteException {
        return null;
    }

    @Override
    public Result readFromSector(byte sectorNo, String keyA, String keyB) throws RemoteException {
        return null;
    }

    @Override
    public boolean start() throws RemoteException {
        log4jUtils.info("Start reading card.");
        boolean rc =driversManager.getCardReaderCtrl().startReadCardTask();
        return rc;
    }

    @Override
    public boolean stop() throws RemoteException {
        log4jUtils.info("Stop reading card.");
        driversManager.getCardReaderCtrl().stopReadCardTask();
        return true;
    }

    @Override
    public boolean addObserver(IObserver observer) throws RemoteException {
        if (observer == null){
            log4jUtils.error("Add CardReader Observer failed. because param is null.");
            return  false;
        }
        driversManager.registerCallbacks2CardReader(observer);
        log4jUtils.info("Add CardReader Observer succeed.");
        return true;
    }

    @Override
    public boolean removeObserver(IObserver observer) throws RemoteException {
        if (observer != null) {
            driversManager.unregisterCallbacks2CardReader(observer);
        }
        log4jUtils.info("Remove CardReader Observer succeed.");
        return true;
    }
}
