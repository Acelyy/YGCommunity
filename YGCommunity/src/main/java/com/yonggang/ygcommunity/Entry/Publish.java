package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/7/17.
 */

public class Publish {

    /**
     * publish : [{"bbscontent":"767648484","bbstime":"17-07-04 10:35","bbstitle":"12：6757","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","username":"Acelyy2"},{"bbscontent":"且行且珍惜明","bbstime":"17-07-04 12:39","bbstitle":"4466","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","username":"Acelyy2"},{"bbscontent":"里high","bbstime":"17-07-04 19:13","bbstitle":"哈哈哈哈哈","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","username":"Acelyy2"},{"bbscontent":"烧烤","bbstime":"17-07-04 19:42","bbstitle":"烧烤","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","username":"Acelyy2"},{"bbscontent":"看","bbstime":"17-07-04 19:45","bbstitle":"时间bug19:45","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","username":"Acelyy2"},{"bbscontent":"263","bbstime":"17-07-12 08:36","bbstitle":"258","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","username":"Acelyy2"}]
     * total : 6
     */

    private int total;
    private List<Bbs.BbsBean> publish;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Bbs.BbsBean> getPublish() {
        return publish;
    }

    public void setPublish(List<Bbs.BbsBean> publish) {
        this.publish = publish;
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
