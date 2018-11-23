/**   
 * Copyright © 2018.
 * 
 * @Package: com.hzdongcheng.drivers.peripheral.cardsender
 * @author: Administrator   
 * @date: 2018年2月7日 下午5:27:29 
 */
package com.hzdongcheng.drivers.peripheral.cardsender;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderStatus;
import com.hzdongcheng.drivers.base.serialport.ISerialPortDevice;

/** 
 * @ClassName: ICardSender 
 * @Description: 发卡器接口类
 * @author: Administrator
 * @date: 2018年2月7日 下午5:27:29  
 */
public interface ICardSender  extends ISerialPortDevice{
	/**
	 * 
	* @Title: resetCardSender  
	* @Description: 复位发卡器 
	* @param @throws DriversException
	* @return void 
	* @throws
	 */
	String resetCardSender()throws DriversException;
	
	/**
	 * 
	* @Title: queryStatus  
	* @Description: 查询发卡机状态 
	* @param @return
	* @param @throws DriversException
	* @return CardSenderStatus 
	* @throws
	 */
	CardSenderStatus queryStatus() throws DriversException;
	
	/**
	 * 
	* @Title: moveCard2ReadPosition  
	* @Description: 发送卡片到读卡位  
	* @param isRF 是否为RF卡
	* @return String 
	* @throws DriversException
	 */
	String moveCard2ReadPosition(boolean isRF) throws DriversException;
	//
	/**
	 * 
	* @Title: moveCard2Front  
	* @Description: 发送卡片到卡嘴
	* @return String 
	* @throws DriversException
	 */
	String moveCard2Front() throws DriversException;
	/**
	 * 
	 * @Title: recycleCard  
	 * @Description: 回收卡
	 * @return String 
	 * @throws DriversException
	 */
	String recycleCard() throws DriversException;
	
	/**
	 * 
	* @Method Name: readData 
	* @Description: TODO(读取指定扇区数据) 
	* @param  @param section
	* @param  @param block
	* @param  @return
	* @param  @throws DriversException
	* @return byte[]
	 */
	byte[] readData(byte section, byte block) throws DriversException;
	
	/**
	 * 
	* @Method Name: writeData 
	* @Description: 往指定块写入数据
	* @param  @param section
	* @param  @param block
	* @param  @param data
	* @return void
	 */
	void writeData(byte section, byte block, byte[] data) throws DriversException;
	
	/**
	 * 
	* @Method Name: modifyPassword 
	* @Description: 修改扇区密码
	* @param  @param section
	* @param  @param keyA
	* @param  @param keyB
	* @return void
	 */
	void modifyPassword(byte section, byte[] keyA, byte[] keyB)	throws DriversException;
	/**
	 * 
	* @Method Name: setBuzzerOnOff 
	* @Description: 设置蜂鸣器开关
	* @param  @param onOff
	* @param  @throws DriversException
	* @return void
	 */
	void setBuzzerOnOff(boolean onOff) throws DriversException;
}
