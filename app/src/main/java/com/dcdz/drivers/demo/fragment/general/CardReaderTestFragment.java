package com.dcdz.drivers.demo.fragment.general;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.dcdz.drivers.R;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.dcdz.drivers.databinding.FragmentCardReadTestBinding;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.demo.business.IMessageListener;
import com.dcdz.drivers.demo.model.CardReadModel;
import com.dcdz.drivers.utils.BaseFragment;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * 读卡器测试
 */
public class CardReaderTestFragment extends BaseFragment implements IMessageListener{
    Unbinder unbinder;
    @BindView(R.id.toggle_start_stop)
    ToggleButton toggleStartStop;

//    @BindView(R.id.toggle_single_card_read)
//    ToggleButton toggleCardRead;

    boolean runPostDelayed;
    Handler handler = new Handler();

    FragmentCardReadTestBinding binding;
    private ConfigManager configManager = ConfigManager.getInstance();
    private DeviceManager deviceManager = DeviceManager.getInstance();

    private CardReadModel cardReadModel;
    public CardReaderTestFragment() {
        // Required empty public constructor
    }

    public static CardReaderTestFragment newInstance() {
        Bundle args = new Bundle();
        CardReaderTestFragment fragment = new CardReaderTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            clearCardInfo();

            if(runPostDelayed){
                handler.postDelayed(this, 5000);
            }
        }
    };
    //读卡回调方法
    @Override
    public void onMessage(final String message) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Dictionary.toastInfo(getActivity(), "读卡信息：" +message, Toast.LENGTH_SHORT);
                Log.d("CardInfo",message);
                setCardReadModel(message);
            }
        });
    }

    private void clearCardInfo(){
        String emptyCardInfo = "{\"cardInformation\":{\"address\":\" \",\"born\":\" \",\"cardID\":\" \",\"grantDept\":\" \",\"name\":\" \",\"nation\":\" \",\"sex\":\" \",\"string\":\" \",\"userLifeBegin\":\" \",\"userLifeEnd\":\" \"},\"errorCode\":0}";
        setCardReadModel(emptyCardInfo);
    }

    private void setCardReadModel(String message){
        com.alibaba.fastjson.JSONObject cardInfo =JsonUtils.toJSONObject(message);
        cardReadModel.setCardNo(cardInfo.getJSONObject("cardInformation").getString("cardID"));
        cardReadModel.setNames(cardInfo.getJSONObject("cardInformation").getString("name"));
        cardReadModel.setAddress(cardInfo.getJSONObject("cardInformation").getString("address"));
        cardReadModel.setNational(cardInfo.getJSONObject("cardInformation").getString("nation"));
        cardReadModel.setGender(cardInfo.getJSONObject("cardInformation").getString("sex"));
        cardReadModel.setDateOfBirth(cardInfo.getJSONObject("cardInformation").getString("born"));
        cardReadModel.setSendLicenseAgencies(cardInfo.getJSONObject("cardInformation").getString("grantDept"));
        cardReadModel.setDateOfRegistration(cardInfo.getJSONObject("cardInformation").getString("userLifeBegin"));
        cardReadModel.setDateOfEnd(cardInfo.getJSONObject("cardInformation").getString("userLifeEnd"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_card_read_test,container,false);
        unbinder = ButterKnife.bind(this, binding.getRoot());
        cardReadModel = new CardReadModel();
        binding.setModel(cardReadModel);
        return binding.getRoot();
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
        binding.tvCardReadPort.setText(configManager.cardReaderConfig.getPortName());
        binding.tvCardReadVendor.setText(configManager.cardReaderConfig.getVendorName());
        clearCardInfo();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        }else{
        }
    }

    @OnClick({R.id.toggle_start_stop})
    public void onViewClicked(View view){
        switch (view.getId()) {
            case R.id.toggle_start_stop://开始/停止读卡
                if(toggleStartStop.isChecked()){
                    //clearCardInfo();
                    runPostDelayed = true;
                    handler.post(runnable);
                    try {
                        deviceManager.peripheralProxy.addCardReadListener(this);
                        deviceManager.peripheralProxy.startReadCardTask();
                    } catch (DcdzSystemException e) {
                        e.printStackTrace();
                    }
                }else{
                    closeCardReader();
                }
                break;
//            case R.id.toggle_single_card_read://单读卡
//
//                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeCardReader();

    }

    private void closeCardReader(){
        try {
            runPostDelayed = false;
            deviceManager.peripheralProxy.removeCardReadListener();
            deviceManager.peripheralProxy.stopReadCardTask();
        } catch (DcdzSystemException e) {
            e.printStackTrace();
        }
    }
}
