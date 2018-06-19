package com.room.mokeys.net;

import com.room.mokeys.model.AliAuthInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.MokeysUserInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 * getAliAuthUrl?identity_code=130626198506060011&user_name=刘佳&token=4967997678540292
 */

public interface MokeysGetAliAuthUrlService {

    @POST("passport/getAliAuthUrl")
    Flowable<MokeysListModel<AliAuthInfo>> getData(@Query("token") String token,
                                                   @Query("user_name") String user_name,
                                                   @Query("identity_code") String identity_code);


}
