package com.highgreat.education.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.highgreat.education.R;

public class ResetPasswordTipDialog {

    private final Context mContext;
    private Dialog mDia;

    public ResetPasswordTipDialog(Context context) {
    this.mContext= context;
    }


    long  time =0;
   public  void  show(){

       mDia = new Dialog(mContext,R.style.time_dialog);
       View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_reset_password_tip, null);
       mDia.show();
       mDia.setContentView(mView);
       mDia.setCancelable(true);
       mDia.setCanceledOnTouchOutside(true);
//       mDia.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        mView.findViewById(R.id.tv_know).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDia.dismiss();
            }
        });
       Window window = mDia.getWindow();
       assert window != null;
       window.setBackgroundDrawableResource(R.color.transparent);
       window.setGravity(Gravity.CENTER);
       WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
       DisplayMetrics dm = new DisplayMetrics();
       assert manager != null;
       manager.getDefaultDisplay().getMetrics(dm);
       WindowManager.LayoutParams lp = window.getAttributes();
       lp.width = dm.widthPixels;
       lp.height =dm.heightPixels;
       window.setAttributes(lp);
       mDia.show();
   }





}
