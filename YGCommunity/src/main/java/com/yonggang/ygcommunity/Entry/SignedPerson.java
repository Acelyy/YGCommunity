package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/7/18.
 */

public class SignedPerson {


    /**
     * address : 永琪园
     * checked : 0
     * name : 李阳洋
     * phone : 18626359305
     * simple_id : 217
     */

    private String address;
    private int checked;
    private String name;
    private String phone;
    private String simple_id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
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

    public String getSimple_id() {
        return simple_id;
    }

    public void setSimple_id(String simple_id) {
        this.simple_id = simple_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
