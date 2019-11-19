package com.highgreat.education.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.highgreat.education.BuildConfig;
import com.highgreat.education.MyApp;
import com.highgreat.education.utils.LocaleLanguageUtil;
import com.highgreat.education.utils.ServerSignUtils;
import com.highgreat.education.utils.SystemUtil;

import java.util.HashMap;
import java.util.Map;

//import com.zhy.http.okhttp.OkHttpUtils;
//import com.zhy.http.okhttp.callback.FileCallBack;

/**
 * 请求参数/url和请求方式的封装,带SessionID参数，当前版本使用
 */
public class RestClientWithSSID {
    private static final String requestParamSign = "sign";
    private static final String requestParamTime = "time";

    /**
     * post参数公共部分统一封装
     */
    public static Map<String, String> getParams(Map<String, String> paramsMap) {

        if (paramsMap==null)  return null;
        //计算md5值
        Map<String, String> paramsTemp = ServerSignUtils.sortMapByKey(paramsMap);
        String signMD5 = ServerSignUtils.getSignMD5(paramsTemp);
        //添加公共参数
        paramsMap.put(requestParamSign, signMD5);
        paramsMap.put(requestParamTime, getRequestTimeValue());
//        LogUtil.e("请求参数统一封装：" + paramsMap.toString());
        return paramsMap;
    }

    @NonNull
    private static String getRequestTimeValue() {
        return System.currentTimeMillis() / 1000 + "";
    }

    /**
     * headers统一封装
     */
    public static Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("sendsessid", "0");
        headers.put("v", getVersionString());
        return headers;
    }

    /**
     * url公共部分统一拼接
     */
    public static String getAbsoluteUrl(String relativeUrl) {
        return BuildConfig.SERVER_API + relativeUrl;
    }

    /**
     * 接口请求统一v参数，例如：1-1-2.0.0-1-1
     * 格式：
     * 第一位：端（1-ios 2-android）
     * 第二位：语言【支持十位】（1-简体中文 2-英语 3-日语 4-韩语 5-台湾繁体 6-香港繁体 7-法语）
     * 第三位：版本号 （客户端）
     * 第四位：版本包（2-测试 0/1-正式）
     * 第五位：接口/模块（1-美拍1接口  2-美拍2接口 ） 这个数阔以暂时不管
     * 第六位：Android的系统版本，如果没有，传0
     * 第七位：手机的型号（如果有型号中有“-”则去掉）如：iPhone6s,
     * 第八位：APP类型 如：1,-FlightGo,2-Amigo,3 Fylo 4 Folk
     * @return
     */
    public static String getVersionString(){
        StringBuilder stringBuilder = new StringBuilder();
//        int    languageValue = LocaleLanguageUtil.getStance().getLanguageCode();
        int interfaceModule = 11 ;
        String currentapiVersion=android.os.Build.VERSION.RELEASE;
        String mobileModels = android.os.Build.MODEL.replace("-","");
        String versionStr = stringBuilder
                .append("2-")
                .append(1 + "-")
                .append(SystemUtil.getVersionName(MyApp.getAppContext()) + "-")
                .append(2 + "-") //2-测试环境 0-正式环境(服务端要求添加此参数控制服务端在同一接口地址情况下走正式逻辑还是测试逻辑,默认0-正式)
                .append(interfaceModule+"-") //接口/模块（0/1-TAKE ，2-AMIGO， 3-VIEW， 4-HESPER，5 - MARK，6-F01S 7-F02S 9-B02 10-B02灯球  11-F07 99-无)
                .append(currentapiVersion+"-")//系统版本
                .append(mobileModels+"-")//手机型号
                .append(4)//APP类型
                .toString();

        Log.e("v_params","params===="+versionStr);
        return  versionStr;
    }


}
