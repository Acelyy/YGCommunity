package com.yonggang.ygcommunity.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.yonggang.ygcommunity.Entry.Comments;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.LinearLayoutForListView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.Subscriber;

public class VideoDetailActivity extends AppCompatActivity implements AbsListView.OnScrollListener, View.OnFocusChangeListener {

    @BindView(R.id.player)
    JCVideoPlayerStandard player;
    @BindView(R.id.list_comments)
    PullToRefreshListView listComments;
    @BindView(R.id.input_comment)
    EditText inputComment;
    @BindView(R.id.txt_send)
    TextView txtSend;

    private NewsItem.NewsBean newsBean;

    private List<Comments.CommentsBean> list_comments;

    private CommentsAdapter adapter;

    private int len;//评论条数的长度，用于分页结束判断

    private String select_id = "";

    AlertDialog dialog;

    private YGApplication app;//全局应用对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        newsBean = (NewsItem.NewsBean) getIntent().getExtras().getSerializable("newsItem");
        player.setUp(newsBean.getJump_url(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, newsBean.getNews_title());
        Glide.with(VideoDetailActivity.this)
                .load(newsBean.getTpicsurl().get(0))
                .error(R.mipmap.pic_loading_error)
                .centerCrop().into(player.thumbImageView);
        player.startButton.performClick();

        listComments.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getAnswers(newsBean.getNews_id(), 1);
            }
        });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(VideoDetailActivity.this);
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
                        Toast.makeText(VideoDetailActivity.this, "复制成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
                dialog = builder.setView(content).create();
                dialog.show();
            }
        });
        inputComment.addTextChangedListener(new CommentChangedListener());
        inputComment.setOnFocusChangeListener(this);

        getAnswers(newsBean.getNews_id(), 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            if (adapter.getCount() < len) {
                Log.i("len", len + "");
                getAnswers(newsBean.getNews_id(), list_comments.size() / 5 + 1);
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

    @OnClick(R.id.img_share)
    public void onViewClicked() {
        UMVideo web = new UMVideo(newsBean.getJump_url());
        String title = newsBean.getNews_title();
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this, newsBean.getTpicsurl().get(0)));  //缩略图
        web.setDescription(title);//描述
        new ShareAction(VideoDetailActivity.this)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.SINA)
                .setCallback(shareListener)
                .open();
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(VideoDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(VideoDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(VideoDetailActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };


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
                convertView = LayoutInflater.from(VideoDetailActivity.this).inflate(R.layout.item_comment, null);
                holder.head = (ImageView) convertView.findViewById(R.id.head);
                holder.comments_author = (TextView) convertView.findViewById(R.id.comments_author);
                holder.comments_time = (TextView) convertView.findViewById(R.id.comments_time);
                holder.comments_content = (TextView) convertView.findViewById(R.id.comments_content);
                holder.comments_child = (LinearLayoutForListView) convertView.findViewById(R.id.comments_child);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.comments_author.setText(list_comments.get(position).getComments_author());
            holder.comments_time.setText(list_comments.get(position).getComments_time());
            holder.comments_content.setText(list_comments.get(position).getComments_content());
            List<Comments.CommentsBean.ChildBean> child = list_comments.get(position).getChild();
            if (child != null) {
                holder.comments_child.setAdapter(new ChildAdapter(child));
            } else {
                holder.comments_child.setAdapter(null);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView head;
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
            View view = LayoutInflater.from(VideoDetailActivity.this).inflate(R.layout.item_comments_child, null);
            TextView comments_index = (TextView) view.findViewById(R.id.comments_index);
            TextView comments_author = (TextView) view.findViewById(R.id.comments_author);
            TextView comments_time = (TextView) view.findViewById(R.id.comments_time);
            TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
            comments_index.setText(position + 1 + "");
            comments_author.setText(data.get(position).getComments_author());
            comments_time.setText(data.get(position).getComments_time());
            comments_content.setText(data.get(position).getComments_content());
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
                    Toast.makeText(VideoDetailActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
                    Toast.makeText(VideoDetailActivity.this, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VideoDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    listComments.setOnScrollListener(VideoDetailActivity.this);
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
                Toast.makeText(VideoDetailActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().sendAnswer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), newsBean.getNews_id(), app.getUser().getUser_id(), comments_content);

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
                Toast.makeText(VideoDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().sendAnswer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), newsBean.getNews_id(), comment_id, app.getUser().getUser_id(), comments_content);
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
