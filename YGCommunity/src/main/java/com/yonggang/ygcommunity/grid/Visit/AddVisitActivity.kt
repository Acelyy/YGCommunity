package com.yonggang.ygcommunity.grid.Visit

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
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
import java.text.SimpleDateFormat
import java.util.*
import com.yonggang.ygcommunity.Util.NoDoubleClickListener
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener


class AddVisitActivity : BaseActivity() {
    private var dialog: AlertDialog? = null
    private lateinit var data: GridStatus
    var sBuffer = StringBuffer()
    var listtype: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_visit)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        getchoose()
        initDatePicker()
        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }

        submit.setOnClickListener(object : NoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                for (item in data.sjfl) {
                    if (item.selection) {
                        sBuffer.append(item.name + ",")
                    }
                }
                if (sBuffer.length > 0) {
                    listtype = sBuffer.substring(0, sBuffer.length - 1)
                }

                Log.i("click", "再按一次退出程序")
            }
        });
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

//    private fun getchoose() {
//        val subscriber = object : Subscriber<GridStatus>() {
//            override fun onNext(it: GridStatus?) {
//                data = it!!
//                var myadapter = AddVisitAdapter(data.sjfl, this@AddVisitActivity)
//                list.adapter = myadapter
//                list.setOnItemClickListener {
//                    data.sjfl[it].selection = !data.sjfl[it].selection
//                    Log.i("hhh","hhhhhhhhhhhhhh")
//                    Log.i("selection",data.sjfl[it].selection.toString())
//                    list.update()
//                }
//            }
//
//            override fun onCompleted() {
//
//            }
//
//            override fun onError(e: Throwable?) {
//                Log.i("visiterror", e.toString())
//            }
//        }
//        HttpUtil.getInstance().getEventStatus(subscriber)
//    }
    /**
     * 获取事件上报的筛选条件
     */
    private fun getchoose() {
        val subscriberOnNextListener = SubscriberOnNextListener<GridStatus> {
            data = it!!
            val myadapter = AddVisitAdapter(data.sjfl, this@AddVisitActivity)
            list.adapter = myadapter
            list.setOnItemClickListener {
                data.sjfl[it].selection = !data.sjfl[it].selection
                list.update()
            }
        }
        HttpUtil.getInstance().getEventStatus(ProgressSubscriber<GridStatus>(subscriberOnNextListener, this, "加载中"))
    }


    class AddVisitAdapter(var data: MutableList<GridStatus.Bean>, val context: Context) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder
            val view: View
            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_add_visit, parent, false)
                holder = ViewHolder(view)
                view.setTag(holder)
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.check.text = data[position].name
            holder.check.isChecked = data[position].selection
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
            var check: CheckBox = view.find(R.id.check)
        }

    }
}
