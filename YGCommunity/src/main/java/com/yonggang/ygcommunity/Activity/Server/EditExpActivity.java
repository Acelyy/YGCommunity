package com.yonggang.ygcommunity.Activity.Server;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Home;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditExpActivity extends BaseActivity {

    @BindView(R.id.edt_type)
    EditText edtType;
    @BindView(R.id.edt_address)
    EditText edtAddress;

    private String id;

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exp);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        Home home = (Home) getIntent().getSerializableExtra("home");
        index = getIntent().getIntExtra("index", 0);
        edtType.setText(home.getTab_name());
        edtAddress.setText(home.getAddress());
    }

    @OnClick({R.id.img_finish, R.id.btn_complete, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                update(id, edtType.getText().toString(), edtAddress.getText().toString());
                break;
            case R.id.btn_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("删除将清除所有缴费信息")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delete(id);
                            }
                        }).create().show();
                break;
        }
    }

    /**
     * 更新家庭信息
     *
     * @param id
     * @param tab_name
     * @param address
     */
    private void update(String id, String tab_name, String address) {
        if ("".equals(tab_name)) {
            Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if ("".equals(address)) {
            Toast.makeText(this, "地址不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Toast.makeText(EditExpActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                Intent finish = new Intent();
                finish.setAction("finish");
                sendBroadcast(finish);
                Intent intent = new Intent(EditExpActivity.this, ExpensesActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
                finish();
            }
        };
        HttpUtil.getInstance().account_update(new ProgressSubscriber<String>(onNextListener, this, "保存中"), id, tab_name, address);
    }

    /**
     * 删除家庭信息
     *
     * @param id
     */
    private void delete(String id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Toast.makeText(EditExpActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                Intent finish = new Intent();
                finish.setAction("finish");
                sendBroadcast(finish);
                Intent intent = new Intent(EditExpActivity.this, ExpensesActivity.class);
                startActivity(intent);
                finish();
            }
        };
        HttpUtil.getInstance().account_delete(new ProgressSubscriber<String>(onNextListener, this, "保存中"), id);

    }

}
