package com.yonggang.ygcommunity.grid.Visit

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Visit
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_visit.*
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.util.*
import android.support.design.widget.Snackbar
import android.view.LayoutInflater


class VisitActivity : BaseActivity() {

    private lateinit var date: String
    private lateinit var app: YGApplication
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        app = application as YGApplication
        pic_back.setOnClickListener {
            finish()
        }
        text_back.setOnClickListener {
            finish()
        }
        refresh.setOnRefreshListener {
            getXfry()
        }
        refresh.autoRefresh()

        pic_add.setOnClickListener { startActivity<AddVisitActivity>() }
        calendarView.setSelectedDate(Date())
        date = getTime(calendarView.selectedDate)
        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(p0: MaterialCalendarView, p1: CalendarDay, p2: Boolean) {
                date = getTime(p1)
                getXfry()
            }
        })

    }

    private fun getTime(date: CalendarDay): String {
        val year = date.year
        val month = date.month + 1 //月份跟系统一样是从0开始的，实际获取时要加1
        val day = date.day
        return year.toString() + "-" + month + "-" + day
    }

    private fun getXfry() {
        val subscriber = object : Subscriber<MutableList<Visit>>() {
            override fun onNext(t: MutableList<Visit>?) {
                val data = t!!
                refresh.finishRefresh()
                list.adapter = VisitAdapter(this@VisitActivity, data)
                list.setOnItemClickListener {
                    startActivity<VisitDetailsActivity>("id" to data[it].id)
                }

            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh()
                refresh.finishLoadMore()
                Snackbar.make(refresh, "刷新失败，请重新尝试", Snackbar.LENGTH_SHORT).show()
            }

        }
        HttpUtil.getInstance().getXfry(subscriber, date, app.grid.id)

//        val subscriberOnNextListener = SubscriberOnNextListener<MutableList<Visit>> {
//            val data = it!!
//            refresh.finishRefresh()
//            list.adapter = VisitAdapter(this@VisitActivity, data)
//            list.setOnItemClickListener {
//                startActivity<VisitDetailsActivity>("id" to data[it].id)
//            }
//
//        }
//        HttpUtil.getInstance().getXfry(ProgressSubscriber<MutableList<Visit>>(subscriberOnNextListener, this, "下载中"), date, app.grid.id)

    }

    inner class VisitAdapter(val context: Context, var data: MutableList<Visit>) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder?
            val view: View
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_visit, parent, false)
                holder = ViewHolder(view)
                view.setTag(holder)
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }

            holder.name.text = data[position].zdry
            if (data[position].swqk == "") {
                holder.morning.setTextColor(this@VisitActivity.getResources().getColor(R.color.refresh_green))
            }
            if (data[position].xwqk == "") {
                holder.afternoon.setTextColor(this@VisitActivity.getResources().getColor(R.color.refresh_green))

            }
            return view
        }

        override fun getItem(position: Int): Any {
            return data[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return data.size
        }

        inner class ViewHolder(view: View) {
            var name = view.findViewById(R.id.name) as TextView
            var morning = view.findViewById(R.id.morning) as TextView
            var afternoon = view.findViewById(R.id.afternoon) as TextView
        }

    }
}
