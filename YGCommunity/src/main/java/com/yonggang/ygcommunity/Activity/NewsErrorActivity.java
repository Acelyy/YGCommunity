package com.yonggang.ygcommunity.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsErrorActivity extends AppCompatActivity {

    @BindView(R.id.activity_head)
    RelativeLayout activityHead;
    @BindView(R.id.reason0)
    CheckBox reason0;
    @BindView(R.id.reason1)
    CheckBox reason1;
    @BindView(R.id.reason2)
    CheckBox reason2;
    @BindView(R.id.reason3)
    CheckBox reason3;
    @BindView(R.id.reason4)
    CheckBox reason4;
    @BindView(R.id.reason5)
    CheckBox reason5;
    @BindView(R.id.reason6)
    CheckBox reason6;
    @BindView(R.id.edit_error)
    EditText editError;

    Map<Integer, String> list_reasons = new HashMap<>();

    private YGApplication app;

    private String news_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_error);
        app = (YGApplication) getApplication();
        news_id = getIntent().getExtras().getString("news_id");
        ButterKnife.bind(this);
        reason0.setOnCheckedChangeListener(new ReasonListener(0));
        reason1.setOnCheckedChangeListener(new ReasonListener(1));
        reason2.setOnCheckedChangeListener(new ReasonListener(2));
        reason3.setOnCheckedChangeListener(new ReasonListener(3));
        reason4.setOnCheckedChangeListener(new ReasonListener(4));
        reason5.setOnCheckedChangeListener(new ReasonListener(5));
        reason6.setOnCheckedChangeListener(new ReasonListener(6));
    }

    @OnClick({R.id.img_finish, R.id.btn_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.btn_complete:
                submitError(news_id, list_reasons, editError.getText().toString());
                break;
        }
    }

    class ReasonListener implements OnCheckedChangeListener {
        private int index;

        public ReasonListener(int index) {
            this.index = index;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                list_reasons.put(index, buttonView.getText().toString());
            } else {
                list_reasons.remove(index);
            }
        }
    }

    /**
     * @param news_id
     * @param error_list
     * @param zdy_errors
     */
    private void submitError(String news_id, Map<Integer, String> error_list, String zdy_errors) {
        Log.i("reason123", JSON.toJSONString(MapToList(error_list)));
        if (error_list.isEmpty()) {
            Toast.makeText(app, "请至少选择一项原因", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Toast.makeText(app, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        HttpUtil.getInstance().getError(new ProgressSubscriber<String>(onNextListener, this, "提交中"), news_id, app.getUser().getUser_id(), JSON.toJSONString(MapToList(error_list)), zdy_errors);
    }

    /**
     * Map转List
     *
     * @param map
     * @return
     */
    private List<String> MapToList(Map<Integer, String> map) {
        List<String> list = new ArrayList<>();
        Iterator it = map.keySet().iterator();
        while (it.hasNext()) {
            int key = (int) it.next();
            list.add(map.get(key));
        }
        return list;
    }

}
