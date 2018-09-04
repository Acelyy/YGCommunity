package com.yonggang.ygcommunity.grid.Visit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.GridStatus
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_add_visit.*
import org.jetbrains.anko.find
import rx.Subscriber
import java.text.SimpleDateFormat
import java.util.*
import com.iflytek.cloud.resource.Resource.setText
import kotlinx.android.synthetic.main.activity_address.view.*
import kotlinx.android.synthetic.main.activity_map_view.*


class AddVisitActivity : BaseActivity() {
    private var dialog: AlertDialog? = null
    val arr: Array<String> = arrayOf()
    val map = TreeMap<String,String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_visit)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }
        initDatePicker()
        list.setLayoutManager(LinearLayoutManager(this));
        getchoose()
    }

    private fun initDatePicker() {
        val view = LayoutInflater.from(this).inflate(R.layout.item_date_picker, null)
        val picker = view.find<DatePicker>(R.id.date)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择时间")
                .setView(view)
                .setPositiveButton("确定") { _, _ ->
                    date.text = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                }.setNegativeButton("取消") { _, _ ->

                }
        if (dialog == null) dialog = builder.create()

        date.setOnClickListener {
            dialog!!.show()
        }

    }

    private fun getchoose() {
        val subscriber = object : Subscriber<GridStatus>() {
            override fun onNext(it: GridStatus?) {
                list.adapter = MyAdapter(it!!.sjfl,this@AddVisitActivity)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("visiterror", e.toString())
            }
        }
        HttpUtil.getInstance().getEventStatus(subscriber)
    }


//    class AddVisitAdapter(var data: MutableList<GridStatus.Bean>, val context: Context) : RecyclerView.Adapter<AddVisitAdapter.ViewHolder>) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            var holder: RecyclerView.ViewHolder
//            val view: View
//            if (convertView == null) {
//                view = LayoutInflater.from(context).inflate(R.layout.item_add_visit, parent, false)
//                holder = ViewHolder(view)
//                view.setTag(holder)
//            } else {
//                view = convertView
//                holder = view.tag as ViewHolder
//            }
//            Log.i("hhh",data.size.toString())
//            holder.checklist.text = data[position].name
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
//        inner class ViewHolder(var view: View): RecyclerView.ViewHolder {
//            var checklist:CheckBox = view.find(R.id.check)
//        }
//    }
    
    inner class MyAdapter(private val data: MutableList<GridStatus.Bean>?, private val context: Context) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        override fun getItemCount(): Int {
            // 返回数据集合大小
            return data?.size ?: 0
        }


        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            //获取这个TextView
            val check = holder.mTvTitle
            var dataName =  data!![position].name
            check.text = dataName
            check.setOnClickListener {
                data[position].selection = !data[position].selection
                if(data[position].selection){
//                    map.add( to data!![position].id)
                }else{

                }
            }
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            var view = ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_add_visit, parent, false))
            return view
        }


        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val mTvTitle: TextView

            init {
                mTvTitle = itemView.findViewById(R.id.check) as TextView
            }

        }

    }
}
