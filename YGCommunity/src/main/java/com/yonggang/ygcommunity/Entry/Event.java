package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class Event {


    /**
     * data : {"count":{"bjl":"1","blz":"0","sbm":"122","wbj":"121","ysl":"0"},"list":[{"id":"1102","time":"2018-06-18 17:57","title":"金手指广场边上护栏","type":1},{"id":"1101","time":"2018-06-16 20:53","title":"报修","type":1},{"id":"1100","time":"2018-06-14 21:06","title":"报子","type":1},{"id":"1099","time":"2018-06-11 20:57","title":"永琪园六栋","type":1},{"id":"1098","time":"2018-06-09 18:57","title":"井盖破损该修一下了","type":1},{"id":"1097","time":"2018-06-08 12:02","title":"自来水问题","type":1},{"id":"1096","time":"2018-06-05 10:30","title":"翻越栏杆","type":1},{"id":"1095","time":"2018-06-03 06:39","title":"租房","type":1},{"id":"1094","time":"2018-06-02 17:33","title":"永瑞园18栋33号停车位","type":1},{"id":"1093","time":"2018-06-01 18:15","title":"乱停车，太烦人","type":1}]}
     * flag : 1
     * msg : success
     * total : 122
     */

    private Data data;
    private int flag;
    private String msg;
    private int total;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class Data {
        /**
         * count : {"bjl":"1","blz":"0","sbm":"122","wbj":"121","ysl":"0"}
         * list : [{"id":"1102","time":"2018-06-18 17:57","title":"金手指广场边上护栏","type":1},{"id":"1101","time":"2018-06-16 20:53","title":"报修","type":1},{"id":"1100","time":"2018-06-14 21:06","title":"报子","type":1},{"id":"1099","time":"2018-06-11 20:57","title":"永琪园六栋","type":1},{"id":"1098","time":"2018-06-09 18:57","title":"井盖破损该修一下了","type":1},{"id":"1097","time":"2018-06-08 12:02","title":"自来水问题","type":1},{"id":"1096","time":"2018-06-05 10:30","title":"翻越栏杆","type":1},{"id":"1095","time":"2018-06-03 06:39","title":"租房","type":1},{"id":"1094","time":"2018-06-02 17:33","title":"永瑞园18栋33号停车位","type":1},{"id":"1093","time":"2018-06-01 18:15","title":"乱停车，太烦人","type":1}]
         */

        private Count count;
        private List<Bean> list;

        public Count getCount() {
            return count;
        }

        public void setCount(Count count) {
            this.count = count;
        }

        public List<Bean> getList() {
            return list;
        }

        public void setList(List<Bean> list) {
            this.list = list;
        }

        public static class Count {
            /**
             * bjl : 1
             * blz : 0
             * sbm : 122
             * wbj : 121
             * ysl : 0
             */

            private int bjl;
            private int blz;
            private int sbm;
            private int wbj;
            private int ysl;

            public int getBjl() {
                return bjl;
            }

            public void setBjl(int bjl) {
                this.bjl = bjl;
            }

            public int getBlz() {
                return blz;
            }

            public void setBlz(int blz) {
                this.blz = blz;
            }

            public int getSbm() {
                return sbm;
            }

            public void setSbm(int sbm) {
                this.sbm = sbm;
            }

            public int getWbj() {
                return wbj;
            }

            public void setWbj(int wbj) {
                this.wbj = wbj;
            }

            public int getYsl() {
                return ysl;
            }

            public void setYsl(int ysl) {
                this.ysl = ysl;
            }
        }

        public static class Bean {
            /**
             * id : 1102
             * time : 2018-06-18 17:57
             * title : 金手指广场边上护栏
             * type : 1
             */

            private String id;
            private String time;
            private String title;
            private int type;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
