package com.dcdz.drivers.demo.business;

import android.os.RemoteException;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;

import java.util.Map;

/**
 * 主控以及副机功能代理
 * Created by Yihang on 2018/8/22.
 */


public class Proxy4Elocker {
    private Log4jUtils log = Log4jUtils.createInstanse(Proxy4Elocker.class);
    private ServiceHelper serviceHelper = ServiceHelper.getInstance();

    private static class SingletonHolder{
        private final static Proxy4Elocker instance = new Proxy4Elocker();
    }
    public static Proxy4Elocker getInstance(){
        return SingletonHolder.instance;
    }
    private Proxy4Elocker(){
    }

    //#region 主控业务代理
    public String queryMainStatus() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.queryMainStatus();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryMainStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String weakCurrentManage(Map channelValues) throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.weakCurrentManage(channelValues);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[weakCurrentManage]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String strongCurrentManage(Map channelValues) throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.strongCurrentManage(channelValues);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[strongCurrentManage]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String toggleMasterLamp(boolean enabled) throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.toggleMasterLamp(enabled);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[toggleMasterLamp]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String toggleSlaveLamp(boolean enabled) throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.toggleSlaveLamp(enabled);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[toggleSlaveLamp]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String reboot(int delayMillis) throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.reboot(delayMillis);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[reboot]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String setCoolingScope(int min, int max) throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.setCoolingScope(min,max);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[setCoolingScope]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String getCoolingScope() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.getCoolingScope();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[getCoolingScope]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String getAmmeterReading() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.getAmmeterReading();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[getAmmeterReading]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String getHostTemperature() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.getHostTemperature();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[getHostTemperature]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String enableKeepAlive() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.enableKeepAlive();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[enableKeepAlive]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String disableKeepAlive() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.disableKeepAlive();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[disableKeepAlive]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    public String keepAlive() throws DcdzSystemException {
        if (serviceHelper.masterController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.masterController.keepAlive();
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[keepAlive]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion

    //#region 副机业务代理
    public String openAllBox(byte boardId) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.openAllBox(boardId);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[openAllBox]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String openBoxById(byte boardId, byte boxId) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.openBoxById(boardId,boxId);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[openBoxById]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String openBoxByName(String boxName) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.openBoxByName(boxName);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[openBoxByName]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryStatusById(byte boardId) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.queryStatusById(boardId);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryStatusById]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryStatusByName(String boardName) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.queryStatusByName(boardName);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryStatusByName]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryBoxStatusById(byte boardId, byte boxId) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.queryBoxStatusById(boardId,boxId);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryBoxStatusById]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryBoxStatusByName(String boxName) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.queryBoxStatusByName(boxName);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryBoxStatusByName]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryFreezerScope(byte freezerId) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.queryFreezerScope(freezerId);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryFreezerScope]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String queryFreezerPower(byte freezerId)  throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.queryFreezerPower(freezerId);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryFreezerPower]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String setFreezerBoot(byte freezerId, int lower) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.setFreezerBoot(freezerId,lower);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[setFreezerBoot]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    public String setFreezerReboot(byte freezerId, int upper) throws DcdzSystemException {
        if (serviceHelper.slaveController ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.slaveController.setFreezerReboot(freezerId,upper);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[setFreezerReboot]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion
}
