package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by liyangyang on 2017/6/27.
 */

public class Home implements Serializable {

    /**
     * address : 张家港步行街1
     * id : 1
     * tab_name : 我家
     */

    private String address;
    private String id;
    private String tab_name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
