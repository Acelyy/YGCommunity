package com.yonggang.ygcommunity.Activity.Server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyViewPager;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Home;
import com.yonggang.ygcommunity.Fragment.Server.ExpensesFragment;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.PagerSlidingTabStrip;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExpensesActivity extends BaseActivity {
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.pager)
    LazyViewPager pager;
    private YGApplication app;

    private List<Home> list_home = new ArrayList<>();

    private MyPagerAdapter adapter;

    private List<ExpensesFragment> fragments = new ArrayList<>();

    private ExitBroadcast broadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        account_tab(getIntent().getIntExtra("index", 0));
        broadcast = new ExitBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("finish");
        registerReceiver(broadcast, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcast);
        super.onDestroy();
    }

    /**
     * 获取家庭列表
     */
    private void account_tab(final int page) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<List<Home>>() {

            @Override
            public void onNext(List<Home> data) {
                Log.i("tab_name_list", data.toString());
                list_home = data;
                fragments.clear();
                for (int i = 0; i < list_home.size(); i++) {
                    ExpensesFragment f = new ExpensesFragment();
                    f.setArg(list_home.get(i), i);
                    fragments.add(f);
                }
                if (adapter == null) {
                    adapter = new MyPagerAdapter(getSupportFragmentManager());
                    pager.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                tabs.setViewPager(pager);
                if (page == 0x888) {
                    pager.setCurrentItem(list_home.size() - 1, false);
                } else {
                    pager.setCurrentItem(page, false);
                }
            }
        };
        HttpUtil.getInstance().tab_name_list(new ProgressSubscriber<List<Home>>(onNextListener, this), app.getUser().getUser_id());
    }

    @OnClick({R.id.img_finish, R.id.bbs_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.bbs_add:
                Intent intent = new Intent(this, AddHomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list_home.get(position).getTab_name();
        }

        @Override
        public int getCount() {
            return list_home.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }

    /**
     * 设置退出的广播
     */
    class ExitBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

}
