package com.yonggang.ygcommunity.grid

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import cn.jpush.android.api.JPushInterface
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.GridUser
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.SpUtil
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_grid_login.*
import org.jetbrains.anko.startActivity

class GridLoginActivity : BaseActivity() {

    private lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_login)
        app = application as YGApplication

        val info = SpUtil.getGrid(this)
        edit_user.text = Editable.Factory.getInstance().newEditable(info["name"] as String)
        edit_pass.text = Editable.Factory.getInstance().newEditable(info["pass"] as String)
        cb_remember.isChecked = info["is_remember"] as Boolean

        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)

        pic_back.setOnClickListener { finish() }

        btn_login.setOnClickListener { login(edit_user.text.toString().trim(), edit_pass.text.toString().trim()) }
    }

    /**
     *  网格化登录
     */
    private fun login(username: String, password: String) {
        if ("" == username) {
            Snackbar.make(btn_login, "用户名不能为空", Snackbar.LENGTH_LONG).show()
            return
        }
        if ("" == password) {
            Snackbar.make(btn_login, "密码不能为空", Snackbar.LENGTH_LONG).show()
            return
        }
        val subscriberOnNextListener = SubscriberOnNextListener<GridUser> {
            app.grid = it
            if (cb_remember.isChecked) {
                SpUtil.saveGrid(this@GridLoginActivity, username, password, true)
            } else {
                SpUtil.saveGrid(this@GridLoginActivity, "", "", false)
            }
            startActivity<WorkSpaceActivity>("id" to it.id,"sswg" to it.sswg)
            finish()
        }
        HttpUtil.getInstance().grid_login(ProgressSubscriber<GridUser>(subscriberOnNextListener, this, "登录中"), username, password, JPushInterface.getRegistrationID(this))
    }
}
