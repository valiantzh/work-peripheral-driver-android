package com.hzdongcheng.drivers.peripheral.scanner.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.hzdongcheng.drivers.peripheral.scanner.IScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.DatalogicScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.DewoScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.HoneywellScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.MotoScanner;
import com.hzdongcheng.drivers.peripheral.scanner.impl.NewWorldScanner;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.components.toolkits.utils.StringUtils;

/**
 * 
 * @ClassName: ScannerHelper 
 * @Description: 扫描枪控制帮助类，用于自动识别扫描枪类型
 * @author Administrator 
 * @date 2018年1月26日 下午4:44:07 
 */
public class ScannerHelper {
	
	private static Log4jUtils logger = Log4jUtils.createInstanse(ScannerHelper.class);
	
	public static IScanner findScanner(final String portName, boolean normallyMode){
		IScanner scanner = null;
		String scannerNameString = searchScanner(portName);
		if (StringUtils.isNotEmpty(scannerNameString)){
			try {
				Constructor<?> con = Class.forName(scannerNameString).getConstructor(boolean.class);
				scanner = (IScanner)con.newInstance(normallyMode);

			} catch (NoSuchMethodException | SecurityException
					| ClassNotFoundException | InstantiationException 
					| IllegalAccessException | IllegalArgumentException 
					| InvocationTargetException e) {
				logger.error(e.getMessage());
			}
		}

		return scanner;
	}
	
	private static String searchScanner(String portName){
		
		String version   = "";
		String className = "";
		
		{
			NewWorldScanner newWorldScanner = new NewWorldScanner(false);

			try {
				newWorldScanner.open(portName);

				try {
					version = newWorldScanner.searchVersion();
					if ( StringUtils.isNotEmpty(version) ){
						className = newWorldScanner.getClass().getName();
					}
				} finally {
					newWorldScanner.close();
				}

			} catch (DcdzSystemException e) {
				logger.error(e.getMessage());
			}

			if ( StringUtils.isNotEmpty(className) ){
				return className;
			}
		}
		{
			HoneywellScanner honeywellScanner = new HoneywellScanner(false);
			
			try {
				honeywellScanner.open(portName);
				
				try {
					version = honeywellScanner.searchVersion();
					if ( StringUtils.isNotEmpty(version) ){
						className = honeywellScanner.getClass().getName();
					}
				}finally{
					honeywellScanner.close();
				}
			} catch (DcdzSystemException e) {
				logger.error(e.getMessage());
			}
			
			if ( StringUtils.isNotEmpty(className) ){
				return className;
			}
		}
		{
			DatalogicScanner datalogicScanner = new DatalogicScanner(false);
			try {
				datalogicScanner.open(portName);
				
				try {
					version = datalogicScanner.searchVersion();
					if ( StringUtils.isNotEmpty(version) ){
						className = datalogicScanner.getClass().getName();
					}
				}finally{
					datalogicScanner.close();
				}
				
			} catch (DcdzSystemException e) {
				logger.error(e.getMessage());
			}
			
			if ( StringUtils.isNotEmpty(className) ){
				return className;
			}
		}
		
		{
			MotoScanner motoScanner = new MotoScanner(false);

			try {
				motoScanner.open(portName);

				try {
					version = motoScanner.searchVersion();
					if ( StringUtils.isNotEmpty(version) ){
						className = motoScanner.getClass().getName();
					}
				} finally {
					motoScanner.close();
				}

			} catch (DcdzSystemException e) {
				logger.error(e.getMessage());
			}

			if ( StringUtils.isNotEmpty(className) ){
				return className;
			}
		}

		{
			DewoScanner dewoScanner = new DewoScanner(false);
			try {
				dewoScanner.open(portName);
				
				try {
					version = dewoScanner.searchVersion();
					if ( StringUtils.isNotEmpty(version) ){
						className = dewoScanner.getClass().getName();
					}
				}finally{
					dewoScanner.close();
				}
				
			} catch (DcdzSystemException e) {
				logger.error(e.getMessage());
			}
			
			if ( StringUtils.isNotEmpty(className) ){
				return className;
			}
		}
		
		return className;
	}
}
