package com.yonggang.ygcommunity.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Comments;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.Entry.PicBean;
import com.yonggang.ygcommunity.Fragment.Main.ImageDetailFragment;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.View.HackyViewPager;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class PicActivity extends BaseActivity implements View.OnFocusChangeListener {

    @BindView(R.id.page_pic)
    HackyViewPager pagePic;

    @BindView(R.id.txt_news_title)
    TextView txtNewsTitle;
    @BindView(R.id.txt_news_index)
    TextView txtNewsIndex;
    @BindView(R.id.txt_news_content)
    TextView txtNewsContent;

    //评论发布输入框
    @BindView(R.id.input_comment)
    EditText inputComment;
    //在inputComment切换焦点所需要隐藏的组件
    @BindView(R.id.comment_num)
    TextView commentNum;//评论数量
    @BindView(R.id.img_comment)
    ImageView imgComment;
    @BindView(R.id.img_input)
    ImageView imgInput;
    @BindView(R.id.img_share)
    ImageView imgShare;

    @BindView(R.id.txt_send)
    TextView txtSend;

    @BindView(R.id.news_content)
    ScrollView newsContent;
    @BindView(R.id.news_head)
    RelativeLayout newsHead;

    private NewsItem.NewsBean newsBean;

    private List<PicBean> list_pic;

    private boolean is_show = true;

    private YGApplication app;

    private Comments comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);
        app = (YGApplication) getApplication();
        newsBean = (NewsItem.NewsBean) getIntent().getExtras().getSerializable("newsItem");
        pics_detail(newsBean.getNews_id());
        inputComment.setOnFocusChangeListener(this);
        inputComment.addTextChangedListener(new CommentChangedListener());
    }

    /**
     * 获取图片新闻的详细
     *
     * @param news_id
     */
    private void pics_detail(final String news_id) {

        Subscriber onSubscriber = new Subscriber<List<PicBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<PicBean> list_pic) {
                Log.i("s", list_pic.toString());
                if (list_pic.size() > 0) {
                    PicActivity.this.list_pic = list_pic;
                    txtNewsTitle.setText(newsBean.getNews_title());
                    txtNewsContent.setText(list_pic.get(0).getNewspic_content());
                    pagePic.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                    pagePic.addOnPageChangeListener(new MyPageChangeListener());
                    getAnswers(news_id);
                    txtNewsIndex.setText(1 + "/" + list_pic.size());
                }
            }
        };
        HttpUtil.getInstance().pics_detail(onSubscriber, news_id);
    }

    @OnClick({R.id.img_finish, R.id.txt_send, R.id.img_comment, R.id.img_share})
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
            case R.id.img_comment:
                Bundle bundle = new Bundle();
                bundle.putString("news_id", newsBean.getNews_id());
                bundle.putSerializable("comments", comments);
                stepActivity(bundle, CommentsActivity.class);
                break;
            case R.id.img_share:
                showPop_shared(list_pic.get(pagePic.getCurrentItem()).getNewspic_url());
                break;
        }
    }

    /**
     * 友盟分享分享pop
     */
    private void showPop_shared(String url) {
        UMImage image = new UMImage(PicActivity.this, url);//网络图片
        String title = newsBean.getNews_title();
        image.setTitle(title);//标题
        image.setDescription(title);//描述
        new ShareAction(PicActivity.this)
                .withMedia(image)
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
            Toast.makeText(PicActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PicActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PicActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.input_comment:
                if (hasFocus) {
                    //inputComment获得焦点，显示发送按钮，隐藏其它
                    txtSend.setVisibility(View.VISIBLE);
                    imgInput.setVisibility(View.GONE);
                    imgComment.setVisibility(View.GONE);
                    imgShare.setVisibility(View.GONE);
                    commentNum.setVisibility(View.GONE);
                } else {
                    //inputComment获得焦点，隐藏发送按钮，显示其它
                    txtSend.setVisibility(View.GONE);
                    imgInput.setVisibility(View.VISIBLE);
                    imgComment.setVisibility(View.VISIBLE);
                    imgShare.setVisibility(View.VISIBLE);
                    commentNum.setVisibility(View.VISIBLE);
                    //清除输入的字符串
                    inputComment.setText("");
                }
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
     * 相册适配器
     */
    public class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String url = list_pic.get(position).getNewspic_url();
            return ImageDetailFragment.newInstance(url, "PicActivity");
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "pic";
        }

        @Override
        public int getCount() {
            return list_pic.size();
        }

    }

    /**
     * 切换图片的监听
     */
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            txtNewsContent.setText(list_pic.get(position).getNewspic_content());
            txtNewsIndex.setText((position + 1) + "/" + list_pic.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 显示和隐藏文字内容
     */
    public void showContent() {
        if (is_show) {
            newsContent.setVisibility(View.GONE);
            newsHead.setVisibility(View.GONE);
        } else {
            newsContent.setVisibility(View.VISIBLE);
            newsHead.setVisibility(View.VISIBLE);
        }
        is_show = !is_show;

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
                getAnswers(newsBean.getNews_id());
                Toast.makeText(PicActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().sendAnswer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), newsBean.getNews_id(), app.getUser().getUser_id(), comments_content);
    }

    /**
     * 获取新闻的评论,新闻界面之取第一页的前三条
     *
     * @param news_id
     */
    private void getAnswers(final String news_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Comments>() {
            @Override
            public void onNext(final Comments comments) {
                Log.i("comments", comments.toString());
                PicActivity.this.comments = comments;
                commentNum.setText(comments.getLen() + "");
            }
        };
        HttpUtil.getInstance().getAnswers(new ProgressSubscriber<Comments>(onNextListener, this), news_id, 1);
    }
}
