package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.activity.IdleGoodsDetailInfoActivity;
import com.example.myapplication.bean.Stuff;
import com.example.myapplication.myview.MyImageView;

import java.util.List;

/**
 * 放置闲置物品的RecyclerView的适配器
 */
public class StuffAdapter extends RecyclerView.Adapter<StuffAdapter.ViewHolder> {

    private final List<Stuff> idleGoodsInfoList;
    private final Context mcontext;


    class ViewHolder extends RecyclerView.ViewHolder {
        MyImageView idlePropertyImgMyImageView;
        LinearLayout idleGoodsItemLinearLayout;
        TextView idlePropertyTitleTextView;
        TextView idlePropertyPersonTextView;
        TextView idlePropertyLocationTextView;
        TextView idlePropertyPriceTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            idleGoodsItemLinearLayout = view.findViewById(R.id.ll_idleGoodsItem);
            idlePropertyImgMyImageView = view.findViewById(R.id.mv_idleGoodsImg);

            idlePropertyTitleTextView = view.findViewById(R.id.tv_idleGoodsTitle);
            idlePropertyPersonTextView = view.findViewById(R.id.tv_idleGoodsPerson);
            idlePropertyLocationTextView = view.findViewById(R.id.tv_idleGoodsLocation);
            idlePropertyPriceTextView = view.findViewById(R.id.tv_idleGoodsPrice);
        }
    }

    public StuffAdapter(List<Stuff> idleGoodsInfoList, Context mcontext) {
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
        //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
        Stuff goods = this.idleGoodsInfoList.get(position - 1);
        if (!TextUtils.isEmpty(goods.getImg())) {
            Glide.with(mcontext).load(goods.getImg()).into(holder.idlePropertyImgMyImageView);
        }
        holder.idlePropertyTitleTextView.setText(goods.getName());
        holder.idlePropertyPersonTextView.setText(goods.getOwner().getUsername());
        holder.idlePropertyLocationTextView.setText(goods.getOwner().getAddress());
        holder.idlePropertyPriceTextView.setText(goods.getPrice() + "");

        holder.idleGoodsItemLinearLayout.setOnClickListener(v -> {
            Intent intent = new Intent(mcontext, IdleGoodsDetailInfoActivity.class);
            intent.putExtra("goodsId", goods.getObjectId());
            mcontext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return idleGoodsInfoList.size();
    }
}
