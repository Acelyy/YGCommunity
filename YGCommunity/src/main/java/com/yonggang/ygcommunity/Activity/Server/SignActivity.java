package com.yonggang.ygcommunity.Activity.Server;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_tel)
    EditText editTel;
    @BindView(R.id.edit_addr)
    EditText editAddr;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
    }

    @OnClick({R.id.img_finish, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                add_contact(editName.getText().toString().trim(), editTel.getText().toString().trim(), editAddr.getText().toString().trim());
                break;
        }
    }

    /**
     * 报名
     *
     * @param name
     * @param phone
     * @param addr
     */
    private void add_contact(String name, String phone, String addr) {
        if ("".equals(name)) {
            Toast.makeText(app, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(phone)) {
            Toast.makeText(app, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(app, "手机号码应为11位", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(addr)) {
            Toast.makeText(app, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Log.i("add_contact", s);
                setResult(0x321);
                finish();
            }
        };
        HttpUtil.getInstance().add_contact(new ProgressSubscriber<String>(onNextListener, this, "报名中"), app.getUser().getUser_id(), name, phone, addr);
    }
}
