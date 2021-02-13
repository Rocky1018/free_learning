package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bean.Announce

class AnnouncementAdapter(
    private val announcementList: List<Announce>
) :
    RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>() {
    class ViewHolder(commentItemView: View) : RecyclerView.ViewHolder(commentItemView) {
        var content = commentItemView.findViewById<View>(R.id.tv_announcement_content) as TextView
        var date = commentItemView.findViewById<View>(R.id.tv_announcement_date) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.announcement_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content.text = announcementList[position].content
        holder.date.text = announcementList[position].createdAt
    }

    override fun getItemCount(): Int {
        return announcementList.size
    }
}
