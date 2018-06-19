package com.room.mokeys.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.room.mokeys.R;
import com.room.mokeys.widget.CustomImageView;
import com.room.mokeys.widget.CustomTitleView;
import com.tsy.sdk.social.PlatformType;
import com.tsy.sdk.social.SocialApi;
import com.tsy.sdk.social.listener.ShareListener;
import com.tsy.sdk.social.share_media.IShareMedia;
import com.tsy.sdk.social.share_media.ShareWebMedia;


import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xdroidmvp.router.Router;
import cn.droidlover.xstatecontroller.XStateController;

/**
 * Created by wanglei on 2016/12/31.
 */

public class MokeysWebActivity extends XActivity {

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
    String url;
    String desc;
    boolean showRight;
    String shareUrl;
    public static final String PARAM_URL = "url";
    public static final String PARAM_TITLE = "title";
    String title;
    @Override
    public void initData(Bundle savedInstanceState) {
        url = getIntent().getStringExtra(PARAM_URL);
        title = getIntent().getStringExtra(PARAM_TITLE);
        initTitleandBottom();
        initContentLayout();
        initRefreshLayout();
        initWebView();
    }
    private void initTitleandBottom(){
        mTtile.setTitle(title);
        mTtile.setRightShow(showRight);
        if(showRight){
            mBootomRel.setVisibility(View.GONE);
        }else{
            mBootomRel.setVisibility(View.VISIBLE);
        }
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
                } else {
                    if (contentLayout != null)
                        contentLayout.showLoading();
                }
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);

        webView.loadUrl(url);
    }

//    private void initToolbar() {
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_white_24dp);
//        getSupportActionBar().setTitle(desc);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                break;
//            case R.id.action_share:
//                AppKit.shareText(this, webView.getTitle() + " " + webView.getUrl() + " 来自「XDroid」");
//                break;
//            case R.id.action_refresh:
//                webView.reload();
//                break;
//            case R.id.action_copy:
//                AppKit.copyToClipBoard(this, webView.getUrl());
//                break;
//            case R.id.action_open_in_browser:
//                AppKit.openInBrowser(this, webView.getUrl());
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
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

    public static void launch(Activity activity, String url,String title) {
        Router.newIntent(activity)
                .to(MokeysWebActivity.class)
                .putString(PARAM_URL, url)
                .putString(PARAM_TITLE,title)
                .launch();
    }
@OnClick({R.id.web_bottom_rel,R.id.backlin,R.id.title_right_icon})
public void onClick(View view){
    switch (view.getId()) {
        case R.id.web_bottom_rel:
                Intent intent = new Intent();
                intent.setClass(context, SignatureActivity.class);
                startActivity(intent);
            break;
        case R.id.backlin:
            finish();
            break;
        case R.id.title_right_icon:
            break;
        default:
            break;
    }
}

    @Override
    public int getLayoutId() {
        return R.layout.activity_mokeys_web;
    }

    @Override
    public Object newP() {
        return null;
    }
}
