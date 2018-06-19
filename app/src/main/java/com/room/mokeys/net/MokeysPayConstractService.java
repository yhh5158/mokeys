package com.room.mokeys.net;

import com.room.mokeys.model.MokeysAlipayInfo;
import com.room.mokeys.model.MokeysListModel;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysPayConstractService {

    @POST("/payment/payContract")
    Flowable<MokeysListModel<MokeysAlipayInfo>> getData(@Query("token") String token,
                                                        @Query("contract_id") String contract_id);
}
