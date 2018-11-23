package com.dcdz.drivers.demo.fragment.elocker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.Proxy4Elocker;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.DriverAsyncTast;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.components.toolkits.utils.Log4jUtils;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.elocker.model.BoxConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.DeskConfig;
import com.hzdongcheng.drivers.peripheral.elocker.model.ViceMachineStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DeskFragment extends BaseFragment {
    Unbinder unbinder;

    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private List<RelativeLayout> llContainers = new ArrayList<>();
    private Map<String, Button> btBoxsMap = new HashMap<>();
    private ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future = null;
    Log4jUtils log4jUtils = Log4jUtils.createInstanse(this.getClass());
    private ConfigManager configManager = ConfigManager.getInstance();
    private Proxy4Elocker proxy4Elocker = Proxy4Elocker.getInstance();

    public DeskFragment() {

    }

    public static DeskFragment newInstance() {
        Bundle args = new Bundle();
        DeskFragment fragment = new DeskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        if (getView() == null) {
            return;
        }
        showAllBoxInfo();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (future != null) {
                future.cancel(true);
            }
        } else {
            future = service.scheduleWithFixedDelay(futureRunnable, 2, 2, TimeUnit.SECONDS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_desk, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (future != null) {
            future.cancel(true);
        }
        unbinder.unbind();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (future != null) {
            future.cancel(true);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!this.isHidden())
            future = service.scheduleWithFixedDelay(futureRunnable, 2, 2, TimeUnit.SECONDS);
    }

    Runnable futureRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                changeBoxStatus();
            } catch (DcdzSystemException e) {
                log4jUtils.error(e.getMessage());
            } catch (Exception e) {
                log4jUtils.error(e.getMessage());
            }
        }
    };

    //更新箱门状态
    private void changeBoxStatus() throws DcdzSystemException {
        if (configManager.eLockerConfigModel.getDeskCount() > 0) {
            List<DeskConfig> deskConfigs = configManager.eLockerConfigModel.getDeskList();
            ViceMachineStatus viceMachineStatus;
            for (DeskConfig deskConfig : deskConfigs) {
                String result = proxy4Elocker.queryStatusById((byte) Integer.parseInt(deskConfig.getBoardID()));
                viceMachineStatus = JsonUtils.toBean(result, ViceMachineStatus.class);
                if (viceMachineStatus == null)
                    continue;
                for (final BoxConfig boxConfig : deskConfig.getBoxList()) {
                    final byte OpenStatus = viceMachineStatus.getBoxStatusArray(Integer.parseInt(boxConfig.getBoxID()) - 1).getOpenStatus();
                    final byte Article = viceMachineStatus.getBoxStatusArray(Integer.parseInt(boxConfig.getBoxID()) - 1).getGoodsStatus();
                    getView().post(new Runnable() {
                        @Override
                        public void run() {
                            if ((OpenStatus == 0) && (Article == 0)) {
                                btBoxsMap.get(boxConfig.getDisplayName()).setBackgroundColor(getResources().getColor(R.color.closeNoting));
                            } else if ((OpenStatus == 1) && (Article == 0)) {
                                btBoxsMap.get(boxConfig.getDisplayName()).setBackgroundColor(getResources().getColor(R.color.openNothing));
                            } else if ((OpenStatus == 0) && (Article == 1)) {
                                btBoxsMap.get(boxConfig.getDisplayName()).setBackgroundColor(getResources().getColor(R.color.closeThing));
                            } else if ((OpenStatus == 1) && (Article == 1)) {
                                btBoxsMap.get(boxConfig.getDisplayName()).setBackgroundColor(getResources().getColor(R.color.openThing));
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * 显示所有箱门信息
     */
    private void showAllBoxInfo() {

        if (llContainers != null) {
            for (RelativeLayout relativeLayout : llContainers) {
                llContainer.removeView(relativeLayout);
            }
        }
        int deskHeight = 650;
        int top;
        int left = 5;
        byte board_id = 0;
        RelativeLayout myLayout = new RelativeLayout(this.getActivity());
        myLayout.setPadding(10, 10, 10, 10); // 单位: pixels
        myLayout.setBackgroundColor(Color.WHITE);

        if (configManager.eLockerConfigModel.getDeskCount() > 0) {
            List<DeskConfig> deskConfigs = configManager.eLockerConfigModel.getDeskList();
            for (DeskConfig deskConfig : deskConfigs) {
                List<BoxConfig> boxConfigs = deskConfig.getBoxList();
                if (left > 5)
                    left = 180 * board_id;
                top = 5;
                Button btallOpen = new Button(getActivity());
                btallOpen.setText(String.valueOf(getString(R.string.info_allopenbox)));
                btallOpen.setOnClickListener(btdeskClick);
                btallOpen.setTag(board_id);

                RelativeLayout.LayoutParams lpball = new RelativeLayout.LayoutParams(80, 50);
                lpball.addRule(RelativeLayout.ALIGN_LEFT);
                lpball.addRule(RelativeLayout.ALIGN_TOP);
                lpball.leftMargin = 180 * board_id + 50;
                lpball.topMargin = top;
                myLayout.addView(btallOpen, lpball);
                top += 52;
                board_id++;
                for (BoxConfig boxConfigModel : boxConfigs) {
                    //箱体按钮
                    Button button = new Button(getActivity());
                    button.setTextColor(getResources().getColor(R.color.white));
                    button.setText(String.valueOf(boxConfigModel.getDisplayName()));
                    button.setOnClickListener(btClick);
                    button.setTag(String.valueOf(boxConfigModel.getDisplayName()));
                    RelativeLayout.LayoutParams lpbt = new RelativeLayout.LayoutParams(85, 50);
                    lpbt.addRule(RelativeLayout.ALIGN_LEFT);
                    lpbt.addRule(RelativeLayout.ALIGN_TOP);
                    lpbt.leftMargin = left;
                    lpbt.topMargin = top;
                    btBoxsMap.put(boxConfigModel.getDisplayName(), button);
                    myLayout.addView(button, lpbt);
                    top += 52;
                    if ((top / 52) > (boxConfigs.size() % 2 == 1 ? (boxConfigs.size() / 2 + 1) : (boxConfigs.size() / 2))) {
                        left += 82;
                        top = 57;
                    } else if (top >= deskHeight) {
                        left += 82;
                        top = 57;
                    }
                }
            }
            RelativeLayout.LayoutParams lpbt = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            lpbt.addRule(RelativeLayout.CENTER_HORIZONTAL);
            lpbt.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            llContainer.addView(myLayout, lpbt);
            llContainers.add(myLayout);
        }
    }

    //打开单个箱门
    View.OnClickListener btClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            List<String> boxNos = new ArrayList<>();
            String boxNo = v.getTag().toString();
            boxNos.add(boxNo);
            DriverAsyncTast asyncTast = new DriverAsyncTast();
            asyncTast.activity = getActivity();
            asyncTast.boxNos = boxNos;
            asyncTast.execute("");
        }
    };
    //打开单组副柜
    View.OnClickListener btdeskClick = new View.OnClickListener() {
        public void onClick(View v) {
            int deskNo = Integer.parseInt(v.getTag().toString());
            List<String> boxNos = new ArrayList<>();
            DeskConfig deskConfigModel = configManager.eLockerConfigModel.getDeskList().get(deskNo);
            for (BoxConfig boxConfigModel : deskConfigModel.getBoxList()) {
                boxNos.add(boxConfigModel.getDisplayName());
            }
            DriverAsyncTast asyncTast = new DriverAsyncTast();
            asyncTast.activity = getActivity();
            asyncTast.boxNos = boxNos;
            asyncTast.execute("");
        }
    };
}
