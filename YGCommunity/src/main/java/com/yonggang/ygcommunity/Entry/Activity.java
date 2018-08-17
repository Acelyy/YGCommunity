package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/6/14.
 */

public class Activity {
    /**
     * activity : [{"ac_type":"会议","address":"市区","date_end":"2017-06-30","date_start":"2017-06-01","id":10,"images":"http://10.10.3.213/actives_imgs/20170613/bbbbb20170613.png","state":"活动中","title":"13131313131313"},{"ac_type":"会议","address":"南丰","date_end":"2017-06-30","date_start":"2017-06-01","id":11,"images":"http://10.10.3.213/actives_imgs/20170613/bbbbb20170613.png","state":"报名中","title":"1313"},{"ac_type":"会议","address":"永联","date_end":"2017-06-30","date_start":"2017-06-01","id":12,"images":"http://10.10.3.213/actives_imgs/20170613/aaaa20170613.jpg","state":"报名中","title":"活动123123"},{"ac_type":"演出","address":"永联","date_end":"2017-06-30","date_start":"2017-06-13","id":13,"images":"http://10.10.3.213/actives_imgs/20170613/bbbbb20170613.png","state":"活动中","title":"新浪新闻"},{"ac_type":"展览","address":"南丰","date_end":"2017-06-30","date_start":"2017-06-01","id":15,"images":"http://10.10.3.213/actives_imgs/20170613/bbbbb20170613.png","state":"报名中","title":"88"},{"ac_type":"培训","address":"市区","date_end":"2017-06-30","date_start":"2017-06-01","id":16,"images":"http://10.10.3.213/actives_imgs/20170613/defautl20170613.jpg","state":"活动中","title":"1131313"},{"ac_type":"公益","address":"南丰","date_end":"2017-06-30","date_start":"2017-06-01","id":17,"images":"http://10.10.3.213/actives_imgs/20170613/aaaa20170613.jpg","state":"已结束","title":"新浪新闻"},{"ac_type":"展览","address":"永联","date_end":"2017-06-30","date_start":"2017-06-01","id":18,"images":"http://10.10.3.213/actives_imgs/20170613/aaaa20170613.jpg","state":"报名中","title":"8888"},{"ac_type":"演出","address":"市区","date_end":"2017-06-30","date_start":"2017-06-01","id":19,"images":"http://10.10.3.213/actives_imgs/20170613/aaaa20170613.jpg","state":"活动中","title":"8888"}]
     * total : 9
     */

    private int total;
    private List<ActivityBean> activity;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ActivityBean> getActivity() {
        return activity;
    }

    public void setActivity(List<ActivityBean> activity) {
        this.activity = activity;
    }

    public static class ActivityBean {
        /**
         * ac_type : 会议
         * address : 市区
         * date_end : 2017-06-30
         * date_start : 2017-06-01
         * id : 10
         * images : http://10.10.3.213/actives_imgs/20170613/bbbbb20170613.png
         * state : 活动中
         * title : 13131313131313
         */

        private String ac_type;
        private String address;
        private String date_end;
        private String date_start;
        private int id;
        private String images;
        private String state;
        private String title;

        public String getAc_type() {
            return ac_type;
        }

        public void setAc_type(String ac_type) {
            this.ac_type = ac_type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDate_end() {
            return date_end;
        }

        public void setDate_end(String date_end) {
            this.date_end = date_end;
        }

        public String getDate_start() {
            return date_start;
        }

        public void setDate_start(String date_start) {
            this.date_start = date_start;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
