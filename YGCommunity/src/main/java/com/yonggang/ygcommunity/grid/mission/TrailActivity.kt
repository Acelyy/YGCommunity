package com.yonggang.ygcommunity.grid.mission

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Trail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_trail.*
import rx.Subscriber

class TrailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        val id = intent.getStringExtra("id")
        refresh.setOnRefreshListener {
            getTrail(id)
        }
        refresh.autoRefresh()
    }

    /**
     * 获取轨迹
     */
    private fun getTrail(id: String) {
        val subscriber = object : Subscriber<List<Trail>>() {
            override fun onNext(t: List<Trail>?) {
                refresh.finishRefresh()
                list_trail.adapter = TrailAdapter(t!!, this@TrailActivity)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getTrail(subscriber, id)
    }


    class TrailAdapter(val data: List<Trail>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val mholder: ViewHolder
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_trail_layout, parent, false)
                mholder = ViewHolder(view)
                view.tag = mholder
            } else {
                view = convertView
                mholder = view.tag as ViewHolder
            }
            var s = ""
            if (data[position].bm != null) {
                s += "部门：" + data[position].bm + "\n"
            }
            if (data[position].bpname != null) {
                s += "处理人：" + data[position].bpname + "\n"
            }
            if (data[position].comment != null) {
                s += "处理意见：" + data[position].comment + "\n"
            }
            s += "处理状态：" + when (data[position].status) {
                0 -> "草稿"
                1 -> "待签收"
                2 -> "已签收"
                3 -> "平台自行处理"
                4 -> "核查通知"
                5 -> "转派部门"
                6 -> "部门签收"
                7 -> "部门处理"
                8 -> "部门完结"
                9 -> "任务完成"
                else -> {
                    ""
                }
            } + "\n"
            if (data[position].time != null) {
                s += "处理时间：" + data[position].time
            }
            mholder.textinfo.text = s
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
            val textinfo = view.findViewById(R.id.info) as TextView
        }
    }

}
