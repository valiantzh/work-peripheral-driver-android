package com.dcdz.drivers.demo.fragment.image;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dcdz.drivers.R;
import com.dcdz.drivers.cv.TrackingOverlay;
import com.dcdz.drivers.dto.UserInfo;
import com.dcdz.drivers.utils.BaseFragment;
import com.otaliastudios.cameraview.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * 人脸入库
 */
public class FaceSaveFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.btn_show)
    Button btnShow;
    @BindView(R.id.rv_face)
    RecyclerView rvFace;
    @BindView(R.id.camera)
    CameraView cameraView;
    @BindView(R.id.tracking_overlay)
    TrackingOverlay overlay;

    UserInfo info = new UserInfo();

    public static FaceSaveFragment newInstance() {
         Bundle args = new Bundle();
         FaceSaveFragment fragment = new FaceSaveFragment();
         fragment.setArguments(args);
         return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_face_save, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {

    }

    @OnClick({R.id.btn_save, R.id.btn_show})
    public void clickListen(View view){
        switch (view.getId()){
            case R.id.btn_save:
                rvFace.setVisibility(View.GONE);
                overlay.setVisibility(View.VISIBLE);
                cameraView.setVisibility(View.VISIBLE);
            break;
            case R.id.btn_show:
                rvFace.setVisibility(View.VISIBLE);
                overlay.setVisibility(View.GONE);
                cameraView.setVisibility(View.GONE);
            break;
        }
    }

}
