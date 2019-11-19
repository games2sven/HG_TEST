package com.highgreat.education.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.mavCommand.FlightCommand;

import org.greenrobot.eventbus.EventBus;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private int mFinalCount;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        //说明从后台回到了前台
        mFinalCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        mFinalCount--;
        //说明从前台回到了后台
        if (mFinalCount == 0 ) {
            EventBus.getDefault().post(new EventCenter(EventBusCode.APP_INTO_BACKGROUNG));
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
