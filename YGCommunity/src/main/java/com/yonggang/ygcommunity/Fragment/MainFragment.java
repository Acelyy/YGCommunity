package com.yonggang.ygcommunity.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyViewPager;
import com.yonggang.ygcommunity.Entry.Title;
import com.yonggang.ygcommunity.Fragment.Main.ImageFragment;
import com.yonggang.ygcommunity.Fragment.Main.NewsFragment;
import com.yonggang.ygcommunity.Fragment.Main.VideoFragment;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.PagerSlidingTabStrip;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


/**
 * Created by liyangyang on 2017/2/25.
 */

public class MainFragment extends Fragment {

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @BindView(R.id.pager)
    LazyViewPager pager;

    MyPagerAdapter adapter;

    private SubscriberOnNextListener onNextListener;

    private List<Title> list_title = new ArrayList<Title>();

    private List<Fragment> list_fragment;

    private int index;//当前page的页码

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_main, container, false);
        ButterKnife.bind(this, view);
        onNextListener = new SubscriberOnNextListener<List<Title>>() {
            @Override
            public void onNext(List<Title> title) {
                list_title = title;
                list_fragment = new ArrayList<Fragment>();
                for (int i = 0; i < list_title.size(); i++) {
                    Fragment fragment = null;
                    switch (list_title.get(i).getCategory_type()) {
                        case 0:
                            fragment = new NewsFragment();
                            ((NewsFragment) fragment).setCategory(list_title.get(i));
                            break;
                        case 1:
                            fragment = new ImageFragment();
                            ((ImageFragment) fragment).setCategory(list_title.get(i));
                            break;
                        case 2:
                            fragment = new VideoFragment();
                            ((VideoFragment) fragment).setCategory(list_title.get(i));
                            break;
                        default:
                            break;
                    }
                    list_fragment.add(fragment);
                }
                adapter = new MyPagerAdapter(getChildFragmentManager());
                pager.setAdapter(adapter);
                pager.addOnPageChangeListener(new MyPagerChangeListener());
                tabs.setViewPager(pager);
            }
        };
        getTitle_list();
        return view;
    }

    public class MyPagerAdapter extends LazyFragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list_title.get(position).getCategory_name();
        }

        @Override
        public int getCount() {
            return list_fragment.size();
        }

        @Override
        protected Fragment getItem(ViewGroup container, int position) {
            return list_fragment.get(position);
        }
    }

    class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (list_fragment.get(index) instanceof VideoFragment) {
                if (position != index) {
                    JCVideoPlayer.releaseAllVideos();
                }
            }
            index = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 获取新闻标题
     */
    private void getTitle_list() {
        HttpUtil.getInstance().getCategory_list(new ProgressSubscriber<List<Title>>(onNextListener, getActivity()));
    }

}
