package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/6/19.
 */

public class ActDetail {

    /**
     * address : 1
     * content : <p>【活动目的】
     * 为了陶冶村民情操，永联村鲜花基地讲举办鲜花观赏活动。
     * 永联鲜花基地特从云南引进新鲜花卉，有近百个品种的鲜花。家长朋友们可以携子一起参加，有助于帮助小朋友们学习自然科学知识，丰富课外生活。
     * 【活动内容】
     * 2月21日至28日，报名参加的村民可以前往永联鲜花基地参加活动，我们现场有鲜花向导给各位讲解鲜花的知识，大家自行携带相机拍摄。
     * 免费提供午饭、各种冷餐水果。
     * 【活动时间】
     * 2017年2月18日 8:00-2017年2月28日 16:00
     * 【活动地点】
     * 地址：永联村鲜花基地
     * 【报名方式】
     * 点击页面下方“立即报名”，填写个人信息即可报名。将链接分享给其他人后邀请他人报名。
     * 【报名时间】
     * 2017年2月1日 0:00-2017年2月20日 0:00</p>
     * date_end : 2017-06-30 05:00
     * date_start : 2017-06-15 08:30
     * images : http://10.10.3.213
     * join_no : 0
     * max_join : 111
     * title : 内容要长
     * type_id : 3
     */

    private String address;
    private String content;
    private String date_end;
    private String date_start;
    private String images;
    private int join_no;
    private int max_join;
    private String title;
    private String type_id;
    private long remaining;
    private int account_sign;
    private String connect_name;
    private String connect_fs;
    private int checked;
    private String hdfx;
    private List<User> userinfo;

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public List<User> getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(List<User> userinfo) {
        this.userinfo = userinfo;
    }

    public int getAccount_sign() {
        return account_sign;
    }

    public void setAccount_sign(int account_sign) {
        this.account_sign = account_sign;
    }

    public long getRemaining() {
        return remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getJoin_no() {
        return join_no;
    }

    public void setJoin_no(int join_no) {
        this.join_no = join_no;
    }

    public int getMax_join() {
        return max_join;
    }

    public void setMax_join(int max_join) {
        this.max_join = max_join;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getConnect_name() {
        return connect_name;
    }

    public void setConnect_name(String connect_name) {
        this.connect_name = connect_name;
    }

    public String getConnect_fs() {
        return connect_fs;
    }

    public void setConnect_fs(String connect_fs) {
        this.connect_fs = connect_fs;
    }

    public String getHdfx() {
        return hdfx;
    }

    public void setHdfx(String hdfx) {
        this.hdfx = hdfx;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
