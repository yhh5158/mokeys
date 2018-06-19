package com.room.mokeys.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.room.mokeys.R;
import com.room.mokeys.kit.BarUtils;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.pay.PayMainActivity;
import com.room.mokeys.pay.PayResult;
import com.room.mokeys.setting.UserSettingActivity;
import com.room.mokeys.ui.contractroom.ContractRoomAcitivity;
import com.room.mokeys.ui.reserveroom.ReserverRoomAcitivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by yhh5158 on 2017/5/24.
 */

public class UserCenterActivity extends XActivity{
    private static final int REQUEST_PAY = 100 ;
    @BindView(R.id.head)
    ImageView mHead;
    @BindView(R.id.mobile)
    TextView mMobile;
    @BindView(R.id.point)
    TextView mPoint;
    @BindView(R.id.main_fl_title)
    RelativeLayout mMainFlTitle;
    @BindView(R.id.main_tv_toolbar_title)
    TextView mMainTvToolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_abl_app_bar)
    AppBarLayout mMainAblAppBar;
    @BindView(R.id.stepview)
    HorizontalStepView stepView;
    @BindView(R.id.steplin)
    LinearLayout mStepLin;
    @Override
    public void initData(Bundle savedInstanceState) {
        BarUtils.setColor(this, Color.parseColor("#5DC9D3"), 0);
        initToolbar();
        mToolbar.setTitle("");
//        setSupportActionBar(mToolbar);

        mMainAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int halfScroll = appBarLayout.getTotalScrollRange() / 2;
                int offSetAbs = Math.abs(verticalOffset);
                float percentage;
                if (offSetAbs < halfScroll) {
                    mMainTvToolbarTitle.setText("摩客钥匙");
                    percentage = 1 - (float) offSetAbs / (float) halfScroll;
                } else {
                    mMainTvToolbarTitle.setText("个人中心");
                    percentage = (float) (offSetAbs - halfScroll) / (float) halfScroll;
                }
                mToolbar.setAlpha(percentage);
            }
        });
    }
    private  void initLoginfo() {
        if (Constants.isLogin(context)) {
            if (TextUtils.isEmpty((String) SPUtil.get(context, Constants.MOKEYS_AVATAR_URL, ""))) {
                mHead.setImageResource(R.mipmap.default_login);
            } else {
                Glide.with(context)
                        .load((String) SPUtil.get(context, Constants.MOKEYS_AVATAR_URL, ""))
//                    .fitCenter()
//                    .load("http://mobilecinema.oss-cn-beijing.aliyuncs.com/cfpl3_34_36/imageContent-68-izhuquuo-m2.jpg")
//                        .transform(new GlideCircleTransform(context))
//                    .placeholder(R.mipmap.expert_default)
//                    .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .fitCenter()
                        .into(mHead);
            }
            String mobile = (String) SPUtil.get(context, Constants.MOKEYS_MOBILEPHONE, "");
            if(TextUtils.isEmpty(mobile)){
                mMobile.setText("点击登录");
            }else{
                mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
                mMobile.setText(mobile);
            }
            mPoint.setVisibility(View.VISIBLE);
        }else{
            mHead.setImageResource(R.mipmap.default_unlogin);
            mMobile.setText("点击登录");
            mPoint.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initStepView();
        initLoginfo();
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
//        getSupportActionBar().setTitle("关于XDroidMvp");
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_usercenter;
    }

    @Override
    public Object newP() {
        return null;
    }
    @OnClick({
            R.id.head,
            R.id.stepview,
            R.id.main_tv_toolbar_back,
            R.id.person_settting,
            R.id.person_reserves,
            R.id.person_contracts
    })
    public void clickEvent(View view) {
        switch (view.getId()) {

            case R.id.head:
            case R.id.stepview:
                String mStatus = (String)SPUtil.get(context,Constants.MOKEYS_USER_STATUS,"");
                if("login".equals(mStatus)){
                    Intent mIntent = new Intent();
                    mIntent.setClass(context, PayMainActivity.class);
                    mIntent.putExtra("title","充值押金");
                    mIntent.putExtra("money","500");
                    startActivityForResult(mIntent,REQUEST_PAY);
                }else if("authentication".equals(mStatus)){
                    Intent mIntent = new Intent(context,ActivityAuthenticaition.class);
                    mIntent.putExtra(Constants.MOKEYS_CHECK_ALIAUTH,true);
                    startActivity(mIntent);
                }else if("finish".equals(mStatus)){
//                    Intent mIntent = new Intent(context,ActivityAuthenticaition.class);
//                    mIntent.putExtra(Constants.MOKEYS_CHECK_ALIAUTH,true);
//                    startActivity(mIntent);
                  getvDelegate().toastShort("个人信息页面");
                }else{
                    Intent mIntent = new Intent(context,PhoneLoginActivity.class);
                    startActivity(mIntent);
                }

                break;
            case R.id.main_tv_toolbar_back:
                finish();
                break;
            case R.id.person_settting:
                Intent mIntent = new Intent(context, UserSettingActivity.class);
                startActivity(mIntent);
                break;

            case R.id.person_reserves:

                Intent intent = new Intent(context, ReserverRoomAcitivity.class);
                startActivity(intent);
                break;
            case R.id.person_contracts:
                Intent intent1 = new Intent(context, ContractRoomAcitivity.class);
                startActivity(intent1);
                break;
        }
    }
    private void initStepView(){
        //-----------------------------this data is example and you can also get data from server-----------------------------
        List<StepBean> stepsBeanList = new ArrayList<>();
        String mStatus = (String)SPUtil.get(context,Constants.MOKEYS_USER_STATUS,"");
        StepBean stepBean0 = null;
        StepBean stepBean2 = null ;
        StepBean stepBean1 = null;
        if("login".equals(mStatus)){
            stepBean0 = new StepBean("手机认证",1);
            stepBean1 = new StepBean("充值押金",-1);
            stepBean2 = new StepBean("实名认证",0);
        }else if("authentication".equals(mStatus)){
            stepBean0 = new StepBean("手机认证",1);
            stepBean1 = new StepBean("充值押金",1);
            stepBean2 = new StepBean("实名认证",-1);
        }else if("finish".equals(mStatus)){
            stepBean0 = new StepBean("手机认证",1);
            stepBean1 = new StepBean("充值押金",1);
            stepBean2 = new StepBean("实名认证",1);
            mStepLin.setVisibility(View.GONE);
        }else{
                stepBean0 = new StepBean("手机认证",0);
                stepBean1 = new StepBean("充值押金",0);
                stepBean2 = new StepBean("实名认证",0);
        }

        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);
        //-----------------------------this data is example and you can also get data from server-----------------------------

        stepView.setStepViewTexts(stepsBeanList)
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getBaseContext(), android.R.color.holo_red_dark))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.black))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getBaseContext(), android.R.color.holo_red_dark))//设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.red))//设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getBaseContext(), R.mipmap.register_progress_complete_dot))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getBaseContext(), R.mipmap.register_progress_undergoing_dot_background))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.attention));//设置StepsViewIndicator AttentionIcon
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
