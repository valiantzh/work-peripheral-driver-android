package com.hzdongcheng.drivers.config;


import com.hzdongcheng.components.toolkits.utils.NumberUtils;
import com.hzdongcheng.drivers.common.BaseConfig;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardReaderConfig;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderConfig;
import com.hzdongcheng.drivers.peripheral.electricmeter.model.AmmeterConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.DeskConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.ELockerConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBox;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterConfig;
import com.hzdongcheng.drivers.peripheral.scanner.model.ScannerConfig;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatConfig;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2018/2/9.
 */

public class ConfigManager {
    public String apparch = "";
    public ELockerConfig eLockerConfigModel;//自提柜系列配置
    public RotateConfig rotateConfigModel; //旋转柜系统配置

    public ScannerConfig scannerConfig;
    public CardReaderConfig cardReaderConfig;
    public PrinterConfig printerConfig;

    public CardSenderConfig cardSenderConfig;//充值卡发卡器
    public CardSenderConfig simCardSenderConfig;//SIM卡发卡器，带读写功能

    /*public AppVersions appVersions;
    public AppConfigModel appConfigModel;
    public IPCConfigModel ipcConfigModel;
    public HeaterConfigModel heaterConfig;*/
    //
    public AmmeterConfig ammeterConfig;
    public ThermostatConfig thermostatConfig;

    public Map<String, BaseConfig> configMap = new HashMap<>();

    public Map<String, Byte> DeskContrastMap = new HashMap<>();
    public Map<String, Byte> BoxContrastMap = new HashMap<>();
    public Map<String, Byte> BoxDeskMap = new HashMap<>();

    public ConcurrentHashMap<String, ShelfBox> shelfBoxMap = new ConcurrentHashMap<>();


    private ConfigManager(){
        //loadObjectFromDeviceConfigFile();
    }
    private static class SingletonHolder{
        private final static ConfigManager instance = new ConfigManager();
    }
    public static ConfigManager getInstance(){
        return SingletonHolder.instance;
    }
    //删除config文件
    public void deletedFromDeviceConfig() {
        File file = new File(ConfigFilesHelper.deviceConfigPath);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
    }

    public void createOrUpdateDeviceConfigFile() {
        configMap.put(ScannerConfig.CONFIG_NAME, scannerConfig);
        configMap.put(CardReaderConfig.CONFIG_NAME, cardReaderConfig);
        configMap.put(PrinterConfig.CONFIG_NAME, printerConfig);

        configMap.put(CardSenderConfig.CONFIG_NAME, cardSenderConfig);
        configMap.put(CardSenderConfig.CONFIG_NAME_SIM,simCardSenderConfig);
        configMap.put(RotateConfig.CONFIG_NAME, rotateConfigModel);
        configMap.put(ThermostatConfig.CONFIG_NAME,thermostatConfig);
        configMap.put(AmmeterConfig.CONFIG_NAME,ammeterConfig);
        configMap.put(ELockerConfig.CONFIG_NAME, eLockerConfigModel);
        ConfigFilesHelper.createOrUpdateDeviceConfigFile(configMap);
    }
    public void loadObjectFromDeviceConfigFile() {
        //加载配置文件
        configMap = ConfigFilesHelper.loadObjectFromDeviceConfigFile();
        //
        scannerConfig = (ScannerConfig) configMap.get(ScannerConfig.CONFIG_NAME);
        cardReaderConfig = (CardReaderConfig) configMap.get(CardReaderConfig.CONFIG_NAME);
        printerConfig = (PrinterConfig) configMap.get(PrinterConfig.CONFIG_NAME);
        cardSenderConfig = (CardSenderConfig)configMap.get(CardSenderConfig.CONFIG_NAME);
        simCardSenderConfig = (CardSenderConfig)configMap.get(CardSenderConfig.CONFIG_NAME_SIM);

        rotateConfigModel = (RotateConfig) configMap.get(RotateConfig.CONFIG_NAME);

        eLockerConfigModel = (ELockerConfig) configMap.get(ELockerConfig.CONFIG_NAME);
        ammeterConfig = (AmmeterConfig) configMap.get(AmmeterConfig.CONFIG_NAME);
        thermostatConfig = (ThermostatConfig) configMap.get(ThermostatConfig.CONFIG_NAME);
        /*
        appConfigModel = (AppConfigModel) configMap.get(Constants.CONFIG_NAME_APP);
        ipcConfigModel = (IPCConfigModel) configMap.get(Constants.CONFIG_NAME_IPC);
        heaterConfig = (HeaterConfigModel) configMap.get(Constants.CONFIG_NAME_HEATER);*/



        //
        if (scannerConfig == null) {
            scannerConfig = new ScannerConfig();
            scannerConfig.setPortName("COM3");
            scannerConfig.setNormallyOn(true);
            scannerConfig.setVendorName(ScannerConfig.SCANNER_VENDOR_NEWLAND);
        }
        if(printerConfig == null){
            printerConfig = new PrinterConfig();
            printerConfig.setVendorName(PrinterConfig.PRINTER_VENDOR_WEIHUANG);
            printerConfig.setPortName("COM2");
        }
        if (cardReaderConfig == null) {
            cardReaderConfig = new CardReaderConfig();
            cardReaderConfig.setVendorName(CardReaderConfig.CARDREADER_VENDOR_JL);
            cardReaderConfig.setPortName("COM3");
        }
        if(cardSenderConfig == null){
            cardSenderConfig = new CardSenderConfig();
            cardSenderConfig.setVendorName(CardSenderConfig.CARDSENDER_VENDOR_TYCS);
            cardSenderConfig.setReadAndWrite(0);//0-不支持读写卡
            cardSenderConfig.setPortName("COM4");

        }
        if(simCardSenderConfig ==null){
            simCardSenderConfig = new CardSenderConfig();
            simCardSenderConfig.setVendorName(CardSenderConfig.CARDSENDER_VENDOR_ATC_SIM);
            simCardSenderConfig.setReadAndWrite(2);//2-支持读&写卡
            simCardSenderConfig.setPortName("COM5");
        }
        if (rotateConfigModel == null) {
            rotateConfigModel = new RotateConfig();
            rotateConfigModel.setPortName("COM1");
            rotateConfigModel.setRotateType("");
            rotateConfigModel.setEnabled(true);
        }
        if (eLockerConfigModel == null) {
            eLockerConfigModel = new ELockerConfig();
            eLockerConfigModel.setPortName("COM2");
            eLockerConfigModel.setElockerType("");
        }

        if (ammeterConfig == null) {
            ammeterConfig = new AmmeterConfig();
            ammeterConfig.setPortName("COM1");
            ammeterConfig.setVendorName("HB");
        }
        if (thermostatConfig == null) {
            thermostatConfig = new ThermostatConfig();
            thermostatConfig.setPortName("COM4");
        }

        /*
        if (heaterConfig == null) {
            heaterConfig = new HeaterConfigModel();
            heaterConfig.setPortName("COM5");
            heaterConfig.setVendorName("WDF");
        }

        if (appConfigModel == null) {
            appConfigModel = new AppConfigModel();
            appConfigModel.setId("");
        }

        if (ipcConfigModel == null) {
            ipcConfigModel = new IPCConfigModel();
            ipcConfigModel.setVendorName(Constants.IPC_VENDOR_WEISHENG);
        }*/

        GoinDeskContrast();
    }

    public void GoinDeskContrast() {
        if(rotateConfigModel != null){
            if(rotateConfigModel.getDeskCount()>0){
                for(ShelfDeskConfig deskConfigModel : rotateConfigModel.getDeskList()){
                    if(deskConfigModel.getLayerCount() > 0){
                        for(ShelfLayerConfig shelfLayerConfig : deskConfigModel.getLayerList()){
                            if(shelfLayerConfig.getBoxCount()>0){
                                for(ShelfBoxConfig boxConfig: shelfLayerConfig.getBoxList()){
                                    ShelfBox shelfBox = new ShelfBox();
                                    shelfBox.setBoxNo(boxConfig.getDisplayName());
                                    shelfBox.setOfDeskNo((byte) NumberUtils.parseInt(deskConfigModel.getSlaveID()));
                                    shelfBox.setOfLayerNo((byte)NumberUtils.parseInt(shelfLayerConfig.getLayerID()));
                                    shelfBox.setOfColNo((byte)NumberUtils.parseInt(boxConfig.getBoxID()));
                                    shelfBox.setBoxType(shelfLayerConfig.getLayerType());
                                    shelfBoxMap.put(boxConfig.getDisplayName(),shelfBox);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (eLockerConfigModel != null) {
            if (eLockerConfigModel.getDeskCount() > 0) {
                for (DeskConfig deskConfigModel : eLockerConfigModel.getDeskList()) {
                    DeskContrastMap.put(deskConfigModel.getDisplayName(), (byte) Integer.parseInt(deskConfigModel.getBoardID()));
                    if (deskConfigModel.getBoxCount() > 0) {
                        for (BoxConfig boxConfigModel : deskConfigModel.getBoxList()) {
                            BoxContrastMap.put(boxConfigModel.getDisplayName(), (byte) Integer.parseInt(boxConfigModel.getBoxID()));
                            BoxDeskMap.put(boxConfigModel.getDisplayName(), (byte) Integer.parseInt(deskConfigModel.getBoardID()));
                        }
                    }
                }
            }
        }

    }
}
