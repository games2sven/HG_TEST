package com.highgreat.education.utils;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.MAVLink.common.msg_heartbeat;
import com.MAVLink.enums.MAV_BASE_MODE;
import com.MAVLink.enums.MAV_STATE;
import com.highgreat.education.R;
import com.highgreat.education.adapter.RecycleViewAdapter;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.UavConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WaterUtils {
    private List<String> list;
    private RecyclerView mRecycleView;
    private RecycleViewAdapter recycleViewAdapter;
    private Handler mHandler;
    private boolean oneLevelLowBattery;
    private boolean flag;
    private boolean twoLevelLowBattery;
    private boolean flag2;

    public static WaterUtils getmInstall() {
       if (mInstall==null){
           mInstall=new WaterUtils();
       }
        return mInstall;
    }

    static WaterUtils mInstall ;
    public WaterUtils() {

    }


    public void  init(Handler handler, List<String> list, RecyclerView mRecycleView, RecycleViewAdapter recycleViewAdapter){
        this.mHandler =handler;
        this.list = list;
        this.mRecycleView = mRecycleView;
        this.recycleViewAdapter = recycleViewAdapter;
    }




    /**自检异常添加到瀑布流**/

    public void addExc(List<String> list, RecycleViewAdapter recycleViewAdapter, RecyclerView mRecyclerVIew, int type){
        if(!recycleViewAdapter.getList().contains(String.valueOf(type))){
            recycleViewAdapter.Add(0,String.valueOf(type));
            mRecyclerVIew.scrollToPosition(0);
        }
    }

    public void addExc(int type){
        if(!recycleViewAdapter.getList().contains(String.valueOf(type))){
            recycleViewAdapter.Add(0,String.valueOf(type));
            mRecycleView.scrollToPosition(0);
        }
    }
    public void addExcWithMsg( String msg, int type) {
        if(!recycleViewAdapter.getList().contains(String.valueOf(type))){
            recycleViewAdapter.AddMessage(0,String.valueOf(type),msg);
            mRecycleView.scrollToPosition(0);
        }
    }

    public void removeExc(List<String> list, RecycleViewAdapter recycleViewAdapter, int type) {
        if(recycleViewAdapter.getList().contains(String.valueOf(type))){
            for(int i=0; i< recycleViewAdapter.getList().size();i++){
                if(list.get(i).equals(String.valueOf(type))){
                    recycleViewAdapter.Remove(i);
                }
            }
        }
    }

    private boolean existNotify;
    /**瀑布流非自检消息提示**/
    public void addNotify(int type) {
        for(int i=0; i<recycleViewAdapter.getList().size();i++){//检查之前的list里有没有非自检消息
            int data = Integer.valueOf(recycleViewAdapter.getList().get(i));
            if(data>=0){//设定非自检消息提示编号大于50
                existNotify = true;
                break;
            }
        }
        if(!existNotify){
            startTimer(type);
        }
        addExc(type);
    }

    public void addNotifyWithMsg(String msg, int type) {
        for(int i=0; i<recycleViewAdapter.getList().size();i++){
            int data = Integer.valueOf(recycleViewAdapter.getList().get(i));
            if(data>=0){//设定非自检消息提示编号大于50
                existNotify = true;
                break;
            }
        }

        if(!existNotify)
            startTimer(type);
        addExcWithMsg(msg,type);
    }

    public void removeNotify(int type) {
        for(int i =0;i<recycleViewAdapter.getList().size();i++){
            int data = Integer.valueOf(recycleViewAdapter.getList().get(list.size()-(i+1)));
            if(data>=0) {
                recycleViewAdapter.Remove(recycleViewAdapter.getList().size() - (i + 1));
                existNotify = false;//假设移除最下面一条消息后非自检消息为空，然后重新检查上面是否存在
                if (recycleViewAdapter.getList().size() != 0) {
                    for (int j = 0; j < list.size(); j++) {
                        int shu = Integer.valueOf(recycleViewAdapter.getList().get(j));
                        if (shu >=0 ) {//光流信号弱和SD卡自检
                            existNotify = true;
                            //stopTimer();//停止后重新开始计时器，是为了避免后面一条消息显示时间过短的情况出现
                            // startTimer(list, recycleViewAdapter, mRecyclerVIew, type);
                            break;
                        }
                        if(j ==recycleViewAdapter.getList().size()&& !existNotify){
                            stopTimer();
                        }
                    }
                    break;
                }else {
                    stopTimer();
                }
            }
        }
    }

    Timer mTimer;
    private TimerTask mTimerTask;


    /**每隔3秒移除一次最下面的提示消息**/
    public void startTimer(final int type){
        if(mTimerTask==null){
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            removeNotify(type);
                        }
                    });
                }
            };
        }
        if(mTimer == null){
            mTimer = new Timer();
            mTimer.schedule(mTimerTask,3000,3000);
        }
    }
    public void stopTimer(){
        if(mTimer !=null && mTimerTask!=null){
            mTimer.cancel();
            mTimerTask.cancel();
            mTimerTask=null;
            mTimer=null;
        }
    }

}
