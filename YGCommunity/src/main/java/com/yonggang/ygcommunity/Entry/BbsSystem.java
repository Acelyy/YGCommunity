package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/6/7.
 */

public class BbsSystem {

    /**
     * answers_author : admin
     * answers_time : 2017-06-07
     * sys_answers : 系统回复123
     */

    private String answers_author;
    private String answers_time;
    private String sys_answers;

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

    public String getSys_answers() {
        return sys_answers;
    }

    public void setSys_answers(String sys_answers) {
        this.sys_answers = sys_answers;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
