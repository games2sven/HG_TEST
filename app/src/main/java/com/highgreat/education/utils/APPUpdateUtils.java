package com.highgreat.education.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.highgreat.education.R;
import com.highgreat.education.bean.APPBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.ServerConstants;
import com.highgreat.education.dialog.AppUpdateDialog;
import com.highgreat.education.dialog.CheckingDialog;
import com.highgreat.education.dialog.MaterialDialogBuilderL;
import com.highgreat.education.net.BaseRequest;
import com.highgreat.education.net.RxJavaRetrofitNetRequestUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.NumberFormat;

import rx.Subscriber;

/**
 * Created by chengbin on 2016/8/11.
 * 推送更新和手动更新
 * fixed by yc 2016-12-2:增加差量升级逻辑,增加已下载包不重复下载逻辑
 */
public class APPUpdateUtils {

  private APPBean.VersionInfo mVersionInfo; //版本信息
  private int    appCurrentVersionCode;
  private String filePath; //apk或patch下载存放目录
  private String newApkPath; //差量包和当前apk合成的整包apk存放目录
  String newApkName = "";//合成新pak名称, 规则：Do.Fun_+服务端下发filename_+filename的md5值+".apk"
  public static final int PUSH_UPDATE = 1; //每次联网自动检查升级信息
  public static final int MANUAL_UPDATE =2; //手动更新
  private  float downloadProgress  = 0;
  private MaterialDialog checkingDialog; //检测版本信息请求的加载框
  private CheckingDialog mCheckingDialog; //检测版本信息请求的加载框

  private  static final String  DOWNLOAD_TAG  = "AppDownloadTag";

  private static APPUpdateUtils mInstance;
  public static APPUpdateUtils getInstantce(){
    if(mInstance == null){
      mInstance = new APPUpdateUtils();
    }
    return mInstance;
  }

  /**
   * 升级逻辑入口，供外部调用
   * @param context
   * @param mode
     */
  public void appUpdate(Context context,int mode){

    init(context);
    if (CommonUtils.isConnInternetNoToast()){
      getHttpVersion(mode,context);
    }else{
      if (mode == MANUAL_UPDATE)  UiUtil.showToast(UiUtil.getContext().getString(R.string.net_unconn));
    }
  }

  private void init( Context context){
    appCurrentVersionCode = CommonUtils.getVersionCode(context);
    filePath = Constants.DATA_PATH + "version/app/";
    newApkPath = Constants.DATA_PATH + "version/patchApp/";
    File file = new File(filePath);
    if (!file.exists() || !file.isDirectory()){
      file.mkdirs();
    }
    File newFile = new File(newApkPath);
    if (!newFile.exists() || !newFile.isDirectory()){
      newFile.mkdirs();
    }
  }

  /**
   * 获取版本信息
   */
  public void getHttpVersion(final int updateMode,final Context context){
//    if (updateMode == MANUAL_UPDATE){ //后台更新过程中用户点击升级，之弹出下载进度弹框即可
//      if (downloadProgress >0 && downloadProgress <1){
//        showAppUpdateDialog(context);
//        return;
//      }
//    }
    if (updateMode == MANUAL_UPDATE){
      if(null != mCheckingDialog && mCheckingDialog.isShowing()){
        mCheckingDialog.dismiss();
        mCheckingDialog=null;
      }
      //手动点击升级，弹出正在检测中...
      mCheckingDialog = new CheckingDialog(context);
      mCheckingDialog.showDialog(context.getString(R.string.app_checking));
    }

    BaseRequest baseRequest  =new RxJavaRetrofitNetRequestUtils();
    baseRequest.getAppUpdateInfo(new Subscriber<APPBean>() {
      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {
        if (updateMode == MANUAL_UPDATE) {
          UiUtil.showToast(R.string.str_no_internet);
          if (null != mCheckingDialog) {
            mCheckingDialog.dismiss();
            mCheckingDialog=null;
          }
        }
      }

      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onNext(APPBean response) {
        if (updateMode == MANUAL_UPDATE) {
          if (null != mCheckingDialog) {
            mCheckingDialog.dismiss();
            mCheckingDialog=null;
          }
        }

        if(response != null && response.getStatus() == 1){
          if(response.data != null){
            handleAPPVersion(response.data, updateMode,context);
          }else{
            if (updateMode == MANUAL_UPDATE){
              //当前已经是最新的版本的时候
              mCheckingDialog = new CheckingDialog(context);
              mCheckingDialog.showDialog(context.getString(R.string.is_lastest_app_version));
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  mCheckingDialog.dismiss();
                  mCheckingDialog = null;
                }
              },2000);
            }
          }
        }else{
          if (updateMode == MANUAL_UPDATE)
          UiUtil.showToast(R.string.getver_failure);
        }
      }
    });
  }

  public void cancelVersionRequest(){
//    RestClientWithSSID.cancelRequest(this);
    if (baseRequest!=null)
    baseRequest.cancel();
  }

  /**
   * 处理版本信息
   * @param versionInfo
     */
  @RequiresApi(api = Build.VERSION_CODES.N)
  private void handleAPPVersion(APPBean.VersionInfo versionInfo, int updateMode, Context context){
    mVersionInfo = versionInfo;
    if(mVersionInfo.type == 1){ //初始化命名差量包合成的新apk
      newApkName = "Do.Fun_" + mVersionInfo.filename + "_" + MD5Util.MD5(mVersionInfo.filename) + ".apk";
    }
    isNeedUpdate(updateMode,context);
  }

  /**
   * 是否需要升级判断逻辑
   */
  @RequiresApi(api = Build.VERSION_CODES.N)
  private void isNeedUpdate(int updateMode , Context context){
    if(mVersionInfo == null){
      return;
    }

    if (updateMode == PUSH_UPDATE){
      if (mVersionInfo.versionCode > appCurrentVersionCode) { //必须满足的升级条件
          setShowIsUpdateDialog(updateMode,context);
      }
    }else if (updateMode == MANUAL_UPDATE){
      if (mVersionInfo.versionCode > appCurrentVersionCode) { //必须满足的升级条件
        setShowIsUpdateDialog(updateMode,context);
      } else {
        //当前已经是最新的版本的时候
        mCheckingDialog = new CheckingDialog(context);
        mCheckingDialog.showDialog(context.getString(R.string.is_lastest_app_version));
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
            mCheckingDialog.dismiss();
            mCheckingDialog = null;
          }
        },2000);
      }
    }
  }

  Handler handler = new Handler();


  /**
   * 展示版本信息dialog，提示用户是否升级
   */
  AppUpdateDialog updateDialog;
  @RequiresApi(api = Build.VERSION_CODES.N)
  private void setShowIsUpdateDialog(int updateMode, final Context context) {
    if(mVersionInfo == null){
      return;
    }

//    if(mVersionInfo.force==1){//强制升级 弹出单按钮对话框
        if(updateDialog != null){
          updateDialog.dissmissDialog();
          updateDialog = null;
        }

      if(hasRightApk(context)){
        Log.i("Sven","有完整安装包");
        File downLoadApk = new File(filePath,mVersionInfo.filename);
        installapk(downLoadApk,context);
        return;
      }


        updateDialog = new AppUpdateDialog( new AppUpdateDialog.IDialogClickListner() {
          @Override
          public void leftClick() {

          }

          @Override
          public void rightClick(ProgressBar progressBar) {
            mAppDownload = progressBar;
            if (NetUtils.getCurrentNetType(context) == 2) {
              useMobileTips(context);
            } else {
              setUILogic(context);
            }
          }
        },1,mVersionInfo);
        updateDialog.showDialog(context);
//    }else{
//      if(updateDialog != null){
//        updateDialog.dissmissDialog();
//        updateDialog = null;
//      }
//
//       updateDialog = new AppUpdateDialog(new AppUpdateDialog.IDialogClickListner() {
//        @Override
//        public void leftClick() {
//        }
//
//        @Override
//        public void rightClick() {
//          if (NetUtils.getCurrentNetType(context) == 2) {
//            useMobileTips(context);
//          } else {
//            setUILogic(context);
//          }
//        }
//      });
//      updateDialog.showDialog(context);
//      updateDialog.setSingleBtn(false);
//    }

    if (updateMode == PUSH_UPDATE){
      SharedPreferencesUtils.setParam(context, ServerConstants.SP_VERSION_CODE, mVersionInfo.versionCode);
    }
  }

  private void exitApp(Context context) {
//    ActivityManager.getInstance().AppExit(context);
  }

  /**
   * 流量资费提示dialog
   */
  private void useMobileTips(final Context context){
    new MaterialDialogBuilderL(context).content(R.string.app_update_mobile_net_tips)
        .cancelable(false)
        .positiveText(R.string.app_download)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            setUILogic(context);
          }
        })
        .negativeText(R.string.cancle)
        .onNegative(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//            if (mVersionInfo.force==1){//强制升级 退出app
//              exitApp(context);
//            }
          }
        })
        .show();
  }

  /**
   * 下载新版本
   */
  private void setUILogic(Context context){
    if(mVersionInfo == null){
      return;
    }
//    Log.i("Sven","下载新版本");
    //存在已下载可用整包，去安装
//    if(hasRightApk(context)){
//      File downLoadApk = new File(filePath,mVersionInfo.filename);
//      installapk(downLoadApk,context);
//      Log.i("Sven","去安装");
//      return;
//    }
    getFile(mVersionInfo.storeurl,filePath,mVersionInfo.filename);
  }
  BaseRequest baseRequest;
  /**
   * 下载apk或patch包
   * @param url
   * @param dir
   * @param mFileName
     */
  private void getFile(String url,String dir,String mFileName){
     baseRequest  = new RxJavaRetrofitNetRequestUtils(new RxJavaRetrofitNetRequestUtils.JsDownloadListener() {
      @Override
      public void onStartDownload() {

      }

      @Override
      public void onProgress(int progress) {
        mProgress =  progress;
        LogUtil.i("mProgress","progress="+progress);
        if (mProgress <= mMaxProgress) {
          Message msg =myHandler.obtainMessage();
          msg.what = 100;
          msg.obj = mProgress;
          myHandler.sendMessage(msg);
        }

        downloadProgress = progress;
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
        downloadProgress = 0;
//        if (null != dialog){
//          dialog.dismiss();
//        }
      }

      @Override
      public void onNext(Object o) {
      onCompleted();
      }
    });

  }

//  private MaterialDialog dialog; //apk下载中dialog
  private TextView       tvAppUpdate; //进度文本
//  private ProgressBar    pbAppDownload; //进度条
  private ProgressBar mAppDownload;
  private boolean mUserCancel = false; //用户取消升级标记

  /**
   * 下载完去安装
   */
  private void gotoInstallApk() {
    if(mVersionInfo == null){
      return;
    }
    if(mVersionInfo.type == 1){ //差量包升级
//      handlePatch();
    }else{ //整包升级
//      if (null != dialog){
//        dialog.dismiss();
//      }
//      updateProgress(100,100,true);
      File downLoadApk = new File(filePath,mVersionInfo.filename);
      if(isApkOk(UiUtil.getContext(),downLoadApk.getPath())){
        installapk(downLoadApk,UiUtil.getContext());
      }else{
      }
    }
  }

  public void  installapk(File downLoadApk,Context context){
    Constants.IS_APP_FORCE=(mVersionInfo.force==1);
    CommonUtils.installApk(context,downLoadApk);
  }


  public void cancelUpdate(){
    downloadProgress = 0;
  }


  /**
   * 是否存在已下载的完整可用的新版本apk
   */
  private boolean hasRightApk(Context context){
    File downLoadApk = new File(filePath,mVersionInfo.filename);
    if(!(downLoadApk.exists()&&downLoadApk.isFile())){ //文件校验
      return false;
    }
    if(!SignUtils.checkMd5(downLoadApk, mVersionInfo.md5)){ //md5校验
      downLoadApk.delete();
      return false;
    }
    if(!isApkOk(context, downLoadApk.getPath())){ //apk完整性校验
      downLoadApk.delete();
      return false;
    }
    return true;
  }

//  /**
//   * 是否存在已下载的完整可用的增量包合成的新版本apk
//   */
//  private boolean hasRightPatchApk(Context context){
//    File downLoadPatch = new File(filePath,mVersionInfo.filename);
//    if(!(downLoadPatch.exists()&&downLoadPatch.isFile())){ //文件校验
//      return false;
//    }
//    if(!SignUtils.checkMd5(downLoadPatch, mVersionInfo.md5)){ //md5校验
//      return false;
//    }
//    File newApk = new File(newApkPath, newApkName);
//    if(!(newApk.exists()&&newApk.isFile())){ //文件校验
//      return false;
//    }
//    if(!isApkOk(context, newApk.getPath())){ //apk完整性校验
//      return false;
//    }
//    return true;
//  }

  /**
   * 判断apk文件是否完整可用
   * @param context
   * @param filePath
   * @return
     */
  private boolean isApkOk(Context context,String filePath) {
    boolean result = false;
    try {
      PackageManager pm = context.getPackageManager();
      PackageInfo info = pm.getPackageArchiveInfo(filePath,PackageManager.GET_ACTIVITIES);
      if (info != null) {
        result = true;//完整
      }
    } catch (Exception e) {
      result = false;//不完整
    }
    return result;
  }


    private int mProgress = 0;//下载进度
    private int mMaxProgress = 100;//百分比
    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    int progress = (int) msg.obj;
                     mAppDownload.setProgress(progress);
                    if (100 == progress) {
                        if (null != updateDialog){
                          updateDialog.dismiss();
                        }
                        if(SignUtils.checkMd5(new File(filePath,mVersionInfo.filename), mVersionInfo.md5)){//校验文件md5
                          gotoInstallApk();
                        }else{
                          UiUtil.showToast(UiUtil.getContext().getString(R.string.correct_error));
                        }
                    }
                    break;
            }
        }
    };




    public  void  release(){
      if (myHandler!=null) myHandler.removeCallbacksAndMessages(null);

      if (updateDialog!=null) {
        updateDialog.dissmissDialog(); updateDialog=null;
      }
//      if (null != dialog){
//        dialog.dismiss();
//        dialog=null;
//        pbAppDownload=null;
//      }
      mInstance=null;

  }

}
