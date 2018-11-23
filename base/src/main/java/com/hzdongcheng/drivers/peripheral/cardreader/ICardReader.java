package com.hzdongcheng.drivers.peripheral.cardreader;


import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;
import com.hzdongcheng.drivers.base.serialport.ISerialPortDevice;


/** 
 * @ClassName: ICardReader 
 * @Description: 读卡器控制接口
 * @author Administrator 
 * @date 2018年1月27 下午4:47:34 
 * @version 1.0 
 */
public interface ICardReader extends ISerialPortDevice {
	
	/**
	 * 
	* @Method Name: readCardInfo 
	* @Description: 读卡信息
	* @return CardInformation
	* @throws DriversException
	 */
	CardInformation readCardInfo() throws DriversException;
	
	/**
	 * 
	* @Method Name: setBuzzerOnOff 
	* @Description: 设置读卡器蜂鸣器开关
	* @param  @param onOff
	* @return void
	* @throws DriversException
	 */
	void setBuzzerOnOff(boolean onOff) throws DriversException;
	
	/**
	 * 
	* @Method Name: readData 
	* @Description: 读取指定扇区数据
	* @param  @param section
	* @param  @param block
	* @return byte[]
	* @throws DriversException
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
	* @throws DriversException
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
	* @throws DriversException
	 */
	void modifyPassword(byte section, byte[] keyA, byte[] keyB)	throws DriversException;
}
