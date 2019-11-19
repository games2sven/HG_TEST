package com.highgreat.education.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.PreferenceNames;
import com.highgreat.education.net.BaseRequest;
import com.highgreat.education.net.RxJavaRetrofitNetRequestUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

//import com.highgreat.space.net.MyHttpCallback;
//import com.highgreat.space.net.RestClientWithSSID;

public class FirmwareUtils {
    /**
     * 请求服务端固件信息-FTP
     */
    public static void getOSVersionInfoFromServer(final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("upobject", 2 + ""); //升级对象(0-android 1-IOS 2-固件 3-系统 4-遥控器)
        final int version = (int) SharedPreferencesUtils.getParam(context, PreferenceNames.FIRMWARE_VERSION_CURRENT,0);//当前飞机版本
        paramsMap.put("vname",version +""); //当前dobby版本，即最近一次连接的飞机的固件版本
        BaseRequest baseRequest =  new RxJavaRetrofitNetRequestUtils();
        baseRequest.getFirmwareInfo(paramsMap, new Subscriber<UpgradePackageBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                EventBus.getDefault().post(new EventCenter(EventBusCode.GET_SERVER_OS_FAIL));
            }

            @Override
            public void onNext(UpgradePackageBean response) {
                if(response != null) {
                    String infoJsonStr = new Gson().toJson(response);
                    LogUtil.e("firmwareInfo", "infoJsonStr=" + infoJsonStr + "current version:"+version);
                    LogUtil.e("current version", "current version=" + version);
                    SharedPreferencesUtils.setPreferences(context, PreferenceNames.FIRMWARE_FILE_NAME_BALL, response.data.filename);
                    //if(version != response.data.versionCode) {
                        LogUtil.e("current version", "CODE_NEW_FIRMWARE");
                        EventBus.getDefault().post(new EventCenter(EventBusCode.GET_SERVER_OS, response.data));
                   // }

                }
            }
        });
    }
}
