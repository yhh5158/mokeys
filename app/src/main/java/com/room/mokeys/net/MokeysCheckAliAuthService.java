package com.room.mokeys.net;

import com.room.mokeys.model.AliAuthInfo;
import com.room.mokeys.model.AliCheckAuthInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysUserInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysCheckAliAuthService {

    @POST("passport/checkAliAuth")
    Flowable<MokeysListModel<AliCheckAuthInfo>> getData(@Query("token") String token);
}
