package com.yonggang.ygcommunity.Entry;

/**
 * Created by liyangyang on 2017/7/24.
 */

public class JPush_Entry {

    private int type;
    private int nmsg;
    private String data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNmsg(int nmsg) {
        this.nmsg = nmsg;
    }

    public int getNmsg() {
        return nmsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
