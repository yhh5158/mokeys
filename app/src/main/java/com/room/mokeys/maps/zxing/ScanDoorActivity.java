package com.room.mokeys.maps.zxing;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysHouseDetailInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysunLocklInfo;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.ui.ActivityRoomResult;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import cn.droidlover.xstatecontroller.XStateController;
import io.reactivex.functions.Consumer;

/**
 * 定制化显示扫描界面
 */
public class ScanDoorActivity extends XActivity {

    private CaptureFragment captureFragment;
    @BindView(R.id.contentLayout)
    XStateController contentLayout;
    Handler mHandler = new Handler();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//        captureFragment = new CaptureFragment();
//        // 为二维码扫描界面设置定制化界面
//        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
//        captureFragment.setAnalyzeCallback(analyzeCallback);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
//
//        initView();
//    }

    public static boolean isOpen = false;

    private void initView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }

            }
        });
    }


    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            if (contentLayout != null) {
                contentLayout.showLoading();
            }
            Log.d("yhh","scan url " +result);
            if(result.startsWith("http://121.42.145.18/house/unlock?")){
                Map<String,String> urlMap = getUrlParams(result.substring(result.indexOf("?")+1,result.length()));
                String hourseId = urlMap.get("house_id");
                String roomId = urlMap.get("room_id");
                getLockInfo((String) SPUtil.get(context, Constants.MOKEYS_A,""),hourseId,roomId);
            }else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, ActivityRoomResult.class);
                        startActivity(intent);
                        finish();
                    }
                }, 5000);
            }
//            SecondActivity.this.setResult(RESULT_OK, resultIntent);
//            SecondActivity.this.finish();
        }
        /**
         * 将url参数转换成map
         * @param param aa=11&bb=22&cc=33
         * @return
         */
        public  Map<String, String> getUrlParams(String param) {
            Map<String, String> map = new HashMap<String, String>(0);
            if (TextUtils.isEmpty(param)) {
                return map;
            }
            String[] params = param.split("&");
            for (int i = 0; i < params.length; i++) {
                String[] p = params[i].split("=");
                if (p.length == 2) {
                    map.put(p[0], p[1]);
                }
            }
            Log.d("yhh","map = "+map.toString());
            return map;
        }

        public void getLockInfo(String token,String horse_id,String room_id) {
            Log.d("yhh","token = "+token);
            Api.getMokeysUnlockService().getData(token,horse_id,room_id)
                    .compose(XApi.<MokeysListModel<MokeysunLocklInfo>>getApiTransformer())
                    .compose(XApi.<MokeysListModel<MokeysunLocklInfo>>getScheduler())
                    .compose(ScanDoorActivity.this.<MokeysListModel<MokeysunLocklInfo>>bindToLifecycle())
                    .subscribe(new ApiSubscriber<MokeysListModel<MokeysunLocklInfo>>() {
                        @Override
                        protected void onFail(NetError error) {
                            Log.d("yhh","onFail = "+error.getMessage());
                            ScanDoorActivity.this.finish();
                        }

                        @Override
                        public void onNext(MokeysListModel<MokeysunLocklInfo> gankResults) {
                            if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                                int roomSize = gankResults.getData().get(0).getHouses().size();
                                Log.d("yhh","roomSize = "+roomSize);
                                if(roomSize==1){
                                    Intent intent = new Intent(context, ActivityRoomResult.class);
                                    MokeysHouseDetailInfo info = gankResults.getData().get(0).getHouses().get(0);
//                                    intent.putExtra("roominfo","您正在看的房子是"+info.getNumber()+"的卧室"+info.getRid());
                                    intent.putExtra("roominfo",info);
                                    startActivity(intent);
                                }else{
                                    getvDelegate().toastShort("开锁大门成功！");
                                }
                            }else{
//                                getvDelegate().toastShort(gankResults.getMsg());
                                Constants.handErrorCode(gankResults.getCode(),context);
                            }
                            ScanDoorActivity.this.finish();
                        }
                    });
        }
        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScanDoorActivity.this.setResult(RESULT_OK, resultIntent);
            ScanDoorActivity.this.finish();
        }
    };

    @Override
    public void initData(Bundle savedInstanceState) {
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        initView();
        initContentLayout();
        showContent();
        getRxPermissions()
                .request(Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            //TODO 许可
                        } else {
                            //TODO 未许可
                            showSettingDilog();
                        }
                    }
                });
    }
    private void showSettingDilog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
        builder.setTitle("权限申请失败");
        builder.setMessage(getString(R.string.permission_text));
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("好，去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 300);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    public Object newP() {
        return null;
    }

    private void initContentLayout() {
        contentLayout.loadingView(View.inflate(context, R.layout.view_opendoor_loading, null));
    }
    private void showContent(){
        if (contentLayout != null) {
            contentLayout.showContent();
        }
    }
}
