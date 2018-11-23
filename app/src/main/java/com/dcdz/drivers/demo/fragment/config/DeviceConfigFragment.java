package com.dcdz.drivers.demo.fragment.config;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.ServiceHelper;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.Dictionary;
import com.hzdongcheng.drivers.peripheral.scanner.model.ScannerConfig;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * 接口配置
 */
public class DeviceConfigFragment extends BaseFragment {

    @BindView(R.id.save_button)
    Button saveButton;
    Unbinder unbinder;

    @BindView(R.id.checkBox_engine)
    CheckBox checkBoxEngine;
    @BindView(R.id.sp_engine_port)
    Spinner spEnginePort;

    @BindView(R.id.checkBox_scanner)
    CheckBox checkBoxScanner;
    @BindView(R.id.sp_scanner_port)
    Spinner spScannerPort;
    @BindView(R.id.sp_scanner_vendor)
    Spinner spScannerVendor;
    @BindView(R.id.sp_scanner_model)
    Spinner spScannerModel;

    @BindView(R.id.checkBox_printer)
    CheckBox checkBoxPrinter;
    @BindView(R.id.sp_print_port)
    Spinner spPrintPort;

    @BindView(R.id.checkBox_card_read)
    CheckBox checkBoxCardReader;
    @BindView(R.id.sp_card_read_port)
    Spinner spCardReadPort;

    @BindView(R.id.checkBox_iccard_send)
    CheckBox checkBoxCardSender;
    @BindView(R.id.sp_iccard_send)
    Spinner spCardSenderPort;
    @BindView(R.id.checkBox_simcard_send)
    CheckBox checkBoxSimCardSender;
    @BindView(R.id.sp_simcard_send)
    Spinner spSimCardSenderPort;

    @BindView(R.id.checkBox_rotate_servo)
    CheckBox checkBoxRotateServo;
    @BindView(R.id.sp_rotate_servo)
    Spinner spRotateServoPort;


    private ConfigManager configManager = ConfigManager.getInstance();

    public static DeviceConfigFragment newInstance() {
        DeviceConfigFragment fragment = new DeviceConfigFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        initDeskEdit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_config, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.save_button)
    public void onViewClicked() {

        //保存配置信息
        configManager.scannerConfig.setPortName(spScannerPort.getSelectedItem().toString());
        configManager.scannerConfig.setVendorName(spScannerVendor.getSelectedItem().toString());
        configManager.cardReaderConfig.setPortName(spCardReadPort.getSelectedItem().toString());
        configManager.cardSenderConfig.setPortName(spCardSenderPort.getSelectedItem().toString());
        configManager.simCardSenderConfig.setPortName(spSimCardSenderPort.getSelectedItem().toString());
        configManager.printerConfig.setPortName(spPrintPort.getSelectedItem().toString());
        configManager.rotateConfigModel.setPortName(spRotateServoPort.getSelectedItem().toString());
        //configManager.eLockerConfigModel.setPortName(spEnginePort.getSelectedItem().toString());  //TODO disabled for rotate machine
        //configManager.cardReaderConfig.setVendorName();

        configManager.scannerConfig.setEnabled(checkBoxScanner.isChecked());
        configManager.cardReaderConfig.setEnabled(checkBoxCardReader.isChecked());
        configManager.printerConfig.setEnabled(checkBoxPrinter.isChecked());
        configManager.cardSenderConfig.setEnabled(checkBoxCardSender.isChecked());
        configManager.simCardSenderConfig.setEnabled(checkBoxSimCardSender.isChecked());
        configManager.rotateConfigModel.setEnabled(checkBoxRotateServo.isChecked());
        if ("常亮模式".equals(spScannerModel.getSelectedItem()))
            configManager.scannerConfig.setNormallyOn(true);
        else
            configManager.scannerConfig.setNormallyOn(false);


        //保存配置
        configManager.createOrUpdateDeviceConfigFile();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ServiceHelper.getInstance().unBindService();
                ServiceHelper.getInstance().bindService(getActivity());
            }
        });
        Dictionary.toastInfo(getActivity(), "保存成功", Toast.LENGTH_LONG);
        EventBus.getDefault().post(true);
    }

    private void initDeskEdit() {
        //spEnginePort.setSelection(Dictionary.portMap.get(configManager.eLockerConfigModel.getPortName()), true);  //TODO disabled for rotate machine
        spScannerPort.setSelection(Dictionary.portMap.get(configManager.scannerConfig.getPortName()), true);
        spPrintPort.setSelection(Dictionary.portMap.get(configManager.printerConfig.getPortName()), true);
        spCardReadPort.setSelection(Dictionary.portMap.get(configManager.cardReaderConfig.getPortName()), true);
        spCardSenderPort.setSelection(Dictionary.portMap.get(configManager.cardSenderConfig.getPortName()), true);
        spSimCardSenderPort.setSelection(Dictionary.portMap.get(configManager.simCardSenderConfig.getPortName()), true);
        spRotateServoPort.setSelection(Dictionary.portMap.get(configManager.rotateConfigModel.getPortName()),true);

        checkBoxScanner.setChecked(configManager.scannerConfig.isEnabled());
        checkBoxCardReader.setChecked(configManager.cardReaderConfig.isEnabled());
        checkBoxPrinter.setChecked(configManager.printerConfig.isEnabled());
        checkBoxCardSender.setChecked(configManager.cardSenderConfig.isEnabled());
        checkBoxSimCardSender.setChecked(configManager.simCardSenderConfig.isEnabled());
        checkBoxRotateServo.setChecked(configManager.rotateConfigModel.isEnabled());

        if (configManager.scannerConfig.isNormallyOn())
            spScannerModel.setSelection(0, true);
        else
            spScannerModel.setSelection(1, true);

        switch (configManager.scannerConfig.getVendorName()) {
            case ScannerConfig.SCANNER_VENDOR_NEWLAND:
                spScannerVendor.setSelection(0, true);
                break;
            case ScannerConfig.SCANNER_VENDOR_HONEYWELL:
                spScannerVendor.setSelection(1, true);
                break;
            case ScannerConfig.SCANNER_VENDOR_DCSR:
                spScannerVendor.setSelection(2, true);
                break;
        }
    }
}
