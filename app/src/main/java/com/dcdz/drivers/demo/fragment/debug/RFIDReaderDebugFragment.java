package com.dcdz.drivers.demo.fragment.debug;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

/**
 * Created by LJW on 2018/3/2.
 * RFID调试
 */
public class RFIDReaderDebugFragment extends BaseFragment {

    public RFIDReaderDebugFragment() {
        // Required empty public constructor
    }

    public static RFIDReaderDebugFragment newInstance() {

        Bundle args = new Bundle();

        RFIDReaderDebugFragment fragment = new RFIDReaderDebugFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rfid, container, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }
}
