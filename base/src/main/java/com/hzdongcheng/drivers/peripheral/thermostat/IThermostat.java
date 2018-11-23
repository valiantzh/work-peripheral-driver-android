package com.hzdongcheng.drivers.peripheral.thermostat;

import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.RelayStatus;
import com.hzdongcheng.drivers.peripheral.thermostat.model.ThermostatStatus.TempControlStatus;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

public interface IThermostat{

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(获取温控器状态) @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */

	TempControlStatus getThermostatStatus(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(设置开机温度) 27.5°传值275 @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */
	void setOpenTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(设置关机温度) @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */
	void setCloseTemperature(int ThermostatID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException, RecvTimeoutException,
			ResponseCodeException, ProtocolParsingException;

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(获取设置开机温度) @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */
	int getOpenTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(获取设置的关机温度) @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */
	int getCloseTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(获取当前温度) @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */
	int getTemperature(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
			RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/**
	 * 
	 * @Title: isNormallyMode @Description: TODO(获取继电器开关状态) @param @return
	 * 设定文件 @return boolean 返回类型 @throws
	 */

	RelayStatus getRelayStatus(int ThermostatID) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;
}
