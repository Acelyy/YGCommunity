package com.yonggang.liyangyang.iyonggang.HttpUtils;


import com.yonggang.liyangyang.iyonggang.Place;
import com.yonggang.liyangyang.iyonggang.Version;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by liyangyang on 17/3/14.
 */
public interface HttpService {
    @GET("iygs/getVersion")
    Observable<Version> getVersion();

    @GET("Sales/getPlace")
    Observable<Place> getPlace();
}
