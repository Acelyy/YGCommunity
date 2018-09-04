package com.yonggang.ygcommunity.grid.Visit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
import android.view.LayoutInflater
import android.widget.TextView
import com.umeng.socialize.utils.DeviceConfig.context
import com.yonggang.ygcommunity.grid.folk.adapter.FolkAdapter
import com.yonggang.ygcommunity.grid.house.SelectHouseActivity
import kotlinx.android.synthetic.main.activity_event_list.view.*
import org.jetbrains.anko.find
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

    }

//    class VisitAdapter : BaseAdapter() {
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
