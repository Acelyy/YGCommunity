package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class EventDetail {

    /**
     * info : {"bbsimg":["http://zhyl.yong-gang.com/bbs/20180618/imgs201806181702542087.jpg"],"bbsposition":"江苏省苏州市张家港市钢村路6号靠近锦江之家锦江宾馆","bbstime":"2018-06-18 17:57","bbstitle":"金手指广场边上护栏","content":"金手指广场边上护拦链条脱落","face_url":"http://zhyl.yong-gang.com/face/default/default.jpg","scount":"0","username":"p"}
     * trail : [{"slr":"张4","status":"已受理","stime":"2018-07-02"},{"bm":"永联惠民服务中心","comment":"23232323","status":"处理中","stime":"2018-07-02"}]
     */

    private Info info;
    private List<Trail> trail;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<Trail> getTrail() {
        return trail;
    }

    public void setTrail(List<Trail> trail) {
        this.trail = trail;
    }

    public static class Info {
        /**
         * img : ["http://zhyl.yong-gang.com/bbs/20180618/imgs201806181702542087.jpg"]
         * position : 江苏省苏州市张家港市钢村路6号靠近锦江之家锦江宾馆
         * time : 2018-06-18 17:57
         * title : 金手指广场边上护栏
         * content : 金手指广场边上护拦链条脱落
         * face_url : http://zhyl.yong-gang.com/face/default/default.jpg
         * scount : 0
         * username : p
         */

        private String position;
        private String time;
        private String title;
        private String content;
        private String faceurl;
        private String count;
        private String username;
        private List<String> img;

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFaceurl() {
            return faceurl;
        }

        public void setFaceurl(String faceurl) {
            this.faceurl = faceurl;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public List<String> getImg() {
            return img;
        }

        public void setImg(List<String> img) {
            this.img = img;
        }
    }

    public static class Trail {
        /**
         * slr : 张4
         * status : 已受理
         * stime : 2018-07-02
         * bm : 永联惠民服务中心
         * comment : 23232323
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
