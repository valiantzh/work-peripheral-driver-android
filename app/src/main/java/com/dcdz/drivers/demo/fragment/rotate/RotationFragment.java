package com.dcdz.drivers.demo.fragment.rotate;


import android.app.Fragment;
import android.os.Bundle;
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


public class RotationFragment extends BaseFragment {
    @BindView(R.id.rg_rotation)
    RadioGroup rbRotation;
    @BindView(R.id.fl_rotation)
    FrameLayout flRotation;
    Unbinder unbinder;

    List<Fragment> fragmentList = new ArrayList<>();

    public RotationFragment() {
        // Required empty public constructor
    }

    public static RotationFragment newInstance() {
        Bundle args = new Bundle();
        RotationFragment fragment = new RotationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_rotation, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (savedInstanceState == null) {
            fragmentList.add(SIMCardSenderFragment.newInstance());
            fragmentList.add(ICCardSenderFragment.newInstance());
            fragmentList.add(ShelfStateFragment.newInstance());
            fragmentList.add(RotationControlFragment.newInstance());
            fragmentList.add(StabilityTestFragment.newInstance());
            FragmentUtils.add(getFragmentManager(), fragmentList, R.id.fl_rotation, 0);
        }else {
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), SIMCardSenderFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), ICCardSenderFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), ShelfStateFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), RotationControlFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), StabilityTestFragment.class));
        }
        rbRotation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.rb_sim_card_send:
                        FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                        break;
                    case R.id.rb_ic_card_send:
                        FragmentUtils.showHide(fragmentList.get(1),fragmentList);
                        break;
                    case R.id.rb_shelf_state:
                        FragmentUtils.showHide(fragmentList.get(2),fragmentList);
                        break;
                    case R.id.rb_rotation_control:
                        FragmentUtils.showHide(fragmentList.get(3),fragmentList);
                        break;
                    case R.id.rb_stability_test:
                        FragmentUtils.showHide(fragmentList.get(4),fragmentList);
                        break;
                }
            }
        });
    }
}
