package com.highgreat.education.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.common.FlightControlCode;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.widget.GestureView;

public class RollDialog{

    private Context mContext;
    private Dialog mDialog;
    private int mMode;
    private GestureView ges_view;
    private ImageView img_close;

    public RollDialog(Context context) {
        mContext = context;
    }


    public void showDialog( ){
        //,,R.style.MyDialogStyle
        mDialog = new Dialog(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.roll_dialog,null);
        ges_view = (GestureView)view.findViewById(R.id.ges_view);
        img_close = (ImageView)view.findViewById(R.id.img_close);

        mDialog.show();
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(null);
        window.setGravity(Gravity.CENTER);

        initView();
    }

    private void initView() {
        ges_view.addListner(new GestureView.DrawResultListner() {
            @Override
            public void over(short cmd) {
                FlightCommand.command_function_control(cmd);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }


}
