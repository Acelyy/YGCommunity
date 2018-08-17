package com.yonggang.ygcommunity.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Comments;
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

import static com.yonggang.ygcommunity.R.id.head;

public class CommentsActivity extends BaseActivity implements AbsListView.OnScrollListener, OnFocusChangeListener {

    @BindView(R.id.list_comments)
    PullToRefreshListView listComments;
    @BindView(R.id.input_comment)
    EditText inputComment;
    @BindView(R.id.txt_send)
    TextView txtSend;

    private String news_id;

    private List<Comments.CommentsBean> list_comments;

    private CommentsAdapter adapter;

    private int len;//评论条数的长度，用于分页结束判断

    private String select_id = "";

    AlertDialog dialog;

    private YGApplication app;//全局应用对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        list_comments = ((Comments) getIntent().getExtras().getSerializable("comments")).getComments();
        len = (((Comments) getIntent().getExtras().getSerializable("comments")).getLen());
        news_id = getIntent().getExtras().getString("news_id");
        adapter = new CommentsAdapter();
        listComments.setAdapter(adapter);
        listComments.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getAnswers(news_id, 1);
            }
        });
        listComments.setOnScrollListener(this);
        listComments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //清除选中项
                select_id = "";
                //如果输入框为焦点状态
                if (inputComment.isFocused()) {
                    inputComment.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                //显示弹出框Dialog
                View content = getLayoutInflater().inflate(R.layout.dialog_comment, null);
                AutoUtils.autoSize(content);
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentsActivity.this);
                LinearLayout answer = (LinearLayout) content.findViewById(R.id.layout_answer);
                LinearLayout copy = (LinearLayout) content.findViewById(R.id.layout_copy);
                //回复按钮
                answer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        select_id = list_comments.get(position - 1).getComments_id();
                        inputComment.setHint("回复" + list_comments.get(position - 1).getComments_author());
                        dialog.dismiss();
                    }
                });
                //复制评论按钮
                copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setText(list_comments.get(position - 1).getComments_content());
                        Toast.makeText(CommentsActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog = builder.setView(content).create();
                dialog.show();
            }
        });
        inputComment.addTextChangedListener(new CommentChangedListener());
        inputComment.setOnFocusChangeListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (adapter.getCount() < len) {
                Log.i("len", len + "");
                getAnswers(news_id, adapter.getCount() / 5 + 1);
            }
        }
    }

    @OnClick({R.id.img_finish, R.id.txt_send, R.id.input_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.txt_send:
                if ("".equals(select_id)) {
                    sendAnswer(inputComment.getText().toString().trim());
                } else {
                    sendAnswer(inputComment.getText().toString().trim(), select_id);
                }
                break;
            case R.id.input_comment:
                if (!"".equals(select_id)) {
                    select_id = "";
                    inputComment.setHint("写跟帖");
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            txtSend.setVisibility(View.VISIBLE);
        } else {
            inputComment.setHint("写跟帖");
            txtSend.setVisibility(View.GONE);
        }
    }


    /**
     * 评论适配器
     */
    class CommentsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list_comments.size();
        }

        @Override
        public Object getItem(int position) {
            return list_comments.get(position);
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
                convertView = LayoutInflater.from(CommentsActivity.this).inflate(R.layout.item_comment, null);
                holder.head = (RoundAngleImageView) convertView.findViewById(head);
                holder.comments_author = (TextView) convertView.findViewById(R.id.comments_author);
                holder.comments_time = (TextView) convertView.findViewById(R.id.comments_time);
                holder.comments_content = (TextView) convertView.findViewById(R.id.comments_content);
                holder.comments_child = (LinearLayoutForListView) convertView.findViewById(R.id.comments_child);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Glide.with(CommentsActivity.this)
                    .load(list_comments.get(position).getFace_url())
                    .into(holder.head);
            holder.comments_author.setText(list_comments.get(position).getComments_author());
            holder.comments_time.setText(list_comments.get(position).getComments_time());
            holder.comments_content.setText(list_comments.get(position).getComments_content());
            List<Comments.CommentsBean.ChildBean> child = list_comments.get(position).getChild();
            if (child != null) {
                holder.comments_child.setAdapter(new ChildAdapter(child));
            } else {
                holder.comments_child.setAdapter(null);
            }
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        class ViewHolder {
            RoundAngleImageView head;
            TextView comments_author;
            TextView comments_time;
            TextView comments_content;
            LinearLayoutForListView comments_child;
        }
    }

    /**
     * 子评论适配器，由于用的LinearLayoutForListView，不能使用ViewHolder
     */
    class ChildAdapter extends BaseAdapter {

        private List<Comments.CommentsBean.ChildBean> data;

        public ChildAdapter(List<Comments.CommentsBean.ChildBean> data) {
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
            View view = LayoutInflater.from(CommentsActivity.this).inflate(R.layout.item_answer_child, null);
            TextView comments_index = (TextView) view.findViewById(R.id.comments_index);
            TextView comments_author = (TextView) view.findViewById(R.id.comments_author);
            TextView comments_time = (TextView) view.findViewById(R.id.comments_time);
            TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
            comments_index.setText(position + 1 + "");
            comments_author.setText(data.get(position).getComments_author());
            comments_time.setText(data.get(position).getComments_time());
            comments_content.setText(data.get(position).getComments_content());
            AutoUtils.autoSize(view);
            return view;
        }
    }

    /**
     * 获取新闻的评论
     *
     * @param news_id
     */
    private void getAnswers(String news_id, final int page) {
        Subscriber onNextListener = new Subscriber<Comments>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof SocketTimeoutException) {
                    Toast.makeText(CommentsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(CommentsActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CommentsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Log.e("error", e.toString());
                listComments.onRefreshComplete();
            }

            @Override
            public void onNext(final Comments comments) {
                Log.i("comments", comments.toString());
                if (page == 1) {
                    list_comments = comments.getComments();
                    adapter = new CommentsAdapter();
                    listComments.setAdapter(adapter);
                } else {
                    list_comments.addAll(comments.getComments());
                    adapter.notifyDataSetChanged();
                }
                listComments.onRefreshComplete();
            }
        };
        HttpUtil.getInstance().getAnswers(onNextListener, news_id, page);
    }


    /**
     * 发送评论
     *
     * @param comments_content 评论内容
     */
    private void sendAnswer(String comments_content) {
        if (comments_content.length() < 1) {
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
                inputComment.setText("");
                inputComment.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //getAnswers(news_id, 1);
                Toast.makeText(CommentsActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().sendAnswer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), news_id, app.getUser().getUser_id(), comments_content);

    }

    /**
     * 回复评论
     *
     * @param comments_content
     * @param comment_id
     */
    private void sendAnswer(String comments_content, String comment_id) {
        if (comments_content.length() < 1) {
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
                inputComment.setText("");
                inputComment.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                Toast.makeText(CommentsActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().sendAnswer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), news_id, comment_id, app.getUser().getUser_id(), comments_content);
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
