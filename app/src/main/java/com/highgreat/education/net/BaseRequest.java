package com.highgreat.education.net;

import android.support.annotation.NonNull;


import com.highgreat.education.bean.APPBean;
import com.highgreat.education.bean.BaseHttpBean;
import com.highgreat.education.bean.UpgradePackageBean;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Response;
import rx.Subscriber;

public interface BaseRequest {


    void  verifySignCode(Map<String, String> params, Subscriber<BaseHttpBean> subscriber);

    void verifyPassCode(String phone, String passcode, Subscriber<BaseHttpBean> subscriber);

    void  getSignCode(String phone, Subscriber<Response<BaseHttpBean>> subscriber);

    void  checkIsSign(String phone, Subscriber<Response<BaseHttpBean>> subscriber);

    void  checkIsCanRetrieve(String phone, Subscriber<Response<BaseHttpBean>> subscriber);

    void  reSetPassword(int type, String password, Subscriber<BaseHttpBean> subscriber);

    void  getAppUpdateInfo(Subscriber<APPBean> subscriber);


    void  getFirmwareInfo(Map<String, String> params, Subscriber<UpgradePackageBean> subscriber);

    void download(@NonNull String url, final String filePath, Subscriber subscriber);

    void sendRepair(Map<String, String> params, Subscriber<BaseHttpBean> subscriber, MultipartBody.Part file);

    void cancel();

    void  cancelDownload();
}
