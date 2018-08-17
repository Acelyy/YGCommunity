package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyangyang on 2017/7/4.
 */

public class Notice {

    /**
     * notice : [{"content":"公告1313公告1313公告1313","id":"12","title":"公告1313"},{"content":"公告1313公告1313公告1313","id":"13","title":"公告1313公告1313公告1313"},{"content":"公告1313公告1313公告1313","id":"14","title":"公告1313"},{"content":"昨天的公告昨天的公告昨天的公告昨天的公告","id":"15","title":"昨天的公告"},{"content":"昨天的公告昨天的公告昨天的公告","id":"16","title":"昨天的公告"},{"content":"明天的公告明天的公告明天的公告明天的公告明天的公告明天的公告","id":"17","title":"明天的公告"},{"content":"明天的公告明天的公告明天的公告明天的公告","id":"18","title":"明天的公告明天的公告"},{"content":"明天的公告明天的公告v","id":"19","title":"明天的公告明天的公告"},{"content":"5-10的公告测试5-10的公告测试5-10的公告测试5-10的公告测试5-10的公告测试5-10的公告测试5-10的公告测试5-10的公告测试5-10的公告测试","id":"20","title":"5-10的公告测试"}]
     * total : 9
     */

    private int total;
    private List<NoticeBean> notice;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<NoticeBean> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeBean> notice) {
        this.notice = notice;
    }

    public static class NoticeBean implements Serializable{
        /**
         * content : 公告1313公告1313公告1313
         * id : 12
         * title : 公告1313
         */

        private String content;
        private String id;
        private String title;
        private String stime;
        private String jump_url;
        private String bm;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStime() {
            return stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public String getBm() {
            return bm;
        }

        public void setBm(String bm) {
            this.bm = bm;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
