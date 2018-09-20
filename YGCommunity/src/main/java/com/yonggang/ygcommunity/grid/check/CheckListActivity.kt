package com.yonggang.ygcommunity.grid.check

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.HcrwList
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_check_list.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber
import java.text.SimpleDateFormat
import java.util.*

class CheckListActivity : BaseActivity() {
    private lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        refresh.setOnRefreshListener {
            getCheckList(1)
        }
        refresh.autoRefresh()
        text_back.setOnClickListener { finish() }
        pic_back.setOnClickListener { finish() }
    }

    private fun getCheckList(page: Int) {
        val subscriber = object : Subscriber<MutableList<HcrwList>>() {
            override fun onNext(t: MutableList<HcrwList>?) {
                list.adapter = CheckAdapter(t!!,this@CheckListActivity)
                list.setOnItemClickListener { parent, view, position, id ->
                    this@CheckListActivity.startActivity<CheckListDetailsActivity>("id" to t[position].id)
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getHcrw(subscriber, page, app.grid.id)
    }

    class CheckAdapter(var data: MutableList<HcrwList>, val context: Context): BaseAdapter() {
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
            var time = data[position].sbsj
            Log.i("time", SimpleDateFormat("yyyy-MM-dd").format(Date(time)))
            holder.time.text = SimpleDateFormat("yyyy-MM-dd").format(Date(time))
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
