package com.dcdz.drivers.demo.business;

import android.support.annotation.Nullable;

import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.components.toolkits.utils.StringUtils;
import com.hzdongcheng.drivers.bean.BoxInfo;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.exception.error.DriversErrorCode;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxStatus;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 柜机设备管理类
 * Created by Administrator on 2018/2/26.
 */

public class DeviceManager {
    private Log4jUtils log = Log4jUtils.createInstanse(this.getClass());
    private ConcurrentHashMap<String, BoxInfo> boxStatusInfoMap;
    public Set<Integer> deskNoSet;
    ConfigManager configManager = ConfigManager.getInstance();
    RotateConfig rotateConfig = new RotateConfig();

    //驱动控制代理对象
    public Proxy4Peripheral peripheralProxy;
    public Proxy4Rotation rotationProxy;
    public Proxy4Servo servoProxy;

    private ExecutorService openBoxService;//开箱服务线程

    private DeviceManager() {
        peripheralProxy = Proxy4Peripheral.getInstance();
        servoProxy = new Proxy4Servo();
        rotationProxy = new Proxy4Rotation();
        openBoxService = Executors.newSingleThreadExecutor();//开箱服务线程
        boxStatusInfoMap = new ConcurrentHashMap();
        deskNoSet = new HashSet();
    }
    private static class SingletonHolder{
        private  final static DeviceManager instance = new DeviceManager();
    }
    public static DeviceManager getInstance(){
        return SingletonHolder.instance;
    }

    //#region 格口信息
    public void addBoxInfo(BoxInfo box){
        if (box != null && StringUtils.isNotEmpty(box.BoxNo)) {
            box.ofLayer = (box.iBoxNo/100);
            box.ofCol   = (box.iBoxNo%100);
            deskNoSet.add(box.iDeskNo);
            boxStatusInfoMap.put(box.BoxNo, box);
        }
    }

    public BoxInfo getBoxInfoByNo(String boxNo) {
        return boxStatusInfoMap.get(boxNo);
    }

    public void clearBoxInfo() {
        boxStatusInfoMap.clear();
        //openBoxCallBackMap.clear();
    }

    public String getDoorName(String boxNo){
        BoxInfo box =  getBoxInfoByNo(boxNo);
        if(box!=null){
            int iDoorNo = box.ofLayer+1;
            /*if(box.iDeskNo == 0 ){
                //TODO 临时
                if(box.ofLayer>=4){
                    iDoorNo = box.ofLayer+2;
                }
            }*/
            return ""+iDoorNo;
        }
        return "";
    }

    /**
     * 加载所有箱体信息
     *
     * @return
     */
    public void loadBoxInfo() {
        clearBoxInfo();
        // 默认按箱号从小到大排序
        rotateConfig = configManager.rotateConfigModel;
        List<ShelfDeskConfig> shelfDeskConfigList = rotateConfig.getDeskList();
        if (shelfDeskConfigList != null) {
            for (ShelfDeskConfig shelfDeskConfig : shelfDeskConfigList) {
                List<ShelfLayerConfig> shelfLayerConfigList = shelfDeskConfig.getLayerList();
                int layerCount = 0;
                for (ShelfLayerConfig shelfLayerConfig : shelfLayerConfigList) {
                    List<ShelfBoxConfig> shelfBoxConfigList = shelfLayerConfig.getBoxList();
                    int boxCount = 1;
                    for (ShelfBoxConfig shelfBoxConfig : shelfBoxConfigList) {
                        BoxInfo box = new BoxInfo();
                        box.BoxNo = shelfBoxConfig.getDisplayName();
                        box.BoxName = shelfBoxConfig.getDisplayName();
                        box.iDeskNo = Integer.parseInt(shelfDeskConfig.getSlaveID());
                        box.iBoxNo = layerCount*100+boxCount;
                        box.BoxType = shelfLayerConfig.getLayerType();
                        //添加箱体信息
                        addBoxInfo(box);
                        boxCount++;
                    }
                    layerCount++;
                }

            }
        }
    }
    //#endregion

    //#region 开箱
    /**
     *
     * @param scopes
     * @param callBack
     * @param mode 1-存物开箱  2-取物开箱 3-管理开箱
     * @return
     * @throws DcdzSystemException
     */
    public String openBox(List<String> scopes, @Nullable OpenBoxCallBack callBack, int mode) throws DcdzSystemException {
        BoxInfo box = null;
        for (String no : scopes) {
            box = getBoxInfoByNo(no);
            if (box != null && box.GoodsStatus == 0 && box.OpenStatus == 0) {
                break;
            }
            box = null;
        }
        if (box == null) {
            return "";
        }
        _openBox(box, callBack, mode);
        return box.BoxNo;
    }


    /**
     *
     * @param boxNo
     * @param callBack
     * @param mode 1-存物开箱  2-取物开箱 3-管理开箱
     * @return
     * @throws DcdzSystemException
     */
    public String openBox(String boxNo, @Nullable OpenBoxCallBack callBack, int mode) throws DcdzSystemException {
        BoxInfo box = getBoxInfoByNo(boxNo);
        if (box == null) {
            return "";
        }
        _openBox(box, callBack, mode);
        return box.BoxNo;
    }

    /**
     *
     * @param box
     * @param callBack
     * @param mode  1-存物开箱  2-取物开箱 3-管理开箱
     * @throws DcdzSystemException
     */
    private void _openBox(BoxInfo box, OpenBoxCallBack callBack, int mode) throws DcdzSystemException {
        openBoxService.execute(new OpenBoxThread(box,callBack, mode));
    }

    private class OpenBoxThread implements Runnable{
        BoxInfo boxInfo ;
        OpenBoxCallBack callBack;
        byte slaveID;
        int mode;
        public OpenBoxThread(BoxInfo boxInfo, OpenBoxCallBack callBack, int mode) {
            this.boxInfo = boxInfo;
            this.callBack = callBack;
            slaveID = (byte)(boxInfo.iDeskNo+1);
            this.mode = mode;
        }
        private void openDoorCallBack(int iDoorNo, int goodsStatus){
            if(callBack != null){
                boxInfo.DoorStatus = 0;
                boxInfo.iDoorNo = iDoorNo;
                boxInfo.GoodsStatus = goodsStatus;
                boxInfo.OpenStatus = 1;
                callBack.onStateChanged(boxInfo);
            }
        }
        //开箱存取：移动到取货口，开门并等待关门
        private void openBox4Access()throws DcdzSystemException{
            String res = Proxy4Rotation.openBox4Access(slaveID, boxInfo.iBoxNo);
            ShelfBoxStatus boxStatus = JsonUtils.toBean(res, ShelfBoxStatus.class);
            if(1 != boxStatus.getOpenStatus()){//门没开
                //开门
                if(!openDoor(boxStatus.getDoorNo())){
                    //打开箱门失败，请联系管理
                    throw new DcdzSystemException(DriversErrorCode.ERR_BUSINESS_OPENDOORFAIL);
                }
            }
            //门已开，回调
            openDoorCallBack(boxStatus.getDoorNo(),boxStatus.getGoodsStatus());

            //门开，等待门关状态
            waitCloseDoor();
        }


        /**
         *开箱前检测物品
         * @param isThing true-有物允许开箱（取）  false 无物允许开箱（存）
         * @throws DcdzSystemException
         */
        private void openBoxBeforeCheckThing(boolean isThing) throws DcdzSystemException{
            String res = Proxy4Rotation.moveShelfBox(slaveID, boxInfo.iBoxNo);
            ShelfBoxStatus boxStatus = JsonUtils.toBean(res, ShelfBoxStatus.class);

            if(0 == boxStatus.getOpenStatus()){//门关
                if(isThing){
                    //有物允许开箱
                    if(1 != boxStatus.getGoodsStatus()){
                        throw new DcdzSystemException(DriversErrorCode.ERR_BUSINESS_FORBIDOPENBOX4NOTHINE);
                    }
                }else{
                    //无物允许开箱
                    if(0 != boxStatus.getGoodsStatus()){
                        throw new DcdzSystemException(DriversErrorCode.ERR_BUSINESS_FORBIDOPENBOX4THINE);
                    }
                }
            }
            //开门
            if(!openDoor(boxStatus.getDoorNo())){
                //打开箱门失败，请联系管理
                throw new DcdzSystemException(DriversErrorCode.ERR_BUSINESS_OPENDOORFAIL);
            }
            //门已开，回调
            openDoorCallBack(boxStatus.getDoorNo(), boxStatus.getGoodsStatus());

            //门开，等待门关状态
            waitCloseDoor();
        }
        //等待箱门开启
        private boolean openDoor(int doorNo) throws DcdzSystemException{
            boolean isOpen = false;
            int count =3;//开三次 开箱
            do {
                String res = Proxy4Rotation.openDoor(slaveID, doorNo);
                if("1".equals(res)){
                    isOpen = true;
                    break;
                }
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                count--;
            }while(count>0);
            return  isOpen;
        }
        //等待关门
        private void waitCloseDoor()throws DcdzSystemException{
            if(callBack == null){
                return;
            }
            //查门关状态
            int count = 120;
            do{
                try {

                    String res = Proxy4Rotation.queryBoxStatus(slaveID, boxInfo.iBoxNo);
                    ShelfBoxStatus boxStatus = JsonUtils.toBean(res, ShelfBoxStatus.class);
                    boxInfo.GoodsStatus = boxStatus.getGoodsStatus();
                    boxInfo.OpenStatus = boxStatus.getOpenStatus();
                    if(0 == boxStatus.getOpenStatus()){
                        callBack.onStateChanged(boxInfo);
                        break;
                    }
                    if(count < 1){//等待关门超时
                        boxInfo.DoorStatus = 1;
                        callBack.onStateChanged(boxInfo);
                        break;
                    }
                    Thread.sleep(1000);
                } catch (DcdzSystemException e ) {
                    e.printStackTrace();
                    log.error("waitCloseDoor:"+e.getMessage());
                } catch (Exception e){
                    e.printStackTrace();
                    log.error("waitCloseDoor:"+e.getMessage());
                }
                count --;
            }while(count>0);
        }
        @Override
        public void run() {
            log.debug("OpenBoxThread start mode="+mode+","+boxInfo.BoxNo);
            try {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {}

                //0-无  1-存物开箱  2-取物开箱 3-管理开箱
                switch (mode){
                    case 1://1-存物开箱
                        openBoxBeforeCheckThing(false);//true-有物允许开箱（取）  false 无物允许开箱（存）
                        break;
                    case 2://2-取物开箱
                        openBoxBeforeCheckThing(true);//true-有物允许开箱（取）  false 无物允许开箱（存）
                        break;
                    case 3:
                        openBox4Access();
                        break;
                    case 11://投递再次开箱
                    case 12://取件再次开箱
                    case 13://取消投递再次开箱
                        openBox4Access();
                        break;
                }
            } catch (DcdzSystemException e) {
                log.error("OpenBoxThread Error:"+e.getErrorCode());
                if(callBack != null){
                    String errorMsg = "E"+e.getErrorCode();
                    callBack.onFault(errorMsg);
                }
            }finally {
                log.debug("OpenBoxThread end mode="+mode+","+boxInfo.BoxNo);
            }

        }
    }

    public interface  OpenBoxCallBack {
        void onStateChanged(BoxInfo boxInfo);
        void onFault(String errorMsg);
    }
    //#endregion
}
