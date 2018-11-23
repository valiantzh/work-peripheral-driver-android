/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  IMainControl.java   
 * @Package com.hzdongcheng.components.driver.elocker   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午2:36:54   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine;


import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.MainMachineStatus;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/** 
* @ClassName: IMainControl 
* @Description: TODO(主控部分接口定义) 
* @author Jxing 
* @date 2017年4月12日 下午2:36:54 
* @version 1.0 
*/
public interface IMainMachineControl {
	/**
	 * 
	* @Method Name: getDeviceCode 
	* @Description: TODO(获取资产编码)
	* @param  @return
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws SendTimeoutException
	* @return String
	 */
	String getDeviceCode() throws NotOpenSerialException, 
								  SerialPortErrorException, 
								  RecvTimeoutException,
								  SendTimeoutException,
								  ProtocolParsingException,
								  ResponseCodeException;										      ;
	
	/**
	 * 
	* @Method Name: setDeviceCode 
	* @Description: TODO(设置资产编码) 
	* @param  @param code
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws SendTimeoutException
	* @return void
	 */
	void setDeviceCode(String code) throws NotOpenSerialException, 
										   SerialPortErrorException, 
										   RecvTimeoutException,
										   SendTimeoutException,
										   ProtocolParsingException,
										   ResponseCodeException;	
	
	/**
	 * 
	 * @Method Name: queryMainStatus
	 * @Description: TODO(获取主机状态)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	MainMachineStatus queryMainStatus() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: setTemperatureRange
	 * @Description: TODO(设置温度 min 5~20 max 30~50)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setTemperatureRange(final int min, final int max)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getTemperatureRange
	 * @Description: TODO(获取设置温度)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	String getTemperatureRange() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: getCurTemperature
	 * @Description: TODO(获取当前温度 int& SI7005, int& DS18B20)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	String getCurTemperature() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: weakCurrentManage
	 * @Description: TODO(弱电控制)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void weakCurrentManage(int channelNO[], int val[])
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: strongCurrentManage
	 * @Description: TODO(强电控制)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void strongCurrentManage(int channelNO[], int val[])
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(打开投递口)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void openDeliveryBox() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: closeDeliveryBox
	 * @Description: TODO(关闭投递口)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void closeDeliveryBox() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: queryDeliveryBoxStatus
	 * @Description: TODO(投递箱状态)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return Strin
	 */
	BoxStatus queryDeliveryBoxStatus() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: openPickUpbox
	 * @Description: TODO(打开收件箱)
	 * @param @param boardID
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return Strin
	 */

	BoxStatus openPickUpbox() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;
	
	/**
	 * 
	* @Method Name: enableKeepAlive 
	* @Description: TODO(启用心跳功能) 
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws RecvTimeoutException
	* @param  @throws SendTimeoutException
	* @param  @throws ProtocolParsingException
	* @param  @throws ResponseCodeException
	* @return void
	 */
	void enableKeepAlive() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;
	
	/**
	 * 
	* @Method Name: disenableKeepAlive 
	* @Description: TODO(禁用心跳功能) 
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws RecvTimeoutException
	* @param  @throws SendTimeoutException
	* @param  @throws ProtocolParsingException
	* @param  @throws ResponseCodeException
	* @return void
	 */
	void disenableKeepAlive() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;
	
	/**
	 * 
	* @Method Name: keepAlive 
	* @Description: TODO(发送心跳) 
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws RecvTimeoutException
	* @param  @throws SendTimeoutException
	* @param  @throws ProtocolParsingException
	* @param  @throws ResponseCodeException
	* @return void
	 */
	void keepAlive() throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;
	
	/**
	 * 
	* @Method Name: restartPower 
	* @Description: TODO(重启电源) 
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws RecvTimeoutException
	* @param  @throws SendTimeoutException
	* @param  @throws ProtocolParsingException
	* @param  @throws ResponseCodeException
	* @return void
	 */
	void restartPower(int delayMills) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;
}
