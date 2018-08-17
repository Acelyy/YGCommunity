package com.yonggang.ygcommunity.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.yonggang.ygcommunity.BaseActivity;
import com.yonggang.ygcommunity.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertismentActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.adv)
    WebView adv;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adv2);
        ButterKnife.bind(this);
        url = getIntent().getExtras().getString("url");
        WebSettings webSettings = adv.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        adv.loadUrl(url);
        adv.setWebViewClient(new YgWebViewClient());
        adv.setWebChromeClient(new WebChromeClient() {
            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(AdvertismentActivity.this)
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
            public void onReceivedTitle(WebView view, String title) {
                AdvertismentActivity.this.title.setText(title);
                super.onReceivedTitle(view, title);
            }
        });
    }

    // Web视图
    private class YgWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        adv.clearCache(true);
        adv.clearHistory();
        adv.loadUrl("about:blank");
        super.onDestroy();
    }

    @OnClick(R.id.img_finish)
    public void onViewClicked() {
        finish();
    }
}
