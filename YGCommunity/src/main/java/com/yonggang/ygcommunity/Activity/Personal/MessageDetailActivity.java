package com.yonggang.ygcommunity.Activity.Personal;

import android.os.Bundle;
import android.widget.TextView;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Message;
import com.yonggang.ygcommunity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageDetailActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.msg_sender)
    TextView msgSender;
    @BindView(R.id.msg_time)
    TextView msgTime;
    @BindView(R.id.msg_content)
    TextView msgContent;

    private Message.MessageBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);
        bean= (Message.MessageBean) getIntent().getExtras().getSerializable("bean");
        title.setText(bean.getSender());
        msgSender.setText(bean.getSender());
        msgTime.setText(bean.getStime());
        msgContent.setText(bean.getMsg());
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }
}
