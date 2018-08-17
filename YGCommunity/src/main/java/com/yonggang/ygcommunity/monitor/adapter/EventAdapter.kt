package com.yonggang.ygcommunity.monitor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yonggang.ygcommunity.Entry.Event
import com.yonggang.ygcommunity.R
import org.jetbrains.anko.find

class EventAdapter(val data: List<Event.Data.Bean>, val context: Context) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }
        holder.title.text = data[position].title
        holder.time.text = data[position].time
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
