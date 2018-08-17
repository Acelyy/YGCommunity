package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/10/13.
 */

public class Address {

    /**
     * address : 数据中心二楼
     * connect_id : 18
     * real_name : 李阳洋
     * rephone : 18626359305
     * user_id : 35
     */

    private String address;
    private String connect_id;
    private String real_name;
    private String rephone;
    private String user_id;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getConnect_id() {
        return connect_id;
    }

    public void setConnect_id(String connect_id) {
        this.connect_id = connect_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getRephone() {
        return rephone;
    }

    public void setRephone(String rephone) {
        this.rephone = rephone;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
