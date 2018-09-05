package com.yonggang.ygcommunity.grid.mission

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.MissionBean
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.grid.check.MissionDetailActivity
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_mission_list.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber

class MissionListActivity : BaseActivity() {

    private lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mission_list)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener {
            finish()
        }
        text_back.setOnClickListener {
            finish()
        }
        app = application as YGApplication
        refresh.setOnRefreshListener {
            getMissionList()
        }
        refresh.autoRefresh()
    }

    /**
     * 获取工单核查列表
     */
    private fun getMissionList() {
        val subscriber = object : Subscriber<List<MissionBean>>() {
            override fun onNext(data: List<MissionBean>?) {
                list.adapter = MissionAdapter(data!!, this@MissionListActivity)
                list.setOnItemClickListener { _, _, position, _ ->
                    startActivity<MissionDetailActivity>("id" to data[position].id)
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("getMissionList", e.toString())
                refresh.finishRefresh()
            }
        }

        HttpUtil.getInstance().getMissionList(subscriber, app.grid.appauth, app.grid.id)
    }

    class MissionAdapter(var data: List<MissionBean>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder
            val view: View
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_folk, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.title.text = data[position].sjbt
            holder.time.text = data[position].sbsj
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

        inner class ViewHolder(var view: View) {
            var title: TextView = view.find(R.id.title)
            var time: TextView = view.find(R.id.time)
        }
    }
}
