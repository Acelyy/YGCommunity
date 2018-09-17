package com.yonggang.ygcommunity.grid

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.amap.api.maps.model.MyLocationStyle
import com.bumptech.glide.Glide
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Gztj
import com.yonggang.ygcommunity.Entry.Swiper
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.grid.Visit.AddVisitActivity
import com.yonggang.ygcommunity.grid.Visit.VisitActivity
import com.yonggang.ygcommunity.grid.check.CheckListActivity
import com.yonggang.ygcommunity.grid.event.AddEventActivity
import com.yonggang.ygcommunity.grid.event.EventActivity
import com.yonggang.ygcommunity.grid.folk.AddFolkActivity
import com.yonggang.ygcommunity.grid.folk.FolkActivity
import com.yonggang.ygcommunity.grid.house.HouseInfoActivity
import com.yonggang.ygcommunity.grid.mission.MissionListActivity
import com.yonggang.ygcommunity.grid.notify.NotifyActivity
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.activity_work_space.*
import kotlinx.android.synthetic.main.item_iamge_single.*
import org.jetbrains.anko.startActivity
import rx.Subscriber

class WorkSpaceActivity : BaseActivity(), View.OnClickListener {

    lateinit var app: YGApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_space)
        app = application as YGApplication
        StatusBarUtil.setColor(this, resources.getColor(R.color.refresh_color), 0)
        val header = WaveSwipeHeader(this)
        refresh.setPrimaryColorsId(R.color.refresh_color)
        refresh.setRefreshHeader(header)
        refresh.isEnableLoadMore = false

        refresh.setOnRefreshListener {
            getGztj()
        }
        pic_back.setOnClickListener { finish() }
        text_back.setOnClickListener { finish() }
        layout_today_event.setOnClickListener(this)
        layout_today_hourse.setOnClickListener(this)
        layout_today_walk.setOnClickListener(this)

        layout_my_task.setOnClickListener(this)
        layout_my_work.setOnClickListener(this)
        layout_my_visit.setOnClickListener(this)
        layout_app_hourse.setOnClickListener(this)
        layout_app_event.setOnClickListener(this)
        layout_app_note.setOnClickListener(this)
        layout_app_walk.setOnClickListener(this)
        layout_app_message.setOnClickListener(this)
        getGztj()
        getSwiper()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_today_event -> startActivity<EventActivity>()
            R.id.layout_app_event -> startActivity<AddEventActivity>()
            R.id.layout_app_hourse -> startActivity<HouseInfoActivity>()
            R.id.layout_app_note -> startActivity<AddFolkActivity>()
            R.id.layout_my_visit -> startActivity<VisitActivity>()
            R.id.layout_app_walk -> startActivity<AddVisitActivity>()
            R.id.layout_today_walk -> startActivity<FolkActivity>()
            R.id.layout_my_task -> {
                if (app.grid.appauth == 1) {
                    startActivity<CheckListActivity>()
                } else {
                    Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
                }
            }
            R.id.layout_my_work -> {
                if (app.grid.appauth != 1) {
                    startActivity<MissionListActivity>()
                } else {
                    Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
                }
            }
            R.id.layout_app_message -> startActivity<NotifyActivity>()
        }
    }

    /**
     * 获取工作统计
     */
    private fun getGztj() {
        val subscriber = object : Subscriber<Gztj>() {
            override fun onNext(data: Gztj?) {
                size_today_event.withNumber(data!!.sbsj).start()
                size_today_hourse.withNumber(data.rfcj).start()
                size_today_walk.withNumber(data.zfqk).start()
                size_my_task.withNumber(data.my_hcrw).start()
                size_my_work.withNumber(data.my_gdcl).start()
                size_my_visit.withNumber(data.my_xfry).start()
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getGztj(subscriber,app.grid.id)
    }

    private fun getSwiper(){
        val subscriber = object:Subscriber<MutableList<Swiper>>(){
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error",e.toString())
            }

            override fun onNext(t: MutableList<Swiper>?) {
                if(t != null){
                    val imageList = ArrayList<String>()
                    val titleList = ArrayList<String>()
                    for(i in t){
                        if(i.status == "1"){
                            imageList.add("http://"+i.imgurl)
                            titleList.add("")
                        }
                    }

                    banner.setImages(imageList)
                            .setDelayTime(3000)
                            .setBannerTitles(titleList)
                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                            .setImageLoader(object : com.youth.banner.loader.ImageLoader() {
                                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                                    imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                                    //ImageLoader.getInstance().displayImage(path.toString(), imageView);
                                    Glide.with(this@WorkSpaceActivity)
                                            .load(path.toString())
                                            .error(R.mipmap.pic_loading_error)
                                            .into(imageView)


                                }
                            })
                            .setBannerStyle(BannerConfig.NOT_INDICATOR)
                            .start()
                }
            }
        }
        HttpUtil.getInstance().getSwiper(subscriber)
    }
}
