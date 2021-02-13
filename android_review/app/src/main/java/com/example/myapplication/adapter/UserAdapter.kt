package com.example.myapplication.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.bean.User

class UserAdapter(private val context: Context, private val userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    class ViewHolder(commentItemView: View) : RecyclerView.ViewHolder(commentItemView) {
        var username = commentItemView.findViewById<View>(R.id.tv_comment_username) as TextView
        var portrait = commentItemView.findViewById<View>(R.id.iv_comment_portrait) as ImageView
        var connect = commentItemView.findViewById<View>(R.id.tv_connect) as TextView
        var date = commentItemView.findViewById<View>(R.id.tv_create_time) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.connect.text = "联系方式：" + userList[position].phoneNum
        holder.username.text = userList[position].username
        holder.date.text = "注册时间：" + userList[position].createdAt
        try {
            Glide.with(context).load(userList[position].portrait).into(holder.portrait)
        } catch (e: Exception) {
            Log.w("onBindViewHolder", "error.${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
