package com.hzdongcheng.drivers.base.serialport.exception.error;

import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;

public class SerialPortErrorCode extends ErrorCode{

	public static final int ERR_SP_NOTFOUNDSERIALLIBRARY = 10100;
	public static final int ERR_SP_SERIALPORT = 10101;
	public static final int ERR_SP_NOTOPENSERIALPORT = 10102;
	public static final int ERR_SP_SENDDATATIMEOUT = 10103;
	public static final int ERR_SP_RECVDATATIMEOUT = 10104;
	public static final int ERR_SP_INITIALIZE = 10105;
	
	static {
		/*serial port error define*/
		ErrorTitle.putErrorTitle(ERR_SP_NOTFOUNDSERIALLIBRARY, 
				"Can not found .so file.");
		ErrorTitle.putErrorTitle(ERR_SP_SERIALPORT, 
				"Serial port error.");
		ErrorTitle.putErrorTitle(ERR_SP_NOTOPENSERIALPORT, 
				"Open serial port failed.");
		ErrorTitle.putErrorTitle(ERR_SP_SENDDATATIMEOUT, 
				"Send data to serial port timeout.");
		ErrorTitle.putErrorTitle(ERR_SP_RECVDATATIMEOUT, 
				"recv data from serial port timeout.");	
		ErrorTitle.putErrorTitle(ERR_SP_INITIALIZE, 
				"Initialize serial port failed.");		
	}
}
