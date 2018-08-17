package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Score;
import com.yonggang.ygcommunity.Entry.TotalScore;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.LinearLayoutForListView;
import com.yonggang.ygcommunity.View.RiseNumberTextView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ScoreActivity extends BaseActivity {

    @BindView(R.id.txt_score)
    RiseNumberTextView txtScore;
    @BindView(R.id.list_score)
    LinearLayoutForListView listScore;
    @BindView(R.id.score_rule)
    LinearLayout scoreRule;
    @BindView(R.id.score_layout)
    PullToRefreshScrollView scoreLayout;
    @BindView(R.id.score_detail)
    RelativeLayout scoreDetail;

    private YGApplication app;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        app = (YGApplication) getApplication();
        ButterKnife.bind(this);
        scoreLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getScore();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getScore();
    }

    @OnClick({R.id.img_finish, R.id.score_record, R.id.score_rule, R.id.score_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.score_rule:
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                stepActivity(bundle, ScoreRuleActivity.class);
                break;
            case R.id.score_record:
                stepActivity(ScoreRecordActivity.class);
                break;
            case R.id.score_pay:
                stepActivity(GiftActivity.class);
                break;
        }
    }

    /**
     * 获取积分总数
     */
    private void getScore() {
        Subscriber subscriber = new Subscriber<TotalScore>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                scoreLayout.onRefreshComplete();
                Log.i("error", e.toString());
                Toast.makeText(ScoreActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(TotalScore data) {
                Log.i("getScore", data.toString());
                url = data.getUrl();
                scoreRule.setVisibility(View.VISIBLE);
                txtScore.withNumber(data.getScore()).start();
                getScoreList(1);
                scoreDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stepActivity(ScoreListActivity.class);
                    }
                });
                scoreLayout.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().getScore(subscriber, app.getUser().getUser_id());
    }

    /**
     * 获取积分详细列表
     *
     * @param page
     */
    private void getScoreList(int page) {
        Subscriber subscriber = new Subscriber<Score>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                scoreLayout.onRefreshComplete();
                Toast.makeText(ScoreActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Score data) {
                Log.i("getScoreList", data.toString());
                listScore.setAdapter(new ScoreAdapter(data.getScore()));
                scoreLayout.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().getScoreList(subscriber, app.getUser().getUser_id(), page);
    }

    /**
     *
     */
    class ScoreAdapter extends BaseAdapter {
        List<Score.ScoreBean> data;

        public ScoreAdapter(List<Score.ScoreBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size() > 3 ? 3 : data.size();
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
            if (position > 2) {
                return null;
            }
            View view = LayoutInflater.from(ScoreActivity.this).inflate(R.layout.item_score, null);
            TextView score_reason = (TextView) view.findViewById(R.id.score_reason);
            TextView score_time = (TextView) view.findViewById(R.id.score_time);
            TextView score_points = (TextView) view.findViewById(R.id.score_points);
            score_reason.setText(data.get(position).getSource());
            score_time.setText(data.get(position).getTime());
            score_points.setText(data.get(position).getPoints());
            AutoUtils.autoSize(view);
            return view;
        }
    }
}
