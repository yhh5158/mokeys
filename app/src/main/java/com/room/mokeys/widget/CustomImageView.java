package com.room.mokeys.widget;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.room.mokeys.R;


/**
 * Created by yhh5158 on 2017/4/18.
 */

public class CustomImageView extends LinearLayout {
    private String customText;
    private int customTextColor;
    private float customTextSize;
    private float customImageSize;
    private float marginSize;
    private Drawable customImage;

    private TextView mText;
    private ImageView mImage;
    private Context mContext;
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //从xml的属性中获取到字体颜色与string
        mContext = context;
        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        customText=ta.getString(R.styleable.CustomImageView_customImageText);
        customTextColor= ta.getColor(R.styleable.CustomImageView_customImageTextColor, ContextCompat.getColor(context, R.color.mokeys_textcolor));
        customTextSize = ta.getDimensionPixelSize(R.styleable.CustomImageView_customImageTextSize,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        customImage = ta.getDrawable(R.styleable.CustomImageView_customImage);
        customImageSize = ta.getDimensionPixelSize(R.styleable.CustomImageView_customImageSize,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics()));
        marginSize = ta.getDimensionPixelSize(R.styleable.CustomImageView_customImageMargin,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        ta.recycle();

//        //获取到控件
//        //加载布局文件，与setContentView()效果一样
        LayoutInflater.from(context).inflate(R.layout.person_custom_lin, this);
        mImage=(ImageView) findViewById(R.id.image);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = (int)customImageSize;
        lp.height = (int)customImageSize;
        lp.setMargins(0,0,0,(int)marginSize);
        mImage.setLayoutParams(lp);
//
//        mImage.setMaxWidth(screenWidth);
//        mImage.setMaxHeight(screenWidth );
        mText=(TextView)findViewById(R.id.text);
        Log.d("yhh","customTextSize = " +customTextSize + " 16sp = " + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
//
//        //将控件与设置的xml属性关联
        mText.setText(customText);
        mText.setTextColor(customTextColor);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX ,customTextSize);
        mImage.setImageDrawable(customImage);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextColor(int colorId){
        mText.setTextColor(mContext.getResources().getColor(colorId));
    }
    public void setImage(int imageId){
        mImage.setImageResource(imageId);
    }
    public void setText(String text){
        mText.setText(text);
    }
    public View getImageView(){
        return mImage;
    }
//
//    public ItemInfoLinealayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

//    public ItemInfoLinealayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        //从xml的属性中获取到字体颜色与string
//        TypedArray ta=context.obtainStyledAttributes(attrs, R.styleable.ItemInfoLinealayout);
//        textLeft=ta.getString(R.styleable.ItemInfoLinealayout_leftText);
//        textRight=ta.getString(R.styleable.ItemInfoLinealayout_rightText);
//        ta.recycle();
//
//        //获取到控件
//        //加载布局文件，与setContentView()效果一样
//        LayoutInflater.from(context).inflate(R.layout.detail_item, this);
//        tvLeft=(TextView)findViewById(R.id.lefttext);
//        tvRight=(TextView)findViewById(R.id.righttext);
//
//        //将控件与设置的xml属性关联
//        tvLeft.setText(textLeft);
//        tvRight.setText(textRight);
//    }

//    public void setTextRight(String text){
//        tvRight.setText(text);
//
//  }

    public void startShakeByPropertyAnim(View view, float scaleSmall, float scaleLarge, float shakeDegrees, long duration) {
        if (view == null) {
            return;
        }
        //TODO 验证参数的有效性

        //先变小后变大
        PropertyValuesHolder scaleXValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );
        PropertyValuesHolder scaleYValuesHolder = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1.0f),
                Keyframe.ofFloat(0.25f, scaleSmall),
                Keyframe.ofFloat(0.5f, scaleLarge),
                Keyframe.ofFloat(0.75f, scaleLarge),
                Keyframe.ofFloat(1.0f, 1.0f)
        );

        //先往左再往右
        PropertyValuesHolder rotateValuesHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(0.1f, -shakeDegrees),
                Keyframe.ofFloat(0.2f, shakeDegrees),
                Keyframe.ofFloat(0.3f, -shakeDegrees),
                Keyframe.ofFloat(0.4f, shakeDegrees),
                Keyframe.ofFloat(0.5f, -shakeDegrees),
                Keyframe.ofFloat(0.6f, shakeDegrees),
                Keyframe.ofFloat(0.7f, -shakeDegrees),
                Keyframe.ofFloat(0.8f, shakeDegrees),
                Keyframe.ofFloat(0.9f, -shakeDegrees),
                Keyframe.ofFloat(1.0f, 0f)
        );

        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleXValuesHolder, scaleYValuesHolder, rotateValuesHolder);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }
}
