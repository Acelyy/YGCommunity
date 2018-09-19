package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class GridEventDetail {

    /**
     * detail : {"name":"wgy1","sbsj":"2018-07-20 13:08","sjbt":"title","sjdw":"江苏省苏州市张家港市妙丰公路6号靠近永钢集团办公大楼","sjms":"司法解释发链接斯洛伐克教务科加入了看见我开了就而坚朗五金儿科软连接瓦尔基里"}
     * trail : [{"bm":"经济合作社","status":"核查通知","time":"2018-08-02 10:30"},{"bm":"经济合作社","status":"处理结束(平台)","time":"2018-08-02 10:30"},{"bm":"经济合作社","status":"已签收","time":"2018-08-02 10:30"}]
     */

    private DetailBean detail;
    private List<TrailBean> trail;

    public DetailBean getDetail() {
        return detail;
    }

    public void setDetail(DetailBean detail) {
        this.detail = detail;
    }

    public List<TrailBean> getTrail() {
        return trail;
    }

    public void setTrail(List<TrailBean> trail) {
        this.trail = trail;
    }

    public static class DetailBean {
        /**
         * name : wgy1
         * sbsj : 2018-07-20 13:08
         * sjbt : title
         * sjdw : 江苏省苏州市张家港市妙丰公路6号靠近永钢集团办公大楼
         * sjms : 司法解释发链接斯洛伐克教务科加入了看见我开了就而坚朗五金儿科软连接瓦尔基里
         */
        private List<String> imgs;
        private String name;
        private String sbsj;
        private String sjbt;
        private String sjdw;
        private String sjms;

        public List<String> getImgs() {
            return imgs;
        }

        public void setImgs(List<String> imgs) {
            this.imgs = imgs;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getSjdw() {
            return sjdw;
        }

        public void setSjdw(String sjdw) {
            this.sjdw = sjdw;
        }

        public String getSjms() {
            return sjms;
        }

        public void setSjms(String sjms) {
            this.sjms = sjms;
        }
    }

    public static class TrailBean {
        /**
         * bm : 经济合作社
         * status : 核查通知
         * time : 2018-08-02 10:30
         */

        private String slr;
        private String status;
        private String stime;
        private String bm;
        private String comment;

        public String getSlr() {
            return slr;
        }

        public void setSlr(String slr) {
            this.slr = slr;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public String getBm() {
            return bm;
        }

        public void setBm(String bm) {
            this.bm = bm;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
