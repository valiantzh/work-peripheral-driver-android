package com.dcdz.drivers.demo.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dcdz.drivers.R;
import com.dcdz.drivers.utils.Dictionary;
import com.hzdongcheng.drivers.config.ConfigManager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shalom on 2018/03/19.
 */

public class DeskConfingAdapter extends BaseAdapter {

    public Context context;
    private LayoutInflater inflater;
    public static int boxCount;
    public static Map<Integer, Integer> desknumMap = new LinkedHashMap<>();
    public static Map<Integer, String> deskstatueMap = new LinkedHashMap<>();
    public static Map<Integer, String> deskCodeMap = new LinkedHashMap<>();
    public static Map<Integer, String> serialCodeMap = new LinkedHashMap<>();
    private ConfigManager configManager = ConfigManager.getInstance();

    public DeskConfingAdapter(Context context, int boxCount) {
        this.context = context;
        DeskConfingAdapter.boxCount = boxCount;
        inflater = LayoutInflater.from(context);
        if (boxCount <= 0) {
            DeskConfingAdapter.boxCount = configManager.eLockerConfigModel.getDeskCount();
            for (Integer i = 0; i < getCount(); i++) {
                desknumMap.put(i, configManager.eLockerConfigModel.getDeskList().get(i).getBoxCount());
                deskstatueMap.put(i, configManager.eLockerConfigModel.getDeskList().get(i).getDisplayName());
                String assetscode = configManager.eLockerConfigModel.getDeskList().get(i).getAssetsCode();
                if (assetscode.length() > 16) {
                    deskCodeMap.put(i, assetscode.substring(0, 14));
                    serialCodeMap.put(i, assetscode.substring(15, assetscode.length()));
                }
            }
        } else {
            for (Integer i = 0; i < boxCount; i++) {
                desknumMap.put(i, 0);
                deskstatueMap.put(i, "");
                deskCodeMap.put(i, "");
                serialCodeMap.put(i, "00000");
            }
        }
    }

    @Override
    public int getCount() {
        return boxCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_desk_config, null);
            holder = new ViewHolder();
            holder.tvNo = (TextView) convertView.findViewById(R.id.tv_no);
            holder.btnBoxNum = (Button) convertView.findViewById(R.id.btn_box_num);
            holder.btnBoxType = (Button) convertView.findViewById(R.id.btn_box_type);
            holder.bt_desk_code = (Button) convertView.findViewById(R.id.bt_desk_code);
            holder.bt_serial_code = (Button) convertView.findViewById(R.id.bt_serial_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNo.setText(position + "");
        holder.btnBoxNum.setText(desknumMap.get(position) + "");
        holder.btnBoxType.setText(deskstatueMap.get(position));
        holder.bt_desk_code.setText("DCDZ");
        deskCodeMap.put(position, "DCDZ");
        holder.bt_serial_code.setText(serialCodeMap.get(position));

        holder.btnBoxNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setSingleChoiceItems(R.array.box_counts, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectBox = context.getResources().getStringArray(R.array.box_counts)[which];
                        holder.btnBoxNum.setText(selectBox);
                        desknumMap.put(position, Integer.parseInt(selectBox));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
        holder.btnBoxType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setSingleChoiceItems(R.array.box_type, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String selectBox = context.getResources().getStringArray(R.array.box_type)[which];
                        if (!deskstatueMap.containsValue(selectBox)) {
                            holder.btnBoxType.setText(context.getString(R.string.info_default).equals(selectBox) ? String.valueOf(position) : selectBox);
                            deskstatueMap.put(position, context.getString(R.string.info_default).equals(selectBox) ? String.valueOf(position) : selectBox);
                        } else {
                            Dictionary.toastInfo(context, context.getString(R.string.info_deskname), Toast.LENGTH_SHORT);
                        }
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
//        holder.bt_serial_code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final EditText inputServer = new EditText(context);
//                inputServer.setSingleLine(true);
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle(context.getString(R.string.info_serialnumber)).setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("Cancel", null);
//                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        String serialString = inputServer.getText().toString();
//                        if (StringUtils.isNotEmpty(serialString)) {
//                            Pattern pattern = Pattern.compile("^[0-9]*$");
//                            Matcher isNum = pattern.matcher(serialString);
//                            if (isNum.matches()) {
//                                if (serialString.length() <= 6) {
//                                    serialString = String.format("%06d", Integer.parseInt(serialString));
//                                    holder.bt_serial_code.setText(serialString);
//                                    serialCodeMap.put(position, serialString);
//                                }
//                            }
//                        }
//                    }
//                });
//                builder.show();
//            }
//        });
        return convertView;
    }

    static class ViewHolder {
        public TextView tvNo;
        public Button btnBoxNum;
        public Button btnBoxType;
        public Button bt_desk_code;
        public Button bt_serial_code;
    }
}
