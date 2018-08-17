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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Message;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class MessageActivity extends BaseActivity {

    @BindView(R.id.list_message)
    PullToRefreshListView listMessage;

    private YGApplication app;

    private List<Message.MessageBean> list_data;

    private int total;

    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        // 上拉加载更多，分页加载
        listMessage.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listMessage.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listMessage.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listMessage.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listMessage.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listMessage.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        app = (YGApplication) getApplication();
        my_message(1);
        listMessage.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listMessage.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                my_message(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    my_message(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    private void my_message(final int page) {
        Subscriber subscriber = new Subscriber<Message>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listMessage.onRefreshComplete();
            }

            @Override
            public void onNext(Message data) {
                Log.i("my_message", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_data = data.getMessage();
                    adapter = new MessageAdapter();
                    listMessage.setAdapter(adapter);
                    listMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("bean", list_data.get(position - 1));
                            stepActivity(bundle, MessageDetailActivity.class);
                        }
                    });
                } else {
                    list_data.addAll(data.getMessage());
                    adapter.notifyDataSetChanged();
                }
                listMessage.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listMessage.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listMessage.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().my_message(subscriber, app.getUser().getUser_id(), page);
    }

    class MessageAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(MessageActivity.this).inflate(R.layout.item_message, null);
                holder.msg_sender = (TextView) convertView.findViewById(R.id.msg_sender);
                holder.msg_time = (TextView) convertView.findViewById(R.id.msg_time);
                holder.msg_content = (TextView) convertView.findViewById(R.id.msg_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.msg_sender.setText(list_data.get(position).getSender());
            holder.msg_time.setText(list_data.get(position).getStime());
            holder.msg_content.setText(list_data.get(position).getMsg());
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView msg_sender;
            TextView msg_time;
            TextView msg_content;
        }
    }
}
