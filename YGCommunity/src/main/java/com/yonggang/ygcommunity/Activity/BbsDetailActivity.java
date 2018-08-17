package com.yonggang.ygcommunity.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Bbs;
import com.yonggang.ygcommunity.Entry.BbsSystem;
import com.yonggang.ygcommunity.Entry.BbsUser;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.LinearLayoutForListView;
import com.yonggang.ygcommunity.View.RoundAngleImageView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;
import com.zhy.autolayout.utils.AutoUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


public class BbsDetailActivity extends BaseActivity implements View.OnFocusChangeListener {

    @BindView(R.id.bbs_head)
    RoundAngleImageView bbsHead;
    @BindView(R.id.bbs_name)
    TextView bbsName;
    @BindView(R.id.bbs_time)
    TextView bbsTime;
    @BindView(R.id.bbs_title)
    TextView bbsTitle;
    @BindView(R.id.bbs_content)
    TextView bbsContent;
    @BindView(R.id.bbs_pic)
    ImageView bbsPic;
    @BindView(R.id.bbs_pic2)
    ImageView bbsPic2;
    @BindView(R.id.bbs_pic3)
    ImageView bbsPic3;
    @BindView(R.id.bbs_pic4)
    ImageView bbsPic4;
    @BindView(R.id.bbs_pic5)
    ImageView bbsPic5;
    @BindView(R.id.bbs_pic6)
    ImageView bbsPic6;
    @BindView(R.id.bbs_comment)
    TextView bbsComment;
    @BindView(R.id.bbs_location)
    TextView bbsLocation;
    @BindView(R.id.layout_pic)
    LinearLayout layoutPic;
    @BindView(R.id.lv_sys)
    LinearLayoutForListView lv_sys;
    @BindView(R.id.lv_user)
    LinearLayoutForListView lv_user;

    @BindView(R.id.input_comment)
    EditText inputComment;
    @BindView(R.id.txt_send)
    TextView txtSend;

    private Bbs.BbsBean bean;

    private List<BbsSystem> list_sys;

    private List<BbsUser.UserAnswersBean> list_user;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_detail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        bean = (Bbs.BbsBean) getIntent().getExtras().getSerializable("bbs");
        initDate();
        get_sys_answers(bean.getId());
        get_user_answers(bean.getId(), 1);
        lv_user.setOnItemClickListener(new LinearLayoutForListView.OnItemClickListener() {
            @Override
            public void onclick(int position) {
                Intent intent = new Intent(BbsDetailActivity.this, BbsCommentsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("nid", bean.getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        inputComment.setOnFocusChangeListener(this);
        inputComment.addTextChangedListener(new CommentChangedListener());
    }

    //赋初值
    private void initDate() {
        Glide.with(BbsDetailActivity.this)
                .load(bean.getFace_url())
                .into(bbsHead);
        bbsName.setText(bean.getBbs_author());
        bbsTitle.setText(bean.getBbstitle());
        bbsTime.setText(bean.getBbstime());
        bbsContent.setText(bean.getBbscontent());
        bbsComment.setText(bean.getAnswers_count());
        bbsLocation.setText(bean.getPosition());
        int size = bean.getBbsimg().size();
        if (size == 1) {
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(0))
                    .into(bbsPic);
            bbsPic.setVisibility(View.VISIBLE);
            bbsPic2.setVisibility(View.INVISIBLE);
            bbsPic3.setVisibility(View.INVISIBLE);
            bbsPic4.setVisibility(View.GONE);
            bbsPic5.setVisibility(View.GONE);
            bbsPic6.setVisibility(View.GONE);
            layoutPic.setVisibility(View.GONE);
        } else if (size == 2) {
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(0))
                    .into(bbsPic);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(1))
                    .into(bbsPic2);
            bbsPic.setVisibility(View.VISIBLE);
            bbsPic2.setVisibility(View.VISIBLE);
            bbsPic3.setVisibility(View.INVISIBLE);
            bbsPic4.setVisibility(View.GONE);
            bbsPic5.setVisibility(View.GONE);
            bbsPic6.setVisibility(View.GONE);
            layoutPic.setVisibility(View.GONE);
        } else if (size == 3) {
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(0))
                    .into(bbsPic);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(1))
                    .into(bbsPic2);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(2))
                    .into(bbsPic3);
            bbsPic.setVisibility(View.VISIBLE);
            bbsPic2.setVisibility(View.VISIBLE);
            bbsPic3.setVisibility(View.VISIBLE);
            bbsPic4.setVisibility(View.GONE);
            bbsPic5.setVisibility(View.GONE);
            bbsPic6.setVisibility(View.GONE);
            layoutPic.setVisibility(View.GONE);
        } else if (size == 4) {
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(0))
                    .into(bbsPic);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(1))
                    .into(bbsPic2);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(2))
                    .into(bbsPic3);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(3))
                    .into(bbsPic4);
            bbsPic.setVisibility(View.VISIBLE);
            bbsPic2.setVisibility(View.VISIBLE);
            bbsPic3.setVisibility(View.VISIBLE);
            bbsPic4.setVisibility(View.VISIBLE);
            bbsPic5.setVisibility(View.INVISIBLE);
            bbsPic6.setVisibility(View.INVISIBLE);
            layoutPic.setVisibility(View.VISIBLE);
        } else if (size == 5) {

            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(0))
                    .into(bbsPic);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(1))
                    .into(bbsPic2);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(2))
                    .into(bbsPic3);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(3))
                    .into(bbsPic4);
            Glide.with(BbsDetailActivity.this)
                    .load(bean.getBbsimg().get(4))
                    .into(bbsPic5);
            bbsPic.setVisibility(View.VISIBLE);
            bbsPic2.setVisibility(View.VISIBLE);
            bbsPic3.setVisibility(View.VISIBLE);
            bbsPic4.setVisibility(View.VISIBLE);
            bbsPic5.setVisibility(View.VISIBLE);
            bbsPic6.setVisibility(View.INVISIBLE);
            layoutPic.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.bbs_pic, R.id.bbs_pic2, R.id.bbs_pic3, R.id.bbs_pic4, R.id.bbs_pic5})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, BbsPicActivity.class);
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.bbs_pic:
                bundle.putInt("index", 0);
                break;
            case R.id.bbs_pic2:
                bundle.putInt("index", 1);
                break;
            case R.id.bbs_pic3:
                bundle.putInt("index", 2);
                break;
            case R.id.bbs_pic4:
                bundle.putInt("index", 3);
                break;
            case R.id.bbs_pic5:
                bundle.putInt("index", 4);
                break;
        }
        bundle.putStringArrayList("imgs", bean.getBbsimg());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 获取Bbs新闻的系统评论
     *
     * @param nid
     */
    private void get_sys_answers(String nid) {
        Subscriber subscriber = new Subscriber<List<BbsSystem>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(BbsDetailActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(BbsDetailActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BbsDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
            }

            @Override
            public void onNext(List<BbsSystem> list_sys) {
                Log.i("s", list_sys.toString());
                BbsDetailActivity.this.list_sys = list_sys;
                lv_sys.setAdapter(new SystemAdapter());
            }
        };
        HttpUtil.getInstance().get_sys_answers(subscriber, nid);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.input_comment:
                if (hasFocus) {
                    //inputComment获得焦点，显示发送按钮，隐藏其它
                    txtSend.setVisibility(View.VISIBLE);
                } else {
                    //inputComment获得焦点，隐藏发送按钮，显示其它
                    txtSend.setVisibility(View.GONE);
                    //清除输入的字符串
                    inputComment.setText("");
                }
                break;
        }
    }

    @OnClick({R.id.img_finish, R.id.txt_send})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.txt_send:
                sendAnswer(inputComment.getText().toString().trim());
                inputComment.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
        }
    }

    class SystemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_sys.size();
        }

        @Override
        public Object getItem(int position) {
            return list_sys.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(BbsDetailActivity.this).inflate(R.layout.item_bbs_sys, null);
            TextView sys_author = (TextView) view.findViewById(R.id.sys_author);
            TextView sys_content = (TextView) view.findViewById(R.id.sys_content);
            TextView sys_time = (TextView) view.findViewById(R.id.sys_time);
            View sys_line = view.findViewById(R.id.sys_line);
            sys_author.setText(list_sys.get(position).getAnswers_author());
            sys_content.setText(list_sys.get(position).getSys_answers());
            sys_time.setText(list_sys.get(position).getAnswers_time());
            if (position == list_sys.size() - 1) {
                sys_line.setVisibility(View.GONE);
            }
            return view;
        }
    }

    /**
     * 发送评论
     *
     * @param user_answer
     */
    private void sendAnswer(String user_answer) {
        if (user_answer.length() < 1) {
            Toast.makeText(this, "请输入评论", Toast.LENGTH_SHORT).show();
            return;
        }
        //判断登录
        if (app.getUser() == null) {
            Toast.makeText(this, "请先登录账号再进行评论", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Log.i("s", s);
                //getAnswers(newsBean.getNews_id());
                get_user_answers(bean.getId(), 1);
                Toast.makeText(BbsDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().set_bbs_answer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), app.getUser().getUser_id(), bean.getId(), user_answer);
    }

    /**
     * @param nid
     * @param page
     */
    private void get_user_answers(String nid, int page) {
        Subscriber subscriber = new Subscriber<BbsUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(BbsDetailActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(BbsDetailActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BbsDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
            }

            @Override
            public void onNext(BbsUser bbsUser) {
                list_user = bbsUser.getUser_answers();
                lv_user.setAdapter(new UserAdapter());
            }
        };
        HttpUtil.getInstance().get_user_answers(subscriber, nid, page);
    }

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int size = 0;
            if (list_user == null || list_user.size() == 0) {
                size = 0;
            } else if (list_user.size() > 3) {
                size = 4;
            } else {
                size = list_user.size() + 1;
            }
            return size;
        }

        @Override
        public Object getItem(int position) {
            return list_user.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (position > 3) {
                return null;
            } else if (position == getCount() - 1) {
                view = LayoutInflater.from(BbsDetailActivity.this).inflate(R.layout.comments_foot, null);
            } else {
                view = LayoutInflater.from(BbsDetailActivity.this).inflate(R.layout.item_bbs_comment, null);
                RoundAngleImageView head = (RoundAngleImageView) view.findViewById(R.id.head);
                TextView comments_author = (TextView) view.findViewById(R.id.comments_author);
                TextView comments_time = (TextView) view.findViewById(R.id.comments_time);
                TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
                LinearLayoutForListView list_child = (LinearLayoutForListView) view.findViewById(R.id.list_child);
                Glide.with(BbsDetailActivity.this)
                        .load(list_user.get(position).getFace_url())
                        .into(head);
                comments_author.setText(list_user.get(position).getAnswers_author());
                comments_time.setText(list_user.get(position).getAnswers_time());
                comments_content.setText(list_user.get(position).getUser_answers());
                if (list_user.get(position).getChildren() != null) {
                    list_child.setAdapter(new ChildAdapter(list_user.get(position).getChildren()));
                } else {
                    list_child.setAdapter(null);
                }
            }
            return view;
        }
    }

    class ChildAdapter extends BaseAdapter {

        private List<BbsUser.Children> data;

        public ChildAdapter(List<BbsUser.Children> data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(BbsDetailActivity.this).inflate(R.layout.item_answer_child, parent, false);
            TextView comments_index = (TextView) view.findViewById(R.id.comments_index);
            TextView comments_author = (TextView) view.findViewById(R.id.comments_author);
            TextView comments_time = (TextView) view.findViewById(R.id.comments_time);
            TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
            comments_index.setText(position + 1 + "");
            comments_author.setText(data.get(position).getAnswers_author());
            comments_time.setText(data.get(position).getAnswers_time());
            comments_content.setText(data.get(position).getUser_answers());
            AutoUtils.autoSize(view);
            return view;
        }
    }

    /**
     * 监听输入框的字符变化
     */
    class CommentChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (inputComment.getText().toString().trim().length() == 0) {
                txtSend.setClickable(false);
                txtSend.setTextColor(Color.parseColor("#999999"));
            } else {
                txtSend.setClickable(true);
                txtSend.setTextColor(Color.parseColor("#16ADFC"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
