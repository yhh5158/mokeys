package com.room.mokeys.net;

import com.room.mokeys.model.MokeysContractInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysReservesInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysContractRoomService {

    @POST("/house/signedContracts")
    Flowable<MokeysListModel<MokeysContractInfo>> getData(@Query("token") String token);
}
