package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/6/8.
 */

public class HotLine {
    /**
     * phonelist : [{"name":"永钢集团1","phone":"12345678"},{"name":"永钢集团2","phone":"12345678"}]
     * total : 2
     */

    private int total;
    private List<PhonelistBean> phonelist;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PhonelistBean> getPhonelist() {
        return phonelist;
    }

    public void setPhonelist(List<PhonelistBean> phonelist) {
        this.phonelist = phonelist;
    }

    public static class PhonelistBean {
        /**
         * name : 永钢集团1
         * phone : 12345678
         */

        private String name;
        private String phone;
        private String marks;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMarks() {
            return marks;
        }

        public void setMarks(String marks) {
            this.marks = marks;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
