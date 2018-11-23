package com.dcdz.drivers;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.os.Process;

import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.demo.business.ServiceHelper;
import com.hzdongcheng.drivers.config.ConfigFilesHelper;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.dcdz.drivers.utils.CrashLogUtil;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/26.
 */

public class PDApplication extends Application {
    public static String SDCARD_PATH;
    public static String CONFIG_PATH;
    public static String HOME_PATH = "/dcdz_drivers";
    public static String ERROR_PATH = HOME_PATH+"/error";
    public static String LOG_PATH   = HOME_PATH+"/logs";
    public static String DATA_PATH  = HOME_PATH+"/data";
    static Context context;
    Log4jUtils log4jUtils;
    private static PDApplication sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //初始化Context
        context = getApplicationContext();
        //初始化数据库LitePal
        LitePal.initialize(this);
        //初始化文件存储路径
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { //存在SD卡
            SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
        } else {
            SDCARD_PATH = Environment.getDataDirectory().toString();
        }
        //初始化全局异常
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.setLogPath(SDCARD_PATH + ERROR_PATH);
        crashHandler.init(getApplicationContext());

        //初始化Log4j
        Log4jUtils.initLog4jInAndroid(SDCARD_PATH + LOG_PATH +"/log.log");

        log4jUtils = Log4jUtils.createInstanse(this.getClass());
        log4jUtils.info("****************************************************");
        log4jUtils.info("*               服务进程启动  " + Process.myPid());
        log4jUtils.info("****************************************************\r\n");

        //初始化错误描述
        new SerialPortErrorCode();

        //创建data目录
        File file = new File(SDCARD_PATH + DATA_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

        int count = CrashLogUtil.clean(crashHandler.getLogPath());
        log4jUtils.info("==清理crash日志, 删除文件 " + count + " 个==");

        //初始化配置文件路径
        CONFIG_PATH = SDCARD_PATH + DATA_PATH;
        ConfigFilesHelper.deviceConfigPath = CONFIG_PATH + "/Config.xml";

        //加载配置信息
        ConfigManager.getInstance().loadObjectFromDeviceConfigFile();
        ConfigManager.getInstance().createOrUpdateDeviceConfigFile();
        DeviceManager.getInstance().loadBoxInfo();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static Context getContext(){
        return context;
    }

    public static PDApplication getInstance() {
        return sInstance;
    }

    //-------------关闭多个activity------------------
    private List<Activity> mList = new LinkedList<>();
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            ServiceHelper.getInstance().unBindService();
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //注意：次方法用于垃圾回收，如果手机内存小，或使用虚拟机测试，一定要注释掉这段代码
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
