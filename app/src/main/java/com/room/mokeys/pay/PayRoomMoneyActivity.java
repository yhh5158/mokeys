package com.room.mokeys.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.EnvUtils;
import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.model.MokeysAlipayInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.net.Api;
import com.room.mokeys.shareandlogin.ThirdAppId;
import com.room.mokeys.widget.CustomImageView;
import com.tsy.sdk.pay.alipay.Alipay;
import com.tsy.sdk.pay.weixin.WXPay;

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

public class PayRoomMoneyActivity extends XActivity {
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
    String mContractId;
    Intent mIntent=new Intent();
    Bundle mBundle = new Bundle();
    private Alipay.AlipayResultCallBack mAliPayback = new Alipay.AlipayResultCallBack() {
        @Override
        public void onSuccess() {
            mPayAlipay.setClickable(true);
            mBundle.putString("status","success");
            mIntent.putExtras(mBundle);
            setResult(RESULT_OK,mIntent);
            finish();
        }

        @Override
        public void onDealing() {
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
        WXPay.init(context, ThirdAppId.WX_APPID);
        mTitle = (String)getIntent().getStringExtra("title");
        mPayTitleText.setText(mTitle);
        mPaymoney = (String)getIntent().getStringExtra("money");
        Spanned tmp = Html.fromHtml(getString(R.string.pay_title,mPaymoney));
        mContractId = (String)getIntent().getStringExtra("contractid");
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
//                gotoTestPay((String)SPUtil.get(context,Constants.MOKEYS_A,""));
//                goAlipay();
                gotoPay((String)SPUtil.get(context,Constants.MOKEYS_A,""));
                mPayAlipay.setClickable(false);
                break;
            case R.id.pay_wechat:
//                goWeixinPay();
                gotoPay((String)SPUtil.get(context,Constants.MOKEYS_A,""));
                mPayWechat.setClickable(false);
                break;
            default:
                break;
        }
    }


    private void gotoPay(String token){
        Api.getMokeysPayConstractService().getData(token,mContractId)
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
                            Log.d("yhh","alipayInfo  = " +alipayInfo +" SANBOX ENV = " + EnvUtils.isSandBox());
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
