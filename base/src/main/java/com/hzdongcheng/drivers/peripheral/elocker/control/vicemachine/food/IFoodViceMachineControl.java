/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  IFoodViceMachineControl.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.vicemachine.food   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:05:24   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.food;

import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.IViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * @ClassName: IFoodViceMachineControl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jxing
 * @date 2017年4月12日 下午3:05:24
 * @version 1.0
 */
public interface IFoodViceMachineControl extends IViceMachineControl {
	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(设置温度)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setTemperatureRange(int boardID, int boxID, final int temperature)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(控制加热（全）)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setAllHeating(int boardID, int heatInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(加热状态查询)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	ViceMachineStatus getAllHeating(int boardID) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(单箱加热)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setBoxHeating(int boardID, int boxID, final int heatInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(单箱加热状态查询)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	int getBoxHeating(int boardID, int boxID) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(红灯控制单)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setRedLantern(int boardID, int boxID, final int LanternInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(红灯查询单)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	int getRedLantern(int boardID, int boxID) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(箱体同空)
	 * @param @return（0为全部箱门）
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setBoxAllControl(int boardID, int boxID, final BoxStatus boxStatus)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(获取所需状态)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	ViceMachineStatus getBoxAllControl(int boardID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(获取单箱所以状态)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	BoxStatus getOneBoxAllControl(int boardID, int boxID)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(LED照明灯控制单)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setLEDLantern(int boardID, int boxID, final int LEDInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(LED照明灯查询单)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	int getLEDLantern(int boardID, int boxID) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(箱内消毒控制)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	void setDisinfect(int boardID, int boxID, final int disinfectInfo)
			throws NotOpenSerialException, SerialPortErrorException,
			RecvTimeoutException, SendTimeoutException,
			ProtocolParsingException, ResponseCodeException;

	/**
	 * 
	 * @Method Name: getDeviceCode
	 * @Description: TODO(箱内消毒查询)
	 * @param @return
	 * @param @throws NotOpenSerialException
	 * @param @throws SerialPortErrorException
	 * @param @throws SendTimeoutException
	 * @return String
	 */
	int getDisinfect(int boardID, int boxID) throws NotOpenSerialException,
			SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException,
			ResponseCodeException;
}
