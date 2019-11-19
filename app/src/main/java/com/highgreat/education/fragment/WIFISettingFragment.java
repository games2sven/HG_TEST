package com.highgreat.education.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.CommonUtils;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.utils.Wifi4GUtils;
import com.highgreat.education.widget.FixedEditText;
import com.highgreat.education.widget.LengthFilter;
import com.runtop.other.RTDeviceCmdUtils;
import com.runtop.presenter.RtSettingWifiPresenter;
import com.runtop.presenter.inter.RtSettingWifiView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WIFISettingFragment extends Fragment implements View.OnClickListener , RtSettingWifiView {


    @BindView(R.id.tv_prefix)
    TextView tv_prefix;
    @BindView(R.id.tv_wifi_name)
    EditText tv_wifi_name;
    @BindView(R.id.tv_sure)
    TextView tv_sure;
    @BindView(R.id.tv_wifi_pwd)
    EditText tv_wifi_pwd;

    Unbinder unbinder;

    RtSettingWifiPresenter presenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter){
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object object = eventCenter.getData();
            switch (code){
                case EventBusCode.WIFI_CHANGE_CALLBACK://修改wifi回复
                    short result = (short)object;
                    if(result == 1){//成功
                        Toast.makeText(getContext(),getString(R.string.change_wifi_success),Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }else{//失败

                    }
                    break;
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_wififragment,null);
        EventBus.getDefault().register(this);

        unbinder = ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {

        this.presenter = new RtSettingWifiPresenter(this);
        RTDeviceCmdUtils utils = new RTDeviceCmdUtils();
        this.presenter.setRtDeviceCmdUtils(utils);

        String ssid = Wifi4GUtils.getWIFISSID(getActivity());
        if(ssid.startsWith("folk")){
            tv_prefix.setText("folk-");
        }else{
            tv_prefix.setText("HG-");
        }
        tv_sure.setOnClickListener(this);

        tv_wifi_name.setFilters(new InputFilter[]{new LengthFilter(20)});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_sure:
                String wifiName = tv_prefix.getText().toString() + tv_wifi_name.getText().toString();
                String wifiPwd = tv_wifi_pwd.getText().toString();
                Log.i("Sven","wifiName :"+wifiName+"wifiPwd :"+wifiPwd);

                if(TextUtils.isEmpty(tv_wifi_name.getText().toString())){
                    UiUtil.showToast(getString(R.string.wifi_name_notempty));
                    return;
                }

                String wifiPrefix = wifiName.split("-")[0];
                String wifiSuffx = wifiName.split("-")[1];

                if(!CommonUtils.isPassword(getContext(),wifiPwd)){
                    UiUtil.showToast(getString(R.string.pwd_format));
                    return;
                }

                if(!CommonUtils.isWifiName(getContext(),wifiSuffx)){
                    Log.i("Sven","wifiSuffx = "+wifiSuffx);
                   UiUtil.showToast(getString(R.string.wifi_format));
                   return;
                }

                if(UavConstants.CURRENT_FLIGHT_CONNECTED){
                    FlightCommand.changeWifi(wifiName.getBytes(), wifiPwd.getBytes());
                }else{
                    this.presenter.setWifiParams(1, wifiPrefix,wifiSuffx, wifiPwd);
                }

                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void getWifiParamsCallBack(int i, String s, String s1) {

    }

    @Override
    public void setWifiParamsCallBack(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(getContext(), getString(R.string.change_wifi_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.change_wifi_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getMyContext() {
        return null;
    }
}
