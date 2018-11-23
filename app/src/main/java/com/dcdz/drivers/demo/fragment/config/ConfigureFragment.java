package com.dcdz.drivers.demo.fragment.config;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.fragment.elocker.DeskFragment;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfigureFragment extends BaseFragment {
    @BindView(R.id.rg_navigate)
    RadioGroup rgNavigate;
    Unbinder unbinder;

    List<android.app.Fragment> fragmentList = new ArrayList<>();

    public ConfigureFragment() {
        // Required empty public constructor
    }


    public static ConfigureFragment newInstance() {
        Bundle args = new Bundle();
        ConfigureFragment fragment = new ConfigureFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_configure, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (savedInstanceState == null) {
            fragmentList.add(DeviceConfigFragment.newInstance());
            fragmentList.add(DeskConfigFragment.newInstance());

            FragmentUtils.add(getFragmentManager(), fragmentList, R.id.fl_configure, 0);
        } else {
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), DeviceConfigFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), DeskConfigFragment.class));

        }
        rgNavigate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_port_config){
                    FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_desk_config){
                    FragmentUtils.showHide(fragmentList.get(1),fragmentList);
                }
            }
        });
    }

}
