package com.hzdongcheng.drivers.service.aidl.impl.rotation;

import android.os.RemoteException;

import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.IRotateControl;
import com.hzdongcheng.drivers.peripheral.plc.model.CheckResult;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxStatus;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;
import com.hzdongcheng.drivers.rotation.IRotationController;
import com.hzdongcheng.drivers.service.aidl.DriversManager;

/**
 * Created by Administrator on 2018/4/23.
 */

public
class RotationController extends IRotationController.Stub {
    DriversManager driversManager = DriversManager.getInstance();
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private IRotateControl rotateControl;
    /**
     * Construct the stub at attach it to the interface.
     */
    public RotationController() {
        super();
        rotateControl = driversManager.getSaleRotation();
    }

    @Override
    public Result initialize(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][RotationCtrl] ==>initialize");
        try {
            rotateControl.initialize(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>initialize -->Success");
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>initialize -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][RotationCtrl] ==>initialize耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
    
    @Override
    public Result reset(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>reset");
        try {
            rotateControl.resetServo(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>reset -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>reset -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>reset Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>reset耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result moveShelfBox(byte slaveID, int boxID)throws RemoteException {
        byte ofLayer = (byte)(boxID/100);
        byte ofCol = (byte)(boxID%100);
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>moveShelfBox");
        try {
            ShelfBoxStatus boxStatus = rotateControl.move2Access(slaveID,ofLayer, ofCol );
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(boxStatus));

        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>moveShelfBox -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>moveShelfBox -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>moveShelfBox Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>moveShelfBox耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result openBox4Access(byte slaveID, int boxID) throws RemoteException {
        byte ofLayer = (byte)(boxID/100);
        byte ofCol = (byte)(boxID%100);
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>move2Access");
        try {
            //rotateControl.move2Access();
            ShelfBoxStatus boxStatus = rotateControl.move2AccessAndOpen(slaveID,ofLayer, ofCol );
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(boxStatus));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>move2Access -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>move2Access -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>move2Access Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>move2Access耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result openDoor(byte slaveID, byte doorNo) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>openDoor");
        try {
            int res = rotateControl.openAccessDoor(slaveID,doorNo, false );
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), ""+res);
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>openDoor -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>openDoor -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>openDoor Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>openDoor耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result openDoorForce(byte slaveID, byte doorNo) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>openDoorForce");
        try {
            int res = rotateControl.openAccessDoor(slaveID,doorNo, true );
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), ""+res);
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>openDoorForce -->Success耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>openDoorForce -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>openDoorForce Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>openDoorForce耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
    @Override
    public Result openRepairDoor(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>openRepairDoor");
        try {
            rotateControl.openRepairDoor(slaveID, (byte)0);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>openRepairDoor -->Success耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>openRepairDoor -->Failed" + " --" + jsonString);
        }
        //log4jUtils.info("[Service][RotationCtrl] ==>openRepairDoor耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result closeDoor(byte slaveID, byte doorNo) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][RotationCtrl] ==>closeDoor");
        try {
            rotateControl.closeAccessDoor(slaveID,doorNo);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>closeDoor -->Success");
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>closeDoor -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][RotationCtrl] ==>closeDoor耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result openAllDoor(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][RotationCtrl] ==>openAllDoor");
        try {
            rotateControl.openAllDoor(slaveID );
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>openAllDoor -->Success");
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>openAllDoor -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][RotationCtrl] ==>openAllDoor耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result sendCheckTask(byte slaveID, int checkToken) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>sendCheckTask");
        try {
            rotateControl.sendCheckTask(slaveID ,checkToken);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>sendCheckTask -->Success 耗时 -->" + (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>sendCheckTask -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>sendCheckTask Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>sendCheckTask耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryCheckResult(byte slaveID, int checkToken) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>queryCheckResult");
        try {
            CheckResult checkResult = rotateControl.queryCheckResult(slaveID ,checkToken);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(checkResult));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>queryCheckResult -->Success 耗时 -->"+ (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>queryCheckResult -->Failed" + " --" + jsonString);
        }
        log4jUtils.debug("[Service][RotationCtrl] ==>queryCheckResult Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>queryCheckResult耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result queryBoxStatus(byte slaveID, int boxID) throws RemoteException {
        byte ofLayer = (byte)(boxID/100);
        byte ofCol = (byte)(boxID%100);
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>queryBoxStatus");
        try {
            ShelfBoxStatus status = rotateControl.queryShelfBoxStatus(slaveID ,ofLayer,ofCol );
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(status));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][RotationCtrl] ==>queryShelfBoxStatus -->Success 耗时 -->"+ (System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>queryShelfBoxStatus -->Failed" + " --" + jsonString);
        }
        //log4jUtils.debug("[Service][RotationCtrl] ==>queryShelfBoxStatus Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>queryShelfBoxStatus耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result querySlaveStatus(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        //log4jUtils.info("[Service][RotationCtrl] ==>querySlaveStatus");
        try {
            SlaveDeskStatus status = rotateControl.querySlaveDeskStatus(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(status));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
//            log4jUtils.info("[Service][RotationCtrl] ==>querySlaveStatus -->Success耗时 -->"+(System.currentTimeMillis() - start));
        } else {
            log4jUtils.error("[Service][RotationCtrl] ==>querySlaveStatus -->Failed" + " --" + jsonString);
        }
        //log4jUtils.debug("[Service][RotationCtrl] ==>querySlaveStatus Result="+ jsonString);
        //log4jUtils.info("[Service][RotationCtrl] ==>queryAllShelfBoxStatus耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
}
