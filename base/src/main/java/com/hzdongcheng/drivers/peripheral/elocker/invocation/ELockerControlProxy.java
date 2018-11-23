/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  ELockerControlProxy.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.Invocation   
 * @Description:    TODO(柜控制代理类)   
 * @author: Jxing     
 * @date:   2017年5月8日 上午9:02:10   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.peripheral.elocker.invocation;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.impl.MainMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.charge.IChargeViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.charge.impl.ChargeViceMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.express.IExpressViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.express.impl.ExpressViceMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.food.IFoodViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.food.impl.FoodViceMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.fresh.IFreshViceMachinControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.fresh.impl.FreshViceMachinControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.sale.ISaleViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.sale.impl.SaleViceMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.showcase.IShowcaseViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.showcase.impl.ShowcaseViceMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.wash.IWashViceMachineControl;
import com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.wash.impl.WashViceMachineControlImpl;
import com.hzdongcheng.drivers.peripheral.elocker.initialize.IELocker;
import com.hzdongcheng.drivers.peripheral.elocker.initialize.impl.ELockerImpl;
import com.hzdongcheng.drivers.aop.TransactionHandler;
import com.hzdongcheng.drivers.peripheral.thermostat.IThermostat;
import com.hzdongcheng.drivers.peripheral.thermostat.impl.ThermostatImpl;

/** 
* @ClassName: ELockerControlProxy 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author Jxing 
* @date 2017年5月8日 上午9:02:10 
* @version 1.0 
*/
public class ELockerControlProxy {
	//读写锁
	private final ReentrantLock lock = new ReentrantLock();
	//
	private final Map<String, Object> mapInterface = new HashMap<String, Object>();
	
	public ELockerControlProxy(){
		
		//elocker 
		final ELockerImpl lockerControl = new ELockerImpl();
		
		//主控柜
		final IMainMachineControl mainMachineControl = 
				new MainMachineControlImpl(lockerControl.getSerialPort());
		//普通快递柜
		final IExpressViceMachineControl expressViceMachineControl = 
				new ExpressViceMachineControlImpl(lockerControl.getSerialPort());
		//食品柜
		final IFoodViceMachineControl foodViceMachineControl = 
				new FoodViceMachineControlImpl(lockerControl.getSerialPort());
		//售卖柜
		final ISaleViceMachineControl saleViceMachineControl =
				new SaleViceMachineControlImpl(lockerControl.getSerialPort());
		//展示柜
		final IShowcaseViceMachineControl showcaseViceMachineControl = 
				new ShowcaseViceMachineControlImpl(lockerControl.getSerialPort());
		//洗衣柜
		final IWashViceMachineControl washViceMachineControl = 
				new WashViceMachineControlImpl(lockerControl.getSerialPort());
		//生鲜柜
		final IFreshViceMachinControl freshViceMachinControl = 
				new FreshViceMachinControlImpl(lockerControl.getSerialPort());
		//温控器
		final IThermostat thermostatControl = 
				new ThermostatImpl(lockerControl.getSerialPort());

		//充电柜
		final IChargeViceMachineControl chargeControl =
				new ChargeViceMachineControlImpl(lockerControl.getSerialPort());
				
		//动态代理处理器
		TransactionHandler handler           = new TransactionHandler(lockerControl, lock);
		TransactionHandler mainHandler       = new TransactionHandler(mainMachineControl, lock);
		TransactionHandler expressHandler    = new TransactionHandler(expressViceMachineControl, lock);
		TransactionHandler foodHandler       = new TransactionHandler(foodViceMachineControl, lock);
		TransactionHandler saleHandler       = new TransactionHandler(saleViceMachineControl, lock);
		TransactionHandler showcaseHandler   = new TransactionHandler(showcaseViceMachineControl, lock);
		TransactionHandler washHandler       = new TransactionHandler(washViceMachineControl, lock);
		TransactionHandler freshHandler      = new TransactionHandler(freshViceMachinControl, lock);
		TransactionHandler thermostatHandler = new TransactionHandler(thermostatControl, lock);
		TransactionHandler chargeHandler    = new TransactionHandler(chargeControl, lock);
		
		//通过代理获取指定接口
		IELocker elocker = 
				(IELocker)Proxy.newProxyInstance(handler.getClass().getClassLoader(), 
						lockerControl.getClass().getSuperclass().getInterfaces(), 
						handler);
		IMainMachineControl mainMachine = 
				(IMainMachineControl)Proxy.newProxyInstance(mainHandler.getClass().getClassLoader(), 
						mainMachineControl.getClass().getSuperclass().getInterfaces(), 
						mainHandler);
		IExpressViceMachineControl express = 
				(IExpressViceMachineControl)Proxy.newProxyInstance(expressHandler.getClass().getClassLoader(), 
						expressViceMachineControl.getClass().getInterfaces(), 
						expressHandler);
		IFoodViceMachineControl food = 
				(IFoodViceMachineControl)Proxy.newProxyInstance(foodHandler.getClass().getClassLoader(), 
						foodViceMachineControl.getClass().getInterfaces(), 
						foodHandler);
		ISaleViceMachineControl sale = 
				(ISaleViceMachineControl)Proxy.newProxyInstance(saleHandler.getClass().getClassLoader(), 
						saleViceMachineControl.getClass().getInterfaces(), 
						saleHandler);
		IShowcaseViceMachineControl showcase = 
				(IShowcaseViceMachineControl)Proxy.newProxyInstance(showcaseHandler.getClass().getClassLoader(), 
						showcaseViceMachineControl.getClass().getInterfaces(), 
						showcaseHandler);
		IWashViceMachineControl wash = 
				(IWashViceMachineControl)Proxy.newProxyInstance(washHandler.getClass().getClassLoader(), 
						washViceMachineControl.getClass().getInterfaces(), 
						washHandler);
		IFreshViceMachinControl fresh = 
				(IFreshViceMachinControl)Proxy.newProxyInstance(freshHandler.getClass().getClassLoader(), 
						freshViceMachinControl.getClass().getInterfaces(), 
						freshHandler);

		IThermostat thermostat = 
				(IThermostat)Proxy.newProxyInstance(thermostatHandler.getClass().getClassLoader(), 
						thermostatControl.getClass().getSuperclass().getInterfaces(), 
						thermostatHandler);

		IChargeViceMachineControl charge =
				(IChargeViceMachineControl)Proxy.newProxyInstance(chargeHandler.getClass().getClassLoader(),
						chargeControl.getClass().getInterfaces(),
						chargeHandler);
		//保存到Map 
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.initialize.IELocker",
				elocker);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.mainmachine.IMainMachineControl",
				mainMachine);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.express.IExpressViceMachineControl",
				express);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.food.IFoodViceMachineControl",
				food);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.sale.ISaleViceMachineControl",
				sale);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.showcase.IShowcaseViceMachineControl",
				showcase);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.wash.IWashViceMachineControl",
				wash);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.fresh.IFreshViceMachinControl",
				fresh);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.elocker.control.vicemachine.charge.IChargeViceMachineControl",
				charge);
		mapInterface.put("com.hzdongcheng.drivers.peripheral.thermostat.IThermostat",
				thermostat);

	}
	
	/**
	 * 
	* @Method Name: queryInterface 
	* @Description: TODO(动态查找接口) 
	* @param  @param name
	* @param  @return
	* @return T
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryInterface(Class<T> name){
		String key = name.getName();
		if (!mapInterface.containsKey(key))
			return null;
		return (T)mapInterface.get(key);
	}
}
