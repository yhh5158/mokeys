package com.room.mokeys.net;

import com.room.mokeys.model.MokeysEstateDetailInfo;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.model.VerificationCodeInfo;

import io.reactivex.Flowable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by wanglei on 2016/12/31.
 *
 * {
 ║    "code": 0,
 ║    "msg": "success",
 ║    "data": [
 ║        {
 ║            "houses": [
 ║                {
 ║                    "house_id": "2294465049640145",
 ║                    "room_id": "0",
 ║                    "floor": "11",
 ║                    "number": "1102",
 ║                    "x": "116.434",
 ║                    "y": "39.9244"
 ║                }
 ║            ],
 ║            "estate_name": "测试小区41",
 ║            "estate_x": "116.434",
 ║            "estate_y": "39.9244",
 ║            "heid": "3658003547422549"
 ║        }
 ║    ]
 ║}
 */

public interface MokeysRoomListService {

    @POST("/house/getList")
    Flowable<MokeysListModel<MokeysEstateDetailInfo>> getData(@Query("token") String token,
                                                              @Query("x") double x, @Query("y") double y);
}
