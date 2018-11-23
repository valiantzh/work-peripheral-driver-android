package com.hzdongcheng.drivers.peripheral.thermostat.impl;

import com.hzdongcheng.drivers.peripheral.thermostat.AbstractThermostat;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.RelayStatus;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.TempControlStatus;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.ThermostatDeviceStatus;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.ThermostatWarning;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * 
 * @ClassName: DatalogicScanner
 * @Description: TODO(w温控器实现类)
 * @author Jxing
 * @date 2017年5月10日 下午3:59:47
 * @version 1.0
 */
public class ThermostatImpl extends AbstractThermostat {


	public ThermostatImpl(SerialPortBase serialPortBase) {
		super(serialPortBase);
	}

	protected TempControlStatus _getThermostatStatus(int ThermostatID)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException {

		TempControlStatus tempControlStatus = new TempControlStatus();
		ThermostatWarning thermostatWarning = new ThermostatWarning();
		ThermostatDeviceStatus thermostatDeviceStatus = new ThermostatDeviceStatus();

		byte[] szRequestData = new byte[7];
		szRequestData[0] = (byte) ThermostatID;
		szRequestData[1] = 0x42;
		szRequestData[2] = 0x09;
		szRequestData[3] = 0x00;
		szRequestData[4] = 0x00;
		szRequestData[5] = 0x00;
		szRequestData[6] = 0x00;
		getCRC(szRequestData, szRequestData.length - 2);

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		;
		byte[] recvBuff = new byte[23];
		recv(recvBuff, recvBuff.length, szRequestData);

		tempControlStatus.setProductType(getTint(recvBuff, 3, 4));
		tempControlStatus.setCuvinTemp(getTint(recvBuff, 5, 6));
		tempControlStatus.setDefrostingTemp(getTint(recvBuff, 7, 8));
		tempControlStatus.setCodeOpenTemp(getTint(recvBuff, 9, 10));
		tempControlStatus.setCodeCloseTemp(getTint(recvBuff, 11, 12));
		tempControlStatus.setHeatOpenTemp(getTint(recvBuff, 13, 14));
		tempControlStatus.setHeatCloseTemp(getTint(recvBuff, 15, 16));

		/**
		 * 温控器警报状态 0：无报警；1：报警
		 */
		byte[] szRecvArray = getBinary(recvBuff, 17);
		thermostatWarning.setDoorWarn(szRecvArray[0]);
		thermostatWarning.setStressWarn(szRecvArray[15]);
		thermostatWarning.setInstancyWarn(szRecvArray[14]);
		thermostatWarning.setGeneralWarn(szRecvArray[13]);
		thermostatWarning.setTimeoutWarn(szRecvArray[12]);
		thermostatWarning.setCuvinMinWarn(szRecvArray[11]);
		thermostatWarning.setCuvinMaxWarn(szRecvArray[10]);
		thermostatWarning.setDefrostingSondeWarn(szRecvArray[9]);
		thermostatWarning.setCuvinSondeWarn(szRecvArray[8]);

		/**
		 * 
		 * @author 温控器设备状态 0：关闭；1：开启
		 *
		 */
		szRecvArray = getBinary(recvBuff, 19);

		thermostatDeviceStatus.setTempStatus(szRecvArray[12]);
		thermostatDeviceStatus.setFanStatus(szRecvArray[11]);
		thermostatDeviceStatus.setHeatStatus(szRecvArray[10]);
		thermostatDeviceStatus.setCodeStatus(szRecvArray[9]);
		thermostatDeviceStatus.setDefrostingStatus(szRecvArray[8]);

		tempControlStatus.setThermostatWarning(thermostatWarning);
		tempControlStatus.setThermostatDeviceStatus(thermostatDeviceStatus);

		return tempControlStatus;
	}

	protected void _setOpenTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException {

		byte[] szRequestData = new byte[8];
		szRequestData[0] = (byte) ThermostatID;
		szRequestData[1] = 0x06;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x00;
		szRequestData[4] = 0x00;
		szRequestData[5] = 0x00;
		szRequestData[6] = 0x00;
		szRequestData[7] = 0x00;
		setTbyte(szRequestData, temperature, 4, 5);
		getCRC(szRequestData, szRequestData.length - 2);

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[8];
		recv(recvBuff, recvBuff.length, szRequestData);

	}

	protected void _setCloseTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException {

		byte[] szRequestData = new byte[8];
		szRequestData[0] = (byte) ThermostatID;
		szRequestData[1] = 0x06;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x01;
		szRequestData[4] = 0x00;
		szRequestData[5] = 0x00;
		szRequestData[6] = 0x00;
		szRequestData[7] = 0x00;
		setTbyte(szRequestData, temperature, 4, 5);
		getCRC(szRequestData, szRequestData.length - 2);

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[8];
		recv(recvBuff, recvBuff.length, szRequestData);

	}

	protected int _getOpenTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		byte[] szRequestData = new byte[8];
		szRequestData[0] = (byte) ThermostatID;
		szRequestData[1] = 0x03;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x00;
		szRequestData[4] = 0x00;
		szRequestData[5] = 0x01;
		szRequestData[6] = 0x00;
		szRequestData[7] = 0x00;
		getCRC(szRequestData, szRequestData.length - 2);

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[5+szRequestData[5]*2];
		recv(recvBuff, recvBuff.length, szRequestData);

		return getTint(recvBuff, 3, 4);
	}

	protected int _getCloseTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		byte[] szRequestData = new byte[8];
		szRequestData[0] = (byte) ThermostatID;
		szRequestData[1] = 0x03;
		szRequestData[2] = 0x04;
		szRequestData[3] = 0x01;
		szRequestData[4] = 0x00;
		szRequestData[5] = 0x01;
		szRequestData[6] = 0x00;
		szRequestData[7] = 0x00;

		getCRC(szRequestData, szRequestData.length - 2);

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[5+szRequestData[5]*2];
		recv(recvBuff, recvBuff.length, szRequestData);

		return getTint(recvBuff, 3, 4);
	}

	protected int _getTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		return 0;
	}

	protected RelayStatus _getRelayStatus(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		byte[] szRequestData = new byte[8];
		szRequestData[0] = (byte) ThermostatID;
		szRequestData[1] = 0x03;
		szRequestData[2] = 0x08;
		szRequestData[3] = 0x00;
		szRequestData[4] = 0x00;
		szRequestData[5] = 0x01;
		szRequestData[6] = 0x00;
		szRequestData[7] = 0x00;
		getCRC(szRequestData, szRequestData.length - 2);

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] recvBuff = new byte[5+szRequestData[5]*2];
		recv(recvBuff, recvBuff.length, szRequestData);

		byte[] szRecvArray = getBinary(recvBuff, 3);
		RelayStatus relayStatus = new RelayStatus();
		relayStatus.setFanRelayStatus(szRecvArray[10]);
		relayStatus.setHeatRelayStatus(szRecvArray[9]);
		relayStatus.setCodeRelayStatus(szRecvArray[8]);

		return relayStatus;
	}

}
