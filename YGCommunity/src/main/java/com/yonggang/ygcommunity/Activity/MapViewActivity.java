package com.yonggang.ygcommunity.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapViewActivity extends BaseActivity implements LocationSource, AMapLocationListener, AMap.OnPOIClickListener,
        AMap.OnMarkerClickListener {

    @BindView(R.id.map)
    MapView mMapView;

    AMap aMap;
    OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;

    private LatLng location;//定位成功后的坐标

    AMapNavi mAMapNavi;

    private boolean is_locate;

    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.activity_map_view);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setOnPOIClickListener(this);
        aMap.setOnMarkerClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setOnceLocationLatest(true);
            // 设置只定位一次
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

            mlocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (mListener != null && aMapLocation != null) {
                if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                    mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                    location = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    if (!is_locate) {
                        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                    }
                    is_locate = true;

                } else {
                    String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                    Log.e("AmapErr", errText);
                }
            }
        }
    }

    //底图poi点击回调
    @Override
    public void onPOIClick(Poi poi) {
        aMap.clear();
        Log.i("MY", poi.getPoiId() + poi.getName());
        MarkerOptions markOptiopns = new MarkerOptions();
        markOptiopns.position(poi.getCoordinate());
        TextView textView = new TextView(getApplicationContext());
        textView.setText("到" + poi.getName() + "去");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundResource(R.mipmap.custom_info_bubble);
        markOptiopns.icon(BitmapDescriptorFactory.fromView(textView));
        aMap.addMarker(markOptiopns);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        NaviLatLng start = new NaviLatLng(location.latitude, location.longitude);
        NaviLatLng end = new NaviLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        Bundle bundle = new Bundle();
        bundle.putParcelable("start", start);
        bundle.putParcelable("end", end);
        stepActivity(bundle, NaviActivity.class);
        aMap.clear();
        return false;
    }
}
