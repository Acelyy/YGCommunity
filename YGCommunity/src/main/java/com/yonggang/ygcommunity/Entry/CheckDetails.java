package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class CheckDetails {

    /**
     * girdimgs : ["http://zhyl.yong-gang.com/gird_img/20180905/imgs201809051625950246.jpg","http://zhyl.yong-gang.com/gird_img/20180905/imgs201809051576886779.jpg","http://zhyl.yong-gang.com/gird_img/20180905/imgs20180905231861133.jpg"]
     * sbsj : 2018-09-05
     * sjbt : test1
     * sjdw : sdfsdf
     * sjms : 是方式发送方
     * yzcd : 轻微
     */

    private String sbsj;
    private String sjbt;
    private String sjdw;
    private String sjms;
    private String yzcd;
    private List<String> girdimgs;

    public String getSbsj() {
        return sbsj;
    }

    public void setSbsj(String sbsj) {
        this.sbsj = sbsj;
    }

    public String getSjbt() {
        return sjbt;
    }

    public void setSjbt(String sjbt) {
        this.sjbt = sjbt;
    }

    public String getSjdw() {
        return sjdw;
    }

    public void setSjdw(String sjdw) {
        this.sjdw = sjdw;
    }

    public String getSjms() {
        return sjms;
    }

    public void setSjms(String sjms) {
        this.sjms = sjms;
    }

    public String getYzcd() {
        return yzcd;
    }

    public void setYzcd(String yzcd) {
        this.yzcd = yzcd;
    }

    public List<String> getGirdimgs() {
        return girdimgs;
    }

    public void setGirdimgs(List<String> girdimgs) {
        this.girdimgs = girdimgs;
    }
}
