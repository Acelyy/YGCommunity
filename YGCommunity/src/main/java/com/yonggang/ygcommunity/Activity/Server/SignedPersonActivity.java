package com.yonggang.ygcommunity.Activity.Server;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Signed;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.CircleImageView;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

import static com.yonggang.ygcommunity.R.id.phone;

public class SignedPersonActivity extends BaseActivity {

    @BindView(R.id.list_signed)
    PullToRefreshListView listSigned;

    private String id;

    private List<User> list_more;

    private int total;

    private SignedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed_person);
        ButterKnife.bind(this);
        id = getIntent().getExtras().getString("id");
        get_signed(1);
        // 上拉加载更多，分页加载
        listSigned.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listSigned.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listSigned.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listSigned.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listSigned.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listSigned.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        listSigned.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                get_signed(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                get_signed(adapter.getCount() / 10 + 1);
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    private void get_signed(final int page) {
        Subscriber subscriber = new Subscriber<Signed>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(SignedPersonActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                listSigned.onRefreshComplete();
            }

            @Override
            public void onNext(Signed data) {
                Log.i("get_signed", data.toString());
                if (page == 1) {
                    list_more = data.getMore();
                    adapter = new SignedAdapter();
                    listSigned.setAdapter(adapter);
                } else {
                    list_more.addAll(data.getMore());
                    adapter.notifyDataSetChanged();
                }
                if (adapter.getCount() < total) {
                    listSigned.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listSigned.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                listSigned.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().get_more_verify(subscriber, id, page);
    }

    class SignedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_more.size();
        }

        @Override
        public Object getItem(int position) {
            return list_more.get(position);
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
                convertView = LayoutInflater.from(SignedPersonActivity.this).inflate(R.layout.item_more, null);
                holder.head = (CircleImageView) convertView.findViewById(R.id.head);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.phone = (TextView) convertView.findViewById(phone);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(list_more.get(position).getUsername());
            String phone = list_more.get(position).getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            holder.phone.setText(phone);
            Glide.with(getApplicationContext())
                    .load(list_more.get(position).getFace_url())
                    .into(holder.head);
            return convertView;
        }

        class ViewHolder {
            CircleImageView head;
            TextView name;
            TextView phone;
        }
    }
}
