package com.dcdz.drivers.utils;

/**
 * Created by Peace on 2017/9/21.
 */

public class DialogEvent {
    public final static int WAITING = 0;
    public final static int TIPS = 1;

    public int dialogMode;
    public boolean isShow = true;
    public String msg;
    public String title;

    public DialogEvent(int dialogMode, String msg, String title, boolean isShow) {
        this.dialogMode = dialogMode;
        this.msg = msg;
        this.title = title;
        this.isShow = isShow;
    }

    public DialogEvent(int dialogMode, boolean isShow) {
        this.dialogMode = dialogMode;
        this.isShow = isShow;
    }
}
