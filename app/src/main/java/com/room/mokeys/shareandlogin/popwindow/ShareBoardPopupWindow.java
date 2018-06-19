package com.room.mokeys.shareandlogin.popwindow;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.room.mokeys.R;
import com.room.mokeys.shareandlogin.model.IconModel;
import com.tsy.sdk.social.PlatformType;
import com.tsy.sdk.social.SocialApi;
import com.tsy.sdk.social.listener.AuthListener;
import com.tsy.sdk.social.listener.ShareListener;
import com.tsy.sdk.social.share_media.IShareMedia;

import java.util.ArrayList;
import java.util.List;


/**
 * 分享面板
 *
 * @author hufei
 */
public class ShareBoardPopupWindow extends PopupWindow {

//    //需要分享的平台
    public PlatformType[] channelEnumArr = {
        PlatformType.WEIXIN,
        PlatformType.WEIXIN_CIRCLE,
//        PlatformType.QQ,
//        PlatformType.QZONE,
        PlatformType.SINA_WB,
        PlatformType.PASTE
    };

    private View baseView;
    private Resources res;
    private LayoutInflater mLayoutInflater;


    //分享的内容
    private Activity activity;
//    private IShareCallback shareCallback;
//    private ShareAction    shareAction;
    private AuthListener mAuthListener;
    private ShareListener mShareListener;
    private View.OnClickListener dismissListener;
    private IShareMedia mShareMedia;
    private SocialApi mSocialApi;
    public ShareBoardPopupWindow(Activity activity) {
        super(activity);
        this.activity = activity;
        res = activity.getResources();
        mLayoutInflater = LayoutInflater.from(activity);
        this.setWidth(LayoutParams.MATCH_PARENT); // 宽
        this.setHeight(LayoutParams.WRAP_CONTENT); // 高
        this.setAnimationStyle(R.style.timepopwindow_anim_style);
        this.setBackgroundDrawable(new BitmapDrawable());//BitmapDrawable() //new DrawableContainer().getCurrent()
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        baseView = mLayoutInflater.inflate(R.layout.umeng_socialize_shareboard_popupwindow, null);
        setContentView(baseView);
        initEvent();
        initView();
    }

    private void initEvent() {
        dismissListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    //初始化View
    private void initView() {
//        shareAction = new ShareAction(activity)
//                .setShareCallback(new IShareCallback() {
//                    @Override
//                    public void onSuccess(ChannelEnum channelEnum, ShareCallbackModel callbackModel) {
//                        Toast.makeText(activity, "分享成功!", Toast.LENGTH_SHORT).show();
//                        if (shareCallback != null) {
//                            shareCallback.onSuccess(channelEnum, callbackModel);
//                        }
//                    }
//
//                    @Override
//                    public void onCancel(ChannelEnum channelEnum) {
//                        Toast.makeText(activity, "分享取消!", Toast.LENGTH_SHORT).show();
//                        if (shareCallback != null) {
//                            shareCallback.onCancel(channelEnum);
//                        }
//                    }
//
//                    @Override
//                    public void onError(ChannelEnum channelEnum, ShareCallbackModel callbackModel) {
//                        Toast.makeText(activity, "分享出错！", Toast.LENGTH_SHORT).show();
//                        if (shareCallback != null) {
//                            shareCallback.onError(channelEnum, callbackModel);
//                        }
//                    }
//                });

        LinearLayout shareContainer = (LinearLayout) baseView.findViewById(R.id.share_container);
        for (IconModel iconModel : getShareData()) {
            View itemView = mLayoutInflater.inflate(R.layout.umeng_socialize_shareboard_popwindow_item, shareContainer, false);
            ImageView iconImage = (ImageView) itemView.findViewById(R.id.umeng_socialize_shareboard_item_image);
            TextView iconText = (TextView) itemView.findViewById(R.id.umeng_socialize_shareboard_item__name);
            iconImage.setImageDrawable(iconModel.iconId);
            iconText.setText(iconModel.iconText);
            itemView.setTag(iconModel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareBoardPopupWindow.this.dismiss();
                    IconModel model = (IconModel) v.getTag();
//                    shareAction.setChannelEnum(model.channelEnum).share();
                    mSocialApi.doShare(activity,model.channelEnum,mShareMedia,mShareListener);
                }
            });
            shareContainer.addView(itemView);
        }

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().setAttributes(params);
            }
        });

        baseView.findViewById(R.id.id_umeng_pw_outer).setOnClickListener(dismissListener);
        baseView.findViewById(R.id.id_umeng_pw_cancel).setOnClickListener(dismissListener);
        baseView.findViewById(R.id.id_umeng_pw_content).setOnClickListener(null);
    }

    /**
     * 设置要分享的内容
     */
//    public void setShareData(ShareModel shareModel) {
//        shareAction.setShareModel(shareModel);
//    }
//
//    public void setShareCallback(IShareCallback shareCallback) {
//        this.shareCallback = shareCallback;
//    }

    public void setAuthListener(AuthListener listener){
        this.mAuthListener = listener;
    }

    public void setShareListener(ShareListener listener){
        this.mShareListener= listener;
    }

    public void setIShareMedia(IShareMedia sharemedia){
        this.mShareMedia= sharemedia;
    }

    public void setSocialApi(SocialApi socialApi){
        this.mSocialApi = socialApi;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * 设置当前PopupWindow的位置
     */
    public void showPopWindow(View parentView) {
        if (isShowing()) {
            dismiss();
            return;
        }
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        update();
        super.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);

        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.alpha = 0.2f;
        activity.getWindow().setAttributes(params);
    }

    //----------------------------获取数据----------------------------
    private List<IconModel> getShareData() {
        List<IconModel> modelList = new ArrayList<>();
        for (PlatformType channelEnum : channelEnumArr) {
            IconModel model;
            switch (channelEnum) {
                case QQ:
                    model = new IconModel(res.getDrawable(R.mipmap.share_qq), res.getString(R.string.tencent_qq), channelEnum);
                    modelList.add(model);
                    break;
                case WEIXIN:
                    model = new IconModel(res.getDrawable(R.mipmap.share_wechat), res.getString(R.string.tencent_wechat), channelEnum);
                    modelList.add(model);
                    break;
                case WEIXIN_CIRCLE:
                    model = new IconModel(res.getDrawable(R.mipmap.share_wechat_circle), res.getString(R.string.tencent_wechat_circle), channelEnum);
                    modelList.add(model);
                    break;
                case SINA_WB:
                    model = new IconModel(res.getDrawable(R.mipmap.share_sina_weibo), res.getString(R.string.sina), channelEnum);
                    modelList.add(model);
                    break;
                case QZONE:
                    model = new IconModel(res.getDrawable(R.mipmap.share_qzone), res.getString(R.string.tencent_qq_zone), channelEnum);
                    modelList.add(model);
                    break;
                case PASTE:
                    model = new IconModel(res.getDrawable(R.mipmap.ic_link_share), res.getString(R.string.copy), channelEnum);
                    modelList.add(model);
                    break;
            }
        }
        return modelList;
    }
}
