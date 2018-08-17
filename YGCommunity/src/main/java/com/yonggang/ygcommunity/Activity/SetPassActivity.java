package com.yonggang.ygcommunity.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.MD5;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetPassActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pass)
    EditText editPass;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);
        ButterKnife.bind(this);
        phone = getIntent().getExtras().getString("phone");
    }

    @OnClick({R.id.img_finish, R.id.btn_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                register(phone, editName.getText().toString(), editPass.getText().toString());
                break;
        }
    }

    /**
     * 账号注册
     *
     * @param phone
     * @param username
     * @param pwd
     */
    private void register(String phone, String username, String pwd) {
        if ("".equals(username.trim())) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 20) {
            Toast.makeText(this, "密码应为6~20位", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Log.i("s", s);
                goActivity(LoginActivity.class);
            }
        };
        HttpUtil.getInstance().register(new ProgressSubscriber<String>(onNextListener, this, "注册中"), phone, username, MD5.GetMD5Code(pwd));
    }

}
