package com.yonggang.ygcommunity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.FirstImg;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.bgabanner.BGABanner;
import rx.Subscriber;

public class GuideActivity extends BaseActivity {
    @BindView(R.id.banner_guide_foreground)
    BGABanner bannerGuideForeground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        getImg();
    }

    private void setListener() {
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        bannerGuideForeground.setEnterSkipViewIdAndDelegate(R.id.btn_guide_enter, R.id.tv_guide_skip, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void processLogic(List<String> imgs) {
        List<View> views = new ArrayList<>();
        for (String url : imgs) {
            ImageView imageView = new ImageView(this);
            imageView.setClickable(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(this)
                    .load(url)
                    .into(imageView);
            views.add(imageView);
        }
        bannerGuideForeground.setData(views);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 如果开发者的引导页主题是透明的，需要在界面可见时给背景 Banner 设置一个白色背景，避免滑动过程中两个 Banner 都设置透明度后能看到 Launcher
        bannerGuideForeground.setBackgroundResource(android.R.color.white);
    }

    private void getImg() {
        Subscriber subscriber = new Subscriber<FirstImg>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                startActivity(new Intent(GuideActivity.this, SplashActivity.class));
                finish();
            }

            @Override
            public void onNext(FirstImg data) {
                Log.i("getImg", data.toString());
                if (SpUtil.getFirst(GuideActivity.this)) {
                    setListener();
                    processLogic(data.getImg_url());
                } else {
                    startActivity(new Intent(GuideActivity.this, SplashActivity.class));
                    finish();
                }
            }
        };
        HttpUtil.getInstance().getFirstImg(subscriber, 1);
    }
}
