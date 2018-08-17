package com.yonggang.ygcommunity.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.yonggang.ygcommunity.Fragment.Main.ImageDetailFragment;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.HackyViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BbsPicActivity extends AppCompatActivity {


    @BindView(R.id.page_pic)
    HackyViewPager pagePic;

    private List<String> imgs;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_pic);
        ButterKnife.bind(this);
        imgs = getIntent().getExtras().getStringArrayList("imgs");
        index = getIntent().getExtras().getInt("index");
        pagePic.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        pagePic.setCurrentItem(index);
    }

    /**
     * 相册适配器
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String url = imgs.get(position);
            return ImageDetailFragment.newInstance(url, "BbsPicActivity");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "pic";
        }

        @Override
        public int getCount() {
            return imgs.size();
        }

    }
}
