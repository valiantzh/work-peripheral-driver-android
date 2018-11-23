package com.hzdongcheng.drivers.peripheral.cardreader;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;
import com.hzdongcheng.components.toolkits.utils.TypeConvertUtils;

/**
 * @ClassName: AbstractCardReader
 * @Description: 读卡器抽象类
 * @author Administrator
 * @date 2018年1月27 下午5:04:50
 * @version 1.0
 */
public abstract class AbstractCardReader implements ICardReader {

	protected final SerialPortBase serialPortBase = new SerialPortBase();
	protected final ReadWriteLock _lock = new ReentrantReadWriteLock();

	protected final int MAX_RECV_LEN = 4 * 1024;
	protected final int MAX_SEND_LEN = 100;

	/*
	 * (non Javadoc) <p>Title: open</p> <p>Description: </p>
	 * 
	 * @param portName
	 * 
	 * @throws NotFoundSerialLibraryException
	 * 
	 * @throws InitializeSerialPortException
	 * 
	 * @throws SerialPortErrorException
	 * 
	 * @see
	 * com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#open(java.lang.
	 * String)
	 */
	@Override
	public void open(String portName)
			throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
		serialPortBase.open(portName);

		try {
			serialPortBase.initialize(SerialPortBase.DCDZ_CBR_115200, SerialPortBase.DCDZ_NOPARITY,
					SerialPortBase.DCDZ_ONESTOPBIT, SerialPortBase.DCDZ_DATABITS8);
			serialPortBase.setInOutQueue(MAX_RECV_LEN, MAX_SEND_LEN);
			serialPortBase.setReadWriteTimeouts(2000, 1000);
		} catch (SerialPortException e) {
			throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
		}
	}

	/*
	 * (non Javadoc) <p>Title: close</p> <p>Description: </p>
	 * 
	 * @see com.hzdongcheng.drivers.base.serialport.ISerialPortDevice#close()
	 */
	@Override
	public void close() {
		serialPortBase.close();
	}



	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.cardreader.ICardReader#readCardInfo()
	 */
	@Override
	public CardInformation readCardInfo() throws DriversException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.cardreader.ICardReader#setBuzzerOnOff(boolean)
	 */
	@Override
	public void setBuzzerOnOff(boolean onOff) throws DriversException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.cardreader.ICardReader#readData(byte, byte)
	 */
	@Override
	public byte[] readData(byte section, byte block) throws DriversException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.cardreader.ICardReader#writeData(byte, byte, byte[])
	 */
	@Override
	public void writeData(byte section, byte block, byte[] data) throws DriversException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.cardreader.ICardReader#modifyPassword(byte, byte[], byte[])
	 */
	@Override
	public void modifyPassword(byte section, byte[] keyA, byte[] keyB) throws DriversException {
		// TODO Auto-generated method stub
		
	}

	protected void readIdentityCard(CardInformation cardInfo) throws DriversException {
		{ // step 1.寻找证/卡
			for (int i = 0; i < 2; i++) {
				// AA AA AA 96 69 00 03 20 01 22
				byte[] szRequestData = new byte[] { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00,
						0x03, 0x20, 0x01, 0x22 };

				//serialPortBase.sendFixLength(szRequestData, szRequestData.length);
				send(szRequestData);

				// error return 11 bytes
				// success return 15 bytes
				// AA AA AA 96 69 00 04 00 00 80 84 (��10���ֽ�Ϊ80����Ѱ�ҿ�ʧ��)
				// AA AA AA 96 69 00 08 00 00 9F 00 00 00 00
				// 97(��10���ֽ�Ϊ9F����Ѱ�ҿ��ɹ�����ִ�������2������)

				byte[] szRecvArray = new byte[MAX_RECV_LEN];

				recv(szRecvArray, szRecvArray.length);

				if (TypeConvertUtils.byteToUnsigned(szRecvArray[6]) != 0x08
						|| TypeConvertUtils.byteToUnsigned(szRecvArray[9]) != 0x9F) {
					if (1 == i) {
						throw new DriversException(SerialPortErrorCode.ERR_PROTOCOLPARSING);
					}
				} else {
					break;
				}
			} // end for
		}

		{ // step 2. 选取证/卡
			// AA AA AA 96 69 00 03 20 02 21
			byte[] szRequestData = new byte[] { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 
					0x03,0x20, 0x02, 0x21 };

			// send
			//serialPortBase.sendFixLength(szRequestData, szRequestData.length);
			send(szRequestData);

			// error return 11 bytes
			// success return 15 bytes
			// AA AA AA 96 69 00 04 00 00 80 84 (��10���ֽ�Ϊ80����Ѱ�ҿ�ʧ��)
			// AA AA AA 96 69 00 08 00 00 9F 00 00 00 00
			// 97(��10���ֽ�Ϊ9F����Ѱ�ҿ��ɹ�����ִ�������2������)

			byte[] szRecvArray = new byte[MAX_RECV_LEN];
			recv(szRecvArray, szRecvArray.length);
		}

		{ // step 3. 读固定信息
			// AA AA AA 96 69 00 03 30 01 32
			byte[] szRequestData = new byte[] { (byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 
					0x03,0x30, 0x01, 0x32 };

			// send
			send(szRequestData);

			// �������
			// error return 11 bytes
			// success return 15 bytes
			// AA AA AA 96 69 00 04 00 00 81 85 (��10���ֽ�Ϊ81����ѡȡ��ʧ��)
			// AA AA AA 96 69 00 0C 00 00 90 00 00 00 00 00 00 00 00 9C
			// (��10���ֽ�Ϊ90����ѡȡ���ɹ�����ִ�������3������)
			byte[] szRecvArray = new byte[MAX_RECV_LEN];

			recv(szRecvArray, szRecvArray.length);

			if (TypeConvertUtils.byteToUnsigned(szRecvArray[5]) * 256
					+ TypeConvertUtils.byteToUnsigned(szRecvArray[6]) != 1288
					|| TypeConvertUtils.byteToUnsigned(szRecvArray[9]) != 0x90)
				throw new DriversException(SerialPortErrorCode.ERR_PROTOCOLPARSING);

			cardInfo.setName(StringUtils.byteArray16LEToString(szRecvArray, 14, 30).trim());
			cardInfo.setSex(StringUtils.byteArray16LEToString(szRecvArray, 44, 2).trim());
			cardInfo.setNation(StringUtils.byteArray16LEToString(szRecvArray, 46, 4).trim());
			cardInfo.setBorn(StringUtils.byteArray16LEToString(szRecvArray, 50, 16).trim());
			cardInfo.setAddress(StringUtils.byteArray16LEToString(szRecvArray, 66, 70).trim());
			cardInfo.setCardID(StringUtils.byteArray16LEToString(szRecvArray, 136, 36).trim().toUpperCase());
			cardInfo.setGrantDept(StringUtils.byteArray16LEToString(szRecvArray, 172, 30).trim());
			cardInfo.setUserLifeBegin(StringUtils.byteArray16LEToString(szRecvArray, 202, 16).trim());
			cardInfo.setUserLifeEnd(StringUtils.byteArray16LEToString(szRecvArray, 218, 16).trim());
			// String sReserved = StringUtils.byteArrayToString(szRecvArray, 14,
			// 30);
		}
	}
	
	/**
	 * 
	 * @Method Name: send  
	 * @Description: 发送串口数据  
	 * @param @param request
	 * @return void 
	 * @throws DriversException
	 */
	protected  void send(byte[] request) throws  DriversException{
		try {
			serialPortBase.sendFixLength(request, request.length);
		} catch (NotOpenSerialException | SerialPortErrorException| SendTimeoutException e) {
			throw new DriversException(e.getErrorCode());
		} 
	}
	/**
	 * 
	* @Method Name: recv 
	* @Description: 接收串口返回数据 
	* @param  @param pszBuff
	* @param  @param len
	* @return void
	 * @throws DriversException 
	 */
	protected void recv(byte[] pszBuff, int len) throws DriversException {
		//协议头7个字节
		int dwHeadBytes = 7;

		try {
			serialPortBase.recvFixLength(pszBuff, dwHeadBytes);
			
			//数据总长度
			int uTotalBytes = TypeConvertUtils.byteToInt(pszBuff[5])*256 + TypeConvertUtils.byteToInt(pszBuff[6]);
			byte[] bodyArray = new byte[uTotalBytes];
			serialPortBase.recvFixLength(bodyArray, uTotalBytes);
			
			System.arraycopy(bodyArray, 0 , pszBuff, dwHeadBytes, uTotalBytes);	
			
			//校验数据有效性
			int bcc1 = TypeConvertUtils.byteToInt(pszBuff[dwHeadBytes + uTotalBytes - 1]);
			int bcc2 = TypeConvertUtils.byteToInt(getBCC(pszBuff, uTotalBytes + 2 - 1));
			if (bcc1 != bcc2)
				throw new ProtocolParsingException(SerialPortErrorCode.ERR_PROTOCOLPARSING);
		} catch (NotOpenSerialException | SerialPortErrorException | RecvTimeoutException | ProtocolParsingException e) {
			throw new DriversException(e.getErrorCode());
		}
		
	}

	/**
	 * 
	 * @Title: getBCC 
	 * @Description: 计算校验码 
	 * @param @param array
	 * @param @param len
	 * @param @return 设定文件 
	 * @return byte 返回类型 
	 * @throws
	 */
	protected byte getBCC(final byte[] array, int len){
		
	    byte bcc = 0;

	    for (int i = 0; i < len; i++)
	    	bcc ^= array[5 + i];
	
	    return bcc;
	}
}
