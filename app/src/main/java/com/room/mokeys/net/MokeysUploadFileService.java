package com.room.mokeys.net;

import com.room.mokeys.model.MokeySignImagelInfo;
import com.room.mokeys.model.MokeysBaseModel;
import com.room.mokeys.model.MokeysListModel;

import io.reactivex.Flowable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface MokeysUploadFileService {
    @Multipart
    @POST("/house/signContract")
    Flowable<MokeysListModel<MokeySignImagelInfo>> upload(@Part MultipartBody.Part file,
                                                          @Query("token") String token,
                                                          @Query("contract_id") String contract_id);
  }