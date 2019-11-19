package com.highgreat.education.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.EditText;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by chengbin on 2016/3/26.
 * 常用的工具，方便调用
 */
public class CommonUtils {
    private static final String TAG = "CommonUtils";
    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            LogUtil.e("exception",e.toString());
//            e.printStackTrace();
        }

        return pi;
    }


    /**
     * 获取当前系统的语言环境
     *
     * @return
     */
    public static boolean isLanguageZH() {
        Locale locale = UiUtil.getResource().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }

    public static String getApkUpdateTime2(Context context) {
        ZipFile zf = null;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), 0);
            zf = new ZipFile(ai.sourceDir);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String date = sdf.format(new Date(ze.getTime()));
            return date;
        } catch (Exception e) {
        } finally {
            if (zf != null) {
                try {
                    zf.close();
                } catch (IOException e) {
//                    e.printStackTrace();
                    LogUtil.e("exception",e.getMessage());
                }
            }
        }
        return "0";
    }

    /**
     * @return true-表示连接外网
     */
    public static boolean isConnInternetNoToast() {
        if (Wifi4GUtils.isSupportCellularNetwork()) {
           // LogUtil.e(TAG,"是否有网："+Wifi4GUtils.isSupportCellularNetwork());
            return NetUtil.isNetworkConnected(UiUtil.getContext());
        } else {
          //  LogUtil.e(TAG,"是否有网："+NetUtils.isNetworkConnected(UiUtil.getContext()) + !UavConstants.IS_UAV_CONN);
            //&& ! Constants.IS_UAV_CONN
            return NetUtil.isNetworkConnected(UiUtil.getContext());
        }
    }

    public static String getCurrentTime(long date) {
        String var1 = (new SimpleDateFormat("yyyy/MM/dd/")).format(date);
        String var0 = (new SimpleDateFormat("HH/mm/ss")).format(date);
        return var1 + var0;
    }

    public static double decimalRetain(double decimal, int num) {
        BigDecimal bd = new BigDecimal(decimal);
        return bd.setScale(num, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static void ForceUpdateIntent(Context context, Class clazz, String intentKey, String intentValue) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(intentKey, intentValue);
        context.startActivity(intent);
    }

    public static double uAVHeight(int locationMode, int height) {
        if (locationMode == 1) {
            return CommonUtils.decimalRetain(height / 100.0, 1);//超声波定位，单位cm
        } else {
            return CommonUtils.decimalRetain(height / 10.0, 1);//默认气压定位 单位dm
        }
    }

    public static double uAVHeight(int height) {
        if (height >= 20000) {
            return CommonUtils.decimalRetain((height - 20000) / 100.0, 1);//超声波定位，单位cm
        } else {
            return CommonUtils.decimalRetain(height / 10.0, 1);//默认气压定位 单位dm
        }
    }

    public static EditText eTMaxLen(EditText et, int length) {
        InputFilter[] filters = {new InputFilter.LengthFilter(length)};
        et.setFilters(filters);
        return et;
    }


    //递归删除文件及文件夹
    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    public static String getAppUpdatecontent(String records) {
        if(TextUtils.isEmpty(records)){
            return "";
        }
        String[] str = records.split("@@");
        String convertTips = "";
        for (String aStr : str) {
            convertTips += aStr + "\r\n";
        }
        return convertTips;
    }

    public static void makeDir(File file) {
        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
        }
    }

    public static String getUpdateTips(String tips) {
        return tips.replace("@@", "\r\n");
    }

    public static void gotoInstallApk(Context context, File apkFile) {
        Intent install = new Intent();
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setAction(Intent.ACTION_VIEW);
        install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(install);
        //LogEvent.onEvent(LogEvent.innerUpgrade);//统计app版本是通过app内部升级的方式从低版本升级到高版本的数量
    }

    /**
     * 重置引导参数
     *
     * @param
     */
//    public static void resetGudieSpValue(Context context) {
//        SPUtil.remove(context, UavConstants.IS_TAKE_FIRST_PAGE_START);
//        SPUtil.remove(context, UavConstants.IS_HESPER_FIRST_PAGE_START);
//        SPUtil.remove(context, UavConstants.ISFIRSTUSEVOICE);
//        SPUtil.remove(context, UavConstants.IS_FIRST_USE_TOUCH_FLY);
//        SPUtil.remove(context, UavConstants.IS_FIRST_USE_SENSOR_FLY);
//        SPUtil.remove(context, UavConstants.IS_FIRST_USE_ROCKER_USA);
//        SPUtil.remove(context, UavConstants.IS_FIRST_USE_ROCKER_JP);
//        SPUtil.remove(context, UavConstants.IS_FIRST_USE_SAFE_ROCKER_USA);
//        SPUtil.remove(context, UavConstants.IS_FIRST_USE_SAFE_ROCKER_Jp);
//        UavConstants.PHOTOS_DELAY_TIME = 0;
//    }


    //得到手机和APP信息的字节
//    public static byte[] getPhoneAndAppInfoBytes() {
//
//        String model = UserDataConstans.PHONE_MODEL;
//        byte[] PHONE_MODEL = str2bytes(model,30);
//        LogUtil.e(TAG, "PHONE_MODEL= " + Arrays.toString(PHONE_MODEL));
//
//        String os = UserDataConstans.PHONE_OS_VERSION;
//        byte[] PHONE_OS_VERSION = str2bytes(os,20);
//        LogUtil.e(TAG, "PHONE_OS_VERSION= " + Arrays.toString(PHONE_OS_VERSION));
//
//        String app = getVersionName(UiUtil.getContext());
//        byte[] appCurrentVersionName = str2bytes(app,20);
//        LogUtil.e(TAG, "appCurrentVersionName= " + Arrays.toString(appCurrentVersionName));
//
//        String nickname= (String) SharedPreferencesUtils.getParam(UiUtil.getContext(), UserUtils.KEY_NICKNAME,"");
//        byte[] nickname2 = str2bytes(nickname,60);
//        LogUtil.e(TAG, "nickname2= " + Arrays.toString(nickname2));
//
//        byte[] info = new byte[130];
//        for (int i= 0;i<30;i++){
//            info[i] = PHONE_MODEL[i];
//        }
//
//        for (int i= 0;i<20;i++){
//            info[i+30] = PHONE_OS_VERSION[i];
//        }
//
//        for (int i= 0;i<20;i++){
//            info[i+50] = appCurrentVersionName[i];
//        }
//
//        for (int i= 0;i<60;i++){
//            info[i+70] = nickname2[i];
//        }
//
//        LogUtil.e(TAG, "all= " + Arrays.toString(info));
//        return info;
//    }

    //得到用户账户的字节
//    public static byte[] getUserAccoutBytes(){
//        byte[] UserId = new byte[500];
//        String id = (String) SharedPreferencesUtils.getParam(UiUtil.getContext(), UavConstants.USER_ID, "");
//        byte[] userIdByte = str2bytes(id,250);
//        LogUtil.e(TAG, "all2= " + Arrays.toString(userIdByte));
//
//        for (int i= 0;i<250;i++){
//            UserId[i] = 0;
//        }
//        for (int i= 0;i<250;i++){
//            UserId[i+250] = userIdByte[i];
//        }
//        LogUtil.e(TAG, "all2= " + Arrays.toString(UserId));
//        return UserId;
//    }

    private static byte[] str2bytes(String str, int length) {

        byte[] byteTemp;
        byte[] byteFinal = new byte[length];

        try {
            byteTemp = str.getBytes("utf-8");
            if (byteTemp.length < length) {
                for (int j = 0; j < byteTemp.length; j++) {
                    byteFinal[j] = byteTemp[j];
                }
                for (int j = byteTemp.length; j < length; j++) {
                    byteFinal[j] = 0;
                }
            }else if (byteTemp.length > length){
                for (int j = 0; j < length; j++) {
                    byteFinal[j] = byteTemp[j];
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return byteFinal;
    }


//    public static  boolean isShowFTPUpgradeRedDot(ImageView ivFtpDot){
//        //下载升级包完毕，存储的版本号。
//        String firmWareVers = (String) SharedPreferencesUtils.getParam(UiUtil.getContext(),
//                UavConstants.LOCAL_FIRMWARE_VERSION, "1.00");
//        if (SpSetGetUtils.getServerFTPVersion() > Float.parseFloat(firmWareVers)){
//            ivFtpDot.setVisibility(View.VISIBLE);
//            return true;
//        }else {
//            ivFtpDot.setVisibility(View.GONE);
//            return false;
//        }
//    }

//    public static  boolean isShowFTPUpgradeRedDot(){
//        //下载升级包完毕，存储的版本号。
//        String firmWareVers = (String) SharedPreferencesUtils.getParam(UiUtil.getContext(),
//                UavConstants.LOCAL_FIRMWARE_VERSION, "1.00");
//        return SpSetGetUtils.getServerFTPVersion() > Float.parseFloat(firmWareVers);
//    }


    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();
        /**
         * 获取Android设备中所有正在运行的App
         */
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static double getLength(String s){
        double valueLength =0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1    
        for(int i = 0; i < s.length();i++){
            // 获取一个字符    
            String temp = s.substring(i,i+1);
            // 判断是否为中文字符    
            if(temp.matches(chinese)){
                // 中文字符长度为1    
                valueLength+=1;
            }else{
                // 其他字符长度为0.5    
                valueLength+=0.5;
            }
        }
        //进位取整    
        return Math.ceil(valueLength);
    }

    /**
     * 检验手机号是否有效
     */
    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)||mobiles.trim().length()!=11) return false;
        for (Character c:mobiles.toCharArray()){
            if (c>57||c<48) return false;
        }

        String regex = "^((13[0-9])|(14[5|7])|(15([0-9]))|(17[0-9])|(18[0-9]))\\d{8}$";
        Pattern p = Pattern
                .compile(regex);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 邮箱验证
     *
     * @param target
     * @return
     */
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    public static void installApk(Context context,File apkFile) {
       /* Intent installApkIntent = new Intent();
        installApkIntent.setAction(Intent.ACTION_VIEW);
        installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
        installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installApkIntent.setDataAndType(Uri.fromFile(apkFile), MIME_TYPE_APK);
        if (sApp.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
            sApp.startActivity(installApkIntent);
        }*/
        //Toast.makeText(sApp,apkFile.getPath(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri contentUri = FileProvider.getUriForFile(context, "com.highgreat.education.demo.fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }


        if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            context.startActivity(intent);
        }
    }


    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{8,20}$";
    public static final String REGEX_WIFINAME = "^[a-zA-Z0-9]{0,20}$";

    public static boolean isPassword(Context content, String pwd) {
        return pwd.matches(REGEX_PASSWORD);
    }

    //wifi名称只支持大小写英文字母和数字
    public static boolean isWifiName(Context content, String wifiName) {
        return wifiName.matches(REGEX_WIFINAME);
    }

}
