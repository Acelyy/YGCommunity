package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/7/19.
 */

public class Contacts {

    /**
     * address : 的花都酒剑仙开心
     * id : 1
     * name : 自己家辛苦辛苦
     * phone : 15851641632
     */

    private String address;
    private String id;
    private String name;
    private String phone;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
