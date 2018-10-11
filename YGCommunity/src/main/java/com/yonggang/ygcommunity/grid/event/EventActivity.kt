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
                    0 -> title = "待签收"
                    1 -> title = "已签收"
                    2 -> title = "平台自行处理"
                    3 -> title = "核查通知"
                    4 -> title = "转派部门"
                    5 -> title = "部门处理中"
                    6 -> title = "部门完结"
                    7 -> title = "任务结束"
                }
                titles[index] = "$title $total"
                pager.adapter.notifyDataSetChanged()
            }
        }
        titles.add("待签收")
        fragments.add(EventFragment.getInstance(0, 1, onTitleChangeListener))

        titles.add("已签收")
        fragments.add(EventFragment.getInstance(1, 2, onTitleChangeListener))

        titles.add("平台自行处理")
        fragments.add(EventFragment.getInstance(2, 3, onTitleChangeListener))

        titles.add("核查通知")
        fragments.add(EventFragment.getInstance(3, 4, onTitleChangeListener))

        titles.add("转派部门")
        fragments.add(EventFragment.getInstance(4, 5, onTitleChangeListener))

        titles.add("部门处理中")
        fragments.add(EventFragment.getInstance(5, 7, onTitleChangeListener))

        titles.add("部门完结")
        fragments.add(EventFragment.getInstance(6, 8, onTitleChangeListener))

        titles.add("任务结束")
        fragments.add(EventFragment.getInstance(7, 10, onTitleChangeListener))

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
