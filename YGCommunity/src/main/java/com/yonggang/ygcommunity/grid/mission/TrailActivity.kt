package com.yonggang.ygcommunity.grid.mission

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.yonggang.ygcommunity.Entry.Trail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.activity_transfer.*
import rx.Subscriber

class TrailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trail)
        val id = intent.getStringExtra("id")
        refresh.setOnRefreshListener {
            getTrail(id)
        }
        refresh.autoRefresh()
    }

    /**
     * 获取轨迹
     */
    private fun getTrail(id: String) {
        val subscriber = object : Subscriber<List<Trail>>() {
            override fun onNext(t: List<Trail>?) {
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                Log.i("error", e.toString())
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getTrail(subscriber, id)
    }


    class TrailAdapter():BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItem(position: Int): Any {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getItemId(position: Int): Long {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getCount(): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }

}
