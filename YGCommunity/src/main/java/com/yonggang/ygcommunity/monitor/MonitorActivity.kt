package com.yonggang.ygcommunity.monitor

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import com.alibaba.fastjson.JSON
import com.daimajia.numberprogressbar.NumberProgressBar
import com.umeng.socialize.utils.Log
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.monitor.model.MonitorModel
import kotlinx.android.synthetic.main.activity_monitor.*
import org.jetbrains.anko.startActivity
import rx.Observable
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class MonitorActivity : BaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_monitor)
        img_finish.setOnClickListener(this)

        layout_all.setOnClickListener(this)
        all_num_sum.setOnClickListener(this)
        all_num_receiver.setOnClickListener(this)
        all_num_doing.setOnClickListener(this)
        all_num_done.setOnClickListener(this)
        all_num_undo.setOnClickListener(this)

        layout_grid.setOnClickListener(this)
        grid_num_sum.setOnClickListener(this)
        grid_num_receiver.setOnClickListener(this)
        grid_num_doing.setOnClickListener(this)
        grid_num_done.setOnClickListener(this)
        grid_num_undo.setOnClickListener(this)

        layout_assembly.setOnClickListener(this)
        assembly_num_sum.setOnClickListener(this)
        assembly_num_receiver.setOnClickListener(this)
        assembly_num_doing.setOnClickListener(this)
        assembly_num_done.setOnClickListener(this)
        assembly_num_undo.setOnClickListener(this)

        layout_tell.setOnClickListener(this)
        tell_num_sum.setOnClickListener(this)
        tell_num_receiver.setOnClickListener(this)
        tell_num_doing.setOnClickListener(this)
        tell_num_done.setOnClickListener(this)
        tell_num_undo.setOnClickListener(this)

        refresh.isEnableLoadMore = false
        refresh.setOnRefreshListener {
            getCount()
        }
        refresh.autoRefresh()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.img_finish -> finish()

            R.id.layout_all -> startActivity<EventListActivity>("title" to "总体工作情况", "index" to 0, "type" to 0)
            R.id.all_num_sum -> startActivity<EventListActivity>("title" to "总体工作情况", "index" to 0, "type" to 0)
            R.id.all_num_receiver -> startActivity<EventListActivity>("title" to "总体工作情况", "index" to 1, "type" to 0)
            R.id.all_num_doing -> startActivity<EventListActivity>("title" to "总体工作情况", "index" to 2, "type" to 0)
            R.id.all_num_done -> startActivity<EventListActivity>("title" to "总体工作情况", "index" to 3, "type" to 0)
            R.id.all_num_undo -> startActivity<EventListActivity>("title" to "总体工作情况", "index" to 4, "type" to 0)

            R.id.layout_grid -> startActivity<EventListActivity>("title" to "网格化事件上报", "index" to 0, "type" to 1)
            R.id.grid_num_sum -> startActivity<EventListActivity>("title" to "网格化事件上报", "index" to 0, "type" to 1)
            R.id.grid_num_receiver -> startActivity<EventListActivity>("title" to "网格化事件上报", "index" to 1, "type" to 1)
            R.id.grid_num_doing -> startActivity<EventListActivity>("title" to "网格化事件上报", "index" to 2, "type" to 1)
            R.id.grid_num_done -> startActivity<EventListActivity>("title" to "网格化事件上报", "index" to 3, "type" to 1)
            R.id.grid_num_undo -> startActivity<EventListActivity>("title" to "网格化事件上报", "index" to 4, "type" to 1)

            R.id.layout_assembly -> startActivity<EventListActivity>("title" to "议事厅反应问题", "index" to 0, "type" to 2)
            R.id.assembly_num_sum -> startActivity<EventListActivity>("title" to "议事厅反应问题", "index" to 0, "type" to 2)
            R.id.assembly_num_receiver -> startActivity<EventListActivity>("title" to "议事厅反应问题", "index" to 1, "type" to 2)
            R.id.assembly_num_doing -> startActivity<EventListActivity>("title" to "议事厅反应问题", "index" to 2, "type" to 2)
            R.id.assembly_num_done -> startActivity<EventListActivity>("title" to "议事厅反应问题", "index" to 3, "type" to 2)
            R.id.assembly_num_undo -> startActivity<EventListActivity>("title" to "议事厅反应问题", "index" to 4, "type" to 2)

            R.id.layout_tell -> startActivity<EventListActivity>("title" to "400热线工单", "index" to 0, "type" to 3)
            R.id.tell_num_sum -> startActivity<EventListActivity>("title" to "400热线工单", "index" to 0, "type" to 3)
            R.id.tell_num_receiver -> startActivity<EventListActivity>("title" to "400热线工单", "index" to 1, "type" to 3)
            R.id.tell_num_doing -> startActivity<EventListActivity>("title" to "400热线工单", "index" to 2, "type" to 3)
            R.id.tell_num_done -> startActivity<EventListActivity>("title" to "400热线工单", "index" to 3, "type" to 3)
            R.id.tell_num_undo -> startActivity<EventListActivity>("title" to "400热线工单", "index" to 4, "type" to 3)
        }
    }

    /**
     * 获取事件数量
     */
    private fun getCount() {
        val subscriber = object : Subscriber<MonitorModel.GridCount>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh(false)
                Snackbar.make(refresh, "刷新失败，请重新尝试", Snackbar.LENGTH_SHORT)
            }

            override fun onNext(data: MonitorModel.GridCount?) {
                Log.i("getCount", JSON.toJSONString(data))
                refresh.finishRefresh()
                setUI(data)
            }
        }
        HttpUtil.getInstance().getCount(subscriber)
    }

    /**
     * 根据4个接口返回的数据，统一刷新UI
     */
    private fun setUI(data: MonitorModel.GridCount?) {
        if (data != null) {
            // 设置总体的UI
            val total = data.total!!
            all_num_sum.withNumber(total.sbm).start()
            all_num_receiver.withNumber(total.ysl).start()
            all_num_doing.withNumber(total.blz).start()
            all_num_done.withNumber(total.bjl).start()
            all_num_undo.withNumber(total.wbj).start()
            all_text.text = "(${total.bjl}/${total.sbm})"
            getProgress(progress_all, total.bjl, total.sbm)

            val grid = data.gird!!
            grid_num_sum.withNumber(grid.sbm).start()
            grid_num_receiver.withNumber(grid.ysl).start()
            grid_num_doing.withNumber(grid.blz).start()
            grid_num_done.withNumber(grid.bjl).start()
            grid_num_undo.withNumber(grid.wbj).start()
            grid_text.text = "(${grid.bjl}/${grid.sbm})"
            getProgress(progress_grid, grid.bjl, grid.sbm)

            val bbs = data.bbs!!
            assembly_num_sum.withNumber(bbs.sbm).start()
            assembly_num_receiver.withNumber(bbs.ysl).start()
            assembly_num_doing.withNumber(bbs.blz).start()
            assembly_num_done.withNumber(bbs.bjl).start()
            assembly_num_undo.withNumber(bbs.wbj).start()
            assembly_text.text = "(${bbs.bjl}/${bbs.sbm})"
            getProgress(progress_assembly, bbs.bjl, bbs.sbm)

            val phone = data.phone!!
            tell_num_sum.withNumber(phone.sbm).start()
            tell_num_receiver.withNumber(phone.ysl).start()
            tell_num_doing.withNumber(phone.blz).start()
            tell_num_done.withNumber(phone.bjl).start()
            tell_num_undo.withNumber(phone.wbj).start()
            tell_text.text = "(${phone.bjl}/${phone.sbm})"
            getProgress(progress_tell, phone.bjl, phone.sbm)
        }
    }

    /**
     * 将进度条缓慢从0涨到办结量
     */
    private fun getProgress(progress: NumberProgressBar, current: Int, max: Int) {
        progress.max = 100
        val pro = if (max == 0) {
            0
        } else {
            current * 100 / max
        }
        val observable = Observable
                .interval(0, 10, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .take(pro + 1)

        val subscriber = object : Subscriber<Long>() {
            override fun onNext(it: Long) {
                Log.i("onNext", "" + it)
                progress.progress = it.toInt()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }
        }
        observable.subscribe(subscriber)
    }
}
