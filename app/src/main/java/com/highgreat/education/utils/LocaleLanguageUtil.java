package com.highgreat.education.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Administrator on 2017/4/17 0017.
 */


public class LocaleLanguageUtil {
    private static LocaleLanguageUtil languageUtil;

    public static LocaleLanguageUtil getStance() {
        if (languageUtil == null) {
            languageUtil = new LocaleLanguageUtil();
        }

        return languageUtil;
    }


    public ContextWrapper changeLanguage(Context ctx) {
        if (ctx == null) {
            return null;
        }

        Resources rs = ctx.getResources();
        Configuration config = rs.getConfiguration();

        Locale sysLocale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sysLocale = config.getLocales().get(0);
        } else {
            sysLocale = config.locale;
        }

        //应用内配置语言
        Resources resources = ctx.getResources();//获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        String country = sysLocale.getCountry();
//        Log.e("language", "language==" +sysLocale.getLanguage() + " -" + country);
        int  language =getLanguage(ctx);
        if (language==0){
            if (chinaArea(sysLocale.getLanguage())) {
                if("TW".equals(country)||"HK".equals(country)){

                    sysLocale =Locale.TAIWAN;
                }else{
                    sysLocale = Locale.SIMPLIFIED_CHINESE;
                }
            } else {
                if ("ko".equals(config.locale.getLanguage())){
                    sysLocale = Locale.KOREA;
                }else{
                    sysLocale = Locale.US;
                }
            }
        }else if(language==1){
            sysLocale = Locale.SIMPLIFIED_CHINESE;

        }else if(language ==2){
            sysLocale = Locale.TAIWAN;
        }else if (language==4){
            sysLocale = Locale.KOREA;
        }else {
            sysLocale = Locale.US;
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(sysLocale);
        } else {
            config.locale = sysLocale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ctx = ctx.createConfigurationContext(config);
        } else {
            ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
        }
//        ContextWrapper contextWrapper =new ContextWrapper(ctx);
//        Context context =   contextWrapper.createConfigurationContext(config);
//        resources.updateConfiguration(context);
      return   new ContextWrapper(ctx);
    }

    private boolean chinaArea( String language) {

        return  Locale.SIMPLIFIED_CHINESE.getLanguage().equals(language);
    }
//
//    public boolean isChinese() {
//        return Locale.SIMPLIFIED_CHINESE.getLanguage().equals("zh");
//    }

    public  String getCountry(){
        int code =    getLanguageCode();
        if(code==1){
            return  "cn";
        }else if(code ==5){
            return "tw";
        }else if(code ==4){
            return  "kr";
        }else {
            return "en";
        }

    }

    private static   String SETTING_LANGUAGE="setting_language";
    public void setLanguage(Context context ,int language) {
        SharedPreferencesUtils.setParam(context,SETTING_LANGUAGE,language);
        changeLanguage(context);
    }

    public int getLanguage(Context context) {
        return (int) SharedPreferencesUtils.getParam(context,SETTING_LANGUAGE,0);
    }

    /**
     * 中文  cn     1
     英文  en     2
     日本  jp     3
     韩国  kr     4
     台湾  tw     5
     香港  hk     6
     法国  fr     7
     德国  gr     8
     越南  vn     9
     * @return
     */
    public int getLanguageCode() {
        Resources resources = UiUtil.getContext().getResources();//获得res资源对象


        if (getLanguage(UiUtil.getContext()) == 0) {
            //获取 Locale 对象的正确姿势：
            Locale locale;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = resources.getConfiguration().getLocales().get(0);
            } else {
                locale = resources.getConfiguration().locale;
            }
            //获取语言的正确姿势：
            String country = locale.getCountry();
            String lang = locale.getLanguage();
            Log.e("language", "language==" + lang + " -" + country);
            if (chinaArea(lang)) {
                if ("CN".equals(country)) {
                    return 1;
                } else {
                    return 5;
                }

            } else {
                if ("ko".equals(lang)) return 4;

                return 2;


            }

        } else {
            if(getLanguage(UiUtil.getContext())==1) return 1;
            if(getLanguage(UiUtil.getContext())==2) return 5;
            if(getLanguage(UiUtil.getContext())==3) return 2;
            return 4;

        }

    }
}
