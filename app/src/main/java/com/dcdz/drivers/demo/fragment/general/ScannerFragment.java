package com.dcdz.drivers.demo.fragment.general;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.DialogEvent;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.demo.business.IMessageListener;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.Dictionary;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ScannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 * 扫描枪
 */
public class ScannerFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.text_port)
    TextView textPort;
    @BindView(R.id.text_vendor)
    TextView textVendor;
    @BindView(R.id.text_model)
    TextView textModel;
    @BindView(R.id.toggle_scanning)
    ToggleButton toggleScanning;
    @BindView(R.id.toggle_barcode)
    ToggleButton toggleBarcode;
    @BindView(R.id.toggle_qr_code)
    ToggleButton toggleQrCode;
    @BindView(R.id.rg_scan_times)
    RadioGroup rgScanTimes;

    @BindView(R.id.text_scanner_message)
    TextView textScannerMessage;
    @BindView(R.id.toggle_model)
    ToggleButton toggleModel;


    //private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    //private ScheduledFuture<?> future = null;

    private ConfigManager configManager = ConfigManager.getInstance();
    private DeviceManager deviceManager = DeviceManager.getInstance();

    public ScannerFragment() {
        // Required empty public constructor
    }

    public static ScannerFragment newInstance() {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            closeScanner();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeScanner();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
        textPort.setText(configManager.scannerConfig.getPortName());
        textVendor.setText(configManager.scannerConfig.getVendorName());
        if (configManager.scannerConfig.isEnabled())
            textModel.setText("常亮模式");
        else
            textModel.setText("触发模式");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeScanner();
        unbinder.unbind();
    }

    @OnClick({R.id.toggle_scanning, R.id.toggle_barcode, R.id.toggle_qr_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toggle_scanning:
                if (toggleScanning.isChecked()) {
                    try {
                        deviceManager.peripheralProxy.initScanner(receiveMessage, toggleModel.isChecked());
                        Dictionary.toastInfo(getActivity(), "开启扫码成功" , Toast.LENGTH_SHORT);
                    } catch (DcdzSystemException e) {
                        //e.printStackTrace();
                        Dictionary.toastInfo(getActivity(), "开启扫码失败：" + e.getMessage(), Toast.LENGTH_SHORT);
                    }
                } else {
                    closeScanner();
                }
                break;
            case R.id.toggle_barcode:
                try {
                    deviceManager.peripheralProxy.toggleBarcode(toggleBarcode.isChecked());
                    Dictionary.toastInfo(getActivity(), "条码切换成功" , Toast.LENGTH_SHORT);
                } catch (DcdzSystemException e) {
                    e.printStackTrace();
                    Dictionary.toastInfo(getActivity(), "条码切换失败" + e.getMessage(), Toast.LENGTH_SHORT);
                }

                break;
            case R.id.toggle_qr_code:
                try {
                    deviceManager.peripheralProxy.toggleQRCode(toggleQrCode.isChecked());
                    Dictionary.toastInfo(getActivity(), "二维码切换成功" , Toast.LENGTH_SHORT);
                } catch (DcdzSystemException e) {
                    e.printStackTrace();
                    Dictionary.toastInfo(getActivity(), "二维码切换失败" + e.getMessage(), Toast.LENGTH_SHORT);
                }
                break;
        }
    }
    private int msgCount =0;
    List<String> messages = new ArrayList<>(11);
    private IMessageListener receiveMessage = new IMessageListener() {
        @Override
        public void onMessage(final String message) {
            msgCount += 1;
            messages.add(0, msgCount+":  "+message);
            if (messages.size() > 10) {
                messages.remove(messages.size() - 1);
            }
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textScannerMessage.setText(StringUtils.join(messages.toArray(), "\r\n"));
                }
            });

        }
    };

    boolean doubleFlag = false;
    Handler handler = new Handler();

    @OnClick(R.id.text_scanner_message)
    public void onViewClicked() {
        if (doubleFlag) {
            textScannerMessage.setText("");
            messages.clear();
        } else {
            doubleFlag = true;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleFlag = false;
                }
            }, 800);
        }
    }

    @OnClick(R.id.toggle_model)
    public void onModelClicked() {
        EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, true));
        try {
            deviceManager.peripheralProxy.toggleMode(toggleModel.isChecked());
            Dictionary.toastInfo(getActivity(), "模式切换成功" , Toast.LENGTH_SHORT);
        } catch (DcdzSystemException e) {
            e.printStackTrace();
            Dictionary.toastInfo(getActivity(), "模式切换失败：" + e.getMessage(), Toast.LENGTH_SHORT);
        } finally {
            EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, false));
        }
    }
    //关闭扫码
    private void closeScanner() {
        try {
            deviceManager.peripheralProxy.closeScanner();
        } catch (DcdzSystemException e) {
            //e.printStackTrace();
            Dictionary.toastInfo(getActivity(), "关闭扫码失败：" + e.getMessage(), Toast.LENGTH_SHORT);
        }
        if(toggleScanning!=null){
            toggleScanning.setChecked(false);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void configNotification(Boolean modified) {
        initView(null, null);
    }
}
