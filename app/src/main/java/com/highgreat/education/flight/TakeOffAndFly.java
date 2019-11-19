package com.highgreat.education.flight;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.MAVLink.common.msg_command_ack;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.enums.MAV_BASE_MODE;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_RESULT;
import com.MAVLink.enums.MAV_STATE;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.FlyData;
import com.highgreat.education.bean.HeartAliveModel;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dialog.MaterialDialogBuilderL;
import com.highgreat.education.dialog.TakeOffDialog;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.TimeUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.utils.WaterUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;

public class TakeOffAndFly {

    private Context mContext;
    private ImageView oneKeyTakeOff;
    private TakeOffDialog takeOffDialog;
    private ImageView land;
    private ImageView emergencyHover;


    private  int fly_mode;
    private short sys_stutus;
    private byte battery_remaining;
    private MaterialDialog landDialog;

    public TakeOffAndFly(Context context, ImageView oneKeyTakeOff,ImageView land,ImageView emergencyHover){
        EventBus.getDefault().register(this);
        this.mContext = context;
        this.oneKeyTakeOff = oneKeyTakeOff;
        this.land = land;
        this.emergencyHover = emergencyHover;
    }

    int  battery;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object data = eventCenter.getData();
            switch (code){
                case EventBusCode.COMMAND_ACK_BACK:     /* ACK应答状态信息返回 */
                    msg_command_ack ack = (msg_command_ack) data;
                    LogUtil.e("COMMAND_ACK_BACK:=="+((msg_command_ack) ack).result+"   "+ack.command);

                    switch (ack.command){
                        case MAV_CMD.MAV_CMD_CTR_TAKEOFF:
                        case MAV_CMD.MAV_CMD_CTR_LAND_ENTER:
                        case MAV_CMD.MAV_CMD_REQUEST_AUTOPILOT_CAPABILITIES:
                            if(ack.result == MAV_RESULT.MAV_RESULT_ACCEPTED){
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case EventBusCode.HEART_BEAT_FROM_PLANE:
                    msg_heartbeat heartbeat = (msg_heartbeat) eventCenter.getData();
                     sys_stutus = heartbeat.system_status;
//                    LogUtil.e("sys_stutus==="+sys_stutus);
                    fly_mode = heartbeat.flight_mode;

                    UavConstants.isFlying =(sys_stutus == MAV_STATE.MAV_STATE_FLYING);
                    if(sys_stutus == MAV_STATE.MAV_STATE_CALIBRATING){//校准中

                    }else if(sys_stutus == MAV_STATE.MAV_STATE_POWEROFF){//关机中

                    } else if (sys_stutus == MAV_STATE.MAV_STATE_STANDBY ||sys_stutus == MAV_STATE.MAV_STATE_UNINIT) {//准备好起飞 /起飞准备完毕
                        emergencyHover.setVisibility(View.GONE);
                        land.setVisibility(View.GONE);
                        oneKeyTakeOff.setVisibility(View.VISIBLE);
                        oneKeyTakeOff.setEnabled(true);
                        UavConstants.isHovering = false;
//                        oneKeyTakeOff.setAlpha(1f);
                    } else if (fly_mode==MAV_BASE_MODE.FLIGHT_MODE_TAKE_OFF||sys_stutus ==MAV_STATE.MAV_STATE_ACTIVE){

                        UavConstants.isHovering = false;
                        oneKeyTakeOff.setEnabled(false);

                    }else if (sys_stutus == MAV_STATE.MAV_STATE_FLYING && fly_mode !=MAV_BASE_MODE.FLIGHT_MODE_LAND&&fly_mode==MAV_BASE_MODE.FLIGHT_MODE_POSHOLD){//飞行中
                        emergencyHover.setVisibility(View.GONE);
                        land.setVisibility(View.VISIBLE);
//                        land.setEnabled(true);
//                        land.setAlpha(1f);
                        oneKeyTakeOff.setVisibility(View.GONE);
                        UavConstants.isHovering = true;

                    }
                    if(heartbeat.flight_mode == MAV_BASE_MODE.FLIGHT_MODE_RTL){//返航中
                        emergencyHover.setVisibility(View.VISIBLE);
                        land.setVisibility(View.GONE);
                        oneKeyTakeOff.setVisibility(View.GONE);
//                        ivReturn.setImageLevel(2);
                    }else{
//                        ivReturn.setImageLevel(1);
                    }
                    if(fly_mode == MAV_BASE_MODE.FLIGHT_MODE_LAND){// 降落中
                        emergencyHover.setVisibility(View.VISIBLE);
                        land.setVisibility(View.GONE);
//                        land.setEnabled(false);
                        UavConstants.isHovering = false;
//                        land.setAlpha(0.6f);
                        oneKeyTakeOff.setVisibility(View.GONE);
                    }
                    break;
                case EventBusCode.HEART_BEAT_SYS_STATUS:
                    msg_sys_status sys_status  = (msg_sys_status) data;
//                    LogUtil.e("HEART_BEAT_SYS_STATUS:  "+sys_status.onboard_control_sensors_present+"    "+sys_status.onboard_control_sensors_health);
//                    LogUtil.e("onboard_control_sensors_present:  "+((sys_status.onboard_control_sensors_present&0x01)==0x01)+"    "+((sys_status.onboard_control_sensors_present&0x02)==0x02)+"   "+((sys_status.onboard_control_sensors_present&0x08)==0x08)+"     "+((sys_status.onboard_control_sensors_present&0x40)==0x40)+"   "+((sys_status.onboard_control_sensors_present&0x1000000)==0x1000000)+"     "+((sys_status.onboard_control_sensors_present&0x2000000)==0x2000000));
//                    LogUtil.e("onboard_control_sensors_health:  "+((sys_status.onboard_control_sensors_health&0x01)==0x01)+"    "+((sys_status.onboard_control_sensors_health&0x02)==0x02)+"   "+((sys_status.onboard_control_sensors_health&0x08)==0x08)+"     "+((sys_status.onboard_control_sensors_health&0x40)==0x40));

                    battery_remaining =   sys_status.battery_remaining;
                    sensor_status =     getMavStatus(sys_status);
//                  LogUtil.e("sensor_status==="+sensor_status);
                    break;
                    default:

                        break;
            }
        }
    }

    int  sensor_status ;

    private int  getMavStatus(msg_sys_status sys_status) {//

     byte[]   hearlthArr =    ByteUtil.byteToBinaryByteArr(sys_status.onboard_control_sensors_health,36);
     byte[]   presentArr =    ByteUtil.byteToBinaryByteArr(sys_status.onboard_control_sensors_present,36);
//        LogUtil.e("getMavStatus1111"+ Arrays.toString(hearlthArr)+"   "+sys_status.onboard_control_sensors_health);
//        LogUtil.e("getMavStatus2222"+ Arrays.toString(presentArr)+"   "+sys_status.onboard_control_sensors_present);

           if (hearlthArr[0]==0||presentArr[0]==0){// 陀螺仪
               return  1;
           }
             if (hearlthArr[1]==0||presentArr[1]==0){// 加速度计
               return  2;
           }
             if (hearlthArr[3]==0||presentArr[3]==0){// 气压计
               return  3;
           }
             if (presentArr[6]==0){// 光流
               return  4;
           }
//            if (presentArr[13]==0){// Z轴控制，Z轴不动时为健康
//                return  10;
//            }
//
//            if (presentArr[14]==0){// XY轴控制，XY轴不动时为健康
//                return  9;
//            }

        if (hearlthArr[21]==0){// 惯导，位置速度是否收敛
               return  5;
           }
             if (hearlthArr[23]==0){ // 电池
               return  6;
           }
             if (hearlthArr[24]==0){// 陀螺仪是否校准过
               return  7;
           }
             if (hearlthArr[25]==0){// 加速度计是否校准过
               return  8;
           }

           return 0;

    }


    //飞机一键起飞事件
    public void init(){
        oneKeyTakeOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UiUtil.isFastClick()) return;
                if (HeartAliveModel.mIsAlive)
                    takeOffWindow(1);
//                showLandDialog();
            }
        });

        land.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showLandDialog();
            }
        });


        emergencyHover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCancelLand();
            }
        });
    }

    private void takeOffWindow(final int  type){
        if(takeOffDialog != null && takeOffDialog.isShowing())return;
        takeOffDialog = new TakeOffDialog(mContext,type);
        takeOffDialog.setFlyClickListener(new TakeOffDialog.OnFlyClickListener() {
            @Override
            public void onFlyLongClick() {
                if(type == 1){
                    sendTakeOff();
                }
                UiUtil.lastClickTime=System.currentTimeMillis();
            }
        });
        takeOffDialog.show();
    }

    public void sendTakeOff() {
        if (battery_remaining<10){
            WaterUtils.getmInstall().addNotify(Constants.WARM_3);
            return;
        }

        if (sys_stutus==0||sys_stutus==2){
            WaterUtils.getmInstall().addNotify(Constants.ERROR_7);
            return;
        }



           switch (sensor_status){
               case 0:
                   FlightCommand.control(0, MAV_CMD.MAV_CMD_CTR_TAKEOFF);
                   break;
               case 1:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_3);
                   break;
               case 2:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_2);
                   break;
               case 3:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_5);
                   break;
               case 4:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_4);
                   break;
               case 5:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_6);
                   break;
               case 6:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_9);
                   break;
               case 7:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_3);
                   break;
               case 8:
                   WaterUtils.getmInstall().addNotify(Constants.ERROR_2);
                   break;
              default:
                  break;

           }

    }

    public void sendLand() {
        FlightCommand.control(0, MAV_CMD.MAV_CMD_CTR_LAND_ENTER);
    }

    public void sendCancelLand(){
        FlightCommand.control(0,MAV_CMD.MAV_CMD_CTR_LAND_EXIT);
    }

    public void destroy(){
        EventBus.getDefault().unregister(this);
    }



    private void showLandDialog(){
        if(landDialog != null && landDialog.isShowing())
            return;

        landDialog = new MaterialDialogBuilderL(mContext).cancelable(false)
                .theme(Theme.LIGHT)
                .customView(R.layout.dialog_pp_tips, true).build();
        TextView tv_cancel = (TextView)  landDialog.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) landDialog.findViewById(R.id.tv_confirm);
        landDialog.show();

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landDialog.cancel();
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLand();
                landDialog.cancel();
            }
        });
    }
}
