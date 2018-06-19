package com.room.mokeys.maps.ui.maps;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.room.mokeys.App;
import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.maps.beans.BJCamera;
import com.room.mokeys.maps.beans.PoiSearchTip;
import com.room.mokeys.maps.presenters.SearchMapsPresenter;
import com.room.mokeys.maps.presenters.iviews.ISearchMapsView;
import com.room.mokeys.maps.ui.anim.AnimEndListener;
import com.room.mokeys.maps.ui.anim.ViewAnimUtils;
import com.room.mokeys.maps.ui.poi.PoiSearchAdapter;
import com.room.mokeys.maps.utils.ToastUtil;
import com.room.mokeys.maps.zxing.ScanDoorActivity;
import com.room.mokeys.model.LoginAccessTokenInfo;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysEstateInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysReservesInfo;
import com.room.mokeys.model.MokeysUserInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.pay.PayMainActivity;
import com.room.mokeys.ui.ActivityAuthenticaition;
import com.room.mokeys.ui.ActivityRoomResult;
import com.room.mokeys.ui.MClearEditText;
import com.room.mokeys.ui.PhoneLoginActivity;
import com.room.mokeys.ui.UserCenterActivity;
import com.room.mokeys.ui.contractroom.ContractRoomAcitivity;
import com.room.mokeys.ui.neighbourhoodroom.NeighbourhoodRoomAcitivity;
import com.room.mokeys.ui.reserveroom.ReserverRoomAcitivity;
import com.room.mokeys.widget.BitmapUtils;
import com.uuch.adlibrary.AdConstant;
import com.uuch.adlibrary.AdManager;
import com.uuch.adlibrary.AnimDialogUtils;
import com.uuch.adlibrary.bean.AdInfo;
import com.uuch.adlibrary.transformer.DepthPageTransformer;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapsFragment extends XFragment implements ISearchMapsView, OnKeyListener,Inputtips.InputtipsListener
        ,LocationSource,
        AMapLocationListener,AMap.OnMapTouchListener,AMap.OnMapClickListener
        ,GeocodeSearch.OnGeocodeSearchListener
        ,AMap.OnCameraChangeListener
        ,AMap.OnMarkerClickListener{

    private static final boolean DEBUG = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_SCAN = 1000;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean mLandscape = false;
    private float mOri = 0;

    private AMap aMap;
    @BindView(R.id.map)
    MapView mapView;

    private SensorManager mSensorManager;
    private MySensorEventListener mEventListener = new MySensorEventListener();
    private float mDevicesDirection = 0f;
    @BindView((R.id.ori_compass))
    ImageButton mCompass;
    @BindView(R.id.my_location_btn)
    ImageButton mMyLocation;
    @BindView(R.id.map_custom)
    ImageButton mMapCustom;
    @BindView(R.id.poi_search_in_maps)
    MClearEditText mSearchEditText;
    @BindView(R.id.tip_notlogin)
    TextView mTipNotlogin;
    @BindView(R.id.tip_reserves)
    TextView mTipReserves;
    @BindView(R.id.tip_contract)
    TextView mTipContract;
    private SearchMapsPresenter mSearchMapsPresenter;

    private OnFragmentInteractionListener mListener1;

    @BindView(R.id.search_result_list_view)
    ListView mListView;
    private PoiSearchAdapter mPoiSearchAdapter;
    private SearchViewHelper mSearchViewHelper;

    // float window
    @BindView(R.id.search_float_rl)
    RelativeLayout mSearchFloatWindow;
    @BindView(R.id.search_result_title)
    TextView mSearchPoiTitle;
    @BindView(R.id.search_poi_summary)
    TextView mSearchPoiSummary;
    @BindView(R.id.search_poi_tel)
    TextView mSearchPoiTel;
    @BindView(R.id.maps_drive_line_btn)
    ImageButton mLineBtn;

    @BindView(R.id.title_text)
     TextView tv_title;

    @BindView(R.id.scan_lin)
    LinearLayout lin_scan;

    @BindView(R.id.left_drawer_switch)
    ImageButton mDrawerSwitch ;
    @BindView(R.id.cancel_search)
    ImageButton mSearchBtn ;

    // progress dialog
    private ProgressDialog mSearchProgressDialog;
    private GeocodeSearch mGeocodeSearch ;


    private PoiItem mPoiItem;

    private  ArrayList<MokeysReservesInfo> mReservesList  = new ArrayList<>();
    private  ArrayList<MokeysContractInfo> mContractsList = new ArrayList<>();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapsFragment.
     */
    // TODO: Rename and change types and number of parameters


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000 && tipList != null) {// 正确返回
            List<PoiSearchTip> tips = new ArrayList<>();

//                                   MapsApplication.getDaoSession().getPoiSearchTipDao().deleteAll();


            for (Tip tip : tipList) {

                PoiSearchTip mtip = new PoiSearchTip(tip.getName(), tip.getDistrict(), tip.getAdcode());

//                                        MapsApplication.getDaoSession().getPoiSearchTipDao().insert(mtip);
                tips.add(mtip);
            }

            List<String> listString = new ArrayList<>();

            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }

            mPoiSearchAdapter.addResultTips(tips);

            if (mListView.getVisibility() == View.GONE){
                mListView.setVisibility(View.VISIBLE);
            }
        }
    }


    public AMap getGaoDeMap() {
        return aMap;
    }

    public ImageButton getMyLocationBtn() {
        return mMyLocation;
    }

    public float getDevicesDirection() {
        return mDevicesDirection;
    }

    private ScanOnclickListener mScanLinstener;
    public interface ScanOnclickListener{
        public void goScan();
        public void goLogin();
    }
    public void setScanLinsterner(ScanOnclickListener listener){
        mScanLinstener = listener;
    }
    /**
     * 初始化
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setMaps();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        Sensor mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Sensor mPSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(mEventListener, mSensor, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(mEventListener,mPSensor, SensorManager.SENSOR_DELAY_UI);
        getUserInfo((String)SPUtil.get(context,Constants.MOKEYS_A,""));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMapsModule.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
        mSensorManager.unregisterListener(mEventListener);
//        mMapsModule.deactivate();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener1 != null) {
            mListener1.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener1 = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener1 = null;
    }

//    public void onLeftDrawerViewClick(int id){
//        if (id == R.id.left_drawer_satellite){
//            int currentStyle = SettingUtils.readCurrentMapsStyle();
//
//            int newStyle = AMap.MAP_TYPE_NORMAL;
//
//            if (currentStyle == AMap.MAP_TYPE_NORMAL){
//                newStyle = AMap.MAP_TYPE_SATELLITE;
//            } else{
//                newStyle = AMap.MAP_TYPE_NORMAL;
//            }
//
//            SettingUtils.writeCurrentMapsStyle(newStyle);
//
//            mMapsModule.changeMapStyle(newStyle);
//
//        } else if (id == R.id.left_drawer_camera){
//            int cameraSwitch = SettingUtils.readCurrentCameraState();
//            if (cameraSwitch == SettingUtils.SWITCH_ON){
//                SettingUtils.writeCurrentCameraState(SettingUtils.SWITCH_OFF);
//                mMapsModule.removeCameras();
//            } else {
//                SettingUtils.writeCurrentCameraState(SettingUtils.SWITCH_ON);
//                mMapsModule.loadCameras();
//            }
//        }
//    }
    private static boolean loginStatus = false;
@OnClick({
          R.id.left_drawer_switch,R.id.cancel_search,R.id.poi_search_in_maps,
          R.id.my_location_btn,R.id.scan_lin,R.id.my_location_refresh,
          R.id.tip_notlogin,R.id.tip_reserves,R.id.tip_contract,
          R.id.map_custom})
public void onClick(View view){
    switch (view.getId()) {
        case R.id.left_drawer_switch:
            if (mSearchViewHelper.isInSearch()) {
                mSearchMapsPresenter.exitSearch();
            } else {
                {
                    Intent i = new Intent(context, UserCenterActivity.class);
                    startActivity(i);
                }
//                mSearchMapsPresenter.openDrawer();
            }
          break;

        case R.id.cancel_search:
//            if (mSearchFloatWindow.getVisibility() == View.VISIBLE){
            if (currentSearchedFinished){
                mSearchMapsPresenter.exitSearch();
            } else{
                mSearchEditText.setVisibility(View.VISIBLE);
                lin_scan.setVisibility(View.GONE);
                mTipReserves.setVisibility(View.GONE);
                mTipContract.setVisibility(View.GONE);
                mTipNotlogin.setVisibility(View.GONE);
                mMapCustom.setVisibility(View.GONE);
                tv_title.setVisibility(View.GONE);
                if (mSearchViewHelper.isInSearch()) {
                    String mSearchText = mSearchEditText.getText().toString();
                    if (TextUtils.isEmpty(mSearchText)){
                        Toast.makeText(getActivity().getApplicationContext(),R.string.search_no_text, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mSearchMapsPresenter.searchPoi(getActivity().getApplicationContext(),mSearchText , "");
                } else {
                    mSearchMapsPresenter.enterSearch();
                }
            }
            break;

        case R.id.poi_search_in_maps:
//            if (mSearchFloatWindow.getVisibility() == View.VISIBLE){
            if (currentSearchedFinished){
                mSearchMapsPresenter.enterSearch();
            } else{
                if (!mSearchViewHelper.isInSearch) {
                    mSearchMapsPresenter.enterSearch();
                }
            }
            break;
        case R.id.my_location_btn:
//            mMapsPresenter.changeMyLocationMode();
            if(mLocation != null) {
                CameraPosition newCP = new CameraPosition(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 18, 0, 0);
                aMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCP), null);
//            aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                getMyLocationBtn().setImageResource(R.mipmap.ic_qu_direction_mylocation);

                mIsEnableMyLocation = true;
            }
            break;
        case R.id.scan_lin:
            if(!Constants.isLogin(context)){
                showloginAd(true);
            }else
            {
                if(!(boolean)SPUtil.get(context,Constants.MOKEYS_LOGIN_TIP,false)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.MyAlertDialogStyle);
                    builder.setTitle("收费提示");
//                    builder.setMessage(getString(R.string.permission_text));
                    LinearLayout linearLayout=(LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.alertdialog_content, null);
                    TextView text = (TextView) linearLayout.findViewById(R.id.alert_message);
                    text.setText(getString(R.string.alert_dialog_text));
                    CheckBox mCheckBox = (CheckBox)linearLayout.findViewById(R.id.alert_nomore);
                    mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            SPUtil.put(context,Constants.MOKEYS_LOGIN_TIP,b);
                        }
                    });
                    builder.setView(linearLayout);
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("好", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
//                            intent.setData(uri);
//                            startActivityForResult(intent, 300);
                            Intent i2 = new Intent(context, ScanDoorActivity.class);
                            startActivityForResult(i2, REQUEST_SCAN);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();
                }else {
                    Intent i = new Intent(context, ScanDoorActivity.class);
                    startActivityForResult(i, REQUEST_SCAN);
                }
            }
            break;
        case R.id.my_location_refresh:
            getvDelegate().toastShort("刷新房子");
            if(mLocation!=null) {
                getEstateList((String) SPUtil.get(context, Constants.MOKEYS_A, ""), mLocation.getLongitude(), mLocation.getLatitude());
                refreshRoom();
            }else{
                getvDelegate().toastShort("定位失败");
            }
            break;
        case R.id.map_custom:
            getvDelegate().toastShort("点击客服");
            break;
        case R.id.tip_notlogin:
            String mStatus = (String)SPUtil.get(context,Constants.MOKEYS_USER_STATUS,"");
            if("login".equals(mStatus)){
                Intent mIntent = new Intent();
                mIntent.setClass(context, PayMainActivity.class);
                mIntent.putExtra("title","充值押金");
                mIntent.putExtra("money","500");
                startActivity(mIntent);
            }else if("authentication".equals(mStatus)){
                Intent mIntent = new Intent(context,ActivityAuthenticaition.class);
                mIntent.putExtra(Constants.MOKEYS_CHECK_ALIAUTH,true);
                startActivity(mIntent);
            }else{
                Intent mIntent = new Intent(context,PhoneLoginActivity.class);
                startActivity(mIntent);
            }
            break;
        case R.id.tip_reserves:
            Intent intent = new Intent(context, ReserverRoomAcitivity.class);
            startActivity(intent);
            break;
        case R.id.tip_contract:
            Intent intent1 = new Intent(context, ContractRoomAcitivity.class);
            startActivity(intent1);
            break;
        default:
            break;
        }
    }
   private void getEstateList(String token,double x,double y){
       Api.getMokeysEstateListService().getData(token,x,y)
               .compose(XApi.<MokeysListModel<MokeysEstateInfo>>getApiTransformer())
               .compose(XApi.<MokeysListModel<MokeysEstateInfo>>getScheduler())
               .compose(this.<MokeysListModel<MokeysEstateInfo>>bindToLifecycle())
               .subscribe(new ApiSubscriber<MokeysListModel<MokeysEstateInfo>>() {
                   @Override
                   protected void onFail(NetError error) {

                       Log.d("yuhh","onFail!!!" + error);
                   }
                   @Override
                   public void onNext(MokeysListModel<MokeysEstateInfo> gankResults) {
                       Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                       if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            new addMarkersTask().execute(gankResults.getData());
                       }else{
//                           getvDelegate().toastShort(gankResults.getMsg());
                           Constants.handErrorCode(gankResults.getCode(),context);

                       }
                   }
               });
   }

//    public  void getAccesstokenInfo(String r) {
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
    private void refreshRoom(){
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        mSearchViewHelper.myRefreshView.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSearchViewHelper.myRefreshView.setVisibility(View.GONE);
                startToRecordTime();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public int num = 0;
    private void startToRecordTime(){
        new Thread() {
            @Override
            public void run() {
                num = 5;
                while (num > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num--;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSearchViewHelper.myRefreshView.setVisibility(View.VISIBLE);
                    }
                });

            }

        }.start();
    }
    public boolean isInSearch(){
        return mSearchViewHelper.isInSearch();
    }

    // DrawerLayout state
//    @Override
//    public void onDrawerSlide(View drawerView, float slideOffset) {
//
//    }
//
//    @Override
//    public void onDrawerOpened(View drawerView) {
//
//    }
//
//    @Override
//    public void onDrawerClosed(View drawerView) {
//
//    }
//
//    @Override
//    public void onDrawerStateChanged(int newState) {
//
//    }
//
//    @Override
//    public void openDrawer() {
//        ((MapsMainActivity) getActivity()).openLeftDrawer();
//    }
//
//    @Override
//    public void closeDrawer() {
//        ((MapsMainActivity) getActivity()).closeLeftDrawer();
//    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            hideKeyboard(mSearchEditText);
            mSearchMapsPresenter.searchPoi(getActivity().getApplicationContext(), mSearchEditText.getText().toString(), "");
            return true;
        }

        return false;
    }


    @Override
    public void onMapClick(LatLng latLng) {
        if(isInSearch()) {
            LatLonPoint mLatLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
            RegeocodeQuery query = new RegeocodeQuery(mLatLonPoint, 200, GeocodeSearch.AMAP);
            mGeocodeSearch.getFromLocationAsyn(query);

        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {
        String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
        Log.e("yhh", "formatAddress:" + formatAddress);
        Log.e("yhh", "rCode:" + rCode);
        String msg = "是否将"+formatAddress+"加入到可教学区域？";
//        DialogUtil.showAlertOkCancel(getActivity(), formatAddress, new MyCallback() {
//            @Override
//            public void onCallback(Object data) {
//
//            }
//        });
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int rCode) {
            String name =  geocodeResult.getGeocodeQuery().getLocationName();
//            Log.d("yhh","address name = " +name);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
//        Log.d("yhh","onCameraChange.latitude= " + cameraPosition.target.latitude+ " mLatLng.longitude =  "+cameraPosition.target.longitude);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        getEstateList((String) SPUtil.get(context, Constants.MOKEYS_A, ""), cameraPosition.target.longitude, cameraPosition.target.latitude);
        Log.d("yhh","onCameraChangeFinish = " + cameraPosition.target.latitude+ " mLatLng.longitude =  "+cameraPosition.target.longitude);
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(cameraPosition.target.latitude,cameraPosition.target.longitude), 200, GeocodeSearch.AMAP);
        mGeocodeSearch.getFromLocationAsyn(query);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("yhh","onMarkerClick!!!");
        if(Constants.isLogin(context)) {
            double longitude = marker.getPosition().longitude;
            double latitude = marker.getPosition().latitude;
            Intent mIntent = new Intent(getActivity(), NeighbourhoodRoomAcitivity.class);
            mIntent.putExtra("x", longitude);
            mIntent.putExtra("y", latitude);
            startActivity(mIntent);
        }else{
            getvDelegate().toastShort(getString(R.string.notlogin_toast));
        }
        return true;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        mSensorManager = (SensorManager) getActivity().getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        mSearchMapsPresenter = new SearchMapsPresenter(MapsFragment.this);

        mPoiSearchAdapter = new PoiSearchAdapter(getActivity().getApplicationContext());

        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        mSearchViewHelper = new SearchViewHelper(getRootView());

        mSearchEditText.setOnKeyListener(this);
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString().trim();
                InputtipsQuery inputquery = new InputtipsQuery(newText,"");
                Inputtips inputTips = new Inputtips(getActivity().getApplicationContext(), inputquery);
                inputTips.setInputtipsListener(MapsFragment.this);
                inputTips.requestInputtipsAsyn();
//                Inputtips inputTips = new Inputtips(getActivity().getApplicationContext(),
//                        new Inputtips.InputtipsListener() {
//
//                            @Override
//                            public void onGetInputtips(List<Tip> tipList, int rCode) {
//
//                                if (rCode == 0 && tipList != null) {// 正确返回
//                                    List<PoiSearchTip> tips = new ArrayList<>();
//
////                                   MapsApplication.getDaoSession().getPoiSearchTipDao().deleteAll();
//
//
//                                    for (Tip tip : tipList) {
//
//                                        PoiSearchTip mtip = new PoiSearchTip(tip.getName(), tip.getDistrict(), tip.getAdcode());
//
////                                        MapsApplication.getDaoSession().getPoiSearchTipDao().insert(mtip);
//                                        tips.add(mtip);
//                                    }
//
//                                    List<String> listString = new ArrayList<>();
//
//                                    for (int i = 0; i < tipList.size(); i++) {
//                                        listString.add(tipList.get(i).getName());
//                                    }
//
//                                    mPoiSearchAdapter.addResultTips(tips);
//
//                                    if (mListView.getVisibility() == View.GONE){
//                                        mListView.setVisibility(View.VISIBLE);
//                                    }
//                                }
//                            }
//                        });
//                try {
//                    inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
//
//                } catch (AMapException e) {
//                    e.printStackTrace();
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        mListView = (ListView) view.findViewById(R.id.search_result_list_view);
        mListView.setAdapter(mPoiSearchAdapter);
        mListView.requestFocus();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard(mSearchEditText);

                // stop follow mode
//                mMapsModule.disableAutoLocation();

                PoiSearchAdapter adapter = (PoiSearchAdapter) parent.getAdapter();
                PoiSearchTip tip = (PoiSearchTip) adapter.getItem(position);
                mSearchEditText.setText(tip.getName());
                mSearchEditText.setSelection(mSearchEditText.getText().toString().length());

                Log.d("Search_OnItemClick ", "" + tip.toString());
                mSearchMapsPresenter.searchPoi(getActivity(), tip.getName(), tip.getAdcode());


            }
        });
//        mSearchFloatWindow = (RelativeLayout) view.findViewById(R.id.search_float_rl);
//        mSearchPoiTitle = (TextView) view.findViewById(R.id.search_result_title);
//        mSearchPoiSummary = (TextView) view.findViewById(R.id.search_poi_summary);
//        mLineBtn = (ImageButton) view.findViewById(R.id.maps_drive_line_btn);
//        mSearchPoiTel = (TextView) view.findViewById(R.id.search_poi_tel);
     initAdData();
        getRxPermissions()
                .request(Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        Log.d("yhh","granted = " +granted);
                        if (granted) {
                            //TODO 许可
                        } else {
                            //TODO 未许可
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
                            return;
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_maps;
    }

    @Override
    public Object newP() {
        return null;
    }
//    GeocodeSearch geocoderSearch = new GeocodeSearch(getActivity());
//    geocoderSearch.setOnGeocodeSearchListener(new OnGeocodeSearchListener(){
//
//        @Override
//        public void onGeocodeSearched(GeocodeResult result, int rCode) {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
//
//            String formatAddress = result.getRegeocodeAddress().getFormatAddress();
//            Log.e("formatAddress", "formatAddress:"+formatAddress);
//            Log.e("formatAddress", "rCode:"+rCode);
//
//        }});



    class SearchViewHelper {
        View compassView;
        View myLocationView;
        View myRefreshView;
        View searchMaskView;
        ImageButton drawerSwitch;
        EditText searchEditText;
        ListView listView;
        private View floatWindow;
        private boolean isInSearch = false;

        public boolean isInSearch() {
            return isInSearch;
        }


        public SearchViewHelper(View rootView) {
            compassView = rootView.findViewById(R.id.ori_compass);
            myLocationView = rootView.findViewById(R.id.my_location_btn);
            myRefreshView = rootView.findViewById(R.id.my_location_refresh);
            searchMaskView = rootView.findViewById(R.id.search_mask);
            drawerSwitch = (ImageButton) rootView.findViewById(R.id.left_drawer_switch);
            searchEditText = (EditText) rootView.findViewById(R.id.poi_search_in_maps);
            listView = (ListView) rootView.findViewById(R.id.search_result_list_view);
            floatWindow =  rootView.findViewById(R.id.search_float_rl);
        }

        public void enterSearch() {
            isInSearch = true;
//            floatWindow.setVisibility(View.GONE);
             currentSearchedFinished = false;
            compassView.setVisibility(View.GONE);
            myLocationView.setVisibility(View.GONE);
            myRefreshView.setVisibility(View.GONE);
            if (listView.getAdapter().getCount() == 0){
//                List<PoiSearchTip> tips = MapsApplication.getDaoSession().getPoiSearchTipDao().loadAll();
//                if (tips.isEmpty()){
//                    listView.setVisibility(View.GONE);
//                } else{
//                    mPoiSearchAdapter.addResultTips(tips);
//                }
                listView.setVisibility(View.GONE);
            }

            ViewAnimUtils.popupinWithInterpolator(searchMaskView, new AnimEndListener() {
                @Override
                public void onAnimEnd() {
                    searchMaskView.setVisibility(View.VISIBLE);
                }
            });

            if (listView.getAdapter().getCount() > 0){
                ViewAnimUtils.popupinWithInterpolator(listView, new AnimEndListener() {
                    @Override
                    public void onAnimEnd() {
                        listView.setVisibility(View.VISIBLE);
                    }
                });
            }


            drawerSwitch.setImageResource(R.mipmap.ic_qu_appbar_back);
            searchEditText.setCursorVisible(true);

            showKeyboard(searchEditText);
        }

        public void exitSearch() {
            isInSearch = false;
//            floatWindow.setVisibility(View.GONE);
            currentSearchedFinished = false;

            if (searchMaskView.getVisibility() == View.VISIBLE){
                ViewAnimUtils.popupoutWithInterpolator(searchMaskView, new AnimEndListener() {
                    @Override
                    public void onAnimEnd() {
                        searchMaskView.setVisibility(View.GONE);
                    }
                });
            }


            if (listView.getVisibility() == View.VISIBLE){
                ViewAnimUtils.popupoutWithInterpolator(listView, new AnimEndListener() {
                    @Override
                    public void onAnimEnd() {
                        listView.setVisibility(View.GONE);
                        //取消掉
                        compassView.setVisibility(View.GONE);
                        myLocationView.setVisibility(View.VISIBLE);
                        myRefreshView.setVisibility(View.VISIBLE);
                    }
                });
            } else{
                //取消掉
                compassView.setVisibility(View.GONE);
                myLocationView.setVisibility(View.VISIBLE);
                myRefreshView.setVisibility(View.VISIBLE);
            }


            drawerSwitch.setImageResource(R.mipmap.ic_qu_menu_grabber);
            searchEditText.setText("");
            searchEditText.setCursorVisible(true);
            mSearchEditText.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);
            hideKeyboard(searchEditText);


        }

        public void showSuggestTips() {
            isInSearch = true;
            listView.clearAnimation();
//            floatWindow.setVisibility(View.GONE);
            currentSearchedFinished = false;
            Log.d("yhh","listView.getVisibility() =" +listView.getVisibility());
            listView.setVisibility(View.GONE);
            Log.d("yhh","listView.getVisibility() =" +listView.getVisibility());
            compassView.setVisibility(View.GONE);
            myLocationView.setVisibility(View.GONE);
            myRefreshView.setVisibility(View.GONE);
            searchEditText.setCursorVisible(true);
            searchMaskView.setVisibility(View.GONE);
            drawerSwitch.setImageResource(R.mipmap.ic_qu_appbar_back);

        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    @Override
    public void enterSearch() {
        mSearchViewHelper.enterSearch();
        lin_scan.setVisibility(View.GONE);
        mTipReserves.setVisibility(View.GONE);
        mTipContract.setVisibility(View.GONE);
        mTipNotlogin.setVisibility(View.GONE);
        mMapCustom.setVisibility(View.GONE);
    }

    @Override
    public void exitSearch() {
        if (mPoiOverlay != null) {
            mPoiOverlay.removeFromMap();
            mPoiOverlay = null;
        }
        mSearchViewHelper.exitSearch();
        lin_scan.setVisibility(View.VISIBLE);
//        mTipReserves.setVisibility(View.VISIBLE);
//        mTipContract.setVisibility(View.VISIBLE);
//        mTipNotlogin.setVisibility(View.VISIBLE);
        setContractReserverTip();
        initLoginStatus();
        mMapCustom.setVisibility(View.VISIBLE);
    }

    private PoiOverlay mPoiOverlay;

    @Override
    public void showSearchResult(List<PoiItem> poiItems) {
        Log.d("yhh","showSearchResult");
        if (poiItems == null || poiItems.isEmpty()){
            ToastUtil.show(getActivity().getApplicationContext(), R.string.no_poi_search);
        } else {
            mSearchViewHelper.showSuggestTips();
            if (mPoiOverlay != null) {
                mPoiOverlay.removeFromMap();
                mPoiOverlay = null;
            }

            PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
            poiOverlay.addToMap();
            poiOverlay.zoomToSpan();
            mPoiOverlay = poiOverlay;


            mSearchMapsPresenter.showPoiFloatWindow(poiItems.get(0));

            mPoiItem = poiItems.get(0);

//            MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_camera_location))
//                    .position(new LatLng(mPoiItem.getLatLonPoint().getLatitude(),mPoiItem.getLatLonPoint().getLongitude()))
//                    .draggable(true);
//            aMap.addMarker(markerOption);


            aMap.moveCamera(
                    CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            new LatLng(mPoiItem.getLatLonPoint().getLatitude(),mPoiItem.getLatLonPoint().getLongitude()), 18, 30, 30)));
//            aMap.clear();
//            aMap.addMarker(new MarkerOptions().position(new LatLng(39.983456, 116.3154950))
//                    .icon(BitmapDescriptorFactory
//                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        }

    }

    @Override
    public void showSearchProgress() {
        if (mSearchProgressDialog == null){
            mSearchProgressDialog = new ProgressDialog(getActivity());
            mSearchProgressDialog.setMessage(getResources().getString(R.string.search_message));
        }
        mSearchProgressDialog.show();

    }

    @Override
    public void hideSearchProgress() {
        mSearchProgressDialog.hide();
    }

    private boolean currentSearchedFinished = false;
    @Override
    public void showPoiFloatWindow(PoiItem poiItem) {
        currentSearchedFinished = true;
//        mSearchFloatWindow.setVisibility(View.VISIBLE);
//
//        PoiItem item = poiItem;
//
////        if (DEBUG){
////            Log.d("showPoiFloatWindow     adCode                -"  , poiItem.getAdCode());
////            Log.d("showPoiFloatWindow     adName                -"  , poiItem.getAdName());
////            Log.d("showPoiFloatWindow     CityCOde              -"  , poiItem.getCityCode());
////            Log.d("showPoiFloatWindow     cityName              -"  , poiItem.getCityName());
////            Log.d("showPoiFloatWindow     direction             -"  , poiItem.getDirection());
////            Log.d("showPoiFloatWindow     email                 -"  , poiItem.getEmail());
////            Log.d("showPoiFloatWindow     poid                  -"  , poiItem.getPoiId());
////            Log.d("showPoiFloatWindow     postCode              -"  , poiItem.getPostcode());
////            Log.d("showPoiFloatWindow     provinceCode          -"  , poiItem.getProvinceCode());
////            Log.d("showPoiFloatWindow     provincename          -"  , poiItem.getProvinceName());
////            Log.d("showPoiFloatWindow     snippet               -"  , poiItem.getSnippet());
////            Log.d("showPoiFloatWindow     tel                   -"  , poiItem.getTel());
////            Log.d("showPoiFloatWindow     title                 -"  , poiItem.getTitle());
////            Log.d("showPoiFloatWindow     typedes               -"  , poiItem.getTypeDes());
////            Log.d("showPoiFloatWindow     website               -"  , poiItem.getWebsite());
////            Log.d("showPoiFloatWindow     distance              -"  , poiItem.getDistance() + "");
////        }
//
//
//
//        mSearchPoiTitle.setText(poiItem.getTitle());
//        String sum = "";
//        if (!TextUtils.isEmpty(poiItem.getProvinceName())){
//            sum += poiItem.getProvinceName();
//        }
//
//        if (!TextUtils.isEmpty(poiItem.getCityName())){
//            sum += "-" + poiItem.getCityName();
//        }
//
//        if (!TextUtils.isEmpty(poiItem.getAdName())){
//            sum += "-" + poiItem.getAdName();
//        }
//
//        if (!TextUtils.isEmpty(poiItem.getSnippet())){
//            sum += "-" + poiItem.getSnippet();
//        }
//
//        mSearchPoiSummary.setText(sum);
//        mSearchPoiTel.setText(poiItem.getTel());
//        mLineBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                AMapNavi aMapNavi = AMapNavi.getInstance(getActivity().getApplicationContext());
////                ArrayList<NaviLatLng> start = new ArrayList<>();
////                start.add(new NaviLatLng(mMapsModule.getMyLocation().getLatitude(), mMapsModule.getMyLocation().getLongitude()));
////
////                ArrayList<NaviLatLng> end = new ArrayList<>();
////                end.add(new NaviLatLng(mPoiItem.getLatLonPoint().getLatitude(), mPoiItem.getLatLonPoint().getLongitude()));
////
////                aMapNavi.calculateDriveRoute(end, null, AMapNavi.DrivingDefault);
//
////                Intent intent = new Intent(getActivity(), NaviRouteActivity.class);
////                intent.putParcelableArrayListExtra(NaviRouteActivity.NAVI_ENDS, end);
////                intent.putParcelableArrayListExtra(NaviRouteActivity.NAVI_START, start);
////                getActivity().startActivity(intent);
//            }
//        });
////        mNaviBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                Intent intent = new Intent(getActivity(), NaviEmulatorActivity.class);
////                getActivity().startActivity(intent);
////            }
////        });
    }

    // DrawerLayout state

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mLandscape = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
    }

    class MySensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION) {
                float x = sensorEvent.values[SensorManager.DATA_X];

                float fixedX = x;
                if (mLandscape ){
                    if (mOri < 0) {
                        fixedX -= 90;
                        Log.d("onSensorChanged","Right" + x + "   fixedX :" + fixedX);
                    } else {
                        fixedX += 90;
                        Log.d("onSensorChanged","Left " + x + "   fixedX :" + fixedX);
                    }
                }

                if (Math.abs(fixedX - mDevicesDirection) > 2) {
//                    if (SettingUtils.readCurrentMyLocationMode() == 3){
                    if (false){
                        mCompass.setRotation(-fixedX);
                    } else{
                        mCompass.setRotation(0);
                    }

                    mDevicesDirection = fixedX;

//                    mMapsModule.onOrientationChanged(mDevicesDirection);
                }
            } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
                float x = sensorEvent.values[SensorManager.DATA_X];
                float y = sensorEvent.values[SensorManager.DATA_Y];
                float z = sensorEvent.values[SensorManager.DATA_Z];
                mOri = x;
//                Log.d("PSensor-onSensorChanged","" + x + "   Ori :" + x);
            }


        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }


    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private AMapLocation mLocation;
    private BitmapDescriptor mMyLocationIcon = BitmapDescriptorFactory.fromResource(R.mipmap.ic_mylocation);
    private Marker marker;
    private boolean mIsEnableMyLocation = true;
    private boolean mIsChanged = false;

    public void setMaps(){
        mGeocodeSearch = new GeocodeSearch(getActivity());
        mGeocodeSearch.setOnGeocodeSearchListener(this);
        aMap.setOnMapTouchListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置缩放控件是否显示
        aMap.setOnCameraChangeListener(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();//自定义蓝点
        aMap.setOnMarkerClickListener(this);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
//        new addMarkersTask().execute();
    }

    private void setupLocationStyle(){
        int STROKE_COLOR = Color.argb(180, 3, 145, 255);
        int FILL_COLOR = Color.argb(10, 0, 0, 180);
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.ic_mylocation));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

//    private void initPoint(){
//        MyLocationStyle myLocationStyle;
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//    }
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (mListener != null && aMapLocation != null) {
////            if ((mLocation == null || (mLocation.getLatitude() != aMapLocation.getLatitude() || mLocation.getLongitude() != aMapLocation.getLongitude()))) {
//            if(mLocation == null){
//                Log.e("yhh", "onLocationChanged");
//                mLocation = aMapLocation;
//                if (mIsEnableMyLocation) {
//                    Log.e("yhh", "mIsEnableMyLocation =====" );
//                    LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                    aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//
//                    if (marker == null) {
//                        aMap.clear();
//                        MarkerOptions mMyLocationMarker = new MarkerOptions().anchor(0.5f, 0.5f).position(latLng).icon(mMyLocationIcon);
//                        marker = aMap.addMarker(mMyLocationMarker);
//                    } else {
//                        marker.setPosition(latLng);
//                    }
////                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//                }
//
//            }
//        }


        if (mListener != null && aMapLocation != null) {
            Log.d("yhh","onLocationChanged  aMapLocation.getLongitude() = " + aMapLocation.getLongitude() + " amapLocation.getLatitude()" + aMapLocation.getLatitude() + " aMapLocation.getErrorCode() = " +aMapLocation.getErrorCode());
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//                CameraPosition newCP= new CameraPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()),18, 0, 0);
//                aMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCP), null);
//                aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                mLocation = aMapLocation;
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Toast.makeText(getActivity(),errText,Toast.LENGTH_SHORT);
                Log.e("yhh",errText);
//                mLocationErrText.setVisibility(View.VISIBLE);
//                mLocationErrText.setText(errText);
            }

        }

    }
    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE){
            if (!mIsChanged){
                mIsEnableMyLocation = false;
//                mMapsPresenter.stopFollowMode();
                getMyLocationBtn().setImageResource(R.mipmap.ic_qu_direction_mylocation_lost);
                mIsChanged = true;
            }
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP){
            mIsChanged = false;
//            LatLng mLatLng = aMap.getProjection().fromScreenLocation(new Point(mapView.getWidth()/2,mapView.getHeight()/2));
//            Log.d("yhh","mLatLng.latitude= " + mLatLng.latitude+ " mLatLng.longitude =  "+mLatLng.longitude);
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(App.getContext());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

//    private void addMarkers(){
//        ArrayList<MarkerOptions> markerOptionses = createMarkerOptions();
//        for(int i = 0;i<markerOptionses.size() ;i++){
//            aMap.addMarker(markerOptionses.get(i));
//        }
//    }
    private ArrayList<MarkerOptions> createMarkerOptions(ArrayList<MokeysEstateInfo> list){
        BitmapDescriptor mMarkerDesc ;
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        for(int i =0;i<list.size();i++){
            if(list.get(i).getCount() > 99){
                 mMarkerDesc = BitmapDescriptorFactory.fromBitmap(BitmapUtils.getNumberBitmap(60,"99+"));
//                mMarkerDesc = BitmapDescriptorFactory.fromBitmap(BitmapUtils.getCounterResources(context,"99+",60));
            }else{
//                 mMarkerDesc = BitmapDescriptorFactory.fromBitmap(BitmapUtils.getNumberBitmap(60,list.get(i).getCount()+""));
                mMarkerDesc = BitmapDescriptorFactory.fromBitmap(BitmapUtils.getCounterResources(context,list.get(i).getCount()+"",60));
            }

            LatLng latLng = new LatLng(list.get(i).getY(),list.get(i).getX());
            MarkerOptions mo = new MarkerOptions().position(latLng).draggable(true).icon(mMarkerDesc);
            markerOptions.add(mo);
        }
//        for (BJCamera cameraBean : cameraBeans) {
//            LatLng latLng = new LatLng(cameraBean.getLatitude(), cameraBean.getLongtitude());
//            MarkerOptions mo = new MarkerOptions().position(latLng).draggable(true).icon(mMarkerDesc);
//            markerOptions.add(mo);
//        }
        return  markerOptions;
    }
    private List<BJCamera> readCameras(){
        List<BJCamera> mCameras = new ArrayList<BJCamera>();
        try {
            mCameras  = App.getDaoSession().getBJCameraDao().queryBuilder().build().list();
            return mCameras;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  mCameras;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d("yhh","requestCode = " +requestCode);
        switch (requestCode) {
            case REQUEST_SCAN:  //
                if (resultCode == getActivity().RESULT_OK) {
                    if (null != data) {
                        Bundle bundle = data.getExtras();
                        if (bundle == null) {
                            return;
                        }
                        if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                            String result = bundle.getString(CodeUtils.RESULT_STRING);
                            Toast.makeText(context, "解析结果:" + result, Toast.LENGTH_LONG).show();
                            Intent mIntent = new Intent(context, ActivityRoomResult.class);
                            startActivity(mIntent);
                        } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                            Toast.makeText(context, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
    }

    public class addMarkersTask extends
            AsyncTask<ArrayList<MokeysEstateInfo>, Integer, List<MarkerOptions>> {

        @Override
        protected List<MarkerOptions> doInBackground(ArrayList<MokeysEstateInfo>... list) {
            ArrayList<MarkerOptions> markerOptionses = createMarkerOptions(list[0]);
            return markerOptionses;
        }

        @Override
        protected void onPostExecute(List<MarkerOptions> result) {
            for(int i = 0;i<result.size() ;i++){
                aMap.addMarker(result.get(i));
            }
        }
    }
    /**
     * 初始化数据
     */
    private List<AdInfo> advList = null;
    private List<AdInfo> advListLogin = null;
    private void initAdData() {
        advList = new ArrayList<>();
        AdInfo adInfo = new AdInfo();
        adInfo.setActivityImg("http://pay.mobilecinema.cn/upload/imgs/2017-05-24/csm_Rattle-Abschied_MR_b7dd4a730d.jpg");
        advList.add(adInfo);

        adInfo = new AdInfo();
        adInfo.setActivityImg("http://pay.mobilecinema.cn/upload/imgs/2017-05-24/csm_Rattle-Abschied_MR_b7dd4a730d.jpg");
        advList.add(adInfo);
        advListLogin = new ArrayList<>();
        advListLogin.add(adInfo);

    }
    private AdManager adManager;
    private void showAd(){
        adManager = new AdManager(getActivity(), advList);
        adManager.setOverScreen(true)
                .setPageTransformer(new DepthPageTransformer());
        adManager.setOnLoginClickLinstener(new AnimDialogUtils.OnLoginClickListenner() {
            @Override
            public void goLogin() {
                getvDelegate().toastShort("go to login");
            }
        });
        adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
            @Override
            public void onImageClick(View view, AdInfo advInfo) {
                getvDelegate().toastShort(advInfo.getActivityImg());
            }
        })
                .setPadding(60)
                .setWidthPerHeight(0.5f)
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN);

    }
    private void showloginAd(boolean login){
        adManager = new AdManager(getActivity(), advListLogin);
        adManager.setOverScreen(true)
                .setPageTransformer(new DepthPageTransformer());
        adManager.setOnLoginClickLinstener(new AnimDialogUtils.OnLoginClickListenner() {
            @Override
            public void goLogin() {
                getvDelegate().toastShort("go to login");
                Intent mIntent = new Intent(context,PhoneLoginActivity.class);
                startActivity(mIntent);
                adManager.dismissAdDialog();
            }
        });
        adManager.setOnImageClickListener(new AdManager.OnImageClickListener() {
            @Override
            public void onImageClick(View view, AdInfo advInfo) {
                getvDelegate().toastShort(advInfo.getActivityImg());
            }
        })
                .setPadding(60)
                .setWidthPerHeight(0.5f)
                .showAdDialog(AdConstant.ANIM_UP_TO_DOWN,login);

    }

    public AdManager getAdManager() {
        return adManager;
    }

    public void setAdManager(AdManager adManager) {
        this.adManager = adManager;
    }

    public void getUserInfo(String token) {
        Api.getMokeysUserInfoService().getData(token)
                .compose(XApi.<MokeysListModel<MokeysUserInfo>>getApiTransformer())
                .compose(XApi.<MokeysListModel<MokeysUserInfo>>getScheduler())
                .subscribe(new ApiSubscriber<MokeysListModel<MokeysUserInfo>>() {
                    @Override
                    protected void onFail(NetError error) {

                        Log.d("yuhh","onFail!!!" + error);
                    }
                    @Override
                    public void onNext(MokeysListModel<MokeysUserInfo> gankResults) {
                        Log.d("yuhh","getUserInfo code = " +gankResults.getCode());
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                            String mStatus = gankResults.getData().get(0).getStatus();
//                            String mStatus = "100";
                            if("100".equals(mStatus)){
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"login");
                            }else if("101".equals(mStatus)){
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"authentication");
                            }else if("111".equals(mStatus)){
                                SPUtil.put(context, Constants.MOKEYS_USER_STATUS,"finish");
                            }
                            mReservesList  = gankResults.getData().get(0).getReserves();
                            mContractsList = gankResults.getData().get(0).getContracts();
                        }else{
//                            Toast.makeText(context,gankResults.getMsg(),Toast.LENGTH_SHORT).show();
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                        initLoginStatus();
                        setContractReserverTip();
                    }
                });
    }
    private void setContractReserverTip(){
        if(mReservesList.size() != 0){
            mTipReserves.setVisibility(View.VISIBLE);
        }else{
            mTipReserves.setVisibility(View.GONE);
        }
        if(mContractsList.size() != 0){
            mTipContract.setVisibility(View.VISIBLE);
        }else{
            mTipContract.setVisibility(View.GONE);
        }
    }
    private void initLoginStatus(){
            if(Constants.isLogin(context)){
                String mStatus = (String)SPUtil.get(context,Constants.MOKEYS_USER_STATUS,"");
                if("login".equals(mStatus)){
                    mTipNotlogin.setVisibility(View.VISIBLE);
                    mTipNotlogin.setText(getString(R.string.notpay_tip));
                }else if("authentication".equals(mStatus)){
                    mTipNotlogin.setVisibility(View.VISIBLE);
                    mTipNotlogin.setText(getString(R.string.notauthentication_tip));
                }else if("finish".equals(mStatus)){
                    mTipNotlogin.setVisibility(View.GONE);
                }else{
                    mTipNotlogin.setVisibility(View.GONE);
                }
            }else{
                mTipNotlogin.setVisibility(View.VISIBLE);
                mTipNotlogin.setText(getString(R.string.notlogin_tip));
            }
    }
}
