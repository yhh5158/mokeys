package com.room.mokeys.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.maps.zxing.ScanDoorActivity;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysContractDetailInfo;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysHouseDetailInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysunLocklInfo;
import com.room.mokeys.net.Api;
import com.room.mokeys.pay.PayDepositActivity;
import com.room.mokeys.pay.PayMainActivity;
import com.room.mokeys.pay.PayResult;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by yhh5158 on 2017/6/6.
 */

public class ActivityRoomResult extends XActivity {
    @BindView(R.id.pay_room_info)
    TextView  mTextRoom ;
    MokeysHouseDetailInfo info;
    @Override
    public void initData(Bundle savedInstanceState) {
        info = (MokeysHouseDetailInfo)getIntent().getSerializableExtra("roominfo");
        String room = "您正在看的房子是"+info.getNumber()+"的卧室"+info.getRid();
        mTextRoom.setText(room);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room_result;
    }

    @Override
    public Object newP() {
        return null;
    }
    @OnClick({R.id.pay_room_notexciting,R.id.backlin,R.id.pay_room_pay,R.id.pay_room_sign})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.pay_room_notexciting:
                finish();
                break;
            case R.id.backlin:
                finish();
                break;
            case R.id.pay_room_pay:
                Intent mIntent = new Intent(context,PayDepositActivity.class);
                mIntent.putExtra("title","定金");
                mIntent.putExtra("roominfo",info);
                startActivity(mIntent);
//                finish();
                break;
            case R.id.pay_room_sign:
                getContrat((String) SPUtil.get(context, Constants.MOKEYS_A,""),info.getHid(),info.getRid(),1);
                break;
            default:
                break;
        }
    }

    private void getContrat(String token,String horse_id,String room_id,int type){
        Api.getMokeysContratService().getData(token,horse_id,room_id,type)
                .compose(XApi.<MokeysBaseModel<MokeysContractDetailInfo>>getApiTransformer())
                .compose(XApi.<MokeysBaseModel<MokeysContractDetailInfo>>getScheduler())
                .compose(this.<MokeysBaseModel<MokeysContractDetailInfo>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysBaseModel<MokeysContractDetailInfo>>() {
                    @Override
                    protected void onFail(NetError error) {
                        Log.d("yhh","onFail = "+error.getMessage());
                    }

                    @Override
                    public void onNext(MokeysBaseModel<MokeysContractDetailInfo> gankResults) {
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
                           MokeysContractInfo info =  gankResults.getData().getContract();
                            String url = gankResults.getData().getContract_url() + "&token=" + SPUtil.get(context, Constants.MOKEYS_A, "");
                            MokeysSignatureWebActivity.launch(context,url,"合同",info,false);
                            finish();
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }

}
