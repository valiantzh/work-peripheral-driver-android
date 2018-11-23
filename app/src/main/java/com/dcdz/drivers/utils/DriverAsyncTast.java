package com.dcdz.drivers.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.dcdz.drivers.demo.business.Proxy4Elocker;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shalom on 2018/01/14.
 */

public class DriverAsyncTast extends AsyncTask<String, Integer, String> {

    private ProgressDialog dialog = null;
    Log4jUtils log = Log4jUtils.createInstanse(DriverAsyncTast.class);
    public Activity activity;
    public List<String> boxNos = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    private static DriverAsyncTast instance = null;
    private ConfigManager configManager = ConfigManager.getInstance();
    private Proxy4Elocker proxy4Elocker = Proxy4Elocker.getInstance();


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setTitle("开箱");
        dialog.setMessage("请稍等...");
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "取消", new SureButtonListener());
        dialog.show();
    }

    //Dialog中确定按钮的监听器
    private class SureButtonListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            onPostExecute("");
        }
    }

    @Override
    protected String doInBackground(String... voids) {
        String ret = "";
        for (final String boxNo : boxNos) {
            if (isCancelled()) {
                break;
            }
            try {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setMessage("正在打开" + boxNo + "号箱，请稍等...");
                    }
                });
                proxy4Elocker.openBoxByName(boxNo);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.setMessage( boxNo + "号箱已打开，请关好箱门...");
                    }
                });
                Thread.sleep(200);
                if (boxNos.size()>1) {
                    while (true) {
                        String result = proxy4Elocker.queryBoxStatusByName(boxNo);
                        BoxStatus boxStatus = JsonUtils.toBean(result, BoxStatus.class);
                        if (boxStatus.getOpenStatus() == 0) {
                            break;
                        }
                        Thread.sleep(200);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (DcdzSystemException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
        this.cancel(true);
    }
}