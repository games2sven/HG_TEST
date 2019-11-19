package com.highgreat.education.mavCommand;

import android.content.Context;
import android.util.Log;

import com.MAVLink.Messages.MAVLinkPayload;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_debug;
import com.MAVLink.common.msg_extern_funtion_control;
import com.MAVLink.common.msg_file_transfer_protocol;
import com.MAVLink.common.msg_formation_control;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_manual_control;
import com.MAVLink.common.msg_param_request_read;
import com.MAVLink.common.msg_param_set;
import com.MAVLink.common.msg_setup_ap;
import com.MAVLink.enums.MAV_CMD;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.CRCUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.SpSetGetUtils;
import com.runtop.utils.CMDSendUtil;

import java.util.Arrays;

public class FlightCommand {

    private volatile float zuoYou = 0, qianHou = 0, shangXia = 0, xuanZhuan = 0;
    private static FlightCommand instance;

    public synchronized static FlightCommand getInstance(){
        if(instance == null){
            instance = new FlightCommand();
        }
        return instance;
    }

    public void setQianhou(float qianhou){
        this.qianHou = qianhou;
    }
    public void setShangxia(float shangxia){
        this.shangXia = shangxia;
    }
    public void setZuoyou(float zuoyou){
        this.zuoYou = zuoyou;
    }
    public void setXuanzhuan(float xuanZhuan){
        this.xuanZhuan = xuanZhuan;
    }

    /**
     * 心跳
     */
    public void sendHeartbeat(Context context){
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_heartbeat.MAVLINK_MSG_LENGTH);
        payLoad.putUnsignedInt(getMav_Function_switch(context));//MAV_FUNCTION_SWITCH
        payLoad.putUnsignedInt(0);//MAV_FUNCTION_SWITCH
        payLoad.putByte((byte) 6);
        payLoad.putUnsignedByte((byte) 0);
        payLoad.putUnsignedByte((byte) 0);
        payLoad.putUnsignedByte((byte) 0);
        payLoad.putUnsignedByte((byte) 0);

        msg_heartbeat heartbeat = new msg_heartbeat();
        heartbeat.unpack(payLoad);
        byte[] packet = heartbeat.pack().encodePacket();
        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
    }

    private static int getMav_Function_switch(Context context){
        int function=0;
//        if (SharedPreferencesUtils.getIntPreferences(context, PreferenceNames.BACK_TYPE,0)==1)
//            function=function| MAV_FUNCTION_SWITCH.MAV_FUNCTION_SWITCH_RTL_HEADING_TYPE;
//        if (SharedPreferencesUtils.getBooleanPreferences(context, PreferenceNames.SWITCH_HORIZONTAL_LIMIT,false))
//            function=function| MAV_FUNCTION_SWITCH.MAV_FUNCTION_SWITCH_HORIZONTAL_LIMIT;
//        if (SharedPreferencesUtils.getIntPreferences(context, PreferenceNames.SWITCH_MANUAL_TYPE,0)==1){
//            function=function| MAV_FUNCTION_SWITCH.MAV_FUNCTION_SWITCH_MANUAL_TYPE;
//        }
        return function;
    }


    //控制命令  //0,MAV_CMD.MAV_CMD_CTR_TAKEOFF
    public static void control(int confirmation,int cmd){
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_command_long.MAVLINK_MSG_LENGTH);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putUnsignedShort(cmd);
        payLoad.putByte((byte) 0x01);
        payLoad.putByte((byte) 0x01);
        payLoad.putUnsignedByte((short) confirmation);
        msg_command_long command_long = new msg_command_long();
        command_long.unpack(payLoad);
        byte[] packet = command_long.pack().encodePacket();

        LogUtil.e("CMD:起飞"+ ByteUtil.bytesToHexString(packet));
        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
    }

    /**一键降落**/
    public static void land(int confirmation,int action){
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_command_long.MAVLINK_MSG_LENGTH);
        payLoad.putFloat((float) action);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putUnsignedShort(MAV_CMD.MAV_CMD_CTR_LAND_ENTER);
        payLoad.putByte((byte) 0x01);
        payLoad.putByte((byte) 0x01);
        payLoad.putUnsignedByte((short) confirmation);
        msg_command_long command_long = new msg_command_long();
        command_long.unpack(payLoad);
        byte[] packet = command_long.pack().encodePacket();
        LogUtil.e("CMD:降落"+ ByteUtil.bytesToHexString(packet));

        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
    }


    /**
     * 虚拟摇杆控制指令
     **/
    public byte[] command_rocker() {
        float x = this.qianHou;
        float y = this.zuoYou;
        float z = this.shangXia;
        float r = this.xuanZhuan;
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_manual_control.MAVLINK_MSG_LENGTH);
        int fly_constant = 1000;
        float pitch = 0;
        float height = 0;
        float roll = 0;
        float rotation = 0;
        if(x > 0){
            pitch =  x *fly_constant;
        }else if(x < 0 ){
            pitch =  x*fly_constant;
        }else if(x == 0){
            pitch = 0;
        }
        if(y > 0){
            roll =  y*fly_constant;
            if(roll > 999){
                roll = 1000;
            }
        }else if( y < 0){
            roll = y*fly_constant;
            if(roll < -999)
                roll = -1000;
        }else
            roll = 0;
        if(z > 0){
            height = z*fly_constant;
            if (height > 900)
                height = 1000;
        }else if(z < 0){
            height = z*fly_constant;
            if(height < -900){
                height = -1000;
            }
        }else
            height = 0;
        if(r > 0){
            rotation = r*fly_constant;
            if(rotation > 900){
                rotation = 1000;
            }
        }else if(r < 0){
            rotation = r*fly_constant;
            if(rotation <  -900){
                rotation = -1000;
            }
        }else
            rotation = 0;

        float f =1;
        if (UavConstants.isFlying) {
            f =  Constants.SPEED_MODE == 1 ? 0.7f : 1f;
        }

        payLoad.putShort((short) (pitch*f));//为正向前，为负数向后
        payLoad.putShort((short) (roll*f));//为正数向右，为负数向左
        payLoad.putShort((short) (height*f));//油门，上升或下降
        payLoad.putShort((short) (rotation*f));//转向，为正时向右转，为负向左


//        LogUtil.e("pitch=="+pitch+"  roll="+roll+"   height="+height+"   rotation="+rotation);
        payLoad.putUnsignedShort((short) 0);
        payLoad.putUnsignedByte((short) 0);
        msg_manual_control command_control=new msg_manual_control();
        command_control.unpack(payLoad);
        byte[] packet = command_control.pack().encodePacket();

        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
        return packet;
    }

    /**
     * 获取版本
     */
    public static void command_get_version() {
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_command_long.MAVLINK_MSG_LENGTH);
        payLoad.putFloat((float) 1);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putFloat((float) 0);
        payLoad.putUnsignedShort(MAV_CMD.MAV_CMD_CTR_VERSION_GET);
        payLoad.putByte((byte) 0x01);
        payLoad.putByte((byte) 1);
        payLoad.putUnsignedByte((short) 1);
        msg_command_long command_long = new msg_command_long();
        command_long.unpack(payLoad);
        byte[] packet = command_long.pack().encodePacket();

        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }

    }

    /**
     * 飞控升级  0x81取消升级 1开始升级
     */
    public static void cmdUpdate(byte cmd){
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_debug.MAVLINK_MSG_LENGTH);
        payLoad.putUnsignedInt(0);
        payLoad.putFloat(0);
        payLoad.putByte(cmd);
        msg_debug debug = new msg_debug();
        debug.unpack(payLoad);
        debug.ind = 1;
        byte[] packet = debug.pack().encodePacket();

        Log.i("Sven","发送升级命令"+ByteUtil.bytesToHexString(packet));

        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
    }


    /**
     * 设置低电报警
     */
    public  void command_set_params(float params1,String paramsId) {
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_param_set.MAVLINK_MSG_LENGTH);
        payLoad.putFloat(params1);
        payLoad.putByte((byte) 0x01);
        payLoad.putByte((byte) 1);
        char[] params = paramsId.toCharArray();
        for (int i=0;i<16;i++){
            if (i<params.length){
                payLoad.putByte((byte) params[i]);
            }else{
                payLoad.putByte((byte) 0);
            }
        }
        payLoad.putByte((byte) 9);
        msg_param_set msg_param_set= new msg_param_set();
        msg_param_set.unpack(payLoad);
        byte[] packet = msg_param_set.pack().encodePacket();

        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
    }


    /**
     * 高级功能
     */
    public static void command_function_control(short cmd) {
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_extern_funtion_control.MAVLINK_MSG_LENGTH);
        payLoad.putFloat(0);
        payLoad.putFloat(0);
        payLoad.putFloat(0);
        payLoad.putFloat(0);
        payLoad.putUnsignedByte((short) 0);
        payLoad.putUnsignedByte(cmd);
        payLoad.putUnsignedByte((short)1);
        msg_extern_funtion_control msg_function_control= new msg_extern_funtion_control();
        msg_function_control.unpack(payLoad);
        byte[] packet = msg_function_control.pack().encodePacket();

        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] datas = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                datas[i] = packet[i];
            }
            CMDSendUtil.send(datas);
        }
    }

    public static void command_send_file(short state,short packet_num, byte[] datas) {

        MAVLinkPayload payLoad = new MAVLinkPayload(msg_file_transfer_protocol.MAVLINK_MSG_LENGTH);
        payLoad.putUnsignedByte(state);
        payLoad.putUnsignedByte((byte)0);
        payLoad.putUnsignedByte(packet_num);
        for(int i=0;i<datas.length;i++){
            payLoad.putByte(datas[i]);
        }
        msg_file_transfer_protocol msg_file_transfer = new msg_file_transfer_protocol();
        msg_file_transfer.unpack(payLoad);
        byte[] packet = msg_file_transfer.pack().encodePacket();
        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] da = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                da[i] = packet[i];
            }
            CMDSendUtil.send(da);
        }
    }

    /**
     * 修改wifi
     */
     public static void changeWifi(byte[] ssid,byte[] pwd){
         MAVLinkPayload payLoad = new MAVLinkPayload(msg_setup_ap.MAVLINK_MSG_LENGTH);
         payLoad.putUnsignedByte((byte)0x03);

         byte[] wifissid = new byte[20];
         System.arraycopy(ssid,0,wifissid,0,ssid.length);
         byte[] wifipwd = new byte[20];
         System.arraycopy(pwd,0,wifipwd,0,pwd.length);

         for(int i=0;i<wifissid.length;i++){
             payLoad.putByte(wifissid[i]);
         }
         for(int i=0;i<wifipwd.length;i++){
             payLoad.putByte(wifipwd[i]);
         }
         payLoad.putUnsignedByte((short)1);
         msg_setup_ap msg_ap = new msg_setup_ap();
         msg_ap.unpack(payLoad);
         byte[] packet = msg_ap.pack().encodePacket();
         if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
             TcpClient.getmInstance().send(packet);
         }else{
             int [] da = new int [packet.length];
             for(int i =0;i<packet.length;i++){
                 da[i] = packet[i];
             }
             CMDSendUtil.send(da);
         }
     }



    /**获取参数*/
    public  void command_get_params(String paramsId){
        MAVLinkPayload payLoad = new MAVLinkPayload(msg_command_long.MAVLINK_MSG_LENGTH);
        payLoad.putShort((short) -1);
        payLoad.putByte((byte) 1);
        payLoad.putByte((byte) 1);
        char[] params =paramsId.toCharArray();
        for (int i=0;i<16;i++){
            if (i<params.length){
                payLoad.putByte((byte) params[i]);
            }else{
                payLoad.putByte((byte) 0);
            }
        }
        msg_param_request_read request_read = new msg_param_request_read();
        request_read.unpack(payLoad);
        byte[] packet  =request_read.pack().encodePacket();
        if(UavConstants.CURRENT_FLIGHT_CONNECTED){//直接连飞机
            TcpClient.getmInstance().send(packet);
        }else{
            int [] da = new int [packet.length];
            for(int i =0;i<packet.length;i++){
                da[i] = packet[i];
            }
            CMDSendUtil.send(da);
        }
    }

}
