package com.hzdongcheng.drivers.config;

import com.hzdongcheng.drivers.common.BaseConfig;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardReaderConfig;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderConfig;
import com.hzdongcheng.drivers.peripheral.electricmeter.model.AmmeterConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.ELockerConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterConfig;
import com.hzdongcheng.drivers.peripheral.scanner.model.ScannerConfig;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.XmlUtils;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatConfig;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jxing on 2017/10/16.
 */

public class ConfigFilesHelper {

    public static String deviceConfigPath;

    public static synchronized void createOrUpdateDeviceConfigFile(Map<String, BaseConfig> objectMap) {

        Document document = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("Config");
        document.setRootElement(root);

        for (Map.Entry<String, BaseConfig> entry : objectMap.entrySet()) {
            String name = entry.getKey();
            String value = JsonUtils.toJSONString(entry.getValue());
            Element element = root.addElement(name);
            element.setText(value);
        }

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");//设置编码格式
        XMLWriter xmlWriter;
        try {
            xmlWriter = new XMLWriter(new FileOutputStream(deviceConfigPath), format);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, BaseConfig> loadObjectFromDeviceConfigFile() {

        Map<String, BaseConfig> map = new HashMap<>();

        Document document = XmlUtils.load(deviceConfigPath);
        if (document != null) {
            Element root = document.getRootElement();

            List<Element> childElements = root.elements();

            for (Element e : childElements) {
                String name = e.getName();
                String value = e.getTextTrim();

                if (ScannerConfig.CONFIG_NAME.equalsIgnoreCase(name)) {
                    map.put(ScannerConfig.CONFIG_NAME, JsonUtils.toBean(value, ScannerConfig.class));
                }else if(PrinterConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(PrinterConfig.CONFIG_NAME,JsonUtils.toBean(value, PrinterConfig.class));
                }else if(CardReaderConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(CardReaderConfig.CONFIG_NAME,JsonUtils.toBean(value, CardReaderConfig.class));
                }else if(CardSenderConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(CardSenderConfig.CONFIG_NAME,JsonUtils.toBean(value, CardSenderConfig.class));
                }else if(CardSenderConfig.CONFIG_NAME_SIM.equalsIgnoreCase(name)){
                    map.put(CardSenderConfig.CONFIG_NAME_SIM,JsonUtils.toBean(value, CardSenderConfig.class));
                }else if(RotateConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(RotateConfig.CONFIG_NAME,JsonUtils.toBean(value, RotateConfig.class));
                }else if(ELockerConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(ELockerConfig.CONFIG_NAME,JsonUtils.toBean(value, ELockerConfig.class));
                }else if(ThermostatConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(ThermostatConfig.CONFIG_NAME,JsonUtils.toBean(value, ThermostatConfig.class));
                }else if(AmmeterConfig.CONFIG_NAME.equalsIgnoreCase(name)){
                    map.put(AmmeterConfig.CONFIG_NAME,JsonUtils.toBean(value, AmmeterConfig.class));
                }
            }
        }

        return map;
    }
}
