package com.highgreat.education.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.UavConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：基类Activty
 * 创建人：mac-likh
 * 创建时间：15/12/18 11:21
 * 修改人：mac-likh
 * 修改时间：15/12/18 11:21
 * 修改备注：
 */
public abstract class BaseActivity extends FragmentActivity {
  /**
   * is bind eventBus
   */
  protected abstract boolean isBindEventBusHere();

  /**
   * when event comming
   */
  protected abstract void onEventComming(EventCenter eventCenter);



  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //注册EventBus
    if (isBindEventBusHere()) {
      EventBus.getDefault().register(this);
    }
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (isBindEventBusHere()) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override protected void onResume() {
    super.onResume();
//    LogEvent.onPageStart(this.getClass().getSimpleName());
//    LogEvent.onResume(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
//    LogEvent.onPageEnd(this.getClass().getSimpleName());
//    LogEvent.onPause(this);
  }

  /**
   * startActivity
   */
  protected void readyGo(Class<?> clazz) {
    Intent intent = new Intent(this, clazz);
    startActivity(intent);
  }

  /**
   * startActivity with bundle
   */
  protected void readyGo(Class<?> clazz, Bundle bundle) {
    Intent intent = new Intent(this, clazz);
    if (null != bundle) {
      intent.putExtras(bundle);
    }
    startActivity(intent);
  }

  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEventMainThread(EventCenter eventCenter) {
    if (null != eventCenter) {
      onEventComming(eventCenter);
    }
  }

  protected ProgressDialog mLoadingDialog;

  public void showLoadingDialog(boolean cancancel, DialogInterface.OnCancelListener cancelLis) {
    if (mLoadingDialog == null || !mLoadingDialog.isShowing()) {
      mLoadingDialog = new ProgressDialog(this);
      mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      mLoadingDialog.setCancelable(cancancel);
      mLoadingDialog.setCanceledOnTouchOutside(false);
      if(cancelLis!=null)
        mLoadingDialog.setOnCancelListener(cancelLis);
      mLoadingDialog.show();
    }
  }

  public void hideLoadingDialog() {
    if (mLoadingDialog != null) {
      mLoadingDialog.dismiss();
      mLoadingDialog = null;
    }
  }


}
