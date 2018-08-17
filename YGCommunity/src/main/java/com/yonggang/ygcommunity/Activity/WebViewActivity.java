package com.yonggang.ygcommunity.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.export.external.interfaces.JsPromptResult;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.container)
    FrameLayout container;

    private String url;

    private String back_url;

    WebView newWebView;

    View contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        url = getIntent().getStringExtra("url");
        name.setText(getIntent().getStringExtra("name"));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
        } else {
            initWebView(webView);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initWebView(webView);
        }
    }

    private void initWebView(final WebView webView) {
        IX5WebViewExtension i5 = webView.getX5WebViewExtension();
        Log.i("i5", i5 == null ? "null" : i5.toString());
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持js调用window.open方法
        webSettings.setAllowFileAccess(true);// 设置允许访问文件数据
        webSettings.setSupportMultipleWindows(true);// 设置允许开启多窗口
        webSettings.setDomStorageEnabled(true);//
        webSettings.setJavaScriptEnabled(true);// 设置支持javascript
        webView.setWebViewClient(new YgWebViewClient());
        webView.setDownloadListener(new MyWebViewDownLoadListener());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                Log.i("onCreateWindow", "onCreateWindow");
                newWebView = new WebView(WebViewActivity.this);//新创建一个webview
                initWebView(newWebView);//初始化webview
                container.addView(newWebView);//把webview加载到activity界面上
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;//以下的操作应该就是让新的webview去加载对应的url等操作。
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                return true;
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                if (newWebView != null) {
                    container.removeView(newWebView);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(WebViewActivity.this)
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

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(WebViewActivity.this)
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

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                //用Android组件替换
                new AlertDialog.Builder(WebViewActivity.this)
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
        webView.loadUrl(url);
    }

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.i("WebUrl", url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("onPageFinished", url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //加载开始

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

    }

    @Override
    protected void onDestroy() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        webView.clearCache(true);
        webView.clearHistory();
        webView.loadUrl("about:blank");
        super.onDestroy();
    }

    @OnClick({R.id.pic_back, R.id.pic_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pic_back:
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
                break;
            case R.id.pic_close:
                finish();
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (back_url != null) {
            String url = URLDecoder.decode(back_url);
            Log.i("URLDecoder", url);
            webView.loadUrl(url);
        } else {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        }
        return true;
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, String> getUrlParams(String param) {
        Map<String, String> map = new HashMap<>(0);
        if (param == null || param.equals("")) {
            return map;
        }
        String[] params = param.split("&");
        for (String param1 : params) {
            String[] p = param1.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
                Log.i("url_map", p[0] + "=" + p[1]);
            }
        }

        return map;
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
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
