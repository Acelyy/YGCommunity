package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.MD5;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassActivity extends BaseActivity {

    @BindView(R.id.pass_old)
    EditText passOld;
    @BindView(R.id.pass_new)
    EditText passNew;
    @BindView(R.id.pass_sure)
    EditText passSure;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
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
                modify_pwd(passOld.getText().toString(), passNew.getText().toString(), passSure.getText().toString());
                break;
        }
    }

    /**
     * 修改密码
     */
    private void modify_pwd(String old_pwd, String new_pwd, String pwd_sure) {
        if (old_pwd.length() < 6 || old_pwd.length() > 20) {
            Toast.makeText(app, "原密码为6~20位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (new_pwd.length() < 6 || new_pwd.length() > 20) {
            Toast.makeText(app, "新密码为6~20位", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!new_pwd.equals(pwd_sure)) {
            Toast.makeText(app, "两次密码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                setResult(0x321);
                finish();
            }
        };
        HttpUtil.getInstance().modify_pwd(new ProgressSubscriber<String>(onNextListener, this, "修改中"), app.getUser().getUser_id(), MD5.GetMD5Code(old_pwd), MD5.GetMD5Code(new_pwd));
    }
}
