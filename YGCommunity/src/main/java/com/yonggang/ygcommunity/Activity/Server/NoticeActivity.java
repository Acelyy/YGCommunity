package com.yonggang.ygcommunity.Activity.Server;

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
import com.yonggang.ygcommunity.Entry.Notice;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class NoticeActivity extends BaseActivity {

    @BindView(R.id.list_notice)
    PullToRefreshListView listNotice;

    private List<Notice.NoticeBean> list_data;

    private int total;

    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        // 上拉加载更多，分页加载
        listNotice.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listNotice.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listNotice.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listNotice.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listNotice.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listNotice.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        notice_list(1);
        listNotice.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        listNotice.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                notice_list(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    notice_list(adapter.getCount() / 10 + 1);
                }
            }
        });

    }

    /**
     * 公告列表
     *
     * @param page
     */
    private void notice_list(final int page) {
        Subscriber subscriber = new Subscriber<Notice>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listNotice.onRefreshComplete();
            }

            @Override
            public void onNext(Notice data) {
                Log.i("notice_list", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_data = data.getNotice();
                    adapter = new NoticeAdapter();
                    listNotice.setAdapter(adapter);
                    listNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("notice", list_data.get(position - 1));
                            stepActivity(bundle, NoticeDetailActivity.class);
                        }
                    });
                } else {
                    list_data.addAll(data.getNotice());
                    adapter.notifyDataSetChanged();
                }
                listNotice.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listNotice.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listNotice.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }

            }
        };
        HttpUtil.getInstance().notice_list(subscriber, page);
    }

    @OnClick({R.id.img_finish, R.id.img_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.img_search:
                stepActivity(SearchNoticeActivity.class);
                break;
        }
    }

    /**
     *
     */
    class NoticeAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(NoticeActivity.this).inflate(R.layout.item_notice, null);
                holder.text_title = (TextView) convertView.findViewById(R.id.text_title);
                holder.text_time = (TextView) convertView.findViewById(R.id.text_time);
                holder.text_bm = (TextView) convertView.findViewById(R.id.text_bm);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.text_title.setText(list_data.get(position).getTitle());
            holder.text_time.setText(list_data.get(position).getStime());
            holder.text_bm.setText(list_data.get(position).getBm());
            AutoUtils.autoSize(convertView);
            return convertView;

        }

        class ViewHolder {
            TextView text_title;
            TextView text_time;
            TextView text_bm;
        }
    }

}
