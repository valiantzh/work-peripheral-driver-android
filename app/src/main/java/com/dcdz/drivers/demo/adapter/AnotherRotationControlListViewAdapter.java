package com.dcdz.drivers.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.demo.business.Proxy4Rotation;
import com.hzdongcheng.components.toolkits.exception.DcdzSystemException;
import com.hzdongcheng.components.toolkits.utils.JsonUtils;
import com.hzdongcheng.drivers.peripheral.plc.model.ShelfLayerConfig;
import com.hzdongcheng.drivers.peripheral.plc.model.SlaveDeskStatus;

import java.util.List;

public class AnotherRotationControlListViewAdapter extends BaseAdapter {

    private Context context;
    private int lineNum;
    private LayoutInflater inflater;
    private List<ShelfLayerConfig> shelfLayerConfigs;
    private int productLocationId = 0;
    private int salveNo = 1;
    private SlaveDeskStatus slaveDeskStatus;

    public AnotherRotationControlListViewAdapter(Context context, int lineNum, List<ShelfLayerConfig> shelfLayerConfigs, int salveNo) {
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
        view = inflater.inflate(R.layout.layout_line_in_list_view_rotation_control_another, null);
        final EditText productIdView = view.findViewById(R.id.et_rotation_control_product_id);
        Button executeMoveButton = view.findViewById(R.id.btn_rotation_control_execute_move);

        //操作
        executeMoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productIdView.getText().toString().isEmpty()) {
                    if (Integer.parseInt(productIdView.getText().toString()) > 0 && Integer.parseInt(productIdView.getText().toString()) < 21) {
                        productLocationId = position * 100 + Integer.parseInt(productIdView.getText().toString());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Proxy4Rotation.openBox4Access(salveNo, productLocationId);
                                } catch (DcdzSystemException e) {
                                    Toast.makeText(context, "开箱失败："+ e.getErrorTitle(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).start();
                    } else {
                        Toast.makeText(context, "请输入正确的货位号。", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请输入正确的货位号。", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //当前为副柜一时禁用第五层
        if (salveNo == 1 && position == 4) {
            productIdView.setEnabled(false);
            executeMoveButton.setEnabled(false);
        }

        return view;
    }
}
