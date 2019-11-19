package com.highgreat.education.dialog;

import android.app.Dialog;
import android.content.Context;
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
import com.highgreat.education.utils.SpSetGetUtils;

import org.greenrobot.eventbus.EventBus;

public class RecoverDialog implements View.OnClickListener {

    private Context mContext;
    private Dialog mDialog;

    public RecoverDialog(Context context) {
        mContext = context;
    }

    TextView tv_known;
    TextView tv_content;
    TextView tv_cancel;
    TextView tv_start;
    LinearLayout ll_function;

    public void showDialog(){
        //,,R.style.MyDialogStyle
        mDialog = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_recover,null);
        ll_function = (LinearLayout)view.findViewById(R.id.ll_function);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_start = (TextView) view.findViewById(R.id.tv_start);
        tv_content = (TextView) view.findViewById(R.id.tv_content);
        tv_known = (TextView)view.findViewById(R.id.tv_known);

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
        tv_known.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                mDialog.dismiss();
                break;
            case R.id.tv_start:
                tv_content.setText(mContext.getString(R.string.recover_ok));
                ll_function.setVisibility(View.GONE);
                tv_known.setVisibility(View.VISIBLE);
                SpSetGetUtils.setFlySpeed(1);
                SpSetGetUtils.setRockerMode(1);
                SpSetGetUtils.setLowBattery(0);
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_RECOVERY_DEFAULT));
                break;
            case R.id.tv_known:
                mDialog.dismiss();
                break;
        }
    }

    public void dismiss(){
        if(mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
        }
    }

}
