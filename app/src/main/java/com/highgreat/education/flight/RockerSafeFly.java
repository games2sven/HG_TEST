package com.highgreat.education.flight;



import android.app.Activity;


import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.widget.RockerSafeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2017/3/3.
 */

public class RockerSafeFly {
    boolean  leftPress=false;
    boolean  rightPress=false;
    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object data = eventCenter.getData();
            switch (code) {
//                case UavConstants.CODE_ROCKER_MODE:
//                    rockerMode = (int) data;
//                    initRockerSafeFly();
//                    break;
//                case UavConstants.IS_FLY_ORITENTION_FAN:
//                    isFlyOritentionFan = (boolean) data;
//                    initRockerSafeFly();
//                    break;
            }
        }
    }

    private int rockerMode; //摇杆是什么手  1美国手   其他日本手
    private boolean isFlyOritentionFan; //摄像头朝向 true 自拍 false 取景
    private RockerSafeView left,right;

    public RockerSafeFly(RockerSafeView left, RockerSafeView right, Activity mCameraActivity){
        EventBus.getDefault().register(this);
        this.left = left;
        this.right = right;
    }

    public void initRockerSafeFly(){
        left.setIsLeft(true,rockerMode);
        right.setIsLeft(false,rockerMode);
        left.setOnSensorLinstener(new RockerSafeView.RockerSafeListener() {
            @Override
            public void onRockerClick(int position,boolean isDown) {

                switch (position){
                    case 0: //top
                        if(rockerMode == 1){
                            if(isDown)
                            FlightCommand.getInstance().setShangxia(0.6f);
                            else
                                FlightCommand.getInstance().setShangxia(0.0f);
                        }else{
                            if(isDown)
                            FlightCommand.getInstance().setQianhou(0.6f);
                            else
                                FlightCommand.getInstance().setQianhou(0.0f);
                        }
                        break;
                    case 1: //right
                        if(isDown)
                        FlightCommand.getInstance().setXuanzhuan(0.6f);
                        else
                            FlightCommand.getInstance().setXuanzhuan(0.0f);
                        break;
                    case 2: //bottom
                        if(rockerMode == 1){
                            if(isDown)
                            FlightCommand.getInstance().setShangxia(-0.6f);
                            else
                                FlightCommand.getInstance().setShangxia(0.0f);
                        }else{
                            if(isDown)
                            FlightCommand.getInstance().setQianhou(-0.6f);
                            else
                                FlightCommand.getInstance().setQianhou(0.0f);
                        }
                        break;
                    case 3: //left
                        if(isDown)
                        FlightCommand.getInstance().setXuanzhuan(-0.6f);
                        else
                            FlightCommand.getInstance().setXuanzhuan(0.0f);
                        break;
                }
            }

            @Override
            public void toggleLeftOrRight(boolean isShow) {
                leftPress=!isShow;
            }
        });
        right.setOnSensorLinstener(new RockerSafeView.RockerSafeListener() {
            @Override
            public void onRockerClick(int position,boolean isDown) {
                switch (position){
                    case 0: //top
                        if(rockerMode == 1){
                            if(isDown)
                            FlightCommand.getInstance().setQianhou(0.6f);
                            else
                                FlightCommand.getInstance().setQianhou(0.0f);
                        }else{
                            if(isDown)
                            FlightCommand.getInstance().setShangxia(0.6f);
                            else
                                FlightCommand.getInstance().setShangxia(0.0f);
                        }
                        break;
                    case 1: //right
                        if(isDown)
                        FlightCommand.getInstance().setZuoyou(0.6f);
                        else
                            FlightCommand.getInstance().setZuoyou(0.0f);
                        break;
                    case 2: //bottom
                        if(rockerMode == 1){
                            if(isDown)
                            FlightCommand.getInstance().setQianhou(-0.6f);
                            else
                                FlightCommand.getInstance().setQianhou(0.0f);
                        }else{
                            if(isDown)
                            FlightCommand.getInstance().setShangxia(-0.6f);
                            else
                                FlightCommand.getInstance().setShangxia(0.0f);
                        }
                        break;
                    case 3: //left
                        if(isDown)
                        FlightCommand.getInstance().setZuoyou(-0.6f);
                        else
                            FlightCommand.getInstance().setZuoyou(0.0f);
                        break;
                }
            }

            @Override
            public void toggleLeftOrRight(boolean isShow) {
                rightPress=!isShow;
            }
        });
    }

    public void dismissRockerSafeFly(){
        reset();
    }

    public void reset(){
        if(left!=null)
            left.reset();
        if(right!=null)
            right.reset();
    }

    public void destroy(){
        EventBus.getDefault().unregister(this);
    }
}
