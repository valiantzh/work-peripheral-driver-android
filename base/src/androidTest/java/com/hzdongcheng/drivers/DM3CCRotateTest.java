package com.hzdongcheng.drivers;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.modbus.helper.ModbusHelper;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl.Dm3ccCmdCharHelper;
import com.hzdongcheng.drivers.peripheral.plc.control.rotate.impl.Dm3ccRotateControlImpl;
import com.hzdongcheng.drivers.peripheral.plc.initialize.impl.PLCControlImpl;
import com.hzdongcheng.drivers.peripheral.plc.model.CheckResult;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 2018/3/21.
 */
@RunWith(AndroidJUnit4.class)
public class DM3CCRotateTest {
    private static final String TAG = "DM3CCRotateTest";

    PLCControlImpl plcControlImpl = new PLCControlImpl();
    Dm3ccRotateControlImpl rotateControl;
    ModbusHelper modbusHelper = ModbusHelper.getInstance();

    byte slaveID = 1;//副机编号 1~
    /**
     * @Method Name: setUp
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @Before
    public void setUp() throws Exception {
        //modbusBase.open("COM5");
        plcControlImpl.connect("/dev/ttyimx3");//
        //plcControlImpl.connect("/dev/ttymxc5");
        //modbusHelper.setModbusBase(plcControlImpl.getModbusBase());
        Dm3ccCmdCharHelper.setServoSpeed(slaveID, 2000);
        rotateControl = new Dm3ccRotateControlImpl(plcControlImpl.getModbusBase());
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
        Dm3ccCmdCharHelper cmdCharHelper = new Dm3ccCmdCharHelper(slaveID,modbusHelper.read(slaveID, Dm3ccCmdCharHelper.START_ADDRESS_READ, Dm3ccCmdCharHelper.CMD_CHAR_NUM));
        byte originStatus = cmdCharHelper.getOriginStatus();
        byte enableStatus = cmdCharHelper.getEnableStatus();
        byte errorReset = cmdCharHelper.getErrorReset();
        Log.i(TAG, "originStatus="+originStatus+",enableStatus="+enableStatus+",errorReset="+errorReset);
        Log.i(TAG, "queryStatusTest="+cmdCharHelper.toString());
        Thread.sleep(20000);
    }

    @Test
    public void move2AccessTest() throws InterruptedException, ModbusException, DriversException {
        //上一位置
        byte ofCol = 4; //1~20
        byte ofLayer = 4;
        try{
            rotateControl.move2Access(slaveID,ofLayer, ofCol);
        }catch (DriversException e){
            e.printStackTrace();
        }
        Log.i(TAG, "move2AccessTest rc=");
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void openBoxTest()throws InterruptedException, DriversException, ModbusException {
        long start = System.currentTimeMillis();
        byte ofLayer = 0;
        byte ofCol = 1; //1~20
        try{
            rotateControl.move2AccessAndOpen(slaveID,ofLayer, ofCol);
        }catch (DriversException e){
            e.printStackTrace();
        }
        Log.i(TAG, "move2AccessTest rc="+ (System.currentTimeMillis() - start)+" ms");
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void openAccessDoorTest() throws InterruptedException, DriversException, ModbusException {

        byte doorNo = 2;
        try{
            int rc = rotateControl.openAccessDoor(slaveID,doorNo, false);
            Log.i(TAG, "openAccessDoorTest doorNo="+doorNo+",rc="+rc);
            Thread.sleep(500);

        }catch (DriversException e){
           e.printStackTrace();
        }
        queryStatusTest();
    }
    @Test
    public void openAccessDoorForceTest()throws InterruptedException, DriversException ,ModbusException {

        byte doorNo = 0;
        int rc = rotateControl.openAccessDoor(slaveID,doorNo, true);
        Log.i(TAG, "openAccessDoorForceTest doorNo="+doorNo+",rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void openRepairDoorTest()throws InterruptedException, DriversException ,ModbusException {

        byte doorNo = 0;
        int rc = rotateControl.openRepairDoor(slaveID,doorNo);
        Log.i(TAG, "openRepairDoorTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }

    @Test
    public void openAllDoorTest()throws InterruptedException, DriversException ,ModbusException {
        int rc = rotateControl.openAllDoor(slaveID);
        Log.i(TAG, "openAllDoorTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void sendCheckTaskTest()throws InterruptedException, DriversException ,ModbusException {
        int rc = rotateControl.sendCheckTask(slaveID, 123);
        Log.i(TAG, "sendCheckTaskTest rc="+rc);
        Thread.sleep(500);
        queryStatusTest();
    }
    @Test
    public void queryCheckResultTest()throws InterruptedException, DriversException ,ModbusException {
        CheckResult checkResult= rotateControl.queryCheckResult(slaveID, 123);
        Log.i(TAG, "queryCheckResultTest rc="+checkResult.toString());
        Log.i(TAG, "queryCheckResultTest CheckStatus="+checkResult.getCheckStatus());


        for(int i=0; i<5; i++){
            int colNum = 20;
            int iLayer = i+1;
            String tmp = "";
            if(i== 0 || i==4){
                colNum = 15;
            }
            for(int j=1; j<=colNum; j++){
                byte goodsStatus= checkResult.getGoodsStatus(i,j);
                if(goodsStatus==1){
                    tmp += " "+j+"="+goodsStatus;
                }
            }
            Log.i(TAG, "Layer"+iLayer+":"+tmp);
        }
        //
        Thread.sleep(500);
    }
    @Test
    public void queryAllShelfBoxStatusTest()throws InterruptedException, DriversException ,ModbusException {
        SlaveDeskStatus slaveDeskStatus = rotateControl.querySlaveDeskStatus(slaveID);
        String data = JsonUtils.toJSONString(slaveDeskStatus);
        Log.i(TAG, "data="+data);

        SlaveDeskStatus deskStatus = JsonUtils.toBean(data,SlaveDeskStatus.class );
        Log.i(TAG, "ErrorMsg="+deskStatus.getErrorMsg());
        Log.i(TAG, "BoxCount="+deskStatus.getBoxCount());
        Log.i(TAG, "CurrentPos="+deskStatus.getCurrentPos());
        Log.i(TAG, "RepairDoorStatus="+deskStatus.getRepairDoorStatus());
        Log.i(TAG, "LayerCount="+deskStatus.getLayerCount());

    }

}
