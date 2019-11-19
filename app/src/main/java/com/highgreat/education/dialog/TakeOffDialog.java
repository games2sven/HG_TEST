package com.highgreat.education.dialog;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.MAVLink.common.msg_command_ack;
import com.MAVLink.common.msg_command_long;
import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.enums.MAV_BASE_MODE;
import com.MAVLink.enums.MAV_CMD;
import com.MAVLink.enums.MAV_RESULT;
import com.MAVLink.enums.MAV_STATE;
import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.mavCommand.FlightCommand;
import com.highgreat.education.utils.LogUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TakeOffDialog {

    private final Context mContext;
    private final int mType;
    private Dialog mDia;

    public void setFlyClickListener(OnFlyClickListener flyClickListener) {
        this.flyClickListener = flyClickListener;
    }

    OnFlyClickListener flyClickListener;
    public TakeOffDialog(Context context,int type) {
        this.mContext= context;
        this.mType =type;
    }

    long  time =0;
   public  void  show(){
       mDia = new Dialog(mContext,R.style.time_dialog);
       View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_take_off, null);
       mDia.show();
       mDia.setContentView(mView);
       mDia.setCancelable(false);
       mDia.setCanceledOnTouchOutside(false);
//       mDia.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
       mView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mDia.dismiss();
           }
       });

       final View view  =mView.findViewById(R.id.v_scale_anim);
       TextView tv_take_off =mView.findViewById(R.id.tv_take_off);
       TextView tv_dialog_tip =mView.findViewById(R.id.tv_dialog_tip);

       tv_take_off.setText(mType==1?R.string.text_take_off:R.string.text_land);
       tv_dialog_tip.setText(mType==1?R.string.text_take_off_tip:R.string.text_land_tip);
       tv_take_off.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               int  action =event.getAction();
               switch (action){
                   case MotionEvent.ACTION_DOWN:
                       time =System.currentTimeMillis();
                       startAnim(view);
                       break;

                   case  MotionEvent.ACTION_MOVE:

                       break;

                   case MotionEvent.ACTION_UP:
                       if (System.currentTimeMillis()-time>CLICK_TIME&&time>0){

                           float  x =event.getX();
                           float y =event.getY();
//                           if (x>=0&&x<=v.getWidth()&&y>=0&&y<=v.getHeight()){

                               LogUtil.e("onTouch:   finish");
                               if (flyClickListener!=null){
                                   mDia.dismiss();
                                   flyClickListener.onFlyLongClick();
                               }

//                           }

                       } else {
                           endAnim(view);
                       }

                       break;

                       default:
                       break;


               }

               LogUtil.e("onTouch:   "+event.getAction()+"   x"+event.getX()+"  y"+event.getY());

               return true;
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


    int  CLICK_TIME=1000;
    AnimatorSet animatorSet;
   public  void  startAnim(View view){
       if (animatorSet!=null){
           animatorSet.cancel();
       }
       view.setPivotX(view.getWidth()/2);  // X方向中点
       view.setPivotY(view.getHeight()/2);   // Y方向底边
        animatorSet = new AnimatorSet();  //组合动画
       ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX",  1f, 2.53f);
       ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 2.53f);

       animatorSet.setDuration(CLICK_TIME);  //动画时间
       animatorSet.setInterpolator(new DecelerateInterpolator());  //设置插值器
       animatorSet.play(scaleX).with(scaleY);  //同时执行
       animatorSet.start();  //启动动画

   }


   public  void  endAnim(View view ){
       if (animatorSet!=null){
           animatorSet.cancel();
       }

       WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
       DisplayMetrics dm = new DisplayMetrics();
       assert manager != null;
       manager.getDefaultDisplay().getMetrics(dm);
       float f =1.0f*dm.density*95/view.getWidth();

       view.setPivotX(view.getWidth()/2);  // X方向中点

       view.setPivotY(view.getHeight()/2);   // Y方向底边
       animatorSet = new AnimatorSet();  //组合动画
       ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f,f);
       ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, f);
       animatorSet.setDuration(200);  //动画时间
       animatorSet.setInterpolator(new DecelerateInterpolator());  //设置插值器
       animatorSet.play(scaleX).with(scaleY);  //同时执行
       animatorSet.start();  //启动动画


   }

   public  interface  OnFlyClickListener{
      void  onFlyLongClick();
   }
   public boolean  isShowing(){

        return mDia != null && mDia.isShowing();
   }

}
