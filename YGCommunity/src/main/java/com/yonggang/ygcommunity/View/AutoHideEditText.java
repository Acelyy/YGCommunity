package com.yonggang.ygcommunity.View;


import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.FocusUtil;

public class AutoHideEditText extends AppCompatEditText {
    public AutoHideEditText(Context context) {
        this(context,null);
    }

    public AutoHideEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public AutoHideEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setOnFocusChangeListener(new FocusUtil(context));
    }

}
