package com.yonggang.ygcommunity.grid.event

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_event.*
import org.jetbrains.anko.startActivity

class EventActivity : BaseActivity() {

    private lateinit var onTitleChangeListener: OnTitleChangeListener

    private var titles = mutableListOf<String>()
    private var fragments = mutableListOf<EventFragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        onTitleChangeListener = object : OnTitleChangeListener {
            override fun onTitleChange(index: Int, total: Int) {
                var title = ""
                when (index) {
                    1 -> title = "代签收"
                    2 -> title = "已签收"
                    3 -> title = "平台自行处理"
                    4 -> title = "核查通知"
                    5 -> title = "转派部门"
                    6 -> title = "部门签收"
                    7 -> title = "部门处理中"
                    8 -> title = "部门完结"
                    9 -> title = "任务结束"
                }
                titles[index-1] = "$title $total"
                pager.adapter.notifyDataSetChanged()
            }
        }
        titles.add("代签收")
        titles.add("已签收")
        titles.add("平台自行处理")
        titles.add("核查通知")
        titles.add("转派部门")
        titles.add("部门签收")
        titles.add("部门处理中")
        titles.add("部门完结")
        titles.add("任务结束")

        for (index in titles.indices) {
            fragments.add(EventFragment.getInstance(index+1, onTitleChangeListener))
        }
        pager.adapter = MyPagerAdapter(supportFragmentManager)
        pager.offscreenPageLimit = 10
        tab.tabMode = TabLayout.MODE_SCROLLABLE
        tab.setSelectedTabIndicatorColor(resources.getColor(R.color.refresh_color))
        tab.setTabTextColors(Color.parseColor("#000000"), resources.getColor(R.color.refresh_color))
        tab.setupWithViewPager(pager)

        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
        pic_add.setOnClickListener { startActivity<AddEventActivity>() }

    }

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
}
