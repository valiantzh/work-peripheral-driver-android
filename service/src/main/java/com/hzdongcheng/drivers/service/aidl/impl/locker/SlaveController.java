package com.hzdongcheng.drivers.service.aidl.impl.locker;

import android.os.RemoteException;


import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.components.toolkits.utils.StringUtils;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.locker.ISlaveController;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus;
import com.hzdongcheng.drivers.service.aidl.DriversManager;

public class SlaveController extends ISlaveController.Stub {

    private Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    DriversManager driverWrapper= DriversManager.getInstance();
    ConfigManager deviceConfigModel = ConfigManager.getInstance();
    @Override
    public Result openAllBox(byte boardId) throws RemoteException {
        Result result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT),"");
        return result;
    }

    @Override
    public Result openBoxById(byte boardId, byte boxId) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][锁控] ==> 开箱 -->驱动板："+boardId+"箱号："+boxId);
        Result result;
        try {
            BoxStatus boxStatus =driverWrapper.getSlaveMachineCtrl(boardId).openBox(boardId, boxId);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(boxStatus));
        }catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }
        if (result.getCode()== ErrorCode.SUCCESS){
            log4jUtils.info("[服务][锁控] ==> 开箱 -->成功");
        } else {
            log4jUtils.error("[服务][锁控] ==> 开箱 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][锁控] ==> 开箱耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result openBoxByName(String boxName) throws RemoteException {
        Result result;
        if (StringUtils.isEmpty(boxName)){
            result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT), "");
            log4jUtils.error("[服务][锁控] ==> 开箱 -->失败 --箱号为空");
            return result;
        }

        byte boardNo = 0;
        byte boxID = 0;
        if (deviceConfigModel.BoxDeskMap != null){
            boardNo= deviceConfigModel.BoxDeskMap.get(boxName);
        }
        if (deviceConfigModel.BoxContrastMap !=null){
            boxID= deviceConfigModel.BoxContrastMap.get(boxName);
        }
        return openBoxById(boardNo, boxID);
    }

    @Override
    public Result queryStatusById(byte boardId) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][锁控] ==> 获取副柜所有状态 -->驱动板："+boardId);
        Result result;
        try {
            ViceMachineStatus viceMachineStatus = driverWrapper.getSlaveMachineCtrl(boardId).queryStatus(boardId);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(viceMachineStatus));
        }catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][锁控] ==> 获取副柜所有状态 -->成功");
        } else {
            log4jUtils.error("[服务][锁控] ==> 获取副柜所有状态 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][锁控] ==> 获取副柜所有状态耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryStatusByName(String boardName) throws RemoteException {
        Result result;
        if (StringUtils.isEmpty(boardName)){
            result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT), "");
            log4jUtils.error("[服务][锁控] ==> 获取副柜所有状态 -->失败 --驱动板号为空");
            return result;
        }

        byte boardID = 0;
        if (deviceConfigModel.DeskContrastMap != null){
            boardID= deviceConfigModel.DeskContrastMap.get(boardName);
        }
        return queryStatusById(boardID);
    }

    @Override
    public Result queryBoxStatusById(byte boardId, byte boxId) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][锁控] ==> 获取箱门状态 -->驱动板："+boardId+"箱号："+boxId);
        Result result;
        try {
            BoxStatus boxStatus = driverWrapper.getSlaveMachineCtrl(boardId).queryStatus(boardId, boxId);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(boxStatus));
        }catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }

        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][锁控] ==> 获取箱门状态 -->成功");
        } else {
            log4jUtils.error("[服务][锁控] ==> 获取箱门状态 -->失败 --"+ JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][锁控] ==> 获取箱门状态耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryBoxStatusByName(String boxName) throws RemoteException {
        Result result;

        if (StringUtils.isEmpty(boxName)){
            result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT), "");
            log4jUtils.error("[服务][锁控] ==> 获取箱门状态耗时 --箱号为空");
            return result;
        }

        byte boardNo = 0;
        byte boxID = 0;
        if (deviceConfigModel.BoxDeskMap != null){
            boardNo= deviceConfigModel.BoxDeskMap.get(boxName);
        }
        if (deviceConfigModel.BoxContrastMap !=null){
            boxID= deviceConfigModel.BoxContrastMap.get(boxName);
        }
        return queryBoxStatusById(boardNo, boxID);
    }

    @Override
    public Result queryFreezerScope(byte freezerId) throws RemoteException {
        if (driverWrapper.getThermostatCtrl()==null) {
            log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->温控器未打开");
            return new Result(ErrorCode.ERR_RESOURCESNOTINITIALIZED, ErrorTitle.getErrorTitle(ErrorCode.ERR_RESOURCESNOTINITIALIZED), "");
        }
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][温控器] ==>获取温控器温度 -->地址："+freezerId);
        Result result;
        try {
            ThermostatStatus.TempControlStatus tempControlStatus=driverWrapper.getThermostatCtrl().getThermostatStatus(freezerId);
            String allTemp="{'openTemp':'"+tempControlStatus.getCodeOpenTemp()+"','closeTemp':'"+tempControlStatus.getCodeCloseTemp()
                    +"','currentTemp':'"+tempControlStatus.getCuvinTemp()+"'}";
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), allTemp);
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }

        if (result.getCode()== ErrorCode.SUCCESS){
            log4jUtils.info("[服务][温控器] ==>获取温控器温度 -->成功 --"+ JsonUtils.toJSONString(result));
        } else {
            log4jUtils.info("[服务][温控器] ==>获取温控器温度 -->失败 --" + JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][温控器] ==>获取温控器温度耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryFreezerPower(byte freezerId) throws RemoteException {
        if (driverWrapper.getThermostatCtrl()==null) {
            log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->温控器未打开");
            return new Result(ErrorCode.ERR_RESOURCESNOTINITIALIZED, ErrorTitle.getErrorTitle(ErrorCode.ERR_RESOURCESNOTINITIALIZED), "");
        }
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->地址："+freezerId);
        Result result;
        try {
            ThermostatStatus.TempControlStatus tempControlStatus=driverWrapper.getThermostatCtrl().getThermostatStatus(freezerId);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(tempControlStatus));
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }

        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->成功 --"+ JsonUtils.toJSONString(result));
        } else {
            log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->失败 --" + JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][温控器] ==>获取温控器状态耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setFreezerBoot(byte freezerId, int lower) throws RemoteException {
        if (driverWrapper.getThermostatCtrl()==null) {
            log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->温控器未打开");
            return new Result(ErrorCode.ERR_RESOURCESNOTINITIALIZED, ErrorTitle.getErrorTitle(ErrorCode.ERR_RESOURCESNOTINITIALIZED), "");
        }
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][温控器] ==>设置开机温度 -->地址："+freezerId+"温度："+lower);
        Result result;
        try {
            driverWrapper.getThermostatCtrl().setOpenTemperature(freezerId,lower);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }
        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][温控器] ==>设置开机温度 -->成功");
        } else {
            log4jUtils.info("[服务][温控器] ==>设置开机温度 -->失败 --" + JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][温控器] ==>设置开机温度耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setFreezerReboot(byte freezerId, int upper) throws RemoteException {
        if (driverWrapper.getThermostatCtrl()==null) {
            log4jUtils.info("[服务][温控器] ==>获取温控器状态 -->温控器未打开");
            return new Result(ErrorCode.ERR_RESOURCESNOTINITIALIZED, ErrorTitle.getErrorTitle(ErrorCode.ERR_RESOURCESNOTINITIALIZED), "");
        }
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][温控器] ==>设置关机温度 -->地址："+freezerId+"温度："+upper);
        Result result;
        try {
            driverWrapper.getThermostatCtrl().setCloseTemperature(freezerId,upper);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (SerialPortException e) {
            result = new Result(e.getErrorCode(),e.getMessage(),"");
        } catch (Exception e) {
            result = new Result(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT,e.getMessage(),"");
        }

        if (result.getCode() == ErrorCode.SUCCESS){
            log4jUtils.info("[服务][温控器] ==>设置关机温度 -->成功");
        } else {
            log4jUtils.info("[服务][温控器] ==>设置关机温度 -->失败 --" + JsonUtils.toJSONString(result));
        }
        log4jUtils.info("[服务][温控器] ==>设置关机温度耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result toggleBoxHeatingById(byte boardId, byte boxId, boolean active) throws RemoteException {
        Result result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT),"");
        return result;
    }

    @Override
    public Result toggleBoxHeatingByName(String boxName, boolean active) throws RemoteException {
        Result result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT),"");
        return result;
    }
}
