package com.yonggang.ygcommunity.grid

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View
import com.scwang.smartrefresh.header.WaveSwipeHeader
import com.yonggang.ygcommunity.BaseActivity
import com.yonggang.ygcommunity.Entry.Gztj
import com.yonggang.ygcommunity.Entry.Swiper
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.Util.StatusBarUtil
import com.yonggang.ygcommunity.YGApplication
import com.yonggang.ygcommunity.grid.Visit.AddVisitActivity
import com.yonggang.ygcommunity.grid.Visit.VisitActivity
import com.yonggang.ygcommunity.grid.event.AddEventActivity
import com.yonggang.ygcommunity.grid.event.EventActivity
import com.yonggang.ygcommunity.grid.folk.AddFolkActivity
import com.yonggang.ygcommunity.grid.folk.FolkActivity
import com.yonggang.ygcommunity.grid.house.HouseInfoActivity
import com.yonggang.ygcommunity.grid.house.HouseListActivity
import com.yonggang.ygcommunity.grid.house.HouseQueryActivity
import com.yonggang.ygcommunity.grid.mission.MissionListActivity
import com.yonggang.ygcommunity.grid.notify.NotifyActivity
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_work_space.*
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
        layout_app_query.setOnClickListener(this)
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
            R.id.layout_today_event -> if (app.grid.appauth != 1) {
                startActivity<EventActivity>()
            } else {
                Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
            }
            R.id.layout_app_event -> startActivity<AddEventActivity>()
            R.id.layout_app_hourse -> startActivity<HouseInfoActivity>("sfzh" to "")
            R.id.layout_app_note -> startActivity<AddFolkActivity>()
            R.id.layout_my_visit -> if (app.grid.appauth != 1) {
                startActivity<VisitActivity>()
            } else {
                Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
            }
            R.id.layout_app_walk -> startActivity<AddVisitActivity>()
            R.id.layout_today_walk -> if (app.grid.appauth != 1) {
                startActivity<FolkActivity>()
            } else {
                Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
            }
            R.id.layout_my_work -> {
                if (app.grid.appauth != 1) {
                    startActivity<MissionListActivity>()
                } else {
                    Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
                }
            }
            R.id.layout_app_message -> startActivity<NotifyActivity>()
            R.id.layout_today_hourse -> if (app.grid.appauth != 1) {
                startActivity<HouseListActivity>()
            } else {
                Snackbar.make(refresh, "抱歉！您无此权限", Snackbar.LENGTH_LONG).show()
            }
            R.id.layout_app_query -> startActivity<HouseQueryActivity>()
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
        HttpUtil.getInstance().getGztj(subscriber, app.grid.id)
    }

    private fun getSwiper() {
        val subscriber = object : Subscriber<MutableList<Swiper>>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
            }

            override fun onNext(t: MutableList<Swiper>?) {
                if (t != null) {
//                    val imageList = ArrayList<String>()
                    val stringList =  ArrayList<String>()

                    for (i in t) {
                        if (i.status == 1) {
//                            imageList.add("http://" + i.imgurl)
                            stringList.add(i.title)
                        }
                    }
                    banner.setStrings(stringList);
//                    banner.setImages(imageList)
//                            .setDelayTime(3000)
//                            .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
//                            .setImageLoader(object : com.youth.banner.loader.ImageLoader() {
//                                override fun displayImage(context: Context, path: Any, imageView: ImageView) {
//                                    imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
//                                    //ImageLoader.getInstance().displayImage(path.toString(), imageView);
//                                    Glide.with(this@WorkSpaceActivity)
//                                            .load(path.toString())
//                                            .error(R.mipmap.pic_loading_error)
//                                            .into(imageView)
//
//                                }
//                            })
//                            .setBannerStyle(BannerConfig.NOT_INDICATOR)
//                            .start()
                }
            }
        }
        HttpUtil.getInstance().getSwiper(subscriber)
    }
}
