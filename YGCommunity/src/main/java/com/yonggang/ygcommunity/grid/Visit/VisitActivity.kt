package com.yonggang.ygcommunity.grid.Visit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_visit.*
import org.jetbrains.anko.startActivity
import com.iflytek.cloud.resource.Resource.setText
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.umeng.socialize.utils.DeviceConfig.context
import com.yonggang.ygcommunity.grid.folk.adapter.FolkAdapter
import com.yonggang.ygcommunity.grid.house.SelectHouseActivity
import kotlinx.android.synthetic.main.activity_event_list.view.*
import org.jetbrains.anko.find
import java.util.*
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener


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
        Log.i("date",getTime(calendarView.selectedDate))
        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(p0: MaterialCalendarView, p1: CalendarDay, p2: Boolean) {
                Log.i("date",getTime(p1))
            }
        })

    }
    private fun getTime(date:CalendarDay):String{
        val year = date.year;
        val month = date.month + 1; //月份跟系统一样是从0开始的，实际获取时要加1
        val day = date.day;
        return year.toString() + "-" + month + "-" + day
    }
//    class VisitAdapter : BaseAdapter(val data,val context: Contenxt) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            var holder: ViewHolder
//            val view: View
//            if (convertView == null) {
//                view = LayoutInflater.from(context).inflate(R.layout.item_visit, parent, false)
//                holder = ViewHolder(view)
//                view.setTag(holder)
//            } else {
//                view = convertView
//                holder = view.tag as ViewHolder
//            }
//            return view
//        }
//
//        override fun getItem(p0: Int): Any {
//            return data[p0]
//        }
//
//        override fun getItemId(p0: Int): Long {
//            return p0.toLong()
//        }
//
//        override fun getCount(): Int {
//            return data.size
//        }
//
//        inner class ViewHolder(var view: View){
//            var name:TextView = view.find(R.id.name)
//            var title:TextView = view.find(R.id.title)
//            var morning:TextView = view.find(R.id.morning)
//            var afternoon:TextView = view.find(R.id.afternoon)
//        }
//    }
}
