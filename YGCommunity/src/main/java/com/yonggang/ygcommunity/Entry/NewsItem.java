package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyangyang on 2017/4/18.
 */

public class NewsItem implements Serializable {

    private List<CarouselBean> carousel;
    private List<NewsBean> news;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CarouselBean> getCarousel() {
        return carousel;
    }

    public void setCarousel(List<CarouselBean> carousel) {
        this.carousel = carousel;
    }

    public List<NewsBean> getNews() {
        return news;
    }

    public void setNews(List<NewsBean> news) {
        this.news = news;
    }

    public static class CarouselBean {
        /**
         * carousel_pic : /uploads/20170418/bbb20170418.jpg
         * jump_url :
         * news_id : 137
         * newstitle : 习近平为网信工作划出这些重点
         */

        private String carousel_pic;
        private String jump_url;
        private String news_id;
        private String newstitle;
        private ArrayList<String> pics_group;//详情页的图片链接

        public String getCarousel_pic() {
            return carousel_pic;
        }

        public void setCarousel_pic(String carousel_pic) {
            this.carousel_pic = carousel_pic;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            this.news_id = news_id;
        }

        public String getNewstitle() {
            return newstitle;
        }

        public void setNewstitle(String newstitle) {
            this.newstitle = newstitle;
        }

        public ArrayList<String> getPics_group() {
            return pics_group;
        }

        public void setPics_group(ArrayList<String> pics_group) {
            this.pics_group = pics_group;
        }
    }

    public static class NewsBean implements Serializable {
        /**
         * collect : 0
         * comment_num : 0
         * isadvert : 0
         * jump_url : detail/news_id/143
         * news_id : 143
         * news_title : 旅游新闻1313
         * newssource : 百度新闻
         * tpicscount : 3
         * tpicsurl : ["/uploads/20170418/aaa20170418.jpg","/uploads/20170418/bbb20170418.jpg","/uploads/20170418/ccc20170418.jpg"]
         * title : 不裁切图片
         * content_num : 0
         */

        private int collect;
        private int comment_num;
        private int isadvert;
        private String jump_url;
        private String news_id;
        private String news_title;
        private String newssource;
        private int tpicscount;
        private String title;
        private int content_num;
        private double weight;
        private int newsstick;
        private List<String> tpicsurl;
        private int advsize;
        private int category_type;
        private int zan;//点赞数
        private ArrayList<String> pics_group;//详情页的图片链接
        private int readcount;
        private String audit_time;


        public ArrayList<String> getPics_group() {
            return pics_group;
        }

        public void setPics_group(ArrayList<String> pics_group) {
            this.pics_group = pics_group;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }

        public int getCategory_type() {
            return category_type;
        }

        public void setCategory_type(int category_type) {
            this.category_type = category_type;
        }

        public int getAdvsize() {
            return advsize;
        }

        public void setAdvsize(int advsize) {
            this.advsize = advsize;
        }

        public int getNewsstick() {
            return newsstick;
        }

        public void setNewsstick(int newsstick) {
            this.newsstick = newsstick;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getContent_num() {
            return content_num;
        }

        public void setContent_num(int content_num) {
            this.content_num = content_num;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getIsadvert() {
            return isadvert;
        }

        public void setIsadvert(int isadvert) {
            this.isadvert = isadvert;
        }

        public String getJump_url() {
            return jump_url;
        }

        public void setJump_url(String jump_url) {
            this.jump_url = jump_url;
        }

        public String getNews_id() {
            return news_id;
        }

        public void setNews_id(String news_id) {
            setWeight(Math.random());
            this.news_id = news_id;
        }

        public String getNews_title() {
            return news_title;
        }

        public void setNews_title(String news_title) {
            this.news_title = news_title;
        }

        public String getNewssource() {
            return newssource;
        }

        public void setNewssource(String newssource) {
            this.newssource = newssource;
        }

        public int getTpicscount() {
            return tpicscount;
        }

        public void setTpicscount(int tpicscount) {
            this.tpicscount = tpicscount;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getTpicsurl() {
            return tpicsurl;
        }

        public void setTpicsurl(List<String> tpicsurl) {
            this.tpicsurl = tpicsurl;
        }

        public int getReadcount() {
            return readcount;
        }

        public void setReadcount(int readcount) {
            this.readcount = readcount;
        }

        public String getAudit_time() {
            return audit_time;
        }

        public void setAudit_time(String audit_time) {
            this.audit_time = audit_time;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static NewsBean CarouselToBean(CarouselBean carousel) {
        NewsBean news = new NewsBean();
        news.setNews_id(carousel.getNews_id());
        news.setJump_url(carousel.getJump_url());
        news.setNews_title(carousel.getNewstitle());
        ArrayList<String> list_pic = new ArrayList<>();
        list_pic.add(carousel.getCarousel_pic());
        news.setTpicsurl(list_pic);
        news.setPics_group(carousel.getPics_group());
        return news;
    }
}
