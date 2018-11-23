package com.hzdongcheng.drivers.service.aidl.impl.system;

import android.os.RemoteException;

import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.system.ISystemController;
import com.hzdongcheng.components.toolkits.exception.error.ErrorCode;
import com.hzdongcheng.components.toolkits.exception.error.ErrorTitle;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;

public class SystemController extends ISystemController.Stub {
    private Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());


    @Override
    public Result getVersions() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][系统] ==>获取系统信息");
        /*String name       = PDApplication.getContext().getResources().getString(R.string.app_name);
        String version    = "";
        String ifVersion  = PDApplication.getContext().getResources().getString(R.string.interface_version);
        String vendorName = PDApplication.getContext().getResources().getString(R.string.app_vendorName);

        try {
            PackageManager packageManager = PDApplication.getContext().getPackageManager();
            PackageInfo info = packageManager.getPackageInfo(PDApplication.getContext().getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            log4jUtils.error("[服务][系统] ==>获取系统信息 -->失败",e);
        }

        AppVersions appVersions = new AppVersions();
        appVersions.setName(name);
        appVersions.setVersion(version);
        appVersions.setIfVersion(ifVersion);
        appVersions.setVendorName(vendorName);*/

        Result result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"" );//JsonUtils.toJSONString(appVersions)
        log4jUtils.info("[服务][系统] ==>获取系统信息 -->成功 --"+ JsonUtils.toJSONString(result));
        log4jUtils.info("[服务][系统] ==>获取系统信息耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result getAssetsCode() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][系统] ==>获取资产信息");

        Result result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        log4jUtils.info("[服务][系统] ==>获取资产信息 -->成功 --"+ JsonUtils.toJSONString(result));
        log4jUtils.info("[服务][系统] ==>获取资产信息耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result getPeripheralsInfo() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][系统] ==>获取外设信息");
        String result = "";
        /*Map<String, ConfigModel> map = ConfigFilesHelper.loadObjectFromDeviceConfigFile();
        JSONArray jsonArray = new JSONArray();
        if (map != null && map.size() > 0){
            for(Map.Entry<String, ConfigModel> entry : map.entrySet()){
                String key = entry.getKey();
                ConfigModel value = entry.getValue();

                JSONObject jsonObject = new JSONObject();
                if (CONFIG_NAME_ELOCKER.equalsIgnoreCase(key)){
                    ELockerConfigModel model = (ELockerConfigModel)value;
                    JSONObject tmpJsonObject = new JSONObject();
                    tmpJsonObject.put("portName", model.getPortName());
                    jsonObject.put(CONFIG_NAME_ELOCKER,tmpJsonObject);
                    jsonArray.add(jsonObject);
                }

                if (CONFIG_NAME_SCANNER.equalsIgnoreCase(key)){
                    ScannerConfigModel model = (ScannerConfigModel)value;
                    JSONObject tmpJsonObject = new JSONObject();
                    tmpJsonObject.put("portName", model.getPortName());
                    jsonObject.put(CONFIG_NAME_SCANNER,tmpJsonObject);
                    jsonArray.add(jsonObject);
                }
            }
            result = jsonArray.toString();
        }*/

        Result result1 = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),result);
        log4jUtils.info("[服务][系统] ==>获取外设信息 -->成功 --"+ JsonUtils.toJSONString(result1));
        log4jUtils.info("[服务][系统] ==>获取外设信息耗时 -->" + (System.currentTimeMillis() - start));
        return result1;
    }

    @Override
    public Result reboot(int delayMillis) throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][系统] ==>重启系统");
        if(delayMillis >0 && delayMillis <2000){
            delayMillis = 2000;
        }

        Result result = new Result(ErrorCode.ERR_INVALIDARGUMENT, ErrorTitle.getErrorTitle(ErrorCode.ERR_INVALIDARGUMENT),"");

        /*String armVendorName = DeviceConfigModel.getInstance().ipcConfigModel.getVendorName();
        if (Constants.IPC_VENDOR_WEISHENG.equalsIgnoreCase(armVendorName) ||
                Constants.IPC_VENDOR_RUIXUN.equalsIgnoreCase(armVendorName)){

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis() + delayMillis);
            String strCurrTime = formatter.format(curDate);

            log4jUtils.info("The system will be reboot at " + strCurrTime);

            Intent it = new Intent();
            it.setAction("android.intent.action.auto_power_shut");//Intent.ACTION_REBOOT
            it.putExtra("effective", true);
            it.putExtra("power_type", delayMillis <= 0 ? 1 : 4);//4定时重启 1马上重启
            it.putExtra("power_time", strCurrTime);
            it.putExtra("shut_time", strCurrTime);
            PDApplication.getContext().sendBroadcast(it);

            result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");
        }*/
        log4jUtils.info("[服务][系统] ==>重启系统 -->成功 --"+ JsonUtils.toJSONString(result));
        log4jUtils.info("[服务][系统] ==>重启系统耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }

    @Override
    public Result getNetworkInfo() throws RemoteException {
        long start = System.currentTimeMillis();
        log4jUtils.info("[服务][系统] ==>获取网络信息");
        /*String operator = NetworkInformation.getInstance().getNetworkOperator();
        String netWorkType = NetworkInformation.getInstance().getNetworkType();
        int strength = NetworkInformation.getInstance().getSignalStrength(netWorkType);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("operator", operator);
        jsonObject.put("networkType", netWorkType);
        jsonObject.put("strength", strength);*/

        Result result = new Result(ErrorCode.SUCCESS, ErrorTitle.getErrorTitle(ErrorCode.SUCCESS),"");//jsonObject.toJSONString()
        log4jUtils.info("[服务][系统] ==>获取网络信息 -->成功 --"+ JsonUtils.toJSONString(result));
        log4jUtils.info("[服务][系统] ==>获取网络信息耗时 -->" + (System.currentTimeMillis() - start));
        return result;
    }
}
