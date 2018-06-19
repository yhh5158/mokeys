package com.room.mokeys.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.room.mokeys.R;

import cn.droidlover.xrecyclerview.LoadMoreUIHandler;

/**
 * Created by yhh5158 on 2017/5/12.
 */

public class LoadmoreView extends LinearLayout implements LoadMoreUIHandler {
    TextView tvMsg;
    ProgressBar progressBar;
    public LoadmoreView(Context context) {
        this(context, null);
    }
    public LoadmoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupView();
    }
    private void setupView() {
        inflate(getContext(), R.layout.view_load_more, this);
        tvMsg = (TextView) findViewById(R.id.tv_msg);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onLoading() {
        setVisibility(VISIBLE);
        tvMsg.setText("加载中...");
        progressBar.setVisibility(VISIBLE);
    }

    @Override
    public void onLoadFinish(boolean hasMore) {
        if (hasMore) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
            tvMsg.setText("已经加载完了");
            progressBar.setVisibility(GONE);
        }
    }
}
