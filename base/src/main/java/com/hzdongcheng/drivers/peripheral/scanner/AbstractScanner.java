package com.hzdongcheng.drivers.peripheral.scanner;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.hzdongcheng.alxdrivers.jni.ISerialEvent;
import com.hzdongcheng.drivers.peripheral.scanner.event.IScannerListener;
import com.hzdongcheng.drivers.peripheral.scanner.event.ScannerEvent;
import com.hzdongcheng.drivers.base.serialport.SerialPortBase;
import com.hzdongcheng.drivers.base.serialport.exception.InitializeSerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ProtocolParsingException;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;

/**
 * 
 * @ClassName: AbstractScanner
 * @Description: 扫描枪控制抽象类
 * @author Administrator
 * @date 2018年1月26日 下午3:28:06
 */
public abstract class AbstractScanner implements IScanner {

	// 串口类对象
	protected final SerialPortBase serialPortBase = new SerialPortBase();
	// 日志类对象
	protected final Log4jUtils logger = Log4jUtils.createInstanse(this.getClass());
	// 最大数据接收长度
	protected final int MAX_RECV_LEN = 1024;
	// 最大数据发送长度
	protected final int MAX_SEND_LEN = 100;
	// 同步锁
	protected final Object lockObject = new Object();
	// 监听器容器
	private Collection<IScannerListener> listeners = null;
	// 读码事件接收类
	protected final HandleEvent handleEvent = new HandleEvent();
	// 常量模式
	protected boolean normallyMode = true;
	// 数据缓存容器
	protected StringBuilder barcodeBuffer = new StringBuilder();
	protected final Lock lock = new ReentrantLock();
	protected final int MAX_OPENBOX_SLEEP = 40;

	public AbstractScanner(boolean normallyMode) {
		this.normallyMode = normallyMode;
	}

	@Override
	public void open(String portName)
			throws NotFoundSerialLibraryException, InitializeSerialPortException, SerialPortErrorException {
		// 打开串口
		serialPortBase.open(portName);

		try {
			// 初始化串口
			serialPortBase.initialize(SerialPortBase.DCDZ_CBR_9600, SerialPortBase.DCDZ_NOPARITY,
					SerialPortBase.DCDZ_ONESTOPBIT, SerialPortBase.DCDZ_DATABITS8);
			// 设置读写缓冲区大小
			serialPortBase.setInOutQueue(MAX_RECV_LEN, MAX_SEND_LEN);
			// 设置读写超时时间
			serialPortBase.setReadWriteTimeouts(2000, 1000);
			// 常量模式特殊处理
			if (normallyMode)
				serialPortBase.openEventMode(handleEvent);

		} catch (SerialPortException e) {
			// 关闭串口
			close();
			// 抛出初始化失败异常
			throw new InitializeSerialPortException(SerialPortErrorCode.ERR_SP_INITIALIZE, e.getMessage());
		}

	}

	@Override
	public void close() {
		// 移除所有的事件监听器
		removeAllListener();
		// 关闭串口
		serialPortBase.close();
	}

	@Override
	public boolean isNormallyMode() {
		return normallyMode;
	}

	@Override
	public boolean addListener(IScannerListener listener) {

		boolean bOK = false;

		synchronized (lockObject) {

			if (listeners == null) {
				listeners = new HashSet<IScannerListener>();
			}

			if (listeners != null) {
				if (!listeners.contains(listener))
					bOK = listeners.add(listener);
				else
					bOK = true;
			}
		}

		return bOK;
	}

	@Override
	public void removeListener(IScannerListener listener) {
		synchronized (lockObject) {
			if (listeners == null)
				return;
			if (listeners.contains(listener)) {
				listeners.remove(listener);
			}
		}
	}

	@Override
	public void removeAllListener() {
		synchronized (lockObject) {
			if (listeners != null)
				listeners.clear();
		}
	}

	// 查询版本
	protected abstract String _searchVersion() throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	@Override
	public String searchVersion() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
			RecvTimeoutException, ResponseCodeException, ProtocolParsingException {

		String versionString = "";

		serialPortBase.closeEventMode();

		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				versionString = _searchVersion();
			}
		} finally {

			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);

		}

		return versionString;
	}

	// 切换扫描枪模式，支持触发扫码
	protected abstract int _switchMode(boolean normallyMode) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	@Override
	public int switchMode(boolean normallyMode) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
		int ret = 0;
		serialPortBase.closeEventMode();
		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = _switchMode(normallyMode);
			}
		} finally {
			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);
		}
		return ret;

	}

	protected abstract int _oneDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/*
	 * (non Javadoc) <p>Title: oneDScanningOnOff</p> <p>Description: </p>
	 * 
	 * @param onOff
	 * 
	 * @return
	 * 
	 * @throws NotOpenSerialException
	 * 
	 * @throws SerialPortErrorException
	 * 
	 * @throws SendTimeoutException
	 * 
	 * @throws RecvTimeoutException
	 * 
	 * @throws ResponseCodeException
	 * 
	 * @throws ProtocolParsingException
	 * 
	 * @see com.hzdongcheng.components.driver.scanner.IScanner#oneDScanningOnOff(
	 * boolean)
	 */
	@Override
	public int oneDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
		int ret = 0;
		serialPortBase.closeEventMode();
		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = _oneDScanningOnOff(onOff);
			}
		} finally {
			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);

		}
		return ret;
	}

	protected abstract int _twoDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/*
	 * (non Javadoc) <p>Title: twoDScanningOnOff</p> <p>Description: </p>
	 * 
	 * @param onOff
	 * 
	 * @return
	 * 
	 * @throws NotOpenSerialException
	 * 
	 * @throws SerialPortErrorException
	 * 
	 * @throws SendTimeoutException
	 * 
	 * @throws RecvTimeoutException
	 * 
	 * @throws ResponseCodeException
	 * 
	 * @throws ProtocolParsingException
	 * 
	 * @see com.hzdongcheng.components.driver.scanner.IScanner#twoDScanningOnOff(
	 * boolean)
	 */
	@Override
	public int twoDScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
		int ret = 0;
		serialPortBase.closeEventMode();
		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = _twoDScanningOnOff(onOff);
			}
		} finally {
			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);
		}
		return ret;
	}

	protected abstract int _enableScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	/*
	 * (non Javadoc) <p>Title: enableScanningOnOff</p> <p>Description: </p>
	 * 
	 * @param onOff
	 * 
	 * @return
	 * 
	 * @throws NotOpenSerialException
	 * 
	 * @throws SerialPortErrorException
	 * 
	 * @throws SendTimeoutException
	 * 
	 * @throws RecvTimeoutException
	 * 
	 * @throws ResponseCodeException
	 * 
	 * @throws ProtocolParsingException
	 * 
	 * @see com.hzdongcheng.components.driver.scanner.IScanner#enableScanningOnOff(
	 * boolean)
	 */
	@Override
	public int enableScanningOnOff(boolean onOff) throws NotOpenSerialException, SerialPortErrorException,
			SendTimeoutException, RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
		int ret = 0;
		serialPortBase.closeEventMode();
		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = _enableScanningOnOff(onOff);
			}
		} finally {
			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);
		}
		return ret;
	}

	// 发送扫码指令
	protected abstract int _startScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
			RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	@Override
	public int startScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
			RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
		int ret = 0;
		serialPortBase.closeEventMode();
		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = _startScan();
			}
		} finally {
			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);
		}
		return ret;
	}

	// 停止扫码指令
	protected abstract int _stopScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
			RecvTimeoutException, ResponseCodeException, ProtocolParsingException;

	@Override
	public int stopScan() throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException,
			RecvTimeoutException, ResponseCodeException, ProtocolParsingException {
		int ret = 0;
		serialPortBase.closeEventMode();
		try {
			synchronized (lockObject) {
				try {
					Thread.sleep(MAX_OPENBOX_SLEEP);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret = _stopScan();
			}
		} finally {
			barcodeBuffer.setLength(0);
			serialPortBase.openEventMode(handleEvent);
		}
		return ret;
	}

	/*
	 * 
	 */
	private class HandleEvent implements ISerialEvent {

		@Override
		public void fire_HandleData(int ret, byte[] szData) {

			if (ret <= 0) {
				logger.error("[驱动][扫描]==>Scanner recv event handler is called. but result is " + ret + ". "
						+ new String(szData));
				return;
			}

			/**
			 * @modified by Peace
			 * @date 2017年12月16日
			 * 
			 */
			// for (byte it : szData) {
			//
			// if (it == 0x0d && !barcodeBuffer.toString().isEmpty()) {
			// String dataString = barcodeBuffer.toString().replace("html",
			// "").trim();
			// ScannerEvent event = new ScannerEvent(this, ret, dataString);
			// barcodeBuffer.setLength(0);
			// notifyListeners(event);
			// break;
			// }
			//
			// barcodeBuffer.append((char) it);
			// }

			byte[] buffer = Arrays.copyOf(szData, szData.length);
			for (int i = 0; i < buffer.length; i++) {
				if (i == 0 && buffer[0] == 0x0a && barcodeBuffer.length() > 0
						&& barcodeBuffer.charAt(barcodeBuffer.length() - 1) == 0x0d) {
					String dataString = barcodeBuffer.deleteCharAt(barcodeBuffer.length() - 1).toString()
							.replace("html", "").trim();
					ScannerEvent event = new ScannerEvent(this, ret, dataString);
					barcodeBuffer.setLength(0);
					notifyListeners(event);
				} else if (buffer[i] == 0x0d && (i + 1 < buffer.length && buffer[i + 1] == 0x0a)) {
					String dataString = barcodeBuffer.toString().replace("html", "").trim();
					ScannerEvent event = new ScannerEvent(this, ret, dataString);
					barcodeBuffer.setLength(0);

					notifyListeners(event);
					break;
				}

				barcodeBuffer.append((char) buffer[i]);
			}

		}

		/**
		 * 通知所有的ScannerListener
		 */
		private void notifyListeners(ScannerEvent event) {

			synchronized (lockObject) {
				Iterator<IScannerListener> iter = listeners.iterator();
				while (iter.hasNext()) {
					IScannerListener listener = (IScannerListener) iter.next();
					listener.onNotice(event);
					logger.info("[驱动][扫描]==>监听扫描--> 扫描结果： [" + event.getDataString()+"]");//
				}
			}
		}
	}
}
