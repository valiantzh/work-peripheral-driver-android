package com.hzdongcheng.drivers.peripheral.printer;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterStatus;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;


/**
 * 
 * @ClassName: AbstractPrinter 
 * @Description: 打印机抽象类
 * @author: Administrator
 * @date: 2018年1月29日 上午9:01:56
 */
public abstract class AbstractPrinter implements IPrinter {
	
	protected final SerialPortBase serialPortBase = new SerialPortBase();
	protected final ReadWriteLock _lock = new ReentrantReadWriteLock();
	
	protected final int MAX_RECV_LEN = 4*1024;
	protected final int MAX_SEND_LEN = 100;
	
	/* (non Javadoc) 
	* <p>Title: open</p> 
	* <p>Description: </p> 
	* @param portName
	* @throws NotFoundSerialLibraryException
	* @throws InitializeSerialPortException
	* @throws SerialPortErrorException 
	* @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#open(java.lang.String)
	*/
	@Override
	public void open(String portName)
			throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
		serialPortBase.open(portName);
		
		try{
			serialPortBase.initialize(SerialPortBase.DCDZ_CBR_9600, SerialPortBase.DCDZ_NOPARITY, 
					SerialPortBase.DCDZ_ONESTOPBIT, SerialPortBase.DCDZ_DATABITS8);
			serialPortBase.setInOutQueue(MAX_RECV_LEN, MAX_SEND_LEN);
			serialPortBase.setReadWriteTimeouts(2000, 1000);
		} catch(SerialPortException e){
			throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
		}
	}
	/* (non Javadoc) 
	* <p>Title: close</p> 
	* <p>Description: </p>  
	* @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#close()
	*/
	@Override
	public void close() {
		serialPortBase.close();
	}
	//打印
	protected abstract String _print(String data) throws DriversException;
	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.printer.IPrinter#print(byte[])
	 */
	@Override
	public void print(String data) throws DriversException {
		// TODO Auto-generated method stub
		_print(data);
	}
	//切纸
	protected abstract void _cutPaper(int mode) throws DriversException;
	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.printer.IPrinter#cutPaper(int)
	 */
	@Override
	public void cutPaper(int mode) throws DriversException {
		// TODO Auto-generated method stub
		_cutPaper(mode);
	}
	//蜂鸣器控制命令
	protected abstract void _buzzer() throws DriversException;
	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.printer.IPrinter#onBuzzer()
	 */
	@Override
	public void onBuzzer() throws DriversException {
		// TODO Auto-generated method stub
		_buzzer();
		
	}
	//状态
	protected abstract PrinterStatus _getStatus() throws DriversException ;
	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.printer.IPrinter#getStatus()
	 */
	@Override
	public PrinterStatus getStatus()
			throws DriversException {
		// TODO Auto-generated method stub
		return _getStatus();
	}
	
}
