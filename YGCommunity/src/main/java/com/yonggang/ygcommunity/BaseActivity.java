package com.yonggang.ygcommunity;

import android.content.Intent;
import android.os.Bundle;

import com.zhy.autolayout.AutoLayoutActivity;

/**
 * Created by liyangyang on 2017/3/2.
 */

public class BaseActivity extends AutoLayoutActivity {

    protected void goActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        this.finish();
    }

    protected void goActivity(Bundle bundle, Class clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
        this.finish();
    }

    protected void stepActivity(Class clz) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    protected void stepActivity(Bundle bundle, Class clz) {
        Intent intent = new Intent(this, clz);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
