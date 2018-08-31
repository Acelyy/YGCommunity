package com.yonggang.ygcommunity.grid.event

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.umeng.socialize.utils.Log
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.GridStatus
import com.yonggang.ygcommunity.PhotoPicker.PhotoAdapter
import com.yonggang.ygcommunity.PhotoPicker.RecyclerItemClickListener
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.ImageUtils
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_add_event.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class AddEventActivity : BaseActivity(), AMapLocationListener {

    var mLocationClient: AMapLocationClient? = null

    var mLocationOption: AMapLocationClientOption? = null

    private var adapter: PhotoAdapter? = null

    private val photoPaths = ArrayList<String>()

    lateinit var app: YGApplication

    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation != null) {
            if (aMapLocation.errorCode == 0) {
                val address = aMapLocation.address
                tv_location.text = address ?: "未定位"
            } else {
                tv_location.text = "未定位"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        getEventStatus()
        tv_grid.text = Editable.Factory.getInstance().newEditable(app.grid.sswg)
        rv_pic.layoutManager = StaggeredGridLayoutManager(5, OrientationHelper.VERTICAL)

        adapter = PhotoAdapter(this, photoPaths)
        rv_pic.adapter = adapter
        rv_pic.addOnItemTouchListener(RecyclerItemClickListener(this,
                RecyclerItemClickListener.OnItemClickListener { _, position ->
                    if (adapter!!.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                        PhotoPicker.builder()
                                .setPhotoCount(PhotoAdapter.MAX)
                                .setShowCamera(true)
                                .setPreviewEnabled(false)
                                .setSelected(photoPaths)
                                .start(this)
                    } else {
                        PhotoPreview.builder()
                                .setPhotos(photoPaths)
                                .setCurrentItem(position)
                                .start(this)
                    }
                }))

        // 定位
        mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        mLocationClient?.setLocationListener(this)

        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption?.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption?.interval = 2000
        //设置定位参数
        mLocationClient?.setLocationOption(mLocationOption)
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mLocationOption?.isOnceLocation = true
        //获取最近3s内精度最高的一次定位结果：
        mLocationOption?.isOnceLocationLatest = true
        //启动定位
        mLocationClient?.startLocation()

        submit.setOnClickListener { addEvent() }
        pic_back.setOnClickListener { finish() }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.stopLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
            var photos: List<String>? = null
            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS)
            }
            photoPaths.clear()
            if (photos != null) {
                photoPaths.addAll(photos)
            }
            adapter!!.notifyDataSetChanged()
        }
    }

    /**
     * 获取事件上报的筛选条件
     */
    private fun getEventStatus() {
        val subscriberOnNextListener = SubscriberOnNextListener<GridStatus> {
            initPicker(layout_area, tv_area, it.xzqh, "请选择行政区域")
            initPicker(layout_plan, tv_plan, it.czfa, "请选择处置方案")
            initPicker(layout_type, tv_type, it.czlx, "请选择处置类型")
            initPicker(layout_severity, tv_severity, it.yzcd, "请选择严重程度")
            initPicker(layout_classify, tv_classify, it.sjfl, "请选择事件分类")
        }
        HttpUtil.getInstance().getEventStatus(ProgressSubscriber<GridStatus>(subscriberOnNextListener, this, "加载中"))
    }

    /**
     * 设置选项
     */
    private fun initPicker(layout: LinearLayout, tv: TextView, data: List<GridStatus.Bean>, title: String) {
        val pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { position, _, _, _ ->
            tv.text = data[position].name
            layout.tag = data[position].id
        }).setOutSideCancelable(false).setContentTextSize(25).build<GridStatus.Bean>()
        pvOptions.setPicker(data)
        pvOptions.setSelectOptions(0)
        pvOptions.setTitleText(title)
        layout.setOnClickListener { pvOptions.show() }
    }

    /**
     * 发布事件
     */
    private fun addEvent() {
        if (tv_title.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写事件标题", Snackbar.LENGTH_LONG).show()
            return
        }
        if (layout_area.tag == null) {
            Snackbar.make(submit, "请选择行政区域", Snackbar.LENGTH_LONG).show()
            return
        }

        if (layout_plan.tag == null) {
            Snackbar.make(submit, "请选择处置方案", Snackbar.LENGTH_LONG).show()
            return
        }

        if (layout_type.tag == null) {
            Snackbar.make(submit, "请选择处置类型", Snackbar.LENGTH_LONG).show()
            return
        }

        if (layout_severity.tag == null) {
            Snackbar.make(submit, "请选择严重程度", Snackbar.LENGTH_LONG).show()
            return
        }

        if (layout_classify.tag == null) {
            Snackbar.make(submit, "请选择事件分类", Snackbar.LENGTH_LONG).show()
            return
        }

        if (tv_name.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写诉求人", Snackbar.LENGTH_LONG).show()
            return
        }

        if (tv_phone.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写诉求人电话", Snackbar.LENGTH_LONG).show()
            return
        }

        if (tv_description.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写事件描述", Snackbar.LENGTH_LONG).show()
            return
        }

        val imgs: MutableList<String> = mutableListOf()

        val observable = rx.Observable.create(rx.Observable.OnSubscribe<String> {
            for (path in photoPaths) {
                imgs.add(ImageUtils.bitmapToString(path))
            }
            it.onCompleted()
        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

        val subscriber = object : Subscriber<String>() {
            override fun onNext(t: String?) {

            }

            override fun onCompleted() {
                val subscriberOnNextListener = SubscriberOnNextListener<String> {
                    Log.i("addEvent", it)
                    Snackbar.make(submit, "上报成功", Snackbar.LENGTH_LONG).show()
//                    finish()
                }


                HttpUtil.getInstance().addEvent(ProgressSubscriber<String>(subscriberOnNextListener, this@AddEventActivity, "上报中"),
                        layout_plan.tag as String,
                        layout_severity.tag as String,
                        layout_classify.tag as String,
                        tv_name.text.trim().toString(),
                        tv_phone.text.trim().toString(),
                        tv_location.text.trim().toString(),
                        tv_address.text.trim().toString(),
                        tv_grid.text.trim().toString(),
                        tv_description.text.trim().toString(),
                        tv_title.text.trim().toString(),
                        layout_area.tag as String,
                        layout_type.tag as String,
                        app.grid.id,
                        JSON.toJSONString(imgs)
                )
            }

            override fun onError(e: Throwable?) {

            }

        }

        observable.subscribe(subscriber)

    }
}
