package com.yonggang.ygcommunity.grid.notify

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.umeng.socialize.utils.Log
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_notice_detail.*
import rx.Subscriber

class NotifyDetailsActivity : BaseActivity() {

    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_details)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        id = intent.getStringExtra("id")
        val string = HttpUtil.BASE_URL + "gird_get_jlist_detail/id/" + id
        if (id != ""){
            webView.loadUrl(string)
        }
    }


}
