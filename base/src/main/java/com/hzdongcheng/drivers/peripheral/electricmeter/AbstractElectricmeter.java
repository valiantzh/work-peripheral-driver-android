package com.hzdongcheng.drivers.peripheral.electricmeter;

import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * 
 * @ClassName: AbstractElectricmeter 
 * @Description: 抽象电表接口实现类
 * @author: Administrator
 * @date: 2018年2月11日 上午11:19:43
 */
public abstract class AbstractElectricmeter implements IElectricmeter {

	protected final SerialPortBase serialPortBase = new SerialPortBase();

	protected final int MAX_RECV_LEN = 1024;
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
			serialPortBase.initialize(SerialPortBase.DCDZ_CBR_1200, SerialPortBase.DCDZ_EVENPARITY, 
					SerialPortBase.DCDZ_ONESTOPBIT, SerialPortBase.DCDZ_DATABITS8);
			serialPortBase.setInOutQueue(MAX_RECV_LEN, MAX_SEND_LEN);
			serialPortBase.setReadWriteTimeouts(2000, 1000);
		} catch(SerialPortException e){
			close();
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

	/* (non Javadoc) 
	* <p>Title: readTheMeter</p> 
	* <p>Description: </p> 
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.electricmeter.IElectricmeter#readTheMeter() 
	*/
	@Override
	public String readTheMeter() throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		// TODO Auto-generated method stub
		return null;
	}

}
