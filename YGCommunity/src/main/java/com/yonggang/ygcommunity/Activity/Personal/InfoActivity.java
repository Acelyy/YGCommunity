package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Info;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoActivity extends BaseActivity {

    @BindView(R.id.qr_code)
    ImageView qrCode;
    @BindView(R.id.version_name)
    TextView versionName;
    @BindView(R.id.statement)
    TextView statement;
    @BindView(R.id.copyright)
    TextView copyright;
    @BindView(R.id.tech)
    TextView tech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
        getInfo();
    }

    @OnClick({R.id.img_finish, R.id.statement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
        }
    }

    /**
     * 关于我们
     */
    private void getInfo() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Info>() {
            @Override
            public void onNext(final Info data) {
                Log.i("getInfo", data.toString());
                versionName.setText(data.getUrl());
                copyright.setText(data.getCopyright1());
                tech.setText(data.getCopyright2());
                Glide.with(getApplicationContext())
                        .load(data.getImg())
                        .into(qrCode);
                statement.setText("免责声明");
                statement.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("url", data.getMzsm());
                        stepActivity(bundle, CopyrightActivity.class);
                    }
                });
            }
        };
        HttpUtil.getInstance().getInfo(new ProgressSubscriber(onNextListener, this), 0);
    }
}
