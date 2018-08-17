package com.yonggang.ygcommunity.Activity.Server;

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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.PayRecord;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class PayRecordActivity extends BaseActivity {

    @BindView(R.id.list_record)
    PullToRefreshListView listRecord;

    private List<Map<Boolean, Object>> list_record = new ArrayList<>();

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_record);
        ButterKnife.bind(this);
        app= (YGApplication) getApplication();
        pay_record();
        listRecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                pay_record();
            }
        });
    }

    private void pay_record() {
        Subscriber subscriber = new Subscriber<List<PayRecord>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(List<PayRecord> data) {
                Log.i("pay_record", data.toString());
                list_record.clear();
                for (PayRecord record : data) {
                    Map<Boolean, Object> map_title = new HashMap<>();
                    map_title.put(false, record.getStime());
                    list_record.add(map_title);
                    for (PayRecord.Record bean : record.getRecord()) {
                        Map<Boolean, Object> map_record = new HashMap<>();
                        map_record.put(true, bean);
                        list_record.add(map_record);
                    }
                }
                listRecord.setAdapter(new RecordAdapter(list_record));
                listRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map<Boolean, Object> map = list_record.get(position - 1);
                        if (map.containsKey(true)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("record", (PayRecord.Record) map.get(true));
                            stepActivity(bundle, RecordDetailActivity.class);
                        }
                    }
                });
                listRecord.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().pay_record(subscriber, app.getUser().getUser_id());
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    /**
     * 缴费记录
     */
    class RecordAdapter extends BaseAdapter {

        List<Map<Boolean, Object>> data;

        public RecordAdapter(List<Map<Boolean, Object>> data) {
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
            View view = null;
            Map<Boolean, Object> bean = data.get(position);
            if (bean.containsKey(false)) {
                String title = (String) bean.get(false);
                view = LayoutInflater.from(PayRecordActivity.this).inflate(R.layout.item_record_title, null);
                TextView record_title = (TextView) view.findViewById(R.id.record_title);
                record_title.setText(title);
            } else {
                PayRecord.Record record = (PayRecord.Record) bean.get(true);
                view = LayoutInflater.from(PayRecordActivity.this).inflate(R.layout.item_record_content, null);
                ImageView img_type = (ImageView) view.findViewById(R.id.img_type);
                TextView record_name = (TextView) view.findViewById(R.id.record_name);
                TextView record_time = (TextView) view.findViewById(R.id.record_time);
                TextView record_money = (TextView) view.findViewById(R.id.record_money);
                //初始化界面
                switch (record.getPay_type()) {
                    case 0:
                        img_type.setImageResource(R.mipmap.server_water);
                        break;
                    case 1:
                        img_type.setImageResource(R.mipmap.server_electric);
                        break;
                }
                record_name.setText(record.getTab_name());
                record_time.setText(record.getPay_time());
                record_money.setText("-" + record.getMoney());
            }
            return view;
        }
    }
}
