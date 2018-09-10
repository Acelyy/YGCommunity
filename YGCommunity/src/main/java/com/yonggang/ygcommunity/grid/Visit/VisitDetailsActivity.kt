package com.yonggang.ygcommunity.grid.Visit

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.DatePicker
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.GridStatus
import com.yonggang.ygcommunity.Entry.VisitDetail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.NoDoubleClickListener
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_visit_details.*
import org.jetbrains.anko.find
import rx.Subscriber
import java.text.SimpleDateFormat
import java.util.*

class VisitDetailsActivity : BaseActivity() {
    private lateinit var app: YGApplication
    private lateinit var id: String
    private var dialog: AlertDialog? = null
    private lateinit var data: GridStatus
    private lateinit var listdata: MutableList<String>
    var sBuffer = StringBuffer()
    var listtype: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_details)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }
        app = application as YGApplication
        id = intent.getStringExtra("id")
//        initDatePicker()
        getchoose()
        submit.setOnClickListener(object : NoDoubleClickListener() {
            override fun onNoDoubleClick(v: View) {
                for (item in data.sjfl) {
                    if (item.selection) {
                        sBuffer.append(item.id + ",")
                    }
                }
                if (sBuffer.length > 0) {
                    listtype = sBuffer.substring(0, sBuffer.length - 1)
                }
                upXfryDetails()
            }
        });
    }

    //时间选择
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

    //获取多选框
    private fun getchoose() {
        val subscriberOnNextListener = SubscriberOnNextListener<GridStatus> {
//            getXfryDetilas()
            data = it!!
            //获取数据
            val subscriber = object : Subscriber<VisitDetail>() {
                override fun onNext(t: VisitDetail?) {
                    if (t != null) {
                        date.text = t.pcsj
                        person.text = Editable.Factory.getInstance().newEditable(t.zdry)
                        telephone.text = Editable.Factory.getInstance().newEditable(t.telephone)
                        number.text = Editable.Factory.getInstance().newEditable(t.sjrs)
                        measures.text = Editable.Factory.getInstance().newEditable(t.wkcs)
                        morning.text = Editable.Factory.getInstance().newEditable(t.wkcs)
                        afternoon.text = Editable.Factory.getInstance().newEditable(t.xwqk)
                        comment.text = Editable.Factory.getInstance().newEditable(t.comment)
                        listdata = t.mdlx
                        for (i in t.mdlx) {
                            data.sjfl[i.toInt()].selection = true
                        }
                        val myadapter = myAdapter(data.sjfl, this@VisitDetailsActivity)
                        list.adapter = myadapter
                    }
                }

                override fun onCompleted() {
                }

                override fun onError(e: Throwable?) {
                    Log.i("error", e.toString())
                }

            }
            HttpUtil.getInstance().getXfryDetails(subscriber, id)

            list.setOnItemClickListener {
                data.sjfl[it].selection = !data.sjfl[it].selection
                list.update()
            }
        }
        HttpUtil.getInstance().getEventStatus(ProgressSubscriber<GridStatus>(subscriberOnNextListener, this, "加载中"))
    }

//    //获取
//    private fun getXfryDetilas() {
//        val subscriber = object : Subscriber<VisitDetail>() {
//            override fun onNext(t: VisitDetail?) {
//                if (t != null) {
//                    date.text = t.pcsj
//                    person.text = Editable.Factory.getInstance().newEditable(t.zdry)
//                    telephone.text = Editable.Factory.getInstance().newEditable(t.telephone)
//                    number.text = Editable.Factory.getInstance().newEditable(t.sjrs)
//                    measures.text = Editable.Factory.getInstance().newEditable(t.wkcs)
//                    morning.text = Editable.Factory.getInstance().newEditable(t.wkcs)
//                    afternoon.text = Editable.Factory.getInstance().newEditable(t.xwqk)
//                    comment.text = Editable.Factory.getInstance().newEditable(t.comment)
//                    listdata = t.mdlx
//                }
//            }
//
//            override fun onCompleted() {
//                val subscriberOnNextListener = SubscriberOnNextListener<GridStatus> {
//                    data = it!!
//                    for (i in listdata) {
//                        data.sjfl[i.toInt()].selection = true
//                    }
//                    val myadapter = myAdapter(data.sjfl, this@VisitDetailsActivity)
//                    list.adapter = myadapter
//                    list.setOnItemClickListener {
//                        data.sjfl[it].selection = !data.sjfl[it].selection
//                        list.update()
//                    }
//                }
//                HttpUtil.getInstance().getEventStatus(ProgressSubscriber<GridStatus>(subscriberOnNextListener, this@VisitDetailsActivity, "加载中"))
//            }
//
//            override fun onError(e: Throwable?) {
//                Log.i("error", e.toString())
//            }
//
//        }
//        HttpUtil.getInstance().getXfryDetails(subscriber, id)
//    }

    //更新
    private fun upXfryDetails() {
        if (date.text.toString() == "") {
            Snackbar.make(submit, "请选择日期", Snackbar.LENGTH_LONG).show()
            return
        }
        if (person.text.toString() == "") {
            Snackbar.make(submit, "请填写重访人员", Snackbar.LENGTH_LONG).show()
            return
        }
        if (telephone.text.toString() == "") {
            Snackbar.make(submit, "请填写联系电话", Snackbar.LENGTH_LONG).show()
            return
        }
        if (number.text.toString() == "") {
            Snackbar.make(submit, "请填写涉及人数", Snackbar.LENGTH_LONG).show()
            return
        }
        if (measures.text.toString() == "") {
            Snackbar.make(submit, "请填写稳控措施", Snackbar.LENGTH_LONG).show()
            return
        }
        if (morning.text.toString() == "") {
            Snackbar.make(submit, "请填写上午见面情况", Snackbar.LENGTH_LONG).show()
            return
        }

        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            Log.i("upVisit", it)
            Snackbar.make(submit, "上报成功", Snackbar.LENGTH_LONG).show()
            finish()
        }
        HttpUtil.getInstance().upXfryDetails(ProgressSubscriber<String>(subscriberOnNextListener, this@VisitDetailsActivity, "上报中"),
                id,
                date.text.toString().trim(),
                person.text.toString().trim(),
                app.grid.sswg,
                listtype,
                number.text.toString().trim(),
                measures.text.toString().trim(),
                app.grid.id,
                morning.text.toString().trim(),
                afternoon.text.toString().trim(),
                telephone.text.toString().trim(),
                comment.text.toString().trim()
        )
    }

    class myAdapter(var data: MutableList<GridStatus.Bean>, val context: Context) : BaseAdapter() {
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
