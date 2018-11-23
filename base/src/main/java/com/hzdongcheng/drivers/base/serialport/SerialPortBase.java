package com.hzdongcheng.drivers.base.serialport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import com.hzdongcheng.alxdrivers.jni.ISerialEvent;
import com.hzdongcheng.alxdrivers.jni.JNISerialPort;
import com.hzdongcheng.drivers.base.serialport.exception.NotFoundSerialLibraryException;
import com.hzdongcheng.drivers.base.serialport.exception.NotOpenSerialException;
import com.hzdongcheng.drivers.base.serialport.exception.RecvTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SendTimeoutException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortErrorException;
import com.hzdongcheng.drivers.base.serialport.exception.SerialPortException;
import com.hzdongcheng.drivers.base.serialport.exception.error.SerialPortErrorCode;
import com.hzdongcheng.components.toolkits.exception.ResponseCodeException;
import com.hzdongcheng.components.toolkits.utils.StringUtils;

/**
 * 
* @ClassName: SerialPortBase 
* @Description: TODO(串口实现基类) 
* @author Jxing 
* @date 2016年12月23日 下午1:33:00 
* @version 1.0
 */
public class SerialPortBase extends JNISerialPort {

	/*
	 * 校验位
	 */
	public static final byte DCDZ_NOPARITY     = 0;
	public static final byte DCDZ_ODDPARITY    = 1;
	public static final byte DCDZ_EVENPARITY   = 2;

	public static final char DCDZ_NOPARITY_C    = 'N';
	public static final char DCDZ_ODDPARITY_C   = 'O';
	public static final char DCDZ_EVENPARITY_C  = 'E';

	/*
	 * 停止位
	 */
	public static final byte DCDZ_ONESTOPBIT   = 0;
	public static final byte DCDZ_ONE5STOPBITS = 1;
	public static final byte DCDZ_TWOSTOPBITS  = 2;

	/*
	 * 数据位
	 */
	public static final byte DCDZ_DATABITS7    = 7;
	public static final byte DCDZ_DATABITS8    = 8;
	
	/*
	 * 波特率
	 */
	public static final int DCDZ_CBR_110      = 110;
	public static final int DCDZ_CBR_300      = 300;
	public static final int DCDZ_CBR_600      = 600;
	public static final int DCDZ_CBR_1200     = 1200;
	public static final int DCDZ_CBR_2400     = 2400;
	public static final int DCDZ_CBR_4800     = 4800;
	public static final int DCDZ_CBR_9600     = 9600;
	public static final int DCDZ_CBR_14400    = 14400;
	public static final int DCDZ_CBR_19200    = 19200;
	public static final int DCDZ_CBR_38400    = 38400;
	public static final int DCDZ_CBR_56000    = 56000;
	public static final int DCDZ_CBR_57600    = 57600;
	public static final int DCDZ_CBR_115200   = 115200;
	public static final int DCDZ_CBR_128000   = 128000;
	public static final int DCDZ_CBR_256000   = 256000;	
	
	//对象锁
	protected final Object lockObject = new Object(); 
	
	//串口标识
	private long _fd = 0;
	//dll文件加载状态
	public static boolean isLoad = false;
	//serial port load helper class
	private static final LoadLibraryHelper loadLibraryHelper = new LoadLibraryHelper();
	
	/*
	 * Arm Linux系统下串口名称
	 */
	private static String[] linux_port = {
			"/dev/ttymxc1", 
			"/dev/ttymxc2", 
			"/dev/ttymxc3", 
			"/dev/ttymxc4", 
			"/dev/ttymxc5",
			"/dev/ttymxc6",
			"/dev/ttymxc7",
			"/dev/ttymxc8"};
		
	/*
	 * Windows系统下串口名称
	 */
	private static final String[] window_port = {
			"COM1", 
			"COM2", 
			"COM3", 
			"COM4", 
			"COM5", 
			"COM6",
			"COM7",
			"COM8"
			};
			
	private static class LoadLibraryHelper {
		
		public void loadLibrary() throws Exception{
			
			/*
			 * 读取当前操作系统版本
			 */
			Properties prop = System.getProperties();
			String os = prop.getProperty("os.name").toUpperCase();
			String arch = prop.getProperty("os.arch").toUpperCase();
			
			String filePath = "";
			String hz = "";
			String fileName = "libDcdzMsComm_CP";
			
			
			//如果是Windows系统
			if (os.startsWith("WIN")) {
				filePath = "/libs/x86/libDcdzMsComm_CP.dll";
				hz = ".dll";
			} else {
				if (arch.contains("I386")){
					filePath = "/libs/linux/libDcdzMsComm_CP_ubuntu.so1";
					linux_port = new String[] {
							"/dev/ttyS0", 
							"/dev/ttyS1", 
							"/dev/ttyS2", 
							"/dev/ttyS3", 
							"/dev/ttyS4",
							"/dev/ttyS5",
							"/dev/ttyS6",
							"/dev/ttyS7"};
				} else {
					//Android
					if (arch.contains("ARMV7")){
						//filePath = "/libs/android/armeabi-v7a/libDcdzMsComm_CP.so1";
						filePath = "/lib/armeabi-v7a/libCommAndModbus.so";
					} else if (arch.contains("I686")) {
						filePath = "/libs/android/x86/libDcdzMsComm_CP.so1";
					} else if (arch.contains("AARCH")){
						//filePath = "/libs/android/armeabi-v7a/libDcdzMsComm_CP.so1";
						filePath = "/libs/android/armeabi-v7a/libCommAndModbus.so";
					}
				}

				hz = ".so";
			}

			InputStream is = getClass().getResource(filePath).openStream();
			File dll = File.createTempFile(fileName, hz);
			FileOutputStream out = new FileOutputStream(dll);

			int i;
			byte [] buf = new byte[1024];
			while((i = is.read(buf)) != -1 ) {
				out.write(buf,0,i);
			}

			is.close();
			out.close();
			dll.deleteOnExit();
			//加载动态库
			System.load(dll.toString());
		}
	}
	
	/**
	 * 加载dll，JNI调用
	 */
	static{
		try{
			//loadLibraryHelper.loadLibrary();
			System.loadLibrary("CommAndModbus");
			isLoad = true;
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static String transformSerialPort(String port){
		
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name").toUpperCase();

		if (!os.startsWith("WIN")){
			
			int i = 0;
			for (i = 0; i < window_port.length; i++){
				if (window_port[i].equalsIgnoreCase(port)){
					return linux_port[i];
				}
			}
			if(StringUtils.isNotEmpty(port) && !port.startsWith("/dev/")){
				port = "/dev/"+port;
			}
		}

		return port;
	}
	
	/**
	 * 
	* @Title: open 
	* @Description: TODO(打开串口) 
	* @param @param portName
	* @param @return
	* @param @throws NotFoundSerialLibraryException
	* @param @throws NotOpenSerialException
	* @return boolean
	* @throws
	 */
	public boolean open(String portName) 
			throws NotFoundSerialLibraryException, SerialPortErrorException {
		
		if (!isLoad)
			throw new NotFoundSerialLibraryException(SerialPortErrorCode.ERR_SP_NOTFOUNDSERIALLIBRARY);
		
		close();
		
		String port = transformSerialPort(portName);
		
		_fd = dllOpen(port);
		if (0 == _fd)
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT,
					"Open serial port " + portName + " failed");
		
		return true;
	}
	
	/**
	 * 
	* @Title: close 
	* @Description: TODO(关闭串口) 
	* @param  设定文件 
	* @return void 返回类型 
	* @throws
	 */
	public void close(){
		
		if (0 == _fd)
			return;
		
		closeEventMode();

		dllClose(_fd);

		_fd = 0;
	}
	
	/**
	 * @throws ResponseCodeException 
	 * @throws NotOpenSerialException 
	 * 
	* @Title: initialize 
	* @Description: TODO(初始化串口) 
	* @param @param dwBaudRate
	* @param @param uPaity
	* @param @param uStopBits
	* @param @param uDataBytes
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	public boolean initialize(int dwBaudRate, byte uPaity, byte uStopBits, byte uDataBytes) 
			throws NotOpenSerialException, SerialPortErrorException{
				
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		if (!dllInitialize(_fd, dwBaudRate, uPaity, uStopBits, uDataBytes))
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Initialize serial port property failed.");
		
		return true;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws ResponseCodeException 
	 * 
	* @Title: setInOutQueue 
	* @Description: TODO(设置输入输出缓冲区大小) 
	* @param @param dwInQueue
	* @param @param dwOutQueue
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	public boolean setInOutQueue(int dwInQueue, int dwOutQueue) 
			throws NotOpenSerialException, SerialPortErrorException {
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		if (!dllSetInOutQueue(_fd, dwInQueue, dwOutQueue))
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Set serial port in and out queue failed.");
		
		return true;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws SerialPortException 
	 * 
	* @Title: setReadWriteTimeouts 
	* @Description: TODO(设置读写超时时间) 
	* @param @param dwReadTotalTimeoutConstant
	* @param @param dwWriteTotalTimeoutConstant
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	public boolean setReadWriteTimeouts(int dwReadTotalTimeoutConstant, int dwWriteTotalTimeoutConstant) 
			throws NotOpenSerialException, SerialPortErrorException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		if (!dllSetReadWriteTimeouts(_fd, dwReadTotalTimeoutConstant, dwWriteTotalTimeoutConstant))
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Set serial port read and write timeout value failed.");
		
		return true;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws SerialPortException 
	 * 
	* @Title: clear 
	* @Description: TODO(清除缓冲区数据) 
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	public boolean clear() throws NotOpenSerialException, SerialPortErrorException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		if (!dllClear(_fd))
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT,
					"Clear serial port queue failed.");
		
		return true;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws SerialPortException 
	 * 
	* @Title: clearError 
	* @Description: TODO(清除串口错误) 
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	public boolean clearError() throws NotOpenSerialException, SerialPortErrorException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		if (!dllClearError(_fd))
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Clear serial port error failed.");
		
		return true;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * 
	* @Title: bytesInQueue 
	* @Description: TODO(查看当前缓冲区有多少字节的数据可以读入) 
	* @param @return 设定文件 
	* @return int 返回类型 
	* @throws
	 */
	public int bytesInQueue() throws NotOpenSerialException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		return dllBytesInQueue(_fd);
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws SerialPortException 
	 * @throws NotOpenSerialException 
	 * 
	* @Title: openEventMode 
	* @Description: TODO(打开串口事件模式) 
	* @param @param event
	* @param @return 设定文件 
	* @return boolean 返回类型 
	* @throws
	 */
	public boolean openEventMode(ISerialEvent event) throws NotOpenSerialException, SerialPortErrorException {
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		boolean bSuccess = false;
		synchronized(lockObject){
			bSuccess = dllOpenEventMode(_fd, event);
		}
		
		if (!bSuccess)
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Open serial port event mode failed.");
		
		return bSuccess;
	}
	
	/**
	 * 
	* @Title: closeEventMode 
	* @Description: TODO(关闭串口事件模式)
	* @return void 返回类型 
	* @throws
	 */
	public void closeEventMode(){
		
		if (0 == _fd)
			return;
		
		synchronized(lockObject){
			dllCloseEventMode(_fd);
		}
	}
	
	/**
	 * @throws SendTimeoutException 
	 * @throws SerialPortErrorException 
	 * @throws NotOpenSerialException 
	 * 
	* @Title: send 
	* @Description: TODO(发送数据) 
	* @param @param szData
	* @param @param uNumberOfBytesToSend
	* @param @return
	* @return int 返回类型 
	* @throws
	 */
	public int send(byte[] szData, int uNumberOfBytesToSend) 
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		int ret = dllSend(_fd, szData, uNumberOfBytesToSend);
		
		if (ret < 0)
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Send data to serial port failed.");
		if (ret == 0)
			throw new SendTimeoutException(SerialPortErrorCode.ERR_SP_SENDDATATIMEOUT);
		
		return ret;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws SerialPortException 
	 * 
	* @Title: sendFixLength 
	* @Description: TODO(发送定长数据) 
	* @param @param array
	* @param @param len
	* @param @return
	* @param @throws LockerException
	* @return int 返回类型 
	* @throws
	 */
	public int sendFixLength(final byte[] array, int len) 
			throws NotOpenSerialException, SerialPortErrorException, SendTimeoutException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		int ret = dllSendFixLength(_fd, array, len);
		
		if (-1 == ret)
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Send data to serial port failed.");
		if (-2 == ret)
			throw new SendTimeoutException(SerialPortErrorCode.ERR_SP_SENDDATATIMEOUT);
		
		return ret;
	}
	
	/**
	 * @throws NotOpenSerialException 
	 * @throws SerialPortErrorException 
	 * @throws RecvTimeoutException 
	 * 
	* @Title: recv 
	* @Description: TODO(接收数据) 
	* @param @param szData
	* @param @param uNumberOfBytesToRecv
	* @param @return
	* @return int 返回类型 
	* @throws
	 */
	public int recv(byte[] szData, int uNumberOfBytesToRecv) 
			throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		int ret = dllRecv(_fd, szData, uNumberOfBytesToRecv);
		
		if (ret < 0)
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Recv data to serial port failed.");
		
		if (ret == 0)
			throw new RecvTimeoutException(SerialPortErrorCode.ERR_SP_RECVDATATIMEOUT);
		
		return ret;
	}
	
	/**
	 * @throws RecvTimeoutException 
	 * @throws SerialPortErrorException 
	 * @throws NotOpenSerialException 
	 * 
	* @Title: recvFixLength 
	* @Description: TODO(接收定长数据) 
	* @param @param szData
	* @param @param uNumberOfBytesToRecv
	* @param @return
	* @param @throws LockerException 设定文件 
	* @return int 返回类型 
	* @throws
	 */
	public int recvFixLength(byte[] szData, int uNumberOfBytesToRecv) 
			throws NotOpenSerialException, SerialPortErrorException, RecvTimeoutException{
		
		if (0 == _fd)
			throw new NotOpenSerialException(SerialPortErrorCode.ERR_SP_NOTOPENSERIALPORT);
		
		int ret = dllRecvFixLength(_fd, szData, uNumberOfBytesToRecv);

		if (-1 == ret)
			throw new SerialPortErrorException(SerialPortErrorCode.ERR_SP_SERIALPORT, 
					"Recv data to serial port failed.");
		
		if (-2 == ret)
			throw new RecvTimeoutException(SerialPortErrorCode.ERR_SP_RECVDATATIMEOUT);

		return ret;
	}
}
