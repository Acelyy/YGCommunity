package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.ygcommunity.Activity.NewsActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Collect;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionActivity extends BaseActivity implements OnScrollListener {
    @BindView(R.id.img_finish)
    ImageView imgFinish;
    @BindView(R.id.text_back)
    TextView textBack;
    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.list_collect)
    ListView listCollect;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private YGApplication app;

    private List<Collect.CollectBean> list_data;

    private CollectAdapter adapter;

    private Map<Integer, String> ids = new HashMap<>();

    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        my_collect(1);
    }

    /**
     * 获取收藏信息
     *
     * @param page
     */
    private void my_collect(final int page) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Collect>() {
            @Override
            public void onNext(Collect data) {
                Log.i("my_collect", data.toString());
                total = data.getTotal();
                if (page == 1) {
                    list_data = data.getCollect();
                    adapter = new CollectAdapter();
                    listCollect.setAdapter(adapter);
                    listCollect.setOnScrollListener(CollectionActivity.this);
                    listCollect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            NewsItem.NewsBean bean = new NewsItem.NewsBean();
                            bean.setNews_id(list_data.get(position).getNews_id());
                            bean.setJump_url(list_data.get(position).getJump_url());
                            bean.setTpicsurl(list_data.get(position).getTpicsurl());
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("newsItem", bean);
                            stepActivity(bundle, NewsActivity.class);
                        }
                    });
                } else {
                    list_data.addAll(data.getCollect());
                    adapter.notifyDataSetChanged();
                }
            }
        };
        HttpUtil.getInstance().my_collect(new ProgressSubscriber<Collect>(onNextListener, this), app.getUser().getUser_id(), page);
    }

    @OnClick({R.id.img_finish, R.id.text_back, R.id.edit, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.text_back:
                imgFinish.setVisibility(View.VISIBLE);
                textBack.setVisibility(View.GONE);
                btnCommit.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                adapter.setCheck_mode(false);
                adapter.notifyDataSetChanged();
                break;
            case R.id.edit:
                imgFinish.setVisibility(View.GONE);
                textBack.setVisibility(View.VISIBLE);
                btnCommit.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                adapter.setCheck_mode(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_commit:
                cancel_collect(ids);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (list_data.size() < total) {
                my_collect(list_data.size() / 10 + 1);
            }
        }
    }


    class CollectAdapter extends BaseAdapter {

        public CollectAdapter() {
            check_mode = false;//默认不显示
        }

        /**
         * 判断是否显示check_box
         */
        private boolean check_mode;

        public boolean isCheck_mode() {
            return check_mode;
        }

        public void setCheck_mode(boolean check_mode) {
            this.check_mode = check_mode;
        }

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.item_collect, null);
                holder.news_title = (TextView) convertView.findViewById(R.id.news_title);
                holder.checked = (CheckBox) convertView.findViewById(R.id.checked);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (check_mode) {
                holder.checked.setVisibility(View.VISIBLE);
            } else {
                holder.checked.setVisibility(View.GONE);
            }
            holder.news_title.setText(list_data.get(position).getNewstitle());
            holder.checked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ids.put(position, list_data.get(position).getNews_id());
                    } else {
                        ids.remove(position);
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            CheckBox checked;
            TextView news_title;
        }
    }

    /**
     * 取消收藏
     */
    private void cancel_collect(Map<Integer, String> ids) {
        if (ids.isEmpty()) {
            Toast.makeText(app, "请至少选择一项", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                textBack.performClick();
                my_collect(1);
            }
        };
        HttpUtil.getInstance().cancel_collect(new ProgressSubscriber<String>(onNextListener, this, "删除中"), app.getUser().getUser_id(), JSON.toJSONString(MapToList(ids)));
    }

    /**
     * Map转List<Object>
     *
     * @param map
     * @return
     */
    private List<String> MapToList(Map<Integer, String> map) {
        List<String> list = new ArrayList<>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            int key = (int) it.next();
            list.add(map.get(key));
        }
        return list;
    }
}
