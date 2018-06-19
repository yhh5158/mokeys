package com.room.mokeys.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.room.mokeys.R;
import com.room.mokeys.model.GankResults;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by wanglei on 2016/12/10.
 */

public class GanhuoAdapter extends SimpleRecAdapter<GankResults.Item, GanhuoAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public GanhuoAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_ganhuo;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final GankResults.Item item = data.get(position);
        holder.tvItem.setText(item.getDesc());
        Glide.with(context)
//                    .fitCenter()
                    .load("http://mobilecinema.oss-cn-beijing.aliyuncs.com/cfpl3_34_36/imageContent-68-izhuquuo-m2.jpg")
//                        .transform(new GlideCircleTransform(context))
//                    .placeholder(R.mipmap.expert_default)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .crossFade()
                .into(holder.ivItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tv_item)
        TextView tvItem;
        @BindView(R.id.image_item)
        ImageView ivItem;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }

}
