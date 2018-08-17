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
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.Activity.BbsDetailActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.Entry.Publish;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class PublishActivity extends BaseActivity{

    @BindView(R.id.list_publish)
    PullToRefreshListView listPublish;

    private YGApplication app;

    private List<Bbs.BbsBean> list_publish;

    private int total;

    private PublishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        // 上拉加载更多，分页加载
        listPublish.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listPublish.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listPublish.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listPublish.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listPublish.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listPublish.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        my_publish(1);

        listPublish.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                my_publish(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    my_publish(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    private void my_publish(final int page) {
        Subscriber subscriber = new Subscriber<Publish>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(PublishActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(PublishActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof UnknownHostException) {
                    Toast.makeText(PublishActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listPublish.onRefreshComplete();
            }

            @Override
            public void onNext(Publish data) {
                Log.i("my_publish", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_publish = data.getPublish();
                    adapter = new PublishAdapter();
                    listPublish.setAdapter(adapter);
                    listPublish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bbs", list_publish.get(position - 1));
                            stepActivity(bundle, BbsDetailActivity.class);
                        }
                    });
                } else {
                    list_publish.addAll(data.getPublish());
                    adapter.notifyDataSetChanged();
                }
                listPublish.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listPublish.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listPublish.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().my_publish(subscriber, app.getUser().getUser_id(), page);
    }

    class PublishAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_publish.size();
        }

        @Override
        public Object getItem(int position) {
            return list_publish.get(position);
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
                convertView = LayoutInflater.from(PublishActivity.this).inflate(R.layout.item_publish, null);
                holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
                holder.text_time = (TextView) convertView.findViewById(R.id.text_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text_title.setText(list_publish.get(position).getBbstitle());
            holder.text_time.setText(list_publish.get(position).getBbstime());
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView text_title;
            TextView text_time;
        }
    }
}
