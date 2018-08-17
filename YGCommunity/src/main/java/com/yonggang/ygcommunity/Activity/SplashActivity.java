package com.yonggang.ygcommunity.Activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.DataBase.DBDao;
import com.yonggang.ygcommunity.Entry.AdvImg;
import com.yonggang.ygcommunity.Entry.FirstImg;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.DownLoadImageService;
import com.yonggang.ygcommunity.Util.ImageDownLoadCallBack;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class SplashActivity extends BaseActivity {
    private static final int SHOW_PIC = 0;
    private static final int SHOW_OVER = 1;

    private static final long DELAY_TIME = 5000;

    @BindView(R.id.banner)
    ImageView banner;

    private DBDao dao;

    private boolean is_finish;

    private List<Bitmap> list_pic = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOW_PIC:
//                    int index = (int) (Math.random() * list_pic.size());
//                    banner.setImageBitmap(list_pic.get(index));
                    if (img != null) {
                        Glide.with(SplashActivity.this)
                                .load(img.getImg_url().get(0))
                                .into(banner);
                        if (img.getG_url() != null && !"".equals(img.getG_url())) {
                            banner.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle bundle = new Bundle();
                                    bundle.putBoolean("adv", true);
                                    bundle.putString("url", img.getG_url());
                                    goActivity(bundle, MainActivity.class);
                                }
                            });
                        }
                        handler.sendEmptyMessageDelayed(SHOW_OVER, DELAY_TIME);
                    } else {
                        handler.sendEmptyMessage(SHOW_OVER);
                    }
                    break;
                case SHOW_OVER:
                    if (!is_finish) {
                        goActivity(MainActivity.class);
                    }
                    break;
            }
        }
    };

    private AdvImg img;

//    private Handler handler_local = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case SHOW_PIC:
//                    double random = Math.random();
//                    Log.i("random", random + "");
//                    if (random < 0.33333) {
//                        banner.setImageResource(R.mipmap.pic_main_logo);
//                    } else if (random > 0.66666) {
//                        banner.setImageResource(R.mipmap.pic_main_logo2);
//                    } else {
//                        banner.setImageResource(R.mipmap.pic_main_logo3);
//                    }
//
//                    handler_local.sendEmptyMessageDelayed(SHOW_OVER, DELAY_TIME);
//                    break;
//                case SHOW_OVER:
//                    goActivity(MainActivity.class);
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        dao = new DBDao(this);
        //getPic(1080, 1920);
        getPic();
        //load_localPic();
    }

//    private void load_localPic() {
//        handler_local.sendEmptyMessage(SHOW_PIC);
//    }

    /**
     * 获取首页广告图
     *
     * @param w
     * @param h
     */
    private void getPic(int w, int h) {
        Subscriber subscriber = new Subscriber<FirstImg>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getLocationPic(new ArrayList<String>());
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(FirstImg data) {
                Log.i("getPic", data.toString());
                checkUpdate(data.getImg_url());
            }
        };
        //HttpUtil.getInstance().getFirstImg(subscriber, w, h);
    }

    private void getPic() {
        Subscriber subscriber = new Subscriber<AdvImg>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                //getLocationPic(new ArrayList<String>());
                Log.i("error", e.toString());
                handler.sendEmptyMessage(SHOW_PIC);
            }

            @Override
            public void onNext(AdvImg data) {
                Log.i("getPic", data.toString());
                img = data;
                handler.sendEmptyMessage(SHOW_OVER);
            }
        };
        HttpUtil.getInstance().getAdvImg(subscriber, 2);
    }

    /**
     * 启动图片下载线程
     */
    private void onDownLoad(final String url) {
        Log.i("file_download", url);
        DownLoadImageService service = new DownLoadImageService(getApplicationContext(),
                url,
                new ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(File file) {
                        Log.i("file_download", file.toString());
                        dao.savePic(url, file.getPath());
                    }

                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                        // 在这里执行图片保存方法
                    }

                    @Override
                    public void onDownLoadFailed() {
                        // 图片保存失败
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
    }

    /**
     * 获取本地图片
     */

    private void getLocationPic(List<String> url) {
        List<String> list_file = dao.getPic();
        try {
            for (String path : list_file) {
                File file = new File(path);
                list_pic.add(BitmapFactory.decodeStream(new FileInputStream(file)));
            }
        } catch (Exception e) {
            dao.clearData();
            for (String bean : url) {
                onDownLoad(bean);
            }
        }
        if (list_pic != null && !list_pic.isEmpty()) {
            handler.sendEmptyMessage(SHOW_PIC);
        } else {
            handler.sendEmptyMessageDelayed(SHOW_OVER, DELAY_TIME);
        }
    }

    /**
     * 监测url在本地是否存在，存在就不需要更新，不存在就删除本地数据重新下载
     *
     * @param url
     */
    private void checkUpdate(List<String> url) {
        getLocationPic(url);
        Log.i("getPic", url.toString());
        if (url.isEmpty()) {
            return;
        }
        String path = dao.getPicByUrl(url.get(0));
        Log.i("path", path == null ? "null" : path);
        if ("".equals(path)) {
            //不存在的，删除本地
            dao.clearData();
            for (String bean : url) {
                onDownLoad(bean);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        is_finish = true;
    }
}
