package com.hzdongcheng.drivers.base.modbus.helper;

import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.base.modbus.ModbusBase;
import com.hzdongcheng.drivers.base.modbus.exception.ModbusException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/4/17.
 */

public class ModbusHelper {
    private static final Log4jUtils log = Log4jUtils.createInstanse(ModbusHelper.class);
    private static long CMD_GAP_MS = 100;//命令发送的时间间隔
    private final static int READ_CMD_GAP_MS = 500;//状态查询间隔 ms
    private ReentrantLock lock = new ReentrantLock();
    private ModbusBase modbusBase;
    private ConcurrentHashMap<Byte,CmdChar> slaveMonitorMap;
    private MonitorTask readTask = new MonitorTask();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future = null;

    private ModbusHelper(){
        slaveMonitorMap = new ConcurrentHashMap(10);
    }
    private static class SingletonHolder {
        private static final ModbusHelper instance = new ModbusHelper();
    }
    public static ModbusHelper getInstance(){
        return SingletonHolder.instance;
    }

    public void setModbusBase(ModbusBase modbusBase) {
        this.modbusBase = modbusBase;
    }

    /**
     *
     * @param slaveID 从站编号 1~
     * @param rAddress
     * @param rLength
     * @return
     */
    public CmdChar readFast(byte slaveID, int rAddress, int rLength){
        synchronized (ModbusHelper.this){
            if(slaveMonitorMap.containsKey(slaveID)) {
                return slaveMonitorMap.get(slaveID);
            }
        }
        return _read(slaveID, rAddress, rLength);
    }

    /**
     *
     * @param slaveID 从站编号 1~
     * @param rAddress
     * @param rLength
     * @return
     */
    public CmdChar read(byte slaveID, int rAddress, int rLength){
        return _read(slaveID, rAddress, rLength);
    }

    /**
     *
     * @param slaveID 从站编号 1~
     * @param rAddress
     * @param rLength
     * @return
     */
    private CmdChar _read(byte slaveID, int rAddress, int rLength){
        CmdChar cmdChar = new CmdChar(rAddress, rLength);
        int rc = -1 ;
        lock.lock();
        try{
            rc = modbusBase.read(slaveID, cmdChar.getStartAddr(), cmdChar.getLength(), cmdChar.getCmdChars());
        }catch (ModbusException e){
            log.error("slaveID="+slaveID+",modbus read error:"+e.getMessage(),e);
        }finally {
            lock.unlock();
        }
        if(rc == 0){
            synchronized (ModbusHelper.this){
                slaveMonitorMap.put(slaveID, cmdChar);
            }
        }else{
            log.error("slaveID="+slaveID+",modbus read error. rc="+rc);
        }
        return cmdChar;
    }

    /**
     *
     * @param slaveID 从站编号 1~
     * @param cmdChar
     * @return
     * @throws ModbusException
     */
    public int write(byte slaveID, CmdChar cmdChar) throws  ModbusException{
        int rc = 0;
        lock.lock();
        try {
            Thread.sleep(CMD_GAP_MS);
            rc = modbusBase.write(slaveID,cmdChar.getStartAddr(), cmdChar.getLength(),cmdChar.getCmdChars());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return rc;
    }

    public synchronized boolean startMonitorTask(){
        boolean bSuccess = true;

        if (future != null) {
            future.cancel(true);
        }
        try {
            //future = service.scheduleWithFixedDelay(readTask, 0, 1, TimeUnit.SECONDS);
            future = service.scheduleWithFixedDelay(readTask, 0, READ_CMD_GAP_MS, TimeUnit.MILLISECONDS);//500ms
        } catch (Exception e){
            bSuccess = false;
            log.error(e.getMessage());
        }
        return bSuccess;
    }

    public synchronized void stopMonitorTask(){

        if (future != null){
            try {
                future.cancel(true);
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }
    private class MonitorTask implements Runnable {

        @Override
        public void run() {

            if(modbusBase==null){
                return;
            }
            if(slaveMonitorMap.isEmpty()){
                return;
            }

            //从站状态监测
            for(Byte slaveID: slaveMonitorMap.keySet()){
                CmdChar cmdChar = slaveMonitorMap.get(slaveID);
                if(cmdChar.getLength() > 0){
                    _read(slaveID, cmdChar.getStartAddr(), cmdChar.getLength());
                    //int rc = modbusBase.read(slaveID, cmdChar.getStartAddr(), cmdChar.getLength(), cmdChar.getCmdChars());
                    //log.info("Read task is started.slaveID="+slaveID+",addr="+cmdChar.getStartAddr()+",len="+cmdChar.getLength());
                }else{
                    slaveMonitorMap.remove(slaveID);
                }
            }
        }
    }
}
