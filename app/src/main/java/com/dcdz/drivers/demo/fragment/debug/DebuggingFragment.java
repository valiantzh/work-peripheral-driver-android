package com.dcdz.drivers.demo.fragment.debug;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.dcdz.drivers.R;
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
public class DebuggingFragment extends BaseFragment {

    @BindView(R.id.rg_debug)
    RadioGroup rbDebug;
    @BindView(R.id.fl_debug)
    FrameLayout flContent;
    Unbinder unbinder;

    List<android.app.Fragment> fragmentList = new ArrayList<>();


    public DebuggingFragment() {
        // Required empty public constructor
    }

    public static DebuggingFragment newInstance() {
        Bundle args = new Bundle();
        DebuggingFragment fragment = new DebuggingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_debugging, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (savedInstanceState == null){
            //fragmentList.add(RotationDebugFragment.newInstance());
            //fragmentList.add(RFIDReaderDebugFragment.newInstance());
            fragmentList.add(ServoDebugFragment.newInstance());
            FragmentUtils.add(getFragmentManager(), fragmentList, R.id.fl_debug, 0);
        }else {
            //fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), RotationDebugFragment.class));
            //fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), RFIDReaderDebugFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), ServoDebugFragment.class));
        }
        rbDebug.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                   /* case R.id.rb_rotation_motor:
                        FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                        break;
                    case R.id.rb_rfid:
                        FragmentUtils.showHide(fragmentList.get(1),fragmentList);
                        break;*/
                    case R.id.rb_servo_debug:
                        FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                        break;
                }
            }
        });
    }

}
