package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/10/17.
 */

public class Signin {

    /**
     * sign_num : 1
     * stime : 17
     */

    private String sign_num;
    private int stime;

    public String getSign_num() {
        return sign_num;
    }

    public void setSign_num(String sign_num) {
        this.sign_num = sign_num;
    }

    public int getStime() {
        return stime;
    }

    public void setStime(int stime) {
        this.stime = stime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
