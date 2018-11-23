package com.hzdongcheng.drivers.peripheral.scanner.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.hzdongcheng.drivers.peripheral.scanner.IScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.DatalogicScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.Dcsr8001Scanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.DewoScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.HoneywellScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.MotoScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.NewWorldScanner;
import com.hzdongcheng.drivers.peripheral.scanner.model.ScannerConfig;

/** 
 * @ClassName: ScannerFactory 
 * @Description: 扫描枪控制工厂类
 * @author Administrator 
 * @date 2018年1月26日 下午3:41:00 
 */
public class ScannerFactory {
	
	/**
	 * 
	* @Method Name: createDatalogicScanner 
	* @Description: TODO(创建Datalogic扫描枪实例) 
	* @param  @param isNormallyMode
	* @param  @return
	* @return IScanner
	 */
	public static IScanner createDatalogicScanner(boolean isNormallyMode){
		return new DatalogicScanner(isNormallyMode);
	}
	
	/**
	 * 
	* @Method Name: createDewoScanner 
	* @Description: TODO(创建Dewo扫描枪实例) 
	* @param  @param isNormallyMode
	* @param  @return
	* @return IScanner
	 */
	public static IScanner createDewoScanner(boolean isNormallyMode){
		return new DewoScanner(isNormallyMode);
	}
	
	/**
	 * 
	* @Method Name: createHoneywellScanner 
	* @Description: TODO(创建Honeywell扫描枪实例) 
	* @param  @param isNormallyMode
	* @param  @return
	* @return IScanner
	 */
	public static IScanner createHoneywellScanner(boolean isNormallyMode){
		return new HoneywellScanner(isNormallyMode);
	}
	
	/**
	 * 
	* @Method Name: createMotoScanner 
	* @Description: TODO(创建MOTO扫描枪实例) 
	* @param  @param isNormallyMode
	* @param  @return
	* @return IScanner
	 */
	public static IScanner createMotoScanner(boolean isNormallyMode){
		return new MotoScanner(isNormallyMode);
	}
	
	/**
	 * 
	* @Method Name: createNewWorldScanner 
	* @Description: TODO(创建NewWorld扫描枪实例) 
	* @param  @param isNormallyMode
	* @param  @return
	* @return IScanner
	 */
	public static IScanner createNewWorldScanner(boolean isNormallyMode){
		return new NewWorldScanner(isNormallyMode);
	}
	
	/**
	 * 
	* @Method Name: createInstance 
	* @Description: TODO(通过反射创建扫描枪实例) 
	* @param  @param classSimpleName
	* @param  @param isNormallyMode
	* @param  @return
	* @return IScanner
	* @deprecated
	 */
	public static IScanner createInstance(String classSimpleName, boolean isNormallyMode){
		
		String className = "com.hzdongcheng.drivers.peripheral.scanner.impl." + classSimpleName;

		IScanner scanner = null;
		
		try {
			Constructor<?> constructor = Class.forName(className).getConstructor(new Class[]{boolean.class});
			scanner = (IScanner)constructor.newInstance(isNormallyMode);
		} catch (NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		return scanner;
	}
	
	/**
	 * 
	* @Title: createInstance  
	* @Description: TODO(创建扫描枪实例)  
	* @param @param config
	* @param @return
	* @return IScanner 
	* @throws
	 */
	public static IScanner createInstance(ScannerConfig config){
		IScanner scanner = null;
		switch(config.getVendorName()){
		case ScannerConfig.SCANNER_VENDOR_NEWLAND:
			scanner = new NewWorldScanner(config.isNormallyOn());
			break;
		case ScannerConfig.SCANNER_VENDOR_HONEYWELL:
			scanner = new HoneywellScanner(config.isNormallyOn());
			break;
		case ScannerConfig.SCANNER_VENDOR_DATALOGIC:
			scanner = new DatalogicScanner(config.isNormallyOn());
			break;
		case ScannerConfig.SCANNER_VENDOR_DCSR:
			scanner = new Dcsr8001Scanner(config.isNormallyOn());
			break;
		case ScannerConfig.SCANNER_VENDOR_DEWO:
			scanner = new DewoScanner(config.isNormallyOn());
			break;
		case ScannerConfig.SCANNER_VENDOR_MOTO:
			scanner = new MotoScanner(config.isNormallyOn());
			break;
		}
		return scanner;
	}
}
