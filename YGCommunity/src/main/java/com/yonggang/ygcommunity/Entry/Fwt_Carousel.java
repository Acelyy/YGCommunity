package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/6/13.
 */

public class Fwt_Carousel {

    /**
     * img_url : http://10.10.3.213/fwt_carousel/20170613/defautl.jpg
     * title : 1313131
     */

    private String img_url;
    private String title;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
