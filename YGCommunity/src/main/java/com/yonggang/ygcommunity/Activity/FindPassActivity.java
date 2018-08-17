package com.yonggang.ygcommunity.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.CountDown;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPassActivity extends BaseActivity {

    @BindView(R.id.edit_tel)
    EditText editTel;
    @BindView(R.id.edit_identify)
    EditText editIdentify;
    @BindView(R.id.forget_send_identify)
    TextView forgetSendIdentify;

    private int second = 60;//验证码发送间隔

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                int code = msg.getData().getInt("time");
                forgetSendIdentify.setText(code + "s");
            } else if (msg.what == 0x321) {
                forgetSendIdentify.setText("获取验证码");
                forgetSendIdentify.setClickable(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_finish, R.id.forget_send_identify, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.forget_send_identify:
                send_ums(editTel.getText().toString());
                break;
            case R.id.btn_next:
                check_code(editTel.getText().toString(), editIdentify.getText().toString());
                break;
        }
    }

    /**
     * 发送验证码
     *
     * @param phone
     */
    private void send_ums(String phone) {
        if ("".equals(phone)) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(this, "手机号码长度不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                countdown();
                Toast.makeText(FindPassActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().send_ums(new ProgressSubscriber<String>(onNextListener, this, "发送中"), phone);
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     */
    private void check_code(final String phone, String code) {
        if ("".equals(phone)) {
            Toast.makeText(this, "手机号码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            Toast.makeText(this, "手机号码长度不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(code)) {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                goActivity(bundle, NewPassActivity.class);
            }
        };
        HttpUtil.getInstance().check_code(new ProgressSubscriber<String>(onNextListener, this, "验证中"), phone, code);
    }


    /*
     * 倒计时1分钟
	 */

    private void countdown() {
        forgetSendIdentify.setClickable(false);
        CountDown cd = new CountDown(second, handler);
        cd.start();
    }
}
