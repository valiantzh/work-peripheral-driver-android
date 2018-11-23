package com.hzdongcheng.drivers.service.aidl;

import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.drivers.peripheral.IObserver;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.common.DriverConfig;
import com.hzdongcheng.drivers.peripheral.cardreader.event.CardReaderEvent;
import com.hzdongcheng.drivers.peripheral.cardreader.event.ICardReaderListener;
import com.hzdongcheng.drivers.peripheral.cardreader.helper.CardReaderHelper;
import com.hzdongcheng.drivers.peripheral.cardsender.ICardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.factory.CardSenderFactory;
import com.hzdongcheng.drivers.peripheral.electricmeter.IElectricmeter;
import com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.IViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.express.IExpressViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.helper.ELockerControlHelper;
import com.hzdongcheng.drivers.peripheral.elocker.initialize.IELocker;
import com.hzdongcheng.drivers.peripheral.elocker.invocation.ELockerControlProxy;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.IRotateControl;
import com.hzdongcheng.drivers.peripheral.plc.helper.RotateControlHelper;
import com.hzdongcheng.drivers.peripheral.plc.initialize.IPLCControl;
import com.hzdongcheng.drivers.peripheral.printer.IPrinter;
import com.hzdongcheng.drivers.peripheral.printer.factory.PrinterFactory;
import com.hzdongcheng.drivers.peripheral.scanner.IScanner;
import com.hzdongcheng.drivers.peripheral.scanner.event.IScannerListener;
import com.hzdongcheng.drivers.peripheral.scanner.event.ScannerEvent;
import com.hzdongcheng.drivers.peripheral.scanner.factory.ScannerFactory;
import com.hzdongcheng.drivers.peripheral.thermostat.IThermostat;
import com.hzdongcheng.drivers.peripheral.thermostat.initialize.ITherELocker;
import com.hzdongcheng.drivers.service.aidl.impl.locker.MasterController;
import com.hzdongcheng.drivers.service.aidl.impl.locker.SlaveController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.CardReaderController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.CardSenderController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.PrinterController;
import com.hzdongcheng.drivers.service.aidl.impl.rotation.ServoController;
import com.hzdongcheng.drivers.service.aidl.impl.rotation.RotationController;
import com.hzdongcheng.drivers.service.aidl.impl.peripheral.ScannerController;
import com.hzdongcheng.drivers.utils.JsonResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/2/11.
 */

public class DriversManager {
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private  ConfigManager configManager = ConfigManager.getInstance();

    public ScheduledExecutorService service= Executors.newSingleThreadScheduledExecutor();
    public ScheduledFuture<?> future = null;
    //锁控
    private static ELockerControlProxy proxy;
    private IELocker elockerCtrl;
    private IMainMachineControl mainMachineControl;
    private IExpressViceMachineControl expressViceMachineControl;

    //旋转控制
    private IPLCControl plcControl;
    private IRotateControl rotateCtrl;//旋转售卖柜
    //扫描枪
    private IScanner scannerCtrl;
    private Map<String, IScanner> scannerMap;
    //打印机
    private IPrinter printerCtrl;
    private Map<String, IPrinter> printerMap;
    //读卡器
    private CardReaderHelper cardReaderCtrl;
    private Map<String, CardReaderHelper> cardReaderMap;

    //发卡器
    private ICardSender cardSenderCtrl;
    private ICardSender simCardSenderCtrl;
    private Map<String, ICardSender> cardSenderMap;

    //温控
    private ITherELocker therELocker;
    private IThermostat thermostatCtrl;
    private Map<String, IThermostat> mapThermostatImpl = new ConcurrentHashMap<>();
    // 电表
    private IElectricmeter ammeterCtrl;
    private Map<String, IElectricmeter> mapAmmeterImpl = new ConcurrentHashMap<>();


    private Map<String, IBinder> driversBinderMap;

    private static class SingletonHolder{
        private final static DriversManager instance = new DriversManager();
    }
    public static DriversManager getInstance(){
        return SingletonHolder.instance;
    }
    private DriversManager(){
        scannerMap = new ConcurrentHashMap<>();
        printerMap = new ConcurrentHashMap<>();
        cardReaderMap = new ConcurrentHashMap<>();
        cardSenderMap = new ConcurrentHashMap<>();
        driversBinderMap = new ConcurrentHashMap<>();
    }

    /**
     * 初始化设备
     */
    public void  initialize(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][Drivers]==>initialize");
        _uninitialize();
  /*      if(getElockerCtrl() != null){  //TODO not available on rotate machine
            if(initializeElockerCtrl()){
                driversBinderMap.put("IMasterController", new MasterController());
                driversBinderMap.put("ISlaveController", new SlaveController());
            }
        }*/
        if(getSaleRotation() != null){
            if(initialize2PLC()){
                driversBinderMap.put("IServoController",new ServoController());
                driversBinderMap.put("IRotationController",new RotationController());
            }
        }
        if(getScannerCtrl() != null){
            if(initialize2Scanner()){
                driversBinderMap.put("IScannerController",new ScannerController());
            }
        }
        if(getCardReaderCtrl() != null){
            if(initialize2CardReader()){
                driversBinderMap.put("ICardReaderController",new CardReaderController());
            }
        }
        if(getPrinterCtrl() !=null){
            if(initialize2Printer()){
                driversBinderMap.put("IPrinterController",new PrinterController());
            }
        }
        if(getCardSenderCtrl() != null){
            if(initialize2CardSender()){
                driversBinderMap.put("ICardSenderController",new CardSenderController(getCardSenderCtrl()));
            }
        }
        if(getSimCardSenderCtrl() != null){
            if(initialize2SimCardSender()){
                driversBinderMap.put("ISimCardSenderController",new CardSenderController(getSimCardSenderCtrl()));
            }
        }
        if(getAmmeterCtrl() != null){
            if(initialize2Ammeter()){
                //TODO
            }
        }
        if(getThermostatCtrl() != null){
            if(initialize2Thermostat()){
                //TODO
            }
        }
        log4jUtils.info("[Service][Drivers]==>initialize 耗时 ==> "+ (System.currentTimeMillis() - start));
    }
    private void _uninitialize(){
        uninitializeElockerCtrl();
        uninitialize2PLC();
        uninitialize2Scanner();
        uninitialize2Printer();
        uninitialize2CardReader();
        unitialize2CardSender();
        uninitialize2Ammeter();
        uninitialize2Thermostat();
    }
    /**
     * 关闭设备
     */
    public  void  uninitialize(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][Drivers]==>uninitialize");
        _uninitialize();
        driversBinderMap.clear();
        log4jUtils.info("[Service][Drivers]==>uninitialize 耗时 ==> "+ (System.currentTimeMillis() - start));
    }


    public IBinder getDriversBinder(String aidlName){
        return driversBinderMap.get(aidlName);
    }
    //#region 旋转控制
    public IRotateControl getSaleRotation(){
        DriverConfig config = configManager.rotateConfigModel;
        if(!config.isEnabled()){
            return null;
        }
        RotateControlHelper rotateControlHelper = RotateControlHelper.getInstance();
        plcControl = rotateControlHelper.queryInterface(IPLCControl.class);
        rotateCtrl = rotateControlHelper.queryInterface(IRotateControl.class);
        return rotateCtrl;
    }
    private boolean initialize2PLC(){
        long start = System.currentTimeMillis();
        DriverConfig config = configManager.rotateConfigModel;
        String portName = config.getPortName();
        JsonResult jsonResult;
        try {
            plcControl.connect(portName);

            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        }catch (ModbusException e) {
            jsonResult = new JsonResult(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            jsonResult = new JsonResult(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][PLC控制] ==>打开PLC控制 -->连接串口" + portName + " 成功]");
        } else {
            log4jUtils.error("[服务][PLC控制] ==>打开PLC控制 -->连接串口" + portName + " 失败]"+jsonString);
        }
        log4jUtils.info("[服务][主控] ==>打开主控耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void uninitialize2PLC(){
        if(plcControl!=null){
            try {
                plcControl.disconnect();
            } catch (ModbusException e) {
                log4jUtils.error("[服务][主控] ==>关闭PLC连接 -->" + e);
            }
            log4jUtils.info("[服务][外设] ==>关闭PLC连接");
        }
    }
    //#endregion

    //#region 主机控制&锁控
    private IELocker getElockerCtrl(){
        proxy = ELockerControlHelper.createInstance();
        elockerCtrl = proxy.queryInterface(IELocker.class);
        mainMachineControl = proxy.queryInterface(IMainMachineControl.class);
        expressViceMachineControl = proxy.queryInterface(IExpressViceMachineControl.class);
        return elockerCtrl;
    }
    private boolean initializeElockerCtrl()
    {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 打开主控");
        String portName = configManager.eLockerConfigModel.getPortName();
        JsonResult jsonResult;
        try {
            elockerCtrl.open(portName);
            try { //开启心跳包功能
                log4jUtils.error("[服务][主控] ==>开启心跳包>");
                mainMachineControl.enableKeepAlive();
                future = service.scheduleWithFixedDelay(
                        futureRunnable, 60, 3 * 60, TimeUnit.SECONDS);
            } catch (DcdzSystemException e) {
                log4jUtils.error("[服务][主控] ==>开启心跳包失败 -->"+e);
            }
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            jsonResult = new JsonResult(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==>打开主控 -->主控串口" + portName + " 成功]");
        } else {
            log4jUtils.error("[服务][主控] ==>打开主控 -->主控串口" + portName + " 失败]"+jsonString);
        }
        log4jUtils.info("[服务][主控] ==>打开主控耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private  void  uninitializeElockerCtrl(){
        if (elockerCtrl!=null) {
            elockerCtrl.close();
            if (mainMachineControl!=null) {
                try {
                    mainMachineControl.disenableKeepAlive();
                } catch (DcdzSystemException e) {
                    log4jUtils.error("[服务][主控] ==>开启心跳包失败 -->" + e);
                }
                if (future != null)
                    future.cancel(true);
            }
            log4jUtils.info("[服务][外设] ==>关闭驱动板");
        }
    }

    public Runnable futureRunnable = new Runnable() {
        @Override
        public void run() {
            if (mainMachineControl!=null){
                try {
                    mainMachineControl.keepAlive();
                    log4jUtils.info("[服务][主控] ==>驱动板心跳发送成功");
                } catch (DcdzSystemException e) {
                    log4jUtils.error("[服务][主控] ==> 发送心跳包 -->失败" + " --" + e.getMessage());
                }
            }
        }
    };
    //获取主柜控制
    public IMainMachineControl getMainMachineCtrl(){
        return mainMachineControl;
    }
    /**
     * 获取副柜控制
     * @param slaveID 副机编号
     * @return
     */
    public IViceMachineControl getSlaveMachineCtrl(byte slaveID){
        return expressViceMachineControl;
    }
    //#endregion

    //#region 扫描枪控制
    public IScanner getScannerCtrl(){
        DriverConfig config = configManager.scannerConfig;
        if(!config.isEnabled()){
            return null;
        }
        String mapKey = config.getVendorName();
        synchronized (this){
            if (scannerMap.containsKey(mapKey)) {
                scannerCtrl = scannerMap.get(mapKey);
            }else{
                scannerCtrl = ScannerFactory.createInstance(configManager.scannerConfig);
                if (scannerCtrl != null) {
                    scannerMap.put(mapKey, scannerCtrl);
                }
            }
        }

        return scannerCtrl;
    }
    private boolean initialize2Scanner(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][Scan] ==>initialize");
        DriverConfig config =configManager.scannerConfig;
        String portName = config.getPortName();

        //RemoteCallbackList
        if(callbacks2Scanner != null){
            callbacks2Scanner.kill();
        }
        callbacks2Scanner = new RemoteCallbackList<>();
        log4jUtils.info("[Service][Scan]==>callbacks2Scanner.new");

        JsonResult jsonResult;
        try {
            scannerCtrl.open(portName);
            scannerCtrl.addListener(iScannerListener);
            //log4jUtils.info("[Service][Scan] ==>open 串口：" + portName + " -->Success");
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        }catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(), e.getMessage(), "");
        }
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() != ErrorCode.SUCCESS) {
            log4jUtils.error("[Service][Scan] ==>open port=" + portName + " -->Failed" + "--" + jsonString);
        }else{
            log4jUtils.info("[Service][Scan] ==>open port="+portName+" -->Success -->耗时 -->" + (System.currentTimeMillis() - start));
        }

        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void uninitialize2Scanner(){
        if (scannerCtrl == null) {
            return;
        }
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][Scan]==>uninitialize");
        if(callbacks2Scanner != null){
            callbacks2Scanner.kill();
            callbacks2Scanner = null;
            log4jUtils.info("[Service][Scan]==>callbacks2Scanner.kill");
        }
        JsonResult jsonResult;
        for (IScanner val : scannerMap.values()) {
            //val.removeAllListener();
            val.close();
        }

        scannerMap.clear();
        jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][Scan] ==>uninitialize -->Success 耗时 -->"+ (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][Scan] ==>uninitialize -->Failed" + jsonString);
        }
        //log4jUtils.info("[Service][Scan] ==>uninitialize耗时 -->" + (System.currentTimeMillis() - start));

    }
    private RemoteCallbackList<IObserver> callbacks2Scanner;
    private final IScannerListener iScannerListener = new IScannerListener() {
        @Override
        public void onNotice(ScannerEvent obj) {
            //log4jUtils.info("Scanner receive barcode is " + obj.getDataString());
            callback2Scanner(obj.getDataString());
        }
    };

    private void callback2Scanner(String arg) {
        if(callbacks2Scanner == null){
            log4jUtils.info("Scanner receive barcode is " + arg+",observer is null");
            return;
        }
        try{
            final int N = callbacks2Scanner.beginBroadcast();
            if(N > 0){
                for (int i=0; i<N; i++) {
                    try {
                        callbacks2Scanner.getBroadcastItem(i).onMessage( arg);
                        log4jUtils.info("Scanner receive barcode is [" + arg+"] iObserver==>"+i);
                    }
                    catch (RemoteException e) {
                        log4jUtils.error("Scanner receive barcode is [" + arg+"] iObserver==>"+i + ",but callback event execute failed." +  e.getMessage());
                    }
                }
            }else{
                log4jUtils.info("Scanner receive barcode is " + arg+",observer is null");
            }

        }catch (Exception e){
            log4jUtils.error("Scanner->Scanner beginBroadcast error->"+e.getMessage());
        }finally {
            try{
                callbacks2Scanner.finishBroadcast();
            }catch (Exception e){
                log4jUtils.error("Scanner->Scanner finishBroadcast error==>"+e.getMessage());
            }
        }
    }
    public void registerCallbacks2Scanner(IObserver observer){
        if(callbacks2Scanner != null){
            boolean isOk = callbacks2Scanner.register(observer);
            log4jUtils.info("registerCallbacks2Scanner isOk=" +isOk);
        }else{
            log4jUtils.error("registerCallbacks2Scanner error! callbacks2Scanner is null");
        }

    }
    public void unregisterCallbacks2Scanner(IObserver observer){
        if(callbacks2Scanner != null){
            boolean isOk = callbacks2Scanner.unregister(observer);
            log4jUtils.info("unregisterCallbacks2Scanner isOk=" +isOk);
        }else{
            log4jUtils.error("unregisterCallbacks2Scanner error! callbacks2Scanner is null");
        }

    }
    //#endregion

    //#region 打印机控制
    public IPrinter getPrinterCtrl(){
        DriverConfig config = configManager.printerConfig;
        if(!config.isEnabled()){
            return null;
        }
        String mapKey = config.getVendorName();
        synchronized (this){
            if(printerMap.containsKey(mapKey)){
                printerCtrl = printerMap.get(mapKey);
            }else{
                printerCtrl = PrinterFactory.createInstance(configManager.printerConfig);
                if(printerCtrl != null ){
                    printerMap.put(mapKey, printerCtrl);
                }
            }
        }

        return printerCtrl;
    }
    private boolean initialize2Printer(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][PrinterCtrl] ==>initialize");

        DriverConfig config = configManager.printerConfig;
        String portName = config.getPortName();
        JsonResult jsonResult;
        try {
            printerCtrl.open(portName);
            log4jUtils.info("[Service][PrinterCtrl] ==>打开串口：" + portName + " -->Success");
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        }catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(), e.getMessage(), "");
        }
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() != ErrorCode.SUCCESS) {
            log4jUtils.error("[Service][PrinterCtrl] ==>打开串口： " + portName + " -->Failed" + "--" + jsonString);
        }
        log4jUtils.info("[Service][PrinterCtrl] ==>打开耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void uninitialize2Printer(){
        if(printerCtrl==null){
            return;
        }
        log4jUtils.info("[Service][PrinterCtrl] ==>uninitialize");
        long start = System.currentTimeMillis();
        for(IPrinter val: printerMap.values()){
            val.close();
        }
        printerMap.clear();
        JsonResult jsonResult;
        jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][PrinterCtrl] ==>close -->Success");
        } else {
            log4jUtils.error("[Service][PrinterCtrl] ==>close -->Failed" + jsonString);
        }
        log4jUtils.info("[Service][PrinterCtrl] ==>关闭耗时 -->" + (System.currentTimeMillis() - start));
    }
    //#endregion

    //#region 读卡器控制
    public CardReaderHelper getCardReaderCtrl(){
        DriverConfig config = configManager.cardReaderConfig;
        if(!config.isEnabled()){
            return null;
        }
        String mapKey = config.getVendorName();
        synchronized (this){
            if(cardReaderMap.containsKey(mapKey)){
                cardReaderCtrl = cardReaderMap.get(mapKey);
            }else{
                cardReaderCtrl = CardReaderHelper.createInstance(configManager.cardReaderConfig);
                if(cardReaderCtrl != null){
                    cardReaderMap.put(mapKey, cardReaderCtrl);
                }
            }
        }

        return cardReaderCtrl;
    }

    private boolean initialize2CardReader(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardReader]==>initialize");
        DriverConfig config = configManager.cardReaderConfig;
        String portName = config.getPortName();

        //callbacks2CardReader
        if(callbacks2CardReader!=null){
            callbacks2CardReader.kill();
        }
        callbacks2CardReader = new RemoteCallbackList<>();
        log4jUtils.info("[Service][CardReaderCtrl]==>callbacks2CardReader.new");

        JsonResult jsonResult;
        try {
            cardReaderCtrl.open(portName);
            cardReaderCtrl.addListener(listener2CardReader);
            log4jUtils.info("[Service][CardReader] ==>open 串口：" + portName + " -->Success");
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        }catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() !=  ErrorCode.SUCCESS)  {
            log4jUtils.error("[Service][CardReader] ==>open 串口： " + portName + " -->Failed" + "--" + jsonString);
        }
        log4jUtils.info("[Service][CardReader] ==>initialize 耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void uninitialize2CardReader(){
        if(cardReaderCtrl == null){
            return;
        }
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardReader] ==>uninitialize");

        //callbacks2CardReader
        if(callbacks2CardReader != null){
            callbacks2CardReader.kill();
            callbacks2CardReader = null;
            log4jUtils.info("[Service][CardReader]==>callbacks2CardReader.kill");
        }

        JsonResult jsonResult;

        for(CardReaderHelper val: cardReaderMap.values()){
            val.close();
            val.removeListener();
        }
        cardReaderMap.clear();

        jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardReader] ==>close -->Success");
        } else {
            log4jUtils.error("[Service][CardReader] ==>close -->Failed" + jsonString);
        }
        log4jUtils.info("[Service][CardReader] ==>uninitialize耗时 -->" + (System.currentTimeMillis() - start));
    }


    private RemoteCallbackList<IObserver> callbacks2CardReader;
    private final ICardReaderListener listener2CardReader = new ICardReaderListener() {
        @Override
        public void onNotice(CardReaderEvent obj) {
            callback2CardReader(JsonUtils.toJSONString(obj));//obj.getCardInformation().getCardID()
        }
    };
    private void callback2CardReader(String arg) {
        if(callbacks2Scanner == null){
            log4jUtils.info("CardReader receive barcode is " + arg+",observer is null");
            return;
        }
        try{
            final int N = callbacks2CardReader.beginBroadcast();
            for (int i=0; i<N; i++) {
                try {
                    if (arg.length()>0) {
                        callbacks2CardReader.getBroadcastItem(i).onMessage(arg);
                    }
                    log4jUtils.info("CardReader read card info is " + arg);
                }
                catch (RemoteException e) {
                    log4jUtils.error("CardReader read card info is " + arg + ",but callback event execute failed." +  e.getMessage());
                }
            }
        }catch (Exception e){
            log4jUtils.error("CardReader->RemoteCallback beginBroadcast error->"+e.getMessage());
        }finally {
            try{
                callbacks2CardReader.finishBroadcast();
            }catch (Exception e){
                log4jUtils.error("CardReader->RemoteCallback finishBroadcast error->"+e.getMessage());
            }

        }
    }
    public void registerCallbacks2CardReader(IObserver observer){
        if(callbacks2CardReader != null){
            boolean isOk = callbacks2CardReader.register(observer);
            log4jUtils.info("registerCallbacks2CardReader isOk=" +isOk);
        }else{
            log4jUtils.error("registerCallbacks2CardReader:callbacks2CardReader is null");
        }

    }
    public void unregisterCallbacks2CardReader(IObserver observer){
        if(callbacks2CardReader != null){
            boolean isOk = callbacks2CardReader.unregister(observer);
            log4jUtils.info("unregisterCallbacks2CardReader isOk=" +isOk);
        }else{
            log4jUtils.error("unregisterCallbacks2CardReader:callbacks2CardReader is null");
        }
    }
    //#endregion

    //#region 发卡器控制
    private ICardSender _getCardSenderCtrl(boolean isSimCard){
        DriverConfig config = null;
        if(isSimCard){
            config = configManager.simCardSenderConfig;
        }else{
            config = configManager.cardSenderConfig;
        }
        if(!config.isEnabled()){
            return null;
        }
        ICardSender cardSenderCtrl = null;
        String mapKey = config.getVendorName();
        synchronized (this){
            if(cardSenderMap.containsKey(mapKey)){
                cardSenderCtrl = cardSenderMap.get(mapKey);
            }else{
                cardSenderCtrl = CardSenderFactory.createInstance(config);
                if(cardSenderCtrl != null){
                    cardSenderMap.put(mapKey,cardSenderCtrl);
                }
            }
        }
        return cardSenderCtrl;
    }
    public ICardSender getCardSenderCtrl(){
        cardSenderCtrl = _getCardSenderCtrl(false);
        return cardSenderCtrl;
    }
    public ICardSender getSimCardSenderCtrl(){
        simCardSenderCtrl = _getCardSenderCtrl(true);
        return simCardSenderCtrl;
    }
    private boolean initialize2CardSender(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][CardSenderCtrl] ==>initialize");
        DriverConfig config = configManager.cardSenderConfig;
        String portName = config.getPortName();
        JsonResult jsonResult;
        try {
            cardSenderCtrl.open(portName);
            log4jUtils.info("[Service][CardSenderCtrl] ==>打开串口：" + portName + " -->Success");
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");

        }catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(), e.getMessage(), "");
        }
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() != ErrorCode.SUCCESS) {
            log4jUtils.error("[Service][CardSenderCtrl] ==>打开串口： " + portName + " -->Failed" + "--" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>打开耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private boolean initialize2SimCardSender(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][SimCardSenderCtrl] ==>initialize");
        DriverConfig config = configManager.simCardSenderConfig;
        String portName = config.getPortName();
        JsonResult jsonResult;
        try {
            simCardSenderCtrl.open(portName);
            log4jUtils.info("[Service][SimCardSenderCtrl] ==>打开串口：" + portName + " -->Success");
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");

        }catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(), e.getMessage(), "");
        }
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() != ErrorCode.SUCCESS) {
            log4jUtils.error("[Service][SimCardSenderCtrl] ==>打开串口： " + portName + " -->Failed" + "--" + jsonString);
        }
        log4jUtils.info("[Service][SimCardSenderCtrl] ==>打开耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void unitialize2CardSender(){
        if(cardSenderCtrl==null && simCardSenderCtrl==null){
            return;
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>unitialize");
        JsonResult jsonResult;
        long start = System.currentTimeMillis();
        for(ICardSender val : cardSenderMap.values()){
            val.close();
        }
        jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][CardSenderCtrl] ==>close -->Success");
        } else {
            log4jUtils.error("[Service][CardSenderCtrl] ==>close -->Failed" + jsonString);
        }
        log4jUtils.info("[Service][CardSenderCtrl] ==>关闭耗时 -->" + (System.currentTimeMillis() - start));
    }
    //#endregion

    //#region 温度控制
    public IThermostat  getThermostatCtrl(){
        DriverConfig config = configManager.thermostatConfig;
        if(!config.isEnabled()){
            return null;
        }
        ELockerControlProxy proxy = ELockerControlHelper.createInstance();
        therELocker = proxy.queryInterface(ITherELocker.class);
        String vendorName = configManager.thermostatConfig.getVendorName();
        if (mapThermostatImpl.containsValue(vendorName)) {
            thermostatCtrl = mapThermostatImpl.get(vendorName);
        } else {
            thermostatCtrl  = proxy.queryInterface(IThermostat.class);
            mapThermostatImpl.put(vendorName, thermostatCtrl);
        }
        log4jUtils.info("[Service][Thermostat] ==>添加温控器");
        return thermostatCtrl;
    }
    private boolean initialize2Thermostat(){
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][Thermostat] ==>initialize");
        JsonResult jsonResult;
        String portName = configManager.thermostatConfig.getPortName();
        try {
            therELocker.open(portName);
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            jsonResult = new JsonResult(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }

        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[Service][Thermostat] ==>打开温控器串口：" + portName + " -->成功");
        } else {
            log4jUtils.info("[Service][Thermostat] ==>打开温控器串口：" + portName + " -->失败 --" + jsonString);
        }
        log4jUtils.info("[Service][Thermostat] ==>打开温控器耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void uninitialize2Thermostat(){
        if (therELocker != null) {
            therELocker.close();
            mapThermostatImpl.clear();
            log4jUtils.info("[Service][Thermostat] ==>关闭温控器");
        }
    }
    //#endregion

    //#region 电表控制
    public IElectricmeter getAmmeterCtrl(){
        //TODO 功能待添加
        /*String vendorName = configManager.ammeterConfig.getVendorName();
        if (mapAmmeterImpl.containsValue(vendorName)) {
            ammeterCtrl = mapAmmeterImpl.get(vendorName);
        } else {
            ammeterCtrl = ElectricmeterFactory.createHBElectricmeter();
            mapAmmeterImpl.put(vendorName, ammeterCtrl);
        }
        log4jUtils.info("[Service][Ammeter] ==>添加电表");*/
        return ammeterCtrl;
    }
    private boolean initialize2Ammeter(){
        //TODO 功能待添加
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][Ammeter] ==>initialize");
        String portName = "";//configManager.ammeterConfig.getPortName();
        JsonResult jsonResult;
        try {
            ammeterCtrl.open(portName);
            jsonResult = new JsonResult(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            jsonResult = new JsonResult(e.getErrorCode(), e.getMessage(), "");
        } catch (Exception e) {
            jsonResult = new JsonResult(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT, e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(jsonResult);
        if (jsonResult.getErrorCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[Service][Ammeter] ==>打开电表串口：" + portName + " -->成功");
        } else {
            log4jUtils.info("[Service][Ammeter] ==>打开电表串口：" + portName + " -->失败 --" + jsonString);
        }
        log4jUtils.info("[Service][Ammeter] ==>打开读卡器耗时 -->" + (System.currentTimeMillis() - start));
        return jsonResult.getErrorCode() == ErrorCode.SUCCESS;
    }
    private void uninitialize2Ammeter(){
        if (ammeterCtrl!=null) {
            for (IElectricmeter val : mapAmmeterImpl.values()) {
                val.close();
            }
            mapAmmeterImpl.clear();
            log4jUtils.info("[Service][Ammeter] ==uninitialize");
        }
    }
    //#endregion

    //#region ==
    //#endregion
}
