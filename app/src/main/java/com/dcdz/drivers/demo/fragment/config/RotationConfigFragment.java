package com.dcdz.drivers.demo.fragment.config;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

/**
 * Created by LJW on 2018/3/2.
 * 旋转柜配置
 */
public class RotationConfigFragment extends BaseFragment {

    public RotationConfigFragment() {
        // Required empty public constructor
    }

    public static RotationConfigFragment newInstance() {
        Bundle args = new Bundle();
        RotationConfigFragment fragment = new RotationConfigFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rotation_config, container, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }
}
