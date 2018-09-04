package com.yonggang.ygcommunity.grid.house

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import com.alibaba.fastjson.JSON
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Garden
import com.yonggang.ygcommunity.Entry.House
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_select_house.*
import org.jetbrains.anko.startActivity
import rx.Subscriber


class SelectHouseActivity : BaseActivity() {

    var page: Int = 0

    private lateinit var finishBroadcastReceiver: FinishBroadCast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_house)
        // 绑定广播
        finishBroadcastReceiver = FinishBroadCast()
        val filter = IntentFilter()
        filter.addAction("houseFinish")
        registerReceiver(finishBroadcastReceiver, filter)

        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        page = intent.getIntExtra("page", 0)
        var title = intent.getStringExtra("title")
        if (title == null) {
            title = "请选择园区"
        }
        tv_title.text = title

        refresh.setOnRefreshListener {
            when (page) {
                0 -> getGarden()
                1 -> getBuilding(intent.getStringExtra("garden"))
                2 -> getHouse(intent.getStringExtra("garden"), intent.getStringExtra("building"))
            }
        }
        refresh.autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(finishBroadcastReceiver)
    }

    /**
     *
     */
    private fun getGarden() {
        val subscriber = object : Subscriber<List<Garden>>() {
            override fun onNext(t: List<Garden>?) {
                Log.i("getGarden", JSON.toJSONString(t))
                val adapter = ArrayAdapter(this@SelectHouseActivity, android.R.layout.simple_list_item_1, t)
                list_house.adapter = adapter
                list_house.setOnItemClickListener { parent, view, position, id ->
                    startActivity<SelectHouseActivity>(
                            "title" to t!![position].yqmc, "page" to 1, "garden" to t[position].yqmc
                    )
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("getGarden", e.toString())
                Snackbar.make(refresh, "获取失败，请稍后重试", Snackbar.LENGTH_LONG).show()
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getGarden(subscriber)
    }

    /**
     * 获取幢
     */
    private fun getBuilding(garden: String) {
        val subscriber = object : Subscriber<List<String>>() {
            override fun onNext(t: List<String>?) {
                val adapter = ArrayAdapter<String>(this@SelectHouseActivity, android.R.layout.simple_list_item_1, t)
                list_house.adapter = adapter
                refresh.finishRefresh()
                list_house.setOnItemClickListener { parent, view, position, id ->
                    startActivity<SelectHouseActivity>(
                            "title" to garden + t!![position],
                            "page" to 2,
                            "garden" to garden,
                            "building" to t[position]
                    )
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("getBuilding", e.toString())
                Snackbar.make(refresh, "获取失败，请稍后重试", Snackbar.LENGTH_LONG).show()
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getBuilding(subscriber, garden)
    }

    private fun getHouse(garden: String, building: String) {
        val subscriber = object : Subscriber<List<House>>() {
            override fun onNext(t: List<House>?) {
                val adapter = ArrayAdapter<House>(this@SelectHouseActivity, android.R.layout.simple_list_item_1, t)
                list_house.adapter = adapter
                list_house.setOnItemClickListener { parent, view, position, id ->
                    val address = garden + building + "幢" + t!![position].fh
                    val intentData = Intent("data")
                    intentData.putExtra("address", address)
                    intentData.putExtra("pk", t[position].fwbm_pk)
                    sendBroadcast(intentData)
                    val intentFinish = Intent("houseFinish")
                    sendBroadcast(intentFinish)
                }

                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("getHouse", e.toString())
                Snackbar.make(refresh, "获取失败，请稍后重试", Snackbar.LENGTH_LONG).show()
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getHouse(subscriber, garden, building)
    }

    /**
     * 关闭广播
     */
    inner class FinishBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            finish()
        }
    }
}
