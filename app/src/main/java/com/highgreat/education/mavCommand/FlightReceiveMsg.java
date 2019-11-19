package com.highgreat.education.mavCommand;


import android.os.Process;
import android.util.Log;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_autopilot_version;
import com.MAVLink.common.msg_aux_setup_ack;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_debug;
import com.MAVLink.common.msg_firmware_heads;
import com.MAVLink.common.msg_formation_ack;
import com.MAVLink.common.msg_formation_request_info;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_local_position_ned;
import com.MAVLink.common.msg_report_stats;
import com.MAVLink.common.msg_setup_ap;
import com.MAVLink.common.msg_sys_status;
import com.highgreat.education.MyApp;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.FlyData;
import com.highgreat.education.bean.HeartAliveModel;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.manager.ThreadManager;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.DroneFTPUpdateUtils;
import com.highgreat.education.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FlightReceiveMsg {

    public FlightReceiveMsg(){
        ThreadManager.getInstance().addTimerTempPool(runHeartBeat);
//        ThreadManager.getInstance().addWorkStealingPool(sendRcControlTask);
    }



    public void handleMessage(MAVLinkPacket packet){
        byte[]  MAVData = packet.encodePacket();// 消息类型
        HeartAliveModel.mReceiveTime =System.currentTimeMillis();
//        Log.i("Sven","数据类型："+MAVData[5]+"飞控数据："+ ByteUtil.bytesToHexString(MAVData));
        switch (MAVData[5]){
            case (byte) 0://心跳
                msg_heartbeat heartbeat = new msg_heartbeat(packet);

                EventBus.getDefault().post(new EventCenter(EventBusCode.HEART_BEAT_FROM_PLANE,heartbeat));
                break;
            case 1://电量信息
                msg_sys_status sys_status =new msg_sys_status(packet);

                EventBus.getDefault().post(new EventCenter(EventBusCode.HEART_BEAT_SYS_STATUS,sys_status));
                break;

            case (byte) 148:
                //LogUtil.e("flightversion","飞控版本信息");
                msg_autopilot_version autopilot_version = new msg_autopilot_version(packet);
                if(autopilot_version.autopilot_sw_version == 0){
                    return;
                }

                String flightVersion = ByteUtil.formatVersionName(autopilot_version.autopilot_sw_version);//飞控版本号
                UavConstants.CURRENT_FTP_VERSION = DroneFTPUpdateUtils.parseFloatNoExcetion(flightVersion);
                Log.i("Sven","flightVersion = "+flightVersion+" UavConstants.CURRENT_FTP_VERSION  = "+UavConstants.CURRENT_FTP_VERSION );
                EventBus.getDefault().post(new EventCenter(EventBusCode.GET_VERSION_INFO_SUCCESS,autopilot_version));
                break;
            case (byte)254://飞控版本升级
                msg_debug command_debug= new msg_debug(packet);
                short back = command_debug.ind;
                EventBus.getDefault().post(new EventCenter(EventBusCode.OS_UPGRADE_CALLBACK,back));
                break;
            case (byte)12://修改wifi回复
                msg_setup_ap msg_ap = new msg_setup_ap(packet);
                short end = msg_ap.result;
                EventBus.getDefault().post(new EventCenter(EventBusCode.WIFI_CHANGE_CALLBACK,end));
                break;
            case 32://飞行速度、高度、距离等


                msg_local_position_ned local_ned = new msg_local_position_ned(
                        packet);
//                LogUtil.e("FLY_DATA_FEEDBACK==="+local_ned.toString());
                double result = Math.pow(local_ned.vx, 2)+ Math.pow(local_ned.vy, 2) + Math.pow(local_ned.vz, 2);
                double vs = Math.sqrt((Math.pow(local_ned.vx,2)+Math.pow(local_ned.vy,2)));
                // LogUtil.i("flghtMessage","vx:"+local_ned.vx + "vy:"+local_ned.vy + "vs:"+ vs);
                double speed = Math.sqrt(result);//速度
                double sumDistance = Math.pow(local_ned.x, 2) + Math.pow(local_ned.y, 2);
                double distance= (float) Math.sqrt(sumDistance);
                double height = local_ned.z;
                // LogUtil.i("flghtMessage","speed:"+speed +  local_ned.y + "  height"+height + " distance:"+distance);
                DecimalFormat fnum = new DecimalFormat("#0.0");
                distance = Math.abs(Double.valueOf(fnum.format(Double
                        .valueOf(distance))));

                if(height>0||height<0){
                    height = -Double.valueOf(fnum.format(Double
                            .valueOf(height)));
                }else {
                    height = Double.valueOf(fnum.format(Double
                            .valueOf(height)));
                }
                //LogUtil.i("flghtMessage", "  height"+height + " distance:"+distance);
                speed = Math.abs(Double.valueOf(fnum.format(Double
                        .valueOf(speed))));
                vs = Math.abs(Double.valueOf(fnum.format(Double
                        .valueOf(vs))));
                FlyData flyData =new FlyData();
                flyData.setSpeed(speed);
                flyData.setDistance(distance);
                flyData.setHeight(height);
                flyData.setVs(vs);
                flyData.setHs(Double.valueOf(fnum.format(Double
                        .valueOf(local_ned.vz))));
                flyData.setTime(local_ned.flying_total_time);//飞行时间
                flyData.setVx(local_ned.vx);
                flyData.setVy(local_ned.vy);
//                Log.e("mav_result","heigh==="+height);
                EventBus.getDefault().post(new EventCenter(EventBusCode.FLY_DATA_FEEDBACK,flyData));
                break;
            case   76:
                msg_command_long msgCommandLong =new msg_command_long(packet);

                LogUtil.e("camamnd==="+msgCommandLong.command);
                EventBus.getDefault().post(new EventCenter(EventBusCode.COMMAND_ACK_BACK,msgCommandLong));
                break;

            default:
                break;
        }
    }


    long  count  =0;
    /**
     * 一秒发一次心跳数据
     */
    private Runnable runHeartBeat = new Runnable() {
        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
            if (count%20==0) {
                FlightCommand.getInstance().sendHeartbeat(MyApp.getAppContext());
                count=0;
                checkHeart();
            }

            FlightCommand.getInstance().command_rocker();
            count++;

        }
    };

    private void checkHeart() {
        if (System.currentTimeMillis()- HeartAliveModel.mReceiveTime<3000&&HeartAliveModel.mReceiveTime>0){
            HeartAliveModel.mIsAlive=true;
            EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_UAVNETCONNECT));
//                 LogUtil.e("mavpaser_paser","连接中");
        }else{
            EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_UDP_CONN_TIMEOUT));
            HeartAliveModel.mIsAlive=false;
//                 LogUtil.e("mavpaser_paser","未连接");
        }


    }


//    //发送摇杆指令线程一直存在
//    private Runnable sendRcControlTask = new Runnable() {
//        @Override
//        public void run() {
//            while(true){
//                Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO);
//                FlightCommand.getInstance().command_rocker();
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };


    byte [] buffer;
    public void parserMavData(byte[] data) {
        synchronized (this) {
//            HeartAliveModel.mReceiveTime =System.currentTimeMillis();
            if (buffer!=null&&buffer.length>0){//前面有数据拼接
                byte[] allBuffer =new byte[data.length+buffer.length];

                System.arraycopy(buffer,0,allBuffer,0,buffer.length);
                System.arraycopy(data,0,allBuffer,buffer.length,data.length);
                data=allBuffer;
                buffer=null;
            }
            int index=-1;
            while ( (index=findOneMav(data)) >-1){
                byte [] mav =new byte[ByteUtil.getUnsignedByte(data[index+1])+8];
                System.arraycopy(data,index,mav,0,mav.length);
                boolean isMav;
                if (  paserData(mav)){
                    isMav=true;
                }else{
                    data[index] =0;
                    isMav=false;
                }
                if (isMav) {
                    int len = data.length - (index + mav.length);
                    if (len > 0) {
                        byte[] newdata = new byte[len];
                        System.arraycopy(data, index + mav.length, newdata, 0, newdata.length);
                        data = newdata;
                    } else {
                        data = null;
                        break;
                    }
                }
            }
            if (buffer!=null&&buffer.length>5*1024) buffer=null;//防止数据一直加 虽然这个方法没用
        }
    }

    private int  findOneMav(byte [] data){
        int index =-1 ;

        if (data==null) return -1;
        for (int i = 0;i<data.length ;i++){

            if (data[i]==(byte)0xfe) {
                index=i;
                break;
            }
        }
        if (index ==-1) return -1;

        if (index>=data.length-1){

            buffer= new byte[]{(byte) 0xfe};
            return -2;//最后一位为fe 不处理}
        }

        int  len =ByteUtil.getUnsignedByte(data[index+1])+8;

        if (len+index<data.length)  return  index;
        buffer =new byte[data.length-index];
        System.arraycopy(data,index,buffer,0,buffer.length);
        return -2;
    }

    private boolean  paserData(byte [] mavdata ){
        if (mavdata==null) return false;
        Parser     parser=new Parser();
        MAVLinkPacket m=null;
        for (int i = 0; i < mavdata.length; i++) {
            m = parser.mavlink_parse_char(mavdata[i] & 0xff);
            if (m != null)
                handleMessage(m);
        }
        return  m!=null;
    }

}
