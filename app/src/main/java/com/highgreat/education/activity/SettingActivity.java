package com.highgreat.education.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.fragment.DeviceSettingFragment;
import com.highgreat.education.fragment.OthersSettingFragment;
import com.highgreat.education.fragment.WIFISettingFragment;
import com.highgreat.education.utils.Wifi4GUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.tv_wifi)
    TextView tv_wifi;
    @BindView(R.id.tv_device)
    TextView tv_device;
    @BindView(R.id.tv_others)
    TextView tv_others;
    @BindView(R.id.fragment_content)
    FrameLayout fragment_content;
    @BindView(R.id.img_close)
    ImageView img_close;

    private FragmentManager fragmnetManager;
    FragmentTransaction transaction;
    private WIFISettingFragment wifiSettingFragment;
    private DeviceSettingFragment deviceSettingFragment;
    private OthersSettingFragment othersSettingFragment;

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_setting);
        ButterKnife.bind(this);
        initWindow();
        fragmnetManager = getSupportFragmentManager();
        tv_wifi.setOnClickListener(this);
        tv_device.setOnClickListener(this);
        tv_others.setOnClickListener(this);
        img_close.setOnClickListener(this);

        String ssid = Wifi4GUtils.getWIFISSID(SettingActivity.this);
//        if(ssid.startsWith("folk")){
//            tv_wifi.setVisibility(View.INVISIBLE);
//            tv_device.performClick();
//        }else{
            tv_wifi.performClick();
//        }

        if(UavConstants.isFlying){
            tv_wifi.setVisibility(View.INVISIBLE);
            tv_others.setVisibility(View.INVISIBLE);
            tv_device.performClick();
        }

    }


    @Override
    public void onClick(View v) {
        clearOthers();
        transaction = fragmnetManager.beginTransaction();
        hindFragment();
        switch (v.getId()){
            case R.id.img_close:
                finish();
                break;
            case R.id.tv_wifi:
                tv_wifi.setSelected(true);
                tv_wifi.setTextColor(getResources().getColor(R.color.white));

                if(wifiSettingFragment == null){
                    wifiSettingFragment = new WIFISettingFragment();
                    transaction.add(R.id.fragment_content,wifiSettingFragment);
                }else{
                    transaction.show(wifiSettingFragment);
                }
                break;
            case R.id.tv_device:
                tv_device.setSelected(true);
                tv_device.setTextColor(getResources().getColor(R.color.white));

                if(deviceSettingFragment == null){
                    deviceSettingFragment = new DeviceSettingFragment();
                    transaction.add(R.id.fragment_content,deviceSettingFragment);
                }else{
                    transaction.show(deviceSettingFragment);
                }


                break;
            case R.id.tv_others:
                tv_others.setSelected(true);
                tv_others.setTextColor(getResources().getColor(R.color.white));

                if(othersSettingFragment == null){
                    othersSettingFragment = new OthersSettingFragment();
                    transaction.add(R.id.fragment_content,othersSettingFragment);
                }else{
                    transaction.show(othersSettingFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hindFragment() {
        if (wifiSettingFragment!=null){
            transaction.hide(wifiSettingFragment);
        }
        if (deviceSettingFragment!=null){
            transaction.hide(deviceSettingFragment);
        }
        if (othersSettingFragment!=null){
            transaction.hide(othersSettingFragment);
        }
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


    private void clearOthers() {
        tv_wifi.setSelected(false);
        tv_device.setSelected(false);
        tv_others.setSelected(false);
        tv_wifi.setTextColor(getResources().getColor(R.color.white_20));
        tv_device.setTextColor(getResources().getColor(R.color.white_20));
        tv_others.setTextColor(getResources().getColor(R.color.white_20));
    }
}
