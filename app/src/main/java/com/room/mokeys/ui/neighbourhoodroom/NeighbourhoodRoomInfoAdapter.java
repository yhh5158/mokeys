package com.room.mokeys.ui.neighbourhoodroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.room.mokeys.R;
import com.room.mokeys.model.MokeysEstateDetailInfo;
import com.room.mokeys.model.MokeysHouseInfo;
import com.room.mokeys.model.NeighbourhoodRoomInfo;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by wanglei on 2016/12/10.
 */

public class NeighbourhoodRoomInfoAdapter extends SimpleRecAdapter<MokeysHouseInfo, NeighbourhoodRoomInfoAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public NeighbourhoodRoomInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_yitang_collection;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MokeysHouseInfo item = data.get(position);
        if(item.getRent_type().equals("1")) {
            holder.tvName.setText(item.getNumber());
            holder.tvIntro.setText("整租");
        }else{
            holder.tvName.setText(item.getNumber());
            holder.tvIntro.setText("卧室" +item.getRoom_id());
        }
//        if(item.getActivity()!=null){
//            Activityinfo activityInfo = item.getActivity();
//            holder.tvIntro.setText(activityInfo.getDescription());
//            holder.tvName.setText(activityInfo.getTitle());
//            Glide.with(context)
//                        .load(activityInfo.getPost())
////                    .fitCenter()
////                    .load("http://mobilecinema.oss-cn-beijing.aliyuncs.com/cfpl3_34_36/imageContent-68-izhuquuo-m2.jpg")
////                        .transform(new GlideCircleTransform(context))
////                    .placeholder(R.mipmap.expert_default)
//                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
////                        .crossFade()
//                        .into(holder.tvImage);
//////            ILFactory.getLoader().loadNet(holder.tvImage, activityInfo.getPost(), null);
//            holder.tvTime.setText(Kits.Date.getYmdDot(item.getCreateTime()));
//        }else if(item.getColumn()!=null){
//            ColumnInfo columnInfo = item.getColumn();
//            holder.tvIntro.setText(columnInfo.getTeacherName());
//            holder.tvName.setText(columnInfo.getColumnName());
//            Glide.with(context)
//                    .load(columnInfo.getColumnUrl())
////                    .fitCenter()
////                    .load("http://mobilecinema.oss-cn-beijing.aliyuncs.com/cfpl3_34_36/imageContent-68-izhuquuo-m2.jpg")
////                        .transform(new GlideCircleTransform(context))
////                    .placeholder(R.mipmap.expert_default)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
////                    .crossFade()
//                    .into(holder.tvImage);
////            ILFactory.getLoader().loadNet(holder.tvImage, columnInfo.getColumnUrl(), null);
//            holder.tvTime.setText(Kits.Date.getYmdDot(item.getCreateTime()));
//        }
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

        @BindView(R.id.collection_head)
        ImageView tvImage;
        @BindView(R.id.collection_name)
        TextView tvName;
        @BindView(R.id.collection_teacher_name)
        TextView tvIntro;
        @BindView(R.id.collection_time)
        TextView tvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }

}
