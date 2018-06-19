package com.room.mokeys.net;

import com.room.mokeys.model.MokeysEstateInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 *  "code": 0,
 ║    "msg": "success",
 ║    "data": [
 ║        {
 ║            "heid": "8822042186070165",
 ║            "count": "51",
 ║            "estate_name": "测试小区41"
                "x": "116.434",
 ║            "y": "39.9244"
 ║        }
 ║    ]
 */

public interface MokeysEstateListService {

    @POST("/house/getList")
    Flowable<MokeysListModel<MokeysEstateInfo>> getData(@Query("token") String token,
                                                        @Query("x") double x, @Query("y") double y);
}
