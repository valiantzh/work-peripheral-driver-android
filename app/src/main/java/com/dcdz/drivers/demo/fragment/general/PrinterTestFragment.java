package com.dcdz.drivers.demo.fragment.general;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.dcdz.drivers.R;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.utils.BaseFragment;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * 打印机测试
 */
public class PrinterTestFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_print_message)
    EditText etPrintMessage;
    @BindView(R.id.tv_command)
    TextView tvCommand;
    @BindView(R.id.tv_port)
    TextView tvPort;
    @BindView(R.id.tv_vendor)
    TextView tvVendor;

    DeviceManager deviceManager = DeviceManager.getInstance();
    ConfigManager configManager = ConfigManager.getInstance();

    public PrinterTestFragment() {
        // Required empty public constructor
    }

    public static PrinterTestFragment newInstance() {
        Bundle args = new Bundle();
        PrinterTestFragment fragment = new PrinterTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
        tvPort.setText(configManager.printerConfig.getPortName());
        tvVendor.setText(configManager.printerConfig.getVendorName());
    }

    @OnClick({R.id.btn_print,R.id.btn_send_command})
    public void clickBtn(View view){
        switch (view.getId()) {
            case R.id.btn_print://打印
                try {
                    deviceManager.peripheralProxy.print(etPrintMessage.getText().toString());
                } catch (DcdzSystemException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_send_command://命令发送
                getPrinterStatus();
                break;
        }
    }

/*    private int getCutPaperMode(){
        int mode = 0;
        mode = spSwitchPaperMode.getSelectedItemPosition();
        Log.d("mode",String.valueOf(mode));
        return mode;
    }*/

    private void getPrinterStatus(){
        String cutterStatusStr = "";
        String pater1StatusStr = "";
        String pater2StatusStr = "";
        String pater3StatusStr = "";
        try {
            String printerStatus = deviceManager.peripheralProxy.queryPrinterStatus();
            Log.d("PrintStatus",printerStatus);
            com.alibaba.fastjson.JSONObject printerStatusJson = JsonUtils.toJSONObject(printerStatus);
            boolean cutterStatus = printerStatusJson.getBoolean("cutterStatus");//getJSONObject("data").
            boolean pater1Status = printerStatusJson.getBoolean("pater1Status");//getJSONObject("data").
            boolean pater2Status = printerStatusJson.getBoolean("pater2Status");//getJSONObject("data").
            boolean pater3Status = printerStatusJson.getBoolean("pater3Status");//getJSONObject("data").

            if(cutterStatus){
                cutterStatusStr = "锁打开";
            } else {
                cutterStatusStr = "锁关闭";
            }

            if(pater1Status){
                pater1StatusStr = "纸将尽";
            } else {
                pater1StatusStr = "纸充足";
            }

            if(pater2Status){
                pater2StatusStr = "未取";
            } else {
                pater2StatusStr = "已取";
            }

            if(pater3Status){
                pater3StatusStr = "缺纸";
            } else {
                pater3StatusStr = "有纸";
            }
        } catch (DcdzSystemException e) {
            e.printStackTrace();
            //TODO 异常处理
        }

        String printerStatusStr = "切刀锁状态: "+cutterStatusStr
                +" 纸将尽状态: "+pater1StatusStr
                +" 纸未取状态: "+pater2StatusStr
                +" 缺纸状态: "+pater3StatusStr;
        tvCommand.setText(printerStatusStr);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
