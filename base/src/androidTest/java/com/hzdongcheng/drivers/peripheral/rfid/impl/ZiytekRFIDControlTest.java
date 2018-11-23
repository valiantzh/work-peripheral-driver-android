package com.hzdongcheng.drivers.peripheral.rfid.impl;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.hzdongcheng.drivers.peripheral.rfid.IRFIDControl;
import com.hzdongcheng.drivers.peripheral.rfid.factory.RFIDControlFactory;
import com.hzdongcheng.drivers.peripheral.rfid.model.RFIDConfig;
import com.hzdongcheng.drivers.peripheral.rfid.model.TagInformation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ZiytekRFIDControlTest {
    IRFIDControl irfidControl;
    private final static String TAG = "TagInformation";

    @Before
    public void setUp() {
        RFIDConfig config = new RFIDConfig();
        config.setVendorName(RFIDConfig.RFID_VENDOR_ZIYTEK);
        irfidControl = RFIDControlFactory.createInstance(config);
    }

    @Test
    public void readTagInfo() {
        try {
            irfidControl.open("/dev/ttymxc2");
            List<TagInformation> list = irfidControl.readTagInfo();
            irfidControl.close();
            for (TagInformation tagInformation : list) {
                Log.d(TAG, tagInformation.toString());
            }
            Log.d(TAG, "Count="+list.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void cancelInfo() {
        try {
            irfidControl.open("/dev/ttymxc2");
            irfidControl.cancel();
            irfidControl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void resetInfo() {
        try {
            irfidControl.open("/dev/ttymxc2");
            irfidControl.reset();
            irfidControl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void abortInfo() {
        try {
            irfidControl.open("/dev/ttymxc2");
            irfidControl.abort();
            irfidControl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAntennaStatusTest() {
        try {
            irfidControl.open("/dev/ttymxc2");
            int status = irfidControl.getAntennaStatus(0);
            Log.d(TAG,""+status);
            irfidControl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}