/**   
 * Copyright © 2018.
 * 
 * @Package: com.hzdongcheng.drivers.peripheral.cardsender
 * @author: Administrator   
 * @date: 2018年2月7日 下午5:32:08 
 */
package com.hzdongcheng.drivers.peripheral.cardsender;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;

/** 
 * @ClassName: AbstractCardSender 
 * @Description: 发卡器抽象类
 * @author: Administrator
 * @date: 2018年2月7日 下午5:32:08  
 */
public abstract class AbstractCardSender implements ICardSender{
	protected final SerialPortBase serialPortBase = new SerialPortBase();
	protected final ReadWriteLock _lock = new ReentrantReadWriteLock();

	protected final int MAX_RECV_LEN = 1024;
	protected final int MAX_SEND_LEN = 1000;
	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#open(java.lang.String)
	 */
	@Override
	public void open(String portName)
			throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
		serialPortBase.open(portName);

		try {
			serialPortBase.initialize(SerialPortBase.DCDZ_CBR_9600, SerialPortBase.DCDZ_NOPARITY,
					SerialPortBase.DCDZ_ONESTOPBIT, SerialPortBase.DCDZ_DATABITS8);
			serialPortBase.setInOutQueue(MAX_RECV_LEN, MAX_SEND_LEN);
			serialPortBase.setReadWriteTimeouts(2000, 1000);
		} catch (SerialPortException e) {
			throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
		}
		
	}
	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		serialPortBase.close();
	}
	/* (non Javadoc) 
	 * @Title: moveCard2ReadPosition
	 * @Description: TODO
	 * @param isRF
	 * @return
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.ICardSender#sendCard2ReadPosition(boolean)
	 */ 
	@Override
	public String moveCard2ReadPosition(boolean isRF) throws DriversException {
		throw new DriversException(DriversErrorCode.ERR_PD_DRIVERFUNNOTEXISTS);
	}
	/* (non Javadoc) 
	 * @Title: readData
	 * @Description: TODO
	 * @param section
	 * @param block
	 * @return
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.ICardSender#readData(byte, byte)
	 */ 
	@Override
	public byte[] readData(byte section, byte block) throws DriversException {
		throw new DriversException(DriversErrorCode.ERR_PD_DRIVERFUNNOTEXISTS);
	}
	/* (non Javadoc) 
	 * @Title: writeData
	 * @Description: TODO
	 * @param section
	 * @param block
	 * @param data
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.ICardSender#writeData(byte, byte, byte[])
	 */ 
	@Override
	public void writeData(byte section, byte block, byte[] data) throws DriversException {
		throw new DriversException(DriversErrorCode.ERR_PD_DRIVERFUNNOTEXISTS);
	}
	/* (non Javadoc) 
	 * @Title: modifyPassword
	 * @Description: TODO
	 * @param section
	 * @param keyA
	 * @param keyB
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.ICardSender#modifyPassword(byte, byte[], byte[])
	 */ 
	@Override
	public void modifyPassword(byte section, byte[] keyA, byte[] keyB) throws DriversException {
		throw new DriversException(DriversErrorCode.ERR_PD_DRIVERFUNNOTEXISTS);
	}
	/* (non Javadoc) 
	 * @Title: setBuzzerOnOff
	 * @Description: TODO
	 * @param onOff
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.ICardSender#setBuzzerOnOff(boolean)
	 */ 
	@Override
	public void setBuzzerOnOff(boolean onOff) throws DriversException {
		throw new DriversException(DriversErrorCode.ERR_PD_DRIVERFUNNOTEXISTS);
	}
	
	
	
}
