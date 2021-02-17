package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.IdleGoodsDetailInfoActivity;
import com.example.myapplication.bean.Stuff;

import java.util.List;

/**
 * 放置闲置物品的RecyclerView的适配器
 */
public class ShopCarAdapter extends RecyclerView.Adapter<ShopCarAdapter.ViewHolder> {

    private final List<Stuff> idleGoodsInfoList;
    private final Context mcontext;


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout idleGoodsItemLinearLayout;
        TextView idlePropertyTitleTextView;
        TextView idlePropertyPersonTextView;
        TextView idlePropertyLocationTextView;
        TextView idlePropertyPriceTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            idleGoodsItemLinearLayout = view.findViewById(R.id.ll_idleGoodsItem);

            idlePropertyTitleTextView = view.findViewById(R.id.tv_idleGoodsTitle);
            idlePropertyPersonTextView = view.findViewById(R.id.tv_idleGoodsPerson);
            idlePropertyLocationTextView = view.findViewById(R.id.tv_idleGoodsLocation);
            idlePropertyPriceTextView = view.findViewById(R.id.tv_idleGoodsPrice);
        }
    }

    public ShopCarAdapter(List<Stuff> idleGoodsInfoList, Context mcontext) {
        this.idleGoodsInfoList = idleGoodsInfoList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.idle_goods_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stuff goods = this.idleGoodsInfoList.get(position);
        holder.idlePropertyTitleTextView.setText(goods.getName());
        holder.idlePropertyPersonTextView.setText(goods.getOwnerName());
        holder.idlePropertyLocationTextView.setText(goods.getOwnerAddress());
        holder.idlePropertyPriceTextView.setText(goods.getPrice() + "");

        holder.idleGoodsItemLinearLayout.setOnClickListener(v -> {
            Toast.makeText(mcontext, R.string.cannot_order_hint, Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public int getItemCount() {
        return idleGoodsInfoList.size();
    }
}
