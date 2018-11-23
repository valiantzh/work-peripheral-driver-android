package com.hzdongcheng.drivers;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.modbus.helper.ModbusHelper;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl.Dm3ccCmdCharHelper;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl.Dm3ccRotateControlImpl;
import com.hzdongcheng.drivers.peripheral.plc.initialize.impl.PLCControlImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 2018/3/21.
 */
@RunWith(AndroidJUnit4.class)
public class DM3CCServoTest {
    private static final String TAG = "DM3CCServoTest";

    PLCControlImpl plcControlImpl = new PLCControlImpl();
    Dm3ccRotateControlImpl rotateControl;
    ModbusHelper modbusHelper = ModbusHelper.getInstance();


    byte slaveID = 1;//副机编号 1~
    int correctingType = 2;//0-无，1-基准点标定 2-基准脉冲数标定 3-格口位置标定
    /**
     * @Method Name: setUp
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @Before
    public void setUp() throws Exception {
        //modbusBase.open("COM5");
        //plcControlImpl.connect("/dev/ttyimx3");//
        //plcControlImpl.connect("/dev/ttymxc5");
        plcControlImpl.connect("/dev/ttymxc6");
        rotateControl = new Dm3ccRotateControlImpl(plcControlImpl.getModbusBase());
        Dm3ccCmdCharHelper.setServoSpeed(slaveID, 2000);
        //modbusHelper.addMonitorSlave(slaveID, Dm3ccCmdCharHelper.START_ADDRESS_READ, Dm3ccCmdCharHelper.CMD_CHAR_NUM);
        //modbusHelper.startMonitorTask();
    }

    /**
     * @Method Name: tearDown
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @After
    public void tearDown() throws Exception {
        modbusHelper.stopMonitorTask();
        plcControlImpl.disconnect();
    }

    @Test
    public void queryStatusTest()throws InterruptedException, DriversException ,ModbusException{
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID, modbusHelper.read(slaveID, Dm3ccCmdCharHelper.START_ADDRESS_READ, Dm3ccCmdCharHelper.CMD_CHAR_NUM));
        byte originStatus = cmdCharHelper.getOriginStatus();
        byte enableStatus = cmdCharHelper.getEnableStatus();
        byte errorReset = cmdCharHelper.getErrorReset();
        Log.i(TAG, "originStatus="+originStatus+",enableStatus="+enableStatus+",errorReset="+errorReset);
        Log.i(TAG, "queryStatusTest="+cmdCharHelper.toString());
        Thread.sleep(20000);
    }
    @Test
    public void toggleServoEnableTest() throws InterruptedException, DriversException ,ModbusException{
        boolean on = true;
        int rc = rotateControl.toggleServoEnable(slaveID, on);
        Log.i(TAG, "toggleServoEnableTest rc="+rc+",on="+on);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void returnServoOriginTest() throws InterruptedException, DriversException ,ModbusException {
        int rc = rotateControl.returnServoOrigin(slaveID);
        Log.i(TAG, "returnServoOriginTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }

    @Test
    public void resetServo4ErrorTest()throws InterruptedException, DriversException ,ModbusException{
        int rc = rotateControl.resetServo4Error(slaveID);
        Log.i(TAG, "resetServo4ErrorTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void resetServo4StopTest()throws InterruptedException, DriversException ,ModbusException{
        int rc = rotateControl.resetServo4Stop(slaveID);
        Log.i(TAG, "resetServo4StopTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void urgentStopServoTest()throws InterruptedException, DriversException ,ModbusException{
        int rc = rotateControl.urgentStopServo(slaveID);
        Log.i(TAG, "urgentStopServoTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }

    @Test
    public void runServoByJogTest()throws InterruptedException, DriversException ,ModbusException {
        int speed = 2000;
        int aspect = 1;//0-正向  1-反向
        //点动
        int rc = rotateControl.runServoByJog(slaveID,aspect, speed,correctingType);
        Log.i(TAG, "runServoByJogTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void stopServoByJogTest()throws InterruptedException, DriversException ,ModbusException {
        //点动-停止
        int rc = rotateControl.stopServoByJog(slaveID,correctingType);
        Log.i(TAG, "stopServoByJogTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }

    @Test
    public void runServoByCoilTest()throws InterruptedException, DriversException ,ModbusException {
        int speed = 2000;
        int aspect = 0;//0-正向  1-反向
        int iCoil = 1;
        correctingType = 2;
        //伺服跑圈
        int rc = rotateControl.runServoByCoil(slaveID,iCoil, aspect, speed);
        Log.i(TAG, "runServoByCoilTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void runServoByNextTest()throws InterruptedException, DriversException ,ModbusException {
        int speed = 300;
        int type = 0;//0-大格口  1-小格口
        //下一位置
        int rc = rotateControl.runServoByNext(slaveID,type,speed);
        Log.i(TAG, "runServoByNextTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void runServoByNextLast()throws InterruptedException, DriversException ,ModbusException {
        int speed = 300;
        int type = 1;//0-大格口  1-小格口

        //上一位置
        int rc = rotateControl.runServoByLast(slaveID,type,speed);
        Log.i(TAG, "runServoByNextLast rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void setPulse4BasetTast()throws InterruptedException, DriversException ,ModbusException {
        int iPulse = 30000;

        //上一位置
        int rc = rotateControl.setPulse4Base(slaveID, iPulse);
        Log.i(TAG, "setPulse4BasetTast rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void setPulse4CoilTest()throws InterruptedException, DriversException ,ModbusException {
        int iPulse = 0;

        int rc = rotateControl.setPulse4Coil(slaveID,iPulse);
        Log.i(TAG, "setPulse4CoilTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void setPulse4PositionTest()throws InterruptedException, DriversException ,ModbusException {
        int iPulse = 300;

        //上一位置
        int rc = rotateControl.setPulse4Position(slaveID,iPulse);
        Log.i(TAG, "setPulse4PositionTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }

}
