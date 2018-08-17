package com.yonggang.ygcommunity.monitor.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.alibaba.fastjson.JSON
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yonggang.ygcommunity.Entry.Event
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.monitor.EventListActivity
import com.yonggang.ygcommunity.monitor.MonitorDetailActivity
import com.yonggang.ygcommunity.monitor.adapter.EventAdapter
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber

class EventFragment : Fragment() {
    lateinit var refresh: SmartRefreshLayout
    lateinit var lvEvent: ListView

    lateinit var adapter: EventAdapter

    private var index = -1
    private var type = -1

    lateinit var list_data: MutableList<Event.Data.Bean>

    lateinit var onTitleChangeListener: EventListActivity.OnTitleChangeListener

    private var sTime: String? = null
    private var eTime: String? = null
    private var depId: String? = null

    companion object {
        fun getInstance(index: Int, onTitleChangeListener: EventListActivity.OnTitleChangeListener, type: Int): EventFragment {
            val f = EventFragment()
            val bundle = Bundle()
            f.onTitleChangeListener = onTitleChangeListener
            bundle.putInt("index", index)
            bundle.putInt("type", type)
            f.arguments = bundle
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = arguments.getInt("index")
        type = arguments.getInt("type")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val itemView = inflater?.inflate(R.layout.layout, container, false)
        refresh = itemView?.find(R.id.refresh)!!
        lvEvent = itemView.find(R.id.list_event)
        refresh.setOnRefreshListener { getData(1) }
        refresh.setOnLoadMoreListener {
            if (adapter.count % 10 == 0) {
                getData(adapter.count / 10 + 1)
            } else {
                refresh.finishLoadMore()
            }
        }
        refresh.autoRefresh()
        return itemView
    }

    /**
     * 对Activity开放方法，用于刷新数据
     */
    public fun refresh(type: Int, sTime: String?, eTime: String?, depId: String?) {
        this.sTime = sTime
        this.eTime = eTime
        this.type = type
        this.depId = depId
        refresh.autoRefresh()
    }

    /**
     * 根据type 分发事件
     */
    private fun getData(page: Int) {
        getEventList(page)
    }

    /**
     * 获取Bbs事件列表
     */
    private fun getEventList(page: Int) {
        var status = ""
        when (index) {
            0 -> status = ""
            1 -> status = "1"
            2 -> status = "2"
            3 -> status = "5"
            4 -> status = "9"
        }
        Log.i("params", "kind=$type,status=$status,page=$page,stime=$sTime,etime=$eTime,bmid=$depId")
        val subscriber = object : Subscriber<Event>() {
            override fun onNext(data: Event?) {
                Log.i("getEventList", JSON.toJSONString(data))
                refresh.finishRefresh()
                refresh.finishLoadMore()
                setUI(page, data!!)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh()
                refresh.finishLoadMore()
                Snackbar.make(refresh, "刷新失败，请重新尝试", Snackbar.LENGTH_SHORT).show()
            }
        }
        HttpUtil.getInstance().getEventList(subscriber, type, status, page, sTime, eTime, depId)
    }

    /**
     * 根据接口返回数据，统一刷新UI
     */
    private fun setUI(page: Int, data: Event) {
        var total = 0
        when (index) {
            0 -> total = data.data.count.sbm
            1 -> total = data.data.count.ysl
            2 -> total = data.data.count.blz
            3 -> total = data.data.count.bjl
            4 -> total = data.data.count.wbj
        }
        onTitleChangeListener.onTitleChange(index, total)
        if (page == 1) {
            list_data = data.data.list
            adapter = EventAdapter(list_data, activity)
            lvEvent.adapter = adapter
            lvEvent.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                activity.startActivity<MonitorDetailActivity>("id" to list_data[position].id, "type" to list_data[position].type)
            }
        } else {
            list_data.addAll(data.data.list)
            adapter.notifyDataSetChanged()
        }
    }
}
