package com.dcdz.drivers.demo.fragment.image;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * 人脸识别
 */
public class FaceContrastFragment extends BaseFragment {

    public FaceContrastFragment() {
        // Required empty public constructor
    }

    public static FaceContrastFragment newInstance() {
         Bundle args = new Bundle();
         FaceContrastFragment fragment = new FaceContrastFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_face_contrast, container, false);
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }
}
