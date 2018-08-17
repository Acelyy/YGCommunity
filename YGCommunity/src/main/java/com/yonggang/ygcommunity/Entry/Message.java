package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2017/7/5.
 */

public class Message {

    /**
     * message : [{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"},{"msg":"恭喜你注册成功","sender":"admin","stime":"17-07-05 03:10"}]
     * total : 40
     */

    private int total;
    private List<MessageBean> message;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean implements Serializable {
        /**
         * msg : 恭喜你注册成功
         * sender : admin
         * stime : 17-07-05 03:10
         */

        private String msg;
        private String sender;
        private String stime;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
