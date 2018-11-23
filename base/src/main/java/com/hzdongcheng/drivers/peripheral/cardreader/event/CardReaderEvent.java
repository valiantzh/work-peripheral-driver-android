package com.hzdongcheng.drivers.peripheral.cardreader.event;

import java.util.EventObject;

import com.hzdongcheng.drivers.peripheral.cardreader.model.CardInformation;

/**
 * 
 * @ClassName: CardReaderEvent 
 * @Description: 读卡器事件类 
 * @author Administrator 
 * @date 2018年1月27 上午8:35:03 
 * @version 1.0
 */
public class CardReaderEvent extends EventObject{
	
	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/
	private static final long serialVersionUID = -1414099574815436289L;
	private int errorCode = 0;
	private CardInformation cardInformation = null;
	
	public CardReaderEvent(Object source, int errorCode, CardInformation cardInformation) {
		super(source);
		// TODO Auto-generated constructor stub
		setErrorCode(errorCode);
		setCardInformation(cardInformation);
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return the cardInformation
	 */
	public CardInformation getCardInformation() {
		return cardInformation;
	}

	/**
	 * @param cardInformation the cardInformation to set
	 */
	public void setCardInformation(CardInformation cardInformation) {
		this.cardInformation = cardInformation;
	}
}
