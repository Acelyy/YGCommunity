package com.yonggang.ygcommunity.Activity.Personal;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.Activity.BbsPicActivity;
import com.yonggang.ygcommunity.Activity.Server.ActDetailActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Activity;
import com.yonggang.ygcommunity.Entry.MyActivity;
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

import static com.yonggang.ygcommunity.R.id.act_title;

public class MyActActivity extends BaseActivity{

    @BindView(R.id.list_act)
    PullToRefreshListView listAct;

    private YGApplication app;

    private List<Activity.ActivityBean> list_act;

    private int total;

    private MyActAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_act);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        // 上拉加载更多，分页加载
        listAct.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listAct.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listAct.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listAct.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listAct.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listAct.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        my_activity(1);
        listAct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
        listAct.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                my_activity(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    my_activity(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    private void my_activity(final int page) {
        Subscriber subscriber = new Subscriber<MyActivity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listAct.onRefreshComplete();
            }

            @Override
            public void onNext(MyActivity data) {
                Log.i("my_activity", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_act = data.getMy_activity();
                    adapter = new MyActAdapter();
                    listAct.setAdapter(adapter);
                    listAct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MyActActivity.this, ActDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id", list_act.get(position - 1).getId() + "");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    list_act.addAll(data.getMy_activity());
                    adapter.notifyDataSetChanged();
                }
                listAct.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listAct.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listAct.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().my_activity(subscriber, app.getUser().getUser_id(), page);
    }

    class MyActAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_act.size();
        }

        @Override
        public Object getItem(int position) {
            return list_act.get(position);
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
                convertView = LayoutInflater.from(MyActActivity.this).inflate(R.layout.item_my_act, null);
                holder.act_pic = (ImageView) convertView.findViewById(R.id.act_pic);
                holder.act_title = (TextView) convertView.findViewById(act_title);
                holder.act_date = (TextView) convertView.findViewById(R.id.act_date);
                holder.act_area = (TextView) convertView.findViewById(R.id.act_area);
                holder.act_type = (TextView) convertView.findViewById(R.id.act_type);
                holder.act_status = (TextView) convertView.findViewById(R.id.act_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.act_title.setText(list_act.get(position).getTitle());
            holder.act_date.setText(list_act.get(position).getDate_start() + "起");
            holder.act_area.setText(list_act.get(position).getAddress());
            holder.act_type.setText(list_act.get(position).getAc_type());
            String status = list_act.get(position).getState();
            if ("2".equals(status)) {
                holder.act_status.setText("已结束");
                holder.act_status.setBackgroundResource(R.drawable.back_gray);
                holder.act_status.setTextColor(Color.parseColor("#999999"));
            } else if ("0".equals(status)) {
                holder.act_status.setText("进行中");
                holder.act_status.setBackgroundResource(R.drawable.back_red);
                holder.act_status.setTextColor(Color.parseColor("#FF0000"));
            } else if ("1".equals(status)) {
                holder.act_status.setText("筹备中");
                holder.act_status.setBackgroundResource(R.drawable.back_blue);
                holder.act_status.setTextColor(Color.parseColor("#16ADFC"));
            }
            Glide.with(MyActActivity.this)
                    .load(list_act.get(position).getImages())
                    .error(R.mipmap.pic_loading_error)
                    .centerCrop()
                    .into(holder.act_pic);

            holder.act_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", 0);
                    ArrayList<String> list_url = new ArrayList<String>();
                    list_url.add(list_act.get(position).getImages());
                    bundle.putStringArrayList("imgs", list_url);
                    stepActivity(bundle, BbsPicActivity.class);
                }
            });
            AutoUtils.auto(convertView);
            return convertView;
        }

        class ViewHolder {
            ImageView act_pic;
            TextView act_title;
            TextView act_date;
            TextView act_area;
            TextView act_type;
            TextView act_status;
        }
    }

}
