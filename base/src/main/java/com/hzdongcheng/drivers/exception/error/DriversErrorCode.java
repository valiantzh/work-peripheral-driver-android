package com.hzdongcheng.drivers.exception.error;

import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;

public class DriversErrorCode extends ErrorCode{

	/**
	 * 外设驱动异常：11000~11999
	 */

	public static final int ERR_PD_DRIVERNOTEXISTS = 11001;//外设驱动程序不存在
	public static final int ERR_PD_DRIVERFUNNOTEXISTS = 11002;//外设驱动功能不存在
	public static final int ERR_PD_COMMUNICATION = 11003;//Communication anomaly
	public static final int ERR_PD_REPLAY_TIMEOUT = 11004;//response
	public static final int ERR_PD_RUNBUSINESSTIMEOUT = 11005;//功能执行超时

	public static final int ERR_PD_UNBINDSERVICE=  11011;//未绑定外设驱动服务
	public static final int ERR_PD_REMOTESERVICEERROR = 11012;//外设服务异常


	//通用外设异常  11101~11199
	public static final int ERR_PD_RFID_SUM_CHECK_FAILED = 11101; //RFID校验和验证失败
	public static final int ERR_PD_RFID_GET_ANTENNA_STATUS_FAILED = 11102; //RFID获得天线状态失败
	public static final int ERR_PD_RFID_GET_ANTENNA_PARAM_FAILED = 11103; //RFID获得天线参数失败


	//伺服异常 11201~11249
	public static final int ERR_PD_SERVO_LOST_ORIGIN = 11201;//伺服无原点
	public static final int ERR_PD_SERVO_NO_REFERENCEPOIINT = 11202;//系统无基准点
	public static final int ERR_PD_SERVO_EXISTSALARM = 11203;//伺服报警
	public static final int ERR_PD_SERVO_RESET_FAIL = 11204;//报警复位失败
	public static final int ERR_PD_SERVO_URGENTSTOPPING = 11205;//紧急停车中
	public static final int ERR_PD_SERVO_BACKING_ORIGIN = 11206;//伺服回原点中
	public static final int ERR_PD_SERVO_BACK_ORIGIN_TIMEOUT = 11207;//伺服回原点超时
	public static final int ERR_PD_SERVO_BACK_ORIGIN_FAIL = 11208;//伺服回原失败
	public static final int ERR_PD_SERVO_DISABLE = 11209;//伺服无使能
	public static final int ERR_PD_SERVO_LOCATING_TIMEOUT = 11210;//伺服标定超时 locating over limit
	public static final int ERR_PD_SERVO_RUN_OVER_LIMIT = 11211;//单侧圈数超限（请求回原点） over limit


	//PLC控制异常 11251~ 11299
	public static final int ERR_PD_PLC_SYSALARM = 11251;//PLC系统异常
	public static final int ERR_PD_PLC_CMDINVALID = 11252;//指令无效invalid
	public static final int ERR_PD_PLC_UPSPOWERSUPPLY =  11253;//UPS供电 power supply
	public static final int ERR_PD_PLC_SHOCKALARM = 11254;//震动报警shock
	public static final int ERR_PD_PLC_RASTERTHING = 11255;//光栅有物遮挡raster
	public static final int ERR_PD_PLC_CHECKSTORAGETIMEOUT  = 11256;//盘库超时
	public static final int ERR_PD_PLC_CHECKSTORAGEINTERRUPT =11257;//盘库被中断 interrupt alignment
	public static final int ERR_PD_PLC_NOALIGNBOX = 11258;//货格与存取口没有对正
	public static final int ERR_PD_PLC_FORBIDMOVE4RUNNING = 11261;//运动中，不允许新的移动
	public static final int ERR_PD_PLC_FORBIDMOVE4AUTODOOROPEN = 11262;//自动门开，不允许移动
	public static final int ERR_PD_PLC_FORBIDMOVE4MANUALDOOROPEN = 11263;//手动门开，不允许移动
	public static final int ERR_PD_PLC_FORBIDMOVE4CHECKING = 11264;//盘库中，不允许移动

	public static final int ERR_PD_PLC_FORBIDOPEN4RUNNING = 11271;//运动中，不允许开门
	public static final int ERR_PD_PLC_FORBIDCHECKSTORAGE4UPS = 11281;//UPS供电，不支持盘库

	//外设业务异常 11800 ~ 11999
	public final static int ERR_BUSINESS_FORBIDOPENBOX4THINE = 11801;//有物，不允许开箱 （投递）
	public final static int ERR_BUSINESS_FORBIDOPENBOX4NOTHINE = 11802;//无物，不允许开箱（取物）
	public final static int ERR_BUSINESS_OPENDOORFAIL = 11803;//打开箱门失败，请联系管理
	public final static int ERR_BUSINESS_FORBIDCLOSEDOOR4RESTERTHINE  = 11804;//光幕遮挡，请检查格口
	static {
		/*Peripheral driver error define*/
		ErrorTitle.putErrorTitle(ERR_PD_DRIVERNOTEXISTS, "Peripheral drive does not exist.");
		ErrorTitle.putErrorTitle(ERR_PD_DRIVERFUNNOTEXISTS, "The device does not support this function.");
		ErrorTitle.putErrorTitle(ERR_PD_COMMUNICATION, "The device communication anomaly.");
		ErrorTitle.putErrorTitle(ERR_PD_REPLAY_TIMEOUT, "The device replay timeout.");
		ErrorTitle.putErrorTitle(ERR_PD_RUNBUSINESSTIMEOUT, "功能执行超时.");

		ErrorTitle.putErrorTitle(ERR_PD_UNBINDSERVICE, "unbind service.");
		ErrorTitle.putErrorTitle(ERR_PD_REMOTESERVICEERROR, "Remote Exception.");

		ErrorTitle.putErrorTitle(ERR_PD_SERVO_LOST_ORIGIN, "Servo lost the origin.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_NO_REFERENCEPOIINT, "Servo no reference point.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_EXISTSALARM, "Servo alarm.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_RESET_FAIL, "Servo reset fail.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_URGENTSTOPPING, "Servo urgent stopping.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_BACKING_ORIGIN, "Servo backing the origin.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_BACK_ORIGIN_TIMEOUT, "Servo back the origin timeout.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_BACK_ORIGIN_FAIL, "Servo back the origin fail.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_DISABLE, "Servo disable.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_LOCATING_TIMEOUT, "Servo locating timeout.");
		ErrorTitle.putErrorTitle(ERR_PD_SERVO_RUN_OVER_LIMIT, "Servo run over limit.");

		ErrorTitle.putErrorTitle(ERR_PD_PLC_SYSALARM, "PLC系统异常.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_CMDINVALID, "PLC指令无效.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_UPSPOWERSUPPLY, "UPS供电中.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_SHOCKALARM, "震动报警.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_RASTERTHING, "光栅有物遮挡，不允许移动.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_CHECKSTORAGETIMEOUT, "盘库超时.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_CHECKSTORAGEINTERRUPT, "盘库被中断.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_NOALIGNBOX, "货格与存取口没有对正.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_FORBIDMOVE4RUNNING, "运动中，不允许新的移动.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_FORBIDMOVE4AUTODOOROPEN, "自动门开，不允许移动.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_FORBIDMOVE4MANUALDOOROPEN, "手动门开，不允许移动.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_FORBIDMOVE4CHECKING, "盘库中，不允许移动.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_FORBIDOPEN4RUNNING, "运动中，不允许开门.");
		ErrorTitle.putErrorTitle(ERR_PD_PLC_FORBIDCHECKSTORAGE4UPS, "UPS供电，不支持盘库.");

		ErrorTitle.putErrorTitle(ERR_PD_RFID_SUM_CHECK_FAILED,"RFID校验和验证失败");
		ErrorTitle.putErrorTitle(ERR_PD_RFID_GET_ANTENNA_STATUS_FAILED,"RFID获得天线状态失败");
		ErrorTitle.putErrorTitle(ERR_PD_RFID_GET_ANTENNA_PARAM_FAILED,"RFID获得天线参数失败");
	}

}
