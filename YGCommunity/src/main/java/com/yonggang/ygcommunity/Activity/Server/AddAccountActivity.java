package com.yonggang.ygcommunity.Activity.Server;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Expense;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAccountActivity extends BaseActivity {

    @BindView(R.id.img_type)
    ImageView imgType;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.txt_home)
    TextView txtHome;
    @BindView(R.id.edt_num)
    TextView edtNum;

    private Expense bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        ButterKnife.bind(this);
        bean = (Expense) getIntent().getExtras().getSerializable("account");
        //初始化界面
        switch (bean.getType()) {
            case 0:
                imgType.setImageResource(R.mipmap.server_water);
                break;
            case 1:
                imgType.setImageResource(R.mipmap.server_electric);
                break;
        }
        txtName.setText(bean.getType_name());
        txtHome.setText(bean.getTab_name());
    }

    @OnClick({R.id.img_finish, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                bind_account(edtNum.getText().toString().trim());
                break;
        }
    }

    /**
     * 绑定表号
     *
     * @param surface
     */
    private void bind_account(String surface) {
        if ("".equals(surface)) {
            Toast.makeText(this, "表号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("bind_account", data);
                Toast.makeText(AddAccountActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                Intent finish = new Intent();
                finish.setAction("finish");
                sendBroadcast(finish);
                Intent intent = new Intent(AddAccountActivity.this, ExpensesActivity.class);
                startActivity(intent);
                finish();
            }
        };
        HttpUtil.getInstance().bind_account(new ProgressSubscriber(onNextListener, this, "登记中"), bean.getId(), bean.getType(), surface);
    }
}