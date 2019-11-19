package com.highgreat.education.common;

public class EventBusCode {

    public static final int COMMAND_ACK_BACK = 1;
    public static final int HEART_BEAT_FROM_PLANE = 2;
    public static final int HIGH_FUNCTION_CODE = 3;//高级功能
    public static final int GET_SERVER_OS = 4;//获取服务器os信息返回
    public static final int GET_SERVER_OS_FAIL = 5;//获取服务器os信息返回错误
    public static final int UPLOAD_OS_SUCCESSED = 6;//获取服务器os信息返回成功
    public static final int UPLOAD_OS_PROGRESS = 7;//固件上传进度
    public static final int DOWNLOAD_OS_PROGRESS = 8;//固件下载进度
    public static final int DOWNLOAD_OS_SUCCESSE = 9;//固件下载完成
    public static final int FORMAT_SUCCESSE = 10;//格式化完成
    public static final int GET_VERSION_INFO_SUCCESS = 11;//获取飞机版本信息成功
    public static final int OS_UPGRADE_CALLBACK = 12;//飞控升级应答
    public static final int APP_INTO_BACKGROUNG = 13;//应用退到后台

    public static final int CODE_NETUNCONNECT = 16;
    public static final int CODE_NETCONNECT = 17;
    public static final int CODE_UAVNETCONNECT = 18;
    public static final int CODE_WIFISSID_CHANGE = 19;
    public static final int CODE_CONNECT_INTERNET = 20;
    public static final int CODE_WIFI_STATE_DISABLED = 21;//wifi关闭
    public static final int CODE_LOGOUT = 22;
    public static final int CODE_IS_APP_HAS_NEW = 23;

    public static final int CODE_LOAD_WELCOME_PICTURE = 24;//欢迎界面图片加载成功


    public static final int CODE_UDP_CONN_TIMEOUT = 28;
    public static final int CODE_REQUEST_OK = 29;
    public static final int CODE_REQUEST_ERROR = 30;
    public static final int CODE_REQUEST_EMPTY = 31;
    public static final int CODE_REQUEST_VIDEO_EMPTY = 32;
    public static final int CODE_LOAD_PIC_ERRO = 33;
    public static final int CODE_LOAD_VIDEO_SUCCESS = 34;
    public static final int CODE_PICINBIG_DELETE = 35;
    public static final int CODE_PICINBIG_LOCALDELETE = 36;//删除本地照片
    public static final int CODE_STOP_VIDEOING = 37;//停止录像
    public static final int CODE_VIDEO_DOWNLOAD_FINISH = 38;//视频全部下载完成
    public static final int CODE_IMAGE_DOWNLOAD_FINISH = 39;//图片全部下载完成
    public static final int CODE_LOAD_IMAGE_SUCCESS = 40;
    public static final int CODE_DOWN_SINGLE_SUCCESS = 41;//下载单个图片成功
    public static final int CODE_ROCKER_MODE = 42;
    public static final int HEART_BEAT_SYS_STATUS = 43;

    public static  final int CODE_ADD_NEED_DOWNLOAD_PIC =44;
    public static  final int CODE_ADD_NEED_DOWNLOAD_VIDEO =45;
    public static  final int FLY_DATA_FEEDBACK =46;


    public static final int WIFI_CHANGE_CALLBACK = 47;

    public static final int CODE_RECOVERY_DEFAULT = 48;

}
