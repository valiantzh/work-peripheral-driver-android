package com.hzdongcheng.drivers.peripheral.scanner.event;

import java.util.EventListener;

/**
 * 
 * @ClassName: IScannerListener 
 * @Description: TODO(扫描枪事件监听器接口) 
 * @author Administrator 
 * @date 2018年1月26日 下午2:32:16 
 */
public interface IScannerListener extends EventListener{
	public void onNotice(ScannerEvent obj);
}
