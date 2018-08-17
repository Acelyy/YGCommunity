package com.yonggang.ygcommunity.Activity.Personal;

import android.content.Intent;
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
import com.yonggang.ygcommunity.Activity.BbsPicActivity;
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

public class GiftActivity extends BaseActivity {

    @BindView(R.id.list_gift)
    PullToRefreshListView listGift;

    private List<Gift.GiftBean> list_gift;

    private int total;

    private GiftAdapter adapter;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        getGift(1);
        // 上拉加载更多，分页加载
        listGift.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listGift.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listGift.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listGift.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listGift.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listGift.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        listGift.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getGift(1);
            }
        });
        listGift.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGift(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getGift(adapter.getCount() / 10 + 1);
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    /**
     * 获取积分兑换的礼品
     */
    private void getGift(final int page) {
        Subscriber subscriber = new Subscriber<Gift>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                Toast.makeText(GiftActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                listGift.onRefreshComplete();
            }

            @Override
            public void onNext(Gift data) {
                Log.i("getGift", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_gift = data.getGift();
                    adapter = new GiftAdapter();
                    listGift.setAdapter(adapter);
                } else {
                    list_gift.addAll(data.getGift());
                    adapter.notifyDataSetChanged();
                }
                listGift.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listGift.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listGift.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().getGift(subscriber, page);
    }

    class GiftAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_gift.size();
        }

        @Override
        public Object getItem(int position) {
            return list_gift.get(position);
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
                convertView = LayoutInflater.from(GiftActivity.this).inflate(R.layout.item_gift, null);
                holder.gift_img = (ImageView) convertView.findViewById(R.id.gift_img);
                holder.gift_name = (TextView) convertView.findViewById(R.id.gift_name);
                holder.gift_points = (TextView) convertView.findViewById(R.id.gift_points);
                holder.gift_price = (TextView) convertView.findViewById(R.id.gift_price);
                holder.gift_buy = (ImageView) convertView.findViewById(R.id.gift_buy);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.gift_name.setText(list_gift.get(position).getGift());
            holder.gift_points.setText(list_gift.get(position).getScore() + "分");
            holder.gift_price.setText("市场价：" + list_gift.get(position).getPrice());
            Glide.with(app)
                    .load(list_gift.get(position).getGift_img())
                    .error(R.mipmap.pic_loading_error)
                    .into(holder.gift_img);
            holder.gift_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(GiftActivity.this);
//                    builder.setTitle("确定花费" + list_gift.get(position).getScore() + "积分兑换该商品？")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    exchange_gift(list_gift.get(position).getGift_id());
//                                }
//                            })
//                            .setNegativeButton("取消", null)
//                            .create().show();
                    Intent intent = new Intent(GiftActivity.this, AddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("gift",list_gift.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.gift_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("index", 0);
                    ArrayList<String> list_url = new ArrayList<String>();
                    list_url.add(list_gift.get(position).getGift_img());
                    bundle.putStringArrayList("imgs", list_url);
                    stepActivity(bundle, BbsPicActivity.class);
                }
            });
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            ImageView gift_img;
            TextView gift_name;
            TextView gift_points;
            TextView gift_price;
            ImageView gift_buy;
        }
    }

}
