package com.hzdongcheng.drivers.peripheral.cardsender.model;

import com.hzdongcheng.drivers.common.DriverConfig;

/**
 * 
 * @ClassName: CardSenderConfig 
 * @Description: 发卡器
 * @author: Administrator
 * @date: 2018年2月8日 下午2:54:24
 */
public class CardSenderConfig extends DriverConfig{
	public static final String CONFIG_NAME = "CardSender";
	public static final String CONFIG_NAME_SIM = "SimCardSender";
	//发卡器厂家
	public static final String CARDSENDER_VENDOR_ATC_SIM = "ATC_SIM";
	public static final String CARDSENDER_VENDOR_TYCS = "TYCS";
		
	public final static String CARD_TYPE_IC = "IC";
	public final static String CARD_TYPE_CPU = "CPU";
	public final static String CARD_TYPE_RF = "RF";
	private int readAndWrite;//0-不支持读写 1-只读 2-读&写
	private String cardType;//支持卡片类型
	/**
	 * @return the readAndWrite 0-不支持读写 1-只读 2-读&写
	 */
	public int getReadAndWrite() {
		return readAndWrite;
	}
	/**
	 * @param readAndWrite 0-不支持读写 1-只读 2-读&写
	 */
	public void setReadAndWrite(int readAndWrite) {
		this.readAndWrite = readAndWrite;
	}
	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
}
