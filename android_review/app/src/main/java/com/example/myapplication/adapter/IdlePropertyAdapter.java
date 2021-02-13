package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.DoMainIdleProperty;

import java.util.List;

/**
 * 放置闲置物品的RecyclerView的适配器
 */
public class IdlePropertyAdapter extends RecyclerView.Adapter<IdlePropertyAdapter.ViewHolder> {
    private final List<DoMainIdleProperty> idleProperties;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView idlePropertyImgImageView;
        TextView idlePropertyTitleTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.idlePropertyImgImageView = view.findViewById(R.id.iv_idlePropertyImg);
            this.idlePropertyTitleTextView = view.findViewById(R.id.tv_idleGoodsTitle);
        }
    }

    public IdlePropertyAdapter(List<DoMainIdleProperty> idleProperties) {
        this.idleProperties = idleProperties;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.idle_property_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DoMainIdleProperty doMainIdleProperty = idleProperties.get(position);

        holder.idlePropertyImgImageView.setImageResource(doMainIdleProperty.getImgId());
        holder.idlePropertyTitleTextView.setText(doMainIdleProperty.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.idleProperties.size();
    }
}
