package com.yonggang.ygcommunity.grid.house

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.HouseInfo
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_house_list.*
import org.jetbrains.anko.startActivity
import rx.Subscriber

class HouseListActivity : BaseActivity() {

    private lateinit var app: YGApplication
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_list)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        text_back.setOnClickListener { finish() }
        pic_back.setOnClickListener { finish() }
        refresh.setOnRefreshListener {
            getHouseList(1)
        }
        refresh.autoRefresh()
        refresh.setOnLoadMoreListener {
            if (adapter.count % 10 == 0) {
                getHouseList(adapter.count / 10 + 1)
            } else {
                refresh.finishLoadMore()
            }
        }
        app = application as YGApplication
    }

    private fun getHouseList(page:Int) {
        val subscriber = object : Subscriber<MutableList<HouseInfo>>() {
            override fun onNext(t: MutableList<HouseInfo>?) {
                refresh.finishRefresh()
                refresh.finishLoadMore()
                adapter =  MyAdapter(t!!,this@HouseListActivity)
                list.adapter = adapter
                list.setOnItemClickListener { parent, view, position, id ->
                    this@HouseListActivity.startActivity<HouseInfoActivity>("sfzh" to t[position].sfzh)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error",e.toString())
            }

        }
        HttpUtil.getInstance().getHouseList(subscriber,page,app.grid.id)
    }

    class MyAdapter(var data: MutableList<HouseInfo>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_house_list, parent, false);
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.name.text = data[position].xm
            holder.number.text = data[position].sfzh
            holder.address.text = data[position].hjdz
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
            val name = view.findViewById(R.id.name) as TextView
            val number = view.findViewById(R.id.number) as TextView
            val address = view.findViewById(R.id.address) as TextView
        }
    }
}
