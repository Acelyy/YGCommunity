package com.yonggang.ygcommunity;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yonggang.ygcommunity.Entry.GridUser;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.zhy.autolayout.config.AutoLayoutConifg;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by liyangyang on 2017/2/13.
 */

public class YGApplication extends Application {
    private User user;
    private GridUser grid;

    public GridUser getGrid() {
        return grid;
    }

    public void setGrid(GridUser grid) {
        this.grid = grid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    static {
        PlatformConfig.setWeixin("wxea73d8c82ab506d0", "4d9832e537668d9255f22ce91adab8ea");
        PlatformConfig.setQQZone("1106237939", "TU4HvND12zrHPska");
        PlatformConfig.setSinaWeibo("2862473529", "2b58dc531b9b011c2809e246a2edb39a", "http://zhyl.yong-gang.com/zhyl/index.php/Home/Index/xlwb_callback");
    }

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.refresh_back, android.R.color.black);//全局设置主题颜色
                return new ClassicsHeader(context);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "d7157dea45", true);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        Config.DEBUG = true;
        UMShareAPI.get(this);
        initTbs();
        if (SpUtil.checkTime(this, System.currentTimeMillis())) {
            setUser(SpUtil.getUser(this));
        }
        AutoLayoutConifg.getInstance().init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    private void initTbs() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("onViewInitFinished", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }


}
