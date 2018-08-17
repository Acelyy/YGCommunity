package com.yonggang.ygcommunity.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.HttpResult;
import com.yonggang.ygcommunity.PhotoPicker.PhotoAdapter;
import com.yonggang.ygcommunity.PhotoPicker.RecyclerItemClickListener;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.ImageUtils;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import rx.Subscriber;

public class AddBbsActivity extends BaseActivity implements AMapLocationListener {
    @BindView(R.id.list_pic)
    RecyclerView listPic;
    @BindView(R.id.edit_title)
    EditText editTitle;
    @BindView(R.id.edit_cotent)
    EditText editCotent;

    @BindView(R.id.text_location)
    TextView text_location;

    private PhotoAdapter adapter;

    private ArrayList<String> photoPaths = new ArrayList<String>();

    private YGApplication app;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private List<String> images;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgressDialog dialog = new ProgressDialog(AddBbsActivity.this);
            dialog.setMessage("压缩中");
            switch (msg.what) {
                case 0:
                    dialog.show();
                    break;
                case 1:
                    dialog.dismiss();
                    Subscriber subscriber = new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("error", e.toString());
                        }

                        @Override
                        public void onNext(String data) {
                            Log.i("file", data);
                            HttpResult<String> result = JSON.parseObject(data, HttpResult.class);
                            if (result.getFlag() == 0)
                                Toast.makeText(AddBbsActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    };
                    HttpUtil.getInstance().setBbs(subscriber, editTitle.getText().toString().trim(), editCotent.getText().toString().trim(), app.getUser().getUser_id(), text_location.getText().toString().trim(), JSON.toJSONString(images));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bbs);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        adapter = new PhotoAdapter(this, photoPaths);
        listPic.setLayoutManager(new StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL));
        listPic.setAdapter(adapter);
        listPic.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (adapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(photoPaths)
                                    .start(AddBbsActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(photoPaths)
                                    .setCurrentItem(position)
                                    .start(AddBbsActivity.this);
                        }
                    }
                }));

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption.setOnceLocationLatest(true);
        //启动定位
        mLocationClient.startLocation();
    }

    @OnClick({R.id.img_finish, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_commit:
                setBbs(editTitle.getText().toString().trim(), editCotent.getText().toString().trim());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            List<String> photos = null;
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
            }
            photoPaths.clear();
            if (photos != null) {
                photoPaths.addAll(photos);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 发布Bbs新闻
     *
     * @param bbs_title
     * @param bbs_content
     */
    private void setBbs(String bbs_title, String bbs_content) {
        if (app.getUser() == null) {
            Toast.makeText(app, "请先登录再发布", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(bbs_title)) {
            Toast.makeText(app, "标题不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(bbs_content)) {
            Toast.makeText(app, "内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (photoPaths.size() == 0) {
            Toast.makeText(app, "请至少选择一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        new WorkThread().start();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                String address = aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                String country = aMapLocation.getCountry();//国家信息
                String province = aMapLocation.getProvince();//省信息
                String city = aMapLocation.getCity();//城市信息
                String district = aMapLocation.getDistrict();//城区信息
                String street = aMapLocation.getStreet();//街道信息
                String streetNum = aMapLocation.getStreetNum();//街道门牌号信息
                String cityCode = aMapLocation.getCityCode();//城市编码
                String adCode = aMapLocation.getAdCode();//地区编码

                Log.i("AmapLocation", "<<address:" + address +
                        "<<country:" + country +
                        "<<province:" + province +
                        "<<city:" + city +
                        "<<district:" + district +
                        "<<street:" + street +
                        "<<streetNum:" + streetNum +
                        "<<cityCode:" + cityCode +
                        "<<adCode:" + adCode);
                text_location.setText(address);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                text_location.setText("未知");
            }
        }
    }

    /**
     * 用于执行BitMap转Base64操作
     */
    class WorkThread extends Thread {
        @Override
        public void run() {
            handler.sendEmptyMessage(0);
            super.run();
            images = new ArrayList<>();
            for (int i = 0; i < photoPaths.size(); i++) {
                try {
                    images.add(ImageUtils.bitmapToString(photoPaths.get(i)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.i("base64", JSON.toJSONString(images));
            handler.sendEmptyMessage(1);
        }
    }
}
