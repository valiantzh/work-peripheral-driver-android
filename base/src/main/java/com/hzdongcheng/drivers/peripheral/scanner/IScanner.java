package com.hzdongcheng.drivers.peripheral.scanner;

import com.hzdongcheng.drivers.peripheral.scanner.event.IScannerListener;
import com.hzdongcheng.drivers.base.serialport.ISerialPortDevice;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;

/**
 * 
 * @ClassName: IScanner 
 * @Description: 扫描枪设备接口 
 * @author Administrator
 * @date 2018年1月26日 上午10:28:50 
 */
public interface IScanner extends ISerialPortDevice {
	/**
	 * 
	* @Title: isNormallyMode 
	* @Description: TODO(判断是否常量模式) 
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	boolean isNormallyMode();

	/**
	 * 
	* @Title: addListener 
	* @Description: TODO(添加事件监听) 
	* @param @param listener
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	boolean addListener(IScannerListener listener);
	
	/**
	 * 
	* @Title: removeListener 
	* @Description: TODO(移除事件监听) 
	* @param @param listener 设定文件 
	* @return void 返回类型 
	* @throws
	 */
	void removeListener(IScannerListener listener);

	/**
	 *
	 * @Title: removeListener
	 * @Description: TODO(移除所有事件监听)
	 * @return void 返回类型
	 * @throws
	 */
	void removeAllListener();

	/**
	 * 
	* @Title: searchVersion 
	* @Description: TODO(查询版本) 
	* @param @return 设定文件 
	* @return String 返回类型 
	* @throws
	 */
	String searchVersion() throws NotOpenSerialException, 
								  SerialPortErrorException, 
								  SendTimeoutException, 
								  RecvTimeoutException, 
								  ResponseCodeException, 
								  ProtocolParsingException;
	
	/**
	 * 
	* @Title: switchMode 
	* @Description: TODO(切换扫描枪模式，支持触发扫码) 
	* @param @return 设定文件 
	* @return int 返回类型 
	* @throws
	 */
	int switchMode(boolean normallyMode) throws NotOpenSerialException, 
											    SerialPortErrorException, 
											    SendTimeoutException, 
											    RecvTimeoutException, 
											    ResponseCodeException, 
											    ProtocolParsingException;

	/**
	 * 
	* @Method Name: oneDScanningOnOff 
	* @Description: TODO(设置是否支持扫描一维码) 
	* @param  @param onOff
	* @param  @return
	* @return int
	 */
	
    int oneDScanningOnOff(boolean onOff) throws NotOpenSerialException, 
											    SerialPortErrorException, 
											    SendTimeoutException, 
											    RecvTimeoutException, 
											    ResponseCodeException, 
											    ProtocolParsingException;
    
    /**
     * 
    * @Method Name: twoDScanningOnOff 
    * @Description: TODO(设置是否支持扫描二维码) 
    * @param  @param onOff
    * @param  @return
    * @return int
     */
    int twoDScanningOnOff(boolean onOff) throws NotOpenSerialException, 
											    SerialPortErrorException, 
											    SendTimeoutException, 
											    RecvTimeoutException, 
											    ResponseCodeException, 
											    ProtocolParsingException;
    
    /**
     * 
    * @Method Name: enableScanningOnOff 
    * @Description: TODO(禁用/启动一二维扫码功能) 
    * @param  @param onOff
    * @param  @return
    * @return int
     */
    int enableScanningOnOff(boolean onOff) throws NotOpenSerialException, 
												  SerialPortErrorException, 
												  SendTimeoutException, 
												  RecvTimeoutException, 
												  ResponseCodeException, 
												  ProtocolParsingException;
    
	/**
	 * 
	* @Title: startScan 
	* @Description: TODO(发送扫码指令) 
	* @param @return 设定文件 
	* @return int 返回类型 
	* @throws*-93.6+-*-
	 */
	int startScan() throws NotOpenSerialException, 
						   SerialPortErrorException, 
						   SendTimeoutException, 
						   RecvTimeoutException, 
						   ResponseCodeException, 
						   ProtocolParsingException;
	/**
	 * 
	* @Title: stopScan 
	* @Description: TODO(停止扫码指令) 
	* @param @return 设定文件 
	* @return int 返回类型 
	* @throws
	 */
	int stopScan() throws NotOpenSerialException, 
					      SerialPortErrorException, 
					      SendTimeoutException, 
					      RecvTimeoutException, 
					      ResponseCodeException, 
					      ProtocolParsingException;
}
