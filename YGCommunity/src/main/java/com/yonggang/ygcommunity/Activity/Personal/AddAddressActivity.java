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

public class AddAddressActivity extends BaseActivity {

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
        setContentView(R.layout.activity_add_address);
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
                addAddress(editName.getText().toString(), editTel.getText().toString(), editAddr.getText().toString());
                break;
        }
    }

    /**
     * 添加联系人
     *
     * @param name
     * @param tel
     * @param address
     */
    private void addAddress(String name, String tel, String address) {
        if ("".equals(name)) {
            Toast.makeText(AddAddressActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tel.length() != 11) {
            Toast.makeText(AddAddressActivity.this, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(address)) {
            Toast.makeText(AddAddressActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("addAddress", data);
                finish();
            }
        };

        HttpUtil.getInstance().addAddress(new ProgressSubscriber(onNextListener, this, "保存中"), app.getUser().getUser_id(), name, tel, address);

    }
}
