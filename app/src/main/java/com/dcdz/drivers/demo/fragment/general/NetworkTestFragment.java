package com.dcdz.drivers.demo.fragment.general;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.BaseFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 网络测试
 */
public class NetworkTestFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.et_net_test_address)
    EditText etNetTestAddress;
    @BindView(R.id.btn_net_test_start)
    Button btnNetTestStart;
    @BindView(R.id.tv_net_test_result)
    TextView tvNetTestResult;
    @BindView(R.id.et_net_test_times)
    EditText etNetTestTimes;


    public NetworkTestFragment() {
        // Required empty public constructor
    }

    public static NetworkTestFragment newInstance() {
        Bundle args = new Bundle();
        NetworkTestFragment fragment = new NetworkTestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network_test, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
    }

    @OnClick(R.id.btn_net_test_start)
    public void onBtnClick(View view){
        btnNetTestStart.setEnabled(false);
        new Thread(ping).start();
    }

    void setPingResult(String pingResult) {
        tvNetTestResult.setText(pingResult);
        Log.d("Ping",pingResult);
        btnNetTestStart.setEnabled(true);
    }


    public Runnable ping = new Runnable() {

        private String pingResult;
        @Override
        public void run() {
            pingResult = "";
            String netAddress = etNetTestAddress.getText().toString();
            String testTimes = etNetTestTimes.getText().toString();
            Runtime runtime = Runtime.getRuntime();

            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c "+testTimes+" "+netAddress);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(ipProcess.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    pingResult = pingResult + inputLine + "\n";
                }
                in.close();
                ipProcess.waitFor();
            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    setPingResult(pingResult);
                }
            });

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
