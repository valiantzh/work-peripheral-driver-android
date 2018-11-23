package com.hzdongcheng.drivers;

import android.support.test.runner.AndroidJUnit4;

import com.hzdongcheng.drivers.peripheral.scanner.IScanner;
import com.hzdongcheng.drivers.peripheral.scanner.event.IScannerListener;
import com.hzdongcheng.drivers.peripheral.scanner.event.ScannerEvent;
import com.hzdongcheng.drivers.peripheral.scanner.factory.ScannerFactory;
import com.hzdongcheng.drivers.peripheral.scanner.model.ScannerConfig;
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
public class ScannerTest {
    IScanner scanner;
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown()throws Exception {
        if(scanner!=null){
            scanner.close();
        }
    }
    IScannerListener scannerListener=new IScannerListener() {

        @Override
        public void onNotice(ScannerEvent obj) {
            // TODO Auto-generated method stub
            System.out.println(obj.getDataString());
            try {
                scanner.stopScan();
            } catch (NotOpenSerialException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SerialPortErrorException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SendTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RecvTimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ResponseCodeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ProtocolParsingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    @Test
    public void scanTest(){
        ScannerConfig config = new ScannerConfig();
        config.setNormallyOn(true);
        config.setVendorName(ScannerConfig.SCANNER_VENDOR_NEWLAND);
        scanner = ScannerFactory.createInstance(config);
        try{
            scanner.open("/dev/ttyUSB0");
            //scanner.switchMode(false);
            //scanner.oneDScanningOnOff(true);
            scanner.addListener(scannerListener);
            scanner.startScan();
            Thread.sleep(200000);
            scanner.stopScan();
            Thread.sleep(10000);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

}
