package com.yonggang.ygcommunity.Entry;

public class Assignor {

    /**
     * id : 1
     * name : 张琰
     */

    private String id;
    private String name;
    private boolean select;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
