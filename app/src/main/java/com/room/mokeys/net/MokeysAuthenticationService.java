package com.room.mokeys.net;

import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 */

public interface MokeysAuthenticationService {

    @POST("/passport/authentication")
    Flowable<MokeysListModel<VerificationCodeInfo>> getData(@Query("token") String token,
                                                                 @Query("user_name") String user_name,
                                                                 @Query("identity_code") String identity_code);
}
