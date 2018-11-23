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
 * @ClassName: HoneywellScanner 
 * @Description: Honeywell扫描枪实现类
 * @author Administrator 
 * @date 2018年1月26 下午4:45:48 
 * @version 1.0
 */
public class HoneywellScanner extends AbstractScanner{

	public HoneywellScanner(boolean normallyMode){
		super(normallyMode);
	}
	
	@Override
	public String _searchVersion() 
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
			       RecvTimeoutException, ResponseCodeException {
		
		//{0x16,0x4D,0x0D,0x52,0x45,0x56,0x5f,0x44,0x52,0x2E};
		byte szRequestData[] = new byte[10];
		szRequestData[0] = 0x16;  szRequestData[1] = 0x4D;  szRequestData[2] = 0x0D;
		szRequestData[3] = 0x52;  szRequestData[4] = 0x45;  szRequestData[5] = 0x56;
		szRequestData[6] = 0x5F;  szRequestData[7] = 0x44;  szRequestData[8] = 0x52;
		szRequestData[9] = 0x2E;

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[49];
		//success return 49 bytes
		//52 45 56 5F 44 52 20 49 6E 74 65 67 72 61 74 65 64 20 44 65 63 6F 64 65
		//72 20 56 65 72 73 69 6F 6E 3A 20 32 30 31 32 2E 33 2E 36 30 35 0D 0A 06 2E
		
		//52 45 56 5F 44 52 20 49 6E 74 65 67 72 61 74 65 64 20 44 65 63 6F 64 65 
		//72 20 56 65 72 73 69 6F 6E 3A 20 32 30 31 32 2E 33 2E 36 30 35 0D 0A 06 2E
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		return StringUtils.byteArrayToString(recvBuff);
	}

	@Override
	public int _switchMode(boolean normallyMode)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
		       RecvTimeoutException, ResponseCodeException {

		//char szRequestData[10] = {0x16,0x4D,0x0D,0x50,0x41,0x50,0x48,0x48,0x46,0x2E};
		byte szRequestData[];
		if (normallyMode){ //常量模式16 4D 0D 53 55 46 42 4B 32 39 39 30 64 30 61 2E  16 4D 0D 50 41 50 53 50 43 2E  16 4D 0D 32 33 32 42 41 44 35 2E
			szRequestData = new byte[]{0x16, 0x4D, 0x0D, 0x53, 0x30, 0x55, 0x46, 0x42,0x4B, 0x32, 0x39, 0x39, 0x30, 0x64, 0x30, 0x61,  0x2E,
					0x16, 0x4D, 0x0D, 0x50, 0x41, 0x50, 0x53,0x50, 0x43, 0x2E,					 //长亮模式
					0x16, 0x4D, 0x0D, 0x32, 0x33, 0x32, 0x42, 0x41,0x44, 0x35, 0x2E};//9600
		} else {//触发模式16 4D 0D 53 55 46 42 4B 32 39 39 30 64 30 61 2E  16 4D 0D 50 41 50 48 48 46 2E  16 4D 0D 32 33 32 42 41 44 35 2E
			szRequestData = new byte[]{0x16, 0x4D, 0x0D, 0x53, 0x30, 0x55, 0x46, 0x42,0x4B, 0x32, 0x39, 0x39, 0x30, 0x64, 0x30, 0x61,  0x2E,
					0x16, 0x4D, 0x0D, 0x50, 0x41, 0x50, 0x48,0x48, 0x46, 0x2E,					 //触发模式
					0x16, 0x4D, 0x0D, 0x32, 0x33, 0x32, 0x42, 0x41,0x44, 0x35, 0x2E};//9600
		}
		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[14];
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		recvBuff = new byte[8];
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		recvBuff = new byte[9];
		serialPortBase.recvFixLength(recvBuff, recvBuff.length);
		
		this.normallyMode = normallyMode;
		
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
	public int _startScan()
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
		       RecvTimeoutException, ResponseCodeException {
		
//		//扫码命令 {0x16, 0x54 + (on ? 0 : 1), 0x0D};
//		byte szRequestData[] = new byte[]{0x16, 0x54, 0x0D};
//		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		return 0;
	}

	@Override
	public int _stopScan()
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, 
		       RecvTimeoutException, ResponseCodeException {
		
//		//扫码命令 {0x16, 0x54 + (on ? 0 : 1), 0x0D};
//		byte szRequestData[] = new byte[]{0x16, 0x55, 0x0D};
//		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		return 0;
	}

}
