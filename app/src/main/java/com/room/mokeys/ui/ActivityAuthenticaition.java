package com.room.mokeys.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.room.mokeys.R;
import com.room.mokeys.clipview.ClipImageActivity;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.kit.Validator;
import com.room.mokeys.model.AliAuthInfo;
import com.room.mokeys.model.AliCheckAuthInfo;
import com.room.mokeys.model.LoginAccessTokenInfo;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysUserInfo;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.pay.PayMainActivity;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import io.reactivex.functions.Consumer;

/**
 * Created by yhh5158 on 2017/5/25.
 */

public class ActivityAuthenticaition extends XActivity {
    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;
    private static final int REQUEST_CODE_SETTING = 300;
    private static final int REQUEST_CODE_EDITNICNAME = 400;
    //调用照相机返回图片临时文件
    private File tempFile;
    private  PopupWindow popupWindow;
    private boolean isFromNormalCheck = false;
    //头像
    @BindView(R.id.authentication_image)
    ImageView headImage2;
    @BindView(R.id.et_name)
    MClearEditText mEditName;
    @BindView(R.id.et_code)
    MClearEditText mEditCode;
    @Override
    public void initData(Bundle savedInstanceState) {
//        getRxPermissions()
//                .request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean granted) throws Exception {
//                        Log.d("yhh","granted = " +granted);
//                        if (granted) {
//                            //TODO 许可
//                        } else {
//                            //TODO 未许可
//                            showSettingDilog();
//                            return;
//                        }
//                    }
//                });
//        createCameraTempFile(savedInstanceState);
        isFromNormalCheck =  getIntent().getBooleanExtra(Constants.MOKEYS_CHECK_ALIAUTH,false);

//        getAccesstokenInfo((String) SPUtil.get(context, Constants.MOKEYS_R,""));
//        getAccesstokenInfo("dsfdsfdsfds");
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAliauth((String)SPUtil.get(context, Constants.MOKEYS_A,""));
    }

    public void checkAliauth(String token) {

        Api.getMokeysCheckAliAuthService().getData(token)
                .compose(XApi.<MokeysListModel<AliCheckAuthInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<AliCheckAuthInfo>>getScheduler())
                .compose(this.<MokeysListModel<AliCheckAuthInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<AliCheckAuthInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                        getvDelegate().toastShort("服务端开小差了，请重新认证");
                    }
                    @Override
                    public void onNext(MokeysListModel<AliCheckAuthInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            if(gankResults.getData().get(0).isAuth_passed()){
                                getvDelegate().toastShort("认证通过");
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"finish");
                                finish();
                            }else{
                                if(!isFromNormalCheck){
                                    getvDelegate().toastShort("认证失败，请重新认证");
                                }
                            }
                        }else{
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

//    public void getAccesstokenInfo(String r) {
//        Api.getMokeysAccessTokenInfoService().getData(r)
//                .compose(XApi.<MokeysListModel<LoginAccessTokenInfo>>getApiTransformer())
//                .compose(XApi.<MokeysListModel<LoginAccessTokenInfo>>getScheduler())
//                .compose(this.<MokeysListModel<LoginAccessTokenInfo>>bindToLifecycle())
//                .subscribe(new ApiSubscriber<MokeysListModel<LoginAccessTokenInfo>>() {
//                    @Override
//                    protected void onFail(NetError error) {
//
//                        Log.d("yuhh","onFail!!!" + error);
//                    }
//                    @Override
//                    public void onNext(MokeysListModel<LoginAccessTokenInfo> gankResults) {
//                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
//                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
////                            SPUtil.put(context, Constants.MOKEYS_R,gankResults.getData().getR());
//                            SPUtil.put(context, Constants.MOKEYS_A,gankResults.getData().get(0).getA());
//                            getvDelegate().toastShort("获取accesstoken成功");
//                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
//                        }
//                    }
//                });
//    }

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
        return R.layout.activity_name_authentication;
    }
    @Override
    public Object newP() {
        return null;
    }
    @OnClick({R.id.backlin,R.id.authentication_image,R.id.btn_confirm})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.backlin:
                finish();
                break;
            case R.id.authentication_image:
                uploadHeadImage();
                break;
            case R.id.btn_confirm:
                onclickConfim();
                break;
            default:
                break;
        }
    }
    private void onclickConfim(){
        String mEtName =  mEditName.getText().toString();
        if (Validator.isBlank(mEtName)) {
            getvDelegate().toastShort("请输入姓名!");
            return;
        }
        String mEtCode =  mEditCode.getText().toString();
        if (Validator.isBlank(mEtCode)) {
            getvDelegate().toastShort("请输入身份证号!");
            return;
        }
       int length = mEtCode.length();


        if (length != 15 || length != 18) {
            getvDelegate().toastShort("请输入正确的身份证号!");
            return;
        }
        getAuthenticationInfo((String)SPUtil.get(context,Constants.MOKEYS_A,""),mEtName,mEtCode);
    }

    public void getAuthenticationInfo(String token, String name, final String code) {
        Api.getMokeysGetAliAuthUrlService().getData(token,name,code)
                .compose(XApi.<MokeysListModel<AliAuthInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<AliAuthInfo>>getScheduler())
                .compose(this.<MokeysListModel<AliAuthInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<AliAuthInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<AliAuthInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            doVerify(gankResults.getData().get(0).getAuth_url());
                            finish();
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }
    /**
     * 启动支付宝进行认证
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
//处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage("是否下载并安装支付宝完成认证?")
                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton("算了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }
    /**
     * 上传头像
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_name_authentication, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                params.alpha = 1.0f;
                getWindow().setAttributes(params);
            }
        });

        btnCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
//                Log.d("yhh","AndPermission.hasPermission(InfoDetailActivity.this, Manifest.permission.CAMERA) " +AndPermission.hasPermission(InfoDetailActivity.this, Manifest.permission.CAMERA));
//                if (AndPermission.hasPermission(InfoDetailActivity.this, Manifest.permission.CAMERA)){
////                    //申请WRITE_EXTERNAL_STORAGE权限
////                    ActivityCompat.requestPermissions(InfoDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
////                            WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
//
//                    AndPermission.with(InfoDetailActivity.this)
//                            .requestCode(WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
//                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
//                            // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                            .rationale(rationaleListener)
//                            .send();
//                } else {
//                    //跳转到调用系统相机
//                }
//                AndPermission.with(InfoDetailActivity.this)
//                        .requestCode(WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
//                        .permission(Manifest.permission.CAMERA)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(rationaleListener)
//                        .send();
                gotoCarema();
                popupWindow.dismiss();
            }
        });
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //权限判断
//                if (AndPermission.hasPermission(InfoDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
////                    //申请READ_EXTERNAL_STORAGE权限
////                    ActivityCompat.requestPermissions(InfoDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
////                            READ_EXTERNAL_STORAGE_REQUEST_CODE);
//                    AndPermission.with(InfoDetailActivity.this)
//                            .requestCode(READ_EXTERNAL_STORAGE_REQUEST_CODE)
//                            .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                            // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                            .rationale(rationaleListener)
//                            .send();
//                } else {
//                    //跳转到调用系统图库
//                    gotoPhoto();
//                }
//                AndPermission.with(InfoDetailActivity.this)
//                        .requestCode(READ_EXTERNAL_STORAGE_REQUEST_CODE)
//                        .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                        // rationale作用是：用户拒绝一次权限，再次申请时先征求用户同意，再打开授权对话框，避免用户勾选不再提示。
//                        .rationale(rationaleListener)
//                        .send();
                gotoPhoto();
                popupWindow.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }
    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }
    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("yhh","requestCode = " +requestCode);
        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));
                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    final String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    Bitmap bitMap = BitmapFactory.decodeFile(cropImagePath);
//                    SPUtil.put(context,Constants.HEAD_IMAGE_URL,cropImagePath);
                    headImage2.setImageBitmap(bitMap);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......
                    new Thread(){
                        @Override
                        public void run() {
//                            super.run();
//                            upLoadFile(cropImagePath);
                        }
                    }.run();
                }
                break;
            case REQUEST_CODE_SETTING:
//                Log.d("yhh","setting return = " +intent.getData());
                break;
            case REQUEST_CODE_EDITNICNAME:  //调用系统相册返回
//                mNicknameLin.setTextRight((String)SPUtil.get(context,Constants.ZYGG_NICK_NAME,""));
                break;
        }
    }
    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", 2);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
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
}
