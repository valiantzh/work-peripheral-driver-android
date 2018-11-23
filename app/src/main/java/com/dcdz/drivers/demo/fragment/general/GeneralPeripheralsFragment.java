package com.dcdz.drivers.demo.fragment.general;


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
public class GeneralPeripheralsFragment extends BaseFragment {


    @BindView(R.id.rg_root)
    RadioGroup rbRoot;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    Unbinder unbinder;

    List<android.app.Fragment> fragmentList = new ArrayList<>();

    public GeneralPeripheralsFragment() {
        // Required empty public constructor
    }

    public static GeneralPeripheralsFragment newInstance() {
        Bundle args = new Bundle();
        GeneralPeripheralsFragment fragment = new GeneralPeripheralsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_general_peripherals, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (savedInstanceState == null) {
            fragmentList.add(ScannerFragment.newInstance());
            fragmentList.add(CardReaderTestFragment.newInstance());
            fragmentList.add(PrinterTestFragment.newInstance());
            fragmentList.add(NetworkTestFragment.newInstance());
            fragmentList.add(CameraTestFragment.newInstance());
            fragmentList.add(SoundTestFragment.newInstance());
            FragmentUtils.add(getFragmentManager(), fragmentList, R.id.fl_content, 0);
        }else {
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), ScannerFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), CardReaderTestFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), PrinterTestFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), NetworkTestFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), CameraTestFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), SoundTestFragment.class));
        }
        rbRoot.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.rb_scanner:
                        FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                        break;
                    case R.id.rb_cardreader:
                        FragmentUtils.showHide(fragmentList.get(1),fragmentList);
                        break;
                    case R.id.rb_printer:
                        FragmentUtils.showHide(fragmentList.get(2),fragmentList);
                        break;
                    case R.id.rb_network:
                        FragmentUtils.showHide(fragmentList.get(3),fragmentList);
                        break;
                    case R.id.radio_camera_test:
                        FragmentUtils.showHide(fragmentList.get(4),fragmentList);
                        break;
                    case R.id.radio_sound_test:
                        FragmentUtils.showHide(fragmentList.get(5),fragmentList);
                        break;
                }
            }
        });
    }

}
