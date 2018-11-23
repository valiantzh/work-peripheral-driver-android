/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  AbstractELockerControlBase.java   
 * @Package com.hzdongcheng.drivers.peripheral.elocker.control
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年5月5日 上午10:02:25   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.control;

import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;

/** 
* @ClassName: AbstractELockerControlBase 
* @Description: TODO(ELocker控制抽象类) 
* @author Jxing 
* @date 2017年5月5日 上午10:02:25 
* @version 1.0 
*/
public abstract class AbstractELockerControlBase {

	protected SerialPortBase serialPort;
	
	//最大接收数据长度
	protected final int MAX_RECV_LEN = 100;
	//最大发送数据长度
	protected final int MAX_SEND_LEN = 20;
	//帧序号
	private byte frameNO = 0;
	
	protected AbstractELockerControlBase(SerialPortBase serialPort){
		this.serialPort = serialPort;
	}
	
	/**
	 * @throws ProtocolParsingException 
	 * @throws ResponseTimeoutException 
	 * @throws SerialErrorException 
	 * @throws NotOpenSerialException 
	 * 
	* @Title: recv 
	* @Description: TODO(接收驱动板返回的指令) 
	* @param @param buff
	* @param @param len
	* @param @throws SystemException 设定文件 
	* @return void 返回类型 
	* @throws
	 */
	protected int recv(byte[] buff, int len) throws NotOpenSerialException, 
													SerialPortErrorException, 
													RecvTimeoutException, 
													ProtocolParsingException {

		byte count = 0;
	    byte[] head  = new byte[1];
	    
	    do{
			serialPort.recvFixLength(head, 1);
			
	        if (head[0] == 0x02)
	            break;

	        if (++count == 100){
	        	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	        			"Recv data failed, because not found '0x02' protocol head.");
	        }
	    }while(true);
	    
	    //得到协议头0x02
	    buff[0] = head[0];
	    
	    //读取长度
	    byte[] totalLen = new byte[1];
	    serialPort.recvFixLength(totalLen, 1);
	    
	    buff[1] = totalLen[0];
	    
	    if (totalLen[0] < 8 || totalLen[0] > len)
	    	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	    			"Recv data failed, because the data length is " + totalLen[0] + ", must between 8 and " + len + ".");
	    
	    //未接收数据长度
	    final byte bodyLen = (byte) (totalLen[0] - 2);
	    byte[] szBody = new byte[bodyLen];
	    serialPort.recvFixLength(szBody, bodyLen);

	    //拷贝数据到buff
	    System.arraycopy(szBody, 0, buff, 2, bodyLen);
	    
	    //校验数据完整性
	    if (buff[totalLen[0] - 1] != 0x03 || buff[totalLen[0] - 2] != getBCC(buff, (byte) (totalLen[0]-2)) ){
	    	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	    			"Recv data failed, the tail is not '0x03' or crc failed.");
	    }

	    return 0;
	}
	
	/**
	 * 
	* @Title: getBCC 
	* @Description: TODO(计算校验码) 
	* @param @param array
	* @param @param len
	* @param @return 设定文件 
	* @return byte 返回类型 
	* @throws
	 */
	protected byte getBCC(final byte[] array, byte len){
		
	    byte bcc = 0;

	    for (byte i = 0; i < len; i++)
	    	bcc ^= array[i];
	    
	    return bcc;
	}
	
	/**
	 * 
	* @Method Name: getFrameNOAndIncrement 
	* @Description: TODO(得到数据帧序号) 
	* @param  @return
	* @return byte
	 */
	protected byte getFrameNOAndIncrement(){
		
		if (frameNO == 255){
			frameNO = 0;
		}
		return frameNO++;
	}
}
