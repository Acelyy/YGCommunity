package com.yonggang.ygcommunity.monitor

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.PopupWindow
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Search
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import com.yonggang.ygcommunity.monitor.fragment.EventFragment
import kotlinx.android.synthetic.main.activity_event_list.*
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

class EventListActivity : BaseActivity() {

    private lateinit var onTitleChangeListener: OnTitleChangeListener

    private lateinit var listResource: MutableList<String>
    private lateinit var listDep: List<Search.Dep>

    private var type: Int = 0

    private val titles = mutableListOf<String>()

    private val fragments = mutableListOf<EventFragment>()

    private lateinit var pop: PopupWindow

    private var departIndex = 0 //选择的部门index
    private var sDate: String? = null
    private var eDate: String? = null

    private var dialog_date1: AlertDialog? = null
    private var dialog_date2: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_list)
        img_finish.setOnClickListener { finish() }
        head_title.text = intent.getStringExtra("title")
        type = intent.getIntExtra("type", -1)
        listResource = mutableListOf()
        listResource.add("总体工作情况")
        listResource.add("网格化事件上报")
        listResource.add("议事厅反应问题")
        listResource.add("400热线工单")
        onTitleChangeListener = object : OnTitleChangeListener {
            override fun onTitleChange(index: Int, total: Int) {
                var title = ""
                when (index) {
                    0 -> title = "上报量"
                    1 -> title = "已受理"
                    2 -> title = "处理中"
                    3 -> title = "办结量"
                    4 -> title = "未办结"
                }
                titles[index] = "$title $total"
                pager.adapter.notifyDataSetChanged()
            }
        }
        titles.add("上报量")
        titles.add("已受理")
        titles.add("处理中")
        titles.add("办结量")
        titles.add("未办结")

        for (i in titles.indices) {
            fragments.add(i, EventFragment.getInstance(i, onTitleChangeListener, type))
        }
        pager.adapter = MyPagerAdapter(supportFragmentManager)
        pager.offscreenPageLimit = 5
        tab.tabMode = TabLayout.MODE_SCROLLABLE
        tab.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#16ADFC"))
        tab.setupWithViewPager(pager)
        pager.currentItem = intent.getIntExtra("index", 0)
        getSearch()
    }

    /**
     * 标题更改时的监听
     */
    interface OnTitleChangeListener {
        fun onTitleChange(index: Int, total: Int)
    }

    inner class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(i: Int): Fragment {
            return fragments[i]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position]
        }
    }

    /**
     * 获取搜索条件
     */
    private fun getSearch() {
        val subscriberOnNextListener = SubscriberOnNextListener<Search> {
            it.dep.add(0, Search.Dep("全部", null))
            listDep = it.dep
            initPop(listDep)
            select.setOnClickListener {
                showPop()
            }
        }
        HttpUtil.getInstance().getSearch(ProgressSubscriber<Search>(subscriberOnNextListener, this))
    }

    /**
     * 初始化PopupWindow
     */
    @SuppressLint("SimpleDateFormat")
    private fun initPop(departs: List<Search.Dep>) {
        val view = LayoutInflater.from(this).inflate(R.layout.pop_grid, null)
        val tv_resource = view.find<TextView>(R.id.tv_resource)
        val tv_s_date = view.find<TextView>(R.id.tv_s_date)
        val tv_e_date = view.find<TextView>(R.id.tv_e_date)
        val tv_depart = view.find<TextView>(R.id.tv_depart)

        tv_resource.text = listResource[type]
        tv_resource.setOnClickListener {
            if (pop.isShowing)
                pop.dismiss()
            val pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, _, _, _ ->
                type = options1
                tv_resource.text = listResource[type]
                showPop()
            }).setOutSideCancelable(false).setContentTextSize(25).build<String>()
            pvOptions.setPicker(listResource)
            pvOptions.setSelectOptions(departIndex)
            pvOptions.setTitleText("请选择业务来源")
            pvOptions.show()

        }
        tv_s_date.text = "空"
        tv_s_date.setOnClickListener {
            if (pop.isShowing)
                pop.dismiss()
            if (dialog_date1 == null) {
                val builder = AlertDialog.Builder(this)
                val itemView = LayoutInflater.from(this).inflate(R.layout.item_date, null)
                val picker = itemView.find<DatePicker>(R.id.dataPicker)
                builder.setTitle("请选择开始日期")
                        .setView(itemView)
                        .setNegativeButton("取消") { _, _ ->
                            tv_s_date.text = "无"
                            sDate = null
                            showPop()
                        }.setPositiveButton("确定") { _, _ ->
                            sDate = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                            tv_s_date.text = sDate
                            showPop()
                        }
                dialog_date1 = builder.create()
            }
            dialog_date1!!.show()
        }
        tv_e_date.text = "空"
        tv_e_date.setOnClickListener {
            if (pop.isShowing)
                pop.dismiss()
            if (dialog_date2 == null) {
                val builder = AlertDialog.Builder(this)
                val itemView = LayoutInflater.from(this).inflate(R.layout.item_date, null)
                val picker = itemView.find<DatePicker>(R.id.dataPicker)
                builder.setTitle("请选择结束日期")
                        .setView(itemView)
                        .setNegativeButton("取消") { _, _ ->
                            tv_e_date.text = "无"
                            eDate = null
                            showPop()
                        }.setPositiveButton("确定") { _, _ ->
                            eDate = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                            tv_e_date.text = eDate
                            showPop()
                        }
                dialog_date2 = builder.create()
            }
            dialog_date2!!.show()
        }
        // 设置选择的部门
        tv_depart.text = departs[0].dep
        tv_depart.setOnClickListener {
            if (pop.isShowing)
                pop.dismiss()
            val pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, _, _, _ ->
                tv_depart.text = departs[options1].dep
                departIndex = options1
                showPop()
            }).setOutSideCancelable(false).setContentTextSize(25).build<Search.Dep>()
            pvOptions.setPicker(departs)
            pvOptions.setSelectOptions(departIndex)
            pvOptions.setTitleText("请选择处理部门")
            pvOptions.show()
        }
        view.find<Button>(R.id.button).setOnClickListener {
            pop.dismiss()
            changeType()
        }

        pop = PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        pop.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFFFFF")))
        pop.isFocusable = true
        pop.isOutsideTouchable = true
        pop.update()
        pop.setOnDismissListener {
            val lp = window.attributes
            lp.alpha = 1f //0.0-1.0
            window.attributes = lp
        }
    }

    /**
     * 显示pop
     */
    private fun showPop() {
        pop.showAtLocation(pager, Gravity.CENTER, 0, 0)
        val lp = window.attributes
        lp.alpha = 0.5f //0.0-1.0
        window.attributes = lp
    }

    /**
     * 切换来源
     */
    private fun changeType() {
        head_title.text = listResource[type]
        for (f in fragments) {
            f.refresh(type, sDate, eDate, listDep[departIndex].id)
        }
    }
}
