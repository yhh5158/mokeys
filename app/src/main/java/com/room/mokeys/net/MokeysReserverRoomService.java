package com.room.mokeys.net;

import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysReservesInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysReserverRoomService {

    @POST("/house/reserves")
    Flowable<MokeysListModel<MokeysReservesInfo>> getData(@Query("token") String token);
}
