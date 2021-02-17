package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.bean.Stuff;
import com.example.myapplication.myview.MyImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 放置闲置物品的RecyclerView的适配器
 */
public class MyCollectedListAdapter extends RecyclerView.Adapter<MyCollectedListAdapter.ViewHolder> {
    private final List<Stuff> idleGoodsInfoList;

    class ViewHolder extends RecyclerView.ViewHolder {
        MyImageView idlePropertyImgMyImageView;
        TextView idlePropertyTitleTextView;
        TextView idlePropertyPersonTextView;
        TextView idlePropertyLocationTextView;
        TextView idlePropertyPriceTextView;

        public ViewHolder(@NonNull View view) {
            super(view);

            idlePropertyImgMyImageView = view.findViewById(R.id.mv_idleGoodsImg);
            idlePropertyTitleTextView = view.findViewById(R.id.tv_idleGoodsTitle);
            idlePropertyPersonTextView = view.findViewById(R.id.tv_idleGoodsPerson);
            idlePropertyLocationTextView = view.findViewById(R.id.tv_idleGoodsLocation);
            idlePropertyPriceTextView = view.findViewById(R.id.tv_idleGoodsPrice);
        }
    }

    public MyCollectedListAdapter(List<Stuff> stuffs) {
        if (stuffs == null) {
            idleGoodsInfoList = new ArrayList<>();
        } else {
            idleGoodsInfoList = stuffs;
        }
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

        //holder.idlePropertyImgMyImageView.setImageURL(goods.getGoodsCoverImgDir());
        holder.idlePropertyTitleTextView.setText(goods.getName());
        holder.idlePropertyPersonTextView.setText(goods.getOwnerName());
        holder.idlePropertyLocationTextView.setText(goods.getOwnerAddress());
        holder.idlePropertyPriceTextView.setText(goods.getPrice());
    }

    @Override
    public int getItemCount() {
        return idleGoodsInfoList.size();
    }
}
