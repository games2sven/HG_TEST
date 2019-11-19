package com.highgreat.education.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * long milliseconds ：震动的时长，单位是毫秒
 * long[] pattern  ：自定义震动模式 。数组中数字的含义依次是[静止时长，震动时长，静止时长，震动时长。。。]时长的单位是毫秒
 * boolean isRepeat ： 是否反复震动，如果是true，反复震动，如果是false，只震动一次
 * 创建人：mac-likh
 * 创建时间：16/1/28 16:29
 * 修改人：PC"binary
 * 修改时间：16/06/29 16:29
 * 修改备注：
 */
public class VibratorUtil {
    static Vibrator vib;
    static Vibrator vibForPhoto;
    private static boolean isVib;

    public static void setisVib(boolean isVib) {
//        VibratorUtil.isVib = isVib;
    }

    public static void Vibrate(final Activity activity, long milliseconds) {
        if (!isVib) return;
        vibForPhoto = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vibForPhoto.vibrate(milliseconds);
    }


    public static void vibratorTwo(final Activity activity) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        long[] pattern = {500,500,500,500};
        vib.vibrate(pattern,-1);
    }
    public static void vibratorCommon(final Context activity) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        long[] pattern = {500,500,500,500};
        vib.vibrate(pattern,-1);
    }

    /**
     * 设置震动时间
     * @param context
     * @param milliseconds
     */
    public static void vibrateWithTime(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void StopVibrate() {
        if (vib == null) return;
        vib.cancel();
    }


}
