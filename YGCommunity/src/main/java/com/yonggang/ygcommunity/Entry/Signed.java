package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/9/5.
 */

public class Signed {
    private int total;
    private List<User> more;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<User> getMore() {
        return more;
    }

    public void setMore(List<User> more) {
        this.more = more;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
