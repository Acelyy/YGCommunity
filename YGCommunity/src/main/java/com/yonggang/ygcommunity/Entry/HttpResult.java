package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/4/8.
 */

public class HttpResult<T> {
    private int flag;
    private String msg;
    private T data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
