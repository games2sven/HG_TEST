package com.highgreat.education.fragment;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.PreferenceNames;
import com.highgreat.education.dialog.AppUpdateDialog;
import com.highgreat.education.dialog.CheckingDialog;
import com.highgreat.education.dialog.FromatDialog;
import com.highgreat.education.dialog.RecoverDialog;
import com.highgreat.education.net.RxJavaRetrofitNetRequestUtils;
import com.highgreat.education.utils.APPUpdateUtils;
import com.highgreat.education.utils.CommonUtils;
import com.highgreat.education.utils.FirmWareUpdateUtils;
import com.highgreat.education.utils.FirmwareUtils;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.MD5Util;
import com.highgreat.education.utils.SharedPreferencesUtils;
import com.highgreat.education.utils.UiUtil;
import com.runtop.other.RTDeviceCmdUtils;
import com.runtop.presenter.RtSettingDevicePresenter;
import com.runtop.presenter.RtSettingMemoryPresenter;
import com.runtop.presenter.inter.RtSettingDeviceView;
import com.runtop.presenter.inter.RtSettingMemoryView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class OthersSettingFragment extends Fragment implements View.OnClickListener, RtSettingMemoryView , RtSettingDeviceView {

    @BindView(R.id.ll_app_update)
    LinearLayout ll_app_update;
    @BindView(R.id.ll_os_update)
    LinearLayout ll_os_update;
    @BindView(R.id.txt_format)
    TextView txt_format;
    @BindView(R.id.tv_recover)
    TextView tv_recover;
    @BindView(R.id.tv_sdcard)
    TextView tv_sdcard;
    @BindView(R.id.tv_os_version)
    TextView tv_os_version;
    @BindView(R.id.tv_app_version)
    TextView tv_app_version;



    UpgradePackageBean.VersionInfo info;
    FromatDialog mDialog;
    private CheckingDialog osChecingDilaog;



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCommming(EventCenter eventCenter){
        int code = eventCenter.getEventCode();
        switch (code){
            case EventBusCode.FORMAT_SUCCESSE:
                if(mDialog != null){
                    mDialog.dismiss();
                }

                osChecingDilaog = new CheckingDialog(getContext());
                osChecingDilaog.showDialog(getString(R.string.format_success));

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        osChecingDilaog.dismiss();
                    }
                },1000);
                break;
        }
    }

    RtSettingMemoryPresenter rtSettingMemoryPresenter;
    RtSettingDevicePresenter rtSettingDevicePresenter;
    private RTDeviceCmdUtils utils;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_othersfragment,null);
        ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);

         rtSettingMemoryPresenter = new RtSettingMemoryPresenter(this);
        rtSettingDevicePresenter = new RtSettingDevicePresenter(this);
        utils = new RTDeviceCmdUtils();
        rtSettingMemoryPresenter.setRtDeviceCmdUtils(utils);
        rtSettingDevicePresenter.setRtDeviceCmdUtils(utils);
        initView();
        return view;
    }


    private void initView() {
        ll_app_update.setOnClickListener(this);
        ll_os_update.setOnClickListener(this);
        txt_format.setOnClickListener(this);
        tv_recover.setOnClickListener(this);
        utils.GetSDSpace();
        utils.GetDeviceParameter();

        String flightVersion = (String)SharedPreferencesUtils.getParam(getContext(), PreferenceNames.DRONE_FTP_VERSION, "");
        if(!TextUtils.isEmpty(flightVersion)){
            tv_os_version.setText(flightVersion);
        }else{
            tv_os_version.setText("N/A");
        }

        String appCurrentVersionName = CommonUtils.getVersionName(getContext());
        tv_app_version.setText(appCurrentVersionName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_app_update:
                APPUpdateUtils.getInstantce().appUpdate(getContext(),2);
                break;
            case R.id.ll_os_update:
                FirmWareUpdateUtils.getInstance().osUpdate(getContext(),2);
                break;
            case R.id.txt_format:
                mDialog = new FromatDialog(getContext());
                mDialog.showDialog();
                break;
            case R.id.tv_recover:
                RecoverDialog recoverDialog = new RecoverDialog(getContext());
                recoverDialog.showDialog();
                break;
        }
    }

    Handler mHandler = new Handler();

    @Override
    public void getSDSpaceCallBack(int i, double space, double totalSpace) {


        String total = String.format("%.2f", totalSpace) + " GB";
        String shengyu = String.format("%.2f", totalSpace * (double)((float)space / 100.0F)) + " GB";

        tv_sdcard.setText(String.format(getString(R.string.sdcard_space),shengyu,total));
    }

    @Override
    public Context getMyContext() {
        return getContext();
    }

    @Override
    public void getDeviceParamsCallBack(int i, String iqversion, String fwversion) {
        Log.i("Sven","获取相机固件回复："+iqversion+"  fw::"+fwversion);
    }

    @Override
    public void setPowerFrequencyCallBack(boolean b) {

    }

    @Override
    public void updateCallBack(boolean b) {

    }

    @Override
    public void uploadCallBack(boolean b) {

    }

    @Override
    public void uploadingCallBack() {

    }

    @Override
    public void getOSDStatusCallBack(int i, String s) {

    }

    @Override
    public void setOSDStatusCallBack(int i) {

    }

    @Override
    public void setOSDStringCallBack(boolean b) {

    }

    @Override
    public void resetToDefauleCallBack(boolean b) {

    }
}
