package com.highgreat.education.utils;

import android.util.Log;

import com.highgreat.education.BuildConfig;


/**
 * Created by liangzi on 2016/1/12.
 */
public class LogUtil {

    public static boolean isOpen = BuildConfig.DEBUG;
    public static final String TAG = "Camera_Log";

    /**
     * 可选是否保存日志信息到文件
     * @param tag
     * @param info
     * @param logFile true-保存日志信息到文件
     */
    public static void e(String tag, String info, boolean logFile) {
        if (isOpen) {
            Log.e(tag, info);
//            if(logFile){
//                FileLogger.getInstance().addLog("E " + tag, info);
//            }
        }
    }

    public static void i(String tag, String info, boolean logFile) {
        if (isOpen) {
            Log.i(tag, info);
//            if(logFile){
//                FileLogger.getInstance().addLog("I " + tag, info);
//            }
        }
    }

    public static void v(String tag, String info, boolean logFile) {
        if (isOpen) {
            Log.v(tag, info);
//            if(logFile){
//                FileLogger.getInstance().addLog("V " + tag, info);
//            }
        }
    }

    public static void d(String tag, String info, boolean logFile) {
        if (isOpen) {
            Log.d(tag, info);
//            if(logFile){
//                FileLogger.getInstance().addLog("D " + tag, info);
//            }
        }
    }

    public static void w(String tag, String info, boolean logFile) {
        if (isOpen) {
            Log.w(tag, info);
//            if(logFile){
//                FileLogger.getInstance().addLog("W " + tag, info);
//            }
        }
    }

    /**
     * 自定义tag字段
     * @param tag
     * @param info
     */
    public static void e(String tag, String info) {
        if (isOpen) {
            e(tag, info, false);
        }
    }

    public static void i(String tag, String info) {
        if (isOpen) {
            i(tag, info, false);
        }
    }

    public static void v(String tag, String info) {
        if (isOpen) {
            v(tag, info, false);
        }
    }

    public static void d(String tag, String info) {
        if (isOpen) {
            d(tag, info, false);
        }
    }

    public static void w(String tag, String info) {
        if (isOpen) {
            w(tag, info, false);
        }
    }

    /**
     * 默认tag字段
     * @param info
     */
    public static void e(String info) {
        if (isOpen) {
            e(TAG, info, false);
        }
    }

    public static void i(String info) {
        if (isOpen) {
            i(TAG, info, false);
        }
    }

    public static void v(String info) {
        if (isOpen) {
            v(TAG, info, false);
        }
    }

    public static void d(String info) {
        if (isOpen) {
            d(TAG, info, false);
        }
    }

    public static void w(String info) {
        if (isOpen) {
            w(TAG, info, false);
        }
    }

    public static void Lee(String info) {
        if (isOpen) {
            e("@LiKH ", info, false);
        }
    }

    public static void yc(String info) {
        if (isOpen) {
            yc(info, false);
        }
    }

    public static void yc(String info, boolean logFile) {
        if (isOpen) {
            e("@YC ", info, logFile);
        }
    }

}
