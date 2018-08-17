package com.yonggang.ygcommunity.Activity.Server;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Activity;
import com.yonggang.ygcommunity.Entry.Filter;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ComActActivity extends BaseActivity {

    @BindView(R.id.gv_activity)
    PullToRefreshListView gvActivity;
    @BindView(R.id.img_down)
    ImageView imgDown;
    @BindView(R.id.layout_filter)
    LinearLayout layoutFilter;

    private Filter filer;

    private List<Activity.ActivityBean> list_activity;

    private boolean is_show;//标记筛选是否显示

    private PopupWindow window;

    private Map<Integer, Integer> status = new HashMap<>();
    private Map<Integer, Integer> area = new HashMap<>();
    private Map<Integer, Integer> type = new HashMap<>();

    private ActivityAdapter adapter;

    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_act);
        ButterKnife.bind(this);
        // 上拉加载更多，分页加载
        gvActivity.getLoadingLayoutProxy(false, true).setPullLabel("加载更多");
        gvActivity.getLoadingLayoutProxy(false, true).setRefreshingLabel("加载中...");
        gvActivity.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载");
        // 下拉刷新
        gvActivity.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新");
        gvActivity.getLoadingLayoutProxy(true, false).setRefreshingLabel("更新中...");
        gvActivity.getLoadingLayoutProxy(true, false).setReleaseLabel("松开更新");
        getActivity(1);
        getFilter();

        gvActivity.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getActivity(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (adapter.getCount() < total) {
                    getActivity(adapter.getCount() / 10 + 1);
                }
            }
        });
    }

    /**
     * 获取社区活动列表
     *
     * @param page
     */
    private void getActivity(final int page) {
        Subscriber subscriber = new Subscriber<Activity>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(ComActActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(ComActActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ComActActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                gvActivity.onRefreshComplete();
            }

            @Override
            public void onNext(Activity data) {
                Log.i("params", JSON.toJSONString(MapToList(status)));
                Log.i("activity", data.toString());
                layoutFilter.setClickable(true);
                total = data.getTotal();
                if (page == 1) {
                    list_activity = data.getActivity();
                    adapter = new ActivityAdapter(list_activity);
                    Log.i("activity", adapter.getCount() + "");
                    gvActivity.setAdapter(adapter);
                    gvActivity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(ComActActivity.this, ActDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("id", list_activity.get(position - 1).getId() + "");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    list_activity.addAll(data.getActivity());
                    adapter.notifyDataSetChanged();
                }
                gvActivity.onRefreshComplete();
                if (adapter.getCount() < total) {
                    gvActivity.setMode(PullToRefreshBase.Mode.BOTH);
                } else {
                    gvActivity.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
            }
        };
        HttpUtil.getInstance().getActivity(subscriber, page, JSON.toJSONString(MapToList(status)), JSON.toJSONString(MapToList(area)), JSON.toJSONString(MapToList(type)));
    }

    class ActivityAdapter extends BaseAdapter {

        private List<Activity.ActivityBean> data;

        public ActivityAdapter(List<Activity.ActivityBean> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_activity, null);
                holder.activity_pic = (ImageView) convertView.findViewById(R.id.activity_pic);
                holder.activity_title = (TextView) convertView.findViewById(R.id.activity_title);
                holder.activity_date = (TextView) convertView.findViewById(R.id.activity_date);
                holder.activity_area = (TextView) convertView.findViewById(R.id.activity_area);
                holder.activity_type = (TextView) convertView.findViewById(R.id.activity_type);
                holder.activity_status = (TextView) convertView.findViewById(R.id.activity_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.activity_title.setText(data.get(position).getTitle());
            holder.activity_date.setText(data.get(position).getDate_start() + "起 " + data.get(position).getDate_end() + "止");
            holder.activity_area.setText(data.get(position).getAddress());
            holder.activity_type.setText(data.get(position).getAc_type());
            String status = data.get(position).getState();
            if ("2".equals(status)) {
                holder.activity_status.setText("已结束");
                holder.activity_status.setBackgroundResource(R.drawable.back_gray);
                holder.activity_status.setTextColor(Color.parseColor("#999999"));
            } else if ("0".equals(status)) {
                holder.activity_status.setText("进行中");
                holder.activity_status.setBackgroundResource(R.drawable.back_red);
                holder.activity_status.setTextColor(Color.parseColor("#FF0000"));
            } else if ("1".equals(status)) {
                holder.activity_status.setText("筹备中");
                holder.activity_status.setBackgroundResource(R.drawable.back_blue);
                holder.activity_status.setTextColor(Color.parseColor("#16ADFC"));
            }
            Glide.with(ComActActivity.this)
                    .load(data.get(position).getImages())
                    .error(R.mipmap.pic_loading_error)
                    .centerCrop()
                    .into(holder.activity_pic);
            AutoUtils.auto(convertView);
            return convertView;
        }

        class ViewHolder {
            ImageView activity_pic;
            TextView activity_title;
            TextView activity_date;
            TextView activity_area;
            TextView activity_type;
            TextView activity_status;
        }
    }

    /**
     * 获取所有筛选条件
     */
    private void getFilter() {
        Subscriber subscriber = new Subscriber<Filter>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(ComActActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(ComActActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ComActActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
            }

            @Override
            public void onNext(Filter data) {
                Log.i("filter", data.toString());
                filer = data;//保存数据
                initPop();
            }
        };
        HttpUtil.getInstance().getFilter(subscriber);
    }

    @OnClick({R.id.img_finish, R.id.layout_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.layout_filter:
                if (is_show && window != null) {
                    imgDown.setImageResource(R.mipmap.pic_up);
                    showPop();
                } else {
                    imgDown.setImageResource(R.mipmap.pic_down);
                    window.dismiss();
                }
                is_show = !is_show;
                break;
        }
    }

    /**
     * 初始化PopupWindow
     */
    private void initPop() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_filter, null);
        GridView filter_status = (GridView) popupView.findViewById(R.id.filter_status);
        GridView filter_area = (GridView) popupView.findViewById(R.id.filter_area);
        GridView filter_type = (GridView) popupView.findViewById(R.id.filter_type);
        Button complete = (Button) popupView.findViewById(R.id.btn_complete);
        filter_status.setAdapter(new FilterAdapter<Filter.StatusBean>(filer.getStatus()));
        filter_area.setAdapter(new FilterAdapter<Filter.AreaBean>(filer.getArea()));
        filter_type.setAdapter(new FilterAdapter<Filter.TypesBean>(filer.getTypes()));
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        window = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        window.setFocusable(true);
        window.setOutsideTouchable(false);
        window.update();
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f; //0.0-1.0
                getWindow().setAttributes(lp);
                imgDown.setImageResource(R.mipmap.pic_down);
                getActivity(1);
            }
        });
    }

    /**
     * 显示筛选window
     */
    private void showPop() {
        window.showAsDropDown(findViewById(R.id.activity_head), Gravity.CENTER_HORIZONTAL, 0, 0);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    class FilterAdapter<T> extends BaseAdapter {
        private int color_unChecked = Color.parseColor("#999999");
        private int color_checked = Color.parseColor("#16ADFC");

        private List<T> data;

        public FilterAdapter(List<T> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
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
        public View getView(final int position, View convertView, final ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_filter, null);
                holder = new ViewHolder();
                holder.filter_text = (CheckBox) convertView.findViewById(R.id.filter_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (data.get(position) instanceof Filter.StatusBean) {
                final Filter.StatusBean bean = (Filter.StatusBean) data.get(position);
                holder.filter_text.setText(bean.getName());
                holder.filter_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            status.put(position, bean.getStatus());
                            holder.filter_text.setTextColor(color_checked);
                        } else {
                            status.remove(position);
                            holder.filter_text.setTextColor(color_unChecked);
                        }
                    }
                });
            } else if (data.get(position) instanceof Filter.AreaBean) {
                final Filter.AreaBean bean = (Filter.AreaBean) data.get(position);
                holder.filter_text.setText(bean.getArea());
                holder.filter_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            area.put(position, bean.getStatus());
                            holder.filter_text.setTextColor(color_checked);
                        } else {
                            area.remove(position);
                            holder.filter_text.setTextColor(color_unChecked);
                        }
                    }
                });
            } else if (data.get(position) instanceof Filter.TypesBean) {
                final Filter.TypesBean bean = (Filter.TypesBean) data.get(position);
                holder.filter_text.setText(bean.getName());
                holder.filter_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            type.put(position, bean.getStatus());
                            holder.filter_text.setTextColor(color_checked);
                        } else {
                            type.remove(position);
                            holder.filter_text.setTextColor(color_unChecked);
                        }
                    }
                });
            }
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            CheckBox filter_text;
        }

    }

    /**
     * Map转List<Key>
     *
     * @param map
     * @return
     */
    private List<Integer> MapToList(Map<Integer, Integer> map) {
        List<Integer> list = new ArrayList<>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            int key = (int) it.next();
            list.add(map.get(key));
        }
        return list;
    }

}
