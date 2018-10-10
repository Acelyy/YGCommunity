package com.yonggang.ygcommunity.Util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.alibaba.idst.nls.internal.protocol.Content;

public class FocusUtil implements View.OnFocusChangeListener {
    private Context context;

    public FocusUtil(Context context) {
        this.context = context;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            InputMethodManager imm = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
