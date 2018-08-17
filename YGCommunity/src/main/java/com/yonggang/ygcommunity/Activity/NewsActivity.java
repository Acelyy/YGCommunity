package com.yonggang.ygcommunity.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Comments;
import com.yonggang.ygcommunity.Entry.NewsItem;
import com.yonggang.ygcommunity.R;
import com.yonggang.ygcommunity.Util.SpUtil;
import com.yonggang.ygcommunity.View.LinearLayoutForListView;
import com.yonggang.ygcommunity.View.RoundAngleImageView;
import com.yonggang.ygcommunity.YGApplication;
import com.yonggang.ygcommunity.httpUtil.HttpUtil;
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber;
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener;
import com.yonggang.ygcommunity.wxapi.Constants;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


public class NewsActivity extends BaseActivity implements View.OnFocusChangeListener {
    //Activity背景
    @BindView(R.id.activity_news)
    LinearLayout activity_news;
    //topBar
    @BindView(R.id.layout_title)
    RelativeLayout layout_title;
    //footer
    @BindView(R.id.layout_foot)
    LinearLayout layout_foot;

    //新闻容器
    @BindView(R.id.news_content)
    WebView newsContent;
    //评论发布输入框
    @BindView(R.id.input_comment)
    EditText inputComment;

    //滚动页面容器
    @BindView(R.id.scroll_content)
    ScrollView scroll_content;

    //评论列表
    @BindView(R.id.list_comment)
    LinearLayoutForListView listComment;
    //分享容器
    @BindView(R.id.layout_shared)
    LinearLayout layoutShared;

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
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.img_shared_zan)
    ImageView imgSharedZan;
    @BindView(R.id.txt_zan)
    TextView txtZan;


    private ImageView img_love;//是否收藏的图片
    private TextView text_love;//收藏的文本

    private TextView text_night;//夜间模式的文本

    private NewsItem.NewsBean newsBean;//新闻对象

    private YGApplication app;//全局应用对象

    private Comments comments;

    private List<Comments.CommentsBean> list_comment;

    IWXAPI api;

    private boolean is_night;

    private int is_collect;

    private static int NIGHT_TEXT = Color.parseColor("#FAFAFA");
    private static int NIGHT_BACK_TOP = Color.parseColor("#222222");
    private static int NIGHT_BACK_FOOT = Color.parseColor("#292929");

    private static int DAY_TEXT = Color.parseColor("#333333");
    private static int DAY_BACK = Color.parseColor("#FAFAFA");

    private static int DAY_LINE = Color.parseColor("#C0C2CC");
    private static int NIGHT_LINE = Color.parseColor("#2F2F2F");
    private OnClickListener popListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            window_menu.dismiss();
            switch (v.getId()) {
                case R.id.layout_love:
                    if (app.getUser() == null) {
                        Toast.makeText(NewsActivity.this, "请先登录后再收藏", Toast.LENGTH_SHORT).show();
                    } else {
                        if (is_collect == 0) {
                            setCollect(app.getUser().getUser_id(), newsBean.getNews_id(), 1);
                        } else {
                            setCollect(app.getUser().getUser_id(), newsBean.getNews_id(), 0);
                        }
                    }
                    break;
                case R.id.layout_font:
                    showFontDialog();
                    break;
                case R.id.layout_error:
                    if (app.getUser() == null) {
                        Toast.makeText(NewsActivity.this, "请先登录后再进行报错", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(NewsActivity.this, NewsErrorActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("news_id", newsBean.getNews_id());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    break;
                case R.id.layout_shared:
                    showPop_shared();
                    break;
                case R.id.layout_night:
                    is_night = !is_night;
                    setNightOrDay();
                    break;
            }
        }
    };

    private PopupWindow window_menu;

    private PopupWindow window_shared;

    private CommentsAdapter adapter;

    private int mCurrentItem = 2;//字体选中的当前项

    private int mCurrentChooseItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        IX5WebViewExtension i5 = newsContent.getX5WebViewExtension();
        Log.i("i5", i5 == null ? "null" : i5.toString());
        app = (YGApplication) getApplication();
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        api.registerApp(Constants.APP_ID);
        newsBean = (NewsItem.NewsBean) getIntent().getExtras().getSerializable("newsItem");
        WebSettings webSettings = newsContent.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        newsContent.loadUrl(newsBean.getJump_url());

        newsContent.setWebViewClient(new YgWebViewClient());
        newsContent.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(NewsActivity.this)
                        .setTitle("提示")
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirm();
                            }
                        })
                        .setCancelable(false)
                        .create().show();
                return true;
            }
        });
        newsContent.addJavascriptInterface(new JsCallJavaObj() {
            @JavascriptInterface
            @Override
            public void showBigImg(String url, String data_index) {
                Log.i("data_index", data_index + "");
                int index = Integer.parseInt(data_index);
                Intent intent = new Intent(NewsActivity.this, BbsPicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgs", newsBean.getPics_group());
                bundle.putInt("index", index - 1);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, "jsCallJavaObj");

        inputComment.setOnFocusChangeListener(this);
        inputComment.addTextChangedListener(new CommentChangedListener());
        initPop_menu();
        if (app.getUser() != null) {
            news_collect(app.getUser().getUser_id(), newsBean.getNews_id());
        }
        getZan();
        //显示分享和加载评论
        getAnswers(newsBean.getNews_id());
        addReadAccount();
    }

    /**
     * 设置网页中图片的点击事件
     *
     * @param view
     */
    private void setWebImageClick(WebView view) {
        String jsCode = "javascript:(function(){" +
                "var imgs=document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<imgs.length;i++){" +
                "imgs[i].onclick=function(){" +
                "window.jsCallJavaObj.showBigImg(this.src,this.getAttribute(\"data_index\"));" +
                "}}})()";
        view.loadUrl(jsCode);
    }

    /**
     * Js調用Java接口
     */
    private interface JsCallJavaObj {
        void showBigImg(String url, String data_index);
    }

    @OnClick({R.id.img_finish, R.id.img_menu, R.id.img_share, R.id.txt_send, R.id.img_comment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.img_menu:
                showPop_menu(view);
                break;
            case R.id.img_share:
                showPop_shared();
                break;
            case R.id.txt_send:
                sendAnswer(inputComment.getText().toString().trim());
                inputComment.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.img_comment:
                if (comments != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("news_id", newsBean.getNews_id());
                    bundle.putSerializable("comments", comments);
                    stepActivity(bundle, CommentsActivity.class);
                }
                break;
        }
    }

    /**
     * 获取新闻点赞
     *
     * @param user_id
     * @param news_id
     */
    private void news_collect(String user_id, String news_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Integer>() {
            @Override
            public void onNext(Integer s) {
                is_collect = s;
                if (s == 1) {
                    img_love.setImageResource(R.mipmap.pic_web_love2);
                }
            }
        };
        HttpUtil.getInstance().news_collect(new ProgressSubscriber<Integer>(onNextListener, this), user_id, news_id);
    }

    /**
     * 收藏新闻
     *
     * @param user_id
     * @param news_id
     */
    private void setCollect(String user_id, String news_id, final int type) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String s) {
                Log.i("s", s);
                if (type == 1) {
                    Toast.makeText(NewsActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    img_love.setImageResource(R.mipmap.pic_web_love2);
                    text_love.setText("取消收藏");
                } else {
                    Toast.makeText(NewsActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                    img_love.setImageResource(R.mipmap.pic_web_love);
                    text_love.setText("收藏");
                }
                is_collect = type;

            }
        };
        HttpUtil.getInstance().setCollect(new ProgressSubscriber<String>(onNextListener, this), user_id, news_id, type);
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
                Toast.makeText(NewsActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };
        HttpUtil.getInstance().sendAnswer(new ProgressSubscriber<String>(onNextListener, this, "发送中"), newsBean.getNews_id(), app.getUser().getUser_id(), comments_content);
    }

    @Override
    protected void onDestroy() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        newsContent.clearCache(true);
        newsContent.clearHistory();
        newsContent.loadUrl("about:blank");
        super.onDestroy();
    }

    /**
     * 增加新闻阅读量
     */
    private void addReadAccount() {
        Subscriber subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i("error", e.toString());
            }

            @Override
            public void onNext(String data) {
                Log.i("addReadAccount", data);
            }
        };
        HttpUtil.getInstance().news_pics_count(subscriber, newsBean.getNews_id());
    }

    //切换焦点时的监听
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

    @OnClick({R.id.img_shared_all, R.id.img_shared_one, R.id.img_shared_weibo, R.id.img_shared_qq})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_shared_all:
                sharedToUM(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.img_shared_one:
                sharedToUM(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.img_shared_weibo:
                sharedToUM(SHARE_MEDIA.SINA);
                break;
            case R.id.img_shared_qq:
                sharedToUM(SHARE_MEDIA.QQ);
                break;
        }
    }

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            is_night = SpUtil.getNightOrDay(NewsActivity.this);
            setNightOrDay();
            imgSharedZan.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    zan();
                }
            });
            setWebImageClick(view);
            layoutShared.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //加载开始
            layoutShared.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //加载失败
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (newsContent.canGoBack()) {
                newsContent.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
     * 获取新闻的评论,新闻界面之取第一页的前三条
     *
     * @param news_id
     */
    private void getAnswers(final String news_id) {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Comments>() {
            @Override
            public void onNext(final Comments comments) {
                layoutShared.setVisibility(View.VISIBLE);
                Log.i("comments", comments.toString());
                NewsActivity.this.comments = comments;
                list_comment = comments.getComments();
                commentNum.setText(comments.getLen() + "");
                if (comments.getLen() > 0) {
                    adapter = new CommentsAdapter();
                    listComment.setAdapter(adapter);
                }
                listComment.setOnItemClickListener(new LinearLayoutForListView.OnItemClickListener() {
                    @Override
                    public void onclick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("news_id", news_id);
                        bundle.putSerializable("comments", comments);
                        stepActivity(bundle, CommentsActivity.class);
                    }
                });
            }
        };
        HttpUtil.getInstance().getAnswers(new ProgressSubscriber<Comments>(onNextListener, this), news_id, 1);
    }

    /**
     * 评论的适配器，由于用的LinearLayoutForListView，不能使用ViewHolder
     */
    class CommentsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int size = 0;
            if (list_comment == null || list_comment.size() == 0) {
                size = 0;
            } else if (list_comment.size() > 3) {
                size = 5;
            } else {
                size = list_comment.size() + 2;
            }
            return size;
        }

        @Override
        public Object getItem(int position) {
            return comments.getComments().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (position > 4) {
                return null;
            }
            if (position == 0) {
                view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.comments_head, null);
                if (!is_night) {
                    view.setBackgroundColor(DAY_BACK);
                } else {
                    view.setBackgroundColor(NIGHT_BACK_TOP);
                }
            } else if (position == getCount() - 1) {
                view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.comments_foot, null);
                if (!is_night) {
                    view.setBackgroundColor(DAY_BACK);
                } else {
                    view.setBackgroundColor(NIGHT_BACK_TOP);
                }
            } else {
                view = LayoutInflater.from(NewsActivity.this).inflate(R.layout.item_comment, null);
                RoundAngleImageView head = (RoundAngleImageView) view.findViewById(R.id.head);
                TextView comments_author = (TextView) view.findViewById(R.id.comments_author);
                TextView comments_time = (TextView) view.findViewById(R.id.comments_time);
                TextView comments_content = (TextView) view.findViewById(R.id.comments_content);
                Glide.with(app)
                        .load(list_comment.get(position - 1).getFace_url())
                        .into(head);
                comments_author.setText(list_comment.get(position - 1).getComments_author());
                comments_time.setText(list_comment.get(position - 1).getComments_time());
                comments_content.setText(list_comment.get(position - 1).getComments_content());
                if (!is_night) {
                    view.setBackgroundColor(DAY_BACK);
                    comments_content.setTextColor(DAY_TEXT);
                } else {
                    view.setBackgroundColor(NIGHT_BACK_TOP);
                    comments_content.setTextColor(NIGHT_TEXT);
                }
            }
            return view;
        }
    }

    /**
     * 设置夜间模式中，除webView之外的其他设置
     */
    private void setNightOrDay() {
        if (!is_night) {//转为日间模式
            newsContent.loadUrl("javascript:setDay()");
            //整体背景修改
            activity_news.setBackgroundColor(DAY_BACK);
            //top和footer背景修改
            layout_title.setBackgroundColor(DAY_BACK);
            layout_foot.setBackgroundColor(DAY_BACK);
            //新增评论的字体颜色修改
            inputComment.setTextColor(DAY_TEXT);
            //分享修改背景
            layoutShared.setBackgroundColor(DAY_BACK);
            text_night.setText("夜间模式");
            line1.setBackgroundColor(DAY_LINE);
            line2.setBackgroundColor(DAY_LINE);
        } else {//改为夜间模式
            newsContent.loadUrl("javascript:setNight()");
            //整体背景修改
            activity_news.setBackgroundColor(NIGHT_BACK_TOP);
            //top和footer背景修改
            layout_title.setBackgroundColor(NIGHT_BACK_TOP);
            layout_foot.setBackgroundColor(NIGHT_BACK_FOOT);
            //新增评论的字体颜色修改
            inputComment.setTextColor(NIGHT_TEXT);
            //分享修改背景
            layoutShared.setBackgroundColor(NIGHT_BACK_TOP);
            text_night.setText("日间模式");
            line1.setBackgroundColor(NIGHT_LINE);
            line2.setBackgroundColor(NIGHT_LINE);
        }
        adapter = new CommentsAdapter();
        listComment.setAdapter(adapter);
        SpUtil.setNightOrDay(this, is_night);
    }

    private void showFontDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setTitle("设置字体");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = newsContent.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentItem = mCurrentChooseItem;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }


    /**
     * 微信分享
     *
     * @param where
     */
    private void sharedToUM(SHARE_MEDIA where) {
        UMWeb web = new UMWeb(newsBean.getJump_url() + "?is_show=true");
        String title = newsBean.getNews_title();
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this, newsBean.getTpicsurl().get(0)));  //缩略图
        web.setDescription(title);//描述
        new ShareAction(NewsActivity.this)
                .withMedia(web)
                .setPlatform(where)
                .setCallback(shareListener)
                .share();
    }

    /**
     * 初始化popupwindow_menu
     */
    private void initPop_menu() {
        View popupView = NewsActivity.this.getLayoutInflater().inflate(R.layout.layout_pop_menu, null);
        popupView.findViewById(R.id.layout_love).setOnClickListener(popListener);
        popupView.findViewById(R.id.layout_font).setOnClickListener(popListener);
        popupView.findViewById(R.id.layout_error).setOnClickListener(popListener);
        popupView.findViewById(R.id.layout_shared).setOnClickListener(popListener);
        popupView.findViewById(R.id.layout_night).setOnClickListener(popListener);
        img_love = (ImageView) popupView.findViewById(R.id.img_love);
        text_love = (TextView) popupView.findViewById(R.id.text_love);
        text_night = (TextView) popupView.findViewById(R.id.text_night);
        AutoUtils.autoSize(popupView);
        window_menu = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window_menu.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        window_menu.setFocusable(true);
        window_menu.setOutsideTouchable(true);
        window_menu.update();
        window_menu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f; //0.0-1.0
                getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * @param view
     */
    private void showPop_menu(View view) {
        window_menu.showAsDropDown(view, 0, 20);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 友盟分享分享pop
     */
    private void showPop_shared() {
        UMWeb web = new UMWeb(newsBean.getJump_url() + "?is_show=true");
        String title = newsBean.getNews_title();
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this, newsBean.getTpicsurl().get(0)));  //缩略图
        web.setDescription(title);//描述
        new ShareAction(NewsActivity.this)
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
            Toast.makeText(NewsActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NewsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(NewsActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);//完成回调
    }

    /**
     * 获取点赞数
     */
    private void getZan() {
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<Integer>() {
            @Override
            public void onNext(Integer data) {
                Log.i("getZan", data + "");
                txtZan.setText(data + "赞");
            }
        };
        HttpUtil.getInstance().getZan(new ProgressSubscriber(onNextListener, this), newsBean.getNews_id());
    }

    /**
     * 点赞
     */
    private void zan() {
        //判断登录
        if (app.getUser() == null) {
            Toast.makeText(this, "请先登录账号再进行评论", Toast.LENGTH_SHORT).show();
            return;
        }
        SubscriberOnNextListener onNextListener = new SubscriberOnNextListener<String>() {
            @Override
            public void onNext(String data) {
                Log.i("getZan", data + "");
                imgSharedZan.setImageResource(R.mipmap.pic_shared_zan2);
                txtZan.setText(data + "赞");
                imgSharedZan.setOnClickListener(null);
            }
        };
        HttpUtil.getInstance().zan(new ProgressSubscriber(onNextListener, this), app.getUser().getUser_id(), newsBean.getNews_id());
    }


}
