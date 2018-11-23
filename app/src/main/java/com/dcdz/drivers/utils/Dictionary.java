package com.dcdz.drivers.utils;

import android.content.Context;
import android.widget.Toast;

import com.dcdz.drivers.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Cainiao
 * description
 *
 * @author Peace
 * @date 2018/1/12.
 */

public final class Dictionary {

    public static Map<String, Integer> portMap = new HashMap<>();
    public static Map<String, String> deskCodeMap = new HashMap<>();
    public static Map<String, String> mainAllStatus = new HashMap<>();
    public static Map<String, String> desktype = new HashMap<>();
    public static Map<String, String> deskgroupMap = new HashMap<>();
    public static Map<String, Integer> deskimage = new HashMap<>();
    public static Map<String, Integer> boardMap = new HashMap<>();
    public static Map<String, String> groupMap = new HashMap<>();
    static
    {
        portMap.put("COM1", 0);
        portMap.put("COM2", 1);
        portMap.put("COM3", 2);
        portMap.put("COM4", 3);
        portMap.put("COM5", 4);
        portMap.put("COM6", 5);
        portMap.put("ttyUSB0",6);
        portMap.put("/dev/ttyimx3",7);
        portMap.put("/dev/ttymxc6",8);

        deskCodeMap.put("Z1(主柜_绿色箭头向右)", "Z1");
        deskCodeMap.put("Z2(主柜_绿色箭头向左)", "Z2");
        deskCodeMap.put("Z3(主柜_纯蓝色)", "Z3");
        deskCodeMap.put("A(副柜_深蓝顶_灰绿蓝)", "A");
        deskCodeMap.put("B(副柜_深蓝顶_蓝)", "B");
        deskCodeMap.put("C(副柜_深蓝顶_蓝_AILOGO)", "C");
        deskCodeMap.put("D(副柜_浅蓝顶_灰绿蓝)", "D");


        mainAllStatus.put("m9", "未知");
        mainAllStatus.put("s9", "未知");
        mainAllStatus.put("p9", "未知");
        mainAllStatus.put("m1", "开");
        mainAllStatus.put("s1", "报警");
        mainAllStatus.put("p1", "备电");
        mainAllStatus.put("m0", "关");
        mainAllStatus.put("s0", "正常");
        mainAllStatus.put("p0", "市电");

        deskgroupMap.put("A-Z1-A-A(一拖三_主机绿色箭头向右)", "A-Z1-A-A");
        deskgroupMap.put("B-Z2-B-C(一拖三_主机绿色箭头向左)", "B-Z2-B-C");
        deskgroupMap.put("D-Z3-D-D(一拖三_纯蓝色AI)", "D-Z3-D-D");
        deskgroupMap.put("A-Z1-A-A-A(一拖四_主机绿色箭头向右)", "A-Z1-A-A-A");
        deskgroupMap.put("B-Z2-B-B-C(一拖四_绿色箭头向左)", "B-Z2-B-B-C");
        deskgroupMap.put("自定义", "");

        groupMap.put("A-Z1-A-A", "A-Z1-A-A(一拖三_主机绿色箭头向右)");
        groupMap.put("B-Z2-B-C", "B-Z2-B-C(一拖三_主机绿色箭头向左)");
        groupMap.put("D-Z3-D-D", "D-Z3-D-D(一拖三_纯蓝色AI)");
        groupMap.put("A-Z1-A-A-A", "A-Z1-A-A-A(一拖四_主机绿色箭头向右)");
        groupMap.put("B-Z2-B-B-C", "B-Z2-B-B-C(一拖四_绿色箭头向左)");


        desktype.put("Z1", "M-DC-00-00-08C");
        desktype.put("Z2", "M-DC-00-01-08C");
        desktype.put("Z3", "M-DC-00-02-08C");
        desktype.put("A", "S-DC-00-30-0CC");
        desktype.put("B", "S-DC-00-31-0CC");
        desktype.put("C", "S-DC-00-32-0CC");
        desktype.put("D", "S-DC-00-33-0CC");

        deskimage.put("Z1", R.drawable.main_z1);
        deskimage.put("Z2", R.drawable.mian_z2);
        deskimage.put("Z3", R.drawable.mian_z3);
        deskimage.put("A", R.drawable.desk_a);
        deskimage.put("B", R.drawable.desk_b);
        deskimage.put("C", R.drawable.desk_c);
        deskimage.put("D", R.drawable.desk_d);

        boardMap.put("Z", 0);
        boardMap.put("Z1", 0);
        boardMap.put("Z2", 0);
        boardMap.put("Z3", 0);
        boardMap.put("A", 1);
        boardMap.put("B", 2);
        boardMap.put("C", 3);
        boardMap.put("D", 4);
        boardMap.put("E", 5);
        boardMap.put("F", 6);
        boardMap.put("G", 7);
    }

    //empty load function
    public static void load() {
    }

    //信息提示
    public static void toastInfo(Context context, String info,int length) {
        Toast toast = Toast.makeText(context, info, length);
        toast.show();
    }
}
