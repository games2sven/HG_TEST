package com.highgreat.education.common;

import android.os.Environment;

import com.highgreat.education.MyApp;

import java.io.File;

public class Constants {

    public static final int FIRMWARE_VERSION_DEFAULT = 0;
    public static final String FYLO ="FYLO/";
    public static final String DO1 ="DO1/" ;
    //是否连接了飞机
    public static        boolean IS_UAV_CONN                      = false;

    public static boolean agpsdownloadflag       = true;//agps后台线程开启标记
    public static final int FLY_DATA_FEEDBACK         = 1;
    public static final int CODE_SYSTEM_HOME_KEY         = 96;
    public  static int CURRENT_DANCE_ID=0;

    public static final File BASE_EXTERNAL_PATH               = Environment.getExternalStorageDirectory();
    private static final File BASE_EXTERNAL_PATH_PIC           =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    private static final File BASE_EXTERNAL_PATH_MOVIES           =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    private static final File BASE_EXTERNAL_PATH_DATA          =
            MyApp.getAppContext().getExternalFilesDir(null);
    public static final File BASE_DATA_PATH                   = MyApp.getAppContext().getFilesDir();

    public static  boolean  IS_READY_FLY;

    public static boolean FIRMWARE_NEED_UPDATE = false;//固件是否需要升级

    public static  boolean IS_APP_FORCE                   = false; //APP强制升级


    /**
     * 数据保存绝对位置
     */
    public static final String DATA_PATH =
            (BASE_EXTERNAL_PATH_DATA != null ? BASE_EXTERNAL_PATH_DATA : BASE_DATA_PATH).getAbsolutePath()
                    + "/";

    public static final String FIRMWARE_PATH = DATA_PATH+"firmware/f07/";
    public static final String FLIGHT_LOG = DATA_PATH + "/log/";
    public static long DANCE_RECIEVER_INFO_TIME;

    public  static int BATTERY_WARM=20;
    public  static  int  SPEED_MODE=1;

    public  static boolean  IS_OUT_ENTER_SETTING_ACTIVITY =true;

    //产生一个随机数
    public static int getRandom(){
        //产生0---1000的整数随机数
        int randomnum = (int) (Math.random() * 1000);
        return randomnum;
    }


    public final static int WARM_1 = 0;//电量低，请谨慎飞行
    public final static int WARM_2 = 1;//电量低，即将降落
    public final static int WARM_3 = 2;//电量低，无法起飞
    public final static int WARM_4 = 3;// 存储卡空间不足，无法拍摄
    public final static int WARM_5 = 4;//未检测到存储卡，无法拍摄
    public final static int WARM_6 = 5;//光线不足，请谨慎飞行



    public final static int ERROR_1 =6;//定位系统异常，无法起飞
    public final static int ERROR_2= 7;//加速传感器异常，无法起飞
    public final static int ERROR_3 = 8;//陀螺仪传感器异常，无法起飞
    public final static int ERROR_4 = 9;//光流异常，无法起飞
    public final static int ERROR_5 = 10;//气压计异常，无法起飞
    public final static int ERROR_6 = 11;//系统准备中，无法起飞
    public final static int ERROR_7 = 12;//自检未通过，无法起飞
    public final static int ERROR_8 = 13;//电池异常，即将降落
    public final static int ERROR_9= 14;//电池异常，无法起飞


}
