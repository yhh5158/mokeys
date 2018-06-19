package com.room.mokeys.net;

import com.room.mokeys.model.GankResults;
import com.room.mokeys.model.LoginInfo;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysListModel;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysLoginService {

    @POST("/passport/login")
    Flowable<MokeysListModel<LoginInfo>> getLoginData(@Query("mobile") String mobile,
                                                       @Query("verification_code") String code);
}
