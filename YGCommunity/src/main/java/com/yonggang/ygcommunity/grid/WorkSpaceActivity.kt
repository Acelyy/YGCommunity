package com.yonggang.ygcommunity.grid

import android.os.Bundle
import android.view.View
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.grid.event.AddEventActivity
import com.yonggang.ygcommunity.grid.event.EventActivity
import com.yonggang.ygcommunity.grid.house.HouseActivity
import kotlinx.android.synthetic.main.activity_work_space.*
import org.jetbrains.anko.startActivity

class WorkSpaceActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_space)
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
        layout_my_visit.setOnClickListener(this)

        layout_app_hourse.setOnClickListener(this)
        layout_app_event.setOnClickListener(this)
        layout_app_note.setOnClickListener(this)
        layout_app_task.setOnClickListener(this)
        layout_app_work.setOnClickListener(this)
        layout_app_walk.setOnClickListener(this)
        layout_app_message.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_today_event -> startActivity<EventActivity>()
            R.id.layout_app_event -> startActivity<AddEventActivity>()
            R.id.layout_today_hourse -> startActivity<HouseActivity>()
        }
    }

}
