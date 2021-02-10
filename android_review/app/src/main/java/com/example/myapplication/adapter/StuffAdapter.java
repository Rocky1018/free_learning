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
    public static final int TYPE_HEADER = 0; //说明是带有Header的
    public static final int TYPE_FOOTER = 1; //说明是带有Footer的
    public static final int TYPE_NORMAL = 2; //说明是不带有header和footer的

    private final List<Stuff> idleGoodsInfoList;
    private final Context mcontext;

    // RecyclerView顶部
    private View mHeaderView;
    private View mFooterView;

    class ViewHolder extends RecyclerView.ViewHolder {
        MyImageView idlePropertyImgMyImageView;
        LinearLayout idleGoodsItemLinearLayout;
        TextView idlePropertyTitleTextView;
        TextView idlePropertyPersonTextView;
        TextView idlePropertyLocationTextView;
        TextView idlePropertyPriceTextView;

        public ViewHolder(@NonNull View view) {
            super(view);
            //如果是headerview或者是footerview,直接返回
            if (view == mHeaderView) {
                return;
            }
            if (view == mFooterView) {
                return;
            }
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
        if (mHeaderView != null && viewType == TYPE_HEADER) {
            return new ViewHolder(mHeaderView);
        }
        if (mFooterView != null && viewType == TYPE_FOOTER) {
            return new ViewHolder(mFooterView);
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.idle_goods_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_NORMAL) {
            if (holder instanceof ViewHolder) {
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
                    intent.putExtra("goodsId", goods.getStuffId());
                    mcontext.startActivity(intent);
                });

                return;
            }
        } else if (getItemViewType(position) == TYPE_HEADER) {
        } else {
        }

    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return this.mHeaderView;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        notifyItemInserted(0);
    }

    public View getFooterView() {
        return this.mFooterView;
    }

    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view
     */
    @Override
    public int getItemViewType(int position) {
        if (this.mHeaderView == null && this.mFooterView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0) {
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (position == getItemCount() + 1) {
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (this.mHeaderView == null && this.mFooterView == null) {
            return this.idleGoodsInfoList.size();
        } else if (this.mHeaderView == null && this.mFooterView != null) {
            return this.idleGoodsInfoList.size() + 1;
        } else if (this.mHeaderView != null && this.mFooterView == null) {
            return this.idleGoodsInfoList.size() + 1;
        } else {
            return this.idleGoodsInfoList.size() + 2;
        }
    }
}
