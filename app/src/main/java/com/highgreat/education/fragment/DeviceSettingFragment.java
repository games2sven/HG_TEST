package com.highgreat.education.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.MAVLink.enums.MAV_CMD_PARAM_ID;
import com.highgreat.education.R;
import com.highgreat.education.activity.HandDemoActivity;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.SpSetGetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DeviceSettingFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tv_normal)
    TextView tv_normal;
    @BindView(R.id.tv_fast)
    TextView tv_fast;
    @BindView(R.id.tv_janpanese)
    TextView tv_janpanese;
    @BindView(R.id.tv_ameriacan)
    TextView tv_ameriacan;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.tv_progress)
    TextView tv_progress;
    @BindView(R.id.tv_control_demo)
    TextView tv_control_demo;

    Unbinder unbinder;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCommming(EventCenter eventCenter){
        int code = eventCenter.getEventCode();
        switch (code){
            case EventBusCode.CODE_RECOVERY_DEFAULT:
                int  rockmode =SpSetGetUtils.getRockerMode();
                if (rockmode==1){
                    tv_janpanese.setSelected(true);
                    tv_ameriacan.setSelected(false);
                    tv_janpanese.setTextColor(getResources().getColor(R.color.black_bg));
                    tv_ameriacan.setTextColor(getResources().getColor(R.color.white));
                }else{
                    tv_janpanese.setSelected(false);
                    tv_ameriacan.setSelected(true);
                    tv_ameriacan.setTextColor(getResources().getColor(R.color.black_bg));
                    tv_janpanese.setTextColor(getResources().getColor(R.color.white));
                }

                int  flySpeed =SpSetGetUtils.getFlySpeed();
                Constants.SPEED_MODE=SpSetGetUtils.getFlySpeed();
                if (flySpeed==2){
                    tv_fast.setSelected(true);
                    tv_normal.setSelected(false);
                    tv_fast.setTextColor(getResources().getColor(R.color.black_bg));
                    tv_normal.setTextColor(getResources().getColor(R.color.white));
                }else{
                    tv_normal.setSelected(true);
                    tv_fast.setSelected(false);
                    tv_normal.setTextColor(getResources().getColor(R.color.black_bg));
                    tv_fast.setTextColor(getResources().getColor(R.color.white));
                }

                int  batterymode =SpSetGetUtils.getLowBattery();
                mSeekbar.setProgress(batterymode);
                tv_progress.setText(batterymode*10+20+"%");
                Constants.BATTERY_WARM = batterymode * 10 + 20;
                if (Constants.BATTERY_WARM < 20) {
                    Constants.BATTERY_WARM = 20;
                }else if (Constants.BATTERY_WARM>50){
                    Constants.BATTERY_WARM=50;
                }
                break;
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EventBus.getDefault().register(this);

        View view = inflater.inflate(R.layout.layout_devicefragment,null);
        unbinder = ButterKnife.bind(this,view);
        initView();

        return view;
    }

    private void initView() {

        tv_normal.setSelected(true);
        tv_normal.setTextColor(getResources().getColor(R.color.black_bg));
        tv_fast.setSelected(false);

        tv_normal.setOnClickListener(this);
        tv_fast.setOnClickListener(this);
        tv_janpanese.setOnClickListener(this);
        tv_ameriacan.setOnClickListener(this);
        tv_control_demo.setOnClickListener(this);

        int  batterymode =SpSetGetUtils.getLowBattery();
        mSeekbar.setProgress(batterymode);
        tv_progress.setText(batterymode*10+20+"%");


        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_progress.setText(progress*10+20+"%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //拖动停止

                Constants.BATTERY_WARM=seekBar.getProgress()*10+20;
//                sendLowBattery( Constants.BATTERY_WARM);
                Log.i("Sven","progress:"+seekBar.getProgress());
                SpSetGetUtils.setLowBattery(seekBar.getProgress());
            }
        });


        int  rockmode =SpSetGetUtils.getRockerMode();
        if (rockmode==1){
            tv_janpanese.setSelected(true);
            tv_ameriacan.setSelected(false);
            tv_janpanese.setTextColor(getResources().getColor(R.color.black_bg));
            tv_ameriacan.setTextColor(getResources().getColor(R.color.white));
        }else{
            tv_janpanese.setSelected(false);
            tv_ameriacan.setSelected(true);
            tv_ameriacan.setTextColor(getResources().getColor(R.color.black_bg));
            tv_janpanese.setTextColor(getResources().getColor(R.color.white));
        }

        int  flySpeed =SpSetGetUtils.getFlySpeed();
        if (flySpeed==2){
            tv_fast.setSelected(true);
            tv_normal.setSelected(false);
            tv_fast.setTextColor(getResources().getColor(R.color.black_bg));
            tv_normal.setTextColor(getResources().getColor(R.color.white));
        }else{
            tv_normal.setSelected(true);
            tv_fast.setSelected(false);
            tv_normal.setTextColor(getResources().getColor(R.color.black_bg));
            tv_fast.setTextColor(getResources().getColor(R.color.white));
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_normal:
                tv_normal.setSelected(true);
                tv_fast.setSelected(false);
                tv_normal.setTextColor(getResources().getColor(R.color.black_bg));
                tv_fast.setTextColor(getResources().getColor(R.color.white));
                SpSetGetUtils.setFlySpeed(1);
                Constants.SPEED_MODE=1;
                break;
            case R.id.tv_fast:
                tv_normal.setSelected(false);
                tv_fast.setSelected(true);
                tv_fast.setTextColor(getResources().getColor(R.color.black_bg));
                tv_normal.setTextColor(getResources().getColor(R.color.white));
                SpSetGetUtils.setFlySpeed(2);
                Constants.SPEED_MODE=2;
                break;
            case R.id.tv_janpanese:
                tv_janpanese.setSelected(true);
                tv_ameriacan.setSelected(false);
                tv_janpanese.setTextColor(getResources().getColor(R.color.black_bg));
                tv_ameriacan.setTextColor(getResources().getColor(R.color.white));
                SpSetGetUtils.setRockerMode(1);
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_ROCKER_MODE,1));
                break;
            case R.id.tv_ameriacan:
                tv_janpanese.setSelected(false);
                tv_ameriacan.setSelected(true);
                tv_ameriacan.setTextColor(getResources().getColor(R.color.black_bg));
                tv_janpanese.setTextColor(getResources().getColor(R.color.white));
                SpSetGetUtils.setRockerMode(2);
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_ROCKER_MODE,2));
                break;
            case R.id.tv_control_demo:
                Intent intent = new Intent();
                intent.setClass(getContext(), HandDemoActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }



    public void sendLowBattery(int low){
        //低电报警只支持 0.25～0.50[25%..50%]
        float f = low*1.0f/100;

        LogUtil.e("sendLowBattery==="+f);
        FlightCommand.getInstance().command_set_params(f, MAV_CMD_PARAM_ID.BATT_LOW_LEVEL);
    }




}
