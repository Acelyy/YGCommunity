package com.yonggang.ygcommunity.grid.event

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.yonggang.ygcommunity.Entry.GridEvent
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.grid.event.adapter.EventAdapter
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber

public class EventFragment : Fragment() {
    private lateinit var listEvent: ListView
    private lateinit var refresh: SmartRefreshLayout

    private lateinit var onTitleChangeListener: EventActivity.OnTitleChangeListener

    private var index = -1

    private lateinit var app: YGApplication

    lateinit var list_data: MutableList<GridEvent.DataBean>

    private lateinit var adapter: EventAdapter

    companion object {
        fun getInstance(index: Int, onTitleChangeListener: EventActivity.OnTitleChangeListener): EventFragment {
            val f = EventFragment()
            f.onTitleChangeListener = onTitleChangeListener
            f.arguments = bundleOf("index" to index)
            return f
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity.application as YGApplication
        index = arguments.getInt("index")
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val itemView = inflater?.inflate(R.layout.layout, container, false)
        listEvent = itemView!!.find(R.id.list_event)
        refresh = itemView.find(R.id.refresh)
        refresh.setOnRefreshListener { getEventList(1) }
        refresh.setOnLoadMoreListener {
            if (adapter.count % 10 == 0) {
                getEventList(adapter.count / 10 + 1)
            } else {
                refresh.finishLoadMore()
            }
        }
        refresh.autoRefresh()

        return itemView
    }

    /**
     * 获取事件列表
     */
    private fun getEventList(page: Int) {
        val status = if (index == 9) {
            10
        } else {
            index
        }

        val subscriber = object : Subscriber<GridEvent>() {
            override fun onNext(data: GridEvent?) {
                refresh.finishRefresh()
                refresh.finishLoadMore()

                onTitleChangeListener.onTitleChange(index, data!!.total)

                if (page == 1) {
                    list_data = data.data
                    adapter = EventAdapter(list_data, activity)
                    listEvent.adapter = adapter
                    listEvent.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        activity.startActivity<EventDetailActivity>("id" to list_data[position].id, "is_draft" to (index == 0))
                    }
                } else {
                    list_data.addAll(data.data)
                    adapter.notifyDataSetChanged()
                }
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
        HttpUtil.getInstance().getGridEvent(subscriber, app.grid.id, "" + status, page)
    }
}