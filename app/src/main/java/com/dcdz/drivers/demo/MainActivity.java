package com.dcdz.drivers.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dcdz.drivers.PDApplication;
import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.ServiceHelper;
import com.dcdz.drivers.demo.fragment.config.ConfigureFragment;
import com.dcdz.drivers.demo.fragment.debug.DebuggingFragment;
import com.dcdz.drivers.demo.fragment.elocker.ELockerFragment;
import com.dcdz.drivers.demo.fragment.general.GeneralPeripheralsFragment;
import com.dcdz.drivers.demo.fragment.image.ImageRecognitionFragment;
import com.dcdz.drivers.demo.fragment.rotate.RotationFragment;
import com.dcdz.drivers.utils.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
//
//    @BindView(R.id.config_button)
//    Button configButton;
    @BindView(R.id.debugging_button)
    Button debuggingButton;
//    @BindView(R.id.rl_navigate)
//    RelativeLayout rlNavigate;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;
    @BindView(R.id.rl_service)
    RelativeLayout rlService;
//    @BindView(R.id.rg_navigate)
//    RadioGroup rgNavigate;

//    List<Fragment> fragmentList = new ArrayList<>();
    GeneralPeripheralsFragment generalPeripheralsFragment;
    ELockerFragment eLockerFragment;
    RotationFragment rotationFragment;
    ConfigureFragment configureFragment;
    DebuggingFragment debuggingFragment;
    ImageRecognitionFragment imageRecognitionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PDApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView(savedInstanceState);

        handler.post(new Runnable() {
            @Override
            public void run() {
                ServiceHelper.getInstance().bindService(getBaseContext());
            }
        });
    }
    Handler handler = new Handler();

    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            configureFragment = ConfigureFragment.newInstance();
            FragmentUtils.add(getFragmentManager(),configureFragment, R.id.fl_container);
        } else {
            FragmentUtils.add(getFragmentManager(),configureFragment, R.id.fl_container);
        }

    }

    @OnClick({R.id.tv_general_peripherals,R.id.tv_rotation_desk,R.id.tv_configure,R.id.tv_smart_desk,R.id.tv_image_recognition})
    public void onButtonClick(View view){
        switch (view.getId()) {
            case R.id.tv_general_peripherals:
                generalPeripheralsFragment = GeneralPeripheralsFragment.newInstance();
                FragmentUtils.add(getFragmentManager(),generalPeripheralsFragment, R.id.fl_container);
                break;
            case R.id.tv_smart_desk:
                eLockerFragment = ELockerFragment.newInstance();
                FragmentUtils.add(getFragmentManager(),eLockerFragment, R.id.fl_container);
                break;
            case R.id.tv_rotation_desk:
                rotationFragment = RotationFragment.newInstance();
                FragmentUtils.add(getFragmentManager(),rotationFragment, R.id.fl_container);
                break;
            case  R.id.tv_configure:
                configureFragment = ConfigureFragment.newInstance();
                FragmentUtils.add(getFragmentManager(),configureFragment, R.id.fl_container);
                break;
            case R.id.tv_image_recognition:
                imageRecognitionFragment = ImageRecognitionFragment.newInstance();
                FragmentUtils.add(getFragmentManager(),imageRecognitionFragment,R.id.fl_container);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.debugging_button)
    public void onDebuggingButtonClicked(){
        debuggingFragment = DebuggingFragment.newInstance();
        FragmentUtils.add(getFragmentManager(),debuggingFragment, R.id.fl_container);
    }

    @OnClick(R.id.exit_button)
    public void onExitButtonClicked() {
        PDApplication.getInstance().exit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServiceHelper.getInstance().unBindService();
    }

    //屏蔽返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
