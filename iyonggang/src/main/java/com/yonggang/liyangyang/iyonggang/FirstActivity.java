package com.yonggang.liyangyang.iyonggang;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.yonggang.liyangyang.iyonggang.HttpUtils.HttpUtil;
import com.yonggang.liyangyang.iyonggang.HttpUtils.ProgressSubscriber;
import com.yonggang.liyangyang.iyonggang.HttpUtils.SPUtil;
import com.yonggang.liyangyang.iyonggang.HttpUtils.SubscriberOnNextListener;

import java.io.File;
import java.util.List;

import rx.Subscriber;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

public class FirstActivity extends AppCompatActivity {

    WebView webView;

    Place.PlaceMap map = null;

    private int mCurrentItem = 0;

    private int mCurrentChooseItem;

    //handler更新ui
    private Handler handler_down = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(FirstActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
                    install(FirstActivity.this);
                    break;
                case DownloadManager.STATUS_RUNNING:
                    break;
                case DownloadManager.STATUS_FAILED:
                    break;
                case DownloadManager.STATUS_PENDING:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getVersion();
        Log.i("onCreate", this.toString());
        String url = getIntent().getStringExtra("url");
        if (!"".equals(SPUtil.getPlace(this))){
            if (url == null) {
                initWeb(Domain.URL, SPUtil.getPlace(this));
            } else {
                initWeb(url, null);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "onResume");
    }

    private void initWeb(String url, String place_id) {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setCacheMode(LOAD_NO_CACHE);
        webView.setWebViewClient(new LyyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String t) {
                super.onReceivedTitle(view, t);
                Log.i("title", t);
            }

            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                //用Android组件替换
                new AlertDialog.Builder(FirstActivity.this)
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
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
        if (place_id != null) {
            webView.loadUrl(url + "?place_id=" + place_id);
        } else {
            webView.loadUrl(url);
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("onDestroy", this.toString());
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        webView.clearCache(true);
        webView.clearHistory();
        super.onDestroy();
    }

    // Web视图
    private class LyyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            Log.i("url", url);
            if (uri.getScheme().equals("js")) {
                Log.i("js", "js");
                if (uri.getAuthority().equals("webview")) {
                    hideInput();
                    Log.i("hideInput", "hideInput");
                } else if (uri.getAuthority().equals("weburl")) {
                    Log.i("weburl", "weburl");
                    String weburl = uri.getQueryParameter("url");
                    Log.i("params", weburl);
                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                    intent.putExtra("url", "http://" + weburl);
                    startActivity(intent);
                    finish();
                }
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    /**
     * 隐藏软键盘
     */
    private void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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

    /**
     * 获取版本
     */
    public void getVersion() {
        Subscriber subscriber = new Subscriber<Version>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Version data) {
                String version_new = data.getVersion();
                String version_local = GetVersion(FirstActivity.this);
                if ("".equals(version_new) || "".equals(version_local)) {
                    return;
                }
                if (Double.parseDouble(version_new) > Double.parseDouble(version_local)) {
                    String url = data.getFile();
                    new Thread(new DownLoadRunnable(FirstActivity.this, url, "i永钢", 0, handler_down)).start();
                } else {
                    //检查使用地点的设置
                    if (SPUtil.getPlace(FirstActivity.this).equals("")) {
                        getPlace();
                    } else {
                        //initWeb("", SPUtil.getPlace(FirstActivity.this));
                    }
                }
            }
        };
        HttpUtil.getInstance().getVersion(subscriber);
    }

    // 取得版本号
    public static String GetVersion(Context context) {
        try {
            PackageInfo manager = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return manager.versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 安装程序
     *
     * @param context
     */
    public void install(Context context) {
        Log.i("install", "start");
        File file = MyUtils.getCacheFile(MyUtils.APP_NAME, context);
        if (file == null || !file.exists()) {
            return;
        }
        Intent installintent = new Intent();
        installintent.setAction(Intent.ACTION_VIEW);
        // 在Boradcast中启动活动需要添加Intent.FLAG_ACTIVITY_NEW_TASK
        installintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installintent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(installintent);
        Log.i("install", "finish");
    }

    /**
     *
     */
    private void getPlace() {
        SubscriberOnNextListener subscriber = new SubscriberOnNextListener<Place>() {

            @Override
            public void onNext(Place data) {
                Log.i("getPlace", data.toString());
                map = data.getMap();
                showDialog(map.getValues());
            }
        };
        HttpUtil.getInstance().getPlace(new ProgressSubscriber<Place>(subscriber, this));
    }

    private void showDialog(List<String> canteens) {
        String[] toBeStored = canteens.toArray(new String[canteens.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择餐点");
        builder.setSingleChoiceItems(toBeStored, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentItem = mCurrentChooseItem;
                SPUtil.setPlace(map.getKeys().get(mCurrentItem), FirstActivity.this);
                initWeb(Domain.URL, SPUtil.getPlace(FirstActivity.this));
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

}
