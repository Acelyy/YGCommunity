package com.yonggang.ygcommunity.monitor

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.alibaba.fastjson.JSON
import com.umeng.socialize.utils.Log
import com.yonggang.ygcommunity.Activity.BbsPicActivity
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.EventDetail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import com.yonggang.ygcommunity.monitor.adapter.TrailAdapter
import com.yonggang.ygcommunity.monitor.adapter.TrailAdapter.OnImageClickListener
import kotlinx.android.synthetic.main.activity_monitor_detail.*
import java.util.*

class MonitorDetailActivity : BaseActivity() {

    lateinit var id: String
    var type: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor_detail)
        id = intent.getStringExtra("id")
        type = intent.getIntExtra("type", -1)
        img_finish.setOnClickListener { finish() }
        getEvenDetail()
    }

    /**
     * 获取事件详情
     */
    private fun getEvenDetail() {
        val subscriberOnNextListener = SubscriberOnNextListener<EventDetail> {
            Log.i("getEvenDetail", JSON.toJSONString(it))
            setUI(it)
        }
        HttpUtil.getInstance().getEventDetail(ProgressSubscriber<EventDetail>(subscriberOnNextListener, this), id, type)
    }

    /**
     * 根据接口数据，刷新界面UI
     */
    private fun setUI(it: EventDetail) {
        rvTrail.layoutManager = LinearLayoutManager(this)
        val adapter = TrailAdapter(it, this)
        adapter.onImageClickListener = object : OnImageClickListener {
            override fun onImageClick(view: View, position: Int) {
                val intent = Intent(this@MonitorDetailActivity, BbsPicActivity::class.java)
                val bundle = Bundle()
                bundle.putStringArrayList("imgs", it.info.img as ArrayList<String>?)
                bundle.putInt("index", position)
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
        rvTrail.adapter = adapter
    }
}
