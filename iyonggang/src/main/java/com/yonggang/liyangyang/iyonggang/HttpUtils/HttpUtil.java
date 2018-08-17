package com.yonggang.liyangyang.iyonggang.HttpUtils;

import com.yonggang.liyangyang.iyonggang.Place;
import com.yonggang.liyangyang.iyonggang.Version;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liyangyang on 2017/3/22.
 */

public class HttpUtil {

    public static String BASE_URL = "http://192.168.0.224/";

    public static int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private HttpService httpService;

    private static HttpUtil INSTANCE = new HttpUtil();

    private HttpUtil() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    public static HttpUtil getInstance() {
        return INSTANCE;
    }

    /**
     * 用来统一处理Http的flag,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
//    public static class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {
//
//        @Override
//        public T call(HttpResult<T> httpResult) {
//            Log.i("result", httpResult.toString());
//            if (httpResult.getFlag() == 0) {
//                String msg = httpResult.getMsg();
//                if (msg == null) {
//                    msg = "msg为空";
//                }
//                throw new RuntimeException(msg);
//            }
//            return httpResult.getData();
//        }
//    }

    /**
     * 统一配置观察者
     *
     * @param o
     * @param <T>
     */
    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 获取版本信息
     *
     * @param subscriber
     */
    public void getVersion(Subscriber<Version> subscriber) {
        Observable observable = httpService.getVersion();
        toSubscribe(observable, subscriber);
    }

    /**
     * 获取地点信息
     *
     * @param subscriber
     */
    public void getPlace(Subscriber<Place> subscriber) {
        Observable observable = httpService.getPlace();
        toSubscribe(observable, subscriber);
    }

}
