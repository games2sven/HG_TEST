package com.highgreat.education.common;

import android.os.Environment;

import com.highgreat.education.utils.UiUtil;

import java.io.File;

public class UavConstants {
    public static final String ipAdress = "192.168.1.1";//TCP
    public static final int mavPort = 5000;//TCP
    public static  boolean  isFlying = false;//
    public static  boolean  isHovering = false;//悬停

    /**
     * 相册布局的列数
     */
    public static        int     RECYCLERVIEWCOLUMS               = 5;
    public static long tCPConnectTime;//是否已经连接
    public static boolean needReconnect = false;//需要重连
    public static boolean usbConnected;
    public static final  int     COMMON_WIDTH                     = 1920;


    public static final File BASE_EXTERNAL_PATH               = Environment.getExternalStorageDirectory();
    public static final File    BASE_DATA_PATH                   = UiUtil.getContext().getFilesDir();
    /**
     * 缩略图下载后高清图的的保存绝对位置
     */
    public static  String  BIG_PIC_ABSOLUTE_PATH            = //fix bug 启动失败的问题，有些机器无法获取picture目录
            (BASE_EXTERNAL_PATH != null ? BASE_EXTERNAL_PATH : BASE_DATA_PATH).getAbsolutePath()
                    + "/highgreat/folkPhoto";

    /**
     * 视频下载后高清图的的保存绝对位置
     */
    public static String  BIG_VIDEO_ABSOLUTE_PATH =
            (BASE_EXTERNAL_PATH != null ? BASE_EXTERNAL_PATH : BASE_DATA_PATH).getAbsolutePath()
                    + "/highgreat/folkVideo";

    /**
     * 所有本地图片和视频缩略图路径
     */
    public static String  LOCAL_VIDEO_THUMB_ABSOLUTE_PATH =
            BIG_VIDEO_ABSOLUTE_PATH+"/thumbnail";


    public  static final boolean isSupportSDCard =false;

    public  static  boolean  IS_RC_OPERATIONING=false;//是否正在操作摇杆
    //#566500
    public static int[] BLUE_COLORS = {0xFF519cfb,0xFF487be5,0xFF4263d7,0xFF3126b0};
    public static int[] YELLOW_COLORS = {0xFFa4bd1d,0xFFf0ff92};
    public static int[] RED_COLORS = {0xFFf34f5e,0xFFe13f4f,0xFFc32437,0xFFa2061d };
    public static int[] GRAY_COLORS = {0xFF333333,0xFF333333,0xFF333333,0xFF333333 };
    public static        boolean IS_UAV_CONN                      = false;
    public static boolean isSelectState          = true;

    public static        boolean ISDOWNLOADFINISH                 = true;
    public static        boolean ISDOWNLOADIMAGEFINISH            = true;

    public static boolean isConnectDevice = false;
    public static boolean shutdownRequested = true;
    public static final int LOCATION_MODE = 0;
    public static final int UWB_MODE = 1;
    public static int DATA_TRANSMISSION = LOCATION_MODE;//默认是定点模式
    public static boolean newDroneConnected;//起飞检测后有新的飞机连接上
    public static boolean startFlyCheck;// 开始起飞检测
    public static boolean LOCATION_CALIBRATION_SUCCESS;
    public static boolean LOCATION_CALIBRATION_FAIL;
    //-----------------左手油门或右手油门-------------------------
    public static final  int     ROCKER_RIGHT_THROTTLE            = 1;//右手油门（日本手）
    public static final  int     ROCKER_LEFT_THROTTLE             = 2;//左手油门（美国手）
    public static final  String  SP_ROCKER_MODE                   = "spRockerMode";
    public static final  String  SP_FLY_SPEED                   = "spFlySpeed";
    public static final  String  SP_LOW_BATTERY                   = "spLowBattery";
    public static int SELECT_CONTROL_MODEL = 25;
    public static final int IS_FLY_ORITENTION_FAN          = 26;
    public static final int CODE_IS_FIRST_SAFETY_TOUCH_FLY = 27;

    public static        int     SCREEN_WIDTH                     = 0;
    public static        int     SCREEN_hEIGHT                    = 0;
    public static        int     MARGIN                           = 0;
    public static        int     GALLERY_WIDTH                    = 0;


    public static float CURRENT_FTP_VERSION     = 0.00f;//飞控当前版本号
    public static boolean CURRENT_FLIGHT_CONNECTED   = false;//直接连接的飞机

    public static boolean noGPS;
    public static boolean badFlow;
    public static boolean isStanby;
}
