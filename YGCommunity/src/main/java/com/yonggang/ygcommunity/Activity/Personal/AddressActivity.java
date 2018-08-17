package com.yonggang.ygcommunity.Activity.Personal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Address;
import com.yonggang.ygcommunity.Entry.Gift;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

import static com.yonggang.ygcommunity.R.id.btn_complete;

public class AddressActivity extends BaseActivity {

    @BindView(R.id.list_addr)
    PullToRefreshListView listAddr;

    private YGApplication app;

    private AddressAdapter adapter;

    private int index = -1;

    private Gift.GiftBean gift;

    private List<Address> list_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        gift = (Gift.GiftBean) getIntent().getExtras().getSerializable("gift");
        listAddr.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                index = -1;
                getAddress();
            }
        });
    }

    @Override
    protected void onResume() {
        getAddress();
        super.onResume();
    }

    /**
     * 获取收货地址
     */
    private void getAddress() {
        Subscriber subscriber = new Subscriber<List<Address>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
                listAddr.onRefreshComplete();
            }

            @Override
            public void onNext(List<Address> data) {
                Log.i("getAddress", data.toString());
                list_address = data;
                adapter = new AddressAdapter(data, AddressActivity.this);
                listAddr.setAdapter(adapter);
                listAddr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        index = position - 1;
                        adapter.setIndex(index);
                        adapter.notifyDataSetChanged();
                    }
                });
                listAddr.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().getAddress(subscriber, app.getUser().getUser_id());
    }

    @OnClick({R.id.img_finish, R.id.add, btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.add:
                stepActivity(AddAddressActivity.class);
                break;
            case R.id.btn_complete:
                if (index != -1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this);
                    builder.setTitle("确定兑换？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    exchange_gift(gift.getGift_id(), list_address.get(index).getRephone(), list_address.get(index).getReal_name(), list_address.get(index).getAddress());
                                }
                            }).setNegativeButton("取消", null).create().show();

                } else {
                    Toast.makeText(this, "请至少选择一个地址", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class AddressAdapter extends BaseAdapter {
        private int index = -1;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        private List<Address> data;
        private LayoutInflater inflater;

        public AddressAdapter(List<Address> data, Context context) {
            this.data = data;
            this.inflater = LayoutInflater.from(context);
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
                convertView = inflater.inflate(R.layout.item_address, null);
                holder.back = (LinearLayout) convertView.findViewById(R.id.layout_back);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.tel = (TextView) convertView.findViewById(R.id.tel);
                holder.addr = (TextView) convertView.findViewById(R.id.addr);
                holder.delete = (LinearLayout) convertView.findViewById(R.id.delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == index) {
                holder.back.setBackgroundResource(R.drawable.back_address);
            } else {
                holder.back.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            holder.name.setText(data.get(position).getReal_name());
            holder.tel.setText(data.get(position).getRephone());
            holder.addr.setText(data.get(position).getAddress());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this);
                    builder.setTitle("确定删除此地址？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delete_address(data.get(position).getConnect_id());
                                }
                            }).setNegativeButton("取消", null).create().show();

                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout back;
            TextView name;
            TextView tel;
            TextView addr;
            LinearLayout delete;
        }
    }

    /**
     * 删除地址
     *
     * @param id
     */
    private void delete_address(String id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("delete_address", data);
                getAddress();
            }
        };
        HttpUtil.getInstance().deleteAddress(new ProgressSubscriber(onNextListener, this, "删除中"), id);
    }


    /**
     * 兑换商品
     *
     * @param gift_id
     */
    private void exchange_gift(String gift_id, String rephone, String real_name, String address) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("exchange_gift", data);
                Toast.makeText(app, data, Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        HttpUtil.getInstance().exchange_gift(new ProgressSubscriber<String>(onNextListener, this, "兑换中"), app.getUser().getUser_id(), gift_id, rephone, real_name, address);
    }


}
