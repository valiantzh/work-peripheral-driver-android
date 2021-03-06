package com.dcdz.drivers.demo.fragment.elocker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.fragment.general.ScannerFragment;
import com.dcdz.drivers.demo.fragment.rotate.ShelfStateFragment;
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
public class ELockerFragment extends BaseFragment {

    @BindView(R.id.rg_elocker)
    RadioGroup rgShelfState;
    Unbinder unbinder;
    List<android.app.Fragment> fragmentList = new ArrayList<>();


    public ELockerFragment() {
        // Required empty public constructor
    }

    public static ELockerFragment newInstance() {
        Bundle args = new Bundle();
        ELockerFragment fragment = new ELockerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_elocker, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (savedInstanceState == null){
            fragmentList.add(DeskFragment.newInstance());
            FragmentUtils.add(getFragmentManager(), fragmentList, R.id.fl_container_elocker, 0);
        }else {
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), DeskFragment.class));
        }
        rgShelfState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()){
                    case R.id.rb_desk_control:
                        FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                        break;
                }
            }
        });
    }
}
