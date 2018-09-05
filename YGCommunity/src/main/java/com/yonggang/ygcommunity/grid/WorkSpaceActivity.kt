package com.yonggang.ygcommunity.grid

import android.os.Bundle
import android.view.View
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Gztj
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.grid.Visit.VisitActivity
import com.yonggang.ygcommunity.grid.check.CheckListActivity
import com.yonggang.ygcommunity.grid.event.AddEventActivity
import com.yonggang.ygcommunity.grid.event.EventActivity
import com.yonggang.ygcommunity.grid.folk.FolkActivity
import com.yonggang.ygcommunity.grid.house.HouseInfoActivity
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_work_space.*
import org.jetbrains.anko.startActivity
import rx.Subscriber

class WorkSpaceActivity : BaseActivity(), View.OnClickListener {

    lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_space)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        val header = WaveSwipeHeader(this)
        refresh.setPrimaryColorsId(R.color.refresh_color)
        refresh.setRefreshHeader(header)
        refresh.isEnableLoadMore = false

        refresh.setOnRefreshListener {
            refresh.finishRefresh()
        }

        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
        layout_today_event.setOnClickListener(this)
        layout_today_hourse.setOnClickListener(this)
        layout_today_walk.setOnClickListener(this)

        layout_my_task.setOnClickListener(this)
        layout_my_work.setOnClickListener(this)

        layout_app_hourse.setOnClickListener(this)
        layout_app_event.setOnClickListener(this)
        layout_app_note.setOnClickListener(this)
        layout_app_walk.setOnClickListener(this)
        layout_app_message.setOnClickListener(this)
        getGztj()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_today_event -> startActivity<EventActivity>()
            R.id.layout_app_event -> startActivity<AddEventActivity>()
            R.id.layout_today_hourse -> startActivity<HouseInfoActivity>()
            R.id.layout_app_note -> startActivity<FolkActivity>()
            R.id.layout_app_walk -> startActivity<VisitActivity>()
            R.id.layout_my_task -> startActivity<CheckListActivity>()
        }
    }

    /**
     * 获取工作统计
     */
    private fun getGztj() {
        val subscriber = object : Subscriber<Gztj>() {
            override fun onNext(data: Gztj?) {
                size_today_event.withNumber(data!!.sbsj)
                size_today_hourse.withNumber(data.rfcj)
                size_today_walk.withNumber(data.zfqk)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getGztj(subscriber)
    }
}
