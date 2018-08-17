package com.yonggang.ygcommunity.Activity.Server;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.Entry.Notice;
import com.yonggang.ygcommunity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    private Notice.NoticeBean notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
        notice = (Notice.NoticeBean) getIntent().getExtras().getSerializable("notice");
        Log.i("X5Version", webView.getX5WebViewExtension() == null ? "null" : webView.getX5WebViewExtension().toString());
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.loadUrl(notice.getJump_url());
        webView.setWebViewClient(new YgWebViewClient());
        webView.setWebChromeClient(new WebChromeClientAboveFive(this));
        webView.setDownloadListener(new MyWebViewDownLoadListener());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.img_finish, R.id.img_shared})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_finish:
                finish();
                break;
            case R.id.img_shared:
                notice_share();
                break;
        }
    }

    /**
     * 友盟分享分享pop
     */
    private void notice_share() {
        UMWeb web = new UMWeb(notice.getJump_url());
        String title = notice.getTitle();
        web.setTitle(title);//标题
        web.setThumb(new UMImage(this, R.mipmap.logo));  //缩略图
        web.setDescription(title);//描述
        new ShareAction(NoticeDetailActivity.this)
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
            Toast.makeText(NoticeDetailActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(NoticeDetailActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(NoticeDetailActivity.this, "取消分享", Toast.LENGTH_LONG).show();

        }
    };

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class BaseWebChromeClient extends WebChromeClient {

        public static final int FILECHOOSER_RESULTCODE = 10000;

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {

        }

        // For Android 3.0+
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {

        }

        //For Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {

        }

        //For Android 5.0+
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            return true;
        }

        public void onActivityResult(int resultCode, Intent data) {

        }
    }

    class WebChromeClientAboveFive extends BaseWebChromeClient {

        private ValueCallback<Uri[]> mUploadCallbackAboveFive;
        private Activity mActivity;

        public WebChromeClientAboveFive(Activity activity) {
            this.mActivity = activity;
        }

        /**
         * 兼容5.0及以上
         *
         * @param webView
         * @param valueCallback
         * @param fileChooserParams
         * @return
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            Log.i("onShowFileChooser", "11111111");
            mUploadCallbackAboveFive = valueCallback;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            mActivity.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            return true;
        }

        @Override
        public void onActivityResult(int resultCode, Intent data) {
            if (null == mUploadCallbackAboveFive) {
                return;
            }
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        int itemCount = clipData.getItemCount();
                        results = new Uri[itemCount];
                        for (int i = 0; i < itemCount; i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mUploadCallbackAboveFive.onReceiveValue(results);
            mUploadCallbackAboveFive = null;
            return;
        }
    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Log.i("tag", "url=" + url);
            Log.i("tag", "userAgent=" + userAgent);
            Log.i("tag", "contentDisposition=" + contentDisposition);
            Log.i("tag", "mimetype=" + mimetype);
            Log.i("tag", "contentLength=" + contentLength);
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
}
