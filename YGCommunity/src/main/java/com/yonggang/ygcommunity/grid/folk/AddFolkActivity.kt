package com.yonggang.ygcommunity.grid.folk

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.amap.api.location.AMapLocationClient
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.FolkChoose
import com.yonggang.ygcommunity.PhotoPicker.PhotoAdapter
import com.yonggang.ygcommunity.PhotoPicker.RecyclerItemClickListener
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.ImageUtils
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_add_folk.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview
import org.jetbrains.anko.find
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*

class AddFolkActivity : BaseActivity() {
    var mLocationClient: AMapLocationClient? = null

    private var adapter: PhotoAdapter? = null

    private val photoPaths = ArrayList<String>()

    lateinit var app: YGApplication

    private var dialog: AlertDialog? = null

    var toggle = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_folk)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        initDatePicker()
        getEventStatus()
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

        submit.setOnClickListener { setFolk() }
        pic_back.setOnClickListener { finish() }
    }


    private fun initDatePicker() {
        val view = LayoutInflater.from(this).inflate(R.layout.item_date_picker, null)
        val picker = view.find<DatePicker>(R.id.date)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("请选择时间")
                .setView(view)
                .setPositiveButton("确定") { _, _ ->
                    if(toggle){
                        tv_downtime.text = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                    }else{
                        tv_uptime.text = SimpleDateFormat("yyyy-MM-dd").format(Date(picker.year - 1900, picker.month, picker.dayOfMonth))
                    }
                }.setNegativeButton("取消") { _, _ ->

                }
        if (dialog == null) dialog = builder.create()

        tv_downtime.setOnClickListener {
            toggle = true
            dialog!!.show()
        }
        tv_uptime.setOnClickListener {
            toggle = false
            dialog!!.show()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onDestroy() {
        super.onDestroy()
        mLocationClient?.stopLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BaseActivity.RESULT_OK && (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {
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
        val subscriberOnNextListener = SubscriberOnNextListener<FolkChoose> {
            initPicker(layout_area, tv_area, it.data.mqxz, "请选择民情性质")
            initPicker2(layout_team, tv_team, it.data.dzb, "请选择走访党组")
        }
        HttpUtil.getInstance().getFolkChoose(ProgressSubscriber<FolkChoose>(subscriberOnNextListener, this, "加载中"))
    }

    /**
     * 设置选项民情
     */
    private fun initPicker(layout: LinearLayout, tv: TextView, data: List<FolkChoose.DataBean.MqxzBean>, title: String) {
        val pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { position, _, _, _ ->
            tv.text = data[position].name
            layout.tag = data[position].id
        }).setOutSideCancelable(false).setContentTextSize(25).build<FolkChoose.DataBean.MqxzBean>()
        pvOptions.setPicker(data)
        pvOptions.setSelectOptions(0)
        pvOptions.setTitleText(title)
        layout.setOnClickListener { pvOptions.show() }
    }

    /**
     * 设置选项党组
     */
    private fun initPicker2(layout: LinearLayout, tv: TextView, data: List<FolkChoose.DataBean.DzbBean>, title: String) {
        val pvOptions = OptionsPickerBuilder(this, OnOptionsSelectListener { position, _, _, _ ->
            tv.text = data[position].name
            layout.tag = data[position].id
        }).setOutSideCancelable(false).setContentTextSize(25).build<FolkChoose.DataBean.DzbBean>()
        pvOptions.setPicker(data)
        pvOptions.setSelectOptions(0)
        pvOptions.setTitleText(title)
        layout.setOnClickListener { pvOptions.show() }
    }

    /**
     * 发布事件
     */
    private fun setFolk() {
        if (tv_name.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写党员姓名", Snackbar.LENGTH_LONG).show()
            return
        }
        if (layout_area.tag == null) {
            Snackbar.make(submit, "请选择走访分类", Snackbar.LENGTH_LONG).show()
            return
        }
        if (layout_team.tag == null) {
            Snackbar.make(submit, "请选择党组", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tv_downtime.text.trim().toString() == "") {
            Snackbar.make(submit, "请选择民情性质", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tv_uptime.text.trim().toString() == "") {
            Snackbar.make(submit, "请选择走访日期", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tv_figure.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写走访对象", Snackbar.LENGTH_LONG).show()
            return
        }

        if (tv_address.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写走访对象地址", Snackbar.LENGTH_LONG).show()
            return
        }

//        if (tv_description.text.trim().toString() == "") {
//            Snackbar.make(submit, "请填写事件描述", Snackbar.LENGTH_LONG).show()
//            return
//        }

        if (tv_reason.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写民事原由", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tv_process.text.trim().toString() == "") {
            Snackbar.make(submit, "请填写代办过程", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tv_result.text.trim().toString() == "") {
            Snackbar.make(submit, "填写处理结果", Snackbar.LENGTH_LONG).show()
            return
        }
        if (tv_label.text.trim().toString() == "") {
            Snackbar.make(submit, "填写自定义标签分类", Snackbar.LENGTH_LONG).show()
            return
        }

        if (tv_person.text.trim().toString() == "") {
            Snackbar.make(submit, "填写录入人", Snackbar.LENGTH_LONG).show()
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
                    finish()
                }


                HttpUtil.getInstance().setFolk(ProgressSubscriber<String>(subscriberOnNextListener, this@AddFolkActivity, "上报中"),
                        tv_name.text.toString().trim(),
                        layout_team.tag as String,
                        tv_address.text.toString().trim(),
                        tv_figure.text.toString().trim(),
                        layout_area.tag as String,
                        tv_reason.text.toString().trim(),
                        tv_process.text.toString().trim(),
                        tv_result.text.toString().trim(),
                        tv_label.text.toString().trim(),
                        tv_person.text.toString().trim(),
                        tv_uptime.text.toString().trim(),
                        tv_downtime.text.toString().trim(),
                        JSON.toJSONString(imgs)
                )
            }

            override fun onError(e: Throwable?) {

            }

        }

        observable.subscribe(subscriber)

    }


}
