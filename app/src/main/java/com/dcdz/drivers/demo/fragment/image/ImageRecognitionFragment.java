package com.dcdz.drivers.demo.fragment.image;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class ImageRecognitionFragment extends BaseFragment {

    Unbinder unbinder;
    List<android.app.Fragment> fragmentList = new ArrayList<>();
    @BindView(R.id.rg_navigate)
    RadioGroup rgNavigate;

    public ImageRecognitionFragment(){}

    public static ImageRecognitionFragment newInstance() {

        Bundle args = new Bundle();

        ImageRecognitionFragment fragment = new ImageRecognitionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_image_recognition, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (savedInstanceState == null) {
            fragmentList.add(FaceRecognitionFragment.newInstance());
            fragmentList.add(FaceSaveFragment.newInstance());
            fragmentList.add(FaceContrastFragment.newInstance());

            FragmentUtils.add(getFragmentManager(), fragmentList, R.id.fl_image, 0);
        } else {
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), FaceRecognitionFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), FaceSaveFragment.class));
            fragmentList.add(FragmentUtils.findFragment(getFragmentManager(), FaceContrastFragment.class));
        }
        rgNavigate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (radioGroup.getCheckedRadioButtonId() == R.id.radio_face_recognition){
                    FragmentUtils.showHide(fragmentList.get(0),fragmentList);
                }else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_face_save){
                    FragmentUtils.showHide(fragmentList.get(1),fragmentList);
                }else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_face_contrast){
                    FragmentUtils.showHide(fragmentList.get(2),fragmentList);
                }
            }
        });
    }
}
