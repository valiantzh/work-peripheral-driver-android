package com.dcdz.drivers.demo.fragment.rotate;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.adapter.AnotherRotationControlListViewAdapter;
import com.dcdz.drivers.demo.adapter.RotationControlListViewAdapter;
import com.dcdz.drivers.demo.business.Proxy4Rotation;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.Constant;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * 旋转柜控制
 */
public class RotationControlFragment extends BaseFragment {

    @BindView(R.id.rg_rotation_control)
    RadioGroup rgRotationControl;
    @BindView(R.id.btn_rotation_control_system_init)
    Button btnSystemInit;
    @BindView(R.id.btn_rotation_control_system_reset)
    Button btnSystemReset;
    @BindView(R.id.tv_rotation_control_system_status)
    TextView tvSystemStatus;
    @BindView(R.id.tv_rotation_control_auto_door_status)
    TextView tvAutoDoorStatus;
    @BindView(R.id.tv_rotation_control_current_box_num)
    TextView tvCurrentBoxNum;
    @BindView(R.id.tv_rotation_control_manual_door_status)
    TextView tvManualDoorStatus;
    @BindView(R.id.tv_rotation_control_moving_status)
    TextView tvMovingStatus;
    @BindView(R.id.tv_rotation_control_power_supply)
    TextView tvPowerSupply;
    @BindView(R.id.tv_rotation_control_raster_status)
    TextView tvRasterStatus;
    @BindView(R.id.tv_rotation_control_repair_door_status)
    TextView tvRepairDoorStatus;
    @BindView(R.id.tv_rotation_control_warning_info)
    TextView tvWarningInfo;
    @BindView(R.id.btn_rotation_control_open_repair_door)
    Button btnOpenRepairDoor;
    @BindView(R.id.lv_rotation_control_list)
    ListView lvRotationControlList;
    @BindView(R.id.lv_rotation_control_list_another)
    ListView lvRotationControlListAnother;



    Unbinder unbinder;

    ConfigManager configManager = ConfigManager.getInstance();
    RotateConfig rotateConfig = configManager.rotateConfigModel;

    List<ShelfDeskConfig> tbDesks = new ArrayList<>(); //货架
    List<ShelfLayerConfig> shelfLayers = new ArrayList<>(); //层
    RotationControlListViewAdapter adapter;
    AnotherRotationControlListViewAdapter aAdapter;


    int currentSelectedDesk = 0;
    int shelfCount = 5;
    int deskSize = 0;

    public RotationControlFragment() {
        // Required empty public constructor
    }

    public static RotationControlFragment newInstance() {
        Bundle args = new Bundle();
        RotationControlFragment fragment = new RotationControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rotation_control, container, false);
        unbinder = ButterKnife.bind(this, view);
        statusUpdateHandler = new StatusUpdateHandler(this);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        tbDesks = rotateConfig.getDeskList();
        deskSize = tbDesks.size();
        shelfCount = rotateConfig.getDeskList().get(currentSelectedDesk).getLayerList().size();
        if(!rotateConfig.getDeskList().isEmpty()){
            shelfLayers = rotateConfig.getDeskList().get(currentSelectedDesk).getLayerList();
        }
        adapter = new RotationControlListViewAdapter(getActivity(),shelfCount,shelfLayers,currentSelectedDesk+1);
        lvRotationControlList.setAdapter(adapter);

        //货位号+操作
        aAdapter = new AnotherRotationControlListViewAdapter(getActivity(),shelfCount,shelfLayers,currentSelectedDesk+1);
        lvRotationControlListAnother.setAdapter(aAdapter);


        generateDeskGroup();

        //查询柜机状态
        Thread inspector = new Thread(runnable);
        inspector.start();
    }

    public void generateDeskGroup(){
        int indexFirst = View.generateViewId();
        for(int i = 0;i<deskSize;i++){
            final RadioButton button = new RadioButton(getActivity());
            if(i == 0){
                button.setId(indexFirst);
            }
            button.setWidth(120);
            button.setHeight(50);
            button.setBackgroundResource(R.drawable.selector_radio_config);
            button.setGravity(View.TEXT_ALIGNMENT_CENTER);
            button.setTextSize(16);
            button.setTextColor(Color.WHITE);
            button.setButtonDrawable(android.R.color.transparent);
            button.setPadding(30, 15, 30, 15);
            button.setText("柜"+ Constant.UPPER_CHAR[i]);
            final int j = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentSelectedDesk = j;

                    shelfLayers = rotateConfig.getDeskList().get(currentSelectedDesk).getLayerList();

                    shelfCount = shelfLayers.size();
                    adapter.newData(shelfCount,shelfLayers,currentSelectedDesk);
                    adapter.notifyDataSetChanged();
                    aAdapter.newData(shelfCount,shelfLayers,currentSelectedDesk);
                    aAdapter.notifyDataSetChanged();
                }
            });
            rgRotationControl.addView(button);
        }
        if(rgRotationControl.getChildCount()!=0){
            rgRotationControl.check(indexFirst);
        }
    }

    Handler enableOpenDoorBtnHandler = new Handler();
    Runnable enableOpenDoorBtn = new Runnable() {
        @Override
        public void run() {
            btnOpenRepairDoor.setEnabled(true);
        }
    };

    @OnClick({R.id.btn_rotation_control_open_repair_door, R.id.btn_rotation_control_system_reset, R.id.btn_rotation_control_system_init})
    public void onButtonClicked(View view) {
        int slaveID = currentSelectedDesk+1;
        switch (view.getId()) {
            case R.id.btn_rotation_control_open_repair_door:   //维修门开
                try {
                    Proxy4Rotation.openRepairDoor(slaveID);
                    btnOpenRepairDoor.setEnabled(false);
                    enableOpenDoorBtnHandler.postDelayed(enableOpenDoorBtn, 5000);
                } catch (DcdzSystemException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_rotation_control_system_reset:   //系统复位
                try {
                    Proxy4Rotation.resetPlc(slaveID);
                } catch (DcdzSystemException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_rotation_control_system_init:  //系统初始化
                try {
                    Proxy4Rotation.initializePlc(slaveID);
                } catch (DcdzSystemException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String slaveDeskStatusString = Proxy4Rotation.querySlaveStatus(currentSelectedDesk+1);
                statusUpdateHandler.sendMessage(statusUpdateHandler.obtainMessage(0,slaveDeskStatusString));
            } catch (DcdzSystemException e) {
                e.printStackTrace();
            }
            handler.postDelayed(this,500);
        }
    };

    private StatusUpdateHandler statusUpdateHandler;

    private static class StatusUpdateHandler extends Handler{
        private final WeakReference<RotationControlFragment> mFragment;

        StatusUpdateHandler(RotationControlFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            RotationControlFragment fragment = mFragment.get();
            if(fragment!=null){
                fragment.adapter.newData((String) msg.obj);
                fragment.adapter.notifyDataSetChanged();
                SlaveDeskStatus slaveDeskStatus = JsonUtils.toBean((String) msg.obj,SlaveDeskStatus.class);
                //系统状态
                switch (slaveDeskStatus.getSlaveStatus()) {
                    case 0:
                        fragment.tvSystemStatus.setText("正常");
                        break;
                    case 1:
                        fragment.tvSystemStatus.setText("报警");
                        break;
                    case 2:
                        fragment.tvSystemStatus.setText("故障");
                        break;
                }
                //报警信息
                String errorMsg = slaveDeskStatus.getErrorMsg();
                fragment.tvWarningInfo.setText(errorMsg+" "+slaveDeskStatus.getErrorMsg());
                //运动状态
                switch (slaveDeskStatus.getRunStatus()) {
                    case 0:
                        fragment.tvMovingStatus.setText("停止");
                        fragment.btnSystemInit.setEnabled(true);
                        fragment.btnSystemReset.setEnabled(true);
                        break;
                    case 1:
                        fragment.tvMovingStatus.setText("运动");
                        fragment.btnSystemInit.setEnabled(false);
                        fragment.btnSystemReset.setEnabled(false);
                        break;
                }
                //维护门状态
                switch (slaveDeskStatus.getRepairDoorStatus()) {
                    case 0:
                        fragment.tvRepairDoorStatus.setText("门关");
                        break;
                    case 1:
                        fragment.tvRepairDoorStatus.setText("门开");
                        break;
                }
                //当前列
                int currentPos = slaveDeskStatus.getCurrentPos();
                if (currentPos > 20) {
                    fragment.tvCurrentBoxNum.setText("小箱："+String.valueOf(currentPos - 20));
                } else {
                    fragment.tvCurrentBoxNum.setText("大箱："+String.valueOf(currentPos));
                }
                //自动门状态
                if (slaveDeskStatus.getAutoDoorStatus() == 0) {
                    fragment.tvAutoDoorStatus.setText("门关");
                } else {
                    fragment.tvAutoDoorStatus.setText("门开");
                }
                //手动门状态
                if (slaveDeskStatus.getManualDoorStatus() == 0) {
                    fragment.tvManualDoorStatus.setText("门关");
                } else {
                    fragment.tvManualDoorStatus.setText("门开");
                }
                //光栅
                if (slaveDeskStatus.getRasterStatus() == 0) {
                    fragment.tvRasterStatus.setText("正常");
                } else {
                    fragment.tvRasterStatus.setText("有物遮挡");
                }
                //供电方式
                if (slaveDeskStatus.getPowerSupply() == 0) {
                    fragment.tvPowerSupply.setText("市电");
                } else {
                    fragment.tvPowerSupply.setText("UPS");
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
