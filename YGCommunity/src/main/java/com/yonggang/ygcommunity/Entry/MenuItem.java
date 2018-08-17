package com.yonggang.ygcommunity.Entry;

/**
 * Created by liyangyang on 2017/7/3.
 */

public class MenuItem {
    private int res;
    private String text;
    private String des;

    public MenuItem(int res, String text, String des) {
        this.res = res;
        this.text = text;
        this.des = des;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
