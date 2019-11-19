package com.highgreat.education.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Parser;
import com.MAVLink.common.msg_autopilot_version;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.common.msg_sys_status;
import com.MAVLink.enums.MAV_CMD_PARAM_ID;
import com.dash.Const;
import com.highgreat.education.MyApp;
import com.highgreat.education.R;
import com.highgreat.education.adapter.RecycleViewAdapter;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.FlyData;
import com.highgreat.education.bean.HeartAliveModel;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.FlightControlCode;
import com.highgreat.education.common.PreferenceNames;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dialog.FirmwareUpgradeDialog;
import com.highgreat.education.dialog.SureDialog;
import com.highgreat.education.flight.RockerFly;
import com.highgreat.education.flight.RockerRelativeLayout;
import com.highgreat.education.flight.RockerView;
import com.highgreat.education.flight.TakeOffAndFly;
import com.highgreat.education.manager.ThreadManager;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.mavCommand.FlightReceiveMsg;
import com.highgreat.education.mavCommand.TcpClient;
import com.highgreat.education.receiver.NetStateReceiver;
import com.highgreat.education.utils.APPUpdateUtils;
import com.highgreat.education.utils.ByteUtil;
import com.highgreat.education.utils.DividerListItemDecoration;
import com.highgreat.education.utils.FirmWareUpdateUtils;
import com.highgreat.education.utils.ImageUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.MyItemAnimator;
import com.highgreat.education.utils.SharedPreferencesUtils;
import com.highgreat.education.utils.SpSetGetUtils;
import com.highgreat.education.utils.TimeTextUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.utils.VibratorUtil;
import com.highgreat.education.utils.WaterUtils;
import com.highgreat.education.utils.Wifi4GUtils;
import com.highgreat.education.widget.NetworkImageView;
import com.highgreat.education.widget.WaterfallRecyclerView;
import com.runtop.other.SystemUtils;
import com.runtop.ui.RtLivePlayActivity;
import com.runtop.utils.CMDSendUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.highgreat.education.activity.HowConnectActivity.REQUEST_CODE;


/**
 * @author 杨青远
 * @create 2017/12/27
 * @Describe
 */
public class FolkCameraActivity extends RtLivePlayActivity {
    APPUpdateUtils appUpdateUtils;
//    Button youBtn_takePic, youBtn_recVideo, youBtn_3d, youBtn_rotate, youBtn_file;

    String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    //    ProgressBar progressBar;
    boolean isRecordVideo = false;
    boolean isEnableRvs = true;
    boolean isFirstEnter = true;
    boolean isHighFunc = false;

    @BindView(R.id.one_key_take_off)
    ImageView oneKeyTakeOff;
    @BindView(R.id.land)
    ImageView land;
    @BindView(R.id.iv_function)
    ImageView iv_function;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    @BindView(R.id.get_small_pic)
    NetworkImageView get_small_pic;
    @BindView(R.id.video_content)
    View video_content;
    @BindView(R.id.camera_seeting)
    ImageView camera_seeting;
    @BindView(R.id.take_photo)
    ImageView take_photo;
    @BindView(R.id.video_recording)
    ImageView video_recording;
    @BindView(R.id.red_dot)
    ImageView redDot;
    @BindView(R.id.tv_video_time)
    TextView tvVideoTime;
    @BindView(R.id.ll_video_time)
    View ll_video_time;
    @BindView(R.id.wifi_signal)
    ImageView wifi_signal;
    @BindView(R.id.iv_batter)
    ImageView iv_batter;
    @BindView(R.id.emergency_hover)
    ImageView emergency_hover;
    @BindView(R.id.tv_batter)
    TextView tv_batter;
    @BindView(R.id.tv_uav_height)
    TextView tv_uav_height;
    @BindView(R.id.tv_uav_dis)
    TextView tv_uav_dis;
    @BindView(R.id.tv_uav_speed)
    TextView tv_uav_speed;
    @BindView(R.id.myRecyclerView)
    WaterfallRecyclerView mRecyclerVIew;

    Unbinder unbinder;

    public TakeOffAndFly mTakeOffAndFly;

    private Parser mParser = new Parser();
    private FlightReceiveMsg flightReceiveMsg = null;


    private SoundPool soundPool;
    private int photo_music, video_music;
    private int takePhotoplayId, videoplayId;
    private TimeTextUtil timeTextUtil;
    private ArrayList<String> listType;
    private RecycleViewAdapter recycleViewAdapter;
    private MyItemAnimator myItemAnimator;
    private DividerListItemDecoration dividerListItemDecoration;
    private long fly_info;
    private boolean returning;
    private boolean enableFly;
    private boolean landing;
    private boolean takingOff;
    private boolean hovering;
    private boolean mUavConnectFlag = true;
    private boolean mUavDisconnectFlag;
    private boolean oneLevelLowBattery;
    private boolean flag;
    private boolean flag2;
    private boolean twoLevelLowBattery;


    long connectTime;
    long disConnectTime;
    private int battery_remaining;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object object = eventCenter.getData();
            switch (code) {
                case EventBusCode.HIGH_FUNCTION_CODE://开启高级功能
                    switch ((int) object) {
                        case 1:
                            showSureDialog(1);
                            break;
                        case 2:
                            showSureDialog(2);
                            break;
                        case 3:
                            showSureDialog(3);
                            break;
                        case 4:
                            showSureDialog(4);
                            break;
                        case 5:
                            showSureDialog(5);
                            break;

                        default:
                            break;
                    }
                    break;
                case EventBusCode.CODE_IS_APP_HAS_NEW:

                    break;
                case EventBusCode.GET_VERSION_INFO_SUCCESS://获取飞机版本信息

                    msg_autopilot_version autopilot_version = (msg_autopilot_version) object;
                    String flightVersion = ByteUtil.formatVersionName(autopilot_version.autopilot_sw_version);//飞控版本号

                    //用这个值可以来判断是否连过飞机
                    if (!TextUtils.isEmpty(flightVersion)) {
                        SharedPreferencesUtils.setParam(FolkCameraActivity.this, PreferenceNames.DRONE_FTP_VERSION, flightVersion);
                    }

                    //本地保存的固件文件信息
                    String ftpVersionInfoStr = SharedPreferencesUtils.getStringPreferences(MyApp.getAppContext(), PreferenceNames.FIRMWARE_VERSION_LOCAL, "0");
                    Log.i("Sven", "flightVersion = " + flightVersion + "  ftpVersionInfoStr" + ftpVersionInfoStr + "");
                    if (!ftpVersionInfoStr.equals(flightVersion) && UiUtil.compare(flightVersion, ftpVersionInfoStr)) {
                        //满足升级固件的条件，弹出升级对话框
                        FirmwareUpgradeDialog mFirmwareDialog = new FirmwareUpgradeDialog();
                        mFirmwareDialog.showDialog(FolkCameraActivity.this);
                    }
                    break;
                default:
                    break;

                case EventBusCode.HEART_BEAT_FROM_PLANE:
                    msg_heartbeat heartbeat = (msg_heartbeat) object;
                    connectTime = System.currentTimeMillis();
                    setFlightStatus(heartbeat);
                    break;

                case EventBusCode.HEART_BEAT_SYS_STATUS:
                    msg_sys_status sys_status = (msg_sys_status) object;
                    battery_remaining = sys_status.battery_remaining;
                    int level = (int) (sys_status.battery_remaining / 12.5 + 1);
                    if (level > 8) level = 8;

                    oneLevelLowBattery = Constants.BATTERY_WARM >=battery_remaining;
                    if (!oneLevelLowBattery){
                        flag = false;
                    }
                    tv_batter.setText(battery_remaining + "%");
                    iv_batter.setImageLevel(level);

                    break;
                case EventBusCode.FLY_DATA_FEEDBACK:

//                       setUAVDisHig((FlyData) data);
//                WarningLightUtil.getInstall().checkBattery(((FlyData) data).getBattery());
                    setFlyData((FlyData) object);
                    break;

                case EventBusCode.CODE_UAVNETCONNECT:
                    if (mUavConnectFlag) {
                        mUavConnectFlag = false;
                        mUavDisconnectFlag = true;
//                        sendLowBattery(Constants.BATTERY_WARM);//连接飞机之后设置飞机低电
                        //判断飞机是否对照之前就要做的事：目前只有获取productname指令，因为就是根据productname来判断飞机是否对应，
//                   LogUtil.e("%获取UAV基本信息%-网络已连接-HesperCameraActivity-answerGetUAVInfo")
                        Log.i("Sven","去获取飞机固件版本");
                        FlightCommand.command_get_version();//进入这个页面就要去获取固件版本
                    }

                    break;


                case EventBusCode.CODE_UDP_CONN_TIMEOUT:
                    if (mUavDisconnectFlag&&isFront&&System.currentTimeMillis()-resumeTime>=2000L) {
                        mUavDisconnectFlag = false;
                        mUavConnectFlag = true;

                        disConnectUI();
                    }

                    break;

                case    EventBusCode.CODE_WIFISSID_CHANGE:
                    stopVideo();

                    break;
            }
//

        }
    }


    public  void  stopVideo(){

        if (isRecordVideo) {
            isRecordVideo = false;
            rtPresenter.videoRecordToPhoneStop();
            video_recording.setImageLevel(1);
            timeTextUtil.pause();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvVideoTime.setText("00:00");
                    get_small_pic.setVisibility(View.VISIBLE);
                    redDot.setVisibility(View.VISIBLE);
                }
            }, 300);
        }

    }

    long  resumeTime;
    @Override
    protected void onResume() {
        super.onResume();
        resumeTime=System.currentTimeMillis();
        isFront=true;
    }


    boolean  isFront;

    boolean isShowConnect = true;

    private void setFlightStatus(msg_heartbeat heartbeat) {
        int fly_mode = heartbeat.flight_mode;

        if (fly_mode == 18) {
            isHighFunc = true;
            iv_function.setImageResource(R.mipmap.tuichu);
        } else {
            isHighFunc = false;
            iv_function.setImageResource(R.mipmap.more_function);
        }

//        long flying_info = (long)heartbeat.flying_info & 0x10;
        long flying_info = (long) heartbeat.flying_info;
        boolean hovering;
        if (flying_info == 0x10) {//悬停中
            UavConstants.isFlying = true;
        }


        if ((flying_info & 0x40) == 0x40) {//是否二级低电
            twoLevelLowBattery = true;
        } else if ((flying_info & 0x40) == 0x00) {
            twoLevelLowBattery = false;
            flag2 = false;
        }

        if (UavConstants.isFlying && HeartAliveModel.mIsAlive && (oneLevelLowBattery || twoLevelLowBattery)) {
            //一级低电操作

            if (oneLevelLowBattery) {
                if (!flag) {
                    flag = true;
                    WaterUtils.getmInstall().addNotify(Constants.WARM_1);
                    VibratorUtil.vibrateWithTime(this, 500L);
                }
            }


            if (twoLevelLowBattery) {
                if (!flag2) {
                    flag2 = true;
                    WaterUtils.getmInstall().addNotify(Constants.WARM_2);
                    VibratorUtil.vibrateWithTime(this, 500L);
                }
            }

        }


    }

    public void sendLowBattery(int low){
        //低电报警只支持 0.25～0.50[25%..50%]
        FlightCommand.getInstance().command_set_params(low*1.0f/100, MAV_CMD_PARAM_ID.BATT_LOW_LEVEL);
    }

    private void disConnectUI() {
        tv_uav_height.setText("H:N/A");
        tv_uav_dis.setText("D:N/A");
        tv_uav_speed.setText("S:N/A");

        tv_batter.setText("0%");
        iv_batter.setImageLevel(1);
        wifi_signal.setImageLevel(5);
        UavConstants.isHovering = false;
        oneLevelLowBattery = false;
        twoLevelLowBattery = false;
        flag = false;
        flag2 = false;
        UavConstants.isFlying = false;
        connectTime=0;
        isShowConnect=true;
        mHandler.sendEmptyMessageDelayed(1, 1500);
        stopVideo();
    }


    private void setFlyData(FlyData flyData) {
        tv_uav_height.setText("H:" + flyData.getHeight() + "m");
        tv_uav_dis.setText("D:" + flyData.getDistance() + "m");
        tv_uav_speed.setText("S:" + flyData.getSpeed() + "m/s");
    }

    private void showSureDialog(int mode) {
        if(battery_remaining < 40){
            Toast.makeText(this, getString(R.string.function_pre2), Toast.LENGTH_SHORT).show();
            return;
        }

        if(!UavConstants.isHovering){
            Toast.makeText(this, getString(R.string.function_pre1), Toast.LENGTH_SHORT).show();
            return ;
        }

        SureDialog dialog = new SureDialog(FolkCameraActivity.this);
        dialog.showDialog(mode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String ssid = Wifi4GUtils.getWIFISSID(this);
        if (!ssid.startsWith("folk")) {
            UavConstants.needReconnect = true;
            TcpClient.getmInstance().startConn();
        }
        View controlView = getLayoutInflater().inflate(R.layout.activity_camera_folk, null);
        rtLayoutPreView.addView(controlView);
        unbinder = ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Const.setCustomerID("0x46525F014600");//设置客户编号
        hideUI();// 隐藏LivePlayActivity中的默认控件
        timeTextUtil = new TimeTextUtil(tvVideoTime, redDot);
        initRocker();
        initData();
        initTakePhotoInfo();


        initTakeoffAndLand();
        initRecycleView();
        iv_function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isHighFunc) {//正在高级功能中，退出
                    FlightCommand.command_function_control(FlightControlCode.MODE_JUMP_EXIT);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(FolkCameraActivity.this, HighFunctionActivity.class);
                    startActivity(intent);
                }

            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(FolkCameraActivity.this, SettingActivity.class);
                startActivity(intent);
            }


        });


        if (isEnableRvs) {
            /**
             * 设置开始rvs连接
             */
            if (!rtPresenter.isEnableRVSReceive()) {
                rtPresenter.setEnableRVSReceive(true);
            }
        }

//        FlightCommand.command_get_version();//进入这个页面就要去获取固件版本
        mHandler.sendEmptyMessageDelayed(1, 1500);

         mReceiver =new NetStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver,filter);
//        check();
    }
    NetStateReceiver mReceiver;

    boolean isCheck;

    private void check() {
        isCheck = true;
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
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    // 只要有一个权限没有被授予, 则直接返回 false
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (hasAllPermissionsGranted(grantResults)) {
                // 如果所有的权限都授予了, 则执行备份代码
                mHandler.removeMessages(1);//重新提示未连接
                mHandler.sendEmptyMessageDelayed(1, 1500);
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

    // 显示缺失权限提示(普通权限)
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        startActivityForResult(intent, 0);
        isCheck = false;
    }


    @BindView(R.id.rrl_throttle_left)
    RockerRelativeLayout rrlThrottleLeft;
    @BindView(R.id.rv_throttle_right)
    RockerView rvThrottleRight;
    @BindView(R.id.rv_throttle_left)
    RockerView rvThrottleLeft;
    @BindView(R.id.rrl_throttle_right)
    RockerRelativeLayout rrlThrottleRight;
    private RockerFly mRockerFly;

    private void initRecycleView() {
        listType = new ArrayList<>();
        recycleViewAdapter = new RecycleViewAdapter(listType, this);
        // recycleViewAdapter.setListener(this);
        mRecyclerVIew.setAdapter(recycleViewAdapter);
        mRecyclerVIew.setLayoutManager(new LinearLayoutManager(this));
        myItemAnimator = new MyItemAnimator();
        myItemAnimator.setAddDuration(500);
        mRecyclerVIew.setItemAnimator(myItemAnimator);
        dividerListItemDecoration = new DividerListItemDecoration(getApplicationContext(), DividerListItemDecoration.VERTICAL_LIST);
        mRecyclerVIew.addItemDecoration(dividerListItemDecoration);
        mRecyclerVIew.setClickable(false);
        WaterUtils.getmInstall().init(mHandler, listType, mRecyclerVIew, recycleViewAdapter);
    }


    private void initRocker() {


        if (mRockerFly != null) {
            mRockerFly.destroy();
            mRockerFly = null;
        }
        //摇杆飞行
        mRockerFly = new RockerFly(rrlThrottleLeft, rrlThrottleRight,
                rvThrottleLeft, rvThrottleRight);
        mRockerFly.initRockerFly();
    }

    @OnClick(R.id.iv_top_close)
    void finishLive() {
//        onBackPressed();
    }


    @Override
    public void onBackPressed() {

        if (UavConstants.isFlying && HeartAliveModel.mIsAlive) { //正在飞行中, 不让返回退出
            UiUtil.showToast(R.string.quit_devices);
            return;
        }


        if (isRecordVideo) {
            //正在录像 提示
            UiUtil.showMaterialDialog(this, R.string.record_exit_confirm, R.string.agree, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopVideo();
                    finish();
                }
            }, R.string.disagree);
            return;
        }

        finish();
    }

    private void initData() {
        //检测飞控升级
        FirmWareUpdateUtils.getInstance().osUpdate(this, 1);

        int batterymode = SpSetGetUtils.getLowBattery();

        Constants.SPEED_MODE=SpSetGetUtils.getFlySpeed();
        Constants.BATTERY_WARM = batterymode * 10 + 20;
        if (Constants.BATTERY_WARM < 20) {
            Constants.BATTERY_WARM = 20;
        }else if (Constants.BATTERY_WARM>50){
            Constants.BATTERY_WARM=50;
        }

//        appUpdateUtils = APPUpdateUtils.getInstantce();
//        appUpdateUtils.appUpdate(this, APPUpdateUtils.PUSH_UPDATE);    //显示APP版本更新推送
    }


    private void initTakeoffAndLand() {
        if (mTakeOffAndFly != null) {
            mTakeOffAndFly.destroy();
            mTakeOffAndFly = null;

        }
        mTakeOffAndFly = new TakeOffAndFly(this, oneKeyTakeOff, land, emergency_hover);
        mTakeOffAndFly.init();
    }

    @OnClick(R.id.get_small_pic)
    void previewPhoto() {

        startActivity(new Intent(this, SmallpicActivity.class));
//        startActivity(new Intent(this, RtFileSDImageActivity.class));
    }

    boolean isCameraSuccess;

    /**
     * 视频开始点播，这里一般会显示一个进度条
     * The video starts to play, where a progress bar is usually displayed.
     */
    @Override
    public void callBack_loading() {
        super.callBack_loading();
        //断开命令通道
        CMDSendUtil.disconnect();
        isCameraSuccess = false;
        stopVideo();
        if (video_content != null && video_content.getVisibility() == View.GONE) {
            video_content.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 视频点播成功，这里会将进度条隐藏掉
     * If the video is successfully played, the progress bar will be hidden here.
     *
     * @param status
     */
    @Override
    public void callBack_loadEnd(boolean status) {
        super.callBack_loadEnd(status);
        CMDSendUtil.connect();
        //连接命令通道
        isCameraSuccess = true;
        if (video_content != null && video_content.getVisibility() == View.VISIBLE) {
            video_content.setVisibility(View.GONE);
        }
    }


    /**
     * 分辨率改变回调，是只在直播过程中，如果检测到分辨率变化了会触发
     * Resolution changes callback, only during the live broadcast, if the change of resolution is detected, it will trigger.
     * <p>
     * 刚刚开始加入成功也会触发
     * Just beginning to succeed, it will trigger.
     *
     * @param resolution
     */
    @Override
    public void callBack_resolutionChange(int resolution) {
        super.callBack_resolutionChange(resolution);
    }


    /**
     * rtsp播放中断，回调，即在直播过程中的回调,如果没有特殊处理，这里就不要做任何修改了，
     * RTSP playback interruption, callback, that is, callback during live broadcasting process, if there is no special treatment,
     * there is no need to make any changes.
     * <p>
     * 本来就已经做了重连处理
     * Already did the reconnection processing
     */
    @Override
    public void callBack_rtspDisConnect() {
        super.callBack_rtspDisConnect();
    }


    /**
     * 获取当前缓存的个数，一秒回调一次
     * Gets the number of current caches.One second callback Once
     *
     * @param cacheNum
     */
    @Override
    public void callBack_getCacheNum(int cacheNum) {

//        Log.e("callBack_getCacheNum", "callBack_getCacheNum==" + cacheNum);
        super.callBack_getCacheNum(cacheNum);
    }


    /**
     * sd card状态改变回调，热拔插sd的时候会触发
     * SD(tf) card status change callback, hot plug SD(tf) will trigger.
     *
     * @param isHaveSDCard
     */
    @Override
    public void callBack_sdCardStateChange(boolean isHaveSDCard) {
        super.callBack_sdCardStateChange(isHaveSDCard);
    }

    /**
     * 接收到的流量回调
     * Received traffic callbacks
     *
     * @param receiveTraffic
     */
    @Override
    public void callBack_getReceiveTraffic(int receiveTraffic) {
        super.callBack_getReceiveTraffic(receiveTraffic);
    }


    /**
     * 遥控器拍照，录像，或者透传数据回调
     * Remote controls take pictures, videos, or pass through data callbacks.
     * <p>
     * 设备有tf卡跟无tf卡都会触发，有tf卡的时候，isSDCardRecordCallBack这个会触发，所以下面
     * Devices with or without TF cards will trigger. When there is a TF card, isSDCardRecordCallBack will trigger, so the following
     * <p>
     * 做了判断无tf卡的是才做相应的处理
     * It is judged that no TF card is processed.
     * <p>
     * 要想这个函数有效，必须presenter.setEnableRVSReceive(true)
     * For this function to be valid, it must be presenter.setEnableRVSReceive (true).
     *
     * @param data
     * @param port
     */
    @Override
    public void callBack_rvsData(byte[] data, int port) {
//        super.callBack_rvsData(data, port);
        UavConstants.CURRENT_FLIGHT_CONNECTED = false;
//        Log.e("callBack_rvsData==", ByteUtil.bytesToHexString(data));
        //收到透传数据，代表连接飞控成功
        if (isFirstEnter) {
            FlightCommand.command_get_version();
            isFirstEnter = false;
        }

        MAVLinkPacket m;
        //将收到的消息发给主界面
        for (int i = 0; i < data.length; i++) {
            m = mParser.mavlink_parse_char(data[i] & 0xff);
            if (flightReceiveMsg == null) {
                flightReceiveMsg = new FlightReceiveMsg();
            }
            if (m != null)
                flightReceiveMsg.handleMessage(m);
        }
    }


    /**
     * 设备端tf卡满回调，有tf卡才有可能触发
     * Device TF card full callback, TF card is likely to trigger.
     *
     * @param isFull
     */
    @Override
    public void callBack_tfCardFull(boolean isFull) {
        super.callBack_tfCardFull(isFull);
    }


    /**
     * 解码并转换后的视频数据回调，视频格式为rgba，如果需要视频流回调，需要再oncreate中
     * Decoded and converted video data callback, video format is rgba, if need video stream callback, need to be in oncreate again
     * <p>
     * 写presenter.setBlockVideoDecodeData(true);
     *
     * @param buffer
     * @param widht
     * @param height
     */
    @Override
    public void callBack_onVideoBuffer(ByteBuffer buffer, int widht, int height) {
        super.callBack_onVideoBuffer(buffer, widht, height);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        disConnectUI();
        unbinder.unbind();
        mHandler.removeCallbacksAndMessages(null);
        TcpClient.getmInstance().closeTcpSocket();
        unregisterReceiver(mReceiver);
        //断开命令通道
        CMDSendUtil.disconnect();
        destroySoundPool();
        if (appUpdateUtils != null) appUpdateUtils.release();

        if (timeTextUtil != null) {
            timeTextUtil.clear();
            timeTextUtil = null;
        }
        clearRcParams();
        if (mRockerFly != null) {
            mRockerFly.destroy();
            mRockerFly = null;
        }


        ThreadManager.getInstance().releaseTimerPool();
    }


    boolean isRecordMode;

    @OnClick(R.id.camera_seeting)
    void switchTakePhoneMode() {
        if (isRecordVideo || !HeartAliveModel.mIsAlive) return;

        isRecordMode = !isRecordMode;
        camera_seeting.setImageLevel(isRecordMode ? 2 : 1);
        if (isRecordMode) {

            recordingStatus();
        } else {
            takePhotoStatus();
        }


    }


    private void recordingStatus() {
        take_photo.setVisibility(View.GONE);
        video_recording.setVisibility(View.VISIBLE);
        ll_video_time.setVisibility(View.VISIBLE);
    }

    private void takePhotoStatus() {
        take_photo.setVisibility(View.VISIBLE);
        video_recording.setVisibility(View.GONE);
        ll_video_time.setVisibility(View.GONE);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                int level = (int) msg.obj;

                if (HeartAliveModel.mIsAlive || isCameraSuccess) {

                    if (level <= 0 && level >= -50) {
                        wifi_signal.setImageLevel(1);
                    } else if (level < -50 && level >= -70) {
                        wifi_signal.setImageLevel(2);
                        ;
                    } else if (level < -70 && level >= -80) {
                        wifi_signal.setImageLevel(3);
                    } else if (level < -80 && level >= -100) {
                        wifi_signal.setImageLevel(4);
                    } else {
                        wifi_signal.setImageLevel(5);
                    }
                } else {
                    wifi_signal.setImageLevel(5);

                }

            } else if (msg.what == 1) {

                if (connectTime == 0 && checkPermissionAllGranted(perms)) {
                    if (isShowConnect) {
                        isShowConnect = false;
                        startActivity(new Intent(FolkCameraActivity.this, HowConnectActivity.class));
                        overridePendingTransition(0,0);
                    }

                }
            }

        }
    };


    /**
     * 录像到手机后，会有一个默认的最长的录像时间，如果超过后，会停止保存录像，并且触发该条件
     * When the video is recorded on the mobile phone, there will be a default maximum recording time.
     * If it exceeds that, it will stop saving the video and trigger the condition.
     */
    @Override
    public void callBack_recordTimeOutListener() {
        super.callBack_recordTimeOutListener();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                isRecordVideo = false;
                rtPresenter.videoRecordToPhoneStop();
                video_recording.setImageLevel(1);
                timeTextUtil.pause();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvVideoTime.setText("00:00");
                        get_small_pic.setVisibility(View.VISIBLE);
                        redDot.setVisibility(View.VISIBLE);

                        if (!TextUtils.isEmpty(videoAbPath)) {
                            File file = new File(videoAbPath);
                            if (!file.exists()) return;
                            //创建缩略图
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoAbPath, MediaStore.Video.Thumbnails.MINI_KIND);
                            if (bitmap == null) return;

                            File bitmapFile = ImageUtil.saveBitmapToFile(bitmap, UavConstants.LOCAL_VIDEO_THUMB_ABSOLUTE_PATH, file.getName().replace(".mp4", "_thumb.jpg"));
                            if (bitmapFile != null && bitmapFile.exists())
                                get_small_pic.displayImage(bitmapFile.getPath());

                            ImageUtil.recycleImageSource(bitmap);
                        }

                    }
                }, 300);

            }
        });
    }


    @OnClick(R.id.take_photo)
    void takePhoto() {
        if (isRecordVideo || !isCameraSuccess || !HeartAliveModel.mIsAlive) return;

        take_photo.setEnabled(false);
        camera_seeting.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                take_photo.setEnabled(true);
                camera_seeting.setEnabled(true);
            }
        }, 1000);

        takePhotoplayId = soundPool.play(photo_music, 1, 1, 0, 0, 1);
        /**
         * 当然有tf卡，你也可以拍照存储到手机，将下面的判断稍微修改即可
         */
        if (!rtPresenter.isHaveSDCard() || !UavConstants.isSupportSDCard) {
            /**
             * 设置，默认的拍照保存路径，下面的路径是默认的，可以自定义，只需要执行一次即可
             *
             * 可以写在事件的外部，避免多次设置
             */
            final String phoneRecordPath = UavConstants.BIG_PIC_ABSOLUTE_PATH;

            Date curDate = new Date(System.currentTimeMillis());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String tmpName = formatter.format(curDate);

            boolean sucee = rtPresenter.takePicToPhone(phoneRecordPath, tmpName);
            if (sucee) {

                final String pic_path = phoneRecordPath + "/" + tmpName + ".jpg";
                Log.d("test", "Photograph success==  " + pic_path);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        get_small_pic.displayImage(pic_path);
                    }
                }, 300);

            } else {

                if (soundPool != null) {
                    soundPool.stop(takePhotoplayId);
                }
                Log.d("test", "Photograph fail");
            }
        } else {
            /**
             * 如果设备端有tf卡
             */
            rtBtnTakePic.performClick();
        }


    }

    /**
     * wifi  连接选择
     */
    @OnClick(R.id.wifi_signal_click)
    public void initWifiClickEvent() {
        if (UavConstants.isFlying) {
            UiUtil.showToast(R.string.switch_wifi_tip2);
            return;
        }


        /**正在录像切换wifi提示*/
        if (isRecordVideo) {
            UiUtil.showSimpleNoteDialog(this, R.string.ornotinterruptvideo, R.string.agree,
                    R.string.cancle, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startWifiPage();
                            stopVideo();

                        }
                    });
            return;
        }
        startWifiPage();

    }

    public void startWifiPage() {
        Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        wifiSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        this.startActivity(wifiSettingsIntent);
        Log.e("playevent", "wifi按钮");
    }


    String videoAbPath;

    @OnClick({R.id.video_recording})
    public void recordingVideo() {
        if (UiUtil.isFastClick() || !isCameraSuccess || !HeartAliveModel.mIsAlive) return;
        video_recording.setEnabled(false);
        camera_seeting.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                camera_seeting.setEnabled(true);
                video_recording.setEnabled(true);
            }
        }, 2000);

        videoplayId = soundPool.play(video_music, 1, 1, 0, 0, 1);
        /**
         * 当然有tf卡，你也可以录像到手机，将下面的判断稍微修改即可
         */
        if (!rtPresenter.isHaveSDCard() || !UavConstants.isSupportSDCard) {

            if (!isRecordVideo) {
                /**
                 * 设置，默认的视频保存路径，下面的路径是默认的，可以自定义，只需要执行一次即可
                 * Set the default video save path, the following path is default, you can customize, just need to execute once.
                 *
                 * 可以写在事件的外部，避免多次设置
                 * It can be written outside the event to avoid multiple settings.
                 */
                String phoneRecordPath = UavConstants.BIG_VIDEO_ABSOLUTE_PATH;
                Date curDate = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
                String tmpName = formatter.format(curDate);
                isRecordVideo = true;
                videoAbPath = phoneRecordPath + "/" + tmpName + ".mp4";
                rtPresenter.videoRecordToPhoneStart(phoneRecordPath, tmpName);
                video_recording.setImageLevel(2);
                timeTextUtil.start();
                get_small_pic.setVisibility(View.GONE);
            } else {
                isRecordVideo = false;
                rtPresenter.videoRecordToPhoneStop();
                video_recording.setImageLevel(1);
                timeTextUtil.pause();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvVideoTime.setText("00:00");
                        get_small_pic.setVisibility(View.VISIBLE);
                        redDot.setVisibility(View.VISIBLE);

                        if (!TextUtils.isEmpty(videoAbPath)) {
                            File file = new File(videoAbPath);
                            if (!file.exists()) return;
                            //创建缩略图
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoAbPath, MediaStore.Video.Thumbnails.MINI_KIND);
                            if (bitmap == null) return;

                            File bitmapFile = ImageUtil.saveBitmapToFile(bitmap, UavConstants.LOCAL_VIDEO_THUMB_ABSOLUTE_PATH, file.getName().replace(".mp4", "_thumb.jpg"));
                            if (bitmapFile != null && bitmapFile.exists())
                                get_small_pic.displayImage(bitmapFile.getPath());

                            ImageUtil.recycleImageSource(bitmap);
                        }

                    }
                }, 300);

            }

        } else {
            /**
             * 如果设备端有tf卡
             * If the device has TF Card
             */
            rtBtnVideoRecord.performClick();
        }
    }

    /**
     * 设置录像状态回调（即发送json命名成功后），只有在设备端有tf卡的时候才会触发，如果是本地录像的话是不会触发的
     * Setting the video status callback (i.e. after sending the JSON name successfully) will only trigger when the device has a TF card,
     * but it will not trigger if it is a local video.
     *
     * @param isSuccess 设置成功与否，是指发送命令成功与否（Whether the setting is successful or not is the success or failure of sending the command.）
     * @param status    是录像，还是关闭录像（It's video recording, Or close the video recording.）
     */
    @Override
    public void callBack_setRecordState(boolean isSuccess, boolean status) {
        if (!isSuccess) {
            if (soundPool != null) {
                soundPool.stop(videoplayId);
            }
            return;
        }

        isRecordVideo = status;

        if (isRecordVideo) {
            video_recording.setImageLevel(2);
            camera_seeting.setEnabled(false);

            timeTextUtil.start();
            get_small_pic.setVisibility(View.GONE);
        } else {
            video_recording.setImageLevel(1);
            camera_seeting.setEnabled(true);


            timeTextUtil.pause();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvVideoTime.setText("00:00");
                    get_small_pic.setVisibility(View.VISIBLE);
                    redDot.setVisibility(View.VISIBLE);

                }
            }, 300);
        }

    }


    /**
     * 拍照状态回调，只有在设备端有tf卡的时候才会触发，如果是本地截图的话，是不会触发的
     * Photo status callbacks are triggered only when the device has a TF card. If it is a local screenshot, they are not triggered.
     *
     * @param state
     */
    @Override
    public void callBack_setTakePicState(boolean state) {
//        super.callBack_setTakePicState(state);
    }


    /**
     * 设备端是否正在录像，这个回调会在app打开的时候如果设备端有tf录像会触发
     * Whether the device side is recording or not, this callback will trigger if the device side has TF video when the app is turned on
     * 跟发送tf录像命令后，也会触发，注意区别一下callBack_setRecordState
     * After sending the TF video command, it will trigger. Notice the difference between callBack_setRecordState.
     * callBack_setRecordState是发送json命令成功后触发
     * CallBack_setRecordState is triggered after sending the JSON command successfully.
     * callBack_isSDCardRecord是检测到I帧码流录像状态标志后触发，遥控器录像的时候如果设备端有tf卡，这个回调也会触发
     * CallBack_isSDCardRecord is triggered when the I frame video status flag is detected. If the device has a TF card on the device side,
     * this callback will also trigger when the remote control video is recorded.
     * 两个都是可以检测到录像状态
     * two are able to detect video status.
     * <p>
     * 但是presneter层做了处理，只会回调一个，presenter会根据哪个先得到结果就先回调哪个，另外一个不再回调
     * But when the presneter layer does the processing, only one callback will be made.
     * Presenter will call back which one will get the result first, and the other will not call back any more.
     *
     * @param isRecordOn
     */
    @Override
    public void callBack_isSDCardRecord(boolean isRecordOn) {
        super.callBack_isSDCardRecord(isRecordOn);

//        if (isRecordOn) {
//
//            isRecordVideo = false;
//            rtPresenter.videoRecordToPhoneStop();
//            if (isRecordMode){
//                video_recording.setImageLevel(1);
//                timeTextUtil.pause();
//                tvVideoTime.setText("00:00");
//                get_small_pic.setVisibility(View.VISIBLE);
//                redDot.setVisibility(View.VISIBLE);
//            }else{
//                takePhotoStatus();
//            }
//
//        }
//        Log.e("callBack_isSDCardRecord", "callBack_isSDCardRecord===" + isRecordOn);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (!isCheck) {
            check();
        }
    }


    @Override
    public void callBack_getWifiSignal(int level) {
        super.callBack_getWifiSignal(level);

        Message message = mHandler.obtainMessage();
        message.what = 100;
        message.obj = level;
        mHandler.sendMessage(message);

//        Log.e("callBack_getWifiSignal", "callBack_getWifiSignal==" + level);

    }


    public void initTakePhotoInfo() {
        //第一个参数为同时播放数据流的最大个数，第二数据流类型(是否受控于手机音量)，第三为声音质量
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        photo_music = soundPool.load(this, R.raw.take_pic, 1); //第3个为音乐的优先级
        video_music = soundPool.load(this, R.raw.video_record, 2); //第3个为音乐的优先级
    }

    public void destroySoundPool() {
        if (soundPool == null) {
            return;
        }
        soundPool.unload(photo_music);
        soundPool.release();
    }

    private void clearRcParams() {

        if (null != mRockerFly) {
            mRockerFly.dismissRockerFly();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        clearRcParams();
        isFront=false;
    }


}