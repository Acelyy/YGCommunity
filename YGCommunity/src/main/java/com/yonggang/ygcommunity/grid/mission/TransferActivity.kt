package com.yonggang.ygcommunity.grid.mission

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Assignor
import com.yonggang.ygcommunity.Entry.Depart
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_transfer.*
import org.jetbrains.anko.find
import rx.Subscriber

class TransferActivity : BaseActivity() {
    private lateinit var app: YGApplication
    private lateinit var id: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transfer)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        id = intent.getStringExtra("id")
        refresh.setOnRefreshListener {
            when (intent.getIntExtra("status", 0)) {
                2 -> getDepartList()
                5 -> getAssignorList()
            }
        }
        tv_title.text = when (intent.getIntExtra("status", 0)) {
            2 -> "请选择指派的部门"
            5 -> "请选择指派的人"
            else -> ""
        }

        refresh.autoRefresh()
        text_back.setOnClickListener { finish() }
        pic_back.setOnClickListener { finish() }
    }

    /**
     * 获取部门列表
     */
    private fun getDepartList() {
        val subscriber = object : Subscriber<List<Depart>>() {
            override fun onNext(data: List<Depart>?) {
                val adapter = DepartAdapter(data!!, LayoutInflater.from(this@TransferActivity))
                list_transfer.adapter = adapter
                list_transfer.setOnItemClickListener { parent, view, position, id ->
                    Log.i("position", "" + position)
                    data[position].isSelect = !data[position].isSelect
                    adapter.notifyDataSetChanged()
                }
                submit.setOnClickListener {
                    var msg = StringBuffer()
                    var ids = StringBuffer()
                    for (bean in data) {
                        if (bean.isSelect) {
                            msg = msg.append(bean.bname + "、")
                            ids = ids.append(bean.rid + ",")
                        }
                    }

                    if (ids.isEmpty()) {
                        Snackbar.make(submit, "指派部门不能为空", Snackbar.LENGTH_LONG).show()
                        return@setOnClickListener
                    } else {
                        Log.i("length", "" + msg.length)
                        msg = StringBuffer(msg.substring(0, msg.length - 1))
                        ids = StringBuffer(ids.substring(0, ids.length - 1))
                    }
                    val view = LayoutInflater.from(this@TransferActivity).inflate(R.layout.item_input,null)
                    val input=view.find<EditText>(R.id.input)
                    val builder = AlertDialog.Builder(this@TransferActivity)
                    builder.setTitle("请确认指派的部门")
                            .setView(view)
                            .setMessage(msg)
                            .setPositiveButton("确定") { _, _ ->
                                transfer(id, ids.toString(), input.text.toString())
                            }.setNegativeButton("取消") { _, _ -> }
                            .create().show()
                }
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                Snackbar.make(refresh, "获取失败，请稍后重新尝试", Snackbar.LENGTH_LONG).show()
            }
        }
        HttpUtil.getInstance().getDepartList(subscriber)
    }

    /**
     * 获取指派人列表
     */
    private fun getAssignorList() {
        val subscriber = object : Subscriber<List<Assignor>>() {
            override fun onNext(data: List<Assignor>?) {
                val adapter = AssignorAdapter(data!!, LayoutInflater.from(this@TransferActivity))
                list_transfer.adapter = adapter
                refresh.finishRefresh()
                list_transfer.setOnItemClickListener { parent, view, position, id ->
                    Log.i("position", "" + position)
                    data[position].isSelect = !data[position].isSelect
                    adapter.notifyDataSetChanged()
                }

                submit.setOnClickListener {
                    var msg = StringBuffer()
                    var ids = StringBuffer()
                    for (bean in data) {
                        if (bean.isSelect) {
                            msg = msg.append(bean.name + "、")
                            ids = ids.append(bean.id + ",")
                        }
                    }

                    if (ids.isEmpty()) {
                        Snackbar.make(submit, "指派人不能为空", Snackbar.LENGTH_LONG).show()
                        return@setOnClickListener
                    } else {
                        Log.i("length", "" + msg.length)
                        msg = StringBuffer(msg.substring(0, msg.length - 1))
                        ids = StringBuffer(ids.substring(0, ids.length - 1))
                    }
                    val view = LayoutInflater.from(this@TransferActivity).inflate(R.layout.item_input,null)
                    val input=view.find<EditText>(R.id.input)
                    val builder = AlertDialog.Builder(this@TransferActivity)
                    builder.setTitle("请确认指派的人")
                            .setView(view)
                            .setMessage(msg)
                            .setPositiveButton("确定") { _, _ ->
                                assignor(id, ids.toString(), input.text.toString())
                            }.setNegativeButton("取消") { _, _ -> }
                            .create().show()
                }
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                Snackbar.make(refresh, "获取失败，请稍后重新尝试", Snackbar.LENGTH_LONG).show()
            }
        }
        HttpUtil.getInstance().getAssignorList(subscriber, app.grid.id)
    }

    class DepartAdapter(var data: List<Depart>, var inflater: LayoutInflater) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_mission_select, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.name.text = data[position].bname
            holder.select.isChecked = data[position].isSelect
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
    }

    class AssignorAdapter(var data: List<Assignor>, var inflater: LayoutInflater) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder
            if (convertView == null) {
                view = inflater.inflate(R.layout.item_mission_select, parent, false)
                holder = ViewHolder(view)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.name.text = data[position].name
            holder.select.isChecked = data[position].isSelect
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
    }

    class ViewHolder(view: View) {
        var name: TextView = view.find(R.id.name)
        var select: CheckBox = view.find(R.id.select)
    }

    /**
     * 转派部门任务
     */
    private fun transfer(id: String, ids: String, comment: String) {
        if(comment.isEmpty()){
            Snackbar.make(pic_back,"意见不能为空",Snackbar.LENGTH_LONG).show()
            return
        }
        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            Log.i("transfer", it)
            finish()
        }
        HttpUtil.getInstance().transfer(ProgressSubscriber<String>(subscriberOnNextListener, this, "指派中"), id, ids, comment)
    }

    /**
     * 指派任务
     */
    private fun assignor(id: String, ids: String, comment: String) {
        if(comment.isEmpty()){
            Snackbar.make(pic_back,"意见不能为空",Snackbar.LENGTH_LONG).show()
            return
        }
        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            Log.i("assignor", it)
            finish()
        }
        HttpUtil.getInstance().assignor(ProgressSubscriber<String>(subscriberOnNextListener, this, "指派中"), app.grid.id, id, ids, comment)
    }
}
