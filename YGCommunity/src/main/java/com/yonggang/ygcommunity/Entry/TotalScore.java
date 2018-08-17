package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/7/25.
 */

public class TotalScore {

    /**
     * score : 21
     * url : http://zhyl.yong-gang.com/zhyl/index.php/Home/Index/score_reg_page
     */

    private int score;
    private String url;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
