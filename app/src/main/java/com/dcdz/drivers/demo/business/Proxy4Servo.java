package com.dcdz.drivers.demo.business;


import android.os.RemoteException;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;

/**
 * 伺服控制接口代理
 * Created by Administrator on 2018/4/23.
 */

public class Proxy4Servo {
    private static Log4jUtils log = Log4jUtils.createInstanse(Proxy4Servo.class);
    private static ServiceHelper serviceHelper = ServiceHelper.getInstance();

    
    //#region 伺服电机控制代理
    /**
     *
     * @Title: servoEnable
     * @Description: 伺服电机使能开关
     * @param slaveID 副机编号
     * @param @param no
     * @return  
     */
    public static String toggleServoEnable(byte slaveID,boolean no) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.toggleServoEnable(slaveID,no);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[toggleServoEnable]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     *
     * @Title: returnServoOrigin
     * @Description: 伺服回原点
     * @param slaveID 副机编号
     * @return  
     */
    public static String returnServoOrigin(byte slaveID) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.returnServoOrigin(slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[returnServoOrigin]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     *
     * @Title: runServoByJog
     * @Description: 伺服点动开始
     * @param slaveID 副机编号
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 运动速度  （低速-转/分钟）
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return  
     */
    public static String runServoByJog(byte slaveID, int aspect, int speed, int type) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.runServoByJog(slaveID,aspect,speed,type);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[runServoByJog]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    /**
     *
     * @Title: stopServoRun
     * @Description: 伺服点动结束
     * @param slaveID 副机编号
     * @param type  0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
     * @return  
     */
    public static String stopServoByJog(byte slaveID, int type) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.stopServoByJog(slaveID,type);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[stopServoByJog]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    
    /**
     * 伺服跑圈
     * @param slaveID
     * @param iCoil 圈数
     * @param aspect 运动方向 0-正向  1-反向
     * @param speed 伺服速度（高速-转/分钟）
     * @return  
     */
    public static String runServoByCoil(byte slaveID,int iCoil, int aspect, int speed) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.runServoByCoil(slaveID,iCoil,aspect,speed);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[runServoByCoil]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     *
     * @Title: runServoByNext
     * @Description: 伺服运动到下一个位置
     * @param slaveID 副机编号
     * @param type type  类型 0-大格口  1-小格口
     * @param speed 运动速度  （转/分钟）
     * @return  
     */
    public static String runServoByNext(byte slaveID, int type, int speed) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.runServoByNext(slaveID,type,speed);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[runServoByNext]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     *
     * @Title: runServoByLast
     * @Description: 伺服运动到上一个位置
     * @param slaveID 副机编号
     * @param type  类型 0-大格口  1-小格口
     * @param speed 运动速度  （转/分钟）
     * @return  
     */
    public static String runServoByLast(byte slaveID, int type, int speed) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.runServoByLast(slaveID,type,speed);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[runServoByLast]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    /**
     * 紧急停车
     * @param slaveID
     * @return  
     */
    public static String urgentStopServo(byte slaveID) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.urgentStopServo(slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[urgentStopServo]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    /**
     * 急停复位
     * @param slaveID
     * @return  
     */
    public static String resetServo4Stop(byte slaveID) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.resetServo4Stop(slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[resetServo4Stop]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 报警复位
     * @param slaveID
     * @return  
     */
    public static String resetServo4Error(byte slaveID) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.resetServo4Error(slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[resetServo4Error]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 设置基准脉冲-基准点
     * @param slaveID
     * @param iPulse
     * @return  
     */
    public static String setPulse4Base(byte slaveID, int iPulse) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.setPulse4Base(slaveID,iPulse);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[setPulse4Base]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 设置基准脉冲-1圈
     * @param slaveID
     * @param iPulse
     * @return  
     */
    public static String setPulse4Coil(byte slaveID, int iPulse) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.setPulse4Coil(slaveID,iPulse);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[setPulse4Coil]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }

    /**
     * 设置基准脉冲-当前位置
     * @param slaveID
     * @param iPulse
     * @return  
     */
    public static String setPulse4Position(byte slaveID, int iPulse) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.setPulse4Position(slaveID,iPulse);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[setPulse4Position]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    
    /**
     *
     * @Title: getServoStatus
     * @Description: 获取伺服状态
     * @param slaveID 副机编号
     * @return  
     */
    public static String getServoStatus(byte slaveID) throws DcdzSystemException {
        if(serviceHelper.servoCtrl == null){
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_UNBINDSERVICE);
        }
        try {
            Result res = serviceHelper.servoCtrl.getServoStatus(slaveID);
            if(res.getCode() != 0){
                throw new DcdzSystemException(res.getCode());
            }
            return res.getData();
        } catch (RemoteException e){
            log.error("[getServoStatus]", e);
            throw new DcdzSystemException(DriversErrorCode.ERR_PD_REMOTESERVICEERROR, e.getMessage());
        }
    }
    //#endregion
}
