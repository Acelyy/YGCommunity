package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Score;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ScoreListActivity extends BaseActivity{

    @BindView(R.id.list_score)
    PullToRefreshListView listScore;

    private YGApplication app;

    private ScoreAdapter adapter;

    private int total;

    private List<Score.ScoreBean> list_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        // 上拉加载更多，分页加载
        listScore.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listScore.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listScore.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listScore.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listScore.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listScore.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        getScoreList(1);
        listScore.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listScore.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getScoreList(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    getScoreList(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    /**
     *
     */
    private void getScoreList(final int page) {
        Subscriber subscriber = new Subscriber<Score>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listScore.onRefreshComplete();
                Toast.makeText(ScoreListActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Score data) {
                Log.i("getScoreList", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_score = data.getScore();
                    adapter = new ScoreAdapter();
                    listScore.setAdapter(adapter);
                } else {
                    list_score.addAll(data.getScore());
                    adapter.notifyDataSetChanged();
                }
                listScore.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listScore.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listScore.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().getScoreList(subscriber, app.getUser().getUser_id(), page);
    }

    class ScoreAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_score.size();
        }

        @Override
        public Object getItem(int position) {
            return list_score.get(position);
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
                convertView = LayoutInflater.from(ScoreListActivity.this).inflate(R.layout.item_score, null);
                holder.score_reason = (TextView) convertView.findViewById(R.id.score_reason);
                holder.score_time = (TextView) convertView.findViewById(R.id.score_time);
                holder.score_points = (TextView) convertView.findViewById(R.id.score_points);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.score_reason.setText(list_score.get(position).getSource());
            holder.score_time.setText(list_score.get(position).getTime());
            holder.score_points.setText(list_score.get(position).getPoints());
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView score_reason;
            TextView score_time;
            TextView score_points;
        }
    }

}
