package com.yonggang.ygcommunity.grid.notify

import android.os.Bundle
import android.view.KeyEvent
import com.tencent.smtt.sdk.*
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_notice_detail.*

class NotifyDetailsActivity : BaseActivity() {

    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_details)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        id = intent.getStringExtra("id")
        val string = HttpUtil.BASE_URL + "gird_get_jlist_detail/id/" + id
        if (id != "") {
            webView.loadUrl(string)
            webView.setWebViewClient(YgWebViewClient())
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // Web视图
    private inner class YgWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view!!.loadUrl(url)
            return true
        }
    }
}
