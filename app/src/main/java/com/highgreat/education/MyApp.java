/**
 * create  by 杨青远 on
 */
package com.highgreat.education;

import android.content.Context;
import android.os.Environment;

import com.UFOApp;
import com.dash.Const;
import com.highgreat.education.utils.ActivityLifecycle;
import com.highgreat.education.utils.TypefaceUtil;
import com.tencent.bugly.crashreport.CrashReport;


/**
 * @author 杨青远
 * @Create 2017/12/26
 * @Describe
 */
public class MyApp extends UFOApp {
    private static MyApp instance;
    public static synchronized MyApp getInstance() {
        return instance;
    }
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        this.registerActivityLifecycleCallbacks(new ActivityLifecycle());
        //字体设置
        TypefaceUtil.replaceSystemDefaultFont(this,"font/pingfanglight.ttf");
        //bugly
         CrashReport.initCrashReport(getApplicationContext());

      //  Const.setFileFolder("highgreat","folkVideo","folkPhoto");
//        Const.FILE_FOLDER= Environment.getExternalStorageDirectory() + "/highgreat";
    }

    public static Context getAppContext(){
        return context;
    }
    public static Context getLanguageContext() {
        Context context = getAppContext();
        return context;
    }
}
