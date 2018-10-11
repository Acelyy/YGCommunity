package com.yonggang.ygcommunity.grid.mission

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Trail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_trail.*
import org.jetbrains.anko.find
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
        text_back.setOnClickListener { finish() }
        pic_back.setOnClickListener { finish() }
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
        @SuppressLint("MissingPermission")
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
            if(data[position].phone != null){
                s +="联系电话；"+ data[position].phone + "\n"
                mholder.textinfo.setOnClickListener {
                    val alertview = LayoutInflater.from(context).inflate(R.layout.event_phone,null)
                    val name=alertview.find<TextView>(R.id.name)
                    val phone=alertview.find<TextView>(R.id.telephone)
                    val call=alertview.find<TextView>(R.id.call)
                    name.text = data[position].bpname
                    phone.text = data[position].phone
                    call.setOnClickListener {
                        val telephone = "tel:"+data[position].phone
                        val intent = Intent(Intent.ACTION_CALL, Uri.parse(telephone))
                        context.startActivity(intent)
                    }
                    val builder = AlertDialog.Builder(context)
                    builder.setView(alertview)
                            .create().show()
                }
            }
            if (data[position].comment != null) {
                s += "处理意见：" + data[position].comment + "\n"
            }
            s += "处理状态：" + data[position].status+ "\n"
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
