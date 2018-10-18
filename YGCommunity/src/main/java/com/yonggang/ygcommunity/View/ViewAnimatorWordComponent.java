package com.yonggang.ygcommunity.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.yonggang.ygcommunity.R;

import java.util.ArrayList;

public class ViewAnimatorWordComponent extends RelativeLayout {
    private ViewAnimator viewAnimator;
    private final int MSG_CODE = 0x667;
    private final int TIMER_INTERVAL = 3000;
    private ArrayList<String> strings;

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
        if (strings != null) {
            for (int i = 0; i < strings.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(strings.get(i));
                //任意设置你的文字样式，在这里
                textView.setTextColor(getResources().getColor(android.R.color.white));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                viewAnimator.addView(textView, i);
            }
        }
    }

    public ViewAnimatorWordComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        viewAnimator = new ViewAnimator(getContext());
        viewAnimator.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(viewAnimator);
        Message message = handler.obtainMessage(MSG_CODE);
        handler.sendMessageDelayed(message, TIMER_INTERVAL);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_CODE) {
                viewAnimator.setOutAnimation(getContext(), R.anim.slide_out_up);
                viewAnimator.setInAnimation(getContext(), R.anim.slide_in_down);
                viewAnimator.showNext();
                Message message = handler.obtainMessage(MSG_CODE);
                handler.sendMessageDelayed(message, TIMER_INTERVAL);
            }
        }
    };
}