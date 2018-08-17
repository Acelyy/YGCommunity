package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2017/5/3.
 */

public class Comments implements Serializable {

    private int len;
    private List<CommentsBean> comments;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public class CommentsBean implements Serializable {
        /**
         * child : [{"comments_conetnt":"子评论123","comments_id":"233"}]
         * comments_author : Acelyy
         * comments_content : 13131313
         * comments_id : 225
         * comments_time : 1493866416
         */

        private String comments_author;
        private String comments_content;
        private String comments_id;
        private String comments_time;
        private String face_url;
        private List<ChildBean> child;

        public String getFace_url() {
            return face_url;
        }

        public void setFace_url(String face_url) {
            this.face_url = face_url;
        }

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

        public String getComments_id() {
            return comments_id;
        }

        public void setComments_id(String comments_id) {
            this.comments_id = comments_id;
        }

        public String getComments_time() {
            return comments_time;
        }

        public void setComments_time(String comments_time) {
            this.comments_time = comments_time;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public class ChildBean implements Serializable {
            /**
             * comments_content : 子评论123
             * comments_id : 233
             */

            private String comments_author;
            private String comments_time;
            private String comments_content;
            private String comments_id;

            public String getComments_time() {
                return comments_time;
            }

            public void setComments_time(String comments_time) {
                this.comments_time = comments_time;
            }

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

            public String getComments_id() {
                return comments_id;
            }

            public void setComments_id(String comments_id) {
                this.comments_id = comments_id;
            }
        }
    }
}
