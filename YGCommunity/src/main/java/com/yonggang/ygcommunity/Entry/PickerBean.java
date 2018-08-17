package com.yonggang.ygcommunity.Entry;

/**
 * Created by liyangyang on 2017/6/1.
 */

public class PickerBean {
    private boolean is_add;
    private String uri;

    public PickerBean(boolean is_add, String uri) {
        this.is_add = is_add;
        this.uri = uri;
    }

    public boolean is_add() {
        return is_add;
    }

    public void setIs_add(boolean is_add) {
        this.is_add = is_add;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
