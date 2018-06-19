package com.room.mokeys.net;

import com.room.mokeys.model.MokeysListModel;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysLogoutService {

    @POST("/passport/logout")
    Flowable<MokeysListModel<String>> getLoginData(@Query("token") String token);
}
