package com.tsy.sdk.social.copy;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;

import com.tencent.tauth.Tencent;
import com.tsy.sdk.social.PlatformConfig;
import com.tsy.sdk.social.SSOHandler;
import com.tsy.sdk.social.listener.AuthListener;
import com.tsy.sdk.social.listener.ShareListener;
import com.tsy.sdk.social.share_media.IShareMedia;
import com.tsy.sdk.social.share_media.ShareWebMedia;

/**
 * Created by yhh5158 on 2017/4/20.
 */

public class CopyHandler extends SSOHandler {
    private Context mContext;
    private Activity mActivity;


    private PlatformConfig.Paste mConfig;
    private ShareListener mShareListener;
    @Override
    public void onCreate(Context context, PlatformConfig.Platform config) {
        this.mContext = context;
        this.mConfig = (PlatformConfig.Paste) config;
    }

    @Override
    public void share(Activity activity, IShareMedia shareMedia, ShareListener shareListener) {
        this.mActivity = activity;
        this.mShareListener = shareListener;
        if(shareMedia instanceof ShareWebMedia){
            ShareWebMedia shareWebMedia = (ShareWebMedia) shareMedia;
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
                ClipboardManager copy = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                copy.setText(shareWebMedia.getWebPageUrl());
            } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager copyq = (android.text.ClipboardManager) mActivity
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                copyq.setText(shareWebMedia.getWebPageUrl());
            }
            mShareListener.onComplete(mConfig.getName());
        }
    }
}
