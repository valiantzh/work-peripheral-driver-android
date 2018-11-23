package com.hzdongcheng.drivers.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.common.Constants;
import com.hzdongcheng.drivers.service.aidl.impl.DriversCtrlFactory;
import com.hzdongcheng.drivers.service.aidl.DriversManager;


public class DriverService extends Service {
    DriversManager driversManager = DriversManager.getInstance();
    private Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private DriversCtrlFactory binder = new DriversCtrlFactory();
    public DriverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log4jUtils.info("Peripheral Drivers service started.");

        //设置成前台服务
        Notification.Builder builder = new Notification.Builder(this);
        //builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setTicker("The peripheral service started");
        builder.setContentTitle("Dcdz peripheral Service");
        builder.setContentText("The peripheral service run in the foreground.");
        Notification notification = builder.build();
        startForeground(Constants.NOTIFICATION_ID_PERIPHERAL, notification);
        driversManager.initialize();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        log4jUtils.info("Peripheral service is bind.");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        log4jUtils.info("Peripheral service is unbind.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true); //移除前台服务
        log4jUtils.info("Peripheral service destroyed.");
        driversManager.uninitialize();
    }
}
