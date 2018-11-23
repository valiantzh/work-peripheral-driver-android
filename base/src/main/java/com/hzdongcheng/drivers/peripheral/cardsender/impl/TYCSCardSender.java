package com.hzdongcheng.drivers.peripheral.cardsender.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardsender.AbstractCardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderStatus;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;

public class TYCSCardSender extends AbstractCardSender {
	ExecutorService executor = Executors.newSingleThreadExecutor();
	volatile boolean is_busy = false;
	CardSenderStatus status = new CardSenderStatus();
	
	@Override
	public String resetCardSender() throws DriversException {
		is_busy = true;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					serialPortBase.send(new byte[] {0x02, 0x30, 0x52, 0x53, 0x03, 0x30, 0x05}, 7);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
				wait_for_sending();
				is_busy = false;
			}
		});
		return null;
	}

	@Override
	public CardSenderStatus queryStatus() throws DriversException {
		if(is_busy) {
			return CardSenderStatus.BUSY();
		} else {
			_query();
			return status;
		}
	}

	@Override
	public String moveCard2Front() throws DriversException {
		is_busy = true;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					serialPortBase.send(new byte[] {0x02, 0x30, 0x44, 0x43, 0x03, 0x36, 0x05}, 7);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
				wait_for_sending();
				is_busy = false;
			}
		});
		return null;
	}

	@Override
	public String recycleCard() throws DriversException {
		is_busy = true;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					serialPortBase.send(new byte[] {0x02, 0x30, 0x43, 0x50, 0x03, 0x22, 0x05}, 7);
				} catch (SerialPortException e) {
					e.printStackTrace();
				}
				wait_for_sending();
				is_busy = false;
			}
		});
		return null;
	}
	
	private byte[] _query() {
		byte[] buf = new byte[128];
		int index = 0;
		try {
//			clear();
			serialPortBase.send(new byte[] {0x02, 0x30, 0x52, 0x46, 0x03, 0x25, 0x05}, 7);
			serialPortBase.recv(buf, 128);
		} catch (SerialPortException e) {
			e.printStackTrace();
		}

		for(int i = 0;i<10;i++) {
			if (buf[i] == 0x46) {
				index = i+1;
			}
		}
		status.setbCardBoxStatus((byte)0);
		status.setbLaneStatus((byte)0);
		status.setCaptureBoxFull(false);
		switch (buf[index]) {
			case 0x30:
				status.setbLaneStatus((byte)0);
				break;
			case 0x31:
				status.setbLaneStatus((byte)9);
				break;
			case 0x32:
				status.setbLaneStatus((byte)9);
				break;
			case 0x34:
				status.setbLaneStatus((byte)2);
				break;
			case 0x38:
				status.setbLaneStatus((byte)1);
				break;
		}

		switch (buf[index+1]) {
			case 0x30:
				status.setbCardBoxStatus((byte)9);
				break;
			case 0x31:
				status.setbCardBoxStatus((byte)1);
				break;
			case 0x32:
				status.setbCardBoxStatus((byte)9);
				break;
			case 0x34:
				status.setbCardBoxStatus((byte)2);
				break;
			case 0x38:
				status.setbCardBoxStatus((byte)2);
				break;
		}

		switch (buf[index+2]) {
			case 0x30:
				break;
			case 0x31:
				status.setbLaneStatus((byte)1);
				break;
			case 0x32:
				status.setCaptureBoxFull(true);
				break;
			case 0x34:
				status.setbLaneStatus((byte)2);
				break;
			case 0x38:
				status.setbCardBoxStatus((byte)0);
				break;
		}
		return new byte[] {buf[index], buf[index+1], buf[index+2]};
	}
	
	private String dump(byte[] respond) {
		StringBuilder sb = new StringBuilder();
		for (byte b : respond) {
			sb.append(String.format("%02x ", b));
		}
		
		return sb.toString();
	}
	
	private void clear() {
		byte[] buf = new byte[1024];
		int i;
		try {
			serialPortBase.recv(buf, 1024);
			Thread.sleep(200);
		} catch (SerialPortException e) {
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void wait_for_sending() {
		while (true) {
			byte[] ret = _query();
			if ((ret[0] == 0 && ret[1] == 0 && ret[2] == 0) || // query status fail
					(ret[0] & 0x38) == 0x38) {// is sending card
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			} else {
				break;
			}
		}
	}
	

}
