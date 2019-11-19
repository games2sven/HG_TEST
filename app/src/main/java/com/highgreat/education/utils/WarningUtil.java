package com.highgreat.education.utils;
import android.util.Log;
import android.util.SparseArray;


import com.highgreat.education.R;
import com.highgreat.education.bean.ZODroneInfoItem;
import com.highgreat.education.common.UavConstants;

import java.util.HashMap;

public class WarningUtil {
    /**
     * 报警类型
     */
    public static final int WARNING_PTZ = 0;//云台故障
    public static final int WARNING_COMPASS = 1;//磁故障(未校磁或者通讯失败)
    public static final int WARNING_BAT_1 = 2;//电量一级报警
    public static final int WARNING_BAT_2 = 3;//电量二级报警
    public static final int WARNING_GPS = 4;//GPS故障
    public static final int WARNING_JINFEI = 5;//禁飞区报警
    public static final int WARNING_OPTIFLOW = 6;//光流故障
    public static final int WARNING_ULTRASONIC = 7;//超声波故障
    public static final int WARNING_BAROMETER = 8;//气压计故障
    public static final int WARNING_ESC = 9;//电调故障
    public static final int WARNING_IMU = 10;//IMU故障
    public static final int WARNING_BATTERY = 11;//智能电池故障
    public static final int WARNING_COMPASS_LOST = 12;//磁故障(通讯失败)
    public static final int WARNING_EEPROM = 13;//内部存储器故障

    public static final int WARNING_ULTRASONIC_UN = 14;//0x00004000 超声波故障(未校准或者校准未通过)
    public static final int WARNING_ULTRASONIC_ERROR = 15;//0x00008000 超声波故障(8v供电异常)
    public static final int WARNING_ULTRASONIC_HARD = 16;//0x00010000 超声波故障(8v供电异常)
    public static final int WARNING_IMU_LOST = 17;//IMU故障（通讯失败或通讯异常）
    public static final int WARNING_IMU_ERROR = 18;//IMU故障（数据异常，16G）
    public static final int WARNING_GPS_WEAK = 19;//GPS环境异常
    public static final int WARNING_WITHOUT_LICENCE = 20;//未授权
    public static final int WARNING_VIBRATE_BIG = 21;//振动大
    public static final int WARNING_TEMPERATURE_HIGH = 22;//0x00400000  温度高(飞机过热)
    public static final int WARNING_COMPASS_BAD = 23;//0x00800000  磁场环境异常(磁数据异常)
    public static final int WARNING_COMPASS_ERROR = 24;//0x01000000  磁力计
    public static final int WARNING_BAROMETER_2 = 25;    //0x02000000   气压计故障-2-气压异常
    public static final int WARNING_GYRO_CALIBRATED = 26;// 陀螺仪是否校准过
    public static final int WARNING_ACCEL_CALIBRATED = 27;//加速度是否校准过
    //工厂
    public static final int WARNING_VIBRATE_FACTOR_BIG = 28;//工厂试飞期间振动大(试飞时间要求>=30s)
    public static final int WARNING_GPS_NOT_TEST = 29;//从未在室外测试过GPS
    public static final int WARNING_MAG_NOT_TEST = 30;//从未校准过指南针(校磁)
    public static final int WARNING_FLY_NOT_TEST = 31;//试飞时间不足20秒(试飞时间要求>=30s)

    //app自定义
    public final static int WARNING_DELAY = 32;//网络延时
    public final static int WARNING_OPTIFLOW_BAD = 33;//光流定位环境差
    public final static int WARNING_FORBID_FLY_AREA_EARLY_WARNING = 34;//飞机进入禁飞区100预警区域
    public final static int WARNING_ENABLE_FLY_UP = 35;//是否能够起飞
    public final static int WARNING_NO_CAMERA = 36;//没有图传视频
    public final static int WARNING_OPTIFLOW_EXCEPTION = 37;//光流异常
    public final static int AHRS_EXCEPTION = 38;//惯性导航异常
    public final static int WARNING_RECEIVER = 39;//接收机异常
    /**
     * Instance
     */
    private static WarningUtil mInstance;
    /**
     * 当前警告
     */

    private long mCurrentWarning = 0;

    /**
     * 解析报警值，存入byte arr
     */
    private byte[] mWarnByteArr;
    private HashMap<Integer,String> warningMap = new HashMap<>();


    public static WarningUtil getInstantce() {
        synchronized (WarningUtil.class) {
            if (mInstance == null) {
                mInstance = new WarningUtil();
            }
        }
        return mInstance;
    }

    public void warningChecking(long present,long sensor_health){
        if(isSameWarning(sensor_health)){
//            LogUtil.e("warningChecking","isSame:"+isSameWarning(sensor_health));
            return;
        }
        parseWarning(present,sensor_health);
    }
    /**
     * 报警是否相同
     */
   public boolean isSameWarning(long warning) {
        boolean isSame = (mCurrentWarning == warning);
        mCurrentWarning = warning;
        return isSame;
    }

    private void parseWarning(long present, long result) {
        mWarnByteArr = UavDataPaserUtil.byteToBinaryByteArr(result, 36);
        long SYRO = result & 0x01; // 陀螺仪传感器
        long ACCEL = result & 0x02; // 加速度传感器
        long MAG = result & 0x04; // 磁罗盘传感器
        long MAG2 = result & 0x80000;// 磁力计
        long ABSOLUTE_PRESSURE = present & 0x08; //气压计传感器
        long GPS = present & 0x20; // GPS模块
        long GPS_SIGNAL = result & 0x20;
        long OPTICAL_FLOW = result & 0x40; // 光流信号
        long FLOW_EXCEPTION = present & 0x40;//光流硬件
        long DIANTIAN = result & 0x8000;//电调
        long RC_REVEIVER = present & 0x10000;// 接收机
        long AHRS = result & 0x200000; // 惯性导航，位置速度是否收敛
        long STATUS_TERRAIN = result & 0x400000;//超声波
        long STATUS_BATTERY = present & 0x800000;//电池
        long GYRO_CALIBRATED = result & 0x1000000; // 陀螺仪是否校准过
        long ACCEL_CALIBRATED = result & 0x2000000;// 加速度计是否校准过
        long MAG2_CALIBRATED = result &  0x20000000;// 磁力计是否校准过
        if(FLOW_EXCEPTION==0){
            warningMap.put(WARNING_OPTIFLOW_EXCEPTION,UiUtil.getString(R.string.warning_6));
        }else {
            if(warningMap.containsKey(WARNING_OPTIFLOW_EXCEPTION))
                warningMap.remove(WARNING_OPTIFLOW_EXCEPTION);
        }
        if(GPS_SIGNAL==0){
            UavConstants.noGPS = true;
        }else {
            UavConstants.noGPS = false;
        }
        if(MAG2_CALIBRATED==0){
            warningMap.put(WARNING_COMPASS,UiUtil.getString(R.string.text_compass_alert));
            Log.i("flystateaa","指南针异常AA");
        }else {
                warningMap.remove(WARNING_COMPASS);
            Log.i("flystateaa","指南针正常  删除异常");
        }
        if(AHRS==0){
            if(!warningMap.containsKey(AHRS_EXCEPTION))
            warningMap.put(AHRS_EXCEPTION,UiUtil.getString(R.string.ahrs_exeception));
        }else {
            warningMap.remove(AHRS_EXCEPTION);
        }
        if(MAG2 == 0){
            warningMap.put(WARNING_COMPASS_ERROR,UiUtil.getString(R.string.magnetic_exception));
        }else {
            if(warningMap.containsKey(WARNING_COMPASS_ERROR))
                warningMap.remove(WARNING_COMPASS_ERROR);
        }
        if(ABSOLUTE_PRESSURE==0){
            warningMap.put(WARNING_BAROMETER_2,UiUtil.getString(R.string.absolute_presure));
        }else {
          if(warningMap.containsKey(WARNING_BAROMETER_2)){
              warningMap.remove(WARNING_BAROMETER_2);
          }
        }
        if(ACCEL == 0&&MAG ==0){
            warningMap.put(WARNING_IMU_ERROR,UiUtil.getString(R.string.imu_exception));
        }else{
            warningMap.remove(WARNING_IMU_ERROR);
        }
    /*    if(STATUS_TERRAIN==0){
            warningMap.put(WARNING_ULTRASONIC_ERROR,UiUtil.getString(R.string.ultrasonic_fault));
        }else {
            if(warningMap.containsKey(WARNING_ULTRASONIC_ERROR))
                warningMap.remove(WARNING_ULTRASONIC_ERROR);
        }*/
        if(STATUS_BATTERY==0){
            warningMap.put(WARNING_BATTERY,UiUtil.getString(R.string.battery_exception));
        }else {
            if(warningMap.containsKey(WARNING_BATTERY))
            warningMap.remove(WARNING_BATTERY);
        }
        if(GYRO_CALIBRATED ==0){
            warningMap.put(WARNING_GYRO_CALIBRATED,UiUtil.getString(R.string.GYRO_exception));
        }else {
           if(warningMap.containsKey(WARNING_GYRO_CALIBRATED))
               warningMap.remove(WARNING_GYRO_CALIBRATED);
        }
        if(ACCEL_CALIBRATED==0){
            warningMap.put(WARNING_ACCEL_CALIBRATED,UiUtil.getString(R.string.ACCEL_exception));
        }else {
           if(warningMap.containsKey(WARNING_ACCEL_CALIBRATED))
                warningMap.remove(WARNING_ACCEL_CALIBRATED);
        }
        if (MAG == 0 && MAG2 == 0) {
            warningMap.put(WARNING_COMPASS_LOST,UiUtil.getString(R.string.compass_exception));
            // LogUtil.e("health", "磁罗盘传感器异常");
        }else {
            if(warningMap.containsKey(WARNING_COMPASS_LOST))
                warningMap.remove(WARNING_COMPASS_LOST);
        }
        if (GPS == 0) {
            warningMap.put(WARNING_GPS,UiUtil.getString(R.string.warning_4));
        }else {
            if(warningMap.containsKey(WARNING_GPS))
                warningMap.remove(WARNING_GPS);
        }
        if(DIANTIAN == 0){
            warningMap.put(WARNING_ESC,UiUtil.getString(R.string.Electric_modulation));
        }else {
            if(warningMap.containsKey(WARNING_ESC)){
                warningMap.remove(WARNING_ESC);
            }
        }
        if(OPTICAL_FLOW == 0){
            UavConstants.badFlow = true;
        }else{
            UavConstants.badFlow = false;
        }

        if(RC_REVEIVER == 0){

        }else{

        }

    }
    public SparseArray setDroneInfo(SparseArray<ZODroneInfoItem> array){
       if(canTakeOff(warningMap) && UavConstants.isStanby){
           ZODroneInfoItem enable = getCorrectInfoItem(array, 1);
           if (enable != null) {
               enable.stateRes = R.string.drone_info_state_allow;
               enable.isEnable = false;
           }
           array.put(1,enable);
       }else {
           ZODroneInfoItem enable = getCorrectInfoItem(array, 1);
           enable.stateRes = R.string.drone_info_state_forbit;
           enable.dialogTitleRes = R.string.error_not_take_off;
           enable.dialogContentRes = R.string.drone_info_dialog_alert;
           enable.isEnable = true;
           array.put(1,enable);
       }

       if(warningMap.containsKey(WARNING_COMPASS_ERROR)|| warningMap.containsKey(WARNING_COMPASS)||warningMap.containsKey(WARNING_COMPASS_LOST)){
           ZODroneInfoItem compass = getCorrectInfoItem(array, 2);
           if (compass != null) {
               compass.stateRes = R.string.drone_info_state_abnormity;
               compass.dialogTitleRes = R.string.warning_1_land;
               compass.dialogContentRes = R.string.drone_info_dialog_compass;
               compass.isEnable = true;
               array.put(2, compass);
           }
       }else {
           ZODroneInfoItem compass = getCorrectInfoItem(array, 2);
           if (compass != null) {
               compass.stateRes = R.string.speed_normal;
               compass.isEnable = false;
               array.put(2, compass);
           }
       }
       if(warningMap.containsKey(WARNING_ULTRASONIC_ERROR)){
           ZODroneInfoItem ultrasonic = getCorrectInfoItem(array, 3);
           if (ultrasonic != null) {
               ultrasonic.stateRes = R.string.drone_info_state_abnormity;
               ultrasonic.dialogTitleRes = R.string.warning_7;
               ultrasonic.dialogContentRes = R.string.drone_info_dialog_alert;
               ultrasonic.isEnable = true;
           }
           array.put(3,ultrasonic);
       }else {
           ZODroneInfoItem ultrasonic2 = getCorrectInfoItem(array, 3);
           if (ultrasonic2 != null) {

               ultrasonic2.stateRes = R.string.speed_normal;
               ultrasonic2.isEnable = false;
           }
           array.put(3,ultrasonic2);
       }
       if(warningMap.containsKey(WARNING_BAROMETER_2)){
           ZODroneInfoItem barometer = getCorrectInfoItem(array, 4);
           if (barometer != null) {
               barometer.stateRes = R.string.drone_info_state_abnormity;
               barometer.dialogTitleRes = R.string.warning_8;
               barometer.dialogContentRes = R.string.drone_info_dialog_alert;
               barometer.isEnable = true;
           }
           array.put(4,barometer);
       }else {
           ZODroneInfoItem barometer = getCorrectInfoItem(array, 4);
           if (barometer != null) {
               barometer.stateRes = R.string.speed_normal;
               barometer.isEnable = false;
           }
           array.put(4,barometer);
       }
       if(warningMap.containsKey(WARNING_OPTIFLOW_EXCEPTION)|| warningMap.containsKey(WARNING_OPTIFLOW_BAD )){
           ZODroneInfoItem optiflow = getCorrectInfoItem(array, 5);
           if (optiflow != null) {
               optiflow.stateRes = R.string.drone_info_state_abnormity;
               optiflow.dialogTitleRes = R.string.warning_6;
               optiflow.dialogContentRes = R.string.drone_info_dialog_alert;
               optiflow.isEnable = true;
           }
           array.put(5,optiflow);
       }else {
           ZODroneInfoItem optiflow = getCorrectInfoItem(array, 5);
           if (optiflow != null) {
               optiflow.stateRes = R.string.speed_normal;
               optiflow.isEnable = false;
           }
           array.put(5,optiflow);
       }
       if(warningMap.containsKey(WARNING_IMU_ERROR)||warningMap.containsKey(WARNING_IMU)||warningMap.containsKey(WARNING_COMPASS_LOST)){
           ZODroneInfoItem imu = getCorrectInfoItem(array, 6);
           if (imu != null) {
               imu.stateRes = R.string.drone_info_state_abnormity;
               imu.dialogTitleRes = R.string.warning_10;
               imu.dialogContentRes = R.string.drone_info_dialog_alert;
               imu.isEnable = true;
           }
           array.put(6,imu);
       }else {
           ZODroneInfoItem imu = getCorrectInfoItem(array, 6);
           if (imu != null) {
               imu.stateRes = R.string.speed_normal;
               imu.isEnable = false;
           }
           array.put(6,imu);
       }
       if(warningMap.containsKey(WARNING_GPS)){
           ZODroneInfoItem gps = getCorrectInfoItem(array, 7);
           if (gps != null) {
               gps.stateRes = R.string.drone_info_state_abnormity;
               gps.dialogTitleRes = R.string.warning_4;
               gps.dialogContentRes = R.string.drone_info_dialog_alert;
               gps.isEnable = true;
           }
           array.put(7,gps);
       }else if(warningMap.containsKey(WARNING_GPS_WEAK)){
           ZODroneInfoItem gps2 = getCorrectInfoItem(array, 7);
           if (gps2 != null) {
               gps2.stateRes = R.string.drone_info_state_abnormity;
               gps2.dialogTitleRes = R.string.warning_19_land;
               gps2.dialogContentRes = R.string.drone_info_dialog_gps_low2;
               gps2.isEnable = true;
           }
           array.put(7,gps2);
       }else {
           ZODroneInfoItem gps2 = getCorrectInfoItem(array, 7);
           if (gps2 != null) {
               gps2.stateRes = R.string.speed_normal;
               gps2.isEnable = false;
           }
           array.put(7,gps2);
       }
       if(warningMap.containsKey(WARNING_ESC)){
           ZODroneInfoItem diantiao = getCorrectInfoItem(array,8);
           if(diantiao != null){
                diantiao.stateRes = R.string.drone_info_state_abnormity;
                diantiao.dialogTitleRes = R.string.Electric_modulation;
                diantiao.dialogContentRes = R.string.Electric_modulation;
                diantiao.isEnable = true;
           }
           array.put(8,diantiao);
       }else {
           ZODroneInfoItem diantiao = getCorrectInfoItem(array,8);
           if(diantiao != null) {
               diantiao.stateRes = R.string.speed_normal;
               diantiao.isEnable = false;
           }
           array.put(8,diantiao);
       }
       return array;
    }
    private ZODroneInfoItem getCorrectInfoItem(SparseArray<ZODroneInfoItem> array, int position) {
        ZODroneInfoItem zoDroneInfoItem;
//        if (array.size() == 8) {//Mark
//            switch (position) {
//                case 0:
//                    zoDroneInfoItem = array.get(0);
//                    break;
//                case 1:
//                    zoDroneInfoItem = array.get(1);
//                    break;
//                case 2:
//                    zoDroneInfoItem = null;
//                    break;
//                case 3:
//                    zoDroneInfoItem = null;
//                    break;
//                case 4:
//                    zoDroneInfoItem = null;
//                    break;
//                case 5:
//                    zoDroneInfoItem = array.get(2);
//                    break;
//                case 6:
//                    zoDroneInfoItem = array.get(3);
//                    break;
//                case 7:
//                    zoDroneInfoItem = null;
//                    break;
//                case 8:
//                    zoDroneInfoItem = array.get(4);
//                    break;
//                case 9:
//                    zoDroneInfoItem = array.get(5);
//                    break;
//                case 10:
//                    zoDroneInfoItem = array.get(6);
//                    break;
//                case 11:
//                    zoDroneInfoItem = array.get(7);
//                    break;
//                default:
//                    zoDroneInfoItem = null;
//                    break;
//
//
//            }
//
//        } else {//Take Hesper
            zoDroneInfoItem = array.get(position);
//        }

        return zoDroneInfoItem;

    }
    public HashMap<Integer,String> getWarningList() {
        return warningMap;
    }

    private boolean canTakeOff(HashMap map){
        boolean enable = true;
        if(map.containsKey(WarningUtil.WARNING_COMPASS) || map.containsKey(WarningUtil.WARNING_GPS) || map.containsKey(WarningUtil.WARNING_BATTERY) ||
                map.containsKey(WarningUtil.WARNING_COMPASS_LOST) || map.containsKey(WarningUtil.WARNING_ULTRASONIC_ERROR) ||
                map.containsKey(WarningUtil.WARNING_IMU_ERROR) || map.containsKey(WarningUtil.WARNING_COMPASS_ERROR) || map.containsKey(WarningUtil.WARNING_BAROMETER_2)
                || map.containsKey(WarningUtil.WARNING_OPTIFLOW_EXCEPTION)){
            enable = false;
        }
        return enable;
    }
}
