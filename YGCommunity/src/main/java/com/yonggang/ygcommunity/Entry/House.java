package com.yonggang.ygcommunity.Entry;

public class House {

    /**
     * fh : 201
     * fwbm_pk : 57ebaab9-fd4c-486c-acfa-c4b770ae
     */

    private String fh;
    private String fwbm_pk;

    public String getFh() {
        return fh;
    }

    public void setFh(String fh) {
        this.fh = fh;
    }

    public String getFwbm_pk() {
        return fwbm_pk;
    }

    public void setFwbm_pk(String fwbm_pk) {
        this.fwbm_pk = fwbm_pk;
    }

    @Override
    public String toString() {
        return this.fh;
    }
}
