package com.dcdz.drivers.demo.fragment.rotate;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

/**
 * Created by LJW on 2018/3/2.
 * 发卡器测试
 */
public class CardSenderTestFragment extends BaseFragment {


    public CardSenderTestFragment() {
        // Required empty public constructor
    }

    public static CardSenderTestFragment newInstance() {
        Bundle args = new Bundle();
        CardSenderTestFragment fragment = new CardSenderTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_sender_test, container, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }
}
