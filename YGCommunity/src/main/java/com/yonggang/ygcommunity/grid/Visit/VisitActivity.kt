package com.yonggang.ygcommunity.grid.Visit

import android.os.Bundle
import android.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_visit.*
import org.jetbrains.anko.startActivity
import java.util.*


class VisitActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }
        pic_add.setOnClickListener { startActivity<AddVisitActivity>() }
        calendarView.setSelectedDate(Date())
        Log.i("date", getTime(calendarView.selectedDate))
        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(p0: MaterialCalendarView, p1: CalendarDay, p2: Boolean) {
                Log.i("date", getTime(p1))
            }
        })

    }

    private fun getTime(date: CalendarDay): String {
        val year = date.year
        val month = date.month + 1 //月份跟系统一样是从0开始的，实际获取时要加1
        val day = date.day
        return year.toString() + "-" + month + "-" + day
    }
}
