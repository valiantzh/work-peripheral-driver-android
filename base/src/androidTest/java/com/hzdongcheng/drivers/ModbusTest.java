package com.hzdongcheng.drivers;

import android.support.test.runner.AndroidJUnit4;

import com.hzdongcheng.drivers.base.modbus.ModbusBase;
import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 2018/3/21.
 */
@RunWith(AndroidJUnit4.class)
public class ModbusTest {
    static{
        try{
            //loadLibraryHelper.loadLibrary();
//            System.loadLibrary("CommAndModbus");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    ModbusBase modbusBase = new ModbusBase();
    /**
     * @Method Name: setUp
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @Before
    public void setUp() throws Exception {
        //modbusBase.open("COM5");
    }

    /**
     * @Method Name: tearDown
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @After
    public void tearDown() throws Exception {

        modbusBase.disconnect();
    }


    @Test
    public void test() throws ModbusException, InterruptedException {
        //modbusBase.initialize(SerialPortBase.DCDZ_CBR_9600,SerialPortBase.DCDZ_NOPARITY_C,SerialPortBase.DCDZ_ONE5STOPBITS, SerialPortBase.DCDZ_DATABITS8);
        modbusBase.initialize(SerialPortBase.DCDZ_CBR_9600,SerialPortBase.DCDZ_NOPARITY_C,SerialPortBase.DCDZ_ONE5STOPBITS, SerialPortBase.DCDZ_DATABITS8);

        //boolean isok = modbusBase.connect("/dev/ttyimx3");//ttymxc5
        boolean isok = modbusBase.connect("/dev/ttymxc6");
        Thread.sleep(100);
        if(isok){
            int charNum = 80;
            char[]  szDataW = new char[charNum];
            szDataW[0] = 1;
            szDataW[1] = 2;
            szDataW[charNum-1] = 8;
            //szDataW = new char[charNum];
            int rc=0;
            rc = modbusBase.write(1,39168, szDataW.length, szDataW);//
            System.out.println("write rc="+rc+","+szDataW[0]);
            char[]  szDataR = new char[charNum];
            rc = modbusBase.read(1,39168, szDataR.length, szDataR);//
            System.out.println("read rc="+rc+","+szDataR[0]);


        }

        Thread.sleep(100000);
    }
}
