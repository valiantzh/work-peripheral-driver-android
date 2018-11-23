package com.hzdongcheng.drivers.peripheral.cardreader.event;

import java.util.EventListener;

/**
 * 
 * @ClassName: ICardReaderListener 
 * @Description: 读卡器事件监听接口
 * @author Administrator 
 * @date 2018年1月27 上午8:34:46 
 * @version 1.0
 */
public interface ICardReaderListener extends EventListener{
	public void onNotice(CardReaderEvent obj);
}
