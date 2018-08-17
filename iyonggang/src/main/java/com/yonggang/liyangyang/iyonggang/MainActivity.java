package com.yonggang.liyangyang.iyonggang;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private WebView webView;

    private TextView title;

    private TextView time;

    private ImageView home;

    private Button btn_back;

    private int second = 30;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                int code = msg.getData().getInt("time");
                time.setText(code + "");
            } else if (msg.what == 0x321) {
                home.performClick();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        btn_back = (Button) findViewById(R.id.btn_back);
        title = (TextView) findViewById(R.id.title);
        time = (TextView) findViewById(R.id.time);
        home = (ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("javascript:goHomeBack()");
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home.performClick();
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webView.setWebViewClient(new LyyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String t) {
                super.onReceivedTitle(view, t);
                Log.i("title", t);
                title.setText(t);
            }

            /**
             * 处理JavaScript Alert事件
             */
            @Override
            public boolean onJsAlert(WebView view, String url,
                                     String message, final JsResult result) {
                if (message.equals("success")) {
                    Log.i("success", "success");
                    Intent finish = new Intent();
                    finish.setAction("finish");
                    sendBroadcast(finish);
                    Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //用Android组件替换
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage(message)
                            .setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    result.confirm();
                                    Intent finish = new Intent();
                                    finish.setAction("finish");
                                    sendBroadcast(finish);
                                    Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new AlertDialog.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    result.cancel();
                                    if (second == 0) {
                                        second = 60;
                                        countdown();
                                    } else {
                                        second = 60;
                                    }
                                }
                            })
                            .setCancelable(false)
                            .create().show();
                }
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
        webView.loadUrl(getIntent().getStringExtra("url"));
        webView.setOnTouchListener(this);
        countdown();
        Log.i("onCreate", this.toString());
    }

    @Override
    protected void onDestroy() {
        Log.i("onDestroy", this.toString());
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().removeSessionCookie();
        second = -1;
        webView.clearCache(true);
        webView.clearHistory();
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("111", "111");
        second = 30;
        return false;
    }

    // Web视图
    private class LyyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Uri uri = Uri.parse(url);
            Log.i("url", url);
            if (uri.getScheme().equals("js")) {
                Log.i("js", "js");
                if (uri.getAuthority().equals("weburl")) {
                    Log.i("weburl", "weburl");
                    String weburl = uri.getQueryParameter("url");
                    Log.i("params", weburl);
                    Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                    intent.putExtra("url", "http://" + weburl);
                    startActivity(intent);
                    finish();
                    //view.loadUrl("http://" + weburl);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                Intent finish = new Intent();
                finish.setAction("finish");
                sendBroadcast(finish);
                Intent intent = new Intent(MainActivity.this, FirstActivity.class);
                startActivity(intent);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
     * 倒计时1分钟
	 */

    private void countdown() {
        CountDown cd = new CountDown(handler);
        cd.start();
    }

    class CountDown extends Thread {
        private Handler handler;

        public CountDown(Handler handler) {
            super();
            this.handler = handler;
        }

        @Override
        public void run() {
            while (second > 0) {
                second--;
                try {
                    sleep(1000);
                    Message msgTime = new Message();
                    msgTime.what = 0x123;
                    Bundle time = new Bundle();
                    time.putInt("time", second);
                    msgTime.setData(time);
                    handler.sendMessage(msgTime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (second == 0) {
                handler.sendEmptyMessage(0x321);
            }
        }
    }

}
