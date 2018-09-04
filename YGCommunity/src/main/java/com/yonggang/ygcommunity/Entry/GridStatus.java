package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class GridStatus {

    private List<Bean> czfa;
    private List<Bean> czlx;
    private List<Bean> sjfl;
    private List<Bean> xzqh;
    private List<Bean> yzcd;

    public List<Bean> getCzfa() {
        return czfa;
    }

    public void setCzfa(List<Bean> czfa) {
        this.czfa = czfa;
    }

    public List<Bean> getCzlx() {
        return czlx;
    }

    public void setCzlx(List<Bean> czlx) {
        this.czlx = czlx;
    }

    public List<Bean> getSjfl() {
        return sjfl;
    }

    public void setSjfl(List<Bean> sjfl) {
        this.sjfl = sjfl;
    }

    public List<Bean> getXzqh() {
        return xzqh;
    }

    public void setXzqh(List<Bean> xzqh) {
        this.xzqh = xzqh;
    }

    public List<Bean> getYzcd() {
        return yzcd;
    }

    public void setYzcd(List<Bean> yzcd) {
        this.yzcd = yzcd;
    }

    public static class Bean {
        /**
         * id : 1
         * name : 现场
         */

        private String id;
        private String name;
        private Boolean selection;

        public Boolean getSelection() {
            return selection;
        }

        public void setSelection(Boolean selection) {
            this.selection = selection;
        }

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

        @Override
        public String toString() {
            return this.name;
        }

    }


}
