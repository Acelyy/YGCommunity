package com.yonggang.ygcommunity.Entry;

/**
 * Created by liyangyang on 2017/4/10.
 */

public class Title {
    private String category_id;
    private String category_name;
    private int category_type;

    public Title(String category_id, String category_name, int category_type) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_type = category_type;
    }

    public Title() {

    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public int getCategory_type() {
        return category_type;
    }

    public void setCategory_type(int category_type) {
        this.category_type = category_type;
    }
}
