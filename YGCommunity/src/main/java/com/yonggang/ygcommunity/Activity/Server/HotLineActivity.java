package com.yonggang.ygcommunity.Activity.Server;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.HotLine;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class HotLineActivity extends BaseActivity {

    @BindView(R.id.list_line)
    PullToRefreshListView listLine;

    private List<HotLine.PhonelistBean> list_phone = new ArrayList<>();

    private int total;

    private HotLineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_line);
        ButterKnife.bind(this);
        getLines();
        listLine.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getLines();
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    private void getLines() {
        Subscriber subscriber = new Subscriber<HotLine>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(HotLineActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(HotLineActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HotLineActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listLine.onRefreshComplete();
            }

            @Override
            public void onNext(HotLine hotLine) {
                Log.i("line", hotLine.toString());
                total = hotLine.getTotal();
                list_phone = hotLine.getPhonelist();
                adapter = new HotLineAdapter(hotLine.getPhonelist());
                listLine.setAdapter(adapter);
                listLine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(HotLineActivity.this);
                        builder.setTitle("是否呼叫" + list_phone.get(position - 1).getPhone() + "？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        call(list_phone.get(position - 1).getPhone());
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create().show();
                    }
                });
                listLine.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().getLines(subscriber);
    }

    private void call(String number) {
        //用intent启动拨打电话
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public class HotLineAdapter extends BaseAdapter {

        private List<HotLine.PhonelistBean> data;

        public HotLineAdapter(List<HotLine.PhonelistBean> data) {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(HotLineActivity.this).inflate(R.layout.item_line, null);
                holder.line_name = (TextView) convertView.findViewById(R.id.line_name);
                holder.line_tel = (TextView) convertView.findViewById(R.id.line_tel);
                holder.line_des = (TextView) convertView.findViewById(R.id.line_des);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.line_name.setText(data.get(position).getName());
            holder.line_tel.setText(data.get(position).getPhone());
            holder.line_des.setText(data.get(position).getMarks());
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView line_name;
            TextView line_tel;
            TextView line_des;
        }
    }
}
