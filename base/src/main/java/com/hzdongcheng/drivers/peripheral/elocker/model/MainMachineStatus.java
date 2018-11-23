/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  MainMachine.java   
 * @Package com.hzdongcheng.components.driver.elocker.model   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:14:16   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.model;

/**
 * @ClassName: MainMachine
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jxing
 * @date 2017年4月12日 下午3:14:16
 * @version 1.0
 */
public class MainMachineStatus {

	public static final byte UNKNOWN_STATUS = 9; // 未知状态
	public static final byte ZERO_STATUS = 0; // 无物,门关,正常，市电，停止加热，开启
	public static final byte ONE_STATUS = 1; // 有物，门开，报警，备电,正在加热，关闭

	public MainMachineStatus() {
		setManagementSwitch(UNKNOWN_STATUS);
		setShockStatus(UNKNOWN_STATUS);
		setTamperStatus(UNKNOWN_STATUS);
		setPowerStatus(UNKNOWN_STATUS);
		setWorkingTemperature(0);
		setWorkingHumidity(0);
		setWorkingTemperatureMax(0);
		setWorkingTemperatureMin(0);
		setAnearStatus(0);
	}

	/**
	 * 管理开关 0:关 1:开 9:未知
	 */
	private int managementSwitch;
	/**
	 * 振动状态 0:正常 1:报警 9:未知
	 */
	private int ShockStatus;
	/**
	 * 防撬状态 0:正常 1:报警 9:未知
	 */
	private int TamperStatus;
	/**
	 * 电源状态 0:市电 1:备电 9:未知
	 */
	private int powerStatus;
	/**
	 * 当前温度
	 */
	private float workingTemperature;
	/**
	 * 当前湿度
	 */
	private float workingHumidity;
	/**
	 * 温度上限
	 */
	private float workingTemperatureMax;
	/**
	 * 温度下限
	 */
	private float workingTemperatureMin;

	/**
	 * 人体靠近状态：0 正常 1 有人靠近
	 */
	private int anearStatus;

	/**
	 * @return the managementSwitch
	 */

	public int getManagementSwitch() {
		return managementSwitch;
	}

	/**
	 * @param managementSwitch
	 *            the managementSwitch to set
	 */
	public void setManagementSwitch(int managementSwitch) {
		this.managementSwitch = managementSwitch;
	}

	/**
	 * @return the powerStatus
	 */
	public int getPowerStatus() {
		return powerStatus;
	}

	/**
	 * @param powerStatus
	 *            the powerStatus to set
	 */
	public void setPowerStatus(int powerStatus) {
		this.powerStatus = powerStatus;
	}

	/**
	 * @return the workingTemperature
	 */
	public float getWorkingTemperature() {
		return workingTemperature;
	}

	/**
	 * @param workingTemperature
	 *            the workingTemperature to set
	 */
	public void setWorkingTemperature(float workingTemperature) {
		this.workingTemperature = workingTemperature;
	}

	/**
	 * @return the workingHumidity
	 */
	public float getWorkingHumidity() {
		return workingHumidity;
	}

	/**
	 * @param workingHumidity
	 *            the workingHumidity to set
	 */
	public void setWorkingHumidity(float workingHumidity) {
		this.workingHumidity = workingHumidity;
	}

	/**
	 * @return the workingTemperatureMax
	 */
	public float getWorkingTemperatureMax() {
		return workingTemperatureMax;
	}

	/**
	 * @param workingTemperatureMax
	 *            the workingTemperatureMax to set
	 */
	public void setWorkingTemperatureMax(float workingTemperatureMax) {
		this.workingTemperatureMax = workingTemperatureMax;
	}

	/**
	 * @return the workingTemperatureMin
	 */
	public float getWorkingTemperatureMin() {
		return workingTemperatureMin;
	}

	/**
	 * @param workingTemperatureMin
	 *            the workingTemperatureMin to set
	 */
	public void setWorkingTemperatureMin(float workingTemperatureMin) {
		this.workingTemperatureMin = workingTemperatureMin;
	}

	/**
	 * @return the shockStatus
	 */
	public int getShockStatus() {
		return ShockStatus;
	}

	/**
	 * @param shockStatus
	 *            the shockStatus to set
	 */
	public void setShockStatus(int shockStatus) {
		ShockStatus = shockStatus;
	}

	/**
	 * @return the tamperStatus
	 */
	public int getTamperStatus() {
		return TamperStatus;
	}

	/**
	 * @param tamperStatus
	 *            the tamperStatus to set
	 */
	public void setTamperStatus(int tamperStatus) {
		TamperStatus = tamperStatus;
	}

	public int getAnearStatus() {
		return anearStatus;
	}

	public void setAnearStatus(int anearStatus) {
		this.anearStatus = anearStatus;
	}
}
