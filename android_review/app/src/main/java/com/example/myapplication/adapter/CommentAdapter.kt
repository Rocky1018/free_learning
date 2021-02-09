package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.bean.CommentItem

class CommentAdapter(private val context: Context, private val commentList: List<CommentItem>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    class ViewHolder(var commentItemView: View) : RecyclerView.ViewHolder(commentItemView) {
        var username = commentItemView.findViewById<View>(R.id.tv_comment_username) as TextView
        var portrait = commentItemView.findViewById<View>(R.id.iv_comment_portrait) as ImageView
        var content = commentItemView.findViewById<View>(R.id.tv_comment_content) as TextView
        var date = commentItemView.findViewById<View>(R.id.tv_comment_date) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.content.text = commentList[position].content
        holder.username.text = commentList[position].username
        holder.date.text = commentList[position].date
        //Glide.with(context).load(commentList[position].portrait).into(holder.portrait)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}
