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

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.Proxy4Peripheral;
import com.dcdz.drivers.utils.Dictionary;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.utils.BaseFragment;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LJW on 2018/3/2.
 * IC卡发卡器
 */
public class ICCardSenderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.tv_iccard_send_port)
    TextView tvIcPort;
    @BindView(R.id.tv_iccard_send_vendor)
    TextView tvIcVendor;

    @BindView(R.id.rg_ic_lane_status)
    RadioGroup rgIcLaneStatus;
    @BindView(R.id.rb_no_iccard)
    RadioButton rbNoIccard;
    @BindView(R.id.rb_have_iccard_out)
    RadioButton rbHaveIccardOut;
    @BindView(R.id.rb_have_iccard_read)
    RadioButton rbHaveIccardRead;
    @BindView(R.id.rb_iccard_unknown)
    RadioButton rbIccardUnknown;

    @BindView(R.id.rg_ic_send_box_status)
    RadioGroup rgIcSendBoxStatus;
    @BindView(R.id.rb_no_iccard_send)
    RadioButton rbNoIccardSend;
    @BindView(R.id.rb_iccard_less)
    RadioButton rbIccardLess;
    @BindView(R.id.rb_iccard_more)
    RadioButton rbIccardMore;
    @BindView(R.id.rb_iccard_unknown_send)
    RadioButton rbIccardUnknownSend;

    @BindView(R.id.rg_ic_recycle_box_status)
    RadioGroup rgIcRecycleBoxStatus;
    @BindView(R.id.rb_iccard_not_full)
    RadioButton rbIccardNotFull;
    @BindView(R.id.rb_iccard_full)
    RadioButton rbIccardFull;

    @BindView(R.id.btn_ic_reset)
    Button btnReset;
    @BindView(R.id.btn_ic_send_to_out)
    Button btnOut;
    @BindView(R.id.btn_ic_recycle)
    Button btnRecycle;
    @BindView(R.id.btn_ic_query)
    Button btnQuery;

    private ConfigManager configManager = ConfigManager.getInstance();
    private DeviceManager deviceManager = DeviceManager.getInstance();




    public ICCardSenderFragment() {
        // Required empty public constructor
    }

    public static ICCardSenderFragment newInstance() {
        Bundle args = new Bundle();
        ICCardSenderFragment fragment = new ICCardSenderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iccard_sender, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
        tvIcPort.setText(configManager.cardSenderConfig.getPortName());
        tvIcVendor.setText(configManager.cardSenderConfig.getVendorName());
    }

    @OnClick({R.id.btn_ic_reset,R.id.btn_ic_send_to_out,R.id.btn_ic_recycle,R.id.btn_ic_query})
    public void click(View view){
        btnOut.setEnabled(false);
        btnQuery.setEnabled(false);
        btnRecycle.setEnabled(false);
        btnReset.setEnabled(false);
        handler.postDelayed(enableButton, 4000);
        new UpdateCardStatus(view.getId()).execute();
    }

    Handler handler = new Handler();
    Runnable enableButton = new Runnable() {
        @Override
        public void run() {
            btnOut.setEnabled(true);
            btnRecycle.setEnabled(true);
            btnReset.setEnabled(true);
            btnQuery.setEnabled(true);
        }
    };

    class UpdateCardStatus extends AsyncTask<Integer, Integer, Boolean> {

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
            switch (values[0]){
                case 9:
                    rgIcLaneStatus.check(R.id.rb_iccard_unknown);
                    break;
                case 0:
                    rgIcLaneStatus.check(R.id.rb_no_iccard);
                    break;
                case 1:
                    rgIcLaneStatus.check(R.id.rb_have_iccard_out);
                    break;
                case 2:
                    rgIcLaneStatus.check(R.id.rb_have_iccard_read);
                    break;
                default:
                    break;
            }
            switch (values[1]){
                case 9:
                    rgIcSendBoxStatus.check(R.id.rb_iccard_unknown_send);
                    break;
                case 0:
                    rgIcSendBoxStatus.check(R.id.rb_no_iccard_send);
                    break;
                case 1:
                    rgIcSendBoxStatus.check(R.id.rb_iccard_less);
                    break;
                case 2:
                    rgIcSendBoxStatus.check(R.id.rb_iccard_more);
                    break;
                default:
                    break;
            }
            switch (values[2]){
                case 1:
                    rgIcRecycleBoxStatus.check(R.id.rb_iccard_full);
                    break;
                case 0:
                    rgIcRecycleBoxStatus.check(R.id.rb_iccard_not_full);
                    break;
                default:
                    break;
            }
        }

        private void queryStatus(){
            String cardStatus = null;
            try {
                cardStatus = deviceManager.peripheralProxy.queryCardSenderStatus();
                Log.d("CardStatus",cardStatus.toString());
                com.alibaba.fastjson.JSONObject cardStatusJson = JsonUtils.toJSONObject(cardStatus.toString());
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
                    case R.id.btn_ic_reset://复位
                        deviceManager.peripheralProxy.resetCardSender();
//                        this.queryStatus();
//                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_ic_send_to_out://发卡到出卡口
                        deviceManager.peripheralProxy.moveCard2Front();
                        //this.queryStatus();
//                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_ic_query://查询
                        queryStatus();
//                        this.queryStatus();
                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
                        break;
                    case R.id.btn_ic_recycle://回收卡
                        deviceManager.peripheralProxy.recycleCard();
//                        this.queryStatus();
//                        publishProgress(laneStatus,cardBoxStatus,isCaptureBoxFull);
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
