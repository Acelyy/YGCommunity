package com.yonggang.ygcommunity.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyViewPager;
import com.yonggang.ygcommunity.Activity.AddBbsActivity;
import com.yonggang.ygcommunity.Fragment.Assembly.BbsFragment;
import com.yonggang.ygcommunity.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liyangyang on 2017/2/25.
 */

public class AssemblyFragment extends Fragment {

    @BindViews({R.id.text_all, R.id.text_done})
    TextView[] textViews;
    @BindView(R.id.pager)
    LazyViewPager pager;

    private List<Fragment> list_fragment = new ArrayList<>();

    private int index;//当前page的页码

    private static final int COLOR_CLICK = Color.parseColor("#16ADFC");
    private static final int COLOR_UNCLICK = Color.parseColor("#666666");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_assembly, container, false);
        ButterKnife.bind(this, view);
        for (int i = 1; i <= 2; i++) {
            BbsFragment bbsFragment = new BbsFragment();
            bbsFragment.setType(i);
            list_fragment.add(bbsFragment);
        }
        pager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        pager.addOnPageChangeListener(new MyPagerChangeListener());
        return view;
    }

    @OnClick({R.id.layout_today, R.id.layout_all, R.id.layout_done, R.id.bbs_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.layout_today:
//                pager.setCurrentItem(0);
//                break;
            case R.id.layout_all:
                pager.setCurrentItem(1);
                break;
            case R.id.layout_done:
                pager.setCurrentItem(2);
                break;
            case R.id.bbs_add:
                Intent intent = new Intent(getActivity(), AddBbsActivity.class);
                startActivity(intent);
                break;
        }
    }

    public class MyPagerAdapter extends LazyFragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "1111";
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

    /**
     * 切换tab操作
     *
     * @param page
     */
    private void changePage(int page) {
        if (page != index) {
            textViews[page].setTextColor(COLOR_CLICK);
            textViews[index].setTextColor(COLOR_UNCLICK);
        }
        index = page;
    }

    class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            changePage(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
