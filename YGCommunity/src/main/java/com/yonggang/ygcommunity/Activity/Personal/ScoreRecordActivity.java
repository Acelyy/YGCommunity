package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Gift;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ScoreRecordActivity extends BaseActivity {

    @BindView(R.id.list_record)
    PullToRefreshListView listRecord;

    private YGApplication app;

    private int total;

    private List<Gift.GiftBean> list_record = new ArrayList<>();

    private RecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_record);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        // 上拉加载更多，分页加载
        listRecord.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listRecord.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listRecord.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listRecord.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listRecord.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listRecord.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        gift_record(1);
        listRecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                gift_record(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    gift_record(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    /**
     * 兑换记录
     *
     * @param page
     */
    private void gift_record(final int page) {
        Subscriber subscriber = new Subscriber<Gift>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(ScoreRecordActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                listRecord.onRefreshComplete();
            }

            @Override
            public void onNext(Gift data) {
                Log.i("gift_record", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_record = data.getGift();
                    adapter = new RecordAdapter();
                    listRecord.setAdapter(adapter);
                } else {
                    list_record.addAll(data.getGift());
                    adapter.notifyDataSetChanged();
                }
                listRecord.onRefreshComplete();
                if (adapter.getCount()<total){
                    listRecord.setMode(PullToRefreshBase.Mode.BOTH);
                }else{
                    listRecord.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().gift_record(subscriber, app.getUser().getUser_id(), page);
    }

    /**
     * 兑换记录
     */
    class RecordAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_record.size();
        }

        @Override
        public Object getItem(int position) {
            return list_record.get(position);
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
                convertView = LayoutInflater.from(ScoreRecordActivity.this).inflate(R.layout.item_gift_record, null);
                holder.gift_img = (ImageView) convertView.findViewById(R.id.gift_img);
                holder.gift_name = (TextView) convertView.findViewById(R.id.gift_name);
                holder.gift_points = (TextView) convertView.findViewById(R.id.gift_points);
                holder.gift_price = (TextView) convertView.findViewById(R.id.gift_price);
                holder.gift_status = (ImageView) convertView.findViewById(R.id.gift_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.gift_name.setText(list_record.get(position).getGift());
            holder.gift_points.setText(list_record.get(position).getScore() + "分");
            holder.gift_price.setText("市场价：" + list_record.get(position).getPrice());
            Glide.with(app)
                    .load(list_record.get(position).getGift_img())
                    .error(R.mipmap.pic_loading_error)
                    .into(holder.gift_img);
            int status = list_record.get(position).getRecord();
            if (status == 1) {
                holder.gift_status.setVisibility(View.VISIBLE);
            } else {
                holder.gift_status.setVisibility(View.GONE);
            }
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            ImageView gift_img;
            TextView gift_name;
            TextView gift_points;
            TextView gift_price;
            ImageView gift_status;
        }
    }

}
