package com.hzdongcheng.drivers.peripheral.cardreader.impl;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.hzdongcheng.alxdrivers.jni.ISerialEvent;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardreader.AbstractCardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;

import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.components.toolkits.utils.StringUtils;

/**
 * 
 * @ClassName: StdCardReader 
 * @Description: 东城读卡器控制类
 * @author Administrator 
 * @date 2018年1月27 下午3:54:58 
 * @version 1.0
 */
public final class StdCardReader extends AbstractCardReader{
	
	private String cardID = "";
	
	private Log4jUtils logger = Log4jUtils.createInstanse(this.getClass());
	
	private final Vector<Byte> byteArray = new Vector<Byte>();
	private final HandleEvent handleEvent = new HandleEvent();

	
	/* (non Javadoc) 
	* <p>Title: open</p> 
	* <p>Description: </p> 
	* @param portName
	* @throws NotFoundSerialLibraryException
	* @throws InitializeSerialPortException
	* @throws SerialPortErrorException 
	* @see com.hzdongcheng.components.driver.cardreader.AbstractCardReader#open(java.lang.String) 
	*/
	@Override
	public void open(String portName)
			throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
		super.open(portName);
		
		try {
			serialPortBase.openEventMode(handleEvent);
		} catch (NotOpenSerialException e) {
			throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
		}
	}

	/* (non Javadoc) 
	* <p>Title: readCardInfo</p> 
	* <p>Description: </p> 
	* @return
	* @throws DriversException 
	* @see com.hzdongcheng.components.driver.cardreader.AbstractCardReader#readCardInfo() 
	*/
	@Override
	public CardInformation readCardInfo() throws DriversException {
		
		CardInformation cardInfo = new CardInformation();
		
		cardInfo.setCardID("");

		try{
			_lock.readLock().lock();

			if (StringUtils.isNotEmpty(cardID)){
				cardInfo.setCardID(cardID);
				cardID = "";
			}
		}
		finally{
			_lock.readLock().unlock();
		}
		
		return cardInfo;
	}
	
	private class HandleEvent implements ISerialEvent {

		@Override
		public void fire_HandleData(int ret, byte[] szData) {
			if (ret <= 0){
				logger.error("Card Reader read event handler error, result is " + ret + ".");
				return;
			}

			//check 02 start 03 end
			int bufferLength = szData.length;

			for (int i = 0; i < bufferLength; i++)
				byteArray.add(szData[i]);			

			byte start = 0x02;
			byte end   = 0x03;

			int startIndex = -1;
			int endIndex   = -1;

			startIndex = byteArray.indexOf(start);
			endIndex   = byteArray.indexOf(end);

			if (-1 == startIndex){
				byteArray.clear();
				return;
			}

			if (-1 == endIndex)
				return;

			if (endIndex <= startIndex + 1){
				byteArray.clear();
				return;				
			}

			byte[] szCardID = new byte[endIndex - startIndex - 1];
			for(int i = startIndex + 1, j = 0; i < endIndex; i++, j++){
				szCardID[j] = byteArray.get(i).byteValue();
			}

			byteArray.clear();

			try {
				_lock.writeLock().lock();
				cardID = new String(szCardID, "gb2312");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			finally{
				_lock.writeLock().unlock();
			}
		}
	}
}
