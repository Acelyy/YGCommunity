package com.yonggang.ygcommunity.grid.house

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.baidu.ocr.ui.camera.CameraNativeHelper
import com.baidu.ocr.ui.camera.CameraView
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.grid.house.Fragment.HouseFamilyFragment
import com.yonggang.ygcommunity.grid.house.Fragment.HouseInfoFragment
import kotlinx.android.synthetic.main.activity_house_info.*

class HouseInfoActivity : BaseActivity() {

//    private val listEducation = listOf(
//            "学龄前儿童",
//            "幼儿园",
//            "文盲",
//            "小学以下",
//            "小学",
//            "中学",
//            "高中（职高）",
//            "大专",
//            "本科",
//            "硕士及以上"
//    )
//
//    private val listMarriage = listOf(
//            "未婚",
//            "初婚",
//            "已婚",
//            "再婚",
//            "离异",
//            "丧偶",
//            "分居",
//            "其他"
//    )
//
//    private val listPolitical = listOf(
//            "群众",
//            "团员",
//            "中共党员",
//            "其他"
//    )
//
//    private val REQUEST_CODE_CAMERA = 102


    private var titles: MutableList<String> = mutableListOf("基本信息")
    private var fragments: MutableList<Fragment> = mutableListOf()

    private lateinit var adapter: HousePageAdapter

    private val onInfoSubmit: OnInfoSubmitListener = object : OnInfoSubmitListener {
        override fun onInfoSubmit() {
            if (fragments.size == 1) {
                titles.add("家庭信息")
                fragments.add(HouseFamilyFragment.newInstance())
                adapter.notifyDataSetChanged()
            }
            pager.setCurrentItem(1, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_house_info)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        fragments.add(HouseInfoFragment.newInstance(onInfoSubmit))
        adapter = HousePageAdapter(supportFragmentManager)
        pager.adapter = adapter
        pager.offscreenPageLimit = 2
        tab.tabMode = TabLayout.MODE_SCROLLABLE
        tab.setSelectedTabIndicatorColor(resources.getColor(R.color.refresh_color))
        tab.setTabTextColors(Color.parseColor("#000000"), resources.getColor(R.color.refresh_color))
        tab.setupWithViewPager(pager)
        initAccessTokenWithAkSk()
        init()
//
//        scanner.setOnClickListener {
//            val intent = Intent(this, CameraActivity::class.java)
//            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
//                    FileUtil.getSaveFile(application).absolutePath)
//            intent.putExtra(CameraActivity.KEY_NATIVE_ENABLE,
//                    true)
//            // KEY_NATIVE_MANUAL设置了之后CameraActivity中不再自动初始化和释放模型
//            // 请手动使用CameraNativeHelper初始化和释放模型
//            // 推荐这样做，可以避免一些activity切换导致的不必要的异常
//            intent.putExtra(CameraActivity.KEY_NATIVE_MANUAL,
//                    true)
//            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT)
//            startActivityForResult(intent, REQUEST_CODE_CAMERA)
//        }
//
//        /**
//         * 监听身份证的字符长度  18位时进行查询
//         */
//        number.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (number.text.length == 18) {
//                    getHouseInfo(number.text.toString().trim())
//                }
//            }
//        })
//
//        // 初始化选择器
//        setPicker(layout_political, political, listPolitical, "政治面貌")
//        setPicker(layout_education, education, listEducation, "文化程度")
//        setPicker(layout_marriage, marriage, listMarriage, "婚姻状况")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                val contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE)
//                val filePath = FileUtil.getSaveFile(applicationContext).absolutePath
//                if (!TextUtils.isEmpty(contentType)) {
//                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT == contentType) {
//                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath)
//                    } else if (CameraActivity.CONTENT_TYPE_ID_CARD_BACK == contentType) {
//                        recIDCard(IDCardParams.ID_CARD_SIDE_BACK, filePath)
//                    }
//                }
//            }
//        }
    }

//    private fun recIDCard(idCardSide: String, filePath: String) {
//        val param = IDCardParams()
//        param.imageFile = File(filePath)
//        // 设置身份证正反面
//        param.idCardSide = idCardSide
//        // 设置方向检测
//        param.isDetectDirection = true
//        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
//        param.imageQuality = 20
//
//        OCR.getInstance(this).recognizeIDCard(param, object : OnResultListener<IDCardResult> {
//            override fun onResult(result: IDCardResult?) {
//                if (result != null) {
//                    number.text = Editable.Factory.getInstance().newEditable("" + result.idNumber)
//                } else {
//                    Snackbar.make(scanner, "扫描失败", Snackbar.LENGTH_LONG)
//                }
//            }
//
//            override fun onError(error: OCRError) {
//                Log.i("recIDCard", JSON.toJSONString(error))
//            }
//        })
//    }
//
//
//    /**
//     * 根据身份证获取信息
//     * @param id 身份证号
//     */
//    private fun getHouseInfo(id: String) {
//        val subscriberOnNextListener = SubscriberOnNextListener<HouseInfo> {
//            Log.i("getHouseInfo", JSON.toJSONString(it))
//            if (it != null) {
//                name.text = Editable.Factory.getInstance().newEditable(it.xm)
//                when (it.xb) {
//                    "男" -> rg_sex.check(R.id.man)
//                    "女" -> rg_sex.check(R.id.woman)
//                }
//                birth.text = Editable.Factory.getInstance().newEditable(it.csrq)
//                depart.text = Editable.Factory.getInstance().newEditable(if (it.gzdw != null) {
//                    it.gzdw
//                } else {
//                    it.zdxx
//                })
//                nation.text = Editable.Factory.getInstance().newEditable(it.mz)
//                political.text = it.zzmm
//                address.text = Editable.Factory.getInstance().newEditable(it.xjzdz)
//                tell.text = Editable.Factory.getInstance().newEditable(it.lxdh)
//                marriage.text = it.hyzk
//                household.text = Editable.Factory.getInstance().newEditable(it.hjdz)
//                house_id.text = Editable.Factory.getInstance().newEditable(it.hjbh)
//                hobby.text = Editable.Factory.getInstance().newEditable(it.xqah)
//                education.text = it.whcd
//            } else {
//
//            }
//        }
//        HttpUtil.getInstance().getHouseInfo(ProgressSubscriber<HouseInfo>(subscriberOnNextListener, this, "查询是否已存在信息"), id)
//    }


    interface OnInfoSubmitListener {
        fun onInfoSubmit()
    }


}
