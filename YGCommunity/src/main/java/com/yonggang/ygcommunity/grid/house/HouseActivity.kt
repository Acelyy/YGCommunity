package com.yonggang.ygcommunity.grid.house

import android.os.Bundle
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_house.*
import org.jetbrains.anko.startActivity

class HouseActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        text_back.setOnClickListener{finish()}
        layout_info.setOnClickListener { startActivity<HouseInfoActivity>() }
        layout_extra.setOnClickListener { startActivity<HouseExtraActivity>() }
        layout_family.setOnClickListener { startActivity<HouseFamilyActivity>() }
    }
}
