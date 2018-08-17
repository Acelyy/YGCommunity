package com.yonggang.ygcommunity.Activity.Server;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.PayRecord;
import com.yonggang.ygcommunity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordDetailActivity extends BaseActivity {

    @BindView(R.id.record_img)
    ImageView recordImg;
    @BindView(R.id.record_money)
    TextView recordMoney;
    @BindView(R.id.record_type)
    TextView recordType;
    @BindView(R.id.record_info)
    TextView recordInfo;
    @BindView(R.id.record_account)
    TextView recordAccount;
    @BindView(R.id.record_score)
    TextView recordScore;
    @BindView(R.id.record_time)
    TextView recordTime;
    @BindView(R.id.record_num)
    TextView recordNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_detail);
        ButterKnife.bind(this);
        PayRecord.Record record = (PayRecord.Record) getIntent().getExtras().getSerializable("record");
        initView(record);
    }

    /**
     *
     */
    private void initView(PayRecord.Record record) {
        //初始化界面
        switch (record.getPay_type()) {
            case 0:
                recordImg.setImageResource(R.mipmap.server_water);
                break;
            case 1:
                recordImg.setImageResource(R.mipmap.server_electric);
                break;
        }
        recordMoney.setText("-" + record.getMoney());
        switch (record.getPay_way()) {
            case 1:
                recordType.setText("微信");
                break;
            case 2:
                recordType.setText("支付宝");
        }
        recordInfo.setText(record.getTab_name());
        recordAccount.setText(record.getSurface());
        recordScore.setText(record.getScore());
        recordTime.setText(record.getPay_time());
        recordNum.setText(record.getOrder_num());
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }
}
