package com.highgreat.education.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.PreferenceNames;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.CRC32;
import com.highgreat.education.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class FirmwareUpgradeDialog implements View.OnClickListener{

    View mView;
    private ImageView img_close;
    private TextView tv_upgrade;
    private TextView tv_app_version;
    private TextView tv_app_size;
    private TextView tv_log;
    private TextView tv_title;
    private TextView tv_version;

    private AlertDialog dialog ;
    private IDialogClickListner mClickListner;
    private Object mData;
    private Context mContext;

    UpgradePackageBean.VersionInfo versionInfo;



    public FirmwareUpgradeDialog(){
        //tv_upgrade
//        mClickListner = listner;

    }

    public void showDialog(Context context){
        mContext = context;
        dialog = new AlertDialog.Builder(context).create();
        mView = LayoutInflater.from(context).inflate(R.layout.layout_firmware_upgrade_dialog, null);
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

    private void initView(Context context) {
        String mDobbyOSVersion = (String)SharedPreferencesUtils.getParam(mContext, PreferenceNames.FIRMWARE_LOCAL_INFO, "");
        versionInfo = new Gson().fromJson(mDobbyOSVersion, UpgradePackageBean.VersionInfo.class);

        img_close = (ImageView)mView.findViewById(R.id.img_close);
        tv_upgrade = (TextView)mView.findViewById(R.id.tv_upgrade);
        tv_app_version = (TextView)mView.findViewById(R.id.tv_app_version);
        tv_app_size = (TextView)mView.findViewById(R.id.tv_app_size);
        tv_log = (TextView)mView.findViewById(R.id.tv_log);

        img_close.setOnClickListener(this);
        tv_upgrade.setOnClickListener(this);

        if(versionInfo != null){
            tv_app_version.setText(versionInfo.versionName);
            tv_app_size.setText(getMB(versionInfo.filesize));
            tv_log.setText(versionInfo.records);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_close:
                dialog.dismiss();
//                mClickListner.leftClick();
                break;
            case R.id.tv_upgrade:
                dialog.dismiss();
                //开始上传固件到飞机
                Log.i("Sven","发送升级命令");
                FlightCommand.cmdUpdate((byte)1);
                UploadingDialog dialog = new UploadingDialog(mContext,versionInfo.filename);
                dialog.showDialog();
//                mClickListner.rightClick();
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
        void rightClick();
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
                DecimalFormat decimalFormat = new DecimalFormat("0.0");
                String result = decimalFormat.format(file_zise) + "MB";
                return result;
            }
        }else
            return size/1024+"KB";
    }
}
