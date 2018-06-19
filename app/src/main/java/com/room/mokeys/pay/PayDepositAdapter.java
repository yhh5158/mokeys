package com.room.mokeys.pay;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.room.mokeys.R;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

/**
 * Created by wanglei on 2016/12/10.
 */

public class PayDepositAdapter extends SimpleRecAdapter<DepositItemModel, PayDepositAdapter.ViewHolder> {

    public static final int TAG_VIEW = 0;
    private int lastPressIndex = -1;
    public PayDepositAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder newViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_pay_deposit;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final DepositItemModel item = data.get(position);
        String text = item.getMoney() + "元" +"/" +item.getTime()/3600 +"小时";
        holder.tvMoney.setText(text);
        if (position == lastPressIndex) {
            holder.tvMoney.setSelected(true);
            holder.tvMoney.setTextColor(ContextCompat.getColor(context, R.color.white));

        } else {
            holder.tvMoney.setSelected(false);
            holder.tvMoney.setTextColor(ContextCompat.getColor(context, R.color.blue_500));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getRecItemClick() != null) {
                    getRecItemClick().onItemClick(position, item, TAG_VIEW, holder);
                }
                if (lastPressIndex == position) {
                    lastPressIndex = -1;
                } else {
                    lastPressIndex = position;
                }
                notifyDataSetChanged();
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv)
        TextView tvMoney;

        public ViewHolder(View itemView) {
            super(itemView);
            KnifeKit.bind(this, itemView);
        }

//        void setData(DepositItemModel data) {
//            if (data != null) {
//                String text = data.getMoney() + "元";
//                tvMoney.setText(text);
//                if (getAdapterPosition() == lastPressIndex) {
//                    tvMoney.setSelected(true);
//                    tvMoney.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white));
//
//                } else {
//                    tvMoney.setSelected(false);
//                    tvMoney.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
//                }
//
//            }
//        }
    }
}
