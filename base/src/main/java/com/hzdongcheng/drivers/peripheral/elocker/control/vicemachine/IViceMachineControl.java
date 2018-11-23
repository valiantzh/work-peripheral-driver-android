/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  IGeneralViceMachineControl.java   
 * @Package com.hzdongcheng.components.driver.elocker   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午2:41:26   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine;

import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/** 
* @ClassName: IGeneralViceMachineControl 
* @Description: TODO(通用副机控制接口定义) 
* @author Jxing 
* @date 2017年4月12日 下午2:41:26 
* @version 1.0
*/
public interface IViceMachineControl {
	/**
	 * 
	* @Method Name: getDeviceCode 
	* @Description: TODO(获取资产编码)
	* @param  @param boardID
	* @param  @return
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws SendTimeoutException
	* @return String
	 */
	String getDeviceCode(byte boardID) throws NotOpenSerialException, 
											  SerialPortErrorException, 
											  RecvTimeoutException,
										      SendTimeoutException,
										      ProtocolParsingException,
										      ResponseCodeException;										      ;
	
	/**
	 * 
	* @Method Name: setDeviceCode 
	* @Description: TODO(设置资产编码) 
	* @param  @param boardID
	* @param  @param code
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws SendTimeoutException
	* @return void
	 */
	void setDeviceCode(byte boardID, String code) throws NotOpenSerialException, 
														 SerialPortErrorException, 
													     RecvTimeoutException,
														 SendTimeoutException,
														 ProtocolParsingException,
														 ResponseCodeException;	
	
	/**
	 * 
	* @Method Name: openBox 
	* @Description: TODO(开箱) 
	* @param  @param boardID
	* @param  @param boxID
	* @param  @return
	* @param  @throws NotOpenSerialException
	* @param  @throws SerialPortErrorException
	* @param  @throws SendTimeoutException
	* @return BoxStatus
	 */
	BoxStatus openBox(byte boardID, byte boxID) throws NotOpenSerialException, 
													   SerialPortErrorException, 
													   RecvTimeoutException,
													   SendTimeoutException,
													   ProtocolParsingException,
													   ResponseCodeException;
	
	/**
	 * 
	* @Method Name: queryStatus 
	* @Description: TODO(查询副机状态) 
	* @param  @param boardID
	* @param  @return
	* @return ViceMachineStatus
	 */
	ViceMachineStatus queryStatus(byte boardID) throws NotOpenSerialException, 
													   SerialPortErrorException, 
													   RecvTimeoutException,
													   SendTimeoutException,
													   ProtocolParsingException,
													   ResponseCodeException;
	
	/**
	 * 
	* @Method Name: queryStatus 
	* @Description: TODO(查询指定箱门状态) 
	* @param  @param boardID
	* @param  @param boxID
	* @param  @return
	* @return BoxStatus
	 */
	BoxStatus queryStatus(byte boardID, byte boxID) throws NotOpenSerialException, 
														   SerialPortErrorException, 
														   RecvTimeoutException,
														   SendTimeoutException,
														   ProtocolParsingException,
														   ResponseCodeException;
}
