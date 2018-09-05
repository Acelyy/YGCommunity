package com.yonggang.ygcommunity.grid.check

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import kotlinx.android.synthetic.main.activity_check_list_details.*

class CheckListDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list_details)
        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
    }
}
