package com.yonggang.ygcommunity.grid.check

import android.os.Bundle
import android.util.Log
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_check_list.*
import rx.Subscriber

class CheckListActivity : BaseActivity() {
    private lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        refresh.setOnRefreshListener {
            getCheckList(1)
        }
        refresh.autoRefresh()
    }

    private fun getCheckList(page: Int) {
        val subscriber = object : Subscriber<String>() {
            override fun onNext(t: String?) {
                Log.i("getCheckList", t)
                refresh.finishRefresh()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getCheckList(subscriber, page, app.grid.id)
    }
}
