package com.room.mokeys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.base.XFragmentAdapter;
import com.room.mokeys.R;
import com.room.mokeys.maps.ui.maps.*;
import com.room.mokeys.model.GankResults;
import com.room.mokeys.model.LoginInfo;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.net.Api;

import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by wanglei on 2016/12/22.
 */

public class MainActivity extends XActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    List<Fragment> fragmentList = new ArrayList<>();
    String[] titles = {"首页", "干货", "妹子"};

    XFragmentAdapter adapter;


    @Override
    public void initData(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);

        fragmentList.clear();
        fragmentList.add(HomeFragment.newInstance());
        fragmentList.add(GanhuoFragment.newInstance());
        fragmentList.add(GirlFragment.newInstance());

        if (adapter == null) {
            adapter = new XFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);
//        testGetdata();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getOptionsMenuId() {
        return R.menu.menu_main;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_droid:
                Intent mItent = new Intent();

                startActivity(mItent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Object newP() {
        return null;
    }


//    public void testGetdata() {
//        Api.getMokeysLoginService().getLoginData("15210965669")
//                .compose(XApi.<MokeysBaseModel<LoginInfo>>getApiTransformer())
//                .compose(XApi.<MokeysBaseModel<LoginInfo>>getScheduler())
//                .compose(this.<MokeysBaseModel<LoginInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<MokeysBaseModel<LoginInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//                    }
//
//                    @Override
//                    public void onNext(MokeysBaseModel<LoginInfo> gankResults) {
//                        Log.d("yhh",gankResults.getData().getR());
//                    }
//                });
//    }

//    public class Thread1 extends Thread{
//        private Handler handler1;
//        public Handler getHandler(){//注意哦，在run执行之前，返回的是null
//            return handler1;
//        }
//        @Override
//        public void run() {
//
//            Looper.prepare();
//            handler1 = new Handler(){
//                public void handleMessage(android.os.Message msg) {
//                    //这里处理消息
//                    Log.i("MThread", "收到消息了："+Thread.currentThread().getName()+"----"+msg.obj);
//                };
//            };
//            Looper.loop();
//        }
//    }


//    public class Thread2 extends Thread{
//        @Override
//        public void run() {
//
//            for(int i=0; i<10; i++){
//                Message msg = Message.obtain();
//                msg.what = 1;
//                msg.obj = System.currentTimeMillis()+"";
//                handler1.sendMessage(msg);
//                Log.i("MThread", Thread.currentThread().getName()+"----发送了消息！"+msg.obj);
//                SystemClock.sleep(1000);
//            }
//
//        }
//    }
    HandlerThread mHandlerTh = new HandlerThread("handler-threan"){
    @Override
    public void run() {
        super.run();
    }
};
 Handler childHandler = null;
 private void testMultiThread(){

     new Thread(new Runnable() {

         @Override
         public void run() {
             Looper loop = Looper.myLooper();
             Message msg = childHandler.obtainMessage();
             msg.obj = "btn2当中子线程";
             childHandler.sendMessage(msg);
         }
     }).start();
     new Thread(new Runnable() {

         @Override
         public void run() {
             String msg;
             Looper.prepare();

             childHandler = new Handler() {
                 @Override
                 public void handleMessage(Message msg) {
                     super.handleMessage(msg);

                     System.out.println("这个消息是从-->>" + msg.obj+ "过来的，在" + "btn的子线程当中" + "中执行的");

                 }

             };
             Looper.loop();//开始轮循

         }
     }).start();
 }

 public class myThread extends  Thread{
     @Override
     public void run() {
         super.run();
     }
 }
}
