package com.highgreat.education.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/28 19:24
 * 修改人：mac-likh
 * 修改时间：16/1/28 19:24
 * 修改备注：
 */
public class TimeTextUtil {
    public volatile boolean flag = false;
    boolean isVisibles = true;
    private long      start;
    private long      end;
    private TextView  tv;
    private ImageView red_dot;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (tv==null||red_dot==null) {
                return;
            }
            if (msg.what == 1) {
                end = System.currentTimeMillis() - start;
//                Log.i("Sven","end = "+end +"type"+(UavConstants.MARK.equals(UavConstants.DEVICES_TYPE))+"mode:"+CameraBaseParamModel.getInstance().video );
                    tv.setText(toTimeWithMinute(end));
            }else {
                if (isVisble()) {
                    red_dot.setVisibility(View.INVISIBLE);
                } else {
                    red_dot.setVisibility(View.VISIBLE);
                }
                handler.sendEmptyMessageDelayed(0, 1000);

            }
        }
    };

    public TimeTextUtil(TextView tv, ImageView red_dot) {
        this.tv = tv;
        this.red_dot = red_dot;

    }

    /**
     * 计时到分
     */
    public void start() {
        start = System.currentTimeMillis();
        flag = true;
        new Thread(new MyThread()).start();
        handler.sendEmptyMessage(0);
    }

    public boolean isVisble() {
        isVisibles = !isVisibles;
        return isVisibles;
    }

    public void pause() {
        flag = false;
        red_dot.setVisibility(View.INVISIBLE);
        handler.removeMessages(0);
    }

    /**
     * 计时到分钟
     *
     * @param start
     * @return
     */
    private String toTimeWithMinute(long start) {
        long millisecond = start % 1000;
        millisecond /= 100;
        start /= 1000;
        long minute = start / 60;
        long second = start % 60;
        minute %= 60;
//        return String.format("%02d:%02d:%01d", minute, second, millisecond);
        return String.format("%02d:%02d", minute, second);
    }

    /**
     * 计时到s
     *
     * @param start
     * @return
     */
    private String toTime(long start) {
        long millisecond = start % 1000;
        millisecond /= 100;
        start /= 1000;
//        long second = start % 60;
        return String.format("%02d.%01d", start, millisecond);
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(100);// 线程暂停，单位毫秒
                    if(flag){
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        handler.sendMessage(message);// 发送消息
                    }
                } catch (Exception e) {
//                    e.printStackTrace();
                    LogUtil.e(e.getMessage());
                }
            }
        }
    }



    public void clear(){
        handler.removeCallbacksAndMessages(null);
        tv = null;
        red_dot = null;
    }
}
