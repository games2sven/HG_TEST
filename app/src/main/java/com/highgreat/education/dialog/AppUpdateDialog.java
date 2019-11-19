package com.highgreat.education.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.highgreat.education.R;
import com.highgreat.education.bean.APPBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.PreferenceNames;
import com.highgreat.education.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class AppUpdateDialog implements View.OnClickListener{

    View mView;
    private ImageView img_close;
    private TextView tv_download;
    private TextView tv_app_version;
    private TextView tv_app_size;
    private TextView tv_log;
    private ProgressBar progressBar;
    private TextView tv_title;
    private TextView tv_version;
    private TextView tv_download_success;

    private AlertDialog dialog ;
    private IDialogClickListner mClickListner;
    private int mType = 1;//1，APP升级  2，os升级
    private Object mData;
    private Context mContext;

    public AppUpdateDialog(IDialogClickListner listner,int type,Object o){
        mClickListner = listner;
        this.mType = type;
        this.mData = o;

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter){
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object object = eventCenter.getData();
            switch (code) {
                case EventBusCode.DOWNLOAD_OS_PROGRESS:
                    progressBar.setProgress((int)(object));

                    if((int)object == 100){
                        tv_download.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        tv_download_success.setVisibility(View.VISIBLE);
                        img_close.setClickable(true);

                        //可以在这里做文件md5值验证，，是否跟服务器文件一致

                        //下载完成，保存本地文件的版本号信息
                        SharedPreferencesUtils.setPreferences(mContext, PreferenceNames.FIRMWARE_VERSION_LOCAL, ((UpgradePackageBean.VersionInfo)mData).versionName);
                        String infoJsonStr = new Gson().toJson(((UpgradePackageBean.VersionInfo)mData));
                        SharedPreferencesUtils.setParam(mContext, PreferenceNames.FIRMWARE_LOCAL_INFO, infoJsonStr);
                    }
                    break;
                case EventBusCode.DOWNLOAD_OS_SUCCESSE:

                    break;
//                tv_download.setText(mContext.getString(R.string.download_success));
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showDialog(Context context){
        mContext = context;
        dialog = new AlertDialog.Builder(context).create();
        mView = LayoutInflater.from(context).inflate(R.layout.layout_update_dialog, null);
        dialog.show();
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(null);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initView(Context context) {
        img_close = (ImageView)mView.findViewById(R.id.img_close);
        tv_download = (TextView)mView.findViewById(R.id.tv_download);
        tv_app_version = (TextView)mView.findViewById(R.id.tv_app_version);
        tv_app_size = (TextView)mView.findViewById(R.id.tv_app_size);
        tv_log = (TextView)mView.findViewById(R.id.tv_log);
        progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);

        tv_title = (TextView)mView.findViewById(R.id.tv_title);
        tv_version= (TextView)mView.findViewById(R.id.tv_version);
        tv_download_success = (TextView)mView.findViewById(R.id.tv_download_success);

        img_close.setOnClickListener(this);
        tv_download.setOnClickListener(this);

        if(mType == 1){
            tv_title.setText(context.getString(R.string.app_update));
            tv_version.setText(context.getString(R.string.app_version));

            APPBean.VersionInfo info = (APPBean.VersionInfo)mData;
            tv_app_version.setText(info.versionName);
            tv_app_size.setText(getMB(info.filesize));
            tv_log.setText(info.records);
        }else{
            tv_title.setText(context.getString(R.string.os_update));
            tv_version.setText(context.getString(R.string.os_version));

            UpgradePackageBean.VersionInfo info =  (UpgradePackageBean.VersionInfo)mData;
            tv_app_version.setText(info.versionName);
            tv_app_size.setText(getMB(info.filesize));
            tv_log.setText(info.records);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_close:
                dialog.dismiss();
                mClickListner.leftClick();
                break;
            case R.id.tv_download:
//                dialog.dismiss();
                mClickListner.rightClick(progressBar);
                tv_download.setText(mContext.getString(R.string.os_downloading));
                tv_download.setClickable(false);
                img_close.setClickable(false);
                break;
            default:
                break;
        }
    }

    public void dissmissDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }
    public interface IDialogClickListner {
        void leftClick();
        void rightClick(ProgressBar progressBar);
    }


    public void dismiss(){
        if(dialog != null)dialog.dismiss();
    }


    public String getMB(long size){
        if(size > 1024*1024){
            if(size % (1024*1024) == 0){
                long file_size = size/1024/1024;
                return file_size + "MB";
            }else {
                float file_zise = (float) (size * 1.0 / 1024 / 1024);
                return String.format("%.2f",file_zise)+"MB";
            }
        }else
            return size/1024+"KB";
    }

}
