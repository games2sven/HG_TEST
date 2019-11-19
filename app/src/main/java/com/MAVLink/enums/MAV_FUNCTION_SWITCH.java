package com.MAVLink.enums;

/**
 * Created by Administrator on 2017/4/8 0008.
 */

public class MAV_FUNCTION_SWITCH {
    public final static int   MAV_FUNCTION_SWITCH_MANUAL_TYPE=1; // 美国手或者日本手 ，0-美国手 | 1-日本手
    public final static int   MAV_FUNCTION_SWITCH_HORIZONTAL_LIMIT=4;// 距离限制，0-关闭 | 1-开启
    public final static int   MAV_FUNCTION_SWITCH_NEW_PLAYER=8;// 新手模式，0-关闭 | 1-开启
    public final static int  MAV_FUNCTION_SWITCH_RTL_HEADING_TYPE=16;// 返航方式，0-对头 | 1-对尾
}
