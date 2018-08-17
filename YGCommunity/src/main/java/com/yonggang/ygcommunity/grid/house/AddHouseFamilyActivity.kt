package com.yonggang.ygcommunity.grid.house

import android.os.Bundle
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil

class AddHouseFamilyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_house_family)

        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)

    }
}
