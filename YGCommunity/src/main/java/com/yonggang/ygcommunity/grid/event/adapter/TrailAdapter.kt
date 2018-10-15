package com.yonggang.ygcommunity.grid.event.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yonggang.ygcommunity.Entry.GridEventDetail
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.View.RoundAngleImageView
import org.jetbrains.anko.find

const val TYPE_HEAD: Int = 0
const val TYPE_LAYOUT: Int = 1

class TrailAdapter(var data: GridEventDetail, var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var onImageClickListener: OnImageClickListener

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        val view: View? = null
        when (viewType) {
            TYPE_HEAD -> return ViewHolderHead(LayoutInflater.from(context).inflate(R.layout.item_trail_head, parent, false))
            TYPE_LAYOUT -> return ViewHolderLayout(LayoutInflater.from(context).inflate(R.layout.item_trail_layout, parent, false))
        }
        return ViewHolderHead(view!!)
    }

    override fun getItemCount(): Int {
        return if (data.trail == null) {
            1
        } else {
            data.trail.size + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when (getItemViewType(position)) {
            TYPE_HEAD -> {
                val holder = holder as ViewHolderHead
                Glide.with(context).load(R.mipmap.pic_head).into(holder.bbs_head)
                holder.bbs_name?.text = data.detail.name
                holder.bbs_title?.text = data.detail.sjbt
                holder.bbs_content?.text = data.detail.sjms
                holder.bbs_time?.text = data.detail.sbsj

                if (data.detail.imgs == null || data.detail.imgs.isEmpty() ||(data.detail.imgs.size == 1 && data.detail.imgs[0] == "")) {
                    holder.layout_pic?.visibility = View.GONE
                    holder.layout_pic2?.visibility = View.GONE
                } else {
                    when (data.detail.imgs.size) {
                        1 -> {
                            holder.bbs_pic?.visibility = View.VISIBLE
                            holder.bbs_pic2?.visibility = View.INVISIBLE
                            holder.bbs_pic3?.visibility = View.INVISIBLE
                            holder.bbs_pic4?.visibility = View.INVISIBLE
                            holder.bbs_pic5?.visibility = View.INVISIBLE
                            holder.bbs_pic6?.visibility = View.INVISIBLE
                            holder.layout_pic?.visibility = View.VISIBLE
                            holder.layout_pic2?.visibility = View.GONE

                            Glide.with(context).load(data.detail.imgs[0]).centerCrop().into(holder.bbs_pic)
                            holder.bbs_pic?.setOnClickListener { onImageClickListener.onImageClick(it, 0) }
                        }
                        2 -> {
                            holder.bbs_pic?.visibility = View.VISIBLE
                            holder.bbs_pic2?.visibility = View.VISIBLE
                            holder.bbs_pic3?.visibility = View.INVISIBLE
                            holder.bbs_pic4?.visibility = View.INVISIBLE
                            holder.bbs_pic5?.visibility = View.INVISIBLE
                            holder.bbs_pic6?.visibility = View.INVISIBLE
                            holder.layout_pic?.visibility = View.VISIBLE
                            holder.layout_pic2?.visibility = View.GONE

                            Glide.with(context).load(data.detail.imgs[0]).centerCrop().into(holder.bbs_pic)
                            Glide.with(context).load(data.detail.imgs[1]).centerCrop().into(holder.bbs_pic2)
                            holder.bbs_pic?.setOnClickListener { onImageClickListener.onImageClick(it, 0) }
                            holder.bbs_pic2?.setOnClickListener { onImageClickListener.onImageClick(it, 1) }
                        }
                        3 -> {
                            holder.bbs_pic?.visibility = View.VISIBLE
                            holder.bbs_pic2?.visibility = View.VISIBLE
                            holder.bbs_pic3?.visibility = View.VISIBLE
                            holder.bbs_pic4?.visibility = View.INVISIBLE
                            holder.bbs_pic5?.visibility = View.INVISIBLE
                            holder.bbs_pic6?.visibility = View.INVISIBLE
                            holder.layout_pic?.visibility = View.VISIBLE
                            holder.layout_pic2?.visibility = View.GONE

                            Glide.with(context).load(data.detail.imgs[0]).centerCrop().into(holder.bbs_pic)
                            Glide.with(context).load(data.detail.imgs[1]).centerCrop().into(holder.bbs_pic2)
                            Glide.with(context).load(data.detail.imgs[2]).centerCrop().into(holder.bbs_pic3)
                            holder.bbs_pic?.setOnClickListener { onImageClickListener.onImageClick(it, 0) }
                            holder.bbs_pic2?.setOnClickListener { onImageClickListener.onImageClick(it, 1) }
                            holder.bbs_pic3?.setOnClickListener { onImageClickListener.onImageClick(it, 2) }
                        }
                        4 -> {
                            holder.bbs_pic?.visibility = View.VISIBLE
                            holder.bbs_pic2?.visibility = View.VISIBLE
                            holder.bbs_pic3?.visibility = View.VISIBLE
                            holder.bbs_pic4?.visibility = View.VISIBLE
                            holder.bbs_pic5?.visibility = View.INVISIBLE
                            holder.bbs_pic6?.visibility = View.INVISIBLE
                            holder.layout_pic?.visibility = View.VISIBLE
                            holder.layout_pic2?.visibility = View.VISIBLE

                            Glide.with(context).load(data.detail.imgs[0]).centerCrop().into(holder.bbs_pic)
                            Glide.with(context).load(data.detail.imgs[1]).centerCrop().into(holder.bbs_pic2)
                            Glide.with(context).load(data.detail.imgs[2]).centerCrop().into(holder.bbs_pic3)
                            Glide.with(context).load(data.detail.imgs[3]).centerCrop().into(holder.bbs_pic4)
                            holder.bbs_pic?.setOnClickListener { onImageClickListener.onImageClick(it, 0) }
                            holder.bbs_pic2?.setOnClickListener { onImageClickListener.onImageClick(it, 1) }
                            holder.bbs_pic3?.setOnClickListener { onImageClickListener.onImageClick(it, 2) }
                            holder.bbs_pic4?.setOnClickListener { onImageClickListener.onImageClick(it, 3) }
                        }
                        5 -> {
                            holder.bbs_pic?.visibility = View.VISIBLE
                            holder.bbs_pic2?.visibility = View.VISIBLE
                            holder.bbs_pic3?.visibility = View.VISIBLE
                            holder.bbs_pic4?.visibility = View.VISIBLE
                            holder.bbs_pic5?.visibility = View.VISIBLE
                            holder.bbs_pic6?.visibility = View.INVISIBLE
                            holder.layout_pic?.visibility = View.VISIBLE
                            holder.layout_pic2?.visibility = View.VISIBLE

                            Glide.with(context).load(data.detail.imgs[0]).centerCrop().into(holder.bbs_pic)
                            Glide.with(context).load(data.detail.imgs[1]).centerCrop().into(holder.bbs_pic2)
                            Glide.with(context).load(data.detail.imgs[2]).centerCrop().into(holder.bbs_pic3)
                            Glide.with(context).load(data.detail.imgs[3]).centerCrop().into(holder.bbs_pic4)
                            Glide.with(context).load(data.detail.imgs[4]).centerCrop().into(holder.bbs_pic5)
                            holder.bbs_pic?.setOnClickListener { onImageClickListener.onImageClick(it, 0) }
                            holder.bbs_pic2?.setOnClickListener { onImageClickListener.onImageClick(it, 1) }
                            holder.bbs_pic3?.setOnClickListener { onImageClickListener.onImageClick(it, 2) }
                            holder.bbs_pic4?.setOnClickListener { onImageClickListener.onImageClick(it, 3) }
                            holder.bbs_pic5?.setOnClickListener { onImageClickListener.onImageClick(it, 4) }
                        }
                    }
                }

                if (data.detail.sjdw == null) {
                    holder.layout_position?.visibility = View.GONE
                } else {
                    holder.layout_position?.visibility = View.VISIBLE
                    holder.bbs_comment?.text = "0"
                    holder.bbs_location?.text = data.detail.sjdw
                }
//                holder.layout_pic?.visibility = View.GONE
//                holder.layout_pic2?.visibility = View.GONE

            }
            TYPE_LAYOUT -> {
                Log.i("position", "" + position)
                val holder = holder as ViewHolderLayout
                var s = ""
                if (data.trail[position - 1].status != null)
                    s += data.trail[position - 1].status + "\n"
                if (data.trail[position - 1].bm != null)
                    s += "部门：" + data.trail[position - 1].bm + "\n"
                if (data.trail[position - 1].slr != null)
                    s += "处理人：" + data.trail[position - 1].slr + "\n"
                if (data.trail[position - 1].stime != null)
                    s += "处理时间：" + data.trail[position - 1].stime + "\n"
                if (data.trail[position - 1].comment != null)
                    s += "处理意见：" + data.trail[position - 1].comment + "\n"
                holder.info?.text = s
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_HEAD
        } else {
            TYPE_LAYOUT
        }
    }

    inner class ViewHolderHead(var view: View) : RecyclerView.ViewHolder(view) {
        var bbs_head: RoundAngleImageView? = view.find(R.id.bbs_head)
        var bbs_name: TextView? = view.find(R.id.bbs_name)
        var bbs_time: TextView? = view.find(R.id.bbs_time)
        var bbs_title: TextView? = view.find(R.id.bbs_title)
        var bbs_content: TextView? = view.find(R.id.bbs_content)
        var bbs_pic: ImageView? = view.find(R.id.bbs_pic)
        var bbs_pic2: ImageView? = view.find(R.id.bbs_pic2)
        var bbs_pic3: ImageView? = view.find(R.id.bbs_pic3)
        var bbs_pic4: ImageView? = view.find(R.id.bbs_pic4)
        var bbs_pic5: ImageView? = view.find(R.id.bbs_pic5)
        var bbs_pic6: ImageView? = view.find(R.id.bbs_pic6)
        var layout_pic: LinearLayout? = view.find(R.id.layout_pic)
        var layout_pic2: LinearLayout? = view.find(R.id.layout_pic2)
        var bbs_comment: TextView? = view.find(R.id.bbs_comment)
        var bbs_location: TextView? = view.find(R.id.bbs_location)
        var layout_position: LinearLayout? = view.find(R.id.layout_position)
    }

    inner class ViewHolderLayout(var view: View) : RecyclerView.ViewHolder(view) {
        var info: TextView? = view.find(R.id.info)
    }

    interface OnImageClickListener {
        fun onImageClick(view: View, position: Int)
    }


}