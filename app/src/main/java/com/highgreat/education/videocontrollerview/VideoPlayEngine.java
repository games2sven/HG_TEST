package com.highgreat.education.videocontrollerview;

import android.text.TextUtils;


import com.highgreat.education.R;
import com.highgreat.education.utils.UiUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * @FileName：Camera1.5
 * @description：
 * @author：mac-likh
 * @date：2017/1/18 14:12
 */

public class VideoPlayEngine  {

    private String           videoUrl;
    private boolean          isEngineActive;
    private boolean          isPlaying;
    private String           mPid;
    private IVideoPlayEngine mIVideoPlayEngine;

    public boolean isEngineActive() {
        return isEngineActive;
    }

    public void setmPid(String mPid) {
        this.mPid = mPid;
    }

    public void testPlay() {
        videoUrl = "http://video.gedoushijie.com/XvLp9nkAirHvoVf0lEKdvlIdaQI=/lqH7C1Unckgfwj4WuvtM15DMItEA";
        initMediaDataSource();
    }


    //初始化播放器
    private void initMediaDataSource() {
        try {
            isEngineActive = true;
//            surfaceView.setOnPreparedListener(this);
//            surfaceView.setOnCompletionListener(this);
//            surfaceView.setOnBufferingUpdateListener(this);
//            surfaceView.setUrl(videoUrl);
//            surfaceView.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setmIVideoPlayEngine(IVideoPlayEngine mIVideoPlayEngine) {
        this.mIVideoPlayEngine = mIVideoPlayEngine;
    }


    public void destroyEngine() {

        isPlaying = false;
        isEngineActive = false;

    }

//    @Override
//    public void onPrepared(IDTLPlayer idtlPlayer) {
//        if (mIVideoPlayEngine != null) {
//            mIVideoPlayEngine.onPreparedEvent();
//        }
//        surfaceView.start();
//    }
//
//    @Override
//    public void onCompletion(IDTLPlayer idtlPlayer) {
//        if (mIVideoPlayEngine != null) {
//            mIVideoPlayEngine.onCompletionEvent();
//        }
//    }
//
//    @Override
//    public void onBufferingUpdate(IDTLPlayer idtlPlayer, int i) {
//        if (idtlPlayer.isPlaying()) {
//            if (!isPlaying) {
//                isPlaying = true;
//                if (mIVideoPlayEngine != null) {
//                    mIVideoPlayEngine.hideNetImage();
//                }
//            }
//        }
//    }

    public interface IVideoPlayEngine {
        void hideNetImage();

        void onPreparedEvent();

        void onCompletionEvent();

    }
}
