package com.dcdz.drivers.demo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

/**
 * Created by LJW on 2018/3/2.
 * 主机
 */
public class HostFragment extends BaseFragment {


    public HostFragment() {
        // Required empty public constructor
    }

    public static HostFragment newInstance() {
        Bundle args = new Bundle();
        HostFragment fragment = new HostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host, container, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }
}
