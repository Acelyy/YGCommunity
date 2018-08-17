package com.yonggang.ygcommunity.Activity.Personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.yonggang.ygcommunity.YGApplication;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
    }

    @OnClick({R.id.img_finish, R.id.line_us, R.id.line_pass, R.id.line_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.line_us:
                stepActivity(InfoActivity.class);
                break;
            case R.id.line_pass:
                Intent intent = new Intent(this, ChangePassActivity.class);
                startActivityForResult(intent, 0x123);
                break;
            case R.id.line_exit:
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
                builder.setTitle("确认退出当前账号？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                exit();
                            }
                        }).setNegativeButton("取消", null)
                        .create().show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x123 && resultCode == 0x321) {
            exit();
        }
    }

    /**
     * 退出当前账号
     */
    private void exit() {
        app.setUser(null);
        SpUtil.saveUser(SettingActivity.this, null);
        setResult(0x321);
        finish();
    }

}
