package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by liyangyang on 2017/7/25.
 */

public class Score {

    /**
     * score : [{"source":"评论成功积分+1","time":"2017-07-24 08:48:08"},{"source":"评论成功积分+1","time":"2017-07-24 10:03:44"},{"source":"评论成功积分+1","time":"2017-07-24 10:04:22"}]
     * total : 3
     */

    private int total;
    private List<ScoreBean> score;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ScoreBean> getScore() {
        return score;
    }

    public void setScore(List<ScoreBean> score) {
        this.score = score;
    }

    public static class ScoreBean {
        /**
         * source : 评论成功积分+1
         * time : 2017-07-24 08:48:08
         */

        private String source;
        private String time;
        private String points;

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
