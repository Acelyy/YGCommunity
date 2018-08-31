package com.yonggang.ygcommunity.grid.folk

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.alibaba.fastjson.JSON
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Event
import com.yonggang.ygcommunity.Entry.Folk
import com.yonggang.ygcommunity.Entry.HouseFamily
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.grid.event.EventDetailActivity
import com.yonggang.ygcommunity.grid.folk.adapter.FolkAdapter
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import com.yonggang.ygcommunity.monitor.adapter.EventAdapter
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_folk.*
import org.jetbrains.anko.startActivity
import rx.Subscriber

class FolkActivity : BaseActivity() {

    lateinit private var list_data: MutableList<Folk>;

    private lateinit var adapter: FolkAdapter
    private var index = -1
    lateinit private var list_folk: ListView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folk)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        list_folk = findViewById(R.id.list_folk) as ListView
        pic_back.setOnClickListener{
            finish()
        }
        refresh.setOnRefreshListener{getFolkList(1)}
        refresh.setOnLoadMoreListener {
            if (adapter.count % 10 == 0) {
                getFolkList(adapter.count / 10 + 1)
            } else {
                refresh.finishLoadMore()
            }
        }
        refresh.autoRefresh()
        pic_add.setOnClickListener{
            startActivity<AddFolkActivity>()
        }
    }

    private fun getFolkList(page: Int){
        val subscriber = object : Subscriber<MutableList<Folk>>(){
            val status = if (index == 9) {
                10
            } else {
                index
            }

            override fun onNext(t: MutableList<Folk>?) {
                Log.i("data",t.toString())
                refresh.finishRefresh()
                refresh.finishLoadMore()
                val data = t!!;
                if (page == 1) {
                    list_data = data
                    adapter = FolkAdapter(list_data, this@FolkActivity)
                    list_folk.adapter = adapter
                    list_folk.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        this@FolkActivity.startActivity<FolkDetailsActivity>("id" to list_data[position].id)
                    }
                } else {
                    list_data.addAll(data)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("Error",e.toString())
                refresh.finishRefresh()
                refresh.finishLoadMore()
                Snackbar.make(refresh, "刷新失败，请重新尝试", Snackbar.LENGTH_SHORT).show()
            }
        }
        HttpUtil.getInstance().getFolkList(subscriber,page)
    }
}


