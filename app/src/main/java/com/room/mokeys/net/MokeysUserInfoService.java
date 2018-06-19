package com.room.mokeys.net;

import com.room.mokeys.model.LoginInfo;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysUserInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysUserInfoService {

    @POST("/passport/getUserInfo")
    Flowable<MokeysListModel<MokeysUserInfo>> getData(@Query("token") String token);
}
