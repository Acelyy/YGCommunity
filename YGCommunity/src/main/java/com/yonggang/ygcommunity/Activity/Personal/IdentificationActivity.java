package com.yonggang.ygcommunity.Activity.Personal;

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

public class IdentificationActivity extends BaseActivity {

    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_id)
    EditText editId;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification);
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
                authorized(editName.getText().toString().trim(), editId.getText().toString().trim());
                break;
        }
    }

    /**
     * 实名认证
     *
     * @param real_name
     * @param card_id
     */
    private void authorized(String real_name, String card_id) {
        if ("".equals(real_name)) {
            Toast.makeText(app, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(card_id)) {
            Toast.makeText(app, "身份证号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("authorized", data);
                Toast.makeText(app, data, Toast.LENGTH_SHORT).show();
                setResult(0x321);
                finish();
            }
        };
        HttpUtil.getInstance().authorized(new ProgressSubscriber<String>(onNextListener, this, "认证中"), app.getUser().getUser_id(), real_name, card_id);
    }
}
