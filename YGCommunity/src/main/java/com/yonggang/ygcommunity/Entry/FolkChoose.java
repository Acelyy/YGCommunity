package com.yonggang.ygcommunity.Entry;

import java.util.List;

public class FolkChoose {

    /**
     * data : {"dzb":[{"id":"1","name":"永联村党总支"},{"id":"2","name":"永钢集团党总支"},{"id":"3","name":"行政支部"},{"id":"4","name":"轧钢支部"},{"id":"5","name":"特钢支部"},{"id":"6","name":"物流支部"},{"id":"7","name":"建设支部"},{"id":"8","name":"生产运营支部"},{"id":"9","name":"天天鲜支部"},{"id":"10","name":"联峰安装支部"},{"id":"11","name":"精筑集团支部"},{"id":"12","name":"恒创软件支部"},{"id":"13","name":"旅游餐饮支部"},{"id":"14","name":"监督审计支部"},{"id":"15","name":"产业发展支部"},{"id":"16","name":"技术支部"},{"id":"17","name":"供销支部"},{"id":"18","name":"产品研发中心支部"},{"id":"19","name":"能源事业支部"},{"id":"20","name":"炼铁支部"},{"id":"21","name":"炼钢支部"},{"id":"22","name":"永合社区党总支"},{"id":"23","name":"永合社区第一支部"},{"id":"24","name":"永合社区第二支部"},{"id":"25","name":"永合社区第三支部"},{"id":"26","name":"永合社区第四支部"},{"id":"27","name":"永合社区第五支部"},{"id":"28","name":"第一党小组"},{"id":"29","name":"第二党小组"},{"id":"30","name":"第三党小组"},{"id":"31","name":"第一党小组"},{"id":"32","name":"第二党小组"},{"id":"33","name":"第一党小组"},{"id":"34","name":"第二党小组"},{"id":"35","name":"第一党小组"},{"id":"36","name":"第二党小组"},{"id":"37","name":"第一党小组"},{"id":"38","name":"第二党小组"},{"id":"39","name":"永联村经济合作社党总支"},{"id":"40","name":"惠民服务中心支部"},{"id":"41","name":"联峰物业支部"},{"id":"42","name":"非公联合支部"},{"id":"43","name":"经济合作社一支部"},{"id":"44","name":"经济合作社二支部"},{"id":"45","name":"惠邻社工支部"},{"id":"46","name":"永联景区管理党支部"},{"id":"47","name":"第三党小组"},{"id":"48","name":"第三党小组"},{"id":"49","name":"第三党小组"},{"id":"50","name":"第三党小组"}],"mqxz":[{"id":"1","name":"民情建议"},{"id":"2","name":"民事调解"},{"id":"3","name":"民困帮扶"},{"id":"4","name":"民生致富"},{"id":"5","name":"中心工作"},{"id":"6","name":"其他工作"}]}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<DzbBean> dzb;
        private List<MqxzBean> mqxz;

        public List<DzbBean> getDzb() {
            return dzb;
        }

        public void setDzb(List<DzbBean> dzb) {
            this.dzb = dzb;
        }

        public List<MqxzBean> getMqxz() {
            return mqxz;
        }


        public void setMqxz(List<MqxzBean> mqxz) {
            this.mqxz = mqxz;
        }

        public static class DzbBean {
            /**
             * id : 1
             * name : 永联村党总支
             */

            private String id;
            private String name;

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

        public static class MqxzBean {
            /**
             * id : 1
             * name : 民情建议
             */

            private String id;
            private String name;

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
}
