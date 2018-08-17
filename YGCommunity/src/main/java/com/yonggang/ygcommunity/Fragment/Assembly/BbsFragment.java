package com.yonggang.ygcommunity.Fragment.Assembly;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.liyangyang.lazyviewpagerlibrary.LazyFragmentPagerAdapter;
import com.yonggang.ygcommunity.Activity.BbsDetailActivity;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.RoundAngleImageView;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

import static com.yonggang.ygcommunity.R.id.bbs_head;

/**
 * Created by liyangyang on 2017/5/27.
 */

public class BbsFragment extends Fragment implements LazyFragmentPagerAdapter.Laziable {
    @BindView(R.id.list_content)
    PullToRefreshListView listContent;

    private int type;

    private int total;

    private List<Bbs.BbsBean> list_bbsBean = new ArrayList<>();

    private BBSAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bbs, container, false);
        ButterKnife.bind(this, view);
        getBbs(1);
        // 上拉加载更多，分页加载
        listContent.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        listContent.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        listContent.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        listContent.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        listContent.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        listContent.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");

        listContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), BbsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bbs", list_bbsBean.get(position - 1));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        listContent.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getBbs(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    getBbs(adapter.getCount() / 10 + 1);
                }
            }
        });
        return view;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取bbs新闻
     *
     * @param page
     */
    private void getBbs(final int page) {
        Subscriber subscriber = new Subscriber<Bbs>() {
            @Override
            public void onCompleted() {
                listContent.onRefreshComplete();
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(getActivity(), "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listContent.onRefreshComplete();
            }

            @Override
            public void onNext(Bbs bbs) {
                Log.i("s", bbs.toString());
                total = bbs.getTotal();
                if (page == 1) {
                    list_bbsBean.clear();
                    list_bbsBean = bbs.getBbs();
                    adapter = new BBSAdapter();
                    listContent.setAdapter(adapter);
                } else {
                    list_bbsBean.addAll(bbs.getBbs());
                    adapter.notifyDataSetChanged();
                }
                listContent.onRefreshComplete();
                if (adapter.getCount() < total) {
                    listContent.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    listContent.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().getBbs(subscriber, type+1, page);
    }

    class BBSAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_bbsBean.size();
        }

        @Override
        public Object getItem(int position) {
            return list_bbsBean.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_bbs, null);
                holder.bbs_head = (RoundAngleImageView) convertView.findViewById(bbs_head);
                holder.bbs_name = (TextView) convertView.findViewById(R.id.bbs_name);
                holder.bbs_time = (TextView) convertView.findViewById(R.id.bbs_time);
                holder.bbs_title = (TextView) convertView.findViewById(R.id.bbs_title);
                holder.bbs_count = (TextView) convertView.findViewById(R.id.bbs_count);
                holder.bbs_pic = (ImageView) convertView.findViewById(R.id.bbs_pic);
                holder.bbs_pic2 = (ImageView) convertView.findViewById(R.id.bbs_pic2);
                holder.bbs_pic3 = (ImageView) convertView.findViewById(R.id.bbs_pic3);
                holder.bbs_location = (TextView) convertView.findViewById(R.id.bbs_location);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(BbsFragment.this)
                    .load(list_bbsBean.get(position).getFace_url())
                    .dontAnimate()
                    .into(holder.bbs_head);
            holder.bbs_name.setText(list_bbsBean.get(position).getBbs_author());
            holder.bbs_time.setText(list_bbsBean.get(position).getBbstime());
            holder.bbs_title.setText(list_bbsBean.get(position).getBbstitle());
            holder.bbs_count.setText(list_bbsBean.get(position).getAnswers_count());
            holder.bbs_location.setText(list_bbsBean.get(position).getPosition());
            int size = list_bbsBean.get(position).getBbsimg().size();
            if (size == 1) {
                Glide.with(BbsFragment.this)
                        .load(list_bbsBean.get(position).getBbsimg().get(0))
                        .error(R.mipmap.pic_loading_error)
                        .dontAnimate()
                        .into(holder.bbs_pic);
                holder.bbs_pic.setVisibility(View.VISIBLE);
                holder.bbs_pic2.setVisibility(View.INVISIBLE);
                holder.bbs_pic3.setVisibility(View.INVISIBLE);
            } else if (size == 2) {
                Glide.with(BbsFragment.this)
                        .load(list_bbsBean.get(position).getBbsimg().get(0))
                        .error(R.mipmap.pic_loading_error)
                        .dontAnimate()
                        .into(holder.bbs_pic);
                Glide.with(BbsFragment.this)
                        .load(list_bbsBean.get(position).getBbsimg().get(1))
                        .error(R.mipmap.pic_loading_error)
                        .dontAnimate()
                        .into(holder.bbs_pic2);
                holder.bbs_pic.setVisibility(View.VISIBLE);
                holder.bbs_pic2.setVisibility(View.VISIBLE);
                holder.bbs_pic3.setVisibility(View.INVISIBLE);
            } else {
                Glide.with(BbsFragment.this)
                        .load(list_bbsBean.get(position).getBbsimg().get(0))
                        .error(R.mipmap.pic_loading_error)
                        .dontAnimate()
                        .into(holder.bbs_pic);
                Glide.with(BbsFragment.this)
                        .load(list_bbsBean.get(position).getBbsimg().get(1))
                        .error(R.mipmap.pic_loading_error)
                        .dontAnimate()
                        .into(holder.bbs_pic2);
                Glide.with(BbsFragment.this)
                        .load(list_bbsBean.get(position).getBbsimg().get(2))
                        .error(R.mipmap.pic_loading_error)
                        .dontAnimate()
                        .into(holder.bbs_pic3);
                holder.bbs_pic.setVisibility(View.VISIBLE);
                holder.bbs_pic2.setVisibility(View.VISIBLE);
                holder.bbs_pic3.setVisibility(View.VISIBLE);
            }
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            RoundAngleImageView bbs_head;
            TextView bbs_name;
            TextView bbs_time;
            TextView bbs_title;
            TextView bbs_count;
            ImageView bbs_pic;
            ImageView bbs_pic2;
            ImageView bbs_pic3;
            TextView bbs_location;
        }
    }

}
