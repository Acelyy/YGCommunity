package com.yonggang.ygcommunity.Fragment.Main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;
import com.yonggang.ygcommunity.Activity.PicActivity;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Title;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by liyangyang on 2017/3/7.
 */

public class ImageFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {

    @BindView(R.id.list_news)
    PullToRefreshListView listNews;

    private Title title;

    private List<NewsItem.NewsBean> list_news;

    private ImageAdapter adapter;

    private YGApplication app;

    private int total;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_image, container, false);
        Log.i("onCreateView", this.toString());
        ButterKnife.bind(this, view);
        // 上拉加载更多，分页加载
        listNews.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listNews.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listNews.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listNews.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listNews.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listNews.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        app = (YGApplication) getActivity().getApplication();
        loadNews(title.getCategory_id(), title.getCategory_type(), 1);
        listNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadNews(title.getCategory_id(), title.getCategory_type(), 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    loadNews(title.getCategory_id(), title.getCategory_type(), adapter.getCount() / 10 + 1);
                }
            }
        });
        return view;
    }

    /**
     * 获取具体新闻列表
     *
     * @param category_id
     * @param ctype
     * @param page
     */
    private void loadNews(String category_id, int ctype, final int page) {
        Subscriber onNextListener = new Subscriber<NewsItem>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(getActivity(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listNews.onRefreshComplete();
            }

            @Override
            public void onNext(NewsItem newsItem) {
                total = newsItem.getTotal();
                if (page == 1) {
                    list_news = newsItem.getNews();
                    adapter = new ImageAdapter();
                    listNews.setAdapter(adapter);
                    listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getActivity(), PicActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsItem", list_news.get(position - 1));
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }
                    });
                } else {
                    list_news.addAll(newsItem.getNews());
                    adapter.notifyDataSetChanged();
                }
                listNews.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listNews.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listNews.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                Log.i("s", newsItem.toString());
            }
        };
        HttpUtil.getInstance().getCList(onNextListener, category_id, ctype, page, app.getUser() == null ? "" : app.getUser().getUser_id());

    }

    public void setCategory(Title title) {
        this.title = title;
    }

    class ImageAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_news == null ? 0 : list_news.size();
        }

        @Override
        public Object getItem(int position) {
            return list_news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (list_news.get(position).getTpicscount() == 1) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_iamge_single, null);
                ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                TextView news_title = (TextView) view.findViewById(R.id.news_title);
                TextView pic_num = (TextView) view.findViewById(R.id.pic_num);
                TextView comment_num = (TextView) view.findViewById(R.id.comment_num);
                //设置初始值
                news_title.setText(list_news.get(position).getNews_title());
                pic_num.setText(list_news.get(position).getContent_num() + "");
                comment_num.setText(list_news.get(position).getComment_num() + "");
                Glide.with(ImageFragment.this)
                        .load(list_news.get(position).getTpicsurl().get(0))
                        .centerCrop()
                        .dontAnimate()
                        .error(R.mipmap.pic_loading_error)
                        .into(news_pic);
            } else if (list_news.get(position).getTpicscount() == 3) {
                if (list_news.get(position).getWeight() <= 0.5) {
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.item_image_triple_a, null);
                } else {
                    view = LayoutInflater.from(getActivity()).inflate(R.layout.item_image_triple_b, null);
                }
                ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                ImageView news_pic2 = (ImageView) view.findViewById(R.id.news_pic2);
                ImageView news_pic3 = (ImageView) view.findViewById(R.id.news_pic3);
                TextView news_title = (TextView) view.findViewById(R.id.news_title);
                TextView pic_num = (TextView) view.findViewById(R.id.pic_num);
                TextView comment_num = (TextView) view.findViewById(R.id.comment_num);
                //设置初始值
                news_title.setText(list_news.get(position).getNews_title());
                pic_num.setText(list_news.get(position).getContent_num() + "");
                comment_num.setText(list_news.get(position).getComment_num() + "");
                Glide.with(ImageFragment.this)
                        .load(list_news.get(position).getTpicsurl().get(0))
                        .centerCrop()
                        .dontAnimate()
                        .error(R.mipmap.pic_loading_error)
                        .into(news_pic);
                Glide.with(ImageFragment.this)
                        .load(list_news.get(position).getTpicsurl().get(1))
                        .centerCrop()
                        .dontAnimate()
                        .error(R.mipmap.pic_loading_error)
                        .into(news_pic2);
                Glide.with(ImageFragment.this)
                        .load(list_news.get(position).getTpicsurl().get(2))
                        .centerCrop()
                        .dontAnimate()
                        .error(R.mipmap.pic_loading_error)
                        .into(news_pic3);
            }
            AutoUtils.autoSize(view);
            return view;
        }
    }
}
