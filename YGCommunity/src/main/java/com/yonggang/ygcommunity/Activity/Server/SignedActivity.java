package com.yonggang.ygcommunity.Activity.Server;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.SignedPerson;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

import static com.yonggang.ygcommunity.R.id.is_checked;

public class SignedActivity extends BaseActivity {

    @BindView(R.id.list_signed)
    PullToRefreshListView listSigned;
    private YGApplication app;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signed);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        id = getIntent().getExtras().getString("id");
        listSigned.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                sign_person(id);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sign_person(id);
    }

    /**
     * 获取已报名人员
     *
     * @param id
     */
    private void sign_person(String id) {
        Subscriber subscriber = new Subscriber<List<SignedPerson>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(SignedActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(SignedActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof UnknownHostException) {
                    Toast.makeText(SignedActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignedActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listSigned.onRefreshComplete();
            }

            @Override
            public void onNext(List<SignedPerson> data) {
                Log.i("sign_person", data.toString());
                listSigned.setAdapter(new PersonAdapter(data));
                listSigned.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().signs_person(subscriber, app.getUser().getUser_id(), id);
    }

    /**
     * 取消报名
     *
     * @param simple_id
     */
    private void cancel_verify(String simple_id) {
        SubscriberOnNextListener subscriberOnNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("cancel_verify", data);
                sign_person(id);
            }
        };
        HttpUtil.getInstance().cancel_verify(new ProgressSubscriber<String>(subscriberOnNextListener, this, "加载中"), id, app.getUser().getUser_id(), simple_id);

    }

    @OnClick({R.id.img_finish, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                stepActivity(bundle,ContactsActivity.class);
                break;
        }
    }

    class PersonAdapter extends BaseAdapter {

        private List<SignedPerson> data;

        public PersonAdapter(List<SignedPerson> data) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(SignedActivity.this).inflate(R.layout.item_person, null);
                holder.person_info = (TextView) convertView.findViewById(R.id.person_info);
                holder.person_addr = (TextView) convertView.findViewById(R.id.person_addr);
                holder.person_delete = (LinearLayout) convertView.findViewById(R.id.person_delete);
                holder.is_checked = (ImageView) convertView.findViewById(is_checked);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.person_info.setText(data.get(position).getName() + "-" + data.get(position).getPhone());
            holder.person_addr.setText(data.get(position).getAddress());
            holder.person_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancel_verify(data.get(position).getSimple_id());
                }
            });
            if (data.get(position).getChecked() == 1) {
                holder.person_delete.setVisibility(View.GONE);
                holder.is_checked.setVisibility(View.VISIBLE);
            } else {
                holder.person_delete.setVisibility(View.VISIBLE);
                holder.is_checked.setVisibility(View.GONE);
            }
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView person_info;
            TextView person_addr;
            LinearLayout person_delete;
            ImageView is_checked;
        }
    }

}
