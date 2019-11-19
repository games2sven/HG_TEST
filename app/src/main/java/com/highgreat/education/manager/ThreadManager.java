package com.highgreat.education.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by liangzi on 2016/3/17.
 */
public class ThreadManager {
  //线程池，存放一直在工作的线程
  private ExecutorService mThreadService;
  //存放临时工作的线程
  private ExecutorService mTempThreadService;
  private ScheduledExecutorService mTiemrThreadService;
  private ScheduledExecutorService mTiemrThreadServiceForgps;
  private ScheduledExecutorService mTiemrThreadServiceForBluetooth;

  public ThreadManager() {
    mThreadService = newWorkStealingPool();
    mTempThreadService = newWorkTempPool();
    mTiemrThreadService = newTimerThreadPool();
    mTiemrThreadServiceForgps = newTimerThreadPoolForCheckgps();
  }

  private static class ThreadManagerHolder {
    private static final ThreadManager INSTANCE = new ThreadManager();
  }

  public static ThreadManager getInstance() {
    return ThreadManagerHolder.INSTANCE;
  }

  /**
   * 创建线程池，存放一直在工作的线程
   */
  private ExecutorService newWorkStealingPool() {
    return Executors.newFixedThreadPool(3);
  }

  /**
   * 创建线程池，存放临时工作的线程
   */
  private ExecutorService newWorkTempPool() {
    return Executors.newCachedThreadPool();
  }

  /**
   * 创建定时线程池，存放定时工作的线程
   */
  private ScheduledExecutorService newTimerThreadPool() {
    return Executors.newScheduledThreadPool(3);
  }

  private ScheduledExecutorService newTimerThreadPoolForCheckgps() {
    return Executors.newScheduledThreadPool(3);
  }
  private ScheduledExecutorService newTimerThreadPoolForCheckBluetooth() {
    return Executors.newScheduledThreadPool(3);
  }

  /**
   * 添加线程到线程池，一直在工作的线程
   */
  public void addWorkStealingPool(Runnable run) {
    if (mThreadService == null) {
      mThreadService = newWorkStealingPool();
    }
    mThreadService.execute(run);
  }

  /**
   * 添加线程到线程池，存放临时工作的线程
   */
  public void addWorkTempPool(Runnable run) {
    if (mTempThreadService == null) {
      mTempThreadService = newWorkTempPool();
    }
    mTempThreadService.execute(run);
  }

  /**
   * 添加线程到线程池，存放定时工作的线程  1000ms/次
   */
  public void addTimerTempPool(Runnable run) {
    if (mTiemrThreadService == null) {
      mTiemrThreadService = newTimerThreadPool();
    }
    mTiemrThreadService.scheduleAtFixedRate(run, 0, 50, TimeUnit.MILLISECONDS);
  }

  /**
   * 添加线程到线程池，存放定时工作的线程  count ms/次
   */
  public void addTimerTempPool(Runnable run,long count) {
    if (mTiemrThreadService == null) {
      mTiemrThreadService = newTimerThreadPool();
    }
    mTiemrThreadService.scheduleAtFixedRate(run, 3000, count, TimeUnit.MILLISECONDS);
  }

  /**
   * 添加线程到线程池，存放定时工作的线程 5min/次   300s
   */
  public void addTimerTempPoolForgps(Runnable run, long doWhenTime, long intervalTime) {
    if (mTiemrThreadServiceForgps == null) {
      mTiemrThreadServiceForgps = newTimerThreadPoolForCheckgps();
    }
    mTiemrThreadServiceForgps.scheduleAtFixedRate(run, doWhenTime, intervalTime, TimeUnit.SECONDS);
  }

  /**
   * 关掉一直在工作的线程
   */
  public void releaseWorkStealPool() {
    if (null != mThreadService) {
      mThreadService.shutdown();
      mThreadService = null;
    }
  }

  /**
   * 关掉临时的线程
   */
  public void releaseWorkTempPool() {
    if (null != mTempThreadService) {
      mTempThreadService.shutdown();
      mTempThreadService = null;
    }
  }

  /**
   * 关掉定时的线程
   */
  public void releaseTimerPool() {
    if (null != mTiemrThreadService) {
      mTiemrThreadService.shutdown();
      mTiemrThreadService = null;
    }
  }

  /**
   * 关掉定时的线程
   */
  public void releaseTimerPoolForgps() {
    if (null != mTiemrThreadServiceForgps) {
      mTiemrThreadServiceForgps.shutdown();
      mTiemrThreadServiceForgps = null;
    }
  }


  /**
   * 添加线程到线程池，存放定时工作的线程 5min/次   300s
   */
  public void addTimerTempPoolForBluetooth(Runnable run, long doWhenTime, long intervalTime) {
    if (mTiemrThreadServiceForBluetooth == null) {
      mTiemrThreadServiceForBluetooth = newTimerThreadPoolForCheckBluetooth();
    }
    mTiemrThreadServiceForBluetooth.scheduleAtFixedRate(run, doWhenTime, intervalTime, TimeUnit.SECONDS);
  }
  /**
   * 关掉定时的线程
   */
  public void releaseTimerPoolForBluetooth() {
    if (null != mTiemrThreadServiceForBluetooth) {
      mTiemrThreadServiceForBluetooth.shutdown();
      mTiemrThreadServiceForBluetooth = null;
    }
  }


}
