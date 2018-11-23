package com.dcdz.drivers.demo.fragment.rotate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.Dictionary;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.drivers.config.ConfigManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * SIM卡发卡器
 */
public class SIMCardSenderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_simcard_send_port)
    TextView tvPort;
    @BindView(R.id.tv_simcard_send_vendor)
    TextView tvVendor;

    @BindView(R.id.rg_lane_status)
    RadioGroup rgLaneStatus;
    @BindView(R.id.rb_no_simcard)
    RadioButton rbNoSimcard;
    @BindView(R.id.rb_have_simcard_out)
    RadioButton rbHaveSimcardOut;
    @BindView(R.id.rb_have_simcard_read)
    RadioButton rbHaveSimcardRead;
    @BindView(R.id.rb_simcard_unknown)
    RadioButton rbSimcardUnknown;

    @BindView(R.id.rg_send_box_status)
    RadioGroup rgSendBoxStatus;
    @BindView(R.id.rb_no_simcard_send)
    RadioButton rbNoSimcardSend;
    @BindView(R.id.rb_simcard_less)
    RadioButton rbSimcardLess;
    @BindView(R.id.rb_simcard_more)
    RadioButton rbSimcardMore;
    @BindView(R.id.rb_simcard_unknown_send)
    RadioButton rbSimcardUnknownSend;

    @BindView(R.id.rg_recycle_box_status)
    RadioGroup rgRecycleBoxStatus;
    @BindView(R.id.rb_simcard_not_full)
    RadioButton rbSimcardNotFull;
    @BindView(R.id.rb_simcard_full)
    RadioButton rbSimcardFull;

    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.btn_send_to_read)
    Button btnRead;
    @BindView(R.id.btn_send_to_out)
    Button btnOut;
    @BindView(R.id.btn_recycle)
    Button btnRecycle;
    @BindView(R.id.btn_check_status)
    Button btnStatus;


    private ConfigManager configManager = ConfigManager.getInstance();
    private DeviceManager deviceManager = DeviceManager.getInstance();

    public SIMCardSenderFragment() {
        // Required empty public constructor
    }

    public static SIMCardSenderFragment newInstance() {
        Bundle args = new Bundle();
        SIMCardSenderFragment fragment = new SIMCardSenderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simcard_sender, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
        tvPort.setText(configManager.simCardSenderConfig.getPortName());
        tvVendor.setText(configManager.simCardSenderConfig.getVendorName());
    }

    @OnClick({R.id.btn_reset,R.id.btn_send_to_read,R.id.btn_send_to_out,R.id.btn_recycle,R.id.btn_check_status})
    public void clickBtn(View view){
        btnOut.setEnabled(false);
        btnRead.setEnabled(false);
        btnRecycle.setEnabled(false);
        btnReset.setEnabled(false);
        btnStatus.setEnabled(false);
        handler.postDelayed(enableButton, 4000);
        new UpdateCardStatus(view.getId()).execute();
    }

    Handler handler = new Handler();
    Runnable enableButton = new Runnable() {
        @Override
        public void run() {
            btnOut.setEnabled(true);
            btnRead.setEnabled(true);
            btnRecycle.setEnabled(true);
            btnReset.setEnabled(true);
            btnStatus.setEnabled(true);
        }
    };

    class UpdateCardStatus extends AsyncTask<Integer, Integer, Boolean>{

        private int viewId;

        private int laneStatus;
        private int cardBoxStatus;
        private int isCaptureBoxFull;

        public UpdateCardStatus(Integer id){
            this.viewId = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result){
                Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(getActivity(), "FAILED", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//        int laneStatus = cardSenderStatus.getbLaneStatus();
//        int cardBoxStatus = cardSenderStatus.getbCardBoxStatus();
//        boolean isCaptureBoxFull = cardSenderStatus.isCaptureBoxFull();

            //Log.d("CardStatus",cardSenderStatus.toString());
            switch (values[0]){
                case -1:
                    rgLaneStatus.check(R.id.rb_simcard_unknown);
                    break;
                case 0:
                    rgLaneStatus.check(R.id.rb_no_simcard);
                    break;
                case 1:
                    rgLaneStatus.check(R.id.rb_have_simcard_out);
                    break;
                case 2:
                    rgLaneStatus.check(R.id.rb_have_simcard_read);
                    break;
                default:
                    break;
            }
            switch (values[1]){
                case -1:
                    rgSendBoxStatus.check(R.id.rb_simcard_unknown_send);
                    break;
                case 0:
                    rgSendBoxStatus.check(R.id.rb_no_simcard_send);
                    break;
                case 1:
                    rgSendBoxStatus.check(R.id.rb_simcard_less);
                    break;
                case 2:
                    rgSendBoxStatus.check(R.id.rb_simcard_more);
                    break;
                default:
                    break;
            }
            switch (values[2]){
                case 1:
                    rgRecycleBoxStatus.check(R.id.rb_simcard_full);
                    break;
                case 0:
                    rgRecycleBoxStatus.check(R.id.rb_simcard_not_full);
                    break;
                default:
                    break;
            }
        }

        private void queryStatus(){
            String cardStatus = null;
            try {
                cardStatus = deviceManager.peripheralProxy.querySimCardSenderStatus();
                Log.d("CardStatus",cardStatus);
                JSONObject cardStatusJson = JsonUtils.toJSONObject(cardStatus);
                laneStatus = cardStatusJson.getInteger("bLaneStatus");//getJSONObject("data").
                cardBoxStatus = cardStatusJson.getInteger("bCardBoxStatus");//getJSONObject("data").
                isCaptureBoxFull = cardStatusJson.getInteger("captureBoxFull");//getJSONObject("data").
            } catch (DcdzSystemException e) {
                e.printStackTrace();
                Dictionary.toastInfo(getActivity(), "状态查询失败：" + e.getMessage(), Toast.LENGTH_SHORT);
            }

        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try{
                switch (viewId){
                    case R.id.btn_reset://复位
                        deviceManager.peripheralProxy.resetSimCardSender();
                        //this.queryStatus();
                        //publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_send_to_read://发卡到读卡位
                        deviceManager.peripheralProxy.moveSimCard2ReadPosition();
//                        this.queryStatus();
//                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_send_to_out://发卡到出卡口
                        deviceManager.peripheralProxy.moveSimCard2Front();
//                        this.queryStatus();
//                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_recycle://回收卡
                        deviceManager.peripheralProxy.recycleSimCard();
//                        this.queryStatus();
//                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_check_status:
                        this.queryStatus();
                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    default:
                        break;
                }
                return true;
            } catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
