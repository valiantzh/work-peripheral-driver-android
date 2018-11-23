package com.hzdongcheng.drivers.peripheral.cardreader.impl;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardreader.AbstractCardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;

import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;
import com.hzdongcheng.components.toolkits.utils.TypeConvertUtils;

/**
 * 
 * @ClassName: XZXCardReader 
 * @Description: 新中新读卡器控制类
 * @author Administrator 
 * @date 2018年1月27 下午3:54:28 
 * @version 1.0
 */
public class XZXCardReader extends AbstractCardReader {

	/* (non Javadoc) 
	* <p>Title: readCardInfo</p> 
	* <p>Description: </p> 
	* @return
	* @throws DriversException 
	* @see com.hzdongcheng.components.driver.cardreader.AbstractCardReader#readCardInfo() 
	*/
	@Override
	public CardInformation readCardInfo() throws DriversException {
		
		CardInformation cardInfo = new CardInformation();
		
		try {
			readRFIDCard(cardInfo);
		} catch (DcdzSystemException e){
			readIdentityCard(cardInfo);
		}
		
		return cardInfo;
	}

	public void readRFIDCard(CardInformation cardInfo) throws DriversException {
		
		//A5 A5 A5 96 69 00 05 01 01 00 00 05
		byte[] szRequestData = new byte[]{(byte)0xA5, (byte)0xA5, (byte)0xA5, (byte)0x96, 0x69, 0x00, 0x05, 0x01, 0x01, 0x00, 0x00, 0x05};

		//send
		//serialPortBase.sendFixLength(szRequestData, szRequestData.length);
		send(szRequestData);

		//�������
		//error return 12 bytes
		//success return 16 bytes
		//A5 A5 A5 96 69 00 05 01 02 00 80 86 (��11���ֽ�Ϊ80����ʧ��)
		//A5 A5 A5 96 69 00 09 01 02 00 90 66 4D 9E C2 ED (��11���ֽ�Ϊ90����ʧ��  ����Ϊ��66 4D 9E C2)
		byte[] szRecvArray = new byte[MAX_RECV_LEN];
		
		recv(szRecvArray, szRecvArray.length);

		if (TypeConvertUtils.byteToUnsigned(szRecvArray[6]) != 0x09 || TypeConvertUtils.byteToUnsigned(szRecvArray[10]) != 0x90){
			throw new DriversException(SerialPortErrorCode.ERR_PROTOCOLPARSING);
		}
		
		cardInfo.setCardID(StringUtils.byteArrayToHexString(szRecvArray, 11, 4).trim().toUpperCase());
	}
}
