package com.yonggang.ygcommunity.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.HttpResult;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.MD5;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPassActivity extends BaseActivity {

    @BindView(R.id.edit_pass)
    EditText editPass;
    @BindView(R.id.edit_identify)
    EditText editIdentify;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        ButterKnife.bind(this);
        phone = getIntent().getExtras().getString("phone");
    }

    @OnClick({R.id.img_finish, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_next:
                getBack_pwd(phone, editPass.getText().toString(), editIdentify.getText().toString());
                break;
        }
    }

    /**
     * 找回密码
     *
     * @param phone
     * @param new_pwd
     */
    private void getBack_pwd(String phone, String new_pwd, String pwd_identify) {

        if (new_pwd.length() < 6 || new_pwd.length() > 20) {
            Toast.makeText(this, "密码应为6~20位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new_pwd.equals(pwd_identify)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                goActivity(LoginActivity.class);
            }
        };
        HttpUtil.getInstance().getBack_pwd(new ProgressSubscriber<HttpResult<String>>(onNextListener, this, "修改中"), phone, MD5.GetMD5Code(new_pwd));
    }
}
