package com.highgreat.education.utils;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.highgreat.education.MyApp;
import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.HeartAliveModel;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.PreferenceNames;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dialog.AppUpdateDialog;
import com.highgreat.education.dialog.CheckingDialog;
import com.highgreat.education.dialog.FirmwareUpgradeDialog;
import com.highgreat.education.net.BaseRequest;
//import com.highgreat.education.net.RestClientWithSSID;
import com.highgreat.education.net.RxJavaRetrofitNetRequestUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public class FirmWareUpdateUtils {

    private static  Context mContext;

    private ProgressBar mAppDownload;
    private  AppUpdateDialog updateDialog;
    private int mMaxProgress = 100;//百分比
    private int mProgress = 0;

    public static final int PUSH_UPDATE = 1; //每次联网自动检查升级信息
    public static final int MANUAL_UPDATE =2; //手动更新

    private int mode = 0;
//    File[] oldFiles;

    private CheckingDialog osChecingDilaog;

    public static  FirmWareUpdateUtils instanse;
    Handler handler = new Handler();

    byte[] payload = new byte[128];

    private FirmWareUpdateUtils(){};

    public static FirmWareUpdateUtils getInstance(){
        synchronized (FirmWareUpdateUtils.class){
            if(instanse == null){
                instanse = new FirmWareUpdateUtils();
            }
        }
        return instanse;
    }

    public void osUpdate(Context context,int mode){
        mContext = context;
        this.mode = mode;

        if(HeartAliveModel.mIsAlive){//连接着飞机
            //与服务器上面下载到本地的固件去比对
            String ftpVersionInfoStr = SharedPreferencesUtils.getStringPreferences(MyApp.getAppContext(), PreferenceNames.FIRMWARE_VERSION_LOCAL, "0");
            //当前飞机的版本
            String flightVersion = (String)SharedPreferencesUtils.getParam(MyApp.getAppContext(), PreferenceNames.DRONE_FTP_VERSION, "");
            if (!ftpVersionInfoStr.equals(flightVersion) && UiUtil.compare(flightVersion, ftpVersionInfoStr)) {
                //满足升级固件的条件，弹出升级对话框
                FirmwareUpgradeDialog mFirmwareDialog = new FirmwareUpgradeDialog();
                mFirmwareDialog.showDialog(mContext);
            }
        }else{
            if(CommonUtils.isConnInternetNoToast()){
                //获取服务器固件版本
                if(mode == MANUAL_UPDATE){
                    osChecingDilaog = new CheckingDialog(context);
                    osChecingDilaog.showDialog(context.getString(R.string.app_checking));
                }
                getOSVersionInfoFromServer(context);
            }else{
                UiUtil.showToast(UiUtil.getContext().getString(R.string.net_unconn));
            }
        }
    }


    private  void getOSVersionInfoFromServer(final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("upobject", 2 + ""); //升级对象(0-android 1-IOS 2-固件 3-系统 4-遥控器)
        final int version = (int) SharedPreferencesUtils.getParam(context, PreferenceNames.FIRMWARE_VERSION_CURRENT,0);//当前飞机版本
        paramsMap.put("vname",version +""); //当前dobby版本，即最近一次连接的飞机的固件版本

        BaseRequest baseRequest =  new RxJavaRetrofitNetRequestUtils();
        baseRequest.getFirmwareInfo(paramsMap, new Subscriber<UpgradePackageBean>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if(mode == MANUAL_UPDATE){
                    UiUtil.showToast(UiUtil.getContext().getString(R.string.net_unconn));
                }

                if(osChecingDilaog != null ){
                    osChecingDilaog.dismiss();
                    osChecingDilaog = null;
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onNext(UpgradePackageBean response) {
                Log.i("Sven","response" +response.toString());
                if(response != null) {
                    String infoJsonStr = new Gson().toJson(response);
                    LogUtil.e("firmwareInfo", "infoJsonStr=" + infoJsonStr + "current version:"+version);
                    LogUtil.e("current version", "current version=" + version);
                    SharedPreferencesUtils.setParam(context, PreferenceNames.FIRMWARE_VERSION_INFO, infoJsonStr);
                    SharedPreferencesUtils.setPreferences(context, PreferenceNames.FIRMWARE_VERSION_SERVICE, response.data.versionCode);
//                    SharedPreferencesUtils.setPreferences(context, PreferenceNames.FIRMWARE_FILE_NAME_BALL, response.data.filename);
                    LogUtil.e("current version", "CODE_NEW_FIRMWARE");
//                    EventBus.getDefault().post(new EventCenter(EventBusCode.GET_SERVER_OS, response.data));
                    hanServerVersion(response.data);
                }
            }
        });
    }


    UpgradePackageBean.VersionInfo info;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void hanServerVersion(UpgradePackageBean.VersionInfo info) {

        if(osChecingDilaog != null ){
            osChecingDilaog.dismiss();
            osChecingDilaog = null;
        }

        this.info  = info;

        if (info == null) {
            return;
        }
        String filename = info.filename;

        File file1 = new File(Constants.FIRMWARE_PATH, filename);
        if (file1.exists() && file1.length()!=0) {
            //本地文件存在
            String current_version = (String) SharedPreferencesUtils.getStringPreferences(mContext, PreferenceNames.FIRMWARE_VERSION_LOCAL, "0");
            Log.i("Sven","current_version = "+current_version+" info.versionName "+info.versionName);
            if(!info.versionName.equals(current_version) && UiUtil.compare(current_version,info.versionName)){
                file1.delete();//删除本地文件，下载服务器最新文件
                toDownLoad();
            }else{//不需要更新
                //提示当前已经是最新版本
                if(mode == MANUAL_UPDATE){
                    osChecingDilaog = new CheckingDialog(mContext);
                    osChecingDilaog.showDialog(mContext.getString(R.string.is_lastest_app_version));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            osChecingDilaog.dismiss();
                            osChecingDilaog = null;
                        }
                    },2000);
                }
            }
        } else {
            //本地不存在文件
            if (file1.exists())
                file1.delete();
            //去下载
            toDownLoad();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void toDownLoad(){
        updateDialog = new AppUpdateDialog(new AppUpdateDialog.IDialogClickListner() {
            @Override
            public void leftClick() {

            }

            @Override
            public void rightClick(ProgressBar progressBar) {
                mAppDownload = progressBar;
//                if (NetUtils.getCurrentNetType(context) == 2) {
//                    useMobileTips(context);
//                }
                if (!new File(Constants.FIRMWARE_PATH).exists()) {
                    new File(Constants.FIRMWARE_PATH).mkdirs();
                }
                getFile(info.storeurl, Constants.FIRMWARE_PATH, info.filename);
//                oldFiles = new File(Constants.FIRMWARE_PATH).listFiles();
            }
        },2,info);
        updateDialog.showDialog(mContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getMB(long size){
        if(size > 1024*1024){
            if(size % (1024*1024) == 0){
                long file_size = size/1024/1024;
                return file_size + "MB";
            }else {
                float file_zise = (float) (size * 1.0 / 1024 / 1024);
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String result = decimalFormat.format(file_zise) + "MB";
                return result;
            }
        }else
            return size/1024+"KB";
    }

    /**
     * 下载apk或patch包
     *
     * @param url
     * @param dir
     * @param mFileName
     */
    private RxJavaRetrofitNetRequestUtils baseRequest;
    private void getFile(String url, String dir, String mFileName) {

        File file = new File(dir+mFileName);
        if (file.exists()) file.delete();
        LogUtil.e("getFileaa","start get file");
        baseRequest = new RxJavaRetrofitNetRequestUtils(new RxJavaRetrofitNetRequestUtils.JsDownloadListener() {
            @Override
            public void onStartDownload() {
            }

            @Override
            public void onProgress(int progress) {
//                mProgress =  progress;
//                LogUtil.i("mProgress","progress="+progress);
//                if (mProgress <= mMaxProgress) {
//                    Message msg =myHandler.obtainMessage();
//                    msg.what = 100;
//                    msg.obj = mProgress;
//                    myHandler.sendMessage(msg);
//                }
                EventBus.getDefault().post(new EventCenter(EventBusCode.DOWNLOAD_OS_PROGRESS,progress));
//                downloadProgress = progress;
            }

            @Override
            public void onFinishDownload() {
            }

            @Override
            public void onFail(String errorInfo) {
            }
        });
        baseRequest.download(url, dir + mFileName, new Subscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object o) {
                onCompleted();
            }
        });
    }


    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    int progress = (int) msg.obj;
                    mAppDownload.setProgress(progress);
                    if(progress == 100) {
                    }
                    break;
            }
        }
    };


}
