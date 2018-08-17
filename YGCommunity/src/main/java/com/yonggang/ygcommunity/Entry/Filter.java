package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/6/13.
 */

public class Filter {

    private List<AreaBean> area;
    private List<StatusBean> status;
    private List<TypesBean> types;

    public List<AreaBean> getArea() {
        return area;
    }

    public void setArea(List<AreaBean> area) {
        this.area = area;
    }

    public List<StatusBean> getStatus() {
        return status;
    }

    public void setStatus(List<StatusBean> status) {
        this.status = status;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public static class AreaBean {
        /**
         * area : 永联
         * status : 1
         */

        private String area;
        private int status;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class StatusBean {
        /**
         * name : 进行中
         * status : 1
         */

        private String name;
        private int status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class TypesBean {
        /**
         * name : 会议123
         * status : 1
         */

        private String name;
        private int status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
