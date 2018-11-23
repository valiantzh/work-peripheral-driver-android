package com.hzdongcheng.drivers.peripheral.electricmeter.impl;


import com.hzdongcheng.drivers.peripheral.electricmeter.AbstractElectricmeter;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.TypeConvertUtils;

/** 
* @ClassName: HBElectricmeterImpl 
* @Description: TODO(华邦电表实现类) 
* @author Jxing 
* @date 2017年10月19日 上午9:35:21 
* @version 1.0 
*/
public class HBElectricmeterImpl extends AbstractElectricmeter {

	/* (non Javadoc) 
	* <p>Title: readTheMeter</p> 
	* <p>Description: </p> 
	* @return
	* @throws NotOpenSerialException
	* @throws SerialPortErrorException
	* @throws RecvTimeoutException
	* @throws SendTimeoutException
	* @throws ProtocolParsingException
	* @throws ResponseCodeException 
	* @see com.hzdongcheng.components.driver.electricmeter.AbstractElectricmeter#readTheMeter() 
	*/
	@Override
	public String readTheMeter() throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		//查询表号
		byte[] szRequestData = new byte[]{0x68, 
				(byte)0x99, (byte)0x99, (byte)0x99,(byte) 0x99, (byte)0x99,(byte)0x99,
				(byte) 0x68, 
				0x01, 
				0x02, 0x65, (byte)0xf3, 
				(byte)0xc1, 0x16};

		serialPortBase.sendFixLength(szRequestData, szRequestData.length);

		byte[] szRecvArray = new byte[30];
		recv(szRecvArray, szRecvArray.length);
		
		if (szRecvArray[9] == 0x01){
	    	throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE, "Recv data sucess, rep code error.");			
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//查询读数
		byte[] electricRequestData = { 0x68, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x68, 
				0x01, 
				0x02, 0x43, (byte)0xC3, 
				(byte)0x00, 0x16 };
		System.arraycopy(szRecvArray, 1, electricRequestData, 1, 6);
		electricRequestData[12] = getSum(electricRequestData, electricRequestData.length - 2);
		serialPortBase.sendFixLength(electricRequestData, electricRequestData.length);
		
		byte[] electricRecvArray = new byte[30];
		recv(electricRecvArray, electricRecvArray.length);
		
		if (szRecvArray[9] == 0x01){
	    	throw new ResponseCodeException(SerialPortErrorCode.ERR_RESPONSECODE, "Recv data sucess, rep code error.");			
		}
		
		return getReading(electricRecvArray);
	}
	
	private int recv(byte[] buffer, int len) throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException,
			 SendTimeoutException, ProtocolParsingException, ResponseCodeException {
		
		byte count = 0;
	    byte[] head  = new byte[1];
	    
	    do{
	    	serialPortBase.recvFixLength(head, 1);
			
	        if (head[0] == 0x68)
	            break;

	        if (++count == 100){
	        	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	        			"Recv data failed, because not found '0x68' protocol head.");
	        }
	    }while(true);
	    
	    //得到协议头0x02
	    buffer[0] = head[0];
	    
	    //读取固定8字节
	    byte[] fixBytes = new byte[8];
	    serialPortBase.recvFixLength(fixBytes, fixBytes.length);
	    
	    if (fixBytes[6] != 0x68){
        	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
        			"Recv data failed, because not found '0x68' protocol in fixed 8 bytes.");	    	
	    }
	    
	    System.arraycopy(fixBytes, 0, buffer, 1, fixBytes.length);
	    
	    //读取数据长度
	    byte[] totalLen = new byte[1];
	    serialPortBase.recvFixLength(totalLen, totalLen.length);
	    
	    buffer[9] = totalLen[0];
	    
	    if (totalLen[0] < 4 || totalLen[0] > len)
	    	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	    			"Recv data failed, because the data length is " + totalLen[0] + ", must between 8 and " + len + ".");
	    
	    //未接收数据长度
	    final byte bodyLen = (byte) (totalLen[0] + 2);
	    byte[] szBody = new byte[bodyLen];
	    serialPortBase.recvFixLength(szBody, bodyLen);

	    //拷贝数据到buff
	    System.arraycopy(szBody, 0, buffer, 10, bodyLen);
	    
	    //校验数据完整性
	    if (buffer[10 + bodyLen - 1] != 0x16 || buffer[10 + bodyLen - 2] != getSum(buffer, 10 + bodyLen - 2) ){
	    	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	    			"Recv data failed, the tail is not '0x16' or crc failed.");
	    }

	    return 0;
	}
	
	private byte getSum(byte[] buffer, int len) {
		
		int value = 0;
		
		for (int i = 0; i < len; i++){
			value += buffer[i];
		}
		
		return (byte)(value % 256);
	}
	
	private String getReading(byte[] buffer) throws ProtocolParsingException{
		
		if (buffer.length < 16){
	    	throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING, 
	    			"Recv data sucess, data length is less 16.");			
		}
		
		byte[] szRecvArray = new byte[4];
		System.arraycopy(buffer, 12, szRecvArray, 0, 4);
		
		StringBuffer stringBuffer = new StringBuffer();
		for (int i = szRecvArray.length - 1; i >=0; i--) {
			String tmpString = String.format("%02x", TypeConvertUtils.byteToUnsigned((byte) (szRecvArray[i] - 0x33)));
			stringBuffer.append(tmpString);
		}
		
		return stringBuffer.toString();
	}
	
}
