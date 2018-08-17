package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by liyangyang on 2017/6/27.
 */

public class Expense implements Serializable{

    /**
     * address : 张家港步行街1
     * id : 1
     * surface : 001
     * tab_name : 我家
     * type : 0
     */

    private String address;
    private String id;
    private String surface;
    private String tab_name;
    private int type;
    private String type_name;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

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

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getTab_name() {
        return tab_name;
    }

    public void setTab_name(String tab_name) {
        this.tab_name = tab_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
