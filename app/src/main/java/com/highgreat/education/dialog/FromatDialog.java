package com.highgreat.education.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.runtop.other.RTDeviceCmdUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class FromatDialog implements View.OnClickListener {

    private Context mContext;
    private Dialog mDialog;
    private final RTDeviceCmdUtils utils;

    public FromatDialog(Context context) {
        mContext = context;

        utils = new RTDeviceCmdUtils();
    }

    TextView tv_title;
    TextView tv_content;
    TextView tv_cancel;
    TextView tv_start;
    RelativeLayout rl_wait;
    LinearLayout ll_function;

    public void showDialog(){
        //,,R.style.MyDialogStyle
        mDialog = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog,null);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_content =  (TextView)view.findViewById(R.id.tv_content);
        tv_cancel =  (TextView)view.findViewById(R.id.tv_cancel);
        tv_start =  (TextView)view.findViewById(R.id.tv_start);
        rl_wait =  (RelativeLayout)view.findViewById(R.id.rl_wait);
        ll_function = (LinearLayout)view.findViewById(R.id.ll_function);

        mDialog.show();
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(null);
        window.setGravity(Gravity.CENTER);

        initView();
    }

    private void initView() {
        tv_cancel.setOnClickListener(this);
        tv_start.setOnClickListener(this);


        tv_title.setText(mContext.getString(R.string.format_sdcard));
        tv_content.setText(mContext.getString(R.string.format_tips));
        tv_start.setText(mContext.getString(R.string.format_start));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                mDialog.dismiss();
                break;
            case R.id.tv_start:
//                mDialog.dismiss();
                rl_wait.setVisibility(View.VISIBLE);
                ll_function.setVisibility(View.GONE);

                utils.SetSDFormat();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new EventCenter(EventBusCode.FORMAT_SUCCESSE));
                    }
                },1000);
                break;
        }
    }

    public void dismiss(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

    Handler handler = new Handler();


}
