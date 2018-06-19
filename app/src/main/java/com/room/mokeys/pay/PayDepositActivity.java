package com.room.mokeys.pay;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.room.mokeys.model.MokeysDepositConfigInfo;
import com.room.mokeys.model.MokeysHouseDetailInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysunLocklInfo;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.shareandlogin.ThirdAppId;
import com.room.mokeys.ui.ActivityReservesResult;
import com.room.mokeys.widget.CustomImageView;
import com.tsy.sdk.pay.alipay.Alipay;
import com.tsy.sdk.pay.weixin.WXPay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.base.SimpleRecAdapter;
import cn.droidlover.xdroidmvp.kit.Kits;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xrecyclerview.RecyclerItemCallback;
import cn.droidlover.xrecyclerview.XRecyclerContentLayout;
import cn.droidlover.xrecyclerview.XRecyclerView;

/**
 * Created by yhh5158 on 2017/4/24.
 */

public class PayDepositActivity extends XActivity {
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
    @BindView(R.id.pay_money_contentLayout)
    XRecyclerContentLayout contentLayout;
    String mTitle;
    String mPaymoney;
    Intent mIntent=new Intent();
    Bundle mBundle = new Bundle();
    int amount = 0;
    int time = 0;
    MokeysHouseDetailInfo info;
    private Alipay.AlipayResultCallBack mAliPayback = new Alipay.AlipayResultCallBack() {
        @Override
        public void onSuccess() {
            mPayAlipay.setClickable(true);
//            mBundle.putString("status","success");
//            mIntent.putExtras(mBundle);
//            setResult(RESULT_OK,mIntent);
            getvDelegate().toastShort("定金支付成功");
            Intent mIntent = new Intent(context,ActivityReservesResult.class);
            mIntent.putExtra("roominfo",info);
            mIntent.putExtra("finishtime", Kits.Date.getYmdhms(System.currentTimeMillis()+time));
            startActivity(mIntent);
            finish();
        }

        @Override
        public void onDealing() {
            mPayAlipay.setClickable(true);
//            mBundle.putString("status","success");
//            mIntent.putExtras(mBundle);
//            setResult(RESULT_OK,mIntent);
            getvDelegate().toastShort("定金支付成功");
            Intent mIntent = new Intent(context,ActivityReservesResult.class);
            mIntent.putExtra("roominfo",info);
            mIntent.putExtra("finishtime", Kits.Date.getYmdhms(System.currentTimeMillis()+time));
            startActivity(mIntent);
            finish();
        }

        @Override
        public void onError(int error_code) {
            mPayAlipay.setClickable(true);
            getvDelegate().toastShort("支付失败");
//            mBundle.putString("status","failure");
//            mIntent.putExtras(mBundle);
//            setResult(RESULT_OK,mIntent);
//            finish();
        }

        @Override
        public void onCancel() {
            mPayAlipay.setClickable(true);
            getvDelegate().toastShort("支付取消");
//            mBundle.putString("status","failure");
//            mIntent.putExtras(mBundle);
//            setResult(RESULT_OK,mIntent);
//            finish();
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
        info = (MokeysHouseDetailInfo)getIntent().getSerializableExtra("roominfo");
        initAdapter();
    }




    public void initAdapter(){
        setLayoutManager(contentLayout.getRecyclerView());
        contentLayout.getRecyclerView()
                .setAdapter(getAdapter());
        contentLayout.getRecyclerView().setRefreshEnabled(false);
        contentLayout.getSwipeRefreshLayout().setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
//        contentLayout.getRecyclerView().horizontalDivider(R.color.comment_divider_color,R.dimen.devider_width);
        contentLayout.loadingView(View.inflate(this, R.layout.view_loading, null));
        contentLayout.emptyView(View.inflate(this, R.layout.view_empty, null));
        contentLayout.getRecyclerView().loadMoreFooterView(null);
        loadData((String)SPUtil.get(context,Constants.MOKEYS_A,""));
        if(contentLayout != null){
            contentLayout.showLoading();
        }
    }
    ArrayList<DepositItemModel> list = new ArrayList<>();
    public void loadData(String token) {

        Api.getMokeysDepositConfigService().getData(token)
                .compose(XApi.<MokeysListModel<MokeysDepositConfigInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<MokeysDepositConfigInfo>>getScheduler())
                .compose(this.<MokeysListModel<MokeysDepositConfigInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<MokeysDepositConfigInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<MokeysDepositConfigInfo> gankResults) {
                        Log.d("yuhh","MokeysDepositConfigInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            MokeysDepositConfigInfo mConfiginfo = gankResults.getData().get(0);
                            list = mConfiginfo.getDeposits();
                        }else{
                            for (int i = 0; i < 6; i++) {
                                int count = (i+1)*10;
                                int time = (i+1)*3600;
                                list.add(new DepositItemModel(count,time));
                            }
                        }
                        showData(list);
                    }
                });
    }

    public void showData( final List<DepositItemModel> model) {
        contentLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                getAdapter().setData(model);
                if (getAdapter().getItemCount() < 1) {
                    contentLayout.showEmpty();
                    return;
                }
            }
        },400L);

    }

    PayDepositAdapter adapter;
    public SimpleRecAdapter getAdapter() {
        if (adapter == null) {
            adapter = new PayDepositAdapter(context);
            adapter.setRecItemClick(new RecyclerItemCallback<DepositItemModel, PayDepositAdapter.ViewHolder>() {
                @Override
                public void onItemClick(int position, DepositItemModel model, int tag, PayDepositAdapter.ViewHolder holder) {
                    super.onItemClick(position, model, tag, holder);
                    amount = model.getMoney();
                    time = model.getTime();
                }
            });
        }
        return adapter;
    }
    public void setLayoutManager(XRecyclerView recyclerView) {
        recyclerView.gridLayoutManager(context, 3);

    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_deposit;
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
                gotoPayDeposit((String)SPUtil.get(context,Constants.MOKEYS_A,""));
                mPayAlipay.setClickable(false);
                break;
            case R.id.pay_wechat:
//                goWeixinPay();
                gotoPayDeposit((String)SPUtil.get(context,Constants.MOKEYS_A,""));
//                gotoTestPay((String)SPUtil.get(context,Constants.MOKEYS_A,""));
                mPayWechat.setClickable(false);
                break;
            default:
                break;
        }
    }
    private void gotoTestPay(String token){
        Api.getMokeysWeChatPayService().getData(token,"500")
                .compose(XApi.<MokeysListModel<VerificationCodeInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<VerificationCodeInfo>>getScheduler())
                .compose(this.<MokeysListModel<VerificationCodeInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<VerificationCodeInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<VerificationCodeInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"finish");
                            getvDelegate().toastShort("支付成功");

                            finish();
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

    private void gotoPayDeposit(String token){
        if(amount ==0){
            getvDelegate().toastShort("请选择定金金额！！");
            return;
        }
        //测试金额
//        amount =
        Api.getMokeysDepositService().getData(token,"0.01"+"",info.getHid(),info.getRid())
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
