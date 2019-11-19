package com.highgreat.education.utils;

import android.content.Context;
import android.text.TextUtils;

import com.highgreat.education.BuildConfig;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.UavConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangchao on 2017/3/3.
 * hesper飞机ftp升级可以使用差量升级
 */

public class DroneFTPUpdateUtils {
    private static final String TAG = "DroneFTPUpdateUtils";
    static Context mContext;
    /**
     * 请求服务端固件信息-FTP
     */
    public static void getFTPVersionInfoFromServer(final Context context) {
    }

    /**
     * 处理服务端固件信息-FTP
     */
    private static void handleFTPResult() {
    }

    /**
     * 修改ftp升级小红点是否显示的变量
     *
     * @param context
     * @param serverFTPVersion
     */
    private static void changeFtpUpgradeRedDotControlValues(Context context, float serverFTPVersion) {
//        String ftpVersion = getLatestDobbyFTPVersion();
//        float dobbyFTPVersion = parseFloatNoExcetion(ftpVersion);

    }

    /**
     * 是否存在已下载的对应的完整FTP文件
     */
//    public static boolean hasRightFTPFile(UpgradePackageBean packageBean) {
//        File downLoadFTP = new File(getmFTPFilePath(), packageBean.data.filename);
//        //版本校验：本地存储的FTP版本号是否和服务端最新FTP版本号一致
//        LocalFtpInfoBean localFtpInfoBean = getLocalSpecifiedFTPInfoBean();
//        if (null == localFtpInfoBean) {
//            return false;
//        }
//        float locatFTPVersion = localFtpInfoBean.localFtpVersion;
//        int locatFTPType = localFtpInfoBean.localFtpType;
//        String locatFTPMd5 = localFtpInfoBean.localFtpMd5;
//        String localFTPProductName = localFtpInfoBean.localFtpProductName;
//        //本地ftp包类型和md5
//        LogUtil.e(TAG, "是否存在已下载的完整FTP文件：locatFTPVersion=" + locatFTPVersion + "|||locatFTPType=" + locatFTPType + "|||locatFTPMd5=" + locatFTPMd5);
//        try {
//            if (locatFTPVersion != parseFloatNoExcetion(packageBean.data.versionName)) {
//                LogUtil.e(TAG, "完整FTP文件-版本校验-是不是服务端最新版本-fail");
//                return false;
//            }
//            if (0 == locatFTPType) { //本地是整包
//                LogUtil.e(TAG, "完整FTP文件-整包，不需要校验飞机版本和当前升级包是否匹配");
//            } else {
////                float ftpVersion = parseFloatNoExcetion(DroneFTPUpdateUtils.getLatestDobbyFTPVersion());
////                if (packageBean.data.type == 1 && ftpVersion != packageBean.data.patchDobbyVersion) {
////                    LogUtil.e(TAG, "完整FTP文件-差量包，需要校验飞机版本和当前升级包是否匹配-fail");
//                    return false;
////                }
//            }
//            if (!(downLoadFTP.exists() && downLoadFTP.isFile())) { //文件校验
//                LogUtil.e(TAG, "完整FTP文件-文件校验-fail");
//                return false;
//            }
//            if (0 == locatFTPType) { //本地是整包
//                if (!SignUtils.checkMd5(downLoadFTP, locatFTPMd5)) { //md5校验
//                    LogUtil.e(TAG, "完整FTP文件-整包-md5校验-fail");
//                    return false;
//                }
//            } else {
//                if (!SignUtils.checkMd5(downLoadFTP, packageBean.data.md5)) { //md5校验
//                    LogUtil.e(TAG, "完整FTP文件-差量包-md5校验-fail");
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            LogUtil.e(TAG, "完整FTP文件-e:" + e.toString());
//            return false;
//        }
//        LogUtil.e(TAG, "完整FTP文件-校验-success");
//        return true;
//    }

    /**
     * FTP升级dialog提示逻辑
     *
     * @return true-升级提示框弹出 false-升级提示框未弹出
     */
    public static boolean showFTPUpgradeDialogTips(final Context context) {
        if (BuildConfig.DEBUG) {
            return false;
        }
        UpgradePackageBean upgradePackageBean = getCurrentChoosedFTPUpgradeInfoBean();
        if (null == upgradePackageBean) {
            return false;//本地没有固件升级信息
        }
        try {
//            String dobbyFTPVersion = getLatestDobbyFTPVersion();
//            float ftpVersion = parseFloatNoExcetion(dobbyFTPVersion);
//            float serverFTPVersion = Float.parseFloat(upgradePackageBean.data.versionName);
//            if (serverFTPVersion > ftpVersion && dobbyFTPVersion != UavConstants.FTP_DEFAULT_VERSION) {
//                //升级提示弹框
//                UiUtil.getFTPUpdateDialog(context, CommonUtils.getUpdateTips(upgradePackageBean.data.records));
//                //修改升级小红点是否显示
//                SharedPreferencesUtils.setParam(MyApplication.getAppContext(), UavConstants.IS_SHOW_FTP_RED_DOT, true);
//                return true; //确保只有在弹出提示框的时候才不起飞，只要没弹出就不阻止起飞
//            } else {
//                SharedPreferencesUtils.setParam(MyApplication.getAppContext(), UavConstants.IS_SHOW_FTP_RED_DOT, false);
                return false;
//            }
        } catch (Exception e) {
            LogUtil.e(TAG, "FTP升级dialog提示逻辑-e：" + e.toString());
            return false;
        }
    }

//    /**
//     * 每次获取到飞机版本号之后，重新判断小红点显示逻辑
//     */
//    public static void resetRedDotStatus(float dobbyFTPVersion) {
//        UpgradePackageBean upgradePackageBean = getLatestFTPUpgradeInfoBean();
//        int modual = upgradePackageBean.data.otypeId;
//        if (upgradePackageBean == null) {
//            return;//本地没有固件升级信息
//        }
//        float serverFTPVersion = parseFloatNoExcetion(upgradePackageBean.data.versionName);
//        if (serverFTPVersion > dobbyFTPVersion && dobbyFTPVersion != UavConstants.FTP_DEFAULT_VERSION) {
//            SharedPreferencesUtils.setParam(UiUtil.getContext(), UavConstants.IS_SHOW_FTP_RED_DOT, true);
//        } else {
//            SharedPreferencesUtils.setParam(UiUtil.getContext(), UavConstants.IS_SHOW_FTP_RED_DOT, false);
//        }
//        switch (modual){
//            case UavConstants.INTERFACE_MODULE_TAKE:
//                if (serverFTPVersion > dobbyFTPVersion && dobbyFTPVersion != UavConstants.FTP_DEFAULT_VERSION) {
//                    SharedPreferencesUtils.setParam(UiUtil.getContext(), UavConstants.IS_SHOW_FTP_RED_DOT_Take, true);
//                } else {
//                    SharedPreferencesUtils.setParam(UiUtil.getContext(), UavConstants.IS_SHOW_FTP_RED_DOT_Take, false);
//                }
//                break;
//        }
//    }

//    /**
//     * 起飞前FTP强制升级逻辑校验
//     *
//     * @return
//     */
//    public static boolean isForceUpdateFTPWhenTakeoff() {
//        if (BuildConfig.DEBUG || BuildConfig.FACTORY) {
//            return false;
//        }
//        if (UserAndFlyData.flightFTPVersion == UavConstants.FTP_DEFAULT_VERSION) {
//            return false;//读取版本号失败，先起飞吧
//        }
//        UpgradePackageBean upgradePackageBean = getCurrentChoosedFTPUpgradeInfoBean();
//        if (upgradePackageBean == null) {
//            return false;//本地没有固件升级信息，先起飞吧
//        }
//        float serverFTPVersion = parseFloatNoExcetion(upgradePackageBean.data.versionName);
//        float dobbyFTPVersion = UserAndFlyData.flightFTPVersion;
//        float serverForceVersion = parseFloatNoExcetion(upgradePackageBean.data.forceVname);
//        LogUtil.e(TAG + "-isForceUpdateRcFtpWhenTakeoff:", "serverFTPVersion:" + serverFTPVersion + "|||dobbyFTPVersion:" + dobbyFTPVersion + "|||serverForceVersion:" + serverForceVersion);
//        if (dobbyFTPVersion < serverForceVersion) {
//            return true; //当前飞机版本小于强制升级版本就需要强制升级
//        } else {
//            return false;
//        }
//    }


//    public static boolean isShowFTPUpgradeRedDot() {
//        return (boolean) SharedPreferencesUtils.getParam(UiUtil.getContext(), UavConstants.IS_SHOW_FTP_RED_DOT, false);
//    }

    /**
     * 先获取内存中飞机版本，没有再获取本地缓存中的，都没有就用默认值--保证飞机版本是最近一次连接飞机的版本
     *
     * @return
     */
//    public static String getLatestDobbyFTPVersion() {
//
//        return  (String)SharedPreferencesUtils.getParam(UiUtil.getContext(), UavConstants.DRONE_FTP_VERSION, UavConstants.FTP_DEFAULT_VERSION);
//
//    }

    public static float parseFloatNoExcetion(String str) {

        str = str.replaceAll("\\.","");
        try {
            float version = Float.parseFloat(str)/100;
            return version;
        } catch (Exception e) {
            LogUtil.e(TAG, "parseFloatNoExcetion-e:" + e.toString());
            return 0.00f;
        }
    }


    /**
     * 分目录保存-多飞机-升级文件
     *
     */
//    public static String getmFTPFilePath() {
//        return UavConstants.DATA_PATH + UavConstants.GUJIAN_SUB_PATH;
//    }

    /**
     * 保存-服务端固件信息-到本地
     */
    public static void setFTPUpgradeInfoBean(UpgradePackageBean upgradeInfoBean) {
        if (upgradeInfoBean == null) {
            return;
        }
//        if (upgradeInfoBean.data.type == 1) {//差量包时候保存对应的飞机FTP版本
//            upgradeInfoBean.data.patchDobbyVersion = parseFloatNoExcetion(getLatestDobbyFTPVersion());
//        }
//        String infoJsonStr = JSON.toJSONString(upgradeInfoBean);
//        //分别保存每个型号飞机对应的飞控信息-多飞机
//        SharedPreferencesUtils.setParam(UiUtil.getContext(), UavConstants.SERVER_FTP_VERSION_INFO, infoJsonStr);
//        if(upgradeInfoBean.data.versionName != null){
//            SharedPreferencesUtils.setParam(mContext, UavConstants.SERVER_FTP_VERSION,  upgradeInfoBean.data.versionName);
//        }

    }

    /**
     * 获取-服务端固件信息-从本地,当前用户选择的飞机
     *
     * @return
     */
    public static UpgradePackageBean getCurrentChoosedFTPUpgradeInfoBean() {
        return getSpecifiedFTPUpgradeInfoBean();
    }

    /**
     * 获取-服务端固件信息-从本地,指定飞机
     *
     * @return
     */
    public static UpgradePackageBean getSpecifiedFTPUpgradeInfoBean() {
//        //-多飞机
//        String ftpVersionInfoStr = "";
//        ftpVersionInfoStr = (String) SharedPreferencesUtils.getParam(MyApplication.getAppContext(), UavConstants.SERVER_FTP_VERSION_INFO, "");
//
//        if (TextUtils.isEmpty(ftpVersionInfoStr)) { //本地没有固件升级信息
            return null;
//        }
//        UpgradePackageBean upgradePackageBean = null;
//        try {
//            upgradePackageBean = JSON.parseObject(ftpVersionInfoStr, UpgradePackageBean.class);
//        } catch (Exception e) {
//            LogUtil.e("tag", e.getMessage());
//        }
//        return upgradePackageBean;
    }

    /**
     * 存储-已下载固件信息-到本地
     *
     * @param upgradeInfoBean
     */
    public static void storeLocalFtpInfo(UpgradePackageBean upgradeInfoBean) {
        //-多飞机
//        LocalFtpInfoBean localFtpInfoBean = new LocalFtpInfoBean();
//        localFtpInfoBean.localFtpVersion = parseFloatNoExcetion(upgradeInfoBean.data.versionName);
//        localFtpInfoBean.localFtpType = upgradeInfoBean.data.type;
//        localFtpInfoBean.localFtpMd5 = upgradeInfoBean.data.md5;
//        localFtpInfoBean.localFtpSize = upgradeInfoBean.data.filesize;
//        localFtpInfoBean.localFtpOtypeId = upgradeInfoBean.data.otypeId;
//        String infoJsonStr = JSON.toJSONString(localFtpInfoBean);
//        SharedPreferencesUtils.setParam(UiUtil.getContext(), UavConstants.LOCAL_FTP_INFORMATION, infoJsonStr);
    }

    /**
     * 获取-已下载固件信息-从本地,指定飞机
     *
     * @return
     */
//    public static LocalFtpInfoBean getLocalSpecifiedFTPInfoBean() {
//        //-多飞机
////        String ftpLocalInfoStr = (String) SharedPreferencesUtils.getParam(MyApplication.getInstance().getContext(), UavConstants.LOCAL_FTP_INFORMATION, "");
////
////        if (TextUtils.isEmpty(ftpLocalInfoStr)) { //本地没有已下载固件信息
//            return null;
////        }
////        LocalFtpInfoBean localFtpInfoBean = null;
////        try {
////            localFtpInfoBean = JSON.parseObject(ftpLocalInfoStr, LocalFtpInfoBean.class);
////        } catch (Exception e) {
////            LogUtil.e("tag", e.getMessage());
////        }
////        return localFtpInfoBean;
//    }

}

