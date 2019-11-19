package com.highgreat.education.dao;

/**
 * Created by liangzi on 2016/4/12.
 */
public class EntityUserOperation {
    public int mAirCrashTimes = 0; // 用户炸机次数：记录用户炸机的次数。
    public int mKnockedTimes = 0; // 碰撞东西次数：记录用户撞东西的次数。
    public int mFlightTimes = 0; // 起落次数：记录客户一共飞行了多少起落（飞了多少次）。
    public int mTakePhotoTimes = 0; // （拍照次数：记录客户拍照的次数。
    public int mTakeVideoTimes  = 0; // 录像次数：记录客户录像的次数。
    public int mAssistFlightTimes  = 0; // 有GPS/光流辅助的飞行次数：记录有辅助的飞行有多少次
    public int mDisAssistFlightTimes = 0; // 无辅助的飞行次数：记录无辅助的飞行有多少次。
    public int mContinuShootingTimies = 0; // 连拍功能使用次数：记录连拍功能的使用次数。
    public int mBeatBeatTimes = 0; // 拍拍起飞功能使用次数：记录拍拍起飞功能的使用次数
    public int mHoldDownTimes = 0; // 手抓降落功能使用次数：记录手抓降落功能的使用次数
    public int mVoiceControlTimes = 0; // 语音控制使用次数：记录语音控制功能的使用次数
    public int mThreeToOneTimes = 0; // 三键合一使用次数：记录三键合一功能的使用次数
    public int mSomaticTimes = 0; // 体感操作使用次数：记录体感操作模式的使用次数
    public int mDrawScreenTimes = 0; // 滑屏操作使用次数：记录滑屏操作模式的使用次数
    public int mShareTimes = 0; // 分享功能使用次数：记录分享功能的使用次数（不管通过那个平台分享的
    public int mDragTimes  = 0;//拖拽飞行功能使用次数：记录拖拽飞行功能的使用次数。
}
