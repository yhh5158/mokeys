package com.room.mokeys.ui;

import android.os.Bundle;
import android.view.View;

import com.room.mokeys.R;

import butterknife.OnClick;
import cn.droidlover.xdroidmvp.mvp.XActivity;

/**
 * Created by yhh5158 on 2017/7/4.
 */

public class ActivityRoomScanResult extends XActivity {
    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_roomscan_result;
    }
    @OnClick({ R.id.backlin,R.id.btn_no,R.id.btn_pledge,R.id.btn_signature})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.btn_no:
            case R.id.backlin:
                finish();
                break;
            case R.id.btn_pledge:
                getvDelegate().toastShort("去交定金");
                break;
            case R.id.btn_signature:
                break;
            default:
                break;
        }
    }
    @Override
    public Object newP() {
        return null;
    }
}
