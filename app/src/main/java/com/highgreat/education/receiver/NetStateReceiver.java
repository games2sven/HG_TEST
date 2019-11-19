package com.highgreat.education.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.highgreat.education.MyApp;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.utils.CommonUtils;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.NetUtils;
import com.highgreat.education.utils.Wifi4GUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：网络状态广播
 * 创建人：mac-likh
 * 创建时间：16/1/4 16:51
 * 修改人：mac-likh
 * 修改时间：16/1/4 16:51
 * 修改备注：
 */
public class NetStateReceiver extends BroadcastReceiver {
  String wifiSSID="";
  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
        Bundle extras = intent.getExtras();
      EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_WIFISSID_CHANGE));
//        WifiSSIDCheck(extras);
        LogUtil.e("WIFI状态","actioin:"+action);
        LogUtil.e("WIFI状态","==>"+printBundle(extras));
      ConnectivityManager mConnectivityManager =
              (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
//      boolean isConnected = NetUtils.isNetworkConnected(context);
      if(info == null){
        return;
      }
      if (info.isConnected()) {
        try {
          Wifi4GUtils.networkChange(context, info);
        } catch (Exception e) {
          e.printStackTrace();
        }
        //链接了wifi,有效的可以上网的wifi <----really?
        EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_NETCONNECT));
      } else {
        EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_NETUNCONNECT));
      }
      int netType = NetUtils.getNetworkType(MyApp.getAppContext());
      if (CommonUtils.isConnInternetNoToast()) {
        LogUtil.e("netconnect","外网已连接 "+ netType);
        EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_CONNECT_INTERNET));
      }
    }

  }

  private String printBundle(Bundle bundle) {
    StringBuilder sb = new StringBuilder();
    for (String key : bundle.keySet()) {
      if (key.equals(WifiManager.EXTRA_WIFI_STATE)) {
        sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
      } else {
        sb.append("\nkey:" + key + ", value:" + bundle.get(key));
      }
    }
//        LogUtil.e("bundle:"+bundle);
    return sb.toString();
  }

  private  void WifiSSIDCheck(Bundle bundle){
    String ssid =bundle.getString("extraInfo");
    if (!TextUtils.isEmpty(ssid)&&!"<unknown ssid>".equals(ssid)){//说明ssid 有效
      if (!ssid.equals(wifiSSID)){
          EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_WIFISSID_CHANGE));
          wifiSSID=ssid;
      }
    }
  }
}
