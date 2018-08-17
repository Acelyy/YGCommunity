package com.yonggang.ygcommunity.Activity.Personal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CopyrightActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView webView;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copyright);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        Log.i("url",url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new YgWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(CopyrightActivity.this)
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
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
