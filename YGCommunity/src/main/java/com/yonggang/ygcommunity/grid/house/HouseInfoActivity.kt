package com.yonggang.ygcommunity.grid.house

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraNativeHelper
import com.baidu.ocr.ui.camera.CameraView
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.grid.house.Fragment.HouseFamilyFragment
import com.yonggang.ygcommunity.grid.house.Fragment.HouseInfoFragment
import kotlinx.android.synthetic.main.activity_house_info.*

class HouseInfoActivity : BaseActivity() {

    private var titles: MutableList<String> = mutableListOf("基本信息")
    private var fragments: MutableList<Fragment> = mutableListOf()
    private lateinit var adapter: HousePageAdapter

    private val onInfoSubmit: OnInfoSubmitListener = object : OnInfoSubmitListener {
        override fun onInfoSubmit(pk: String, sfsy: Int) {
            if (fragments.size == 1) {
                titles.add("家庭信息")
                fragments.add(HouseFamilyFragment.newInstance(pk, sfsy))
                adapter.notifyDataSetChanged()
            }
            pager.setCurrentItem(1, true)
        }
    }

    private val onRemoveFragment: OnRemoveFragment = object : OnRemoveFragment {
        override fun onRemoveFragment() {
            if (fragments.size == 2) {
                titles.removeAt(1)
                fragments.removeAt(1)
                adapter.notifyDataSetChanged()
            }
//            pager.setCurrentItem(0, true)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        fragments.get(0).onActivityResult(requestCode, resultCode, data)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_info)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        fragments.add(HouseInfoFragment.newInstance(onInfoSubmit, onRemoveFragment))
        adapter = HousePageAdapter(supportFragmentManager)
        pager.adapter = adapter
        pager.offscreenPageLimit = 2
        tab.tabMode = TabLayout.MODE_SCROLLABLE
        tab.setSelectedTabIndicatorColor(resources.getColor(R.color.refresh_color))
        tab.setTabTextColors(Color.parseColor("#000000"), resources.getColor(R.color.refresh_color))
        tab.setupWithViewPager(pager)
        initAccessTokenWithAkSk()
        init()
        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }
    }

    inner class HousePageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return titles[position]
        }

    }


    private fun init() {
        CameraNativeHelper.init(this, OCR.getInstance(this).license) { errorCode, _ ->
            when (errorCode) {
                CameraView.NATIVE_AUTH_FAIL -> init()
                else -> errorCode.toString()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CameraNativeHelper.release()
    }

    /**
     * 用明文ak，sk初始化
     */
    private fun initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(object : OnResultListener<AccessToken> {
            override fun onResult(result: AccessToken) {
                val token = result.accessToken
                Log.i("initAccessTokenWithAkSk", token)
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
            }
        }, applicationContext, "CM7bjqHmH3xTO8yQvULj6BYx", "tIgNdidBjLLbiv387yoIdkEZ5DBnwzyh")
    }

    interface OnInfoSubmitListener {
        fun onInfoSubmit(pk: String, sfsy: Int)
    }

    interface OnRemoveFragment {
        fun onRemoveFragment()
    }

}
