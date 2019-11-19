package com.highgreat.education.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class FirmwareDownLoadDialog implements View.OnClickListener{

    View mView;
    private ImageView img_close;
    private TextView tv_download;
    private TextView tv_app_version;
    private TextView tv_app_size;
    private TextView tv_log;
    private ProgressBar progressBar;
    private TextView tv_title;
    private TextView tv_version;

    private AlertDialog dialog ;
    private IDialogClickListner mClickListner;
    private int mType = 1;//1，APP升级  2，os升级
    private Object mData;
    private Context mContext;

    public FirmwareDownLoadDialog(IDialogClickListner listner){
        mClickListner = listner;
//        this.mType = type;
//        this.mData = o;

        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode =  ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter){
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object object = eventCenter.getData();
            switch (code) {
//                tv_download.setText(mContext.getString(R.string.download_success));
            }
        }
    }


    public void showDialog(Context context){
        mContext = context;
        dialog = new AlertDialog.Builder(context).create();
        mView = LayoutInflater.from(context).inflate(R.layout.layout_update_dialog, null);
        dialog.show();
        dialog.setContentView(mView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
//        window.setBackgroundDrawableResource(R.color.transparent);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        initView(context);
    }

    private void initView(Context context) {
        img_close = (ImageView)mView.findViewById(R.id.img_close);
        tv_download = (TextView)mView.findViewById(R.id.tv_download);
        tv_app_version = (TextView)mView.findViewById(R.id.tv_app_version);
        tv_app_size = (TextView)mView.findViewById(R.id.tv_app_size);
        tv_log = (TextView)mView.findViewById(R.id.tv_log);
        progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);

        tv_title = (TextView)mView.findViewById(R.id.tv_title);
        tv_version= (TextView)mView.findViewById(R.id.tv_version);

        img_close.setOnClickListener(this);
        tv_download.setOnClickListener(this);

        if(mType == 1){
            tv_title.setText(context.getString(R.string.app_update));
            tv_version.setText(context.getString(R.string.app_version));
        }else{
            tv_title.setText(context.getString(R.string.os_update));
            tv_version.setText(context.getString(R.string.os_version));
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
                mClickListner.rightClick();
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


}
