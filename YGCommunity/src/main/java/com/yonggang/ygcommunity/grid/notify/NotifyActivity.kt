package com.yonggang.ygcommunity.grid.notify

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.umeng.socialize.media.Base
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Notify
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_notify.*
import org.jetbrains.anko.startActivity
import rx.Subscriber

class NotifyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        text_back.setOnClickListener { finish() }
        pic_back.setOnClickListener { finish() }
        refresh.setOnRefreshListener {
            getNotify()
        }
        refresh.autoRefresh()
    }

    private fun getNotify() {
        val subscriber = object : Subscriber<MutableList<Notify>>() {
            override fun onNext(t: MutableList<Notify>?) {
                refresh.finishRefresh()
                list.adapter = MyAdapter(t!!,this@NotifyActivity)
                list.setOnItemClickListener { parent, view, position, id ->
                    this@NotifyActivity.startActivity<NotifyDetailsActivity>("id" to t[position].id)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
            }

        }
        HttpUtil.getInstance().getNotify(subscriber)
    }

    class MyAdapter(var data: MutableList<Notify>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view:View
            val holder: ViewHolder
            if(convertView == null){
                view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent,false);
                holder = ViewHolder(view)
                view.tag = holder
            }else{
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.text_title.text = data[position].title
            holder.text_bm.text = data[position].creater
            holder.text_time.text = data[position].stime
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
        inner class ViewHolder(view:View) {
            var text_title = view.findViewById(R.id.text_title) as TextView
            var text_time = view.findViewById(R.id.text_time) as TextView
            var text_bm = view.findViewById(R.id.text_bm) as TextView
        }
    }
}
