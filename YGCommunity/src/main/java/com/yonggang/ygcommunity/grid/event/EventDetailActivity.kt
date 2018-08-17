package com.yonggang.ygcommunity.grid.event

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.GridEventDetail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.grid.event.adapter.TrailAdapter
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_event_detail.*
import rx.Subscriber

class EventDetailActivity : BaseActivity() {

    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_detail)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        id = intent.getStringExtra("id")
        pic_back.setOnClickListener { finish() }
        submit.visibility = if (intent.getBooleanExtra("is_draft", false)) {
            View.VISIBLE
        } else {
            View.GONE
        }
        submit.setOnClickListener { submit() }
        getGridEventDetail()
    }

    /**
     * 获取事件详情
     */
    private fun getGridEventDetail() {
        val subscriber = object : Subscriber<GridEventDetail>() {
            override fun onNext(it: GridEventDetail?) {
                rvTrail.layoutManager = LinearLayoutManager(this@EventDetailActivity)
                val adapter = TrailAdapter(it!!, this@EventDetailActivity)
                rvTrail.adapter = adapter
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
            }

        }
        HttpUtil.getInstance().getGridEventDetail(subscriber, id)
    }

    /**
     * 提交草稿
     */
    private fun submit() {
        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            finish()
        }
        HttpUtil.getInstance().submitEvent(ProgressSubscriber<String>(subscriberOnNextListener, this, "提交中"), id)
    }
}
