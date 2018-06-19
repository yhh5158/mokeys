package com.room.mokeys.shareandlogin.model;

import android.graphics.drawable.Drawable;

import com.tsy.sdk.social.PlatformType;


/**
 * user hufei
 * date 2016/3/18:17:04
 * describe:首页图标和文字
 */
public class IconModel {
    public IconModel() {
    }

    public IconModel(Drawable iconDrawable, String iconText) {
        this.iconId = iconDrawable;
        this.iconText = iconText;
    }

    public IconModel(Drawable iconDrawable, String iconText, PlatformType channelEnum) {
        this.iconId = iconDrawable;
        this.iconText = iconText;
        this.channelEnum = channelEnum;
    }

    public Drawable iconId;
    public String iconText;
    public PlatformType channelEnum;
}
