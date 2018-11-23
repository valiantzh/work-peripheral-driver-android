 package com.hzdongcheng.drivers.peripheral.cardsender.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.cardsender.AbstractCardSender;
import com.hzdongcheng.drivers.peripheral.cardsender.model.CardSenderStatus;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;

/**
 * 
 * @ClassName: ACTSimCardSender 
 * @Description: 深圳驰卡SIM卡发卡器控制类  --ACT-F3通讯协议V1.02
 * @author: Administrator
 * @date: 2018年2月28日 上午11:06:01
 */
public class ACTSimCardSender extends AbstractCardSender{
	ExecutorService executor = Executors.newSingleThreadExecutor();
	volatile boolean is_busy = false;
	CardSenderStatus status = new CardSenderStatus();
	byte[] queryRespond;
	
	@Override
	public String resetCardSender() throws DriversException {
		if (is_busy) {
			throw new DriversException(1, "Device is busy");
		}
		is_busy = true;
		executor.submit(new Runnable() {
			public void run() {
				try {
					send((byte)0x00, (byte)0x30, (byte)0x37);
					recv_ack();
					consume_respond();
				} catch (DriversException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				is_busy = false;
			}
		});
		return null;
	}

	@Override
	public CardSenderStatus queryStatus() throws DriversException {
		if (is_busy) {
			throw new DriversException(1, "Device is busy");
		} else {
			if (queryRespond[0] == 0x31) {
				_query();
			}
			return status;
		}
	}
	
	private void _query() throws DriversException {
		if(is_busy) {
			throw new DriversException(1, "Device is busy");
		}
		is_busy = true;
		executor.submit(new Runnable() {
			@Override
			public void run() {
				try {
					send((byte)0x00, (byte)0x31, (byte)0x30);
					recv_ack();
					consume_respond();
					if (queryRespond != null) {
						switch (queryRespond[0]) {
							case 0x30:
								status.setbLaneStatus((byte)0);
								break;
							case 0x31:
								status.setbLaneStatus((byte)1);
								break;
							case 0x32:
								status.setbLaneStatus((byte)2);
								break;
						}
						switch (queryRespond[1]) {
							case 0x30:
								status.setbCardBoxStatus((byte)0);
								break;
							case 0x31:
								status.setbCardBoxStatus((byte)1);
								break;
							case 0x32:
								status.setbCardBoxStatus((byte)2);
								break;
						}
						switch (queryRespond[2]) {
							case 0x30:
								status.setCaptureBoxFull(false);
								break;
							case 0x31:
								status.setCaptureBoxFull(true);
								break;
						}
					}
				} catch (DriversException e) {
					e.printStackTrace();
				}
				is_busy = false;
			}
		});
	}

	@Override
	public String moveCard2Front() throws DriversException {
		if(is_busy) {
			throw new DriversException(1, "Device is busy");
		}
		is_busy = true;
		executor.submit(new Runnable() {
			public void run() {
				try {
					send((byte)0x00, (byte)0x32, (byte)0x30);
					recv_ack();
					try {
						Thread.sleep(5000);
					} catch(InterruptedException e) {
						
					}
					consume_respond();
				} catch (DriversException e) {
					e.printStackTrace();
				}
				is_busy = false;
			}
		});
		return null;
	}

	@Override
	public String recycleCard() throws DriversException {
		if(is_busy) {
			throw new DriversException(1, "Device is busy");
		}
		is_busy = true;
		executor.submit(new Runnable() {
			public void run() {
				try {
					send((byte)0x00, (byte)0x32, (byte)0x33);
					recv_ack();
					try {
						Thread.sleep(5000);
					} catch(InterruptedException e) {
						
					}
					consume_respond();
				} catch (DriversException e) {
					e.printStackTrace();
				}
				is_busy = false;
			}
		});
		return null;
	}

	/* (non Javadoc) 
	 * @Title: moveCard2ReadPosition
	 * @Description: TODO
	 * @param isRF
	 * @return
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.AbstractCardSender#sendCard2ReadPosition(boolean)
	 */ 
	@Override
	public String moveCard2ReadPosition(boolean isRF) throws DriversException {
		if(isRF) {
			throw new DriversException(0, "RF card is not currently supported");
		}
		if(is_busy) {
			throw new DriversException(0, "Device is busy");
		}
		is_busy = true;
		executor.submit(new Runnable() {
			public void run() {
				try {
					send((byte)0x00, (byte)0x32, (byte)0x32);
					recv_ack();
					consume_respond();
				} catch (DriversException e) {
					e.printStackTrace();
				}
				is_busy = false;
			}
		});
		return null;
	}

	/* (non Javadoc) 
	 * @Title: readData
	 * @Description: TODO
	 * @param section
	 * @param block
	 * @return
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.AbstractCardSender#readData(byte, byte)
	 */ 
	@Override
	public byte[] readData(byte section, byte block) throws DriversException {
		return null;
	}

	/* (non Javadoc) 
	 * @Title: writeData
	 * @Description: TODO
	 * @param section
	 * @param block	
	 * @param data
	 * @throws DriversException 
	 * @see com.hzdongcheng.drivers.peripheral.cardsender.AbstractCardSender#writeData(byte, byte, byte[])
	 */ 
	@Override
	public void writeData(byte section, byte block, byte[] data) throws DriversException {
		
	}
	
	private void short_delay() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			
		}
	}
	
	private void long_delay() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
		}
	}
	
	private void consume_respond() {
		try {
			recv_respond();
		} catch (DriversException e) {
			e.printStackTrace();
		}
	}
	
	private String dump_respond(byte[] respond) {
		StringBuilder sb = new StringBuilder(respond.length * 3);
		for(byte b : respond)
			sb.append(String.format("%02x ", b));
		return sb.toString();
	}
	
	private byte[] recv_respond() throws DriversException {
		byte[] temp = new byte[512];
		int index = 0;
		int len;
		long_delay();
		try {
			len = serialPortBase.recv(temp, 512);
		} catch (SerialPortException e) {
			throw new DriversException(0, "serial communication error: " + e.getMessage());
		}
		// parse frame
		if(temp[0] != (byte)0xf2 || temp[len - 2] != (byte)0x03) {
			throw new DriversException(0, "incomplete respond frame");
		}
		if((temp[2] * 0x100 + temp[3] + 6) != len) {
			throw new DriversException(0, "mismatched length");
		}
		
		if((temp[4] != (byte)0x50)) {
			throw new DriversException(0, String.format("opration failed with cmd: 0x%02x, pm: 0x%02x, e1e2:%c%c", 
					temp[5], temp[6], temp[7], temp[8]));
		}
		byte[] respond = new byte[len];
		System.arraycopy(temp, 0, respond, 0, len);
		for(int i = 0;i<10;i++) {
			if (respond[i] == 0x50) {
				index = i+3;
			}
		}
		queryRespond = new byte[] {respond[index],respond[index+1],respond[index+2]};
		switch (queryRespond[0]) {
			case 0x30:
				status.setbLaneStatus((byte)0);
				break;
			case 0x31:
				status.setbLaneStatus((byte)1);
				break;
			case 0x32:
				status.setbLaneStatus((byte)2);
				break;
		}
		switch (queryRespond[1]) {
			case 0x30:
				status.setbCardBoxStatus((byte)0);
				break;
			case 0x31:
				status.setbCardBoxStatus((byte)1);
				break;
			case 0x32:
				status.setbCardBoxStatus((byte)2);
				break;
		}
		switch (queryRespond[2]) {
			case 0x30:
				status.setCaptureBoxFull(false);
				break;
			case 0x31:
				status.setCaptureBoxFull(true);
				break;
		}
		return respond;
	}
	
	private void recv_ack() throws DriversException {
		byte[] respond = new byte[1];
		short_delay();
		try {
			serialPortBase.recv(respond, 1);
		} catch (SerialPortException e) {
			throw new DriversException(0, "serial communication error: " + e.getMessage());
		}
		if(respond[0] == 0x15) {
			throw new DriversException(0, "NAK received, command will not be executed");
		}
	}
	
	private void send(byte addr, byte cmd, byte para) throws DriversException {
		send(addr, cmd, para, null);
	}
	
	private void send(byte addr, byte cmd, byte para, byte[] payload) throws DriversException {
		try {
			byte[] request = generate_cmd(addr, cmd, para, payload);
			serialPortBase.send(request, request.length);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			throw new DriversException(0, "serial communication error: " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unused")
	private byte[] generate_cmd(byte addr, byte cmd, byte para, byte[] payload){
		return append_checksum(wrap_up(format_cmd(addr, cmd, para, payload)));
	}
	
	private byte[] generate_cmd(byte addr, byte cmd, byte para) {
		return generate_cmd(addr, cmd, para, null);
	}
	
	private byte[] format_cmd(byte addr, byte cmd, byte para, byte[] payload) {
		byte[] data = new byte[] {(byte)0x43, cmd, para};
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(addr);
			int len = data.length + (payload == null? 0: payload.length);
			outputStream.write(len / 0x100);
			outputStream.write(len % 0x100);
			outputStream.write(data);
			if(payload != null) {
				outputStream.write(payload);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputStream.toByteArray();
	}
	
	// parameters may or may not reference to the results, Don't use it. 
	// Use the return value instead
	private byte[] append_checksum(byte[] cmd) {
		int checksum = 0;
		for(int i = 0; i < cmd.length; i++) {
			checksum ^= cmd[i];
		}
		
		checksum &= 0xff;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(cmd);
			outputStream.write(checksum);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
	
	private byte[] wrap_up(byte[] cmd) {
		int header = 0xf2;
		int tail = 0x03;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			outputStream.write(header);
			outputStream.write(cmd);
			outputStream.write(tail);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return outputStream.toByteArray();
	}
}
