package com.yonggang.ygcommunity.Activity.Server;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Contacts;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class ContactsActivity extends BaseActivity {

    @BindView(R.id.list_contacts)
    PullToRefreshListView listContacts;
    private YGApplication app;

    private String id;

    private Map<Integer, String> ids = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        id = getIntent().getExtras().getString("id");
        getContacts();
        listContacts.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getContacts();
            }
        });
    }

    /**
     * 获取联系人
     */
    private void getContacts() {
        Subscriber subscriber = new Subscriber<List<Contacts>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(ContactsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(ContactsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof UnknownHostException) {
                    Toast.makeText(ContactsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ContactsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listContacts.onRefreshComplete();
            }

            @Override
            public void onNext(List<Contacts> data) {
                Log.i("getContacts", data.toString());
                listContacts.setAdapter(new ContactsAdapter(data));
                listContacts.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().contact_list(subscriber, app.getUser().getUser_id());
    }

    @OnClick({R.id.img_finish, R.id.add, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.add:
                Intent intent = new Intent(this, SignActivity.class);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.btn_complete:
                sign();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getContacts();
    }

    /**
     * 联系人适配器
     */
    class ContactsAdapter extends BaseAdapter {

        private List<Contacts> data;

        public ContactsAdapter(List<Contacts> data) {
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
                convertView = LayoutInflater.from(ContactsActivity.this).inflate(R.layout.item_contacts, null);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.tel = (TextView) convertView.findViewById(R.id.tel);
                holder.addr = (TextView) convertView.findViewById(R.id.addr);
                holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
                holder.check = (CheckBox) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(data.get(position).getName());
            holder.tel.setText(data.get(position).getPhone());
            holder.addr.setText(data.get(position).getAddress());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除联系人
                    del_contact(data.get(position).getId());
                }
            });
            holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        ids.put(position, data.get(position).getId());
                    } else {
                        ids.remove(position);
                    }
                }
            });
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView tel;
            TextView addr;
            LinearLayout delete;
            CheckBox check;
        }
    }

    /**
     * 报名
     */
    private void sign() {
        if (ids.isEmpty()){
            Toast.makeText(app, "请至少选择一位联系人", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Toast.makeText(app, data, Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        HttpUtil.getInstance().signs(new ProgressSubscriber<String>(onNextListener, this, "报名中"), app.getUser().getUser_id(), id, JSON.toJSONString(MapToList(ids)));
    }

    /**
     * 删除联系人
     */
    private void del_contact(String simple_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                //Toast.makeText(app, data, Toast.LENGTH_SHORT).show();
                getContacts();
            }
        };
        HttpUtil.getInstance().del_contact(new ProgressSubscriber<String>(onNextListener, this, "删除中"), simple_id);
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
