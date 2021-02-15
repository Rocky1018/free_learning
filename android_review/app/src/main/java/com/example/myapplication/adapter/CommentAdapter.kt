package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.bean.Comment

class CommentAdapter(private val context: Context, private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(commentItemView: View) : RecyclerView.ViewHolder(commentItemView) {
        var username = commentItemView.findViewById<View>(R.id.tv_comment_username) as TextView
        var portrait = commentItemView.findViewById<View>(R.id.iv_comment_portrait) as ImageView
        var content = commentItemView.findViewById<View>(R.id.tv_connect) as TextView
        var date = commentItemView.findViewById<View>(R.id.tv_create_time) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content.text = commentList[position].content
        holder.username.text = commentList[position].username
        holder.date.text = commentList[position].date
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}
