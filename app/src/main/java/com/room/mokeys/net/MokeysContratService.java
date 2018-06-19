package com.room.mokeys.net;

import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysContractDetailInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysunLocklInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysContratService {

    @POST("/house/getContract")
    Flowable<MokeysBaseModel<MokeysContractDetailInfo>> getData(@Query("token") String token,
                                                                @Query("house_id") String house_id,
                                                                @Query("room_id") String room_id,
                                                                @Query("type") int type);
}
