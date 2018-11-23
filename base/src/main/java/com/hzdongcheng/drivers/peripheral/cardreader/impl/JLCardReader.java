package com.hzdongcheng.drivers.peripheral.cardreader.impl;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardreader.AbstractCardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;

/**
 * 
 * @ClassName: JLCardReader 
 * @Description: 精伦读卡器控制类
 * @author: Administrator
 * @date: 2018年1月31日 上午8:18:26
 * @version 1.0
 */
public class JLCardReader extends AbstractCardReader{

	/* (non-Javadoc)
	 * @see com.hzdongcheng.drivers.peripheral.cardreader.AbstractCardReader#readCardInfo()
	 */
	@Override
	public CardInformation readCardInfo() throws DriversException {
		// TODO Auto-generated method stub
		CardInformation cardInfo = new CardInformation();
		
		readIdentityCard(cardInfo);
		
		return cardInfo;
	}
	
}
