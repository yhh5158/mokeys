package com.room.mokeys.pay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.model.MokeysAlipayInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.shareandlogin.ThirdAppId;
import com.room.mokeys.ui.ActivityAuthenticaition;
import com.room.mokeys.widget.CustomImageView;
import com.tsy.sdk.pay.alipay.Alipay;
import com.tsy.sdk.pay.weixin.WXPay;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by yhh5158 on 2017/4/24.
 */

public class PayMainActivity extends XActivity {
    @BindView(R.id.pay_title_lin)
    View mViewTitle;
    @BindView(R.id.pay_bottom_lin)
    View mViewBottom;
    @BindView(R.id.pay_alipay)
    CustomImageView mPayAlipay;
    @BindView(R.id.pay_wechat)
    CustomImageView mPayWechat;
    @BindView(R.id.pay_text)
    TextView mPayText;
    private Alipay mAlipay;
    @BindView(R.id.pay_title)
    TextView mPayTitleText;
    String mTitle;
    String mPaymoney;
    Intent mIntent=new Intent();
    Bundle mBundle = new Bundle();
    private Alipay.AlipayResultCallBack mAliPayback = new Alipay.AlipayResultCallBack() {
        @Override
        public void onSuccess() {
            SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"authentication");
            mPayAlipay.setClickable(true);
            mBundle.putString("status","success");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

        @Override
        public void onDealing() {
            SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"authentication");
            mPayAlipay.setClickable(true);
            mBundle.putString("status","success");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

        @Override
        public void onError(int error_code) {
            mPayAlipay.setClickable(true);
            mBundle.putString("status","failure");
            getvDelegate().toastShort("支付失败");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

        @Override
        public void onCancel() {
            mPayAlipay.setClickable(true);
            mBundle.putString("status","failure");
            getvDelegate().toastShort("支付取消");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

    };
    private WXPay.WXPayResultCallBack mWXPayCallBack = new WXPay.WXPayResultCallBack() {
        public void onSuccess() {
            mPayWechat.setClickable(true);
            mBundle.putString("status","success");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

        @Override
        public void onError(int error_code) {
            mPayWechat.setClickable(true);
            mBundle.putString("status","failure");
            getvDelegate().toastShort("支付失败");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

        @Override
        public void onCancel() {
            mPayWechat.setClickable(true);
            mBundle.putString("status","failure");
            getvDelegate().toastShort("支付取消");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }
};
    @Override
    public void initData(Bundle savedInstanceState) {
        Log.d("yhh","paymainactivity initdata!!");
        WXPay.init(context, ThirdAppId.WX_APPID);
        mTitle = (String)getIntent().getStringExtra("title");
        mPayTitleText.setText(mTitle);
        mPaymoney = (String)getIntent().getStringExtra("money");
        Spanned tmp = Html.fromHtml(getString(R.string.pay_title,mPaymoney));
        mPayText.setText(tmp);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public Object newP() {
        return null;
    }

    @OnClick({R.id.pay_title_lin,R.id.pay_bottom_lin,R.id.pay_alipay,R.id.pay_wechat})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.pay_title_lin:
                finish();
                break;
            case R.id.pay_bottom_lin:
                finish();
                break;
            case R.id.pay_alipay:
                gotoCashPledge((String)SPUtil.get(context,Constants.MOKEYS_A,""));
                mPayAlipay.setClickable(false);
                break;
            case R.id.pay_wechat:
//                goWeixinPay();
                gotoCashPledge((String)SPUtil.get(context,Constants.MOKEYS_A,""));
//                gotoTestPay((String)SPUtil.get(context,Constants.MOKEYS_A,""));
                mPayWechat.setClickable(false);
                break;
            default:
                break;
        }
    }
//    private void gotoPayDeposit(String token){
//        Api.getMokeysDepositService().getData(token,"10","7677622595505826","1")
//                .compose(XApi.<MokeysListModel<MokeysAlipayInfo>>getApiTransformer())
//                .compose(XApi.<MokeysListModel<MokeysAlipayInfo>>getScheduler())
//                .compose(this.<MokeysListModel<MokeysAlipayInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<MokeysListModel<MokeysAlipayInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//
//                        Log.d("yuhh","onFail!!!" + error);
//                    }
//                    @Override
//                    public void onNext(MokeysListModel<MokeysAlipayInfo> gankResults) {
//                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
//                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                            final String alipayInfo = gankResults.getData().get(0).getAlipay();
//                            Log.d("yuhh","alipayInfo  = " +alipayInfo +" SANBOX ENV = " + EnvUtils.isSandBox());
////                            final String payInfo = "alipay_sdk=alipay-sdk-php-20161101&app_id=2016082000292799&biz_content=%7B%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22subject%22%3A+%22App%E6%94%AF%E4%BB%98%E6%B5%8B%E8%AF%95%22%2C%22out_trade_no%22%3A+%221000888191072560%22%2C%22timeout_express%22%3A+%2230m%22%2C%22total_amount%22%3A+%2250%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F121.42.145.18%3A8777%2Fpayment%2FRechargeCallback&sign_type=RSA2&timestamp=2017-07-31+09%3A48%3A57&version=1.0&sign=0fYIh6ngxeFYmCAGKoJVe3Gq1H1j3xyJprMdMJ%2FaAZjVbpPMx1zA%2Fw%2BA4iGCtaAEU0rfhmfLfFg0Dd6aORE8bNrGEpkHiNI%2Fx8nHP6HYZmCuBKey%2FlXaj8KTTZFnxkpRCeA0NY%2BIcyFW5gMK0KXDv8FlybgjEB%2BHC5ilhBimYNCfwIwJt%2Bjtuy%2BKscNbX%2FBP%2F9XmpeNVLMb3Elr%2BhBE%2FBNgqXwWLg23KLNwE5WWSJyXkZ0RKwDjdwayyvRDN86LzxhXvqjMhco9%2BNYS20D5Z2H9BPgo2KucAV9p9x8BdCsvmIEYhHdrYXfCz7BPYTS69jAMVQ4RaaN3ZeID5Y%2FV2KA%3D%3D";
//                            mAlipay = new Alipay(context, alipayInfo,mAliPayback);
//                            mAlipay.doPay();
//                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
//                        }
//                    }
//                });
//    }
//    private void gotoTestPay(String token){
//        Api.getMokeysWeChatPayService().getData(token,"500")
//                .compose(XApi.<MokeysListModel<VerificationCodeInfo>>getApiTransformer())
//                .compose(XApi.<MokeysListModel<VerificationCodeInfo>>getScheduler())
//                .compose(this.<MokeysListModel<VerificationCodeInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<MokeysListModel<VerificationCodeInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//
//                        Log.d("yuhh","onFail!!!" + error);
//                    }
//                    @Override
//                    public void onNext(MokeysListModel<VerificationCodeInfo> gankResults) {
//                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
//                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                            SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"finish");
//                            getvDelegate().toastShort("支付成功");
//                            finish();
//                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
//                        }
//                    }
//                });
//    }

    private void gotoCashPledge(String token){
        Api.getMokeyscashPledgeService().getData(token)
                .compose(XApi.<MokeysListModel<MokeysAlipayInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<MokeysAlipayInfo>>getScheduler())
                .compose(this.<MokeysListModel<MokeysAlipayInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<MokeysAlipayInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<MokeysAlipayInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            final String alipayInfo = gankResults.getData().get(0).getAlipay();
                            Log.d("yuhh","alipayInfo  = " +alipayInfo +" SANBOX ENV = " +EnvUtils.isSandBox());
                            mAlipay = new Alipay(context, alipayInfo,mAliPayback);
                            mAlipay.doPay();
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

    private void goAlipay(){
//        Api.getPayAlipayService().getdata(mColumnInfo.getColumnId(),mColumnInfo.getColumnSubCost(),mColumnInfo.getColumnName(),"")
//                .compose(XApi.<ArtistBaseOneModel<AliPayReturn>>getApiTransformer())
//                .compose(XApi.<ArtistBaseOneModel<AliPayReturn>>getScheduler())
//                .compose(this.<ArtistBaseOneModel<AliPayReturn>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<ArtistBaseOneModel<AliPayReturn>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//                        Log.d("yhh","error type = " +error.getType() + " error = "+ error.getMessage());
//                        getvDelegate().toastShort(error.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ArtistBaseOneModel<AliPayReturn> gankResults){
//                        if(gankResults.getReturnCode() == 1){
//                            mAlipay = new Alipay(context,gankResults.getData().getPaydata(),mAliPayback);
//                            mAlipay.doPay();
//                        }else{
//                            getvDelegate().toastShort(gankResults.getReturnMessage());
//                        }
//                    }
//                });

    }
    private void goWeixinPay(){
//        Api.getPayWechatPayService().getdata(mColumnInfo.getColumnId(),mColumnInfo.getColumnSubCost(),mColumnInfo.getColumnName(),"")
//                .compose(XApi.<ArtistBaseOneModel<WechatPayReturn>>getApiTransformer())
//                .compose(XApi.<ArtistBaseOneModel<WechatPayReturn>>getScheduler())
//                .compose(this.<ArtistBaseOneModel<WechatPayReturn>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<ArtistBaseOneModel<WechatPayReturn>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//                        Log.d("yhh","error type = " +error.getType() + " error = "+ error.getMessage());
//                        getvDelegate().toastShort(error.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(ArtistBaseOneModel<WechatPayReturn> gankResults){
//                        if(gankResults.getReturnCode() == 1){
//                            PayReq req = new PayReq();
//                            req.appId = ThirdAppId.WX_APPID;
//                            req.partnerId = gankResults.getData().getPartnerid();
//                            req.prepayId = gankResults.getData().getPrepayid();
////                            req.packageValue = gankResults.getData().getPackagevalue();
//                            req.packageValue = "Sign=WXPay";
//                            req.nonceStr = gankResults.getData().getNoncestr();
//                            req.timeStamp = gankResults.getData().getTimestamp();
//                            req.sign = gankResults.getData().getSign();
//                            WXPay.getInstance().doPay(req,mWXPayCallBack);
//                        }else{
//                            getvDelegate().toastShort(gankResults.getReturnMessage());
//                        }
//                    }
//                });

    }


}
