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
import com.example.myapplication.bean.Order

class OrderAdapter(private val context: Context, private val orderList: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    class ViewHolder(orderItemView: View) : RecyclerView.ViewHolder(orderItemView) {
        var username = orderItemView.findViewById<View>(R.id.username) as TextView
        var stuffImage = orderItemView.findViewById<View>(R.id.stuff_image) as ImageView
        var orderStatus = orderItemView.findViewById<View>(R.id.order_status) as TextView
        var date = orderItemView.findViewById<View>(R.id.tv_create_time) as TextView
        var stuffName = orderItemView.findViewById<View>(R.id.stuff_name) as TextView
        var stuffPrice = orderItemView.findViewById<View>(R.id.stuff_price) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.order_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        orderList[position].stuff.apply {
            holder.username.text = owner?.nickname
            holder.stuffName.text = name
            holder.stuffPrice.text = price
        }
        holder.orderStatus.text = orderList[position].orderStatus
        holder.date.text = orderList[position].createdAt

        try {
            Glide.with(context).load(orderList[position].stuff.img).into(holder.stuffImage)
        } catch (e: Exception) {
            Log.w("onBindViewHolder", "error ${e.message}")
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }
}
