package com.dcdz.drivers.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.Proxy4Rotation;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;

import java.util.List;

public class RotationControlListViewAdapter extends BaseAdapter {

    private Context context;
    private int lineNum;
    private LayoutInflater inflater;
    private List<ShelfLayerConfig> shelfLayerConfigs;
    private int productLocationId = 0;
    private ViewHolder viewHolder;
    private int salveNo = 1;
    private SlaveDeskStatus slaveDeskStatus;

    public RotationControlListViewAdapter(Context context, int lineNum, List<ShelfLayerConfig> shelfLayerConfigs, int salveNo) {
        this.context = context;
        this.lineNum = lineNum;
        this.shelfLayerConfigs = shelfLayerConfigs;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.salveNo = salveNo;
    }

    public void newData(int lineNum,List<ShelfLayerConfig> vtbShelfPerLayers, int salveNo){
        this.lineNum = lineNum;
        this.shelfLayerConfigs = vtbShelfPerLayers;
        this.salveNo = salveNo;
    }

    public void newData(String data){
        slaveDeskStatus = JsonUtils.toBean(data,SlaveDeskStatus.class);
    }

    @Override
    public int getCount() {
        return lineNum;
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
    public View getView(final int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.layout_line_in_list_view_rotation_control, null);
            viewHolder = new ViewHolder();
            viewHolder.doorIdView = view.findViewById(R.id.tv_rotation_control_door_id);
            viewHolder.boxTypeView = view.findViewById(R.id.tv_rotation_control_box_type);
            viewHolder.doorStatus = view.findViewById(R.id.tv_rotation_control_door_status);
            viewHolder.openDoorButton = view.findViewById(R.id.btn_rotation_control_open_door);
            viewHolder.boxStatus = view.findViewById(R.id.tv_rotation_control_box_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //门编号
        /*if(position>3&& salveNo ==1){
            viewHolder.doorIdView.setText("#"+String.valueOf(position+2)); //主柜5号箱门不可用 跳过

        } else {*/
            viewHolder.doorIdView.setText("#"+String.valueOf(position+1));
//        }


        //箱类型
        if("0".equals(shelfLayerConfigs.get(position).getLayerType())){
            viewHolder.boxTypeView.setText("小");
        } else if ("2".equals(shelfLayerConfigs.get(position).getLayerType())) {
            viewHolder.boxTypeView.setText("大");
        } else if ("99".equals(shelfLayerConfigs.get(position).getLayerType())) {
            viewHolder.boxTypeView.setText("不可用");
        }

        //门操作
        viewHolder.openDoorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    /*if (position > 3 && salveNo == 1) {
                        Proxy4Rotation.openDoor(salveNo, position + 1);
                    } else {*/
                        Proxy4Rotation.openDoor(salveNo, position);
//                    }
                } catch (DcdzSystemException e) {
                    Toast.makeText(context, "开门失败:"+e.getErrorTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        String positionString = String.valueOf(position);
        if(slaveDeskStatus!=null){
            //门状态
            switch (slaveDeskStatus.getBoxStatusMap().get(positionString).getOpenStatus()){
                case 0:
                    viewHolder.doorStatus.setText("关闭");
                    break;
                case 1:
                    viewHolder.doorStatus.setText("打开");
                    break;
                case 9:
                    viewHolder.doorStatus.setText("未知");
                    break;
            }
            //箱状态
            switch (slaveDeskStatus.getBoxStatusMap().get(positionString).getGoodsStatus()){
                case 0:
                    viewHolder.boxStatus.setText("无物");
                    break;
                case 1:
                    viewHolder.boxStatus.setText("有物");
                    break;
                case 9:
                    viewHolder.boxStatus.setText("未知");
                    break;
            }
        }

        //当前为副柜一时禁用第五层
        if (salveNo == 1 && position == 4) {
            viewHolder.doorIdView.setEnabled(false);
            viewHolder.boxTypeView.setEnabled(false);
            viewHolder.doorStatus.setEnabled(false);
            viewHolder.openDoorButton.setEnabled(false);
            viewHolder.boxStatus.setEnabled(false);
        }

        return view;
    }

    static class ViewHolder {
        public TextView doorIdView;
        public TextView boxTypeView;
        public TextView doorStatus;
        public Button openDoorButton;
        public TextView boxStatus;
    }
}
