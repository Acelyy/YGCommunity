package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyangyang on 2017/5/27.
 */

public class Bbs {

    /**
     * bbs : [{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"29","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"30","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"31","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"32","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"33","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"34","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"35","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"36","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"37","is_finish":"0"},{"bbs_author":"kakaxinow","bbscontent":" 今日1231313","bbsimg":["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"],"bbstime":"17-05-27","bbstitle":" 今日1231313","id":"38","is_finish":"0"}]
     * total : 14
     */

    private int total;
    private List<BbsBean> bbs;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<BbsBean> getBbs() {
        return bbs;
    }

    public void setBbs(List<BbsBean> bbs) {
        this.bbs = bbs;
    }

    public static class BbsBean implements Serializable {
        /**
         * bbs_author : kakaxinow
         * bbscontent :  今日1231313
         * bbsimg : ["http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/2017052420170527.jpg","http://10.10.3.213/bbs/20170527/ddd20170527.jpg"]
         * bbstime : 17-05-27
         * bbstitle :  今日1231313
         * id : 29
         * is_finish : 0
         *
         */

        private String bbs_author;
        private String face_url;
        private String bbscontent;
        private String bbstime;
        private String bbstitle;
        private String id;
        private String is_finish;
        private String position;
        private String answers_count;
        private ArrayList<String> bbsimg;

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public String getBbs_author() {
            return bbs_author;
        }

        public void setBbs_author(String bbs_author) {
            this.bbs_author = bbs_author;
        }

        public String getBbscontent() {
            return bbscontent;
        }

        public void setBbscontent(String bbscontent) {
            this.bbscontent = bbscontent;
        }

        public String getBbstime() {
            return bbstime;
        }

        public void setBbstime(String bbstime) {
            this.bbstime = bbstime;
        }

        public String getBbstitle() {
            return bbstitle;
        }

        public void setBbstitle(String bbstitle) {
            this.bbstitle = bbstitle;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_finish() {
            return is_finish;
        }

        public void setIs_finish(String is_finish) {
            this.is_finish = is_finish;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getPosition() {
            return position;
        }

        public void setAnswers_count(String answers_count) {
            this.answers_count = answers_count;
        }

        public String getAnswers_count() {
            return answers_count;
        }

        public ArrayList<String> getBbsimg() {
            return bbsimg;
        }

        public void setBbsimg(ArrayList<String> bbsimg) {
            this.bbsimg = bbsimg;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
