package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/8/3.
 */

public class Free {

    /**
     * jfmsg : [{"annual":"年份2017","price":"总价3.000","quarter":"季度1","unit_price":"3.000"},{"annual":"年份2017","price":"总价3.000","quarter":"季度2","unit_price":"3.000"}]
     * pinfos : {"area":"恒创园","owner":"李阳洋"}
     * total_price : 6.00
     */

    private Pinfos pinfos;
    private String total_price;
    private List<JfmsgBean> jfmsg;

    public Pinfos getPinfos() {
        return pinfos;
    }

    public void setPinfos(Pinfos pinfos) {
        this.pinfos = pinfos;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public List<JfmsgBean> getJfmsg() {
        return jfmsg;
    }

    public void setJfmsg(List<JfmsgBean> jfmsg) {
        this.jfmsg = jfmsg;
    }

    public static class Pinfos {
        /**
         * area : 恒创园
         * owner : 李阳洋
         */

        private String area;
        private String owner;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }
    }

    public static class JfmsgBean {
        /**
         * annual : 年份2017
         * price : 总价3.000
         * quarter : 季度1
         * unit_price : 3.000
         */

        private String annual;
        private String price;
        private String quarter;
        private String unit_price;
        private String id;

        public String getAnnual() {
            return annual;
        }

        public void setAnnual(String annual) {
            this.annual = annual;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getQuarter() {
            return quarter;
        }

        public void setQuarter(String quarter) {
            this.quarter = quarter;
        }

        public String getUnit_price() {
            return unit_price;
        }

        public void setUnit_price(String unit_price) {
            this.unit_price = unit_price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
