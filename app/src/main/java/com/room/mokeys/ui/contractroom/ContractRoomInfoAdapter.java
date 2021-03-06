package com.room.mokeys.ui.contractroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.room.mokeys.R;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysReservesInfo;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by wanglei on 2016/12/10.
 */

public class ContractRoomInfoAdapter extends SimpleRecAdapter<MokeysContractInfo, ContractRoomInfoAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;

    public ContractRoomInfoAdapter(Context context) {
        super(context);
    }
    private  OnButtonClickListener mOnClickListener;
    public interface OnButtonClickListener {
        /**
         * Called when a view has been clicked.
         *
         */
        void onClick(MokeysContractInfo item);
    }

    public void setOnClickListener( OnButtonClickListener l) {
        mOnClickListener = l;
    }
    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_reserver_room;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final MokeysContractInfo item = data.get(position);
//        holder.tvName.setText(item.getCreate_time());
//        holder.tvFinishTime.setText(item.getUpdate_time());
        holder.tvRoom.setText(item.getEstate()+" "+item.getHouse().getNumber()+" 卧室"+item.getHouse().getRoom_id());
        holder.tvName.setText(getString(R.string.sign_begin_time)+item.getCreate_time());
        holder.tvFinishTime.setText(getString(R.string.reserved_end_time)+item.getSign_expire_time());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
            }
        });
        holder.signButton.setText("去支付");
        holder.signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(item);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reserver_room)
        TextView tvRoom;
        @BindView(R.id.reserver_room_head)
        ImageView tvImage;
        @BindView(R.id.reserver_room_info)
        TextView tvName;
        @BindView(R.id.reserver_room_finish)
        TextView tvFinishTime;
        @BindView(R.id.reserver_gosign_button)
        Button signButton;
        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }
    }

}
