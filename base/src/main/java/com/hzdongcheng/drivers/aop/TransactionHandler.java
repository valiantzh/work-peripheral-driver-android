/**
 * All rights Reserved, Designed By Android_Robot   
 * @Title:  TransactionHandler.java   
 * @Package com.dcdzsoft.drivers.controller.elocker.proxy   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: Jxing     
 * @date:   2017年4月12日 下午2:55:52   
 * @version V1.0     
 */
package com.hzdongcheng.drivers.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import com.hzdongcheng.components.toolkits.utils.Log4jUtils;

/**
 * @ClassName: TransactionHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jxing
 * @date 2017年4月12日 下午2:55:52
 * @version 1.0
 */
public class TransactionHandler implements InvocationHandler {

	// 默认指令调用间隔时长
	protected final int MAX_DEFAULT_SLEEP = 40;

	private static final Log4jUtils logger = Log4jUtils.createInstanse(TransactionHandler.class);

	private Object target = null;
	private ReentrantLock lock = null;

	public TransactionHandler(Object target, ReentrantLock lock) {
		this.target = target;
		this.lock = lock;
	}

	/*
	 * (non Javadoc) <p>Title: invoke</p> <p>Description: </p>
	 * 
	 * @param proxy
	 * 
	 * @param method
	 * 
	 * @param args
	 * 
	 * @return
	 * 
	 * @throws Throwable
	 * 
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Object result = null;
		lock.lock();
		logger.info("[elocker] ==" + target.getClass().getSimpleName() + ":" + method.getName() + "参数："
				+ Arrays.toString(args) + "  calling ==");
		try {
			Thread.sleep(MAX_DEFAULT_SLEEP);
			result = method.invoke(target, args);
			logger.info("[elocker] ==" + method.getName() + "  call is successful==");
		} catch (IllegalArgumentException | InterruptedException ex) {
			logger.error("[elocker] ==" + "Thread.sleep(" + MAX_DEFAULT_SLEEP + ") exception. -->" + ex.getMessage());
		} catch (Exception ex) {
			logger.error("[elocker] ==" + method.getName() + " call failed==");
			throw ex.getCause();
		} finally {
			lock.unlock();
		}

		return result;
	}

}
