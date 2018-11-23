package com.dcdz.drivers.dto;

import org.litepal.crud.LitePalSupport;

/**
 * Created by LJW on 2018/10/16.
 */
public class UserInfo extends LitePalSupport {

    private int id;
    private float data[] = new float[128];

    public float[] getData() {
        return data;
    }

    public void setData(float[] data) {
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
