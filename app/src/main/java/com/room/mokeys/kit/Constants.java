package com.room.mokeys.kit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.room.mokeys.R;
import com.room.mokeys.model.LoginAccessTokenInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.net.Api;
import com.room.mokeys.ui.PhoneLoginActivity;

import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.VDelegateBase;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by yhh5158 on 2017/4/18.
 */

public class Constants {
    public static String MOKEYS_R = "mokeys_login_r";
    public static String MOKEYS_A = "mokeys_login_a";
    public static String MOKEYS_MOBILEPHONE = "mokeys_mobilephone";
    public static String MOKEYS_AVATAR_URL = "mokeys_avatar_url";
    public static String MOKEYS_USER_STATUS = "mokeys_user_status";
    public static String MOKEYS_SIGNATURE_PIC = "mokeys_signature_pic";
    public static String MOKEYS_SIGNATURE_SUCCESS = "mokeys_signature_success";
    public static String MOKEYS_LOGIN_TIP = "mokeys_login_tip";
    public static String MOKEYS_CHECK_ALIAUTH = "mokeys_check_aliauth";
    public static String ZYGG_NICK_NAME = "zygg_nick_name";
    public static String ZYGG_USER_ADDRESS = "zygg_user_address";
    public static String ZYGG_USER_SEX = "zygg_user_sex";
    public static String ZYGG_USER_LIKE = "zygg_user_like";
    public static String ZYGG_USER_LOGINTYPE = "zygg_user_logintype";
    public static String ZYGG_USER_COOKIE = "zygg_user_cookie";
    public final static int USER_NOT_AUTH = 4001;
    public static final int USER_NOT_PLEDGE = 4002;
    public static final int SER_PAID_PLEDGE = 4003;
    public static final int NOT_FOUND = 4004;
    public static final int HOUSE_ALREADY_DEAL = 4005;
    public static final int HOUSE_NOT_EXIST = 4006;
    public static final int HOUSE_ALREADY_RESERVED = 4007;
    public static final int VERIFY_ERROR = 4011;
    public static final int METHOD_INVALID = 4015;
    public static final int ACCESS_TOKEN_EXPIRE_ERROR = 4021;
    public static final int REFRESH_TOKEN_EXPIRE_ERROR = 4031;
    public static final int USER_HAVE_NOT_SIGNED_CONTRACT = 4041;
    public static boolean isLogin(Context context) {
        String userid = (String) SPUtil.get(context, Constants.MOKEYS_R, "");
        if (!TextUtils.isEmpty(userid)) {
            return true;
        } else {
            return false;
        }
    }
    public static void handErrorCode(int code,Context context){
            switch (code) {
                case USER_NOT_AUTH:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.user_not_auth));
                    break;
                case USER_NOT_PLEDGE:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.user_not_pledge));
                    break;
                case SER_PAID_PLEDGE:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.user_paid_pledge));
                    break;
                case NOT_FOUND:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.not_found));
                    break;
                case HOUSE_ALREADY_DEAL:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.house_already_deal));
                    break;
                case HOUSE_NOT_EXIST:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.house_not_exist));
                    break;
                case HOUSE_ALREADY_RESERVED:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.house_already_reserved));
                    break;
                case VERIFY_ERROR:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.verify_error));
                    break;
                case METHOD_INVALID:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.method_invalid));
                    break;
                case ACCESS_TOKEN_EXPIRE_ERROR:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.access_token_expire_error));
                    getAccesstokenInfo((String) SPUtil.get(context, Constants.MOKEYS_R,""),context);
                    break;
                case REFRESH_TOKEN_EXPIRE_ERROR:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.refresh_token_expire_error));
                    Intent mIntent = new Intent(context,PhoneLoginActivity.class);
                    context.startActivity(mIntent);
                    break;
                case USER_HAVE_NOT_SIGNED_CONTRACT:
                    VDelegateBase.create(context).toastShort(context.getString(R.string.user_have_not_signed_contract));
                    break;
                default:
                    break;

            }
    }
    public  static void getAccesstokenInfo(String r,final Context context) {
        Api.getMokeysAccessTokenInfoService().getData(r)
                .compose(XApi.<MokeysListModel<LoginAccessTokenInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<LoginAccessTokenInfo>>getScheduler())
                .subscribe(new ApiSubscriber<MokeysListModel<LoginAccessTokenInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<LoginAccessTokenInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            SPUtil.put(context, Constants.MOKEYS_A,gankResults.getData().get(0).getA());
                        }else{
                            VDelegateBase.create(context).toastShort(gankResults.getMsg());
                        }
                    }
                });
    }
}
