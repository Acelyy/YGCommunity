package com.yonggang.ygcommunity.grid.folk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yonggang.ygcommunity.Entry.Event
import com.yonggang.ygcommunity.Entry.Folk
import com.yonggang.ygcommunity.R
import com.yonggang.ygcommunity.grid.event.adapter.EventAdapter
import org.jetbrains.anko.find

class FolkAdapter(var data: MutableList<Folk>, val context: Context): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_folk, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.title.text = data[position].mqsy
        holder.time.text = data[position].xfsj
        return view
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    inner class ViewHolder(var view: View) {
        var title: TextView = view.find(R.id.title)
        var time: TextView = view.find(R.id.time)
    }
}