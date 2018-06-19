package com.room.mokeys.ui;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysContractDetailInfo;
import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysHouseDetailInfo;
import com.room.mokeys.net.Api;
import com.tsy.sdk.pay.alipay.Alipay;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by yhh5158 on 2017/7/24.
 */

public class ActivityReservesResult extends XActivity {
    @BindView(R.id.reserves_room_info)
    TextView mRoomText;
    @BindView(R.id.reserves_room_result)
    TextView mFinishText;
    MokeysHouseDetailInfo info;
    String mFinishTime = "";
    @Override
    public void initData(Bundle savedInstanceState) {
        info = (MokeysHouseDetailInfo)getIntent().getSerializableExtra("roominfo");
        mFinishTime = getIntent().getStringExtra("finishtime");
        Spanned room = Html.fromHtml(getString(R.string.reserved_result,info.getNumber(),info.getRid()));
//        String room = "您已经交定金的房子是"+info.getNumber()+"的卧室"+info.getRid();
        mRoomText.setText(room);
        mFinishText.setText(getString(R.string.reserved_end_time_tip));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_reserves_result;
    }

    @Override
    public Object newP() {
        return null;
    }
    @OnClick({ R.id.backlin,R.id.reseves_finish,R.id.reseves_sign})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.reseves_finish:
            case R.id.backlin:
                finish();
                break;
            case R.id.reseves_sign:
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
                        }else{
//                            getvDelegate().toastShort(gankResults.getMsg());
                            Constants.handErrorCode(gankResults.getCode(),context);
                        }
                    }
                });
    }
}
