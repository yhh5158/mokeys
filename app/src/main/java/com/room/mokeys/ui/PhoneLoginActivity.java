package com.room.mokeys.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.kit.Validator;
import com.room.mokeys.model.LoginInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysUserInfo;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.pay.PayMainActivity;
import com.room.mokeys.widget.CustomTitleView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by yhh5158 on 2017/5/24.
 */

public class PhoneLoginActivity extends XActivity {
    private static final int REQUEST_PAY = 100;
    public int num = 0;
    @BindView(R.id.et_phone)
    MClearEditText mEditPhone;
    @BindView(R.id.et_vercode)
    MClearEditText mEditCode;
    @BindView(R.id.btn_get_vercode)
    Button mButtonGetCode;
    @BindView(R.id.phone_login_custom_title)
    CustomTitleView mCustomTitleView;
    @BindView(R.id.tx_service)
    TextView mService;
    @Override
    public void initData(Bundle savedInstanceState) {
        Spanned tmp = Html.fromHtml(getString(R.string.service_clause));
        mService.setText(tmp);
        mEditPhone.setFocusable(true);
        mEditPhone.setFocusableInTouchMode(true);
        mEditPhone.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }

        }, 200);//这里的时间大概是自己测试的
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_phone_login;
    }
    private void onclickConfim(){
        String mEtphone =  mEditPhone.getText().toString();
        if (Validator.isBlank(mEtphone)) {
            getvDelegate().toastShort("请输入您的手机号!");
            return;
        }
        if(Validator.isNotMobile(mEtphone)){
//            et_phone.requestFocus();
//            et_phone.setError("请输入正确的手机号");
            getvDelegate().toastShort("请输入正确的手机号!");
            return;
        }
        getVerificationCode(mEtphone);
        startToRecordTime();
    }

    private void onclickVerification(){
        String mEtphone =  mEditPhone.getText().toString();
        if (Validator.isBlank(mEtphone)) {
            getvDelegate().toastShort("请输入您的手机号!");
            return;
        }
        String mEtCode =  mEditCode.getText().toString();
        if (Validator.isBlank(mEtCode)) {
            getvDelegate().toastShort("请输入验证码");
            return;
        }
        getRandACode(mEtphone,mEtCode);
    }

    private void startToRecordTime(){
        new Thread() {
            @Override
            public void run() {
                num = 90;
                while (num > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mButtonGetCode.setText("" + num + " s");
                            mButtonGetCode.setEnabled(false);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num--;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mButtonGetCode.setText("重新发送");
                        mButtonGetCode.setEnabled(true);
                    }
                });

            }

        }.start();
    }
    @OnClick({R.id.btn_get_vercode,R.id.backlin,R.id.btn_confirm,R.id.tx_service})
        public void onClick(View view){
            switch (view.getId()) {
                case R.id.btn_get_vercode:
                    onclickConfim();
                    break;
                case R.id.backlin:
                    finish();
                    break;
                case R.id.btn_confirm:
                    onclickVerification();
                    break;
                case R.id.tx_service:
                    MokeysWebActivity.launch(this,"http:baidu.com","看房服务条款");
                    break;
                default:
                    break;
            }
    }
    @Override
    public Object newP() {
        return null;
    }

    public void getVerificationCode(String num) {
        Api.getMokeysVerificationService().getLoginData(num)
                .compose(XApi.<MokeysListModel<String>>getApiTransformer())
                .compose(XApi.<MokeysListModel<String>>getScheduler())
                .compose(this.<MokeysListModel<String>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<String>>() {
                    @Override
                    protected void onFail(NetError error) {
                    }

                    @Override
                    public void onNext(MokeysListModel<String> gankResults) {
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                            Log.d("yuhh", gankResults.getData().get(0).getVerification_code());
//                            getvDelegate().toastShort("验证码为：" + gankResults.getData().get(0).getVerification_code());
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

    public void getRandACode(String mobile, final String code) {
        Api.getMokeysLoginService().getLoginData(mobile,code)
                .compose(XApi.<MokeysListModel<LoginInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<LoginInfo>>getScheduler())
                .compose(this.<MokeysListModel<LoginInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<LoginInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<LoginInfo> gankResults) {
                        Log.d("yuhh","code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            Log.d("yuhh", gankResults.getData().get(0).getR());
                            SPUtil.put(context, Constants.MOKEYS_R,gankResults.getData().get(0).getR());
                            SPUtil.put(context, Constants.MOKEYS_A,gankResults.getData().get(0).getA());
                            getUserInfo(gankResults.getData().get(0).getA());
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

    public void getUserInfo(String token) {
        Api.getMokeysUserInfoService().getData(token)
                .compose(XApi.<MokeysListModel<MokeysUserInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<MokeysUserInfo>>getScheduler())
                .compose(this.<MokeysListModel<MokeysUserInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<MokeysUserInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<MokeysUserInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            SPUtil.put(context, Constants.MOKEYS_MOBILEPHONE,gankResults.getData().get(0).getMobile());
                            SPUtil.put(context, Constants.MOKEYS_AVATAR_URL,gankResults.getData().get(0).getAvatar_small());
                            getvDelegate().toastShort("获取个人信息成功");
                            String mStatus = gankResults.getData().get(0).getStatus();
                            if("100".equals(mStatus)){
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"login");
                                Intent mIntent = new Intent();
                                mIntent.setClass(context, PayMainActivity.class);
                                mIntent.putExtra("title","充值押金");
                                mIntent.putExtra("money","500");
                                startActivityForResult(mIntent,REQUEST_PAY);
                                finish();
                            }else if("101".equals(mStatus)){
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"authentication");
                                Intent mIntent = new Intent(context,ActivityAuthenticaition.class);
                                startActivity(mIntent);
                                finish();
                            }else if("111".equals(mStatus)){
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"finish");
                                finish();
                            }else{
                                finish();
                            }
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("yhh","requestCode = " +requestCode);
        switch (requestCode) {
            case REQUEST_PAY: //调用支付返回
                if (resultCode == RESULT_OK){
                    Bundle mBundle=intent.getExtras(); //data为B中回传的Intent
                    String mStatus = mBundle.getString("status");
                    if("failure".equals(mStatus)){
                            finish();
                    }else if ("success".equals(mStatus)){
                        Intent mIntent = new Intent(context,ActivityAuthenticaition.class);
                        mIntent.putExtra(Constants.MOKEYS_CHECK_ALIAUTH,true);
                        startActivity(mIntent);
                        finish();
                    }
                }
                break;
        }
    }
}
