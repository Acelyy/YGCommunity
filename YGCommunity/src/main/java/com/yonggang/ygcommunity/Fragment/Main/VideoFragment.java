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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;
import com.yonggang.ygcommunity.Activity.VideoDetailActivity;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.Title;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.Subscriber;

/**
 * Created by liyangyang on 2017/3/7.
 */

public class VideoFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {

    @BindView(R.id.grid_video)
    PullToRefreshListView lv_video;

    private List<NewsItem.NewsBean> list_video;

    private Title title;

    private VideoAdapter adapter;

    private int totalCount;

    private List<JCVideoPlayerStandard> players;

    private YGApplication app;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_video, container, false);
        Log.i("onCreateView", this.toString());
        ButterKnife.bind(this, view);
        // 上拉加载更多，分页加载
        lv_video.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        lv_video.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        lv_video.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        lv_video.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        lv_video.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        lv_video.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        app = (YGApplication) getActivity().getApplication();
        loadNews(title.getCategory_id(), title.getCategory_type(), 1);
        lv_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = players.get(position - 1).getCurrentPositionWhenPlaying();
                JCUtils.saveProgress(getActivity(), list_video.get(position - 1).getJump_url(), pos);
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsItem", list_video.get(position - 1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        lv_video.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadNews(title.getCategory_id(), title.getCategory_type(), 1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < totalCount) {
                    loadNews(title.getCategory_id(), title.getCategory_type(), adapter.getCount() / 10 + 1);
                }
            }
        });
        return view;
    }

    public void stopPlay() {
        JCVideoPlayer.releaseAllVideos();
    }

    public void setCategory(Title title) {
        this.title = title;
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
                lv_video.onRefreshComplete();
                JCVideoPlayer.releaseAllVideos();
            }

            @Override
            public void onNext(final NewsItem newsItem) {
                Log.i("s", newsItem.toString());
                totalCount = newsItem.getTotal();
                if (page == 1) {
                    list_video = newsItem.getNews();
                    adapter = new VideoAdapter();
                    lv_video.setAdapter(adapter);
                } else {
                    list_video.addAll(newsItem.getNews());
                    adapter.notifyDataSetChanged();
                }
                lv_video.onRefreshComplete();
                if (adapter.getCount() < totalCount) {
                    lv_video.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    lv_video.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                Log.i("total+position", totalCount + "-------" + adapter.getCount());

                JCVideoPlayer.releaseAllVideos();
            }
        };
        HttpUtil.getInstance().getCList(onNextListener, category_id, ctype, page, app.getUser() == null ? "" : app.getUser().getUser_id());
    }

    /**
     * 视频适配器
     */
    class VideoAdapter extends BaseAdapter {

        public VideoAdapter() {
            players = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return list_video == null ? 0 : list_video.size();
        }

        @Override
        public Object getItem(int position) {
            return list_video.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_video, null);
                holder.player = (JCVideoPlayerStandard) convertView.findViewById(R.id.player);
                holder.video_source = (TextView) convertView.findViewById(R.id.video_source);
                holder.comment_num = (TextView) convertView.findViewById(R.id.comment_num);
                holder.share = (ImageView) convertView.findViewById(R.id.share);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.player.setUp(list_video.get(position).getJump_url(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, list_video.get(position).getNews_title());
            holder.video_source.setText(list_video.get(position).getNewssource());
            holder.comment_num.setText(list_video.get(position).getComment_num() + "跟帖");
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sharedToUM(list_video.get(position));
                }
            });
            Glide.with(VideoFragment.this)
                    .load(list_video.get(position).getTpicsurl().get(0))
                    .error(R.mipmap.pic_loading_error)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.player.thumbImageView);
            players.add(position, holder.player);
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            JCVideoPlayerStandard player;
            TextView video_source;
            TextView comment_num;
            ImageView share;
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
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("onResume", this.toString());
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 微信分享
     */
    private void sharedToUM(NewsItem.NewsBean newsBean) {
        UMVideo web = new UMVideo(newsBean.getJump_url());
        String title = newsBean.getNews_title();
        web.setTitle(title);//标题
        web.setThumb(new UMImage(getActivity(), newsBean.getTpicsurl().get(0)));  //缩略图
        web.setDescription(title);//描述
        new ShareAction(getActivity())
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.SINA)
                .setCallback(shareListener)
                .open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), "取消分享", Toast.LENGTH_LONG).show();
        }
    };

}
