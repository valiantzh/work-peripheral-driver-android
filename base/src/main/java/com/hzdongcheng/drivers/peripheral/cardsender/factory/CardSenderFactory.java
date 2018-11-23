package com.hzdongcheng.drivers.peripheral.cardsender.factory;

import com.hzdongcheng.drivers.common.DriverConfig;
import com.hzdongcheng.drivers.peripheral.cardsender.ICardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.impl.ACTSimCardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.impl.TYCSCardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderConfig;

/**
 * 
 * @ClassName: CardSenderFactory 
 * @Description:发卡器控制工厂类
 * @author: Administrator
 * @date: 2018年2月28日 上午11:35:28
 */
public class CardSenderFactory {

	/**
	 * 
	 * @Title: createInstance  
	 * @Description: 创建发卡器实例
	 * @param @param config
	 * @param @return
	 * @return ICardSender 
	 * @throws
	 */
	public static ICardSender createInstance(DriverConfig config){
		ICardSender cardSender = null;
		switch(config.getVendorName()){
		case CardSenderConfig.CARDSENDER_VENDOR_ATC_SIM:
			cardSender = new ACTSimCardSender();
			break;
		case CardSenderConfig.CARDSENDER_VENDOR_TYCS:
			cardSender = new TYCSCardSender();
			break;
		}
		return cardSender;
	}
}
