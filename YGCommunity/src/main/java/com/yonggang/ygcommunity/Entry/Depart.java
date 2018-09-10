package com.yonggang.ygcommunity.Entry;

public class Depart {

    /**
     * bname : 经济合作社
     * rid : 1
     */

    private String bname;
    private String rid;

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    @Override
    public String toString() {
        return this.bname;
    }
}
