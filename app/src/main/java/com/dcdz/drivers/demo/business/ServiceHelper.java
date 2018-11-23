package com.dcdz.drivers.demo.business;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.IDriversCtrlFactory;
import com.hzdongcheng.drivers.locker.IMasterController;
import com.hzdongcheng.drivers.locker.ISlaveController;
import com.hzdongcheng.drivers.peripheral.cardreader.ICardReaderController;
import com.hzdongcheng.drivers.peripheral.cardsender.ICardSenderController;
import com.hzdongcheng.drivers.peripheral.printer.IPrinterController;
import com.hzdongcheng.drivers.peripheral.scanner.IScannerController;
import com.hzdongcheng.drivers.rotation.IRotationController;
import com.hzdongcheng.drivers.rotation.IServoController;


/**
 * 柜机设备接口服务助手
 * Created by Administrator on 2018/2/26.
 */

public class ServiceHelper {
    private Log4jUtils log = Log4jUtils.createInstanse(this.getClass());
    private IDriversCtrlFactory driversCtrlFactory;
    IScannerController scannerCtrl;
    ICardReaderController cardReaderCtrl;
    IPrinterController printerCtrl;
    ICardSenderController cardSenderCtrl;
    ICardSenderController simCardSenderCtrl;
    IRotationController rotationCtrl;

    IMasterController masterController;
    ISlaveController slaveController;

    IServoController servoCtrl;


    private Context context;
    private Handler handler;

    private final static String PACKAGE_NAME = "com.dcdz.drivers";

    private ServiceHelper() {

    }
    private static class SingletonHolder{
        private  final static ServiceHelper instance = new ServiceHelper();
    }
    public static ServiceHelper getInstance(){
        return SingletonHolder.instance;
    }
    public void bindService(Context  context){
        this.context = context;
        handler = new Handler(context.getMainLooper());

        log.info("[demo] == drivers bindService==");
        Intent PeripheralIntent = new Intent("com.dcdz.drivers.service");
        PeripheralIntent.setPackage(PACKAGE_NAME);
        context.bindService(PeripheralIntent, driversServiceConnection, Context.BIND_AUTO_CREATE);

    }

    public void unBindService() {
        log.info("[demo] == drivers unBindService==");
        if (context != null) {
            try {
                context.unbindService(driversServiceConnection);
            } catch (Exception ignore) {
            }
        }
    }
    private ServiceConnection driversServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            driversCtrlFactory = IDriversCtrlFactory.Stub.asInterface(iBinder);

            if (handler != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //masterController
                            masterController = IMasterController.Stub.asInterface(driversCtrlFactory.getService("IMasterController"));
                            if (masterController != null) {
                                log.info("[demo] ==InitSuccess-masterController==");
                            } else {
                                log.info("[demo] ==InitFailed-masterController==");
                            }

                            //slaveController
                            slaveController = ISlaveController.Stub.asInterface(driversCtrlFactory.getService("ISlaveController"));
                            if (slaveController != null) {
                                log.info("[demo] ==InitSuccess-slaveController==");
                            } else {
                                log.info("[demo] ==InitFailed-slaveController==");
                            }

                            //scannerCtrl
                            scannerCtrl = IScannerController.Stub.asInterface(driversCtrlFactory.getService("IScannerController"));
                            if (scannerCtrl != null) {
                                log.info("[demo] ==InitSuccess-scannerCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-scannerCtrl==");
                            }

                            //printerCtrl
                            printerCtrl = IPrinterController.Stub.asInterface(driversCtrlFactory.getService("IPrinterController"));
                            if (printerCtrl != null) {
                                log.info("[demo] ==InitSuccess-printerCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-printerCtrl==");
                            }

                            //cardReaderCtrl
                            cardReaderCtrl = ICardReaderController.Stub.asInterface(driversCtrlFactory.getService("ICardReaderController"));
                            if (cardReaderCtrl != null) {
                                log.info("[demo] ==InitSuccess-cardReaderCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-cardReaderCtrl==");
                            }
                            //cardSenderCtrl
                            cardSenderCtrl = ICardSenderController.Stub.asInterface(driversCtrlFactory.getService("ICardSenderController"));

                            if (cardSenderCtrl != null) {
                                log.info("[demo] ==InitSuccess-cardSenderCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-cardSenderCtrl==");
                            }

                            //simCardSenderCtrl
                            simCardSenderCtrl = ICardSenderController.Stub.asInterface(driversCtrlFactory.getService("ISimCardSenderController"));
                            if (simCardSenderCtrl != null) {
                                log.info("[demo] ==InitSuccess-simCardSenderCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-simCardSenderCtrl==");
                            }

                            //RotationCtrl
                            rotationCtrl = IRotationController.Stub.asInterface(driversCtrlFactory.getService("IRotationController"));
                            if (rotationCtrl != null) {
                                log.info("[demo] ==InitSuccess-rotationCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-rotationCtrl==");
                            }

                            //rotateServoCtrl
                            servoCtrl = IServoController.Stub.asInterface(driversCtrlFactory.getService("IServoController"));
                            if (servoCtrl != null) {
                                log.info("[demo] ==InitSuccess-servoCtrl==");
                            } else {
                                log.info("[demo] ==InitFailed-servoCtrl==");
                            }
                        } catch (RemoteException e) {
                            log.error("[demo]==InitFailed-driver_service ==", e);
                        }
                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //Log.d("ServiceHelper","[demo]==ServiceDisconnected-Driver ==" + componentName.getClassName());
            log.info("[demo]==ServiceDisconnected-Peripheral ==" + componentName.getClassName());
        }
    };
}
