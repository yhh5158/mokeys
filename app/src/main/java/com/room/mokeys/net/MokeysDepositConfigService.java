package com.room.mokeys.net;

import com.room.mokeys.model.MokeysDepositConfigInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;
import com.room.mokeys.pay.DepositItemModel;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysDepositConfigService {

    @POST("payment/getReserveConfig")
    Flowable<MokeysListModel<MokeysDepositConfigInfo>> getData(@Query("token") String token);
}
