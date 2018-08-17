package com.yonggang.ygcommunity.Activity.Server;

import android.content.Intent;
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

public class AddHomeActivity extends BaseActivity {

    @BindView(R.id.edt_type)
    EditText edtType;
    @BindView(R.id.edt_address)
    EditText edtAddress;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home);
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
                account_add(edtType.getText().toString().trim(), edtAddress.getText().toString().trim());
                break;
        }
    }

    /**
     * 新增家庭信息
     *
     * @param tab_name
     * @param address
     */
    private void account_add(String tab_name, String address) {
        if ("".equals(tab_name)) {
            Toast.makeText(app, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(address)) {
            Toast.makeText(app, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Log.i("account_add", s);
                Intent finish = new Intent();
                finish.setAction("finish");
                sendBroadcast(finish);
                Intent intent = new Intent(AddHomeActivity.this, ExpensesActivity.class);
                intent.putExtra("index", 0x888);
                startActivity(intent);
                finish();
            }
        };
        HttpUtil.getInstance().account_add(new ProgressSubscriber<String>(onNextListener, this, "保存中"), app.getUser().getUser_id(), tab_name, address);
    }

}
