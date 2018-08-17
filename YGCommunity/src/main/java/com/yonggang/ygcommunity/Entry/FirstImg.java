package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/7/31.
 */

public class FirstImg {

    /**
     * img_url : ["http://zhyl.yong-gang.com/firstimg/v1-1-gs10801882-1080-1882-.jpg","http://zhyl.yong-gang.com/firstimg/v1-2-ylc10801882-1080-1882-.jpg","http://zhyl.yong-gang.com/firstimg/v1-3-yljs10801882-1080-1882-.jpg"]
     * total : 3
     */

    private int total;
    private List<String> img_url;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
