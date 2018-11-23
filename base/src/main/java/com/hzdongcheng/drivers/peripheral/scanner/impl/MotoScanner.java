package com.hzdongcheng.drivers.peripheral.scanner.impl;

import com.hzdongcheng.drivers.peripheral.scanner.AbstractScanner;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;

/**
 * 
 * @ClassName: MotoScanner 
 * @Description: TODO(MOTO扫描枪实现类) 
 * @author Administrator 
 * @date 2018年1月26 下午4:46:12 
 * @version 1.0
 */
public class MotoScanner extends AbstractScanner{
	
	public MotoScanner(boolean normallyMode){
		super(normallyMode);
	}
	
	@Override
	public String _searchVersion()
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
		       RecvTimeoutException, ResponseCodeException {
		// TODO Auto-generated method stub
		//{0x04,0xA3,0x04,0x00,0xff,0x55}
		byte szRequestData[] = new byte[6];
		szRequestData[0] = 0x04;
		szRequestData[1] = (byte) 0xA3;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x00;
		szRequestData[4] = (byte) 0xff;
		szRequestData[5] = 0x55;

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		//
		//success return 31 bytes
		//1D A4 00 00 50 41 41 41 53 53 30 30 2D 30 30 35 2D 52 30 31 20 20 20 46 20 56 20 01 01 FA 46
		byte[] recvBuff = new byte[31];
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		return StringUtils.byteArrayToString(recvBuff);
	}
	
	@Override
	protected int _switchMode(boolean normallyMode)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
		       RecvTimeoutException, ResponseCodeException {
		// TODO Auto-generated method stub
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
	public int _startScan() throws NotOpenSerialException, 
								   SerialPortErrorException, 
								   SendTimeoutException,
								   RecvTimeoutException,
								   ResponseCodeException,
								   ProtocolParsingException {

		//扫码命令  {0x04, 0xE9, 0x04, 0x00, 0xFF, 0x0F};
		byte szRequestData[] = new byte[6];
		szRequestData[0] = 0x04;
		szRequestData[1] = (byte) 0xE4;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x00;
		szRequestData[4] = (byte) 0xff;
		szRequestData[5] = 0x14;

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[6];
		//success return 6 bytes
		//04 D0 00 00 FF 2C
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		
		if (recvBuff[0] != 0x04 || recvBuff[1] != 0xD0)
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);
		
		return 0;
	}
	
	@Override
	public int _stopScan()
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
		       RecvTimeoutException, ResponseCodeException {
		// TODO Auto-generated method stub
		//扫码命令  {0x04, 0xEA, 0x04, 0x00, 0xFF, 0x0E};
		byte szRequestData[] = new byte[6];
		szRequestData[0] = 0x04;
		szRequestData[1] = (byte) 0xEA;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x00;
		szRequestData[4] = (byte) 0xff;
		szRequestData[5] = 0x0E;
		
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[6];
		//success return 6 bytes
		//04 D0 00 00 FF 2C
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		
		if (recvBuff[0] != 0x04 || recvBuff[1] != 0xD0)
			throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE);
		
		return 0;
	}
}
