package com.hzdongcheng.drivers.peripheral.scanner.impl;

import com.hzdongcheng.drivers.peripheral.scanner.AbstractScanner;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;

/**
 * 
 * @ClassName: DatalogicScanner 
 * @Description: Datalogic扫描枪实现类
 * @author Administrator 
 * @date 2018年1月26 下午3:59:47 
 * @version 1.0
 */
public class DatalogicScanner extends AbstractScanner{

	public DatalogicScanner(boolean normallyMode){
		super(normallyMode);
	}
	
	@Override
	protected String _searchVersion() 
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
			       RecvTimeoutException, ResponseCodeException{
		// {0x24,0x2b,0x24,0x21,0x0d};
		byte szRequestData[] = new byte[]{0x24, 0x2B, 0x24, 0x21, 0x0D};
		
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[72];
		//
		//success return 72 bytes
		//47 72 79 70 68 6F 6E 2D 47 46 53 34 34 35 30 20 53 4F 46 54 57 41 52 45 20 52 45 4C 45 41
		//53 45 20 36 31 30 30 31 35 35 30 35 20 42 55 49 4C 44 20 30 2E 30 2E 30 2E 30 31 35 20 31
		//37 20 44 65 63 2C 20 32 30 31 33 0D
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		
		return StringUtils.byteArrayToString(recvBuff);
	}
	
	@Override
	protected int _switchMode(boolean normallyMode) 
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
			       RecvTimeoutException, ResponseCodeException{
		return 0;
	}

	/* (non Javadoc) 
	* <p>Title: oneDScanningOnOff</p> 
	* <p>Description: </p> 
	* @param onOff
	* @return 
	* @see com.hzdongcheng.components.driver.scanner.IScanner#oneDScanningOnOff(boolean) 
	*/
	@Override
	protected int _oneDScanningOnOff(boolean onOff) throws NotOpenSerialException, 
														   SerialPortErrorException, 
														   SendTimeoutException, 
														   RecvTimeoutException, 
														   ResponseCodeException, 
														   ProtocolParsingException {
		return 0;
	}

	/* (non Javadoc) 
	* <p>Title: twoDScanningOnOff</p> 
	* <p>Description: </p> 
	* @param onOff
	* @return 
	* @see com.hzdongcheng.components.driver.scanner.IScanner#twoDScanningOnOff(boolean) 
	*/
	@Override
	protected int _twoDScanningOnOff(boolean onOff) throws NotOpenSerialException, 
														   SerialPortErrorException, 
														   SendTimeoutException, 
														   RecvTimeoutException, 
														   ResponseCodeException, 
														   ProtocolParsingException {
		return 0;
	}

	/* (non Javadoc) 
	* <p>Title: enableScanningOnOff</p> 
	* <p>Description: </p> 
	* @param onOff
	* @return 
	* @see com.hzdongcheng.components.driver.scanner.IScanner#enableScanningOnOff(boolean) 
	*/
	@Override
	protected int _enableScanningOnOff(boolean onOff) throws NotOpenSerialException, 
															 SerialPortErrorException, 
															 SendTimeoutException, 
															 RecvTimeoutException, 
															 ResponseCodeException, 
															 ProtocolParsingException {
		return 0;
	}
	
	@Override
	protected int _startScan()
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
			       RecvTimeoutException, ResponseCodeException {	
		//扫码命令 {on ? 0x45 : 0x44};
		byte szRequestData[] = new byte[]{0x45};
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		return 0;
	}

	@Override
	protected int _stopScan()
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
				   RecvTimeoutException, ResponseCodeException{
		//扫码命令 {on ? 0x45 : 0x44};
		byte szRequestData[] = new byte[]{0x44};
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);	
		return 0;
	}
}
