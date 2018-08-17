package com.yonggang.ygcommunity.grid.house

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_house_family.*
import org.jetbrains.anko.startActivity

class HouseFamilyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_family)

        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)

        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }

        add.setOnClickListener { startActivity<AddHouseFamilyActivity>() }
    }
}
