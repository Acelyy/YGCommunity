package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.Activity.NewsActivity;
import com.yonggang.ygcommunity.Activity.PicActivity;
import com.yonggang.ygcommunity.Activity.VideoDetailActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Answer;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.LinearLayoutForListView;
import com.yonggang.ygcommunity.View.RoundAngleImageView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class AnswerActivity extends BaseActivity {
    @BindView(R.id.list_answer)
    PullToRefreshListView listAnswer;
    private YGApplication app;

    private List<Answer.CommentsBean> list_data;

    private int total;

    private AnswerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.bind(this);
        // 上拉加载更多，分页加载
        listAnswer.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listAnswer.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listAnswer.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listAnswer.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listAnswer.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listAnswer.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        app = (YGApplication) getApplication();
        my_answers(1);
        listAnswer.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                my_answers(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    my_answers(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    /**
     * 获取我的跟帖
     *
     * @param page
     */
    private void my_answers(final int page) {
        Subscriber subscriber = new Subscriber<Answer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listAnswer.onRefreshComplete();
            }

            @Override
            public void onNext(Answer data) {
                Log.i("my_answers", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_data = data.getComments();
                    adapter = new AnswerAdapter();
                    listAnswer.setAdapter(adapter);
                    listAnswer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            NewsItem.NewsBean news = list_data.get(position - 1).getNews();
                            bundle.putSerializable("newsItem", news);
                            int type = news.getCategory_type();
                            if (type == 0) {
                                stepActivity(bundle, NewsActivity.class);
                            } else if (type == 1) {
                                stepActivity(bundle, PicActivity.class);
                            } else if (type == 2) {
                                stepActivity(bundle, VideoDetailActivity.class);
                            }
                        }
                    });
                } else {
                    list_data.addAll(data.getComments());
                    adapter.notifyDataSetChanged();
                }
                listAnswer.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listAnswer.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listAnswer.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().my_answers(subscriber, app.getUser().getUser_id(), page);
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    class AnswerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_data.size();
        }

        @Override
        public Object getItem(int position) {
            return list_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AnswerActivity.this).inflate(R.layout.item_mine_comment, null);
                holder.head = (RoundAngleImageView) convertView.findViewById(R.id.head);
                holder.comments_author = (TextView) convertView.findViewById(R.id.comments_author);
                holder.comments_time = (TextView) convertView.findViewById(R.id.comments_time);
                holder.comments_content = (TextView) convertView.findViewById(R.id.comments_content);
                holder.comments_title = (TextView) convertView.findViewById(R.id.comments_title);
                holder.comments_child = (LinearLayoutForListView) convertView.findViewById(R.id.comments_child);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.comments_author.setText(list_data.get(position).getComments_author());
            holder.comments_time.setText(list_data.get(position).getComments_time());
            holder.comments_content.setText(list_data.get(position).getComments_content());
            holder.comments_title.setText("原文：" + list_data.get(position).getNews().getNews_title());
            List<Answer.CommentsBean.ParentBean> data = list_data.get(position).getParent();
            if (data != null) {
                holder.comments_child.setAdapter(new ChildAdapter(data));
            } else {
                holder.comments_child.setAdapter(null);
            }

            Glide.with(AnswerActivity.this)
                    .load(list_data.get(position).getFace_url())
                    .into(holder.head);
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            RoundAngleImageView head;
            TextView comments_author;
            TextView comments_time;
            TextView comments_content;
            TextView comments_title;
            LinearLayoutForListView comments_child;
        }
    }

    /**
     * 子评论
     */
    class ChildAdapter extends BaseAdapter {

        private List<Answer.CommentsBean.ParentBean> data;

        public ChildAdapter(List<Answer.CommentsBean.ParentBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(AnswerActivity.this).inflate(R.layout.item_comments_child, null);
            TextView comments_index = (TextView) view.findViewById(R.id.comments_index);
            TextView comments_author = (TextView) view.findViewById(R.id.comments_author);
            TextView comments_time = (TextView) view.findViewById(R.id.comments_time);
            TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
            comments_index.setText(position + 1 + "");
            comments_author.setText(data.get(position).getComments_author());
            comments_time.setText(data.get(position).getComments_time());
            comments_content.setText(data.get(position).getComments_content());
            AutoUtils.autoSize(view);
            return view;
        }
    }
}
