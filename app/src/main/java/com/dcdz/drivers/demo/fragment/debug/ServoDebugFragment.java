package com.dcdz.drivers.demo.fragment.debug;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.Proxy4Rotation;
import com.dcdz.drivers.demo.business.Proxy4Servo;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.DialogEvent;
import com.dcdz.drivers.utils.Dictionary;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.peripheral.servo.model.ServoStatus;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ServoDebugFragment extends BaseFragment{
    Unbinder unbinder;
    @BindView(R.id.et_servo_1)
    EditText etServoLowSpeed;
    @BindView(R.id.et_servo_2)
    EditText etServoHighSpeed;
    @BindView(R.id.et_servo_3)
    EditText etServoCoilNum;
    @BindView(R.id.et_servo_pulse_num_base)
    EditText etServoPulseNumBase;
    @BindView(R.id.et_servo_pulse_num_coil)
    EditText etServoPulseNumCoil;
    @BindView(R.id.et_servo_pulse_num_position)
    EditText etServoPulseNumPosition;
    @BindView(R.id.sw_servo_selected_box_type)
    Switch swSelectedBoxType;
    @BindView(R.id.rb_servo_1)
    RadioButton rbServoBase;
    @BindView(R.id.rb_servo_2)
    RadioButton rbServoCoil;
    @BindView(R.id.rb_servo_3)
    RadioButton rbServoPosition;
    @BindView(R.id.btn_servo_set_pulse_4_base)
    Button btnServoSetPulse4Base;
    @BindView(R.id.btn_servo_set_pulse_4_coil)
    Button btnServoSetPulse4Coil;
    @BindView(R.id.btn_servo_set_pulse_4_position)
    Button btnServoSetPulse4Position;
    @BindView(R.id.rg_servo_1)
    RadioGroup rgServoSetPulse;
    @BindView(R.id.btn_servo_next_box)
    Button btnServoNextBox;
    @BindView(R.id.btn_servo_last_box)
    Button btnServoLastBox;
    @BindView(R.id.btn_servo_run_coil)
    Button btnServoRunCoil;
    @BindView(R.id.tv_servo_total_pulse)
    TextView tvTotalPulse;
    @BindView(R.id.tv_servo_correct_pulse)
    TextView tvCorrectPulse;
    @BindView(R.id.tv_servo_enable_status)
    TextView tvEnableStatus;
    @BindView(R.id.tv_servo_origin_status)
    TextView tvOriginStatus;
    @BindView(R.id.tv_servo_run_status)
    TextView tvRunStatus;
    @BindView(R.id.tv_servo_run_forward)
    TextView tvRunForward;
    @BindView(R.id.tv_servo_run_mode)
    TextView tvRunMode;
    @BindView(R.id.tv_servo_run_speed)
    TextView tvRunSpeed;
    @BindView(R.id.tv_servo_correcting_type)
    TextView tvCorrectingType;
    @BindView(R.id.tv_servo_correct_coil)
    TextView tvCorrectCoil;
    @BindView(R.id.tv_servo_current_pos)
    TextView tvCurrentPos;
    @BindView(R.id.tv_servo_target_pos)
    TextView tvTargetPos;
    @BindView(R.id.tv_servo_error_code)
    TextView tvErrorCode;
    @BindView(R.id.tv_servo_total_coil)
    TextView tvTotalCoil;
    @BindView(R.id.btn_servo_jog_pos)
    Button btnJogPos;
    @BindView(R.id.btn_servo_jog_neg)
    Button btnJogNeg;
    @BindView(R.id.tb_servo_1)
    ToggleButton tbToggleServoEnable;
    @BindView(R.id.tv_servo_cmd_char)
    TextView tvCmdChar;
    @BindView(R.id.tv_query_count)
    TextView tvQueryCount;
    @BindView(R.id.tv_servo_error_reset)
    TextView tvErrorReset;
    @BindView(R.id.tv_servo_run_error_code_1)
    TextView tvRunErrorCode1;
    @BindView(R.id.tv_servo_run_error_code_2)
    TextView tvRunErrorCode2;
    @BindView(R.id.tv_servo_bus_error_code)
    TextView tvBusErrorCode;
    @BindView(R.id.tv_servo_urgent_stop_reset)
    TextView tvUrgentStopReset;
    @BindView(R.id.tv_servo_current_box_id)
    TextView tvCurrentBoxId;
    @BindView(R.id.sw_servo_current_box_type)
    Switch swCurrentBoxType;
    @BindView(R.id.btn_servo_origin)
    Button btnOrigin;
    @BindView(R.id.et_servo_row_num)
    EditText etRowNum;
    @BindView(R.id.et_servo_column_num)
    EditText etColumnNum;
    @BindView(R.id.et_servo_door_num)
    EditText etDoorNum;

    private int speedLow = 100;
    private int speedHigh = 2000;
    private int iCoil = 0;


    private byte slaveID = 1;
    private int selectedBoxType = 0;

    private int rgSelectedItem = 0;
    private String resultData = "";
    private com.alibaba.fastjson.JSONObject resultDataJson;

    private Result result;
    private int queryCount = 0;

    private ServoStatus servoStatus;

    public ServoDebugFragment() {
        // Required empty public constructor
    }

    public static ServoDebugFragment newInstance() {
        Bundle args = new Bundle();
        ServoDebugFragment fragment = new ServoDebugFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_servo_debug, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        swSelectedBoxType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    selectedBoxType = 1;
                } else {
                    selectedBoxType = 0;
                }
            }
        });

        tbToggleServoEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, true));
                try {
                    if(isChecked){
                        Proxy4Servo.toggleServoEnable(slaveID,true);
                    } else {
                        Proxy4Servo.toggleServoEnable(slaveID,false);
                    }
                } catch (DcdzSystemException e) {
                    //e.printStackTrace();
                    Dictionary.toastInfo(getActivity(), "执行失败：" + e.getMessage(), Toast.LENGTH_SHORT);
                }finally {
                    EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, false));
                }
            }
        });

        rgServoSetPulse.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int CheckedId) {
                switch (CheckedId){
                    case R.id.rb_servo_1:
                        etServoPulseNumBase.setEnabled(true);
                        btnServoSetPulse4Base.setEnabled(true);
                        etServoPulseNumCoil.setEnabled(false);
                        etServoCoilNum.setEnabled(false);
                        btnServoSetPulse4Coil.setEnabled(false);
                        btnServoRunCoil.setEnabled(false);
                        etServoPulseNumPosition.setEnabled(false);
                        btnServoSetPulse4Position.setEnabled(false);
                        btnServoNextBox.setEnabled(false);
                        btnServoLastBox.setEnabled(false);
                        rgSelectedItem = 1;
                        break;
                    case R.id.rb_servo_2:
                        etServoPulseNumBase.setEnabled(false);
                        btnServoSetPulse4Base.setEnabled(false);
                        etServoPulseNumCoil.setEnabled(true);
                        etServoCoilNum.setEnabled(true);
                        btnServoSetPulse4Coil.setEnabled(true);
                        btnServoRunCoil.setEnabled(true);
                        etServoPulseNumPosition.setEnabled(false);
                        btnServoSetPulse4Position.setEnabled(false);
                        btnServoNextBox.setEnabled(false);
                        btnServoLastBox.setEnabled(false);
                        rgSelectedItem = 2;
                        break;
                    case R.id.rb_servo_3:
                        etServoPulseNumBase.setEnabled(false);
                        btnServoSetPulse4Base.setEnabled(false);
                        etServoPulseNumCoil.setEnabled(false);
                        etServoCoilNum.setEnabled(false);
                        btnServoSetPulse4Coil.setEnabled(false);
                        btnServoRunCoil.setEnabled(false);
                        etServoPulseNumPosition.setEnabled(true);
                        btnServoSetPulse4Position.setEnabled(true);
                        btnServoNextBox.setEnabled(true);
                        btnServoLastBox.setEnabled(true);
                        rgSelectedItem = 3;
                        break;
                }
            }
        });

        Thread inspector = new Thread(runnable);
        inspector.start();
    }

    @OnClick({R.id.btn_servo_origin,R.id.btn_servo_jog_pos,R.id.btn_servo_jog_neg,R.id.btn_servo_stop,R.id.btn_servo_reset_stop,R.id.btn_servo_reset_error,R.id.btn_servo_run_coil})
    public void onButtonClicked(View v){
        EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, true));
        try{
            switch (v.getId()){
                case R.id.btn_servo_origin:
                    Proxy4Servo.returnServoOrigin(slaveID);
                    break;
                case R.id.btn_servo_jog_pos:
                    Proxy4Servo.runServoByJog(slaveID,0,speedLow,rgSelectedItem);
                    break;
                case R.id.btn_servo_jog_neg:
                    Proxy4Servo.runServoByJog(slaveID,1,speedLow,rgSelectedItem);
                    break;
                case R.id.btn_servo_stop:
                    Proxy4Servo.stopServoByJog(slaveID,rgSelectedItem);
                    break;
                case R.id.btn_servo_reset_stop:
                    Proxy4Servo.resetServo4Stop(slaveID);
                    break;
                case R.id.btn_servo_reset_error:
                    Proxy4Servo.resetServo4Error(slaveID);
                    break;
                case R.id.btn_servo_run_coil:
                    if(etServoCoilNum.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"请输入圈数",Toast.LENGTH_SHORT).show();
                    } else {
                        iCoil = Integer.parseInt(etServoCoilNum.getText().toString());
                        Proxy4Servo.runServoByCoil(slaveID,iCoil,0,speedHigh);
                    }
                    break;
            }
        }catch (DcdzSystemException e) {
            //e.printStackTrace();
            Dictionary.toastInfo(getActivity(), "执行失败：" + e.getMessage(), Toast.LENGTH_SHORT);
        }finally {
            EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, false));
        }

    }

    @OnClick(R.id.btn_servo_save_config)
    public void onSaveBtnClicked(){
        if(etServoLowSpeed.getText().toString().equals("")){
            Toast.makeText(getActivity(),"请输入低速",Toast.LENGTH_SHORT).show();
        } else {
            speedLow = Integer.parseInt(etServoLowSpeed.getText().toString());
            Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
        }
        if(etServoHighSpeed.getText().toString().equals("")){
            Toast.makeText(getActivity(),"请输入高速",Toast.LENGTH_SHORT).show();
        } else {
            speedHigh = Integer.parseInt(etServoHighSpeed.getText().toString());
            Toast.makeText(getActivity(),"保存成功",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.btn_servo_last_box,R.id.btn_servo_next_box})
    public void onBoxBtnClicked(View v){
        EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, true));
        try {
            switch (v.getId()){
                case R.id.btn_servo_next_box:
                    Proxy4Servo.runServoByNext(slaveID,selectedBoxType,speedHigh);
                    break;
                case R.id.btn_servo_last_box:
                    Proxy4Servo.runServoByLast(slaveID,selectedBoxType,speedHigh);
                    break;
            }
        } catch (DcdzSystemException e) {
            //e.printStackTrace();
            Dictionary.toastInfo(getActivity(), "执行失败：" + e.getMessage(), Toast.LENGTH_SHORT);
        }finally {
            EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, false));
        }

    }

    @OnClick({R.id.btn_servo_set_pulse_4_base,R.id.btn_servo_set_pulse_4_coil,R.id.btn_servo_set_pulse_4_position})
    public void onSetPulseBtnClicked(View v){
        EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, true));
        try {
            switch ((v.getId())){
                case R.id.btn_servo_set_pulse_4_base:
                    if(etServoPulseNumBase.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"请输入脉冲数",Toast.LENGTH_SHORT).show();
                    } else {
                        final int pulseNumBase = Integer.parseInt(etServoPulseNumBase.getText().toString());
                        Proxy4Servo.setPulse4Base(slaveID,pulseNumBase);
                        break;
                    }

                case R.id.btn_servo_set_pulse_4_coil:
                    if(etServoPulseNumCoil.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"请输入脉冲数",Toast.LENGTH_SHORT).show();
                    } else {
                        final int pulseNumCoil = Integer.parseInt(etServoPulseNumCoil.getText().toString());
                        Proxy4Servo.setPulse4Coil(slaveID,pulseNumCoil);
                        break;
                    }

                case R.id.btn_servo_set_pulse_4_position:
                    if(etServoPulseNumPosition.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"请输入脉冲数",Toast.LENGTH_SHORT).show();
                    } else {
                        final int pulseNumPosition = Integer.parseInt(etServoPulseNumPosition.getText().toString());
                        Proxy4Servo.setPulse4Position(slaveID,pulseNumPosition);
                        break;
                    }

            }
        }catch (DcdzSystemException e) {
            //e.printStackTrace();
            Dictionary.toastInfo(getActivity(), "执行失败：" + e.getMessage(), Toast.LENGTH_SHORT);
        }finally {
            EventBus.getDefault().post(new DialogEvent(DialogEvent.WAITING, false));
        }

    }

    @OnClick(R.id.btn_servo_open_box)
    public void onOpenBoxClicked(){
        if (etRowNum.getText().toString().isEmpty() || etColumnNum.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入正确的行列号", Toast.LENGTH_SHORT).show();
        } else {
            int rowNum = Integer.parseInt(etRowNum.getText().toString());
            int colNum = Integer.parseInt(etColumnNum.getText().toString());
            final int boxNo = (rowNum-1)*100+colNum;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Proxy4Rotation.openBox4Access((byte) 1,boxNo);
                    } catch (DcdzSystemException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    @OnClick(R.id.btn_servo_open_door)
    public void onOpenDoorClicked(){
        if (etDoorNum.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "请输入正确的门号", Toast.LENGTH_SHORT).show();
        } else {
            final int doorNum = Integer.parseInt(etDoorNum.getText().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Proxy4Rotation.openDoor((byte)1,(byte)doorNum-1);
                    } catch (DcdzSystemException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }


    public void removeThreat(int servoRunStatus){
        if(servoRunStatus == 1){
            btnJogNeg.setEnabled(false);
            btnJogPos.setEnabled(false);
            btnOrigin.setEnabled(false);
            tbToggleServoEnable.setEnabled(false);
            if(rgServoSetPulse.getCheckedRadioButtonId()==R.id.rb_servo_3){
                btnServoNextBox.setEnabled(false);
                btnServoLastBox.setEnabled(false);
                swSelectedBoxType.setEnabled(false);
            } else if (rgServoSetPulse.getCheckedRadioButtonId() == R.id.rb_servo_2) {
                btnServoRunCoil.setEnabled(false);
            }
        } else if (servoRunStatus == 0) {
            btnJogNeg.setEnabled(true);
            btnJogPos.setEnabled(true);
            btnOrigin.setEnabled(true);
            tbToggleServoEnable.setEnabled(true);
            if(rgServoSetPulse.getCheckedRadioButtonId()==R.id.rb_servo_3){
                btnServoNextBox.setEnabled(true);
                btnServoLastBox.setEnabled(true);
                swSelectedBoxType.setEnabled(true);
            } else if (rgServoSetPulse.getCheckedRadioButtonId() == R.id.rb_servo_2) {
                btnServoRunCoil.setEnabled(true);
            }
        }

    }

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String resultData = Proxy4Servo.getServoStatus(slaveID);
                servoStatus = JsonUtils.toBean(resultData, ServoStatus.class);
                resultDataJson = JsonUtils.toJSONObject(resultData);
                queryCount++;
                statusUpdateHandler.sendMessage(statusUpdateHandler.obtainMessage(0, resultDataJson));
            } catch (DcdzSystemException e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, 200);
        }
    };

    private final StatusUpdateHandler statusUpdateHandler = new StatusUpdateHandler(this);

    private static class StatusUpdateHandler extends Handler {
        private final WeakReference<ServoDebugFragment> mFragment;

        StatusUpdateHandler(ServoDebugFragment fragment){
            mFragment = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ServoDebugFragment fragment = mFragment.get();
            if (fragment != null) {
                com.alibaba.fastjson.JSONObject jsonObject = (com.alibaba.fastjson.JSONObject) msg.obj;
                String enableStatus;
                switch (jsonObject.getInteger("enableStatus")){
                    case 0:
                        enableStatus = "伺服无使能";
                        fragment.tbToggleServoEnable.setChecked(false);
                        break;
                    case 1:
                        enableStatus = "伺服有使能";
                        fragment.tbToggleServoEnable.setChecked(true);
                        break;
                    case 9:
                        enableStatus = "未知";
                        break;
                    default:
                        enableStatus = "未知";
                        break;

                }
                String originStatus;
                switch (jsonObject.getInteger("originStatus")){
                    case 0:
                        originStatus = "伺服无原点";
                        break;
                    case 1:
                        originStatus = "伺服有原点";
                        break;
                    case 9:
                        originStatus = "未知";
                        break;
                    default:
                        originStatus = "未知";
                        break;

                }
                String runStatus;
                switch (jsonObject.getInteger("runStatus")){
                    case 0:
                        runStatus = "伺服停止";
                        break;
                    case 1:
                        runStatus = "伺服运行中";
                        break;
                    case 9:
                        runStatus = "未知";
                        break;
                    default:
                        runStatus = "未知";
                        break;

                }
                String runForward;
                switch (jsonObject.getInteger("runForward")){
                    case 0:
                        runForward = "正向";
                        break;
                    case 1:
                        runForward = "反向";
                        break;
                    case 9:
                        runForward = "未知";
                        break;
                    default:
                        runForward = "未知";
                        break;

                }
                String runMode;
                switch (jsonObject.getInteger("runMode")){
                    case 0:
                        runMode = "高速";
                        break;
                    case 1:
                        runMode = "低速";
                        break;
                    case 9:
                        runMode = "未知";
                        break;
                    default:
                        runMode = "未知";
                        break;

                }
                String correctingType;
                switch (jsonObject.getInteger("correctingType")){
                    case 1:
                        correctingType = "系统基准点标定中";
                        break;
                    case 2:
                        correctingType = "基准脉冲数标定中";
                        break;
                    case 3:
                        correctingType = "格口位置标定中";
                        break;
                    default:
                        correctingType = "未知";
                        break;

                }
                fragment.tvEnableStatus.setText(enableStatus);
                fragment.tvOriginStatus.setText(originStatus);
                fragment.tvRunStatus.setText(runStatus);
                fragment.tvRunForward.setText(runForward);
                fragment.tvRunMode.setText(runMode);
                fragment.tvCorrectingType.setText(correctingType);

                fragment.tvRunSpeed.setText(String.valueOf(jsonObject.getInteger("runSpeed")));
                fragment.tvCorrectPulse.setText(String.valueOf(jsonObject.getInteger("correctPulse")));
                fragment.tvCorrectCoil.setText(String.valueOf(jsonObject.getInteger("correctCoil")));
                int currentPos = jsonObject.getInteger("currentPos");
                if (currentPos > 20) {
                    fragment.tvCurrentPos.setText(String.valueOf(currentPos));
                } else {
                    fragment.tvCurrentPos.setText(String.valueOf(currentPos - 20));
                }

                fragment.tvTargetPos.setText(String.valueOf(jsonObject.getInteger("targetPos")));
                fragment.tvTotalPulse.setText(String.valueOf(jsonObject.getInteger("totalPulse")));
                fragment.tvTotalCoil.setText(String.valueOf(jsonObject.getInteger("totalCoil")));

                String busErrorCode = String.valueOf(jsonObject.getInteger("busErrorCode"));
                String runErrorCode1= String.valueOf(jsonObject.getInteger("runErrorCode1"));
                String runErrorCode2 = String.valueOf(jsonObject.getInteger("runErrorCode2"));
                String urgentStopReset = String.valueOf(jsonObject.getInteger("urgentStopReset"));
                String errorReset = String.valueOf(jsonObject.getInteger("errorReset"));
                String servoErrorCode = String.valueOf(jsonObject.getInteger("servoErrorCode"));
                fragment.tvBusErrorCode.setText(busErrorCode);
                fragment.tvRunErrorCode1.setText(runErrorCode1);
                fragment.tvRunErrorCode2.setText(runErrorCode2);
                fragment.tvUrgentStopReset.setText(urgentStopReset);
                fragment.tvErrorReset.setText(errorReset);
                fragment.tvErrorCode.setText(servoErrorCode);

                if (busErrorCode.equals("0")) {
                    fragment.tvBusErrorCode.setTextColor(Color.BLACK);
                } else {
                    fragment.tvBusErrorCode.setTextColor(Color.RED);
                }
                if (runErrorCode1.equals("0")) {
                    fragment.tvRunErrorCode1.setTextColor(Color.BLACK);
                } else {
                    fragment.tvRunErrorCode1.setTextColor(Color.RED);
                }
                if (runErrorCode2.equals("0")) {
                    fragment.tvRunErrorCode2.setTextColor(Color.BLACK);
                } else {
                    fragment.tvRunErrorCode2.setTextColor(Color.RED);
                }if (urgentStopReset.equals("0")) {
                    fragment.tvUrgentStopReset.setTextColor(Color.BLACK);
                } else {
                    fragment.tvUrgentStopReset.setTextColor(Color.RED);
                }if (errorReset.equals("0")) {
                    fragment.tvErrorReset.setTextColor(Color.BLACK);
                } else {
                    fragment.tvErrorReset.setTextColor(Color.RED);
                }if (servoErrorCode.equals("0")) {
                    fragment.tvErrorCode.setTextColor(Color.BLACK);
                } else {
                    fragment.tvErrorCode.setTextColor(Color.RED);
                }





                fragment.tvCurrentBoxId.setText(String.valueOf(jsonObject.getInteger("currentPos")));
                if(jsonObject.getInteger("currentPos")>20){
                    fragment.swCurrentBoxType.setChecked(true);
                } else {
                    fragment.swCurrentBoxType.setChecked(false);
                }

                char[] cmdChars = fragment.servoStatus.getCmdChar();
                StringBuffer stringBuffer = new StringBuffer();
                int count = 1;
                for(char cmdChar : cmdChars){
                    String hex = String.format("%04x", (int) cmdChar);
                    stringBuffer.append(hex).append(" ");
                    if(count % 10 == 0){
                        stringBuffer.append("\n");
                    }
                    count++;
                }
                fragment.tvCmdChar.setText(stringBuffer);

                fragment.tvQueryCount.setText(String.valueOf(fragment.queryCount));

                fragment.removeThreat(jsonObject.getInteger("runStatus"));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
