package com.yonggang.ygcommunity.Util;

import android.view.View;

public abstract class NoDoubleClickListener implements View.OnClickListener{
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    public abstract void onNoDoubleClick(View v);

    @Override
    public void onClick(View v) {
        long curClickTime = System.currentTimeMillis();
        if((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            // 超过点击间隔后再将lastClickTime重置为当前点击时间
            lastClickTime = curClickTime;
            onNoDoubleClick(v);
        }
    }
}
