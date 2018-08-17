package com.yonggang.ygcommunity.Activity.Server;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class SearchNoticeActivity extends BaseActivity {

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.list_notice)
    ListView listNotice;

    private List<Notice.NoticeBean> list_data;

    private NoticeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_notice);
        ButterKnife.bind(this);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("search_notice", edtSearch.getText().toString().trim());
                search_notice(edtSearch.getText().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.txt_finish)
    public void onViewClicked() {
        finish();
    }

    private void search_notice(String keywords) {
        if ("".equals(keywords)) {
            return;
        }
        Subscriber subscriber = new Subscriber<Notice>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("search_notice", e.toString());
                Toast.makeText(SearchNoticeActivity.this, "网络连接异常，请检查网络设置", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Notice data) {
                Log.i("search_notice", data.toString());
                list_data = data.getNotice();
                adapter=new NoticeAdapter();
                listNotice.setAdapter(adapter);
                listNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", list_data.get(position).getJump_url());
                        stepActivity(bundle, NoticeDetailActivity.class);
                    }
                });
            }
        };
        HttpUtil.getInstance().search_notice(subscriber, keywords);
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
                convertView = LayoutInflater.from(SearchNoticeActivity.this).inflate(R.layout.item_notice, null);
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
