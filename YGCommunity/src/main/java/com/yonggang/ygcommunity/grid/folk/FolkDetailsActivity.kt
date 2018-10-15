package com.yonggang.ygcommunity.grid.folk

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.yonggang.ygcommunity.Activity.BbsPicActivity
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.FolkChoose
import com.yonggang.ygcommunity.Entry.FolkDetails
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_folk_details.*
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.text.SimpleDateFormat
import java.util.*

class FolkDetailsActivity : BaseActivity() {
    private lateinit var id: String
    private lateinit var data: FolkDetails
    private lateinit var datadzb: MutableList<FolkChoose.DataBean.DzbBean>
    private lateinit var datamqxz: MutableList<FolkChoose.DataBean.MqxzBean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folk_details)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        id = intent.getStringExtra("id")

        getFolkChoose()
        getFolkDetails(id)
        pic_back.setOnClickListener { finish() }
    }

    private fun getFolkDetails(id: String) {
        val subscriber = object : Subscriber<FolkDetails>() {
            @SuppressLint("SetTextI18n")
            override fun onNext(it: FolkDetails?) {
                data = it!!
                tv_name.text = data.dyxm
                tv_person.text = data.input_person
                tv_uptime.text = data.input_time
                tv_uptime.text = data.input_time
                tv_downtime.text = data.visit_time
                tv_address.text = data.visit_place
                tv_figure.text = data.interviewee
                tv_reason.text = data.content
                tv_result.text = data.result
                tv_label.text = data.tag
                tv_process.text = data.handling_process
                tv_photes.text = "共" + if (data.mqrj_imgs == null || (data.mqrj_imgs.size == 1 && data.mqrj_imgs[0] == "")) {
                    0
                } else {
                    data.mqrj_imgs.size
                } + "张图片"
                for (i in datadzb) {
                    if (data.dzbxxb_id.equals(i.id)) {
                        tv_team.text = i.name
                    }
                }
                for (i in datamqxz) {
                    if (data.nature_id.equals(i.id)) {
                        tv_area.text = i.name
                    }
                }
                if (data.mqrj_imgs == null || (data.mqrj_imgs.size == 1 && data.mqrj_imgs[0] == "")) {
                }else{
                    layout_photes.setOnClickListener {
                        this@FolkDetailsActivity.startActivity<BbsPicActivity>("imgs" to data.mqrj_imgs, "index" to 0)
                    }
                }

            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
            }
        }
        HttpUtil.getInstance().getFolkDetails(subscriber, id)
    }

    private fun getFolkChoose() {
        val subscriber = object : Subscriber<FolkChoose>() {
            override fun onNext(it: FolkChoose?) {
                datadzb = it!!.data.dzb
                datamqxz = it.data.mqxz
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
            }
        }
        HttpUtil.getInstance().getFolkChoose(subscriber)
    }

}
