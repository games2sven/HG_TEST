package com.highgreat.education.utils;
import android.content.Context;
import android.text.TextUtils;
import com.highgreat.education.MyApp;
import com.highgreat.education.common.Constants;


/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class UserUtils {

    private static final String KEY_EMAIL = "edit_data_email";
    private static final String KEY_OUTSIDE_DANCE ="out_side_dance" ;
    private static final String DRONE_TYPE ="drone_type" ;
    public static final String START_FLIGHT_INFO ="start_flight_info" ;
    private static long lastClickTime;
    private static long lastClickTimes;
    private static String USER_ID = null;
    public static final String KEY_TEL = "edit_data_tel";
    public static final String KEY_REALNAME = "edit_data_name";
    public static final String KEY_NICKNAME = "edit_data_nickname";
    public static final String KEY_ADDRESS = "edit_data_address";
    public static final String KEY_SEX = "edit_data_sex";
    public static final String KEY_AVATAR = "edit_data_avatar";
    public static final String KEY_BGURL = "edit_data_bgurl";
    public static final String KEY_USERAUTH = "edit_data_serauth";

    public static String U_STATUS        = "uStatus";






    public static boolean isLogin() {
        return getUserStatus() != 0;
    }


    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000 && time - lastClickTime > 0) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public synchronized static boolean isRepeatClick(long times) {
        long time = System.currentTimeMillis();
        if (time - lastClickTimes < times) {
            return true;
        }
        lastClickTimes = time;
        return false;
    }

    /**
     * 判断是不是需要特殊处理的服务端code码
     *
     * @param status
     * @return
     */
    public static boolean isErrorCode(int status) {
        return (status == 110 || status == 107 || status == 106 || status == 100000011||status==0||status==9999);
    }

    //是否登录
    public static void setUserStatus(int userLevel) {
        SharedPreferencesUtils.setParam(MyApp.getInstance().getAppContext(), U_STATUS, userLevel);
    }

    public static int getUserStatus() {
        return  (int) SharedPreferencesUtils.getParam(MyApp.getInstance().getAppContext(), U_STATUS, 0);
    }


    private static String SESSIONID_ID = null;


    private static void clearUserData(Context context) {
//        PreferencesUtil.removeParam(context, UavConstants.USER_ID);
        SharedPreferencesUtils.removeParam(context, KEY_NICKNAME);
        SharedPreferencesUtils.removeParam(context, KEY_AVATAR);
        SharedPreferencesUtils.removeParam(context, KEY_TEL);
        SharedPreferencesUtils.removeParam(context, KEY_ADDRESS);
        SharedPreferencesUtils.removeParam(context, KEY_SEX);
        SharedPreferencesUtils.removeParam(context, KEY_BGURL);
        SharedPreferencesUtils.removeParam(context, KEY_USERAUTH);
        SharedPreferencesUtils.removeParam(context, KEY_EMAIL);
    }

}
