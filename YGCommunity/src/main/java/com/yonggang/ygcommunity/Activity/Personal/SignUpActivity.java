package com.yonggang.ygcommunity.Activity.Personal;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.CalendarView.CalendarCard;
import com.yonggang.ygcommunity.CalendarView.CalendarViewAdapter;
import com.yonggang.ygcommunity.CalendarView.CustomDate;
import com.yonggang.ygcommunity.Entry.Signin;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements CalendarCard.OnCellClickListener {
    @BindView(R.id.btnPreMonth)
    TextView btnPreMonth;
    @BindView(R.id.btnNextMonth)
    TextView btnNextMonth;
    @BindView(R.id.tvCurrentMonth)
    TextView tvCurrentMonth;
    @BindView(R.id.vp_calendar)
    ViewPager vpCalendar;
    @BindView(R.id.btn_sign)
    Button btnSign;

    private View contentView;

    private CalendarViewAdapter<CalendarCard> adapter;

    private YGApplication app;

    private List<Signin> list_sign;

    private PopupWindow pop;//签到成功提示框

    private TextView score;//签到成功加积分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = LayoutInflater.from(this).inflate(R.layout.activity_sign_up, null);
        setContentView(contentView);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        btnPreMonth.setText("<");
        btnNextMonth.setText(">");
        get_today_sign();
        get_curmonth();
        initPop();
    }

    private void initPop() {
        View popupView = SignUpActivity.this.getLayoutInflater().inflate(R.layout.pop_sign, null);
        score = (TextView) popupView.findViewById(R.id.score);
        AutoUtils.autoSize(popupView);
        pop = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.update();
        popupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void clickDate(CustomDate date) {

    }

    @Override
    public void changeDate(CustomDate date) {
        tvCurrentMonth.setText(date.getYear() + "年-" + date.month + "月");
    }


    /**
     * 获取当月签到记录
     */
    private void get_curmonth() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<List<Signin>>() {
            @Override
            public void onNext(List<Signin> data) {
                Log.i("get_curmonth", data.toString());
                list_sign = data;
                CalendarCard[] views = new CalendarCard[1];
                views[0] = new CalendarCard(SignUpActivity.this, SignUpActivity.this, list_sign);
                adapter = new CalendarViewAdapter<>(views);
                vpCalendar.setAdapter(adapter);
            }
        };
        HttpUtil.getInstance().get_curmonth(new ProgressSubscriber(onNextListener, this), app.getUser().getUser_id());
    }

    /**
     * 获取当天签到状况
     */
    private void get_today_sign() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Integer>() {
            @Override
            public void onNext(Integer data) {
                Log.i("get_today_sign", data + "");
                if (data == 1) {
                    btnSign.setText("签到\n领积分");
                    btnSign.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            signin();
                        }
                    });
                } else {
                    btnSign.setText("今日\n已签到");
                    btnSign.setOnClickListener(null);
                }
            }
        };
        HttpUtil.getInstance().get_today_sign(new ProgressSubscriber(onNextListener, this), app.getUser().getUser_id());
    }

    /**
     * 获取当月签到记录
     */
    private void signin() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("signin", data);
                get_today_sign();
                score.setText(data+"分");
                pop.showAtLocation(contentView, Gravity.CENTER, 0, 0);
            }
        };
        HttpUtil.getInstance().signin(new ProgressSubscriber(onNextListener, this, "签到中"), app.getUser().getUser_id());
    }
}
