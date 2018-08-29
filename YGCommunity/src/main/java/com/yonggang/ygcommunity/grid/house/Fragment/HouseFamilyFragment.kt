package com.yonggang.ygcommunity.grid.house.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.yonggang.ygcommunity.Entry.HouseFamily
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.grid.house.AddHouseFamilyActivity
import com.yonggang.ygcommunity.httpUtil.HttpUtil
import kotlinx.android.synthetic.main.fragment_house_family.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import rx.Subscriber

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HouseFamilyFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HouseFamilyFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HouseFamilyFragment : Fragment() {

    private lateinit var pk: String
    private var sfsy: Int = 0

    private var listData = mutableListOf<HouseFamily>()
    private lateinit var adapter: FamilyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("hhh","hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh")
        arguments?.let {
            pk = it.getString("pk")
            sfsy = it.getInt("sfsy")
            Log.i("pk", pk)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_house_family, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = FamilyAdapter(listData, LayoutInflater.from(activity))
        list_member.layoutManager = LinearLayoutManager(activity)
        list_member.adapter = adapter
        refresh.setOnRefreshListener {
            getHouseFamily()
        }
        refresh.autoRefresh()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment HouseFamilyFragment.
         */
        fun newInstance(pk: String, sfsy: Int) = HouseFamilyFragment().apply {
            arguments = Bundle().apply {
                putString("pk", pk)
                putInt("sfsy", sfsy)
            }

        }
    }

    /**
     * 获取家庭成员
     */
    private fun getHouseFamily() {
        val subscriber = object : Subscriber<List<HouseFamily>>() {
            override fun onNext(data: List<HouseFamily>?) {
                Log.i("getHouseFamily", JSON.toJSONString(data))
                adapter.data = data as MutableList<HouseFamily>
                adapter.notifyDataSetChanged()
                refresh.finishRefresh()
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                refresh.finishRefresh()
            }
        }
        HttpUtil.getInstance().getHouseFamily(subscriber, pk, sfsy)
    }


    /**
     * 家庭成员适配器
     */
    inner class FamilyAdapter(var data: List<HouseFamily>, var layoutInflater: LayoutInflater) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
            return NormalViewHolder(layoutInflater.inflate(R.layout.item_house_family, parent, false))
        }


        override fun getItemCount(): Int {
            return data.size
        }

        override fun onBindViewHolder(_holder: RecyclerView.ViewHolder?, position: Int) {
            val holder = _holder!! as NormalViewHolder
            holder.name?.text = data[position].xm
            holder.relationship?.text = data[position].gx
            holder.type?.text = data[position].type
            holder.birth?.text = data[position].csrq
            holder.tell?.text = data[position].lxdh
            holder.job?.text = data[position].gzdw
            holder.id?.text = data[position].sfzh
            if (data[position].xb == "男") {
                holder.sex?.setImageResource(R.mipmap.pic_house_man)
            } else {
                holder.sex?.setImageResource(R.mipmap.pic_house_woman)
            }

        }

        inner class NormalViewHolder(item: View) : RecyclerView.ViewHolder(item) {
            val name: TextView? = item.find(R.id.name)
            val sex: ImageView? = item.find(R.id.sex)
            val relationship: TextView? = item.find(R.id.relationship)
            val type: TextView? = item.find(R.id.type)
            val birth: TextView? = item.find(R.id.birth)
            val tell: TextView? = item.find(R.id.tell)
            val job: TextView? = item.find(R.id.job)
            val id: TextView? = item.find(R.id.id)
        }

//        inner class ButtonViewHolder(item: View) : RecyclerView.ViewHolder(item) {
//            val add: Button? = item.find(R.id.add)
//        }
    }
}
