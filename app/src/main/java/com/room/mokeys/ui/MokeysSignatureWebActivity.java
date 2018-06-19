package com.room.mokeys.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.model.MokeySignImagelInfo;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.net.Api;
import com.room.mokeys.pay.PayMainActivity;
import com.room.mokeys.pay.PayRoomMoneyActivity;
import com.room.mokeys.widget.CustomTitleView;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xstatecontroller.XStateController;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by wanglei on 2016/12/31.
 */

public class MokeysSignatureWebActivity extends XActivity {

    @BindView(R.id.web_title)
    CustomTitleView mTtile;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.contentLayout)
    XStateController contentLayout;
    @BindView(R.id.web_bottom_rel)
    RelativeLayout mBootomRel;
    @BindView(R.id.web_bottom_signature)
    LinearLayout mBootomSignatureLin;
    @BindView(R.id.web_bottom_signature_img)
    ImageView mBootomSignatureImg;
    @BindView(R.id.web_bottom_pay)
    TextView mPayTv;
    @BindView(R.id.mokeys_web_signup)
    TextView mPayOrSign;
    String url;
    String desc;
    boolean isPay = false;
    boolean showSignatureLin = true;
    String shareUrl;
    public static final String PARAM_URL = "url";
    public static final String PARAM_TITLE = "title";
    public static final String PARAM_CONTRACTINFO = "contractinfo";
    public static final String PARAM_SIGNORPAY = "signorpay";
    String title;
    MokeysContractInfo mMokeysContractInfo;
    //请求签名
    private static final int REQUEST_CROP_SIGNATURE = 102;
    private static final int REQUEST_PAY = 103;
    @Override
    public void initData(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(PARAM_URL);
        title = getIntent().getStringExtra(PARAM_TITLE);
        mMokeysContractInfo = (MokeysContractInfo)getIntent().getSerializableExtra(PARAM_CONTRACTINFO);
        isPay = getIntent().getBooleanExtra(PARAM_SIGNORPAY,false);
        initTitleandBottom();
        initContentLayout();
        initRefreshLayout();
        initWebView();
    }
    private void initTitleandBottom(){
        mTtile.setTitle(title);
//        mTtile.setRightShow(showSignatureLin);
        if(TextUtils.isEmpty(mMokeysContractInfo.getSigned_contract())){
            mBootomRel.setVisibility(View.VISIBLE);
            mBootomSignatureLin.setVisibility(View.GONE);

        }else{
            if(isPay){
                mBootomRel.setVisibility(View.VISIBLE);
                mBootomSignatureLin.setVisibility(View.GONE);
                mPayOrSign.setText("去支付");
            }else {
                mBootomRel.setVisibility(View.GONE);
                mBootomSignatureLin.setVisibility(View.VISIBLE);
                String picUrl = (String) SPUtil.get(context, Constants.MOKEYS_SIGNATURE_PIC, "");
//            String picUrl = mMokeysContractInfo.getSign_img_url();
                Glide.with(context)
                        .load(picUrl)
                        .into(new SimpleTarget<GlideDrawable>() {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                                mBootomSignatureImg.setBackgroundDrawable(resource);
                            }
                        });
            }
        }
    }
    private void setBottom(String signUrl){
            mBootomRel.setVisibility(View.GONE);
            mBootomSignatureLin.setVisibility(View.VISIBLE);
//            String picUrl = mMokeysContractInfo.getSign_img_url();
            Glide.with(context)
                    .load(signUrl)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            mBootomSignatureImg.setBackgroundDrawable(resource);
                        }
                    });
    }
    private void initContentLayout() {
        contentLayout.loadingView(View.inflate(context, R.layout.view_loading, null));
    }

    private void initRefreshLayout() {
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });

    }

    private void initWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (contentLayout != null)
                        contentLayout.showContent();
                    if (webView != null)
                        url = webView.getUrl();
//                        url = url + "&token=" + SPUtil.get(context, Constants.MOKEYS_A, "");
                } else {
                    if (contentLayout != null)
                        contentLayout.showLoading();
                }
            }
        });
//        webView.setWebViewClient(new InterceptingWebViewClient(context,webView));
//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//                request.getRequestHeaders().put("host","www.mokeys.cn");
//                return super.shouldInterceptRequest(view, request);
//            }
//        });
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheEnabled(true);

        webView.loadUrl(url);
    }

@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
    }

    public static void launch(Activity activity, String url, String title, MokeysContractInfo info,boolean ispay) {
        Router.newIntent(activity)
                .to(MokeysSignatureWebActivity.class)
                .putString(PARAM_URL, url)
                .putString(PARAM_TITLE,title)
                .putBoolean(PARAM_SIGNORPAY,ispay)
                .putSerializable(PARAM_CONTRACTINFO,info)
                .launch();
    }
@OnClick({R.id.web_bottom_rel,R.id.backlin,R.id.title_right_icon,R.id.web_bottom_pay,R.id.web_bottom_signature_lin})
public void onClick(View view){
    switch (view.getId()) {
        case R.id.web_bottom_rel:
            if(isPay){
                Intent mIntent = new Intent();
                mIntent.setClass(context, PayRoomMoneyActivity.class);
                mIntent.putExtra("title","交房租");
                mIntent.putExtra("money",mMokeysContractInfo.getAmount());
                mIntent.putExtra("contractid",mMokeysContractInfo.getCid());
                startActivityForResult(mIntent,REQUEST_PAY);
            }else {
                Intent intent = new Intent();
                intent.setClass(context, SignatureActivity.class);
                startActivityForResult(intent, REQUEST_CROP_SIGNATURE);
            }
            break;
        case R.id.backlin:
            finish();
            break;
        case R.id.title_right_icon:
            break;
        case R.id.web_bottom_pay:
            if((Boolean) SPUtil.get(context, Constants.MOKEYS_SIGNATURE_SUCCESS,false)){
                Intent mIntent = new Intent();
                mIntent.setClass(context, PayRoomMoneyActivity.class);
                mIntent.putExtra("title","交房租");
                mIntent.putExtra("money",mMokeysContractInfo.getAmount());
                mIntent.putExtra("contractid",mMokeysContractInfo.getCid());
                startActivityForResult(mIntent,REQUEST_PAY);
            }else{
                getvDelegate().toastShort("您需要先去签约才能付款！");
            }
            break;
        case R.id.web_bottom_signature_lin:
            Intent intent1 = new Intent();
            intent1.setClass(context, SignatureActivity.class);
            startActivityForResult(intent1,REQUEST_CROP_SIGNATURE);
            break;
        default:
            break;
    }
}

    @Override
    public int getLayoutId() {
        return R.layout.activity_mokeys_signature_web;
    }

    @Override
    public Object newP() {
        return null;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("yhh","requestCode = " +requestCode);
        switch (requestCode) {
            case REQUEST_CROP_SIGNATURE:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    final String cropImagePath = getRealFilePathFromUri(context, uri);
                    SPUtil.put(context, Constants.MOKEYS_SIGNATURE_PIC,cropImagePath);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
                    mBootomSignatureImg.setBackground(new BitmapDrawable(bitMap));
                    showSignatureLin = false;
//                    initTitleandBottom();
                    setBottom(cropImagePath);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    new Thread(){
                        @Override
                        public void run() {
//                            super.run();
                            upLoadFile(cropImagePath,mMokeysContractInfo.getCid());
                        }
                    }.run();
                }
                break;
            case REQUEST_PAY: //调用支付返回
                if (resultCode == RESULT_OK){
                    Bundle mBundle=intent.getExtras(); //data为B中回传的Intent
                    String mStatus = mBundle.getString("status");
                    if("failure".equals(mStatus)){

                    }else if ("success".equals(mStatus)){
                        mPayOrSign.setText("已付款");
                    }
                }
                break;
        }
    }
    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public void upLoadFile(String path,String contrcatid){
        File file=new File(path);
        //file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Api.getMokeysUploadFileService().upload(body,(String)SPUtil.get(context,Constants.MOKEYS_A,""), contrcatid)
                .compose(XApi.<MokeysListModel<MokeySignImagelInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<MokeySignImagelInfo>>getScheduler())
                .compose(this.<MokeysListModel<MokeySignImagelInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<MokeySignImagelInfo>>() {
                    @Override
                    protected void onFail(NetError error) {
                        Log.d("yhh","error type = " +error.getType() + " error = "+ error.getMessage());
                        getvDelegate().toastShort(error.getMessage());
                    }

                    @Override
                    public void onNext(MokeysListModel<MokeySignImagelInfo> gankResults){
//                        Log.d("yhh","gankResults url = " +gankResults.getData().getUrl());

                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            String signUrl = gankResults.getData().get(0).getSign();
                            url =  gankResults.getData().get(0).getSigned_contract() + "&token=" + SPUtil.get(context, Constants.MOKEYS_A, "");
                            webView.loadUrl(url);
                            SPUtil.put(context, Constants.MOKEYS_SIGNATURE_SUCCESS,true);
                            Log.d("yhh","gankResults url = " +signUrl + " web url = " +url);
                            setBottom(signUrl);
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }

                });

    }
}
