/**   
 * Copyright © 2018.
 * 
 * @Package: com.hzdongcheng.drivers.utils
 * @author: Administrator   
 * @date: 2018年2月6日 下午1:44:00 
 */
package com.hzdongcheng.drivers.common;

/** 
 * @ClassName: Constants 
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年2月6日 下午1:44:00  
 */
public class Constants {
	/**
	 * 未知状态
	 */
	public static final byte UNKNOWN_STATUS = 9;
	/**
	 * 控制器状态：无物,门关,正常，市电，停止加热，开启
	 * 打印机状态：切刀锁关闭，纸充足，纸取走，有纸
	 */
	public static final byte ZERO_STATUS = 0; // 
	/**
	 * 控制器状态：有物，门开，报警，备电,正在加热，关闭
	 * 打印机状态：切刀锁打开，纸将近，纸未取走,缺纸
	 */
	public static final byte ONE_STATUS = 1;


	//设置Service为前台服务时的 NOTIFICATION ID
	public static final int NOTIFICATION_ID_PDSYSTEM   = 880;
	public static final int NOTIFICATION_ID_ELOCKER    = 881;
	public static final int NOTIFICATION_ID_SCANNER    = 882;
	public static final int NOTIFICATION_ID_CARDREADER = 883;
	public static final int NOTIFICATION_ID_PRINTER = 886;
	public static final int NOTIFICATION_ID_CARDSENDER = 887;
	public static final int NOTIFICATION_ID_CARDSENDER_SIM = 889;
	public static final int NOTIFICATION_ID_AMMETER    = 884;
	public static final int NOTIFICATION_ID_THERMOSTAT = 885;
	public static final int NOTIFICATION_ID_PERIPHERAL = 890;//外设服务

    //工控机供应商名称 
    public static final String IPC_VENDOR_WEISHENG = "WeiSheng";//威盛
    public static final String IPC_VENDOR_RUIXUN = "RuiXun";//瑞讯
	
}
