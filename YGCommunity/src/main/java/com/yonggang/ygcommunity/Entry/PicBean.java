package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/5/10.
 */

public class PicBean {

    /**
     * newspic_content : 森林
     * newspic_url : /uploads_pics/20170509/720170509.jpg
     */

    private String newspic_content;
    private String newspic_url;

    public String getNewspic_content() {
        return newspic_content;
    }

    public void setNewspic_content(String newspic_content) {
        this.newspic_content = newspic_content;
    }

    public String getNewspic_url() {
        return newspic_url;
    }

    public void setNewspic_url(String newspic_url) {
        this.newspic_url = newspic_url;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
