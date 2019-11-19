package com.highgreat.education.bean;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/2/26 12:23
 * 修改人：mac-likh
 * 修改时间：16/2/26 12:23
 * 修改备注：
 */
public class HeartAliveModel {
    public static long mSendTime;
    public static long mReceiveTime;
    public static long currentUavTime;
    public static String lastUavTime = "";
    public static boolean mIsAlive;

    public static volatile long receiveTimeRC;
}
