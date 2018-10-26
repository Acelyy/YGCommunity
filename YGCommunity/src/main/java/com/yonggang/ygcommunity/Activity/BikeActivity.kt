package com.yonggang.ygcommunity.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import com.just.agentweb.AgentWeb
import com.uuzuche.lib_zxing.activity.CaptureActivity
import com.uuzuche.lib_zxing.activity.CodeUtils
import com.yonggang.ygcommunity.R
import kotlinx.android.synthetic.main.activity_bike.*


class BikeActivity : AppCompatActivity() {

    lateinit var mAgentWeb: AgentWeb

    private val mWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            //do you  work
        }

    }
    private val mWebChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            //do you work
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bike)
        img_finish.setOnClickListener {
            finish()
        }
        mAgentWeb = AgentWeb.with(this)//传入Activity
                .setAgentWebParent(container, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()// 使用默认进度条
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()//
                .ready()
                .go("http://61.155.218.146:8015/default.aspx")

        mAgentWeb.jsInterfaceHolder.addJavaObject("android", AndroidInterface(mAgentWeb, this))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //处理扫描结果（在界面上显示）
        if (null != data) {
            val bundle = data.extras ?: return
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                val result = bundle.getString(CodeUtils.RESULT_STRING)
                mAgentWeb.jsAccessEntrace.quickCallJs("app_scan_callback", result)
            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    public override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    inner class AndroidInterface(private val agent: AgentWeb, private val context: Context) {

        private val deliver = Handler(Looper.getMainLooper())

        @JavascriptInterface
        fun callFromJs() {
            val intent = Intent(context, CaptureActivity::class.java)
            startActivityForResult(intent, 0x666)
        }

    }
}
