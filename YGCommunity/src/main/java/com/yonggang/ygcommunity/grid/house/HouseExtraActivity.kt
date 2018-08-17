package com.yonggang.ygcommunity.grid.house

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil

class HouseExtraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_extra)

        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)


    }
}
