package com.dcdz.drivers.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.Constant;
import com.dcdz.drivers.utils.CustomProgressBar;
import com.hzdongcheng.drivers.config.ConfigManager;
import com.hzdongcheng.drivers.peripheral.plc.model.RotateConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBox;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfBoxConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfDeskConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;

import java.util.ArrayList;
import java.util.List;

public class ShelfLayerAdapter extends BaseAdapter {

    public Context context;
    private LayoutInflater inflater;
    List<ShelfLayerConfig> shelfLayers;
    IModSucc iModSucc;
    public boolean enableEdit;
    List<ShelfDeskConfig> tbDesks = new ArrayList<>(); //货架
    ConfigManager configManager = ConfigManager.getInstance();
    RotateConfig rotateConfig = new RotateConfig();

    public ShelfLayerAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setLayerNumber(List<ShelfLayerConfig> shelfLayers){
        this.shelfLayers = shelfLayers;
    }

    public void setLayerModSucc(IModSucc iModSucc){
        this.iModSucc = iModSucc;
    }

    public void setEnableEdit(boolean enableEdit){
        this.enableEdit = enableEdit;
    }

    @Override
    public int getCount() {
        return shelfLayers == null ? 0 : shelfLayers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_shelf_layer, null);
            holder = new ViewHolder();
            holder.tvLayerNo = convertView.findViewById(R.id.tv_shelf_layer);
            holder.cusProgress = convertView.findViewById(R.id.cus_progress);
            holder.spLayer = convertView.findViewById(R.id.sp_shelf_layer);
            holder.spLayer.setTag(position);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<ShelfBoxConfig> vtbShelfPerLayers = shelfLayers.get(position).getBoxList();
        int num = position + 1;
        if (vtbShelfPerLayers.size() > 0){
            holder.tvLayerNo.setText("L"+ num);
            holder.cusProgress.setBoxSum(shelfLayers.get(position).getBoxCount());
            holder.cusProgress.setLayerNo(Constant.UPPER_CHAR[position] + "");
            String boxType = shelfLayers.get(position).getLayerType();
            if (boxType.equals("0")){
                holder.spLayer.setText("小");
            }else if (boxType.equals("2")){
                holder.spLayer.setText("大");
            } else if (boxType.equals("99")) {
                holder.spLayer.setText("不可用");
                holder.cusProgress.setEnabled(false);
            }
        }else {
            holder.tvLayerNo.setText("");
            holder.cusProgress.setVisibility(View.INVISIBLE);
        }

        //设置是否可以修改类型
        if (enableEdit){
            holder.spLayer.setEnabled(true);
        }else {
            holder.spLayer.setEnabled(false);
        }
        /*for (int i = 0; i < vtbShelfPerLayers.size(); i ++){
            String boxType = vtbShelfPerLayers.get(i).BoxType;
            if (boxType.equals("0")){
                holder.spLayer.setText("小");
            }else if (boxType.equals("2")){
                holder.spLayer.setText("大");
            }
        }*/

        holder.spLayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iModSucc.modSucc(holder.spLayer, (int) holder.spLayer.getTag());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        public TextView tvLayerNo;
        public CustomProgressBar cusProgress;
        public TextView spLayer;
    }

    public interface IModSucc{
        public void modSucc(TextView tv, int position);
    }

    //获取柜子信息
    public List<ShelfBoxConfig> queryLayer(int DeskNo, int LayerNo){
//        InParamTBShelfLayerQry inParam = new InParamTBShelfLayerQry();
//        inParam.DeskNo = DeskNo;
//        inParam.LayerNo = LayerNo;
//        try {
//            return DClient.getInstance().localBusiness.doBusiness(inParam);
//        } catch (DcdzSystemException e) {
//            e.printStackTrace();
//        }
        rotateConfig = configManager.rotateConfigModel;

        return rotateConfig.getDeskList().get(DeskNo).getLayerList().get(LayerNo).getBoxList();
    }
}
