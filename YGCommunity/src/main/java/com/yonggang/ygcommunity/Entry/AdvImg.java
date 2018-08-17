package com.yonggang.ygcommunity.Entry;

import java.util.List;

/**
 * Created by liyangyang on 2017/9/27.
 */

public class AdvImg {

    /**
     * g_url : https://mp.weixin.qq.com/s?__biz=MzI4NTU2NjI5Nw==&mid=2247484425&idx=1&sn=56aff418a5a38853d8b9e5eed62dfe79&chksm=ebeb703fdc9cf929f7821c8b3cb596fdb6dc069105f8d7d697c12fbb8b0d1718e7a48c933089#rd
     * img_url : ["http://zhyl.yong-gang.com/firstimg/be08dfe997fad9cd5924e7c3a2542147.jpg"]
     */

    private String g_url;
    private List<String> img_url;

    public String getG_url() {
        return g_url;
    }

    public void setG_url(String g_url) {
        this.g_url = g_url;
    }

    public List<String> getImg_url() {
        return img_url;
    }

    public void setImg_url(List<String> img_url) {
        this.img_url = img_url;
    }
}
