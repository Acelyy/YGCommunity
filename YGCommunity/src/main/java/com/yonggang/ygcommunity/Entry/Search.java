package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class Search {

    private List<Dep> dep;
    private List<Source> source;

    public List<Dep> getDep() {
        return dep;
    }

    public void setDep(List<Dep> dep) {
        this.dep = dep;
    }

    public List<Source> getSource() {
        return source;
    }

    public void setSource(List<Source> source) {
        this.source = source;
    }

    public static class Dep {



        /**
         * dep : 惠邻服务中心
         * id : 1
         */

        public Dep() {
        }

        public Dep(String dep, String id) {
            this.dep = dep;
            this.id = id;
        }

        private String dep;
        private String id;

        public String getDep() {
            return dep;
        }

        public void setDep(String dep) {
            this.dep = dep;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return this.dep;
        }
    }

    public static class Source {
        /**
         * id : 1
         * source : 网格化
         */

        private int id;
        private String source;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
