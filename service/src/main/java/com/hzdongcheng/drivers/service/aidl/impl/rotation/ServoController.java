package com.hzdongcheng.drivers.service.aidl.impl.rotation;

import android.os.RemoteException;

import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.rotation.IServoController;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.servo.IServoControl;
import com.hzdongcheng.drivers.peripheral.servo.model.ServoStatus;
import com.hzdongcheng.drivers.service.aidl.DriversManager;

/**
 * Created by Administrator on 2018/4/23.
 */

public class ServoController extends IServoController.Stub {
    DriversManager driversManager = DriversManager.getInstance();
    private final Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private IServoControl servoControl;

    public ServoController(){
        this.servoControl = driversManager.getSaleRotation();
    }

    @Override
    public Result toggleServoEnable(byte slaveID, boolean no) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>toggleServoEnable");
        try{
            servoControl.toggleServoEnable(slaveID,no);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>toggleServoEnable -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>toggleServoEnable -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>toggleServoEnable耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result returnServoOrigin(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>returnServoOrigin");
        try{
            servoControl.returnServoOrigin(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>returnServoOrigin -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>returnServoOrigin -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>returnServoOrigin耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result runServoByJog(byte slaveID, int aspect, int speed, int type) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>runServoByJog");
        try{
            servoControl.runServoByJog(slaveID,aspect,speed,type);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>runServoByJog -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>runServoByJog -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>runServoByJog耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result stopServoByJog(byte slaveID, int type) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>stopServoByJog");
        try{
            servoControl.stopServoByJog(slaveID,type);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>stopServoByJog -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>stopServoByJog -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>stopServoByJog耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result runServoByCoil(byte slaveID, int iCoil, int aspect, int speed) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>runServoByCoil");
        try{
            servoControl.runServoByCoil(slaveID,iCoil,aspect,speed);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>runServoByCoil -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>runServoByCoil -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>runServoByCoil耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result runServoByNext(byte slaveID, int type, int speed) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>runServoByNext");
        try{
            servoControl.runServoByNext(slaveID,type,speed);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>runServoByNext -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>runServoByNext -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>runServoByNext耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result runServoByLast(byte slaveID, int type, int speed) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>runServoByLast");
        try{
            servoControl.runServoByLast(slaveID,type,speed);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>runServoByLast -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>runServoByLast -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>runServoByLast耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result urgentStopServo(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>urgentStopServo");
        try{
            servoControl.urgentStopServo(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>urgentStopServo -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>urgentStopServo -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>urgentStopServo耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result resetServo4Stop(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>resetServo4Stop");
        try{
            servoControl.resetServo4Stop(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>resetServo4Stop -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>resetServo4Stop -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>resetServo4Stop耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result resetServo4Error(byte slaveID) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>resetServo4Error");
        try{
            servoControl.resetServo4Error(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>resetServo4Error -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>resetServo4Error -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>resetServo4Error耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setPulse4Base(byte slaveID, int iPulse) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>setPulse4Base");
        try{
            servoControl.setPulse4Base(slaveID,iPulse);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>setPulse4Base -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>setPulse4Base -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>setPulse4Base耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setPulse4Coil(byte slaveID, int iPulse) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>setPulse4Coil");
        try{
            servoControl.setPulse4Coil(slaveID,iPulse);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>setPulse4Coil -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>setPulse4Coil -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>setPulse4Coil耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result setPulse4Position(byte slaveID, int iPulse) throws RemoteException {
        Result result;
        long start = System.currentTimeMillis();
        log4jUtils.info("[Service][ServoController]==>setPulse4Position");
        try{
            servoControl.setPulse4Position(slaveID,iPulse);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), "");
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
            log4jUtils.info("[Service][ServoController] ==>setPulse4Position -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>setPulse4Position -->Failed" + " --" + jsonString);
        }
        log4jUtils.info("[Service][ServoController] ==>setPulse4Position耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result getServoStatus(byte slaveID) throws RemoteException {
        Result result;
//        long start = System.currentTimeMillis();
//        log4jUtils.info("[Service][ServoController]==>getServoStatus");
        try{
            ServoStatus servoStatus = servoControl.getServoStatus(slaveID);
            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS), JsonUtils.toJSONString(servoStatus));
        } catch (DriversException e) {
            result = new Result(e.getErrorCode(), e.getMessage(), "");
        }

        String jsonString = JsonUtils.toJSONString(result);
        if (result.getCode() == ErrorCode.SUCCESS) {
//            log4jUtils.info("[Service][ServoController] ==>getServoStatus -->Success");
        } else {
            log4jUtils.error("[Service][ServoController] ==>getServoStatus -->Failed" + " --" + jsonString);
        }
//        log4jUtils.info("[Service][ServoController] ==>getServoStatus耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
}
