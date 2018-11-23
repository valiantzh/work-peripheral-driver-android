package com.hzdongcheng.drivers;

import android.support.test.runner.AndroidJUnit4;

import com.hzdongcheng.drivers.peripheral.cardreader.ICardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.event.CardReaderEvent;
import com.hzdongcheng.drivers.peripheral.cardreader.event.ICardReaderListener;
import com.hzdongcheng.drivers.peripheral.cardreader.helper.CardReaderHelper;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardReaderConfig;
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
public class CardReaderTest {
    CardReaderHelper cardReaderHelper;
    ICardReader cardReader;
    /**
     * @Method Name: setUp
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @Before
    public void setUp() throws Exception {
        CardReaderConfig config = new CardReaderConfig();
        config.setVendorName(CardReaderConfig.CARDREADER_VENDOR_JL);
        cardReaderHelper = CardReaderHelper.createInstance(config);
    }

    /**
     * @Method Name: tearDown
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param  @throws java.lang.Exception
     * @return void
     */
    @After
    public void tearDown() throws Exception {
        if(cardReaderHelper!= null){
            cardReaderHelper.stopReadCardTask();
        }

    }

    ICardReaderListener cardReaderListener=new ICardReaderListener() {

        @Override
        public void onNotice(CardReaderEvent obj) {
            // TODO Auto-generated method stub

            System.out.println(obj.getCardInformation());
            cardReaderHelper.removeListener();
        }
    };
    @Test
    public void test() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException, NotFoundSerialLibraryException, InitializeSerialPortException, InterruptedException {

        if(cardReaderHelper == null){
            return;
        }
        cardReaderHelper.open("COM5");
        cardReaderHelper.startReadCardTask();
        cardReaderHelper.addListener(cardReaderListener);

        Thread.sleep(100000);
    }
}
