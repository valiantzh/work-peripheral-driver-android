package com.hzdongcheng.drivers.peripheral.cardreader.helper;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hzdongcheng.drivers.peripheral.cardreader.ICardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.event.CardReaderEvent;
import com.hzdongcheng.drivers.peripheral.cardreader.event.ICardReaderListener;
import com.hzdongcheng.drivers.peripheral.cardreader.factory.CardReaderFactory;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardReaderConfig;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;

/** 
 * @ClassName: CardReaderHelper 
 * @Description: 读卡器帮助类
 * @author Administrator 
 * @date 2018年1月27 下午3:59:07 
 * @version 1.0 
 */
public final class CardReaderHelper {
	
	private static final Log4jUtils logger = Log4jUtils.createInstanse(CardReaderHelper.class);
	
	private ICardReader cardReader = null;
	
	private Collection<ICardReaderListener> listeners = null;
	private final ReadWriteLock _lock = new ReentrantReadWriteLock();
	
	private final ReadCardTask readCardTask = new ReadCardTask();
	private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor(); 
	private ScheduledFuture<?> future = null;

	private CardReaderHelper(){
		
	}
	public static CardReaderHelper createInstance(CardReaderConfig config){
		CardReaderHelper helper = new CardReaderHelper();

		if ( null == (helper.cardReader = CardReaderFactory.createInstance(config))){
			helper = null;
		}
		
		return helper;
	}
	public synchronized void open(String portName) throws NotFoundSerialLibraryException, 
	  InitializeSerialPortException, 
	  SerialPortErrorException {
		cardReader.open(portName);
	}
	
	public synchronized void close(){
		//注销监听事件
		removeListener();
		//停止读卡线程
		stopReadCardTask();
		//关闭读卡器
		cardReader.close();
	}
	
	public boolean addListener(ICardReaderListener listener){

		boolean bSuccess = false;

		try{
			_lock.writeLock().lock();
			{
				if (listeners == null)
					listeners = new HashSet<ICardReaderListener>();

				if (listeners != null){

					bSuccess = true;
					if (!listeners.contains(listener))
						bSuccess = listeners.add(listener);
				}
			} 
		}
		finally {
			_lock.writeLock().unlock();    
		}

		return bSuccess;
	}

	public void removeListener(ICardReaderListener listener){
		try{
			_lock.writeLock().lock();
			{
				if (listeners == null){
					return;
				}
				if(listeners.contains(listener)){
					listeners.remove(listener);
				}
			}
		}
		finally {
			_lock.writeLock().unlock();
		}
	}

	public void removeListener(){

		try{
			_lock.writeLock().lock();
			{
				if (listeners != null)
					listeners.clear();
			} 
		}
		finally {
			_lock.writeLock().unlock();
		}
	}
	
	public synchronized boolean startReadCardTask(){
		boolean bSuccess = true;

		if (future != null) {
			future.cancel(true);
		}
		try {
			future = service.scheduleWithFixedDelay(readCardTask, 0, 1, TimeUnit.SECONDS);
		} catch (Exception e){
			bSuccess = false;
			logger.error(e.getMessage());
		}
		return bSuccess;		
	}
	
	public synchronized void stopReadCardTask(){
		
		if (future != null){
			try {
				future.cancel(true);
				//service.shutdown();
			} catch (Exception e){
				logger.error(e.getMessage());
			}
		}
		
	}
	
	private class ReadCardTask implements Runnable {

		@Override
		public void run() {	
			logger.info("Read card task is started.");

			CardInformation cardInfo = null;
			
			try {
				cardInfo = cardReader.readCardInfo();
				logger.info(JsonUtils.toJSONString(cardInfo));
			} catch (DcdzSystemException e) {
				logger.error(e.getMessage());
			}

			if (cardInfo != null){

				CardReaderEvent event = new CardReaderEvent(this, 0, cardInfo);
				try {
					_lock.readLock().lock();
					{
						Iterator<ICardReaderListener> iter = listeners.iterator();
						while (iter.hasNext()) {
							ICardReaderListener listener = (ICardReaderListener) iter.next();
							try {
								listener.onNotice(event);
							} catch (Exception e) {
								logger.error(e.getMessage());
							}
						}
					}
				}
				finally{
					_lock.readLock().unlock();
				}
			}

			logger.info("Read card task end.");
		}
	}
	
	public ICardReader getCardReader(){
		return cardReader;
	}
}
