package com.room.mokeys;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.room.mokeys.maps.beans.BJCamera;
import com.room.mokeys.maps.greendao.DaoMaster;
import com.room.mokeys.maps.greendao.DaoSession;
import com.room.mokeys.maps.greendao.MySQLiteOpenHelper;
import com.room.mokeys.maps.utils.FileUtils;
import com.room.mokeys.maps.utils.JsonUtils;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysReservesInfo;
import com.room.mokeys.testjava.TestC;
import com.uuch.adlibrary.utils.DisplayUtil;

import org.greenrobot.greendao.database.Database;

import java.util.ArrayList;

import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.NetProvider;
import cn.droidlover.xdroidmvp.net.RequestHandler;
import cn.droidlover.xdroidmvp.net.XApi;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

/**
 * Created by wanglei on 2016/12/31.
 */

public class App extends MultiDexApplication {

    private static Context context;
    public static DaoSession mDaoSession;

    private static ArrayList<MokeysReservesInfo> mReservesList;
    private static ArrayList<MokeysContractInfo> mContractsList;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initDisplayOpinion();
        Fresco.initialize(this);
        initGreenDao();
//        initCamera();
        new TestC().setClassA();
        XApi.registerProvider(new NetProvider() {

            @Override
            public Interceptor[] configInterceptors() {
//                Interceptor[] mInterceptor = {new MokeysRequestInterceptor()};
//                return mInterceptor;
                return new Interceptor[0];
            }

            @Override
            public void configHttps(OkHttpClient.Builder builder) {

            }

            @Override
            public CookieJar configCookie() {
                return null;
            }

            @Override
            public RequestHandler configHandler() {
                return null;
            }

            @Override
            public long configConnectTimeoutMills() {
                return 0;
            }

            @Override
            public long configReadTimeoutMills() {
                return 0;
            }

            @Override
            public boolean configLogEnable() {
                return true;
            }

            @Override
            public boolean handleError(NetError error) {
                return false;
            }
        });

    }
    private void initGreenDao() {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this,"music_db",null);
//        Database db = helper.getWritableDb();
//        mDaoSession = new DaoMaster(db).newSession();
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this, "room_db",
                null);
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession(){
        return mDaoSession;
    }
    public static Context getContext() {
        return context;
    }

    private void initCamera(){
        if (!FileUtils.readBooleanFromSharedPreference("init", false)){
            ArrayList<BJCamera> cameraBeans = JsonUtils.prasePaperCameras(FileUtils.readStringFromAsset(getContext(), "beijing_paper.json"));
            for (BJCamera camera: cameraBeans){
                try {
                   getDaoSession().getBJCameraDao().insertOrReplace(camera);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            FileUtils.writeBooleanToSharedPreference("init",true);
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initDisplayOpinion() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(getApplicationContext(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(getApplicationContext(), dm.heightPixels);
    }



    public static ArrayList<MokeysReservesInfo> getReservesList() {
        return mReservesList;
    }

    public static void setReservesList(ArrayList<MokeysReservesInfo> mReservesList) {
        App.mReservesList = mReservesList;
    }

    public static ArrayList<MokeysContractInfo> getmContractsList() {
        return mContractsList;
    }

    public static void setContractsList(ArrayList<MokeysContractInfo> mContractsList) {
        App.mContractsList = mContractsList;
    }
}
