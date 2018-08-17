package com.yonggang.ygcommunity.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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

public class BbsCommentsActivity extends AppCompatActivity implements AbsListView.OnScrollListener, OnFocusChangeListener {

    @BindView(R.id.list_comments)
    PullToRefreshListView listComments;
    @BindView(R.id.input_comment)
    EditText inputComment;
    @BindView(R.id.txt_send)
    TextView txtSend;

    private List<BbsUser.UserAnswersBean> list_user;

    private UserAdapter adapter;

    private int total;

    private String nid;

    private YGApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_comments);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        nid = getIntent().getExtras().getString("nid");
        get_user_answers(nid, 1);
        listComments.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                get_user_answers(nid, 1);
            }
        });
        inputComment.setOnFocusChangeListener(this);
        inputComment.addTextChangedListener(new CommentChangedListener());
    }

    @OnClick({R.id.img_finish, R.id.txt_send})
    public void onViewClicked(View view) {
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

    /**
     * @param nid
     * @param page
     */
    private void get_user_answers(String nid, final int page) {
        Subscriber subscriber = new Subscriber<BbsUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(BbsCommentsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(BbsCommentsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BbsCommentsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listComments.onRefreshComplete();
            }

            @Override
            public void onNext(BbsUser bbsUser) {
                Log.i("list_user", bbsUser.toString());
                total = bbsUser.getTotal();
                if (page == 1) {
                    list_user = bbsUser.getUser_answers();
                    adapter = new UserAdapter();
                    listComments.setAdapter(adapter);
                    listComments.setOnScrollListener(BbsCommentsActivity.this);
                } else {
                    list_user.addAll(bbsUser.getUser_answers());
                    adapter.notifyDataSetChanged();
                }
                listComments.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().get_user_answers(subscriber, nid, page);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (list_user.size() < total) {
                get_user_answers(nid, list_user.size() / 10 + 1);
            }
        }
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

    class UserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_user.size();
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
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BbsCommentsActivity.this).inflate(R.layout.item_bbs_comment, null);
                holder.head = (RoundAngleImageView) convertView.findViewById(R.id.head);
                holder.comments_author = (TextView) convertView.findViewById(R.id.comments_author);
                holder.comments_time = (TextView) convertView.findViewById(R.id.comments_time);
                holder.comments_content = (TextView) convertView.findViewById(R.id.comments_content);
                holder.list_child = (LinearLayoutForListView) convertView.findViewById(R.id.list_child);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.comments_author.setText(list_user.get(position).getAnswers_author());
            holder.comments_time.setText(list_user.get(position).getAnswers_time());
            holder.comments_content.setText(list_user.get(position).getUser_answers());
            Glide.with(BbsCommentsActivity.this)
                    .load(list_user.get(position).getFace_url())
                    .error(R.mipmap.pic_loading_error)
                    .into(holder.head);
            if (list_user.get(position).getChildren() != null) {
                holder.list_child.setAdapter(new ChildAdapter(list_user.get(position).getChildren()));
            } else {
                holder.list_child.setAdapter(null);
            }
            return convertView;
        }

        class ViewHolder {
            RoundAngleImageView head;
            TextView comments_author;
            TextView comments_time;
            TextView comments_content;
            LinearLayoutForListView list_child;
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
            View view = LayoutInflater.from(BbsCommentsActivity.this).inflate(R.layout.item_answer_child, parent, false);
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
                Toast.makeText(BbsCommentsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().set_bbs_answer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), app.getUser().getUser_id(), nid, user_answer);
    }
}
