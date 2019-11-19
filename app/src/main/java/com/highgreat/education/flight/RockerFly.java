package com.highgreat.education.flight;

import android.view.View;


import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.FlightControlCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.SpSetGetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by chengbin on 2016/7/15.
 * 虚拟遥控飞行
 * 安全摇杆
 */
public class RockerFly {
  boolean  leftPress=false;
  boolean  rightPress=false;
  @Subscribe
  public void onEventMainThread(EventCenter eventCenter) {
    if (null != eventCenter) {
      int code = eventCenter.getEventCode();
      Object data = eventCenter.getData();
      switch (code) {
        case EventBusCode.CODE_ROCKER_MODE:
          rockerMode = (int) data;
          LogUtil.e("rockerMode", "rockerMode==" + rockerMode);
          initRockerFly();
          break;
        case  EventBusCode.CODE_RECOVERY_DEFAULT://恢复默认摇杆设置
          rockerMode = SpSetGetUtils.getRockerMode();
          initRockerFly();
          break;
//        case UavConstants.IS_FLY_ORITENTION_FAN:
//          isFlyOritentionFan = (boolean) data;
//          initRockerFly();
//          break;
          default:
            break;
      }
    }
  }

  private int                  rockerMode; //摇杆是什么手  0美国手   1日本手
  private boolean              isFlyOritentionFan; //摄像头朝向
  private RockerRelativeLayout rrlThrottleLeft;
  private RockerRelativeLayout rrlThrottleRight;
  private RockerView rvThrottleLeft;
  private RockerView           rvThrottleRight;
  public static float mi = 2f;

  /**
   * @param rrlThrottleLeft 左侧遥控范围
   * @param rrlThrottleRight 右侧遥控范围
   * @param rvThrottleLeft 左侧摇杆
   * @param rvThrottleRight 右侧摇杆
     */
  public RockerFly(
                   RockerRelativeLayout rrlThrottleLeft, RockerRelativeLayout rrlThrottleRight,
                   RockerView rvThrottleLeft, RockerView rvThrottleRight) {
    EventBus.getDefault().register(this);
    rockerMode = SpSetGetUtils.getRockerMode();
//    isFlyOritentionFan = SpSetGetUtils.getIsFlyOritentionFan();
    this.rrlThrottleLeft = rrlThrottleLeft;
    this.rrlThrottleRight = rrlThrottleRight;
    this.rvThrottleLeft = rvThrottleLeft;
    this.rvThrottleRight = rvThrottleRight;
    initListener();
  }

  private void initListener() {
    /**左侧摇杆显示/隐藏监听*/
    rvThrottleLeft.setOnSensorLinstener(new RockerView.RockerViewListener(){
      @Override
      public void toggleLeftOrRight(boolean isShow) {
        leftPress=!isShow;
        UavConstants.IS_RC_OPERATIONING=rightPress||leftPress;
//        if(isShow){
//          mCameraActivity.showCameraLeftControll();
//        }else{
//          mCameraActivity.hideCameraLeftControll();
//        }
      }
    });
    /**右侧摇杆显示/隐藏监听*/
    rvThrottleRight.setOnSensorLinstener(new RockerView.RockerViewListener(){
      @Override
      public void toggleLeftOrRight(boolean isShow) {
        rightPress=!isShow;
        UavConstants.IS_RC_OPERATIONING=rightPress||leftPress;
//        if(isShow){
//          mCameraActivity.showCameraRightControll();
//        }else{
//          mCameraActivity.hideCameraRightControll();
//        }
      }
    });
  }

  public void initRockerFly() {
    initRocker(rvThrottleLeft, rvThrottleRight);
  }

  //油门
  private void initRocker(RockerView mSvRocker1, RockerView mSvRocker2) {
    if (rockerMode == UavConstants.ROCKER_LEFT_THROTTLE) {//左手油门（美国手）
      mSvRocker1.initView(R.mipmap.rocker_left_throttle_left,
              R.mipmap.rocker_point,
              R.mipmap.rocker_point);
      mSvRocker1.setOnRockerChanged(new RockerViewInterface() {
        @Override
        public void onRockerChanged(View view, float x, float y, float angle) {
          //xuanZhuan = x; // 左右方向  shangXia = y;// 上下升降
          x = getX(x, angle);
          y = -getY(y, angle);
          if(x >=0.99){
            x = 1.0f;
          }else if(x <= -0.99){
            x = -1.0f;
          }
          if(y >0.99){
            y = 1.0f;
          }else if(y < -0.99){
            y = -1.0f;
          }
          FlightCommand.getInstance().setShangxia(y);
          FlightCommand.getInstance().setXuanzhuan(x);


        }
      });

      mSvRocker2.initView(R.mipmap.rocker_left_throttle_right,
              R.mipmap.rocker_point,
              R.mipmap.rocker_point);
      mSvRocker2.setOnRockerChanged(new RockerViewInterface() {
        @Override
        public void onRockerChanged(View view, float x, float y, float angle) {
          //zuoYou = x;// 左右副翼   qianHou = y;// 上下油门
          x = getXOrY(x);
          y = -getXOrY(y);
          LogUtil.i("rockerChange2","x:" + x + "  y:" +y);
          if(x >0.99){
            x = 1.0f;
          }else if(x < -0.99){
            x = -1.0f;
          }
          if(y >0.99){
            y = 1.0f;
          }else if(y < -0.99){
            y = -1.0f;
          }
          FlightCommand.getInstance().setZuoyou(x);
          FlightCommand.getInstance().setQianhou(y);
        }
      });
    } else if (rockerMode ==  UavConstants.ROCKER_RIGHT_THROTTLE) {//右手油门（日本手）
      mSvRocker1.initView(R.mipmap.rocker_right_throttle_left,
              R.mipmap.rocker_point,
              R.mipmap.rocker_point);
      mSvRocker1.setOnRockerChanged(new RockerViewInterface() {
        @Override
        public void onRockerChanged(View view, float x, float y, float angle) {
          // xuanZhuan = x; // 左右方向  qianHou = y;// 上下升降

          x = getXOrY(x);
          y = -getXOrY(y);
          LogUtil.i("rockerChange2","x:" + x + "  y:" +y);
          if(x >0.99){
            x = 1.0f;
          }else if(x < -0.99){
            x = -1.0f;
          }
          if(y >0.99){
            y = 1.0f;
          }else if(y < -0.99){
            y = -1.0f;
          }
          FlightCommand.getInstance().setQianhou(y);
          FlightCommand.getInstance().setXuanzhuan(x);
        }
      });

      mSvRocker2.initView(R.mipmap.rocker_right_throttle_right,
              R.mipmap.rocker_point,
              R.mipmap.rocker_point);
      mSvRocker2.setOnRockerChanged(new RockerViewInterface() {
        @Override
        public void onRockerChanged(View view, float x, float y, float angle) {
          //zuoYou = x;// 左右副翼  shangXia = y;// 上下油门

          x = getX(x, angle);
          y = -getY(y, angle);
          if(x >0.99){
            x = 1.0f;
          }else if(x < -0.99){
            x = -1.0f;
          }
          if(y >0.99){
            y = 1.0f;
          }else if(y < -0.99){
            y = -1.0f;
          }
          FlightCommand.getInstance().setZuoyou(x);
          FlightCommand.getInstance().setShangxia(y);
        }
      });
    }
  }

  public void dismissRockerFly() {
//    UavConstants.RC_CMD_MODE = 0;
    resetPoint();
    if (null != rrlThrottleLeft) {
      rrlThrottleLeft.resetlayout();
    }
    if (null != rrlThrottleRight) {
      rrlThrottleRight.resetlayout();
    }
//    myApplication.uavParaGetAndSendManager.resetRocker(1500, 1500, 1500, 1500);
  }

  public void resetPoint() {
    if (null != rvThrottleLeft) {
      rvThrottleLeft.reset();
    }
    if (null != rvThrottleRight) {
      rvThrottleRight.reset();
    }
//    myApplication.uavParaGetAndSendManager.resetRocker(1500, 1500, 1500, 1500);
  }

  private int toIntCmd(float f) {
    return (int) (f * 1000);
  }

  private float getX(float x, float angle) {
    if ((Math.abs(angle) > 0 && Math.abs(angle) < 10) || ((180 - Math.abs(angle)) > 0
        && (180 - Math.abs(angle)) < 10)) {
      x = 0;
      return x;
    }
    return getXOrY(x);
  }

  private float getY(float y, float angle) {
    if ((Math.abs(angle) > 80 && Math.abs(angle) < 100)) {
      y = 0;
      return y;
    }
    return getXOrY(y);
  }

  private float getXOrY(float z) {
    if (Math.abs(z) <= 0.05) {
      z = 0f;
      return z;
    }

    if (z > 0) {
      z = (float) Math.pow(z, mi);
    } else if (z < 0) {
      z = -(float) Math.pow(-z, mi);
    }
    return z;
  }

  public void reset(){
    if(rvThrottleLeft!=null) {
      rvThrottleLeft.reset();
      rvThrottleLeft.setAlpha(0.3f);
    }

    if(rvThrottleRight!=null) {
      rvThrottleRight.reset();
      rvThrottleRight.setAlpha(0.3f);
    }
  }

  public void destroy(){
    EventBus.getDefault().unregister(this);
  }
}
