package com.yonggang.ygcommunity.Fragment.Main;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;
import com.yonggang.ygcommunity.Activity.NewsActivity;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Title;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

/**
 * Created by liyangyang on 2017/3/7.
 */

public class NewsFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {
    @BindView(R.id.list_news)
    PullToRefreshListView listNews;

    private Title title;

    private NewsItem newsItem;

    private NewsAdapter adapter;

    private int totalCount;

    private YGApplication app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_news, container, false);
        ButterKnife.bind(this, view);
        listNews.setMode(PullToRefreshBase.Mode.BOTH);
        // 上拉加载更多，分页加载
        listNews.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listNews.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listNews.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listNews.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listNews.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listNews.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        Log.i("onCreateView", this.toString());
        app = (YGApplication) getActivity().getApplication();
        loadNews(title.getCategory_id(), title.getCategory_type(), 1);
        listNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), NewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsItem", newsItem.getNews().get(position - 2));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }

        });

        listNews.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadNews(title.getCategory_id(), title.getCategory_type(), 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() - 1 < totalCount) {
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
    public void loadNews(String category_id, int ctype, final int page) {
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
            public void onNext(final NewsItem newsItem) {
                totalCount = newsItem.getTotal();

                if (page == 1) {
                    NewsFragment.this.newsItem = newsItem;
                    adapter = new NewsAdapter();
                    listNews.setAdapter(adapter);
                } else {
                    NewsFragment.this.newsItem.getCarousel().addAll(newsItem.getCarousel());
                    NewsFragment.this.newsItem.getNews().addAll(newsItem.getNews());
                    adapter.notifyDataSetChanged();
                }
                listNews.onRefreshComplete();
                if (adapter.getCount() - 1 < totalCount) {
                    listNews.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listNews.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                Log.i("s", newsItem.toString());
                Log.i("NewsTotal", totalCount + "-------" + adapter.getCount());
            }
        };
        HttpUtil.getInstance().getCList(onNextListener, category_id, ctype, page, app.getUser() == null ? "" : app.getUser().getUser_id());
    }

    public void setCategory(Title title) {
        this.title = title;
    }

    class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (newsItem == null) {
                return 0;
            } else {
                return newsItem.getNews().size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return position == 0 ? newsItem.getCarousel() : newsItem.getNews().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, final ViewGroup parent) {
            View view = null;
            if (position == 0) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_banner, null);
                final Banner banner = (Banner) view.findViewById(R.id.banner);
                if (null == newsItem.getCarousel() || newsItem.getCarousel().size() == 0) {
                    banner.setVisibility(View.GONE);
                } else {
                    //加载轮播图
                    List<String> urlList = new ArrayList<>();
                    List<String> titleList = new ArrayList<>();
                    for (NewsItem.CarouselBean bean : newsItem.getCarousel()) {
                        urlList.add(bean.getCarousel_pic());
                        titleList.add(bean.getNewstitle());
                    }
                    banner.setImages(urlList)
                            .setDelayTime(3000)
                            .setBannerTitles(titleList)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                            .setImageLoader(new com.youth.banner.loader.ImageLoader() {
                                @Override
                                public void displayImage(Context context, Object path, ImageView imageView) {
                                    //ImageLoader.getInstance().displayImage(path.toString(), imageView);
                                    Glide.with(NewsFragment.this)
                                            .load(path.toString())
                                            .error(R.mipmap.pic_loading_error)
                                            .centerCrop()
                                            .into(imageView);
                                }
                            }).start();
                    banner.setOnBannerListener(new OnBannerListener() {
                        @Override
                        public void OnBannerClick(int position) {
                            Intent intent = new Intent(getActivity(), NewsActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsItem", NewsItem.CarouselToBean(newsItem.getCarousel().get(position)));
                            intent.putExtras(bundle);
                            getActivity().startActivity(intent);
                        }
                    });

                }
            } else {
                if (newsItem.getNews().get(position - 1).getIsadvert() == 1) {//如果是广告
                    if (newsItem.getNews().get(position - 1).getAdvsize() == 1) {
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_advert, null);
                        TextView news_title = (TextView) view.findViewById(R.id.news_title);
                        ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                        news_title.setText(newsItem.getNews().get(position - 1).getNews_title());
                        Glide.with(NewsFragment.this)
                                .load(newsItem.getNews().get(position - 1).getTpicsurl().get(0))
                                .centerCrop()
                                .dontAnimate()
                                .error(R.mipmap.pic_loading_error)
                                .into(news_pic);
                    } else {
                        if (newsItem.getNews().get(position - 1).getTpicscount() == 1) {
                            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_advert_single, null);
                            TextView news_title = (TextView) view.findViewById(R.id.news_title);
                            ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                            news_title.setText(newsItem.getNews().get(position - 1).getNews_title());
                            Glide.with(NewsFragment.this)
                                    .load(newsItem.getNews().get(position - 1).getTpicsurl().get(0))
                                    .centerCrop()
                                    .dontAnimate()
                                    .error(R.mipmap.pic_loading_error)
                                    .into(news_pic);
                        } else {
                            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_advert_triple, null);
                            TextView news_title = (TextView) view.findViewById(R.id.news_title);
                            ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                            ImageView news_pic2 = (ImageView) view.findViewById(R.id.news_pic2);
                            ImageView news_pic3 = (ImageView) view.findViewById(R.id.news_pic3);
                            news_title.setText(newsItem.getNews().get(position - 1).getNews_title());
                            Glide.with(NewsFragment.this)
                                    .load(newsItem.getNews().get(position - 1).getTpicsurl().get(0))
                                    .centerCrop()
                                    .dontAnimate()
                                    .error(R.mipmap.pic_loading_error)
                                    .into(news_pic);
                            Glide.with(NewsFragment.this)
                                    .load(newsItem.getNews().get(position - 1).getTpicsurl().get(1))
                                    .centerCrop()
                                    .dontAnimate()
                                    .error(R.mipmap.pic_loading_error)
                                    .into(news_pic2);
                            Glide.with(NewsFragment.this)
                                    .load(newsItem.getNews().get(position - 1).getTpicsurl().get(2))
                                    .centerCrop()
                                    .dontAnimate()
                                    .error(R.mipmap.pic_loading_error)
                                    .into(news_pic3);
                        }
                    }

                } else {
                    if (newsItem.getNews().get(position - 1).getTpicscount() == 1) {
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_single, null);
                        TextView news_title = (TextView) view.findViewById(R.id.news_title);
                        TextView news_source = (TextView) view.findViewById(R.id.news_source);
                        TextView read_count = (TextView) view.findViewById(R.id.read_count);
                        TextView audit_time = (TextView) view.findViewById(R.id.audit_time);
                        ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                        RelativeLayout layout_not_stick = (RelativeLayout) view.findViewById(R.id.layout_not_stick);
                        RelativeLayout layout_stick = (RelativeLayout) view.findViewById(R.id.layout_stick);
                        news_title.setText(newsItem.getNews().get(position - 1).getNews_title());
                        news_source.setText(newsItem.getNews().get(position - 1).getNewssource());
                        read_count.setText(newsItem.getNews().get(position - 1).getReadcount() + "阅读");
                        audit_time.setText(newsItem.getNews().get(position - 1).getAudit_time());
                        Glide.with(NewsFragment.this)
                                .load(newsItem.getNews().get(position - 1).getTpicsurl().get(0))
                                .centerCrop()
                                .dontAnimate()
                                .error(R.mipmap.pic_loading_error)
                                .into(news_pic);
                        if (newsItem.getNews().get(position - 1).getNewsstick() == 0) {
                            layout_not_stick.setVisibility(View.VISIBLE);
                            layout_stick.setVisibility(View.GONE);
                        } else {
                            layout_not_stick.setVisibility(View.GONE);
                            layout_stick.setVisibility(View.VISIBLE);
                        }
                    } else if (newsItem.getNews().get(position - 1).getTpicscount() == 3) {
                        view = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_triple, null);
                        TextView news_title = (TextView) view.findViewById(R.id.news_title);
                        TextView news_source = (TextView) view.findViewById(R.id.news_source);
                        TextView audit_time = (TextView) view.findViewById(R.id.audit_time);
                        TextView read_count = (TextView) view.findViewById(R.id.read_count);
                        ImageView news_pic = (ImageView) view.findViewById(R.id.news_pic);
                        ImageView news_pic2 = (ImageView) view.findViewById(R.id.news_pic2);
                        ImageView news_pic3 = (ImageView) view.findViewById(R.id.news_pic3);
                        LinearLayout layout_not_stick = (LinearLayout) view.findViewById(R.id.layout_not_stick);
                        RelativeLayout layout_stick = (RelativeLayout) view.findViewById(R.id.layout_stick);
                        news_title.setText(newsItem.getNews().get(position - 1).getNews_title());
                        news_source.setText(newsItem.getNews().get(position - 1).getNewssource());
                        read_count.setText(newsItem.getNews().get(position - 1).getReadcount() + "阅读");
                        audit_time.setText(newsItem.getNews().get(position - 1).getAudit_time());
                        Glide.with(NewsFragment.this)
                                .load(newsItem.getNews().get(position - 1).getTpicsurl().get(0))
                                .centerCrop()
                                .dontAnimate()
                                .error(R.mipmap.pic_loading_error)
                                .into(news_pic);
                        Glide.with(NewsFragment.this)
                                .load(newsItem.getNews().get(position - 1).getTpicsurl().get(1))
                                .centerCrop()
                                .dontAnimate()
                                .error(R.mipmap.pic_loading_error)
                                .into(news_pic2);
                        Glide.with(NewsFragment.this)
                                .load(newsItem.getNews().get(position - 1).getTpicsurl().get(2))
                                .centerCrop()
                                .dontAnimate()
                                .error(R.mipmap.pic_loading_error)
                                .into(news_pic3);
                        if (newsItem.getNews().get(position - 1).getNewsstick() == 0) {
                            layout_not_stick.setVisibility(View.VISIBLE);
                            layout_stick.setVisibility(View.GONE);
                        } else {
                            layout_not_stick.setVisibility(View.GONE);
                            layout_stick.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
            AutoUtils.autoSize(view);
            return view;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", this.toString());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i("onActivityCreated", this.toString());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("onAttach", this.toString());
    }

    @Override
    public void onPause() {
        Log.i("onPause", this.toString());
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("onResume", this.toString());
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.i("onDestroy", this.toString());
        super.onDestroy();
    }

}
