package com.highgreat.education.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.common.FlightControlCode;
import com.highgreat.education.mavCommand.FlightCommand;

public class SureDialog implements View.OnClickListener {

    private Context mContext;
    private AlertDialog mDialog;
    private int mMode;

    public SureDialog(Context context) {
        mContext = context;
    }

//    @BindView(R.id.tv_title)
    TextView tv_title;
//    @BindView(R.id.tv_content)
    TextView tv_content;
//    @BindView(R.id.tv_cacle)
    TextView tv_cancel;
//    @BindView(R.id.tv_start)
    TextView tv_start;

    public void showDialog(int mode){
        //,,R.style.MyDialogStyle
        this.mMode = mode;
        mDialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_dialog,null);
        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_start = view.findViewById(R.id.tv_start);

        mDialog.show();
        mDialog.setContentView(view);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(null);
        window.setGravity(Gravity.CENTER);

        initView(mode);
    }

    private void initView(int mode) {
        switch (mode){
            case 1:
                tv_title.setText(mContext.getString(R.string.mode_1));
                tv_content.setText(mContext.getString(R.string.mode_1_start_sure));
                break;
            case 2:
                tv_title.setText(mContext.getString(R.string.mode_2));
                tv_content.setText(mContext.getString(R.string.mode_2_start_sure));
                break;
            case 3:
                tv_title.setText(mContext.getString(R.string.mode_3));
                tv_content.setText(mContext.getString(R.string.mode_3_start_sure));
                break;
            case 4:
                tv_title.setText(mContext.getString(R.string.mode_4));
                tv_content.setText(mContext.getString(R.string.mode_4_start_sure));
                break;
            case 5:
                tv_title.setText(mContext.getString(R.string.mode_5));
                tv_content.setText(mContext.getString(R.string.mode_5_start_sure));
                break;
        }

        tv_cancel.setOnClickListener(this);
        tv_start.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                mDialog.dismiss();
                break;
            case R.id.tv_start:
                mDialog.dismiss();

                if (mMode!=4){
                    int i=0;
                    while (i<3){
                        sendControl();
                        i++;
                    }
                }else{
                    sendControl();
                }

                break;
                default:
                    break;
        }
    }

    public void sendControl(){
        switch (mMode){
            case 1:
                FlightCommand.command_function_control(FlightControlCode.MODE_THROW_FLY);
                break;
            case 2:
                FlightCommand.command_function_control(FlightControlCode.MODE_CIRCLE);
                break;
            case 3:
                FlightCommand.command_function_control(FlightControlCode.MODE_360);
                break;
            case 4:
                RollDialog rollDialog = new RollDialog(mContext);
                rollDialog.showDialog();

//                FlightCommand.command_function_control(FlightControlCode.MODE_FLIP_RIGHT);
                break;
            case 5:
                FlightCommand.command_function_control(FlightControlCode.MODE_JUMP);
                break;
                default:
                    break;
        }
    }

}
