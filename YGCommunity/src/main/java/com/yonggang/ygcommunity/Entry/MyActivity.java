package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/8/14.
 */

public class MyActivity {
    private int total;
    private List<Activity.ActivityBean> my_activity;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Activity.ActivityBean> getMy_activity() {
        return my_activity;
    }

    public void setMy_activity(List<Activity.ActivityBean> my_activity) {
        this.my_activity = my_activity;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
