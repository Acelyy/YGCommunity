package com.yonggang.ygcommunity.Entry;

public class GridUser {

    /**
     * id : 1
     * sswg : 1
     * username : wgy1
     */

    private String id;
    private String sswg;
    private String username;
    private int appauth;

    public int getAppauth() {
        return appauth;
    }

    public void setAppauth(int appauth) {
        this.appauth = appauth;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSswg() {
        return sswg;
    }

    public void setSswg(String sswg) {
        this.sswg = sswg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
