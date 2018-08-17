package com.yonggang.ygcommunity.Entry;

import com.alibaba.fastjson.JSON;

/**
 * Created by liyangyang on 2017/8/17.
 */

public class Alipay {

    /**
     * response : alipay_sdk=alipay-sdk-php-20161101&app_id=2017072807929448&biz_content=%7B%22body%22%3A%22%5Cu5546%5Cu54c1%5Cu4fe1%5Cu606f%22%2C%22subject%22%3A%22%5Cu6c38%5Cu8054%5Cu4e00%5Cu70b9%5Cu901a%5Cu6c34%5Cu8d39%5Cu7f34%5Cu7eb3--APP%5Cu652f%5Cu4ed8%22%2C%22out_trade_no%22%3A%22ZHYLALIPAY1708172941502931913%22%2C%22total_amount%22%3A%220.02%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22timeout_express%22%3A%22120m%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fzhyl.yong-gang.com%2Fzhyl%2Findex.php%2FHome%2FIndex%2Failipay_callback&sign_type=RSA2&timestamp=2017-08-17+09%3A05%3A13&version=1.0&sign=LebNfNcJOuHDseI%2BbIJjtoVFR7KgKgtc%2FPDKhIVHDCCfn1Rfv0Gas3BCNcbavY%2BbXXcrVOF1Qt6hu6zdoCsfi5xHUQxX8NfeVKfuIM7dgE28WOU812oKyd5Wk0LIkQ8%2BoLzWdGoYLsP%2FcycCuOPlxtMU06f2n7qgUKSH0uBhd2PQaM4gyr3kEVju4N7ADGmFDKcNeUqsJk%2Buu7tHuN7GlVt2CeJayJdiAXM2HJB%2BDXZlV90JPnQprm2n1EKE7kjFTWuNGJVg1CujweLlwNu1eo5sclDfxDRIO131ySK3g3vdr0eGnvim058mNUw44itEUd0z5v%2FSGw6Ax3buwC%2Btew%3D%3D
     * score : 2
     */

    private String response;
    private String score;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
