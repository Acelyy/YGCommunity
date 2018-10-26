package com.yonggang.ygcommunity.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yonggang.ygcommunity.Activity.BikeActivity;
import com.yonggang.ygcommunity.Activity.MapViewActivity;
import com.yonggang.ygcommunity.Activity.Server.ComActActivity;
import com.yonggang.ygcommunity.Activity.Server.ExpensesActivity;
import com.yonggang.ygcommunity.Activity.Server.HotLineActivity;
import com.yonggang.ygcommunity.Activity.Server.NoticeActivity;
import com.yonggang.ygcommunity.Activity.WebViewActivity;
import com.yonggang.ygcommunity.Entry.Fwt_Carousel;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.grid.GridLoginActivity;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.monitor.MonitorActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by liyangyang on 2017/2/25.
 */

public class ServerFragment extends Fragment {

    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.server_content)
    PullToRefreshScrollView server_content;
    @BindView(R.id.layout_work)
    LinearLayout layout_work;

    private YGApplication app;

    @OnClick({R.id.tab_search, R.id.tab_tel, R.id.tab_activity, R.id.tab_water, R.id.tab_notice, R.id.tab_order, R.id.tab_rechage, R.id.tab_grid, R.id.tab_red, R.id.tab_work, R.id.tab_bike})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tab_search:
                intent = new Intent(getActivity(), MapViewActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_tel:
                intent = new Intent(getActivity(), HotLineActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_activity:
                intent = new Intent(getActivity(), ComActActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_water:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先登录，再进行操作", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(getActivity(), ExpensesActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tab_notice:
                intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_order:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "在线点餐");
                intent.putExtra("url", "http://dc.yong-gang.com");
                startActivity(intent);
                break;
            case R.id.tab_rechage:
                intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://dc.yong-gang.com/members/login?url=/payments/recharge");
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.tab_grid:
                intent = new Intent(getActivity(), GridLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tab_red:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("name", "红星闪耀");
                intent.putExtra("url", "http://hxsy.yong-gang.cn:9002/");
                startActivity(intent);
                break;
            case R.id.tab_work:
                if (app.getUser() == null) {
                    Toast.makeText(app, "请先登录，再进行操作", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(getActivity(), MonitorActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.tab_bike:
                intent = new Intent(getActivity(), BikeActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_server, container, false);
        ButterKnife.bind(this, view);
        app = (YGApplication) getActivity().getApplication();
        checkAuth();
        getCarousel();
        server_content.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getCarousel();
            }
        });
        return view;
    }

    /**
     * 监测权限
     */
    public void checkAuth() {
        if (app.getUser() != null && app.getUser().getAuth() == 1) {
            layout_work.setVisibility(View.VISIBLE);
        } else {
            layout_work.setVisibility(View.GONE);
        }
    }

    /**
     * 获取轮播图
     */
    private void getCarousel() {
        Subscriber subscriber = new Subscriber<List<Fwt_Carousel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                banner.setVisibility(View.GONE);
                Log.i("error", e.toString());
                server_content.onRefreshComplete();
            }

            @Override
            public void onNext(List<Fwt_Carousel> list) {
                Log.i("fwt", list.toString());
                if (list.size() > 0) {
                    List<String> urlList = new ArrayList<>();
                    List<String> titleList = new ArrayList<>();
                    for (Fwt_Carousel bean : list) {
                        urlList.add(bean.getImg_url());
                        titleList.add(bean.getTitle());
                    }
                    banner.setImages(urlList)
                            .setBannerTitles(titleList)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                            .setImageLoader(new ImageLoader() {
                                @Override
                                public void displayImage(Context context, Object path, ImageView imageView) {
                                    Glide.with(ServerFragment.this)
                                            .load(path.toString())
                                            .error(R.mipmap.pic_loading_error)
                                            .centerCrop()
                                            .into(imageView);

                                }
                            })
                            .start();
                    banner.setVisibility(View.VISIBLE);
                }
                server_content.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().fwt_carousel(subscriber);
    }

}
