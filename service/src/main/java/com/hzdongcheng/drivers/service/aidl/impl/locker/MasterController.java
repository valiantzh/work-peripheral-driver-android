package com.hzdongcheng.drivers.service.aidl.impl.locker;

import android.os.RemoteException;

import com.alibaba.fastjson.JSONObject;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.locker.IMasterController;
import com.hzdongcheng.drivers.peripheral.elocker.model.MainMachineStatus;
import com.hzdongcheng.drivers.service.aidl.DriversManager;

import java.util.HashMap;
import java.util.Map;

public class MasterController extends IMasterController.Stub{
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private DriversManager driverWrapper = DriversManager.getInstance();
    @Override
    public Result queryMainStatus() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 获取主机状态");
        Result result;
        try {
            MainMachineStatus mainMachineStatus = driverWrapper.getMainMachineCtrl().queryMainStatus();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(mainMachineStatus));
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 获取主机状态 -->成功");
        } else {
            log4jUtils.error("[服务][主控] ==> 获取主机状态 -->失败"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==>获取主机状态耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result weakCurrentManage(Map channelValues) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 弱电控制");
        Result result;

        if (channelValues == null || channelValues.size() == 0){
            result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT), "");
            return result ;
        }

        int[] channelNo = new int[16];
        int[] values = new int[16];

        for (Map.Entry<Byte, Boolean> entry : (Iterable<Map.Entry<Byte, Boolean>>) channelValues.entrySet()) {
            Byte key = entry.getKey();
            Boolean value = entry.getValue();
            if (key > 0 && key < 16) {
                channelNo[key] = key;
                values[key] = (byte) (value ? 1 : 0);
            }
        }

        try {
            driverWrapper.getMainMachineCtrl().weakCurrentManage(channelNo, values);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 弱电控制 -->成功");
        } else {
            log4jUtils.error("[服务][主控] ==> 弱电控制 -->失败"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==>弱电控制 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result strongCurrentManage(Map channelValues) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 强电控制");
        Result result;

        if (channelValues == null || channelValues.size() == 0)
            return new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT), "");

        int[] channelNo = new int[8];
        int[] values = new int[8];

        for (Map.Entry<Byte, Boolean> entry : (Iterable<Map.Entry<Byte, Boolean>>) channelValues.entrySet()) {
            Byte key = entry.getKey();
            Boolean value = entry.getValue();
            if (key > 0 && key < 8) {
                channelNo[key] = key;
                values[key] = (byte) (value ? 1 : 0);
            }
        }
        try {
            driverWrapper.getMainMachineCtrl().strongCurrentManage(channelNo, values);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(), "");
        }
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[服务][主控] ==> 强电控制 -->成功");
        } else {
            log4jUtils.error("[服务][主控] ==> 强电控制 -->失败" + JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 强电控制耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result toggleMasterLamp(boolean enabled) throws RemoteException {
        Map<Byte, Boolean> channelValues = new HashMap<>();
        channelValues.put((byte)3, enabled);
        log4jUtils.info("[服务][主控] ==> 主灯箱"+(enabled ? "亮" : "灭"));
        return weakCurrentManage(channelValues);
    }

    @Override
    public Result toggleSlaveLamp(boolean enabled) throws RemoteException {
        Map<Byte, Boolean> channelValues = new HashMap<>();
        channelValues.put((byte)2, enabled);
        log4jUtils.info("[服务][主控] ==> 副灯箱"+(enabled ? "亮" : "灭"));
        return weakCurrentManage(channelValues);
    }

    @Override
    public Result reboot(int delayMillis) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 硬件重启 -->延时"+delayMillis);
        Result result;
        try {
            driverWrapper.getMainMachineCtrl().restartPower(delayMillis);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 硬件重启 -->成功" );
        } else {
            log4jUtils.error("[服务][主控] ==> 硬件重启 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 硬件重启耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setCoolingScope(int min, int max) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 设置温度范围 --" + min + "-" + max);
        Result result;
        try {
            driverWrapper.getMainMachineCtrl().setTemperatureRange(min, max);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }

        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 设置温度范围 -->成功"  );
        } else {
            log4jUtils.error("[服务][主控] ==> 设置温度范围 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 设置温度范围耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result getCoolingScope() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 获取温度范围");
        Result result;
        try {
            String range = driverWrapper.getMainMachineCtrl().getTemperatureRange();
            String[] values = range.split(":");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("min", values[0]);
            jsonObject.put("max", values[1]);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),jsonObject.toJSONString());
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode()== ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 获取温度范围 -->成功 --" + JsonUtils.toJSONString(result));
        } else {
            log4jUtils.error("[服务][主控] ==> 获取温度范围 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 获取温度范围耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setWarmingScope(int min, int max) throws RemoteException {
        return null;
    }

    @Override
    public Result getWarmingScope() throws RemoteException {
        return null;
    }

    @Override
    public Result getAmmeterReading() throws RemoteException {

        if (driverWrapper.getAmmeterCtrl()==null)
            return new Result(ErrorCode.ERR_RESOURCESNOTINITIALIZED, ErrorTitle.getErrorTitle(ErrorCode.ERR_RESOURCESNOTINITIALIZED), "");
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][电表] ==>获取电表度数");
        Result result;
        try {
            float reading = (float) (Integer.valueOf(driverWrapper.getAmmeterCtrl().readTheMeter())*0.01);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Reading", reading);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), jsonObject.toJSONString());
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_OUTOFMEMORY, e.getMessage(), "");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][电表] ==>获取电表度数 -->成功 --"+ JsonUtils.toJSONString(result));
        } else {
            log4jUtils.error("[服务][电表] ==>获取电表度数 -->失败 --" +  JsonUtils.toJSONString(result));
        }

        return result;
    }

    @Override
    public Result getHostTemperature() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 获取温度");
        Result result;
        try {
            String temp = driverWrapper.getMainMachineCtrl().getCurTemperature();
            String[] values = temp.split(":");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SI7005", values[0]);
            jsonObject.put("DS18B20", values[1]);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),jsonObject.toJSONString());
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 获取温度 -->成功 --" + JsonUtils.toJSONString(result));
        } else {
            log4jUtils.error("[服务][主控] ==> 获取温度 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 获取温度耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result enableKeepAlive() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 启用心跳");
        Result result;
        try {
            driverWrapper.getMainMachineCtrl().enableKeepAlive();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 启用心跳 -->成功");
        } else {
            log4jUtils.error("[服务][主控] ==> 启用心跳 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 启用心跳耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result disableKeepAlive() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 禁用心跳");
        Result result;
        try {
             driverWrapper.getMainMachineCtrl().disenableKeepAlive();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 禁用心跳 -->成功");
        } else {
            log4jUtils.error("[服务][主控] ==> 禁用心跳 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 禁用心跳耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result keepAlive() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][主控] ==> 发送心跳");
        Result result;
        try {
           driverWrapper.getMainMachineCtrl().keepAlive();
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        } catch (DcdzSystemException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e){
            result = new Result(SerialPortErrorCode.ERR_SP_SERIALPORT, e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][主控] ==> 发送心跳 -->成功");
        } else {
            log4jUtils.error("[服务][主控] ==> 发送心跳 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][主控] ==> 发送心跳耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
}
