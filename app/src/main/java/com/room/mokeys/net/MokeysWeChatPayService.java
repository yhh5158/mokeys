package com.room.mokeys.net;

import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysWeChatPayService {

    @POST("/payment/recharge")
    Flowable<MokeysListModel<VerificationCodeInfo>> getData(@Query("token") String token,
                                                            @Query("amount") String amount);
}
