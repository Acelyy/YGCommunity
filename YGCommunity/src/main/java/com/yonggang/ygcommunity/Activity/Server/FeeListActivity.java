package com.yonggang.ygcommunity.Activity.Server;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yonggang.ygcommunity.Activity.PayActivity;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.Entry.Free;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class FeeListActivity extends BaseActivity {

    @BindView(R.id.img_type)
    ImageView imgType;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.txt_owner)
    TextView txtOwner;
    @BindView(R.id.txt_addr)
    TextView txtAddr;
    @BindView(R.id.txt_sum)
    TextView txtSum;
    @BindView(R.id.list_free)
    ListView listFree;
    @BindView(R.id.btn_complete)
    Button btn_complete;
    @BindView(R.id.txt_none)
    TextView txtNone;

    private Expense bean;

    private String txt_sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_list);
        ButterKnife.bind(this);
        bean = (Expense) getIntent().getExtras().getSerializable("account");
        //初始化界面
        switch (bean.getType()) {
            case 0:
                imgType.setImageResource(R.mipmap.server_water);
                break;
            case 1:
                imgType.setImageResource(R.mipmap.server_electric);
                break;
        }
        txtName.setText(bean.getTab_name());
        txtNum.setText(bean.getSurface());
    }

    @Override
    protected void onResume() {
        check_costs();
        super.onResume();
    }

    @OnClick({R.id.img_finish, R.id.txt_record, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.txt_record:
                stepActivity(PayRecordActivity.class);
                break;
            case R.id.btn_complete:
                Intent intent = new Intent(this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", bean);
                bundle.putString("sum", txt_sum);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取欠费信息
     */
    private void check_costs() {
        Subscriber subscriber = new Subscriber<Free>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(Free data) {
                Log.i("check_costs", data.toString());
                txtOwner.setText(data.getPinfos().getOwner());
                txtAddr.setText(data.getPinfos().getArea());
                if (data.getJfmsg().isEmpty()) {
                    //无欠费
                    btn_complete.setVisibility(View.GONE);
                    txtNone.setVisibility(View.VISIBLE);
                } else {
                    btn_complete.setVisibility(View.VISIBLE);
                    txtNone.setVisibility(View.GONE);
                    txtSum.setText("总金额：" + data.getTotal_price() + "元");
                    txt_sum = data.getTotal_price();
                }
                listFree.setAdapter(new FreeAdapter(data.getJfmsg()));

            }
        };
        HttpUtil.getInstance().check_costs(subscriber, bean.getId(), bean.getType());
    }

    class FreeAdapter extends BaseAdapter {

        List<Free.JfmsgBean> data;

        public FreeAdapter(List<Free.JfmsgBean> data) {
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
                convertView = LayoutInflater.from(FeeListActivity.this).inflate(R.layout.item_free, null);
                holder.free_year = (TextView) convertView.findViewById(R.id.free_year);
                holder.free_season = (TextView) convertView.findViewById(R.id.free_season);
                holder.free_sum = (TextView) convertView.findViewById(R.id.free_sum);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.free_year.setText(data.get(position).getAnnual());
            holder.free_season.setText(data.get(position).getQuarter());
            holder.free_sum.setText(data.get(position).getPrice());
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            TextView free_year;
            TextView free_season;
            TextView free_sum;
        }
    }
}
