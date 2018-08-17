package com.yonggang.ygcommunity.Entry;

/**
 * Created by liyangyang on 2017/7/24.
 */

public class JPush_news {

    /**
     * news_id : 42
     * category_type : 0
     * newstitle : 8888
     * jump_url : http://zhyl.yong-gang.com/zhyl/Home/Index/detail/news_id/42
     */

    private String news_id;
    private int category_type;
    private String newstitle;
    private String jump_url;

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public int getCategory_type() {
        return category_type;
    }

    public void setCategory_type(int category_type) {
        this.category_type = category_type;
    }

    public String getNewstitle() {
        return newstitle;
    }

    public void setNewstitle(String newstitle) {
        this.newstitle = newstitle;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }
}
