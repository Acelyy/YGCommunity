package com.yonggang.ygcommunity.grid.Visit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_visit_details.*

class VisitDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_details)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }
    }
}
