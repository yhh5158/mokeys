package com.room.mokeys.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.room.mokeys.R;


/**
 * Created by yhh5158 on 2017/4/18.
 */

public class CustomTitleView extends RelativeLayout {
    private String textTitle;
    private TextView tvTitle;
    private boolean isRightshow;
    private ImageView mRightView;
    public CustomTitleView(Context context) {
        super(context);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //从xml的属性中获取到字体颜色与string
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.CustomTitleView);
        textTitle=ta.getString(R.styleable.CustomTitleView_customTitleText);
        isRightshow = ta.getBoolean(R.styleable.CustomTitleView_customTitleRightimage, false);
        ta.recycle();

        //获取到控件
        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.custom_title_layout, this);
        tvTitle=(TextView)findViewById(R.id.titletext);
        mRightView = (ImageView)findViewById(R.id.title_right_icon);
        if(isRightshow){
            mRightView.setVisibility(VISIBLE);
        }else{
            mRightView.setVisibility(INVISIBLE);
        }
        //将控件与设置的xml属性关联
        tvTitle.setText(textTitle);
    }
    public void setTitle(String title){
        tvTitle.setText(title);
    }
    public void setRightShow(boolean show){
        if(show){
            mRightView.setVisibility(VISIBLE);
        }else{
            mRightView.setVisibility(GONE);
        }
    }
    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
