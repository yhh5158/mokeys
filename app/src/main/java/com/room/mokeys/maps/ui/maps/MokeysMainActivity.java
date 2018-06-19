package com.room.mokeys.maps.ui.maps;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.room.mokeys.R;
import com.room.mokeys.pay.PayResult;

import java.util.Map;

import cn.droidlover.xdroidmvp.mvp.XActivity;


public class MokeysMainActivity extends XActivity implements MapsFragment.OnFragmentInteractionListener {

//private Handler mHandler = new Handler();
    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if (getMapsFragment().isInSearch()) {
            getMapsFragment().exitSearch();
        }
//        else if(getMapsFragment().getAdManager()!= null && getMapsFragment().getAdManager().getIsShowing()){
//            getMapsFragment().getAdManager().dismissAdDialog();
//        }
        else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MokeysMainActivity.this, "再按一次退出程序", Toast.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
//                finish();
            }
        }
    }
    private MapsFragment getMapsFragment() {
        MapsFragment mapsFragment = (MapsFragment) getSupportFragmentManager().findFragmentByTag("mapfragment");
        if (mapsFragment == null) {
            mapsFragment = new MapsFragment();
        }
        return mapsFragment;
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.setting_fragment_frame, getMapsFragment(),"mapfragment").commit();
        }
        ImageButton back = (ImageButton) findViewById(R.id.setting_back_image);
//        testGetdata();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                payV2();
            }
        });
//        initAdData();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showAd();
//            }
//        },3000L);
//        showAd();
//        testProvider();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_new;
    }

    @Override
    public Object newP() {
        return null;
    }

    public void testProvider(){
        Uri testUri = Uri.parse("content://app.greyshirts.sslcapture.captureprovider/captureset");
        Cursor testCursor = context.getContentResolver().query(testUri,null,null,null,"captureset_start_time DESC limit 1");
        if(testCursor!=null && testCursor.moveToNext()){
            long id = testCursor.getLong(testCursor.getColumnIndex("captureset_set_id"));
            Log.d("yuhh"," id = " +id);
            testCursor.close();
        }
    }

}
