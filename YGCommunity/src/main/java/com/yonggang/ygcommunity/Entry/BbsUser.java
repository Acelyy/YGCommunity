package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/6/8.
 */

public class BbsUser {

    /**
     * total : 2
     * user_answers : [{"answers_author":"kakaxinow","answers_time":"2017-06-08 09:33","face_url":"/bbs/20170607/imgs201706071466570451.jpg","user_answers":"用户评论123"},{"answers_author":"kakaxinow","answers_time":"2017-06-08 09:33","face_url":"/bbs/20170607/imgs201706071466570451.jpg","user_answers":"用户评论123"}]
     */

    private int total;
    private List<UserAnswersBean> user_answers;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UserAnswersBean> getUser_answers() {
        return user_answers;
    }

    public void setUser_answers(List<UserAnswersBean> user_answers) {
        this.user_answers = user_answers;
    }

    public static class UserAnswersBean {
        /**
         * answers_author : kakaxinow
         * answers_time : 2017-06-08 09:33
         * face_url : /bbs/20170607/imgs201706071466570451.jpg
         * user_answers : 用户评论123
         */

        private String answers_author;
        private String answers_time;
        private String face_url;
        private String user_answers;
        private List<Children> children;

        public String getAnswers_author() {
            return answers_author;
        }

        public void setAnswers_author(String answers_author) {
            this.answers_author = answers_author;
        }

        public String getAnswers_time() {
            return answers_time;
        }

        public void setAnswers_time(String answers_time) {
            this.answers_time = answers_time;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public String getUser_answers() {
            return user_answers;
        }

        public void setUser_answers(String user_answers) {
            this.user_answers = user_answers;
        }

        public List<Children> getChildren() {
            return children;
        }

        public void setChildren(List<Children> children) {
            this.children = children;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    public static class Children {
        private String user_answers;
        private String answers_time;
        private String answers_author;

        public String getUser_answers() {
            return user_answers;
        }

        public void setUser_answers(String user_answers) {
            this.user_answers = user_answers;
        }

        public String getAnswers_time() {
            return answers_time;
        }

        public void setAnswers_time(String answers_time) {
            this.answers_time = answers_time;
        }

        public String getAnswers_author() {
            return answers_author;
        }

        public void setAnswers_author(String answers_author) {
            this.answers_author = answers_author;
        }
    }
}
