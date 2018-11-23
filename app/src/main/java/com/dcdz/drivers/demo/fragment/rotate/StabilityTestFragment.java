package com.dcdz.drivers.demo.fragment.rotate;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.demo.business.Proxy4Rotation;
import com.dcdz.drivers.utils.BaseFragment;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.DateUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.components.toolkits.utils.StringUtils;
import com.hzdongcheng.drivers.bean.BoxInfo;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StabilityTestFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_stability_times)
    EditText etTimes;
    @BindView(R.id.sp_stability_type)
    Spinner spType;
    @BindView(R.id.btn_stability_start_test)
    Button btnStart;
    @BindView(R.id.btn_stability_stop_test)
    Button btnStop;
    @BindView(R.id.btn_stability_stop_test_alter)
    Button btnStopAlter;
    @BindView(R.id.tv_stability_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_stability_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_stability_test_times)
    TextView tvTestTimes;
    @BindView(R.id.btn_stability_open_box_again_pickup)
    Button btnOpenBoxAgainPickup;
    @BindView(R.id.btn_stability_open_box_again_delivery)
    Button btnOpenBoxAgainDelivery;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    ConfigManager configManager = ConfigManager.getInstance();
    RotateConfig rotateConfig = new RotateConfig();
    private Log4jUtils log = Log4jUtils.createInstanse(this.getClass());

    private int testTimes;
    private int testType;
    int testCounter;

    private List<String> boxList;
    private List<String> randomBoxList;
    private List<String> boxIdList;
    private List<String> randomBoxIdList;
    private String testBarCode;
    private String boxNo;
    private ObservableField<String> doorNo = new ObservableField<>();
    private int currentSlaveNo;
    private int index;
    private int randomTestCounter;

    public StabilityTestFragment() {

    }

    public static StabilityTestFragment newInstance() {
        Bundle args = new Bundle();
        StabilityTestFragment fragment = new StabilityTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stability_test, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        rotateConfig = configManager.rotateConfigModel;
        currentSlaveNo = 1; //TODO
        boxList = new ArrayList<>();
        boxIdList = new ArrayList<>();
        this.buildBoxList();
        testBarCode = "test20186201338";

        final String[] testTypes = getResources().getStringArray(R.array.stability_test_type);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (testTypes[0].equals(spType.getSelectedItem().toString())) {
                    testType = 0;//顺序
                } else if (testTypes[1].equals(spType.getSelectedItem().toString())) {
                    testType = 1;//随机
                    randomBoxList = generateRandomBoxNo(boxList);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnClick({R.id.btn_stability_start_test, R.id.btn_stability_stop_test,
            R.id.btn_stability_open_box_again_delivery,R.id.btn_stability_open_box_again_pickup,
            R.id.btn_stability_start_test_alter,R.id.btn_stability_stop_test_alter})
    public void onButtonClicked(View v) {
        tvTips.setText("");
        switch (v.getId()) {
            case R.id.btn_stability_start_test:
                if (!etTimes.getText().toString().isEmpty()) {
                    testTimes = Integer.parseInt(etTimes.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "请输入测试次数", Toast.LENGTH_SHORT).show();
                }
                testCounter = 0;
                this.startTest();
                Date currentTime = new Date();
                log.debug("<稳定性测试> 开始测试时间："+ DateUtils.datetimeToString(currentTime));
                tvStartTime.setText(DateUtils.datetimeToString(currentTime));
                btnStop.setEnabled(true);
                break;
            case R.id.btn_stability_stop_test:
                this.stopTest();
                currentTime = new Date();
                log.debug("<稳定性测试> 结束测试时间："+ DateUtils.datetimeToString(currentTime));
                tvEndTime.setText(DateUtils.datetimeToString(currentTime));
                btnStop.setEnabled(false);
                break;
            case R.id.btn_stability_open_box_again_delivery:
                openBox4Delivery(testBarCode,boxNo,true);
                break;
            case R.id.btn_stability_open_box_again_pickup:
                openBox4Pickup(testBarCode,boxNo,true);
                break;
            case R.id.btn_stability_start_test_alter:
                index = 0;
                randomTestCounter = 0;
                this.buildBoxIdList();
                randomBoxIdList = generateRandomBoxNo(boxIdList);
                this.startTestAlter();
                btnStopAlter.setEnabled(true);
                break;
            case R.id.btn_stability_stop_test_alter:
                this.stopTestAlter();
                btnStopAlter.setEnabled(false);
                break;
        }
    }

    public void startTest() {
        switch (testType) {
            case 0:
                if (testCounter<testTimes) {
                    boxNo = boxList.get(testCounter);
                    if (!boxNo.substring(2, 3).equals("E")) {
                        openBox4Delivery(testBarCode, boxNo, false);
                    } else {
                        testCounter++;
                        startTest();
                    }
                }
                break;
            case 1:
                if (testCounter<testTimes) {
                    boxNo = randomBoxList.get(testCounter);
                    if (!boxNo.substring(2, 3).equals("E")) {
                        openBox4Delivery(testBarCode, boxNo, false);
                    } else {
                        testCounter++;
                        startTest();
                    }

                }
                break;
        }
    }

    public void stopTest() {
        testTimes = 0;
        Toast.makeText(getActivity(), "将在这轮操作后停止测试", Toast.LENGTH_SHORT).show();
        log.debug("<稳定性测试> 测试次数："+ testCounter);
        tvTestTimes.setText(randomTestCounter+"次");
    }

    final Handler handler = new Handler();

    final Runnable startTestRunnable = new Runnable() {
        @Override
        public void run() {
            if (index == randomBoxIdList.size()) {
                index = 0;
            }
            int boxId = Integer.parseInt(randomBoxIdList.get(index));
            try {
                Proxy4Rotation.moveShelfBox(currentSlaveNo,boxId);
                log.debug("<稳定性测试> 当前测试次数："+ randomTestCounter);
            } catch (DcdzSystemException e) {
                e.printStackTrace();
            }
            index++;
            randomTestCounter++;
            handler.postDelayed(startTestRunnable, 2000);
        }
    };

    public void startTestAlter() {
        new Thread(startTestRunnable).start();
        Toast.makeText(getActivity(), "开始测试", Toast.LENGTH_SHORT).show();
        tvTips.setText("测试中");
        Date currentTime = new Date();
        log.debug("<稳定性测试> 开始测试时间："+ DateUtils.datetimeToString(currentTime));
        tvStartTime.setText(DateUtils.datetimeToString(currentTime));
    }

    public void stopTestAlter() {
        handler.removeCallbacks(startTestRunnable);
        Toast.makeText(getActivity(), "停止测试，测试次数："+randomTestCounter+"次", Toast.LENGTH_SHORT).show();
        tvTips.setText("");
        Date currentTime = new Date();
        log.debug("<稳定性测试> 结束测试时间："+ DateUtils.datetimeToString(currentTime));
        log.debug("<稳定性测试> 测试次数："+ randomTestCounter);
        tvEndTime.setText(DateUtils.datetimeToString(currentTime));
        tvTestTimes.setText(randomTestCounter+"次");
    }

    public void buildBoxList() {
        List<ShelfDeskConfig> shelfDeskConfigList = rotateConfig.getDeskList();
        for (ShelfDeskConfig shelfDeskConfig : shelfDeskConfigList) {
            List<ShelfLayerConfig> shelfLayerConfigList = shelfDeskConfig.getLayerList();
            for (ShelfLayerConfig shelfLayerConfig : shelfLayerConfigList) {
                List<ShelfBoxConfig> shelfBoxConfigList = shelfLayerConfig.getBoxList();
                for (ShelfBoxConfig shelfBoxConfig : shelfBoxConfigList) {
                    boxList.add(shelfBoxConfig.getDisplayName());
                }
            }
        }
    }

    public void buildBoxIdList() {
        for(int i = 101;i<121;i++) {
            boxIdList.add(String.valueOf(i));
        }
    }

    public List<String> generateRandomBoxNo(List<String> boxList) {
        List<String> mBoxList = new ArrayList<>(boxList);
        Collections.shuffle(mBoxList);
        return mBoxList;
    }

    private String openBox4Delivery(final String goodsNo,final String boxNo, boolean isAgain) {
        String boxType = "";
        if(StringUtils.isEmpty(goodsNo)
                || (StringUtils.isEmpty(boxNo)&&StringUtils.isEmpty(boxType))){
            log.debug("openBox4Delivery 参数不能为空");
            return "";
        }
        doorNo.set("");
        String openBoxNo = "";
        DeviceManager.OpenBoxCallBack openBoxCallBack = getOpenBox4DeliveryCallBack(goodsNo,boxNo,isAgain);
        try {
            openBoxNo = DeviceManager.getInstance().openBox(boxNo, openBoxCallBack, 1);
            doorNo.set(DeviceManager.getInstance().getDoorName(openBoxNo));
        } catch (DcdzSystemException e) {
            log.error("openBox4Delivery fail:"+e);
        }
        return openBoxNo;
    }

    DeviceManager.OpenBoxCallBack getOpenBox4DeliveryCallBack(final String goodsNo, final String boxNo, final boolean isAgain) {
        return new DeviceManager.OpenBoxCallBack(){
            @Override
            public void onStateChanged(BoxInfo boxInfo) {
                if (boxInfo.OpenStatus == 1) {
                    int iDoorNo = boxInfo.iDoorNo + 1;
                    doorNo.set("" + (iDoorNo));
                } else {
                    if (1 == boxInfo.DoorStatus) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTips.setText("开门超时");
                            }
                        });
                    } else {
                        switch (boxInfo.GoodsStatus) {
                            case 0://箱门关闭，未检测到物品
                                if (isAgain) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnOpenBoxAgainDelivery.setVisibility(View.VISIBLE);
                                            btnOpenBoxAgainDelivery.setEnabled(true);
                                            tvTips.setText("未检测到物品");
                                        }
                                    });
                                } else {
                                    openBox4Delivery(goodsNo,boxNo,true);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvTips.setText("未检测到物品");
                                        }
                                    });
                                }
                                break;
                            case 1://箱门关闭 检测到物品 确认投递
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvTips.setText("入库完成");
                                    }
                                });
                                openBox4Pickup(goodsNo,boxNo,false);
                                break;
                            case 2://光幕被挡，再次开箱，检查货格
                            case 3:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnOpenBoxAgainDelivery.setVisibility(View.VISIBLE);
                                        btnOpenBoxAgainDelivery.setEnabled(true);
                                        tvTips.setText("光幕被挡，再次开箱");
                                    }
                                });
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFault(String errorMsg) {
                log.error("openBox4Delivery fail:"+errorMsg);
            }
        };
    }

    private boolean openBox4Pickup(final String goodsNo,final String boxNo, boolean isAgain) {
        if(StringUtils.isEmpty(goodsNo)
                || StringUtils.isEmpty(boxNo)){
            log.debug("openBox4Pickup 参数不能为空");
            return false;
        }
        doorNo.set(DeviceManager.getInstance().getDoorName(boxNo));
        DeviceManager.OpenBoxCallBack openBox4PickupCallBack = getOpenBox4PickupCallBack(goodsNo, boxNo, isAgain);
        try {
            DeviceManager.getInstance().openBox(boxNo,openBox4PickupCallBack,2);
        } catch (DcdzSystemException e) {
            log.error("openBox4DeliveryAgain fail:"+e);
            return false;
        }
        return true;
    }

    DeviceManager.OpenBoxCallBack getOpenBox4PickupCallBack(final String goodsNo,final String boxNo, final boolean isAgain) {
        return new DeviceManager.OpenBoxCallBack(){
            @Override
            public void onStateChanged(BoxInfo boxInfo) {
                if (boxInfo.OpenStatus == 1) {
                    //提示箱门已打开，取出物品
                } else {
                    if (1 == boxInfo.DoorStatus) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTips.setText("关门超时");
                            }
                        });
                    } else {
                        switch (boxInfo.GoodsStatus) {
                            case 0://取件完成
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvTips.setText("出库完成");
                                    }
                                });
                                testCounter++;
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                startTest();
                                break;
                            case 1://箱门关闭，未检测到物品
                                if (isAgain) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            btnOpenBoxAgainDelivery.setVisibility(View.VISIBLE);
                                            btnOpenBoxAgainDelivery.setEnabled(true);
                                            tvTips.setText("未检测到物品");
                                        }
                                    });
                                } else {
                                    openBox4Pickup(goodsNo,boxNo,true);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            tvTips.setText("未检测到物品");
                                        }
                                    });
                                }
                                break;
                            case 2://光幕被挡，再次开箱，检查货格
                            case 3:
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnOpenBoxAgainDelivery.setVisibility(View.VISIBLE);
                                        btnOpenBoxAgainDelivery.setEnabled(true);
                                        tvTips.setText("光幕被挡，再次开箱");
                                    }
                                });
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFault(String errorMsg) {
                log.error("openBox4Delivery fail:"+errorMsg);
            }
        };
    }
}
