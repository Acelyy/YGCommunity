package com.yonggang.ygcommunity.grid.check

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import com.alibaba.fastjson.JSON
import com.yonggang.ygcommunity.Activity.BbsPicActivity
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.CheckDetails
import com.yonggang.ygcommunity.PhotoPicker.PhotoAdapter
import com.yonggang.ygcommunity.PhotoPicker.RecyclerItemClickListener
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.ImageUtils
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.yonggang.ygcommunity.httpUtil.ProgressSubscriber
import com.yonggang.ygcommunity.httpUtil.SubscriberOnNextListener
import kotlinx.android.synthetic.main.activity_check_list_details.*
import me.iwf.photopicker.PhotoPicker
import me.iwf.photopicker.PhotoPreview
import org.jetbrains.anko.startActivity
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.ArrayList

class CheckListDetailsActivity : BaseActivity() {

    private var adapter: PhotoAdapter? = null

    private val photoPaths = ArrayList<String>()

    private lateinit var id: String

    private lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_list_details)
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
        id = intent.getStringExtra("id")
        app = application as YGApplication
        getTask()
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
        submit.setOnClickListener {
            setTask()
        }
        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
    }

    private fun getTask() {
        val subscriber = object : Subscriber<CheckDetails>() {
            override fun onNext(t: CheckDetails?) {
                number.text = t!!.sjbt
                term.text = t.sbsj
                serious.text = t.yzcd
                tv_location.text = t.sjdw
                describe.text = t.sjms
//                handle.text = t.sjbt
                tv_photes.text = t.girdimgs.size.toString() + "张照片"
                layout_photes.setOnClickListener {
                    this@CheckListDetailsActivity.startActivity<BbsPicActivity>("imgs" to t.girdimgs, "index" to 0)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }
        }
        HttpUtil.getInstance().getTaskDetails(subscriber, id)
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

    private fun setTask() {
        if (opinion.text.toString() == "") {
            Snackbar.make(submit, "请填写核查意见", Snackbar.LENGTH_LONG)
            return
        }
//        val imgs: MutableList<String> = mutableListOf()
//
//        val observable = rx.Observable.create(rx.Observable.OnSubscribe<String> {
//            for (path in photoPaths) {
//                imgs.add(ImageUtils.bitmapToString(path))
//            }
//            it.onCompleted()
//        }).subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//
//        val subscriber = object : Subscriber<String>() {
//            override fun onNext(t: String?) {
//
//            }
//
//            override fun onCompleted() {
//                val subscriberOnNextListener = SubscriberOnNextListener<String> {
//                    Snackbar.make(submit, "提交成功", Snackbar.LENGTH_LONG).show()
//                    finish()
//                }
//                HttpUtil.getInstance().setTaskDetails(
//                        ProgressSubscriber<String>(subscriberOnNextListener, this@CheckListDetailsActivity, "上报中"),
//                        id,
//                        app.grid.id,
//                        JSON.toJSONString(imgs),
//                        opinion.text.toString().trim()
//                )
//            }
//
//            override fun onError(e: Throwable?) {
//
//            }
//
//        }
//
//        observable.subscribe(subscriber)
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
                Log.i("imgs",JSON.toJSONString(imgs))
            }

            override fun onCompleted() {
                val subscriberOnNextListener = SubscriberOnNextListener<String> {
                    Snackbar.make(submit, "提交成功", Snackbar.LENGTH_LONG).show()
                    finish()
                }


                HttpUtil.getInstance().setTaskDetails(ProgressSubscriber<String>(subscriberOnNextListener, this@CheckListDetailsActivity, "上报中"),
                        id,
                        app.grid.id,
                        JSON.toJSONString(imgs),
                        opinion.text.toString().trim()
                )
            }

            override fun onError(e: Throwable?) {

            }

        }

        observable.subscribe(subscriber)

    }
}
