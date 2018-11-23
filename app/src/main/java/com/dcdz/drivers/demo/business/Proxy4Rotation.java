package com.dcdz.drivers.demo.business;


import android.os.RemoteException;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;

/**
 * 旋转业务控制代理
 * Created by Administrator on 2018/4/23.
 */

public class Proxy4Rotation {
    private static Log4jUtils log = Log4jUtils.createInstanse(Proxy4Rotation.class);
    private static ServiceHelper serviceHelper = ServiceHelper.getInstance();

    //#region 旋转业务代理
    /**
     * 初始化PLC
     * @param slaveID 副机编号1~
     * @return
     */
    public static String initializePlc(int slaveID) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.initialize((byte)slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[initializePlc]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 复位PLC
     * @param slaveID 副机编号1~
     * @return
     */
    public static String resetPlc(int slaveID) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.reset((byte)slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[resetPlc]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 移动货格到存取口
     * @param slaveID 副机编号1~
     * @param boxID 副机内格口编号
     */
    public static String moveShelfBox(int slaveID, int boxID) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.moveShelfBox((byte)slaveID, boxID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[move2Access]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 打开货格进行存取
     * @param slaveID 副机编号1~
     * @param boxID 副机内格口编号
     */
    public static String openBox4Access(int slaveID, int boxID) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.openBox4Access((byte)slaveID, boxID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[move2Access]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 打开指定存取门
     * @param slaveID 副机编号1~
     * @param doorNo  门编号 0开始
     * @return
     */
    public static String openDoor(int slaveID, int doorNo) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            //Result res = serviceHelper.rotationCtrl.openDoor((byte)slaveID, (byte)doorNo);
            Result res = serviceHelper.rotationCtrl.openDoorForce((byte)slaveID, (byte)doorNo);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[openAccessDoor]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 关闭指定存取门
     * @param slaveID 副机编号1~
     * @param doorNo 0开始
     * @return
     */
    public static String closeDoor(int slaveID, int doorNo) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.closeDoor((byte)slaveID, (byte)doorNo);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[closeAccessDoor]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 打开指定维修门口
     * @param slaveID 副机编号1~
     * @return {
     */
    public static String openRepairDoor(int slaveID)  throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.openRepairDoor((byte)slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[openRepairDoor]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 打开所有门
     * @param slaveID 副机编号1~
     * @return {"errorCode": 0, "errorMessage": "success", "data": {}}
     */
    public static String openAllDoor(int slaveID) throws DcdzSystemException {
        if (serviceHelper.rotationCtrl ==null ){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.openAllDoor((byte)slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode(), res.getErrorMsg());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[openAllDoor]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 发送盘库任务
     * @param slaveID 副机编号1~
     * @param checkToken 盘库令牌
     * @return
     */
    public static String sendCheckTask(int slaveID, int checkToken) throws DcdzSystemException {
        if(serviceHelper.rotationCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        String result =null;
        try {
            Result res = serviceHelper.rotationCtrl.sendCheckTask((byte)slaveID, checkToken);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            result = res.getData();
        } catch (RemoteException e){
            log.error("[sendCheckTask]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
        return result;
    }

    /**
     * 查询盘库结果
     * @param slaveID 副机编号1~
     * @param checkToken
     * @return
     */
    public static String queryCheckResult(int slaveID, int checkToken) throws DcdzSystemException {
        if(serviceHelper.rotationCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.queryCheckResult((byte)slaveID, checkToken);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryCheckResult]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 查询货格状态
     * @param slaveID 副机编号1~
     * @param boxID 副机内格口编号 1~
     * @return
     */
    public static String queryBoxStatus(int slaveID, int boxID) throws DcdzSystemException {
        if(serviceHelper.rotationCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }

        try {
            Result res =serviceHelper.rotationCtrl.queryBoxStatus((byte)slaveID, boxID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[queryBoxStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 查询副状态
     * @param slaveID 副机编号1~
     */
    public static String querySlaveStatus(int slaveID) throws DcdzSystemException {
        if(serviceHelper.rotationCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.rotationCtrl.querySlaveStatus((byte)slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[querySlaveStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion

    //#region ==
    //#endregion
}
