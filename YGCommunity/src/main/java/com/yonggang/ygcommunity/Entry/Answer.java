package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/7/6.
 */

public class Answer {

    /**
     * comments : [{"comments_author":"Acelyy","comments_content":"撸撸撸补补啊","comments_time":"2017-06-16","face_url":"http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg","news":{"jump_url":"http://zhyl.yong-gang.com/zhyl/Home/Index/detail/news_id/129537","news_id":"129537","news_title":"2020告别贫困！习近平要求限时完成的目标"},"parent":[{"comments_author":"Acelyy","comments_content":"撸撸撸撸撸撸撸","comments_time":"2017-06-16"}]}]
     * total : 27
     */

    private int total;
    private List<CommentsBean> comments;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * comments_author : Acelyy
         * comments_content : 撸撸撸补补啊
         * comments_time : 2017-06-16
         * face_url : http://zhyl.yong-gang.com/face/20170705/imgs20170705180875896.jpg
         * news : {"jump_url":"http://zhyl.yong-gang.com/zhyl/Home/Index/detail/news_id/129537","news_id":"129537","news_title":"2020告别贫困！习近平要求限时完成的目标"}
         * parent : [{"comments_author":"Acelyy","comments_content":"撸撸撸撸撸撸撸","comments_time":"2017-06-16"}]
         */

        private String comments_author;
        private String comments_content;
        private String comments_time;
        private String face_url;
        private NewsItem.NewsBean news;
        private List<ParentBean> parent;

        public String getComments_author() {
            return comments_author;
        }

        public void setComments_author(String comments_author) {
            this.comments_author = comments_author;
        }

        public String getComments_content() {
            return comments_content;
        }

        public void setComments_content(String comments_content) {
            this.comments_content = comments_content;
        }

        public String getComments_time() {
            return comments_time;
        }

        public void setComments_time(String comments_time) {
            this.comments_time = comments_time;
        }

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

        public NewsItem.NewsBean getNews() {
            return news;
        }

        public void setNews(NewsItem.NewsBean news) {
            this.news = news;
        }

        public List<ParentBean> getParent() {
            return parent;
        }

        public void setParent(List<ParentBean> parent) {
            this.parent = parent;
        }

        public static class ParentBean {
            /**
             * comments_author : Acelyy
             * comments_content : 撸撸撸撸撸撸撸
             * comments_time : 2017-06-16
             */

            private String comments_author;
            private String comments_content;
            private String comments_time;

            public String getComments_author() {
                return comments_author;
            }

            public void setComments_author(String comments_author) {
                this.comments_author = comments_author;
            }

            public String getComments_content() {
                return comments_content;
            }

            public void setComments_content(String comments_content) {
                this.comments_content = comments_content;
            }

            public String getComments_time() {
                return comments_time;
            }

            public void setComments_time(String comments_time) {
                this.comments_time = comments_time;
            }
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
