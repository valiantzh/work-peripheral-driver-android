package com.dcdz.drivers.demo.fragment.rotate;


import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dcdz.drivers.R;
import com.dcdz.drivers.databinding.FragmentShelfStateBinding;
import com.dcdz.drivers.demo.adapter.ShelfAddRecyclerAdapter;
import com.dcdz.drivers.demo.adapter.ShelfLayerAdapter;
import com.dcdz.drivers.demo.business.DeviceManager;
import com.dcdz.drivers.utils.BaseFragment;
import com.dcdz.drivers.utils.Constant;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShelfStateFragment extends BaseFragment {

//    @BindView(R.id.btn_add)
//    Button btnAdd;
//    @BindView(R.id.ib_del)
//    ImageButton ibDel;
//    @BindView(R.id.lv_shelf)
//    ListView lvShelf;
    @BindView(R.id.rv_shelf)
    TextView rvShelf;
    @BindView(R.id.lv_shelf_layer)
    ListView lvShelfLayer;
    @BindView(R.id.rv_shelf_add)
    RecyclerView rvShelfAdd;
    @BindView(R.id.btn_edit)
    Button btnEdit;

    Unbinder unbinder;

    FragmentShelfStateBinding binding;

    ShelfLayerAdapter shelfLayerAdapter;
    ShelfAddRecyclerAdapter shelfAddAdapter;

    int shelfPosition = 0;
    boolean enableEdit = false;
    List<ShelfDeskConfig> tbDesks = new ArrayList<>(); //货架
    List<ShelfLayerConfig> shelfLayers = new ArrayList<>(); //层
    String shelfNames; //货架名

    ConfigManager configManager = ConfigManager.getInstance();
    RotateConfig rotateConfig = new RotateConfig();

    Logger log = Logger.getLogger(ShelfStateFragment.class);

    private PopupWindow mPopWindow;

    public ShelfStateFragment() {
        // Required empty public constructor
    }

    public static ShelfStateFragment newInstance() {
        Bundle args = new Bundle();
        ShelfStateFragment fragment = new ShelfStateFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_shelf_state,container,false);
        unbinder = ButterKnife.bind(this, binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void initView(Bundle savedInstanceState, View contentView) {
        rotateConfig = configManager.rotateConfigModel;
        shelfLayerAdapter = new ShelfLayerAdapter(getActivity());
        shelfAddAdapter = new ShelfAddRecyclerAdapter(getActivity());

        queryDesk();
        //如果数据库为空，默认添加一个空柜子
        if (tbDesks == null || tbDesks.size() == 0){
            addShelf(5);
        }

        queryShelfInfo(0);

        rvShelfAdd.setAdapter(shelfAddAdapter);
        shelfAddAdapter.setAddItemClickListener(new ShelfAddRecyclerAdapter.IAddClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                addShelf(5);
            }
        });
        shelfAddAdapter.setDelItemClickListener(new ShelfAddRecyclerAdapter.IDelClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                queryDesk();
                tbDesks.remove(position);
                log.info("删除货架成功");
                queryDesk();
            }
        });
        shelfAddAdapter.setiShelfClickListener(new ShelfAddRecyclerAdapter.IShelfClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                shelfPosition = position;
                queryDesk();
                queryShelfInfo(position);
                rvShelf.setText(shelfNames);
            }
        });
        shelfAddAdapter.setiSpinnerClickListener(new ShelfAddRecyclerAdapter.ISpinnerClickListener() {
            @Override
            public void onItemClick(TextView view, final int shelfNo) {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_mod_layer, null);
                mPopWindow = new PopupWindow(contentView);
                mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView five = contentView.findViewById(R.id.tv_five);
                TextView six = contentView.findViewById(R.id.tv_six);
                five.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modShellLayerNum(5, shelfNo);
                        queryShelfInfo(shelfPosition);
                    }
                });
                six.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modShellLayerNum(6, shelfNo);
                        queryShelfInfo(shelfPosition);
                    }
                });
                mPopWindow.setOutsideTouchable(true);
                mPopWindow.setBackgroundDrawable(new BitmapDrawable());
                mPopWindow.showAsDropDown(view,40,0);
            }
        });

        queryDesk();

        lvShelfLayer.setAdapter(shelfLayerAdapter);
        shelfLayerAdapter.setLayerModSucc(new ShelfLayerAdapter.IModSucc() {
            @Override
            public void modSucc(TextView tv, final int position) {
                View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_window_mod_type, null);
                mPopWindow = new PopupWindow(contentView);
                mPopWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

                TextView big = contentView.findViewById(R.id.tv_big);
                TextView small = contentView.findViewById(R.id.tv_small);
                TextView none = contentView.findViewById(R.id.tv_none);
                big.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modLayerType(position, shelfPosition,"2");
                    }
                });
                small.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modLayerType(position, shelfPosition,"0");
                    }
                });
                none.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modLayerType(position, shelfPosition,"99");
                    }
                });
                mPopWindow.setOutsideTouchable(true);
                mPopWindow.setBackgroundDrawable(new BitmapDrawable());
                mPopWindow.showAsDropDown(tv,40,0);

            }
        });



    }

    @OnClick({R.id.btn_edit})
    public void onClick(View view){
        switch (view.getId()){
//            case R.id.btn_add:
//                int layerCount = 5;
//                int layerType = 2;
//                this.addDesk(layerCount,layerType);
//                break;
            case R.id.btn_edit:
                if(!rotateConfig.getDeskList().isEmpty()){
                    enableEdit = !enableEdit;
                    queryShelfInfo(shelfPosition);
                    shelfLayerAdapter.setEnableEdit(enableEdit);
                    shelfLayerAdapter.setLayerNumber(shelfLayers);
                    lvShelfLayer.setAdapter(shelfLayerAdapter);
                    shelfLayerAdapter.notifyDataSetChanged();

                    if (enableEdit){
                        binding.btnEdit.setText("完成");
                    }else{
                        binding.btnEdit.setText("编辑");
                    }
                    allShelf();
                    break;
                }

        }
    }


    public void addDesk(int layerCount, int layerType){
        int currentDeskCount = rotateConfig.getDeskCount();
        List<ShelfDeskConfig> rotateConfigList = rotateConfig.getDeskList();
        List<ShelfLayerConfig> shelfDeskConfigList = new ArrayList<>();
        for(int layerIndex=0;layerIndex<layerCount;layerIndex++) {
            List<ShelfBoxConfig> shelfLayerConfigList = new ArrayList<>();
            if(layerType == 0){
                for(int boxIndex=0;boxIndex<20;boxIndex++) {
                    ShelfBoxConfig shelfBoxConfig = new ShelfBoxConfig();
                    shelfBoxConfig.setBoxID(String.valueOf(boxIndex));
                    if (boxIndex < 10) {
                        shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[currentDeskCount]+"-"+Constant.UPPER_CHAR[layerIndex]+"0"+(boxIndex+1));
                    } else {
                        shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[currentDeskCount]+"-"+Constant.UPPER_CHAR[layerIndex]+(boxIndex+1));
                    }
                    shelfLayerConfigList.add(shelfBoxConfig);
                }
            } else if(layerType == 2){
                for(int boxIndex=0;boxIndex<15;boxIndex++) {
                    ShelfBoxConfig shelfBoxConfig = new ShelfBoxConfig();
                    shelfBoxConfig.setBoxID(String.valueOf(boxIndex));
                    if (boxIndex < 10) {
                        shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[currentDeskCount]+"-"+Constant.UPPER_CHAR[layerIndex]+"0"+(boxIndex+1));
                    } else {
                        shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[currentDeskCount]+"-"+Constant.UPPER_CHAR[layerIndex]+(boxIndex+1));
                    }
                    shelfLayerConfigList.add(shelfBoxConfig);
                }
            }
            ShelfLayerConfig shelfLayerConfig = new ShelfLayerConfig();
            shelfLayerConfig.setBoxList(shelfLayerConfigList);
            shelfLayerConfig.setLayerID(String.valueOf(layerIndex));
            shelfLayerConfig.setDisplayName("Layer #"+String.valueOf(layerIndex));
            shelfLayerConfig.setLayerType(String.valueOf(layerType));
            shelfDeskConfigList.add(shelfLayerConfig);
        }
        ShelfDeskConfig shelfDeskConfig = new ShelfDeskConfig();
        shelfDeskConfig.setLayerList(shelfDeskConfigList);
        shelfDeskConfig.setAssetsCode("1000"+String.valueOf(currentDeskCount));
        shelfDeskConfig.setDisplayName("Desk #"+String.valueOf(currentDeskCount));
        shelfDeskConfig.setSlaveID(String.valueOf(currentDeskCount));
        rotateConfigList.add(shelfDeskConfig);
        rotateConfig.setDeskList(rotateConfigList);
        configManager.rotateConfigModel = rotateConfig;
        configManager.createOrUpdateDeviceConfigFile();
    }


    private void allShelf() {
        shelfAddAdapter.setShelfData(tbDesks);
        shelfAddAdapter.setEnableEdit(enableEdit);
        rvShelfAdd.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvShelfAdd.setAdapter(shelfAddAdapter);
        shelfAddAdapter.notifyDataSetChanged();
    }

    //查询货架信息
    public void queryShelfInfo(int position){
        if(!rotateConfig.getDeskList().isEmpty()){
            List<ShelfLayerConfig> mShelfPerLayers = rotateConfig.getDeskList().get(position).getLayerList();
            shelfLayers = mShelfPerLayers;
            queryDesk();
        }
    }

    //查询货架
    private void queryDesk() {
        tbDesks = rotateConfig.getDeskList();
        log.info("查询货架成功");
        allShelf();
        shelfNames = "货架" + Constant.UPPER_CHAR[shelfPosition];
        shelfLayerAdapter.setLayerNumber(shelfLayers);
        lvShelfLayer.setAdapter(shelfLayerAdapter);
        shelfLayerAdapter.notifyDataSetChanged();
    }

    //添加货架
    private void addShelf(int layerNum) {
        this.addDesk(layerNum,2);
        log.info("添加货架成功");
        DeviceManager.getInstance().loadBoxInfo();
        queryDesk();
    }

    //修改货架层数
    public void modShellLayerNum(int layerNum, int deskIndex){
        List<ShelfDeskConfig> rotateConfigList = rotateConfig.getDeskList();
        List<ShelfLayerConfig> shelfDeskConfigList = rotateConfigList.get(deskIndex).getLayerList();
        int currentLayerNum = rotateConfigList.get(deskIndex).getLayerCount();

        if (layerNum == 6 && currentLayerNum == 5) {
            List<ShelfBoxConfig> shelfLayerConfigList = new ArrayList<>();
            for(int boxIndex=0;boxIndex<15;boxIndex++) {
                ShelfBoxConfig shelfBoxConfig = new ShelfBoxConfig();
                shelfBoxConfig.setBoxID(String.valueOf(boxIndex));
                if (boxIndex < 10) {
                    shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[deskIndex]+"-"+Constant.UPPER_CHAR[currentLayerNum]+"0"+(boxIndex+1));
                } else {
                    shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[deskIndex]+"-"+Constant.UPPER_CHAR[currentLayerNum]+(boxIndex+1));
                }
                shelfLayerConfigList.add(shelfBoxConfig);
            }
            ShelfLayerConfig shelfLayerConfig = new ShelfLayerConfig();
            shelfLayerConfig.setBoxList(shelfLayerConfigList);
            shelfLayerConfig.setLayerID("5");
            shelfLayerConfig.setDisplayName("Layer #5");
            shelfLayerConfig.setLayerType(String.valueOf("2"));
            shelfDeskConfigList.add(shelfLayerConfig);
            rotateConfigList.get(deskIndex).setLayerList(shelfDeskConfigList);
            rotateConfig.setDeskList(rotateConfigList);
            configManager.rotateConfigModel = rotateConfig;
            configManager.createOrUpdateDeviceConfigFile();
            log.info("修改货架层成功");
        } else if (layerNum == 5 && currentLayerNum == 6) {
            shelfDeskConfigList.remove(shelfDeskConfigList.size()-1);
            rotateConfigList.get(deskIndex).setLayerList(shelfDeskConfigList);
            rotateConfig.setDeskList(rotateConfigList);
            configManager.rotateConfigModel = rotateConfig;
            configManager.createOrUpdateDeviceConfigFile();
            log.info("修改货架层成功");
        } else {
            log.info("修改货架层失败");
        }
        queryDesk();
        DeviceManager.getInstance().loadBoxInfo();
        mPopWindow.dismiss();
    }

    public void modLayerType(int layerIndex, int deskIndex, String type){
        List<ShelfDeskConfig> rotateConfigList = rotateConfig.getDeskList();
        List<ShelfLayerConfig> shelfDeskConfigList = rotateConfigList.get(deskIndex).getLayerList();
        ShelfLayerConfig shelfLayerConfig = shelfDeskConfigList.get(layerIndex);
        String currentType = shelfLayerConfig.getLayerType();
        if(type.equals("2") && currentType.equals("0")){
            List<ShelfBoxConfig> shelfBoxConfigList = shelfLayerConfig.getBoxList();
            for(int i=19;i>14;i--){
                shelfBoxConfigList.remove(i);
            }
            shelfDeskConfigList.get(layerIndex).setBoxList(shelfBoxConfigList);
            shelfDeskConfigList.get(layerIndex).setLayerType(type);
            rotateConfigList.get(deskIndex).setLayerList(shelfDeskConfigList);
            rotateConfig.setDeskList(rotateConfigList);
            configManager.rotateConfigModel = rotateConfig;
            configManager.createOrUpdateDeviceConfigFile();
        } else if(type.equals("0")&&currentType.equals("2")){
            List<ShelfBoxConfig> shelfLayerConfigList = shelfLayerConfig.getBoxList();
            for(int i=15;i<20;i++){
                ShelfBoxConfig shelfBoxConfig = new ShelfBoxConfig();
                shelfBoxConfig.setBoxID(String.valueOf(i));
                shelfBoxConfig.setDisplayName(Constant.UPPER_CHAR[deskIndex]+"-"+Constant.UPPER_CHAR[layerIndex]+(i+1));
                shelfLayerConfigList.add(shelfBoxConfig);
            }
            shelfDeskConfigList.get(layerIndex).setBoxList(shelfLayerConfigList);
            shelfDeskConfigList.get(layerIndex).setLayerType(type);
            rotateConfigList.get(deskIndex).setLayerList(shelfDeskConfigList);
            rotateConfig.setDeskList(rotateConfigList);
            configManager.rotateConfigModel = rotateConfig;
            configManager.createOrUpdateDeviceConfigFile();
        } else if (type.equals("99")) {
            List<ShelfBoxConfig> shelfLayerConfigList = shelfLayerConfig.getBoxList();
            shelfDeskConfigList.get(layerIndex).setBoxList(shelfLayerConfigList);
            shelfDeskConfigList.get(layerIndex).setLayerType(type);
            rotateConfigList.get(deskIndex).setLayerList(shelfDeskConfigList);
            rotateConfig.setDeskList(rotateConfigList);
            configManager.rotateConfigModel = rotateConfig;
            configManager.createOrUpdateDeviceConfigFile();
        } else if (type.equals("0") && currentType.equals("99")) {
            List<ShelfBoxConfig> shelfLayerConfigList = shelfLayerConfig.getBoxList();
            shelfDeskConfigList.get(layerIndex).setBoxList(shelfLayerConfigList);
            shelfDeskConfigList.get(layerIndex).setLayerType(type);
            rotateConfigList.get(deskIndex).setLayerList(shelfDeskConfigList);
            rotateConfig.setDeskList(rotateConfigList);
            configManager.rotateConfigModel = rotateConfig;
            configManager.createOrUpdateDeviceConfigFile();
        }


        queryShelfInfo(shelfPosition); //先查询货架信息
        shelfLayerAdapter.setLayerNumber(shelfLayers);
        lvShelfLayer.setAdapter(shelfLayerAdapter);
        shelfLayerAdapter.notifyDataSetChanged();
        DeviceManager.getInstance().loadBoxInfo();
        mPopWindow.dismiss();
    }

}
