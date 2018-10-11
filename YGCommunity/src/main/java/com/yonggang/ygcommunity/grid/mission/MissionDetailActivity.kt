package com.yonggang.ygcommunity.grid.mission

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import com.yonggang.ygcommunity.Activity.BbsPicActivity
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.MissionDetail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.NoDoubleClickListener
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_mission_detail.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.util.*

class MissionDetailActivity : BaseActivity() {

    private lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_detail)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        val id = intent.getStringExtra("id")
        refresh.setOnRefreshListener {
            getDetail(id)
        }
        trail.setOnClickListener {
            startActivity<TrailActivity>("id" to id)
        }
        text_back.setOnClickListener { finish() }
        pic_back.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        refresh.autoRefresh()
    }

    /**
     * 获取工单详情
     */
    private fun getDetail(id: String) {
        val subscriber = object : Subscriber<MissionDetail>() {
            @SuppressLint("SetTextI18n")
            override fun onNext(data: MissionDetail?) {
                if (data != null) {
                    tv_id.text = data.sjbh
                    tv_severity.text = data.yzcd
                    tv_title.text = data.sjbt
                    tv_type.text = data.sjfl
                    tv_location.text = data.sjdw
                    tv_des.text = data.sjms

                    tv_photes.text = "共" + if (data.girdimgs == null) {
                        0
                    } else {
                        data.girdimgs.size
                    } + "张图片"

                    tv_status.text = when (data.status) {
                        1 -> "待签收"
                        2 -> "已签收"
                        3 -> "平台自行处理"
                        4 -> "核查通知"
                        5 -> "转派部门"
                        7 -> "部门处理"
                        8 -> "部门完结"
                        10 -> "任务完成"
                        else -> ""
                    }
                    submit.visibility = when (data.status) {
                        1 -> View.VISIBLE
                        2 -> View.VISIBLE
                        3 -> View.VISIBLE

                        5 -> if (app.grid.appauth == 2) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                        6 -> if (app.grid.appauth == 2) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                        7 -> if (app.grid.appauth == 2) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                        8 -> if (app.grid.appauth == 2) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }

                        else -> View.GONE
                    }
                    submit.text = when (data.status) {
                        1 -> "签收"
                        2 -> "平台自行处理"
                        3 -> "发出核查通知"

                        5 -> "签收"
                        6 -> "任务完结"
                        7 -> "任务完结"
                        8 -> "发出核查通知"
                        else -> ""
                    }
                    submit.setOnClickListener(object : NoDoubleClickListener() {
                        override fun onNoDoubleClick(v: View?) {
                            when (data.status) {
                                1 -> signEvent(id)
                                2 -> endEvent(id)
                                3 -> {
                                    val view = LayoutInflater.from(this@MissionDetailActivity).inflate(R.layout.item_input,null)
                                    val input=view.find<EditText>(R.id.input)
                                    val builder = AlertDialog.Builder(this@MissionDetailActivity)
                                    builder.setTitle("请输入意见")
                                            .setPositiveButton("确定") { _, _ ->
                                                sendCheck(id, input.text.toString())
                                            }.setNegativeButton("取消") { _, _ -> }
                                            .create().show()
                                }

                                5 -> signEvent(id)
                                6 -> endEvent(id)
                                7 -> endEvent(id)
                                8 -> {
                                    val view = LayoutInflater.from(this@MissionDetailActivity).inflate(R.layout.item_input,null)
                                    val input=view.find<EditText>(R.id.input)
                                    val builder = AlertDialog.Builder(this@MissionDetailActivity)
                                    builder.setTitle("请输入意见")
                                            .setPositiveButton("确定") { _, _ ->
                                                sendCheck(id, input.text.toString())
                                            }.setNegativeButton("取消") { _, _ -> }
                                            .create().show()
                                }
                            }
                        }
                    })

                    // 额外的按钮，用于status为2和6的时候指派、
                    extra.visibility = when (data.status) {
                        2 -> View.VISIBLE
                        6 -> if (app.grid.appauth == 2) {
                            View.VISIBLE
                        } else {
                            View.GONE
                        }
                        else -> View.GONE
                    }
                    extra.text = when (data.status) {
                        2 -> "转办部门"
                        6 -> "任务指派"
                        else -> ""
                    }
                    extra.setOnClickListener {
                        startActivity<TransferActivity>("id" to id, "status" to data.status)
                    }

                    if (data.girdimgs != null && !data.girdimgs.isEmpty()) {
                        layout_pic.setOnClickListener {
                            val intent = Intent(this@MissionDetailActivity, BbsPicActivity::class.java)
                            val bundle = Bundle()
                            bundle.putStringArrayList("imgs", data.girdimgs as ArrayList<String>)
                            bundle.putInt("index", 0)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    }
                }

                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("getDetail", e.toString())
                Snackbar.make(refresh, "获取失败，请稍后再试", Snackbar.LENGTH_LONG).show()
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getMissionDetail(subscriber, id, app.grid.id)
    }

    /**
     * 事件签收
     */
    private fun signEvent(id: String) {
        val subscriber = object : Subscriber<String>() {
            override fun onNext(data: String?) {
                Log.i("signEvent", data)
                finish()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("signEvent", e.toString())
            }
        }
        HttpUtil.getInstance().signEvent(subscriber, id, app.grid.id, app.grid.appauth)
    }

    /**
     * 事件完结：平台自行处理或者
     */
    private fun endEvent(id: String) {
        val subscriber = object : Subscriber<String>() {
            override fun onNext(data: String?) {
                Log.i("signEvent", data)
                finish()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("signEvent", e.toString())
            }
        }
        HttpUtil.getInstance().endEvent(subscriber, id, app.grid.id, app.grid.appauth)
    }

    /**
     * 发出核查通知
     */
    private fun sendCheck(id: String,comment: String) {
        if(comment.isEmpty()){
            Snackbar.make(pic_back,"意见不能为空",Snackbar.LENGTH_LONG)
            return
        }
        val subscriber = object : Subscriber<String>() {
            override fun onNext(data: String?) {
                Log.i("signEvent", data)
                finish()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("signEvent", e.toString())
            }
        }
        HttpUtil.getInstance().sendCheck(subscriber, id, app.grid.id,comment)
    }
}
