/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ELockerControlHelper.java   
 * @Package com.hzdongcheng.drivers.peripheral.elocker.helper
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午3:59:05   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.helper;

import com.hzdongcheng.drivers.peripheral.elocker.invocation.ELockerControlProxy;

/** 
* @ClassName: ELockerControlHelper 
* @Description: TODO(ELocker控制帮助类) 
* @author Jxing 
* @date 2017年4月12日 下午3:59:05 
* @version 1.0 
*/
public final class ELockerControlHelper {
	
	public static ELockerControlProxy createInstance(){
		return new ELockerControlProxy();
	}
}
