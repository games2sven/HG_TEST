package com.highgreat.education.utils;


import com.highgreat.education.MyApp;
import com.highgreat.education.common.UavConstants;

/**
 * Created by chengbin on 2016/7/5.
 * 存储和获取SP
 */
public class SpSetGetUtils {

  public SpSetGetUtils() {

  }


  //摇杆是什么手  1美国手   2日本手
  public static int getRockerMode() {
    return (int) SharedPreferencesUtils.getParam(MyApp.getAppContext(), UavConstants.SP_ROCKER_MODE, 1);
  }

  public static void setRockerMode(int i) {
    if(i == 1 || i == 2){
      SharedPreferencesUtils.setParam(MyApp.getAppContext(), UavConstants.SP_ROCKER_MODE, i);
    }
  }

  //飞行速度  1 普通 2快速
  public static int getFlySpeed() {
    return (int) SharedPreferencesUtils.getParam(MyApp.getAppContext(), UavConstants.SP_FLY_SPEED, 1);
  }

  public static void setFlySpeed(int i) {
    if(i == 1 || i == 2){
      SharedPreferencesUtils.setParam(MyApp.getAppContext(), UavConstants.SP_FLY_SPEED, i);
    }
  }

  //低电量报警值
  public static int getLowBattery() {
    return (int) SharedPreferencesUtils.getParam(MyApp.getAppContext(), UavConstants.SP_LOW_BATTERY, 1);
  }

  public static void setLowBattery(int i) {
      SharedPreferencesUtils.setParam(MyApp.getAppContext(), UavConstants.SP_LOW_BATTERY, i);
  }



}
