package com.hzdongcheng.drivers.peripheral.thermostat;


import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.RelayStatus;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.TempControlStatus;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * 
 * @ClassName: AbstractScanner
 * @Description: TODO(温控器控制抽象类)
 * @author Jxing
 * @date 2017年5月10日 下午3:28:06
 * @version 1.0
 */
public abstract class AbstractThermostat implements IThermostat {

	// 串口类对象
	protected final SerialPortBase serialPortBase;

	protected AbstractThermostat(SerialPortBase serialPortBase) {
		this.serialPortBase = serialPortBase;
	}

	protected abstract TempControlStatus _getThermostatStatus(int ThermostatID)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	public TempControlStatus getThermostatStatus(int ThermostatID)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException {

		return _getThermostatStatus(ThermostatID);

	}

	protected abstract void _setOpenTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException,ProtocolParsingException;

	public void setOpenTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException {

		_setOpenTemperature(ThermostatID, temperature);

	}

	protected abstract void _setCloseTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	public void setCloseTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException {

		_setCloseTemperature(ThermostatID, temperature);

	}

	protected abstract int _getOpenTemperature(int ThermostatID)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	public int getOpenTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		return _getOpenTemperature(ThermostatID);

	}

	protected abstract int _getCloseTemperature(int ThermostatID)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	public int getCloseTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		return _getCloseTemperature(ThermostatID);

	}

	protected abstract int _getTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	public int getTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		return _getTemperature(ThermostatID);

	}

	protected abstract RelayStatus _getRelayStatus(int ThermostatID)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	public RelayStatus getRelayStatus(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		return _getRelayStatus(ThermostatID);
	}

	/**
	 * @throws ProtocolParsingException
	 * 			@throws ResponseTimeoutException @throws
	 *             SerialErrorException @throws NotOpenSerialException
	 * 
	 * @Title: recv @Description: TODO(接收驱动板返回的指令) @param @param
	 *         buff @param @param len @param @throws SystemException
	 *         设定文件 @return void 返回类型 @throws
	 */
	protected int recv(byte[] buff, int len, byte[] szRequestData)
			throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException, ProtocolParsingException {

		byte count = 0;
		byte[] head = new byte[2];

		do {
			serialPortBase.recvFixLength(head, 2);

			if (head[0] == szRequestData[0] && head[1] == szRequestData[1])
				break;

			if (++count == 100) {
				throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING,
						"Recv data failed, because not found 'ThermostatID' protocol head.");
			}
		} while (true);

		// 得到协议头
		System.arraycopy(head, 0, buff, 0, 2);

		// 读取功能码
		byte[] szBody = new byte[len - 4];
		serialPortBase.recvFixLength(szBody, len - 4);

		// 拷贝数据到buff
		System.arraycopy(szBody, 0, buff, 2, len - 4);

		byte[] szGetCRC = new byte[2];
		serialPortBase.recvFixLength(szGetCRC, 2);

		getCRC(buff, buff.length - 2);

		// 校验数据完整性
		if (buff[buff.length - 2] != szGetCRC[0] || buff[buff.length - 1] != szGetCRC[1]) {
			throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, "CRC Check failure .");
		}

		return 0;
	}

	/**
	 * /**
	 * 
	 * @Title: getCRC @Description: TODO(计算CRC校验码) @param @param
	 *         array @param @param len @param @return 设定文件 @return int
	 *         返回类型 @throws
	 */
	protected int getCRC(byte[] pszBuff, int len) {

		int CRC = 0x0000ffff;
		int POLYNOMIAL = 0x0000a001;
		int i, j;

		for (i = 0; i < len; i++) {
			CRC ^= ((int) pszBuff[i] & 0x000000ff);
			for (j = 0; j < 8; j++) {
				if ((CRC & 0x00000001) != 0) {
					CRC >>= 1;
					CRC ^= POLYNOMIAL;
				} else {
					CRC >>= 1;
				}
			}
		}
		pszBuff[len] = (byte) (CRC & 0x00ff);
		pszBuff[len + 1] = (byte) (CRC >> 8);
		return 0;
	}

	/**
	 * 
	 * @Title: setTbyte @Description: TODO(温度转换byte[]) @param @param
	 *         pszBuff @param @param temperature @param @return 设定文件 @return
	 *         void 返回类型 @throws
	 */
	protected void setTbyte(byte[] pszBuff, int temperature, int frist, int list) {
		pszBuff[frist] = (byte) (temperature >> 8);
		pszBuff[list] = (byte) (temperature & 0x00ff);
	}

	/**
	 * 
	 * @Title: getTint @Description: TODO(byte[]转换温度) @param @param
	 *         pszBuff @param @param temperature @param @return 设定文件 @return int
	 *         返回类型 @throws
	 */
	protected int getTint(byte[] pszBuff, int frist, int list) {
		return (pszBuff[frist] << 8) + (pszBuff[list] & 0x00ff);
	}

	/**
	 * 
	 * @Title: getBinary @Description: TODO(获取温控器状态,二进制数) @param @param
	 *         pszBuff @param @param temperature @param @return 设定文件 @return
	 *         void 返回类型 @throws
	 */
	protected byte[] getBinary(byte[] pszBuff, int param) {
		byte[] szRecvArray = new byte[16];
		for (int i = 0; i < szRecvArray.length; i++) {
			if (i < 8)
				szRecvArray[i] = (byte) ((pszBuff[param] >> i) & 0x01);
			else
				szRecvArray[i] = (byte) ((pszBuff[param + 1] >> (i - 8)) & 0x01);
		}
		return szRecvArray;
	}
}
