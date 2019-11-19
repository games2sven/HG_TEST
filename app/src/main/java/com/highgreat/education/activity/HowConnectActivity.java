package com.highgreat.education.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.dialog.ResetPasswordTipDialog;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.LogUtil;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HowConnectActivity extends  BaseActivity {

    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected boolean isBindEventBusHere() {
        return true;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
           if (eventCenter!=null){
               switch (eventCenter.getEventCode()){

                   case EventBusCode.CODE_UAVNETCONNECT:
                       finish();
                       break;
                   default:
                       break;

               }


           }

    }
    Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_connect);

        unbinder = ButterKnife.bind(this);
        initWindow();
        check();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }


    @OnClick(R.id.tv_connect)
    void  startLive(){
//        readyGo(FolkCameraActivity.class);

        startWifiPage();
    }


    @OnClick(R.id.iv_close)
    void colse (){
        onBackPressed();
    }
    @OnClick(R.id.tv_forget_password)
    void showForgetDialog (){
        ResetPasswordTipDialog passwordTipDialog  =new ResetPasswordTipDialog(this);
        passwordTipDialog.show();

    }

    public static final int REQUEST_CODE = 1;
boolean  isCheck;
    private void check() {
        isCheck=true;
        boolean isAllGranted = checkPermissionAllGranted(perms);

        if (isAllGranted) {
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                perms,
                REQUEST_CODE
        );

    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        if (Build.VERSION.SDK_INT>=23) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    // 只要有一个权限没有被授予, 则直接返回 false
                    return false;
                }
            }
            return true;
        }else{
            return  true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
                // 如果所有的权限都授予了, 则执行备份代码
//                mHandler.sendEmptyMessageDelayed(110,SHOW_LOADING_TIME);
            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                showMissingPermissionDialog();
            }
        }
    }

    // 含有全部的权限
    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void initWindow() {
        getWindow().setGravity(Gravity.CENTER);       //设置靠右对齐
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.x = 0;//向右靠齐的情况下向左偏移的像素大小
        p.y= 0;
        p.height= ViewGroup.LayoutParams.MATCH_PARENT;
        p.width= ViewGroup.LayoutParams.MATCH_PARENT;

        getWindow().setAttributes(p);
    }

    // 显示缺失权限提示(普通权限)
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                startAppSettings();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
    }
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    // 启动应用的设置
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
        startActivityForResult(intent,0);
        isCheck=false;
    }

    public  void   startWifiPage(){
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wifiSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(wifiSettingsIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
