package com.hzdongcheng.drivers.peripheral.cardreader.factory;


import com.hzdongcheng.drivers.peripheral.cardreader.ICardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.impl.JLCardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.impl.StdCardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.impl.XZXCardReader;
import com.hzdongcheng.drivers.peripheral.cardreader.model.CardReaderConfig;

/** 
 * @ClassName: CardReaderFactory 
 * @Description: 读卡器控制工厂类
 * @author Administrator 
 * @date 2018年1月27 上午8:36:40 
 * @version 1.0 
 */
public class CardReaderFactory {
	
	/**
	 * 
	* @Method Name: createJLCardReader 
	* @Description: TODO(创建精伦读卡器实例) 
	* @param  @return
	* @return ICardReader
	 */
	public static ICardReader createJLCardReader(){
		return new JLCardReader();
	}
	
	/**
	 * 
	* @Method Name: createStdCardReader 
	* @Description: TODO(创建东城标准读卡器实例) 
	* @param  @return
	* @return ICardReader
	 */
	public static ICardReader createStdCardReader(){
		return new StdCardReader();
	}
	
	/**
	 * 
	* @Method Name: createXZXCardReader 
	* @Description: TODO(创建新中新读卡器实例) 
	* @param  @return
	* @return ICardReader
	 */
	public static ICardReader createXZXCardReader(){
		return new XZXCardReader();
	}
	
	/**
	 * 
	* @Method Name: createInstance 
	* @Description: TODO(通过反射创建读卡器实例) 
	* @param  @param classSimpleName
	* @param  @return
	* @return ICardReader
	* @deprecated
	 */
	public static ICardReader createInstance(String classSimpleName){
		
		String className = "com.dcdzsoft.drivers.cardreader.impl." + classSimpleName;

		ICardReader cardReader = null;
		try {
			cardReader = (ICardReader)Class.forName(className).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		return cardReader;
	}
	public static ICardReader createInstance(CardReaderConfig config){
		ICardReader cardReader = null;
		switch(config.getVendorName()){
		case CardReaderConfig.CARDREADER_VENDOR_DCDZ:
			cardReader = new StdCardReader();
			break;
		case CardReaderConfig.CARDREADER_VENDOR_JL:
			cardReader = new JLCardReader();
			break;
		case CardReaderConfig.CARDREADER_VENDOR_XZX:
			cardReader = new XZXCardReader();
			break;
				
		}
		return cardReader;
	}
}
