package com.highgreat.education.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.highgreat.education.R;

public class CheckingDialog implements View.OnClickListener {

    private Context mContext;
    private Dialog mDialog;

    public CheckingDialog(Context context) {
        mContext = context;
    }

    TextView tv_title;

    public void showDialog(String content){
        //,,R.style.MyDialogStyle
        mDialog = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_checking_dialog,null);
        tv_title = view.findViewById(R.id.tv_title);

        mDialog.show();
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(null);
        window.setGravity(Gravity.CENTER);

        initView(content);
    }

    private void initView(String content) {
        tv_title.setText(content);
    }

    @Override
    public void onClick(View v) {
    }


    public void dismiss(){
        if(mDialog != null)mDialog.dismiss();
    }

    public boolean isShowing(){
        if(mDialog != null){
            return mDialog.isShowing();
        }
        return false;
    }

}
