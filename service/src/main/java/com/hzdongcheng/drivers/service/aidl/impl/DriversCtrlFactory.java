package com.hzdongcheng.drivers.service.aidl.impl;


import android.os.IBinder;
import android.os.RemoteException;

import com.hzdongcheng.drivers.service.aidl.DriversManager;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.CardReaderController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.CardSenderController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.PrinterController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.ScannerController;
import com.hzdongcheng.drivers.IDriversCtrlFactory;

/**
 * Created by Administrator on 2018/3/11.
 */

public class DriversCtrlFactory extends IDriversCtrlFactory.Stub {
    private DriversManager driversManager = DriversManager.getInstance();

    @Override
    public IBinder getScannerCtrl() throws RemoteException {
        if(driversManager.getScannerCtrl() == null ){
            return null;
        }
        return new ScannerController();
    }


    @Override
    public IBinder getPrinterCtrl() throws RemoteException {
        if(driversManager.getPrinterCtrl()==null){
            return null;
        }
        return new PrinterController();
    }

    @Override
    public IBinder getCardReaderCtrl() throws RemoteException {
        if(driversManager.getCardReaderCtrl() == null){
            return null;
        }
        return new CardReaderController();
    }
    @Override
    public IBinder getCardSenderCtrl() throws RemoteException {
        if(driversManager.getCardSenderCtrl() == null){
            return null;
        }
        return new CardSenderController(driversManager.getCardSenderCtrl());
    }

    @Override
    public IBinder getSimCardSenderCtrl() throws RemoteException {
        if(driversManager.getSimCardSenderCtrl() == null){
            return null;
        }
        return new CardSenderController(driversManager.getSimCardSenderCtrl());
    }

    @Override
    public IBinder getService(String serviceName) throws RemoteException {
        return driversManager.getDriversBinder(serviceName);
    }
}
