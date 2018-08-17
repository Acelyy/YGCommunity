package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class GridEvent {

    /**
     * data : [{"id":"1","sbsj":"2018-07-20","sjbt":"事件1"},{"id":"3","sbsj":"2018-07-20","sjbt":"title"},{"id":"4","sbsj":"2018-07-20","sjbt":"事件1"},{"id":"5","sbsj":"2018-07-20","sjbt":"事件1"},{"id":"6","sbsj":"2018-07-20","sjbt":"事件1"},{"id":"24","sbsj":"2018-07-20","sjbt":"title"},{"id":"25","sbsj":"2018-07-20","sjbt":"title"},{"id":"26","sbsj":"2018-07-20","sjbt":"title"},{"id":"27","sbsj":"2018-07-20","sjbt":"title"},{"id":"28","sbsj":"2018-07-20","sjbt":"title"}]
     * flag : 1
     * msg : success
     * total : 10
     */

    private int flag;
    private String msg;
    private int total;
    private List<DataBean> data;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * sbsj : 2018-07-20
         * sjbt : 事件1
         */

        private String id;
        private String sbsj;
        private String sjbt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSbsj() {
            return sbsj;
        }

        public void setSbsj(String sbsj) {
            this.sbsj = sbsj;
        }

        public String getSjbt() {
            return sjbt;
        }

        public void setSjbt(String sjbt) {
            this.sjbt = sjbt;
        }
    }
}
