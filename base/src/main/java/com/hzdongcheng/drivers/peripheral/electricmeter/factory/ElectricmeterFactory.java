/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ElectricmeterFactory.java   
 * @Package com.hzdongcheng.components.driver.electricmeter.factory   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年10月19日 上午10:16:38   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.electricmeter.factory;

import com.hzdongcheng.drivers.peripheral.electricmeter.IElectricmeter;
import com.hzdongcheng.drivers.peripheral.electricmeter.impl.HBElectricmeterImpl;

/** 
* @ClassName: ElectricmeterFactory 
* @Description: TODO(电表工厂类) 
* @author Jxing 
* @date 2017年10月19日 上午10:16:38 
* @version 1.0 
*/
public class ElectricmeterFactory {
	
	public static IElectricmeter createHBElectricmeter() {
		return new HBElectricmeterImpl();
	}
}
