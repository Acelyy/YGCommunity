package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/4/26.
 */

public class User {

    private String user_id;
    private String username;
    private String face_url;
    private String phone;
    private int auth;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    private int sex;

    public String getFace_url() {
        return face_url;
    }

    public void setFace_url(String face_url) {
        this.face_url = face_url;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAuth() {
        return auth;
    }

    public void setAuth(int auth) {
        this.auth = auth;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
