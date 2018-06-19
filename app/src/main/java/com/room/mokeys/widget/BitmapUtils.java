package com.room.mokeys.widget;

/**
 * Created by yhh5158 on 2017/6/1.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.room.mokeys.R;

/**
 * Created by maple on 16/11/14.
 */
public class BitmapUtils {
    private static Context mContext;
    public static Bitmap getNumberBitmap(int iconSize, String number) {
        return getNumberBitmap(iconSize, iconSize / 10, number);
    }

    public static Bitmap getNumberBitmap(int iconSize, int padding, String number) {
        Bitmap bitmap = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF rect = new RectF(0, 0, canvas.getWidth(), canvas.getHeight());
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        // draw background
        paint.setColor(Color.WHITE);
        canvas.drawOval(rect, paint);
        paint.setColor(Color.RED);
        canvas.drawOval(new RectF(padding, padding, rect.width() - padding, rect.height() - padding), paint);
        // draw text
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        if (number.length() == 1) {
            paint.setTextSize(iconSize * 0.8f);

        }else if(number.length() == 2){
            paint.setTextSize(iconSize * 0.5f);
        }else if(number.length() == 3){
            paint.setTextSize(iconSize * 0.4f);
        }

        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        float baseline = (rect.bottom + rect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(number, rect.centerX(), baseline, paint);
        return bitmap;
    }
    public static Bitmap getCounterResources(Context context,String count,int iconSize ) {
        mContext = context;
        Drawable mCounterDrawable = null;
        Bitmap bitmapDrawable = null;

        if (mCounterDrawable == null) {
            // 初始化画布
            mCounterDrawable = mContext.getResources().getDrawable(R.mipmap.hourse_default_small);
            Bitmap bitmapDrawables = ((BitmapDrawable) mCounterDrawable).getBitmap();
            int bitmapX = bitmapDrawables.getWidth();
            int bitmapY = bitmapDrawables.getHeight();
//            int bitmapX = iconSize;
//            int bitmapY = iconSize;
            bitmapDrawable = Bitmap.createBitmap(bitmapX, bitmapY, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmapDrawable);

            // 拷贝图片
            Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG| Paint.DEV_KERN_TEXT_FLAG);
            mPaint.setDither(true);// 防抖动
            mPaint.setFilterBitmap(true);// 用来对Bitmap进行滤波处理，这样，当你选择Drawable时，会有抗锯
            Rect src = new Rect(0, 0, bitmapX, bitmapX);
            Rect dst = new Rect(0, 0, bitmapX, bitmapX);
            canvas.drawBitmap(((BitmapDrawable) mCounterDrawable).getBitmap(), src, dst, mPaint);

            //            canvas.drawBitmap(bitmapDrawable, bitmapX, bitmapY, mPaint);
            //画数字
//            mPaint.setTextAlign(Paint.Align.CENTER);
//            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
//            mPaint.setColor(Color.BLACK);
////            mPaint.setTextSize(20);
//            if (count.length() == 1) {
//                mPaint.setTextSize(iconSize * 0.8f);
//
//            }else if(count.length() == 2){
//                mPaint.setTextSize(iconSize * 0.5f);
//            }else if(count.length() == 3){
//                mPaint.setTextSize(iconSize * 0.4f);
//            }
//            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
//
//            canvas.drawText(count, 56, 56 + (fontMetrics.bottom - fontMetrics.top) / 4, mPaint);
//            canvas.save();

            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(3);
            if (count.length() == 1) {
                mPaint.setTextSize(iconSize * 0.75f);

            }else if(count.length() == 2){
                mPaint.setTextSize(iconSize * 0.45f);
            }else if(count.length() == 3){
                mPaint.setTextSize(iconSize * 0.3f);
            }

            mPaint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            float baseline = (src.bottom + src.top - fontMetrics.bottom - fontMetrics.top) / 2;
            canvas.drawText(count, src.centerX(), baseline, mPaint);
        }
        return  bitmapDrawable;
//        return new BitmapDrawable(getResources(), bitmapDrawable);
    }
}
