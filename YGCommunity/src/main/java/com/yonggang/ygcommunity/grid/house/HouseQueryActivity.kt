package com.yonggang.ygcommunity.grid.house

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.HouseInfo
import com.yonggang.ygcommunity.Entry.HouseQuery
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_house_query.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber

class HouseQueryActivity : BaseActivity() {
    private lateinit var adapter: MyAdapter
    private var nametext:String = ""
    private var phonetext:String = ""
    private var numbertext:String = ""
    private var cartext:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_query)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
        query.setOnClickListener {
            val view = LayoutInflater.from(this@HouseQueryActivity).inflate(R.layout.house_query,null)
            val name=view.find<EditText>(R.id.name)
            val phone=view.find<EditText>(R.id.phone)
            val car=view.find<EditText>(R.id.car)
            val number=view.find<EditText>(R.id.number)
            val builder = AlertDialog.Builder(this@HouseQueryActivity)
            builder.setView(view)
                    .setPositiveButton("确定") { _, _ ->
                        nametext = name.text.toString().trim()
                        phonetext = phone.text.toString().trim()
                        numbertext = number.text.toString().trim()
                        cartext = car.text.toString().trim()
                        getHouseQuery(nametext,phonetext,numbertext,cartext,1)
                    }.setNegativeButton("取消") { _, _ -> }
                    .create().show()
        }
        refresh.setOnRefreshListener {
            getHouseQuery(nametext,phonetext,numbertext,cartext,1)
        }
        refresh.autoRefresh()
        refresh.setOnLoadMoreListener {
            if (adapter.count % 10 == 0) {
                getHouseQuery(nametext,phonetext,numbertext,cartext,adapter.count / 10 + 1)
            } else {
                refresh.finishLoadMore()
            }
        }
    }
    private fun getHouseQuery(name:String,phone:String,number:String,car:String,page:Int){
        val subscriber = object:Subscriber<MutableList<HouseQuery>>(){
            override fun onNext(t: MutableList<HouseQuery>?) {
                refresh.finishRefresh()
                refresh.finishLoadMore()
                adapter = MyAdapter(t!!,this@HouseQueryActivity)
                list.adapter = adapter
                list.setOnItemClickListener { parent, view, position, id ->
                    this@HouseQueryActivity.startActivity<HouseInfoActivity>("sfzh" to t[position].sfzh)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error",e.toString())
            }

        }
        HttpUtil.getInstance().getHouseQuery(subscriber,name,phone,number,car,page)
    }

    class MyAdapter(var data: MutableList<HouseQuery>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.itme_house_query, parent, false);
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.name.text = data[position].xm
            holder.number.text = data[position].sfzh
            holder.address.text = data[position].xb
            if (data[position].xb == "男") {
                holder.sex?.setImageResource(R.mipmap.pic_house_man)
            } else {
                holder.sex?.setImageResource(R.mipmap.pic_house_woman)
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
            val name = view.findViewById(R.id.name) as TextView
            val number = view.findViewById(R.id.number) as TextView
            val address = view.findViewById(R.id.address) as TextView
            val sex = view.findViewById(R.id.sex) as ImageView
        }
    }
}
