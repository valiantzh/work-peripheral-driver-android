package com.hzdongcheng.drivers.peripheral.printer.impl;

import java.io.UnsupportedEncodingException;

import com.hzdongcheng.drivers.common.Constants;
import com.hzdongcheng.drivers.exception.DriversException;
import com.hzdongcheng.drivers.peripheral.printer.AbstractPrinter;
import com.hzdongcheng.drivers.peripheral.printer.model.PrinterStatus;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;

public class WeiHuangPrinter extends AbstractPrinter {

	/**
	 * 
	 * @Method Name: send  
	 * @Description: 发送串口数据  
	 * @param @param request
	 * @return void 
	 * @throws DriversException
	 */
	protected  void send(byte[] request) throws  DriversException{
		try {
			serialPortBase.sendFixLength(request, request.length);
		} catch (NotOpenSerialException | SerialPortErrorException| SendTimeoutException e) {
			throw new DriversException(e.getErrorCode(),e.getErrorTitle());
		} 
	}
	
	/**
	 * @Method Name: recv 
	 * @Description: 接收串口返回数据 
	 * @param  @param pszBuff
	 * @param  @param len
	 * @return  DriversException
	 */
	protected void recv(byte[] pszBuff, int len)throws DriversException {
		try {
			serialPortBase.recv(pszBuff, len);
		} catch (NotOpenSerialException | SerialPortErrorException | RecvTimeoutException e) {
			throw new DriversException(e.getErrorCode(),e.getErrorTitle());
		}
	}

	//初始化打印机
	private void _reset() throws DriversException{
		send(new byte[]{0x1b, 0x40});//打印机初始化
	}
	//蜂鸣器控制命令
	protected void _buzzer() throws DriversException{
		send(new byte[]{0x1b, 0x1e});//蜂鸣器控制命令
	}
	//切纸 mode 0-半切 1-全切
	protected void _cutPaper(int mode) throws DriversException{
		byte[] cmd = new byte[4];
		cmd[0] = 0x1d;
		cmd[1] = 0x56;
		cmd[2] = 0x42;
		cmd[3] = 0x3;
		send(cmd);
	}
	//#region 打印机控制命令
	private byte setPrintLF(){//打印并换行
		return 0x0a;
	}
	private byte setPrintCR(){//打印并回车
		return 0x0d;
	}
	private byte[] setPrinterReset(){//打印机初始化
		return new byte[]{0x1b, 0x40};
	}
	private byte[] setPrintJ(int n){//打印并进纸 n*0.125 毫米
		byte nn;
		if(n>255){
			nn = (byte)255;
		}else if (n<0){
			nn = 0;
		}else{
			nn = (byte)n;
		}
		return new byte[]{0x1b, 0x4a, nn};
	}
	private byte[] setPrintD(int n){//打印并走纸 n 行
		byte nn;
		if(n>255){
			nn = (byte)255;
		}else if (n<0){
			nn = 0;
		}else{
			nn = (byte)n;
		}
		return new byte[]{0x1b, 0x64, nn};
	}

	/**
	 * 设置打印模式 ESC  ！ n
	 * @param n 0-字符字体 A（12*24）  1-字符字体 B（8*16）
	 * @return
	 */
	private byte[] setPrintMode(int n){//
		byte nn = (byte)n;
		return new byte[]{0x1b, 0x21, nn};
	}

	/**
	 * 切换汉字模式
	 * @param toggle true  设置汉字模式  false 取消汉字模式
	 * @return
	 */
	private byte[] toggleChineseMode(boolean toggle){
		if(toggle) {
			return new byte[] {0x1c, 0x26};
		} else {
			return new byte[] {0x1c, 0x2E};
		}
	}

	/**
	 * 对齐方式
	 * @param type 0-左对齐 1-居中 2-右对齐
	 * @return
	 */
	private byte[] setAlign(int type){
		if(type<0 || type>2){
			type = 0;
		}
		byte n = (byte)type;
		byte[] cmd = new byte[] {0x1c, 0x26, n};
		return cmd;
	}
	private byte[] setDefaultLineSpace(){
		byte[] cmd = new byte[] {0x1b, 0x32};//为 0.375 毫米（3*0.125 毫米）
		return cmd;
	}

	/**
	 * 3.12 设置行间距为（n*0.125 毫米)
	 * @param n
	 * @return
	 */
	private byte[] setLineSpace(int n){
		byte nn = (byte)n;
		if(n>255 ||  n< 0){
			nn = (byte)3;
		}
		byte[] cmd = new byte[] {0x1b, 0x33, nn};
		return cmd;
	}
	/**
	 * 3.13 设置字符右间距（n*0.125 毫米）
	 * @param n
	 * @return
	 */
	private byte[] setRightSpace(int n){
		byte nn = (byte)n;
		if(n>255 ||  n< 0){
			nn = (byte)0;
		}
		byte[] cmd = new byte[] {0x1b, 0x20, nn};
		return cmd;
	}
	/**
	 * 3.14 用 nL 和 nH 设定左边空白量为（nL+ Nh*256）*0.125 毫米
	 * @param nL
	 * @param nH
	 * @return
	 */
	private byte[] setLeftSpace(int nL, int nH){
		byte nl = (byte)nL;
		if(nL>255 ||  nL< 0){
			nl = (byte)0;
		}
		byte nh = (byte)nH;
		if(nH>255 ||  nH< 0){
			nh = (byte)0;
		}
		byte[] cmd = new byte[] {0x1d, 0x4c, nl, nh};
		return cmd;
	}
	/**
	 * 3.15 nL 和 nH 设置打印区域宽度为（nL+nH*256）*0.125 毫米
	 * @param nL
	 * @param nH
	 * @return
	 */
	private byte[] setWidth(int nL, int nH){
		byte nl = (byte)nL;
		if(nL>255 ||  nL< 0){
			nl = (byte)128;
		}
		byte nh = (byte)nH;
		if(nH>255 ||  nH< 0){
			nh = (byte)1;
		}
		byte[] cmd = new byte[] {0x1d, 0x4c, nl, nh};
		return cmd;
	}
	/**
	 * 3.16 设置字符大小
	 * @param n
	 * @return
	 */
	private byte[] setFontSize(int width, int hight){
		int nn = 0;
		if(width>=0 && width<8){
			nn += (width<<3)&0xff;
		}
		if(hight>=0 && hight<8){
			nn += (hight)&0xff;
		}
		byte[] cmd = new byte[] {0x1d, 0x21, (byte)nn};
		return cmd;
	}

	/**
	 * 设置打印灰度
	 * @param n 1≤n≤12,默认 n=7，可通过手动设置默认灰度值 ;n=1 打印颜色最浅，n=12 打印颜色最深
	 * @return
	 */
	private byte[] setGray(int n){
		int nn = n;
		if(n<1 || n>12){
			nn = 7;
		}
		byte[] cmd = new byte[] {0x1d, 0x21, (byte)nn};
		return cmd;
	}
	//#endregion

	//打印
	protected String _print(byte[] data) throws DriversException  {
		_reset();
		send(data);
		//TODO 切纸
		//_cutPaper();
		return null;
	}

	//状态
	protected PrinterStatus _getStatus() throws DriversException {
		PrinterStatus status = new PrinterStatus();
		
		byte[] szRequestData = new byte[2];
		szRequestData[0] = 0x1c;
		szRequestData[1] = 0x76;
		send(szRequestData);
		
		byte[] recvBuff = new byte[MAX_RECV_LEN];
		recv(recvBuff, MAX_RECV_LEN);
		if((recvBuff[0]&2) == 2){
			status.setCutterStatus(Constants.ONE_STATUS);
		}else{
			status.setCutterStatus(Constants.ZERO_STATUS);
		}
		if((recvBuff[0]&4) == 4){
			status.setPater1Status(Constants.ONE_STATUS);
		}else{
			status.setPater1Status(Constants.ZERO_STATUS);
		}
		if((recvBuff[0]&8) == 8){
			status.setPater2Status(Constants.ONE_STATUS);
		}else{
			status.setPater2Status(Constants.ZERO_STATUS);
		}
		if((recvBuff[0]&16) == 16){
			status.setPater3Status(Constants.ONE_STATUS);
		}else{
			status.setPater3Status(Constants.ZERO_STATUS);
		}
		//_buzzer();
		return status;
	}
	
	private void toggleChineseCharacter(boolean toggle) throws DriversException {
		if(toggle) {
			send(new byte[] {0x1c, 0x26});
		} else {
			send(new byte[] {0x1c, 0x2E});
		}
	}
	
	private static abstract class func<R, T> {
		public abstract R run(T value) throws DriversException;
	}
	
	private void try_print_func(Character c, func<Boolean, Character>... print_funcs) throws DriversException {
		for (func<Boolean, Character> f : print_funcs) {
			boolean ret = f.run(c);
			if(ret) {
				return;
			}
		}
	}
	
	private func<Boolean, Character> print_ascii_char = 
			new func<Boolean, Character>(){
		@Override
		public Boolean run(Character c) throws DriversException {
			char temp = c.charValue();
			if(c >= 0 && c <= 127) {
				send(new byte[] {(byte)temp});
				return true;
			} else {
				return false;
			}
		}
	};
	
	private func<Boolean, Character> print_chinese_char = 
			new func<Boolean, Character>(){
		@Override
		public Boolean run(Character c) throws DriversException {
			char temp = c.charValue();
			if(c >= 0x4e00 && c <= 0x9ffff) {
				String str = c.toString();
				toggleChineseCharacter(true);
				try {
					send(str.getBytes("GBK"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				toggleChineseCharacter(false);
			}
			return false;
		}
	};

	
	public void reset() throws DriversException {
		_reset();
	}
	
	@SuppressWarnings("unchecked")
	private void _print(char c) throws DriversException {
		try_print_func(c, print_ascii_char, print_chinese_char);
	}
	
	public String _print(String str) throws DriversException {
		char[] chars = str.toCharArray();
		for(char c : chars) {
			_print(c);
		}
		//切纸
		/*if(){
			_cutPaper(1);
		}*/

		return null;
	}
	
}