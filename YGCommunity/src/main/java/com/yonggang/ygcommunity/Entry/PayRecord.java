package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2017/8/18.
 */

public class PayRecord {


    /**
     * record : [{"meter_id":"29","money":"0.03","order_num":"ZHYLALIPAY1708183281503037386","pay_time":"2017-08-18","pay_type":"0","pay_way":"2","surface":"2","tab_name":"啊啊啊啊啊啊啊"}]
     * stime : 2017-8-17
     */

    private String stime;
    private List<Record> record;

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public List<Record> getRecord() {
        return record;
    }

    public void setRecord(List<Record> record) {
        this.record = record;
    }

    public static class Record implements Serializable {
        /**
         * meter_id : 29
         * money : 0.03
         * order_num : ZHYLALIPAY1708183281503037386
         * pay_time : 2017-08-18
         * pay_type : 0
         * pay_way : 2
         * surface : 2
         * tab_name : 啊啊啊啊啊啊啊
         */

        private String meter_id;
        private String money;
        private String order_num;
        private String pay_time;
        private int pay_type;
        private int pay_way;
        private String surface;
        private String tab_name;
        private String score;

        public String getMeter_id() {
            return meter_id;
        }

        public void setMeter_id(String meter_id) {
            this.meter_id = meter_id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public int getPay_way() {
            return pay_way;
        }

        public void setPay_way(int pay_way) {
            this.pay_way = pay_way;
        }

        public String getSurface() {
            return surface;
        }

        public void setSurface(String surface) {
            this.surface = surface;
        }

        public String getTab_name() {
            return tab_name;
        }

        public void setTab_name(String tab_name) {
            this.tab_name = tab_name;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
