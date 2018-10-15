package com.yonggang.ygcommunity.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.User;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.MD5;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pass)
    EditText editPass;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
    }

    @OnClick({R.id.img_finish, R.id.btn_complete, R.id.btn_forget_pass, R.id.btn_login_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                login(editName.getText().toString(), editPass.getText().toString());
                break;
            case R.id.btn_forget_pass:
                goActivity(FindPassActivity.class);
                break;
            case R.id.btn_login_register:
                goActivity(RegisterActivity.class);
                break;
        }
    }

    /**
     * 账号登录
     *
     * @param phone
     * @param pwd
     */
    public void login(String phone, String pwd) {
        if ("".equals(phone.trim())) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pwd.length() < 6 || pwd.length() > 20) {
            Toast.makeText(this, "密码应为6~20位", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("pwd",JPushInterface.getRegistrationID(this));
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<User>() {
            @Override
            public void onNext(User user) {
                Log.i("user", JSON.toJSONString(user));
                app.setUser(user);
                SpUtil.saveUser(LoginActivity.this, user);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("login", true);
                startActivity(intent);
                finish();
            }
        };
        HttpUtil.getInstance().login(new ProgressSubscriber<User>(onNextListener, this, "登录中"), phone, MD5.GetMD5Code(pwd), JPushInterface.getRegistrationID(this));
    }

}
