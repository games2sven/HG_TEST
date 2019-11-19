package com.highgreat.education.net;


import com.highgreat.education.bean.APPBean;
import com.highgreat.education.bean.BaseHttpBean;
import com.highgreat.education.bean.UpgradePackageBean;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

public interface PersonCenterApi {


    @POST("sign/login")
    Observable<Response<BaseHttpBean>> login(@Query("phone") String phone, @Query("password") String password, @HeaderMap Map<String, String> header);


    @GET("sign/verifyCode")
    Observable<BaseHttpBean> verifySignCode(@QueryMap Map<String, String> params, @HeaderMap Map<String, String> header);


    @GET("sign/verifyPassCode")
    Observable<BaseHttpBean> verifyPassCode(@Query("account") String phone, @Query("passcode") String passcode, @HeaderMap Map<String, String> header);


    @GET("sign/getPhoneStatus")
    Observable<Response<BaseHttpBean>> checkIsSign(@Query("phone") String phone);


    @GET("sign/getpasswd")
    Observable<Response<BaseHttpBean>> checkIsCanRetrieve(@Query("phone") String phone, @HeaderMap Map<String, String> header);


    @GET("sign")
    Observable<Response<BaseHttpBean>> getVerifySignCode(@Query("phone") String phone, @HeaderMap Map<String, String> header);


    @POST("sign/reg")
    Observable<BaseHttpBean> signSetPwd(@Query("password") String password, @HeaderMap Map<String, String> header);


    @POST("sign/setPasswd")
    Observable<BaseHttpBean>  reSetPwd(@Query("password") String password, @HeaderMap Map<String, String> header);



    @GET("Apps/version")
    Observable<APPBean> getAppUpdateInfo(@HeaderMap Map<String, String> header);


    @GET("Apps/version")
    Observable<UpgradePackageBean> getDroneFirnWare(@QueryMap Map<String, String> params, @HeaderMap Map<String, String> header);

    @Multipart
    @POST("repair")
    Observable<BaseHttpBean> sendFauiltRepair(@QueryMap Map<String, String> params, @HeaderMap Map<String, String> header, @Part MultipartBody.Part file);


    @Streaming
    @GET
    Observable<okhttp3.ResponseBody> downFile(@Url String fileUrl);


}
