package com.hzdongcheng.drivers.peripheral.scanner.impl;

import com.hzdongcheng.drivers.peripheral.scanner.AbstractScanner;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * 
 * @ClassName: DewoScanner 
 * @Description: Dewo扫描枪实现类
 * @author Administrator 
 * @date 2018年1月26 下午4:45:25 
 * @version 1.0
 */
public class DewoScanner extends AbstractScanner {

	public DewoScanner(boolean normallyMode) {
		super(normallyMode);
	}

	@Override
	protected String _searchVersion() throws NotOpenSerialException, 
										     SerialPortErrorException, 
										     SendTimeoutException, 
										     RecvTimeoutException, 
										     ResponseCodeException, 
										     ProtocolParsingException {
		return "";
	}

	@Override
	protected int _switchMode(boolean normallyMode) throws NotOpenSerialException, 
									   SerialPortErrorException, 
									   SendTimeoutException, 
									   RecvTimeoutException, 
									   ResponseCodeException, 
									   ProtocolParsingException {
		
		byte szRequestData[] = new byte[19];
		szRequestData[0] = 0x1A;  szRequestData[1] = 0x4B;  szRequestData[2] = 0x0D;
		szRequestData[3] = 0x38;  szRequestData[4] = 0x36;  szRequestData[5] = 0x31;
		szRequestData[6] = 0x30;  szRequestData[7] = 0x30;  szRequestData[8] = 0x32;
		szRequestData[9] = 0x30;  szRequestData[10] = 0x3B;  szRequestData[11] = 0x39;
		szRequestData[12] = 0x35;  szRequestData[13] = 0x37;  szRequestData[14] = 0x30;
		szRequestData[15] = 0x30;  szRequestData[16] = 0x32;  szRequestData[17] = 0x30;
		szRequestData[18] = 0x2E; 
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[8];
		//success return 8 bytes
		//50 41 50 48 48 46 06 2E
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		
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
	protected int _startScan() throws NotOpenSerialException, 
								      SerialPortErrorException, 
								      SendTimeoutException, 
								      RecvTimeoutException, 
								      ResponseCodeException, 
								      ProtocolParsingException {
		//扫码命令  {0x1A, 0x54, 0x0D};
		byte szRequestData[] = new byte[3];
		szRequestData[0] = 0x1A;
		szRequestData[1] = 0x54;
		szRequestData[2] = 0x0D;
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		
		return 0;
	}

	@Override
	protected int _stopScan() throws NotOpenSerialException, 
								     SerialPortErrorException, 
								     SendTimeoutException, 
								     RecvTimeoutException, 
								     ResponseCodeException, 
								     ProtocolParsingException {
		//扫码命令  {0x1A, 0x55, 0x0D};
		byte szRequestData[] = new byte[3];
		szRequestData[0] = 0x1A;
		szRequestData[1] = 0x55;
		szRequestData[2] = 0x0D;
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		
		return 0;
	}

}
