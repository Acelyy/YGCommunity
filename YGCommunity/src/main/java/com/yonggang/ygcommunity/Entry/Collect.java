package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/6/22.
 */

public class Collect {

    /**
     * collect : [{"jump_url":"http://10.10.3.106/zhyl/Home/index/detail/news_id/214","news_id":214}]
     * total : 0
     */

    private int total;
    private List<CollectBean> collect;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CollectBean> getCollect() {
        return collect;
    }

    public void setCollect(List<CollectBean> collect) {
        this.collect = collect;
    }

    public static class CollectBean {
        /**
         * jump_url : http://10.10.3.106/zhyl/Home/index/detail/news_id/214
         * news_id : 214
         */

        private String jump_url;
        private String news_id;
        private String newstitle;
        private List<String> tpicsurl;

        public List<String> getTpicsurl() {
            return tpicsurl;
        }

        public void setTpicsurl(List<String> tpicsurl) {
            this.tpicsurl = tpicsurl;
        }

        public String getNewstitle() {
            return newstitle;
        }

        public void setNewstitle(String newstitle) {
            this.newstitle = newstitle;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
