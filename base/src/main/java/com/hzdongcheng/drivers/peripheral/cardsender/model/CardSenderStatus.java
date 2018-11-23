package com.hzdongcheng.drivers.peripheral.cardsender.model;

public class CardSenderStatus {
	/**
	 * 卡机通道状态
	 * 0-无卡 1-出卡口处有一张卡  2-RF/IC卡位有卡
	 */
	private byte bLaneStatus;
	/**
	 * 发卡箱状态
	 * 0-无卡 1- 卡少 2-卡充足
	 */
	private byte bCardBoxStatus ;
	/**
	 * 回收箱状态
	 * 0-未满 1-卡满
	 */
	private boolean isCaptureBoxFull ;
	/**
	 * @return the bLaneStatus
	 */
	private boolean isBusy = false;
	
	@Override
	public String toString() {
		return new String("通道状态： " + bLaneStatus + " 发卡箱状态： " + bCardBoxStatus + " 回收箱状态： " + isCaptureBoxFull);
	}
	public byte getbLaneStatus() {
		return bLaneStatus;
	}
	/**
	 * @param bLaneStatus the bLaneStatus to set
	 */
	public void setbLaneStatus(byte bLaneStatus) {
		this.bLaneStatus = bLaneStatus;
	}
	/**
	 * @return the bCardBoxStatus
	 */
	public byte getbCardBoxStatus() {
		return bCardBoxStatus;
	}
	/**
	 * @param bCardBoxStatus the bCardBoxStatus to set
	 */
	public void setbCardBoxStatus(byte bCardBoxStatus) {
		this.bCardBoxStatus = bCardBoxStatus;
	}
	/**
	 * @return the isCaptureBoxFull
	 */
	public boolean isCaptureBoxFull() {
		return isCaptureBoxFull;
	}
	/**
	 * @param isCaptureBoxFull the isCaptureBoxFull to set
	 */
	public void setCaptureBoxFull(boolean isCaptureBoxFull) {
		this.isCaptureBoxFull = isCaptureBoxFull;
	}
	
	public boolean isBusy() {
		return isBusy;
	}
	
	public static CardSenderStatus BUSY() {
		CardSenderStatus status = new CardSenderStatus();
		status.bCardBoxStatus = -1;
		status.bLaneStatus = -1;
		status.isCaptureBoxFull = false;
		status.isBusy = true;
		return status;
	}
	
}
