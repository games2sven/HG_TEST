package com.highgreat.education.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.io.IOException;

/**
 * Created by din on 16/11/8.
 */

public class Wifi4GUtils {
  /**
   * 照片删除-判断当前连接的wifi是否是dobby
   */

  static final Object lock = new Object();
  private static boolean isUAVWifi = false;




  public static boolean isSupportCellularNetwork() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
//        return false;
  }

  /**
   * 网络变化时，需要通知设置网络
   *
   * @param context
   * @throws Exception
   */
  public synchronized static void networkChange(Context context, NetworkInfo _info) {
//            LogUtil.e("---+++", "|||networkChange |||_info:" + _info.getDetailedState().toString());
    bringUpCellularNetwork(context, false, true);
  }

  /**
   * 如果连的WiFi的是飞机的，则使用蜂窝数据
   * 否则，继续使用WiFi网络
   *
   * @param context
   * @throws Exception
   */
  public static void bringUpCellularNetwork(Context context) {
    try {
      bringUpCellularNetwork(context, false, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 切换到WiFi网络，一般是需要有跟飞机交互的地方
   *
   * @param context
   * @throws Exception
   */
  public static void bringUpWifiNetwork(Context context) {
    try {
      bringUpCellularNetwork(context, true, false);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 是否连接的是飞机WiFi
   *
   * @param context
   * @return
   */
  public static boolean isConnectUAVWifi(Context context) {
    return isConnectUAVWifi(context, false);
  }

  private synchronized static boolean isConnectUAVWifi(Context context, boolean isNetworkChange) {
    isUAVWifi = false;
    if (!isConnectWiFi(context)) {
      isUAVWifi = false;
    } else if (isNetworkChange || !isUAVWifi) {
      isUAVWifi = false;
      set2WiFi(context); //有可能连着wifi但是现在网络用的4g，所以需要主动切换到wifi然后再检查是否是飞机的wifi
    }
    return isUAVWifi;
  }

  /**
   * 网络是否连接
   *
   * @param context
   * @return
   */
  public static boolean isNetworkConnected(Context context) {
    if (isSupportCellularNetwork() && isConnectUAVWifi(context)) {
      return isUAVCellular(context);
    }
    final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
    if (mNetworkInfo != null) {
      return mNetworkInfo.isConnected();
    }
    return false;
  }

  /**
   * 连接飞机WiFi时是否切换到了数据网络
   *
   * @param context
   * @return
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static boolean isUAVCellular(Context context) {
    final ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (isSupportCellularNetwork() && isConnectUAVWifi(context)) {
      boolean isConnected = false;
      Network[] networks = mConnectivityManager.getAllNetworks();
      if (networks != null && networks.length > 1) {
        for (Network network : networks) {
          NetworkInfo info = mConnectivityManager.getNetworkInfo(network);
          if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE) {
            isConnected = true; //支持切换并且连着飞机wifi，这时候只要有移动网络信息就说明切换到了数据网络么？？
          }
        }
      }
      return isConnected;
    }

    return false;
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static void set2WiFi(Context context) {
    try {
      final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      final NetworkRequest.Builder networkReq = new NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
      networkReq.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

      final ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
          super.onAvailable(network);
          ConnectivityManager.setProcessDefaultNetwork(network);
        }
      };
      cm.requestNetwork(networkReq.build(), callback);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @param context
   * @param isWifi  如果为true说明想用WiFi
   * @throws Exception
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  private static void bringUpCellularNetwork(Context context, final boolean isWifi, boolean isNetworkChange) {
    try {
      if (!isSupportCellularNetwork()) {
        return;
      }
      final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      boolean isNeedChangeCellular = false;
      if ((!isWifi && isConnectUAVWifi(context, isNetworkChange)) || !isConnectWiFi(context)) {//这种情况说明是连接的飞机，或者未连接WiFi时切换到数据网络
        isNeedChangeCellular = true;
      }
//            LogUtil.e("---+++", "|||isNeedChangeCellular = " + isNeedChangeCellular);
      final NetworkRequest.Builder networkReq = new NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

      if (isNeedChangeCellular) {
        networkReq.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
      } else {
        networkReq.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
      }

      final ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
          super.onAvailable(network);
//                    if (Build.VERSION.SDK_INT >= 23) {
//                        cm.bindProcessToNetwork(network);
//                    } else {
          ConnectivityManager.setProcessDefaultNetwork(network);
//                    }
        }
      };
      cm.requestNetwork(networkReq.build(), callback);
    } catch (Exception e) {
    }

  }

  /**
   * 是否连接WiFi
   *
   * @param context
   * @return
   */
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public static boolean isConnectWiFi(Context context) {
    boolean isConnected = false;
    final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (isSupportCellularNetwork()) {
      Network[] networks = cm.getAllNetworks();
      for (Network network : networks) {
        NetworkInfo info = cm.getNetworkInfo(network);
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
          isConnected = true;
        }
      }
    }
    return isConnected;
  }

  public static boolean isNetworkOnline() {
    Runtime runtime = Runtime.getRuntime();
    try {
      Process ipProcess = runtime.exec("ping -c 3 www.baidu.com");
      int exitValue = ipProcess.waitFor();
      LogUtil.i("Avalible", "Process:"+exitValue);
      return (exitValue == 0);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * 获取SSID
   * @param activity 上下文
   * @return  WIFI 的SSID
   */
  public static String getWIFISSID(Activity activity) {
    String ssid="unknown id";

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O||Build.VERSION.SDK_INT==Build.VERSION_CODES.P) {

      WifiManager mWifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

      assert mWifiManager != null;
      WifiInfo info = mWifiManager.getConnectionInfo();

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        return info.getSSID();
      } else {
        return info.getSSID().replace("\"", "");
      }
    } else if (Build.VERSION.SDK_INT==Build.VERSION_CODES.O_MR1){

      ConnectivityManager connManager = (ConnectivityManager) activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
      assert connManager != null;
      NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
      if (networkInfo.isConnected()) {
        if (networkInfo.getExtraInfo()!=null){
          return networkInfo.getExtraInfo().replace("\"","");
        }
      }
    }
    return ssid;
  }
//  /**
//   * 判断手机是否禁止切换4g(google手机原生系统在wifi不可用时候默认使用4g通道，不需要切换)
//   * @param context
//   * @return true-禁止主动切4g
//   */
//  public static boolean isDisAllowChange4G(Context context) {
//    String brand = (Build.BRAND).toLowerCase().trim();
//    String model = (Build.MODEL).toLowerCase().trim();
//    LogUtil.e("切4g判断:","wifi  当前手机 brand:" + brand + "|||model: " + model);
//    String[] disModelArray = context.getResources().getStringArray(R.array.disallow_4G_Change);
//    for (String disPhoneModel : disModelArray) {
//      String disPhoneModelLow = disPhoneModel.toLowerCase().trim();
//      if (disPhoneModelLow.equals(model)) {
//        LogUtil.e("切4g判断:","不允许切换4g");
//        return true;//不允许切换4g
//      }
//    }
//    return false;
//  }
}
