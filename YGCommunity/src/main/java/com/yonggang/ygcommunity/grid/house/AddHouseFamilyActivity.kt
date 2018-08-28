package com.yonggang.ygcommunity.grid.house

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.ocr.ui.camera.CameraActivity
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.umeng.socialize.utils.Log
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.FileUtil
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_add_house_family.*
import org.jetbrains.anko.find
import java.text.SimpleDateFormat
import java.util.*

class AddHouseFamilyActivity : BaseActivity() {
    private var dialog: AlertDialog? = null
    companion object {
        private val types = listOf(
                "流动人口",
                "社员",
                "户籍",
                "非社员"
        )
        private val REQUEST_CODE_CAMERA = 102
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

        initDatePicker()

        scanner.setOnClickListener{
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(this).absolutePath)
            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
                    true)
            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
            // 请手动使用CameraNativeHelper初始化和释放模型
            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
                    true)
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
            startActivityForResult(intent, AddHouseFamilyActivity.REQUEST_CODE_CAMERA)
        }
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
            finish()
        }
        HttpUtil.getInstance().setHouseFamily(ProgressSubscriber<String>(subscriberOnNextListener, this, "添加中"), "83a1e0a3-fce2-11e7-a81b-b083fecfd46e", name, sex, id, birth, relationship, phone, type, job)
    }

    private fun initDatePicker() {
        val view = LayoutInflater.from(this).inflate(R.layout.item_date_picker, null)
        val picker = view.find<DatePicker>(R.id.date)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择时间")
                .setView(view)
                .setPositiveButton("确定") { _, _ ->
                    birth.text = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                }.setNegativeButton("取消"){ _, _ ->

                }
        if (dialog == null)
            dialog = builder.create()

        birth.setOnClickListener {
            dialog!!.show()
        }

    }
}
