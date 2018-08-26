package com.yonggang.ygcommunity.grid.house

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.umeng.socialize.utils.Log
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_add_house_family.*

class AddHouseFamilyActivity : BaseActivity() {

    companion object {
        private val types = listOf(
                "流动人口",
                "社员",
                "户籍",
                "非社员"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_house_family)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)

        pic_back.setOnClickListener {
            finish()
        }

        text_back.setOnClickListener {
            finish()
        }

        setPicker(layout_type, type, types, "请选择人员类型")

        submit.setOnClickListener {
            setHouseFamily(
                    name.text.toString().trim(),
                    if (rg_sex.checkedRadioButtonId == R.id.man) {
                        "男"
                    } else {
                        "女"
                    },
                    number.text.toString().trim(),
                    tell.text.toString().trim(),
                    birth.text.toString().trim(),
                    relationship.text.toString().trim(),
                    job.text.toString().trim(),
                    when (type.text) {
                        "户籍" -> "0"
                        "流动人口" -> "1"
                        "社员" -> "2"
                        "非社员" -> "3"
                        else -> "1"
                    }
            )
        }


    }

    /**
     * 统一设置选择器
     */
    private fun setPicker(layout: LinearLayout, tv: TextView, data: List<String>, title: String) {
        var index = 0
        layout.setOnClickListener {
            val pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { options1, _, _, _ ->
                index = options1
                tv.text = data[index]
            }).setOutSideCancelable(false).setContentTextSize(25).build<String>()
            pvOptions.setPicker(data)
            pvOptions.setSelectOptions(index)
            pvOptions.setTitleText("请选择$title")
            pvOptions.show()
        }
    }


    /**
     *
     */
    private fun setHouseFamily(name: String, sex: String, id: String, phone: String, birth: String, relationship: String, job: String, type: String) {
        val subscriberOnNextListener = SubscriberOnNextListener<String> {
            Log.i("setHouseFamily", it)

        }
        HttpUtil.getInstance().setHouseFamily(ProgressSubscriber<String>(subscriberOnNextListener, this, "添加中"), "83a1e0a3-fce2-11e7-a81b-b083fecfd46e", name, sex, id, birth, relationship, phone, type, job)
    }


}
