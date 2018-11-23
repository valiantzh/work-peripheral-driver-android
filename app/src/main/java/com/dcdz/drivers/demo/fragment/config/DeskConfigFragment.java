package com.dcdz.drivers.demo.fragment.config;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.adapter.DeskConfingAdapter;
import com.dcdz.drivers.demo.business.ServiceHelper;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.Dictionary;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.DeskConfig;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DeskConfigFragment extends BaseFragment {

    @BindView(R.id.lv_desk_setting)
    ListView lvDeskSetting;
    Unbinder unbinder;
    @BindView(R.id.sp_locker_group)
    Spinner spLockerGroup;
    @BindView(R.id.btn_desk_save)
    Button btnDeskSave;

    private ConfigManager configManager = ConfigManager.getInstance();
    DeskConfingAdapter adapter;

    public DeskConfigFragment() {
        // Required empty public constructor
    }

    public static DeskConfigFragment newInstance() {
        DeskConfigFragment fragment = new DeskConfigFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desk_config, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        spLockerGroup.setOnItemSelectedListener(onSelect);
        adapter = new DeskConfingAdapter(contentView.getContext(), -1);
        lvDeskSetting.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Spinner.OnItemSelectedListener onSelect = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Integer boxNum = Integer.parseInt(spLockerGroup.getSelectedItem().toString());
            adapter = new DeskConfingAdapter(view.getContext(), boxNum);
            lvDeskSetting.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    @OnClick(R.id.btn_desk_save)
    public void onBtnDeskSaveClicked() {
        List<DeskConfig> deskConfigModels = new ArrayList<>();
        boolean saveDdeskName1 = false;
        boolean saveDdeskName2 = false;
        int DeskOneBoxNo = 0;
        for (Integer i = 0; i < DeskConfingAdapter.boxCount; i++) {
            DeskConfig deskConfig = new DeskConfig();
            deskConfig.setBoardID(i.toString());
            deskConfig.setDisplayName(StringUtils.isEmpty(DeskConfingAdapter.deskstatueMap.get(i)) ? i.toString() : DeskConfingAdapter.deskstatueMap.get(i));
            deskConfig.setAssetsCode(DeskConfingAdapter.deskCodeMap.get(i) + "_" + DeskConfingAdapter.serialCodeMap.get(i));
            List<BoxConfig> boxConfigs = new ArrayList<>();
            for (Integer j = 1; j <= DeskConfingAdapter.desknumMap.get(i); j++) {
                BoxConfig boxConfigModel = new BoxConfig();
                boxConfigModel.setBoxID(j + "");
                if (StringUtils.isNumeric(DeskConfingAdapter.deskstatueMap.get(i)) || StringUtils.isEmpty(DeskConfingAdapter.deskstatueMap.get(i))) {
                    boxConfigModel.setDisplayName(j + DeskOneBoxNo + "");
                    saveDdeskName1 = true;
                } else {
                    boxConfigModel.setDisplayName(DeskConfingAdapter.deskstatueMap.get(i) + String.format("%02d", j));
                    saveDdeskName2 = true;
                }
                boxConfigs.add(boxConfigModel);
            }
            DeskOneBoxNo += DeskConfingAdapter.desknumMap.get(i);
            deskConfig.setBoxList(boxConfigs);
            deskConfigModels.add(deskConfig);
        }
        if (saveDdeskName1 == saveDdeskName2) {
            Dictionary.toastInfo(getActivity(),getString(R.string.info_deskType), Toast.LENGTH_SHORT);
            return;
        }
        configManager.eLockerConfigModel.setDeskList(deskConfigModels);
        configManager.createOrUpdateDeviceConfigFile();
        configManager.GoinDeskContrast();
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                ServiceHelper.getInstance().unBindService();
                ServiceHelper.getInstance().bindService(getActivity());
            }
        });
        Dictionary.toastInfo(getActivity(),getString(R.string.info_savesucceed), Toast.LENGTH_SHORT);
        EventBus.getDefault().post(true);
    }
}
