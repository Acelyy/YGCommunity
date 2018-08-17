package com.yonggang.ygcommunity.Entry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2017/7/25.
 */

public class Gift {

    /**
     * gift : [{"gift":"爱心超市20元券","gift_id":"1","gift_img":"http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg","price":"1000","score":"200"},{"gift":"爱心超市50元券","gift_id":"2","gift_img":"http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg","price":"1000","score":"500"},{"gift":"永联大米（20斤）","gift_id":"3","gift_img":"http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg","price":"1000","score":"600"},{"gift":"农耕园入园门票*2","gift_id":"4","gift_img":"http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg","price":"1000","score":"700"},{"gift":"天天鲜购物卡   （100元）","gift_id":"5","gift_img":"http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg","price":"1000","score":"1000"},{"gift":"天天鲜购物卡   （200元）","gift_id":"6","gift_img":"http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg","price":"1000","score":"1900"}]
     * total : 6
     */

    private int total;
    private List<GiftBean> gift;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<GiftBean> getGift() {
        return gift;
    }

    public void setGift(List<GiftBean> gift) {
        this.gift = gift;
    }

    public static class GiftBean implements Serializable{
        /**
         * gift : 爱心超市20元券
         * gift_id : 1
         * gift_img : http://zhyl.yong-gang.com/uploads/20170724/ylg.jpg
         * price : 1000
         * score : 200
         */

        private String gift;
        private String gift_id;
        private String gift_img;
        private String price;
        private String score;
        private int record;

        public int getRecord() {
            return record;
        }

        public void setRecord(int record) {
            this.record = record;
        }

        public String getGift() {
            return gift;
        }

        public void setGift(String gift) {
            this.gift = gift;
        }

        public String getGift_id() {
            return gift_id;
        }

        public void setGift_id(String gift_id) {
            this.gift_id = gift_id;
        }

        public String getGift_img() {
            return gift_img;
        }

        public void setGift_img(String gift_img) {
            this.gift_img = gift_img;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }
}
