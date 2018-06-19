package com.room.mokeys.net;

import com.room.mokeys.model.MokeysAlipayInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeyscashPledgeService {

    @POST("/payment/cashPledge")
    Flowable<MokeysListModel<MokeysAlipayInfo>> getData(@Query("token") String token);
}
