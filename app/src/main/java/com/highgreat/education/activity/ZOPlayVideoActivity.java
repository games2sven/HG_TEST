package com.highgreat.education.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.videocontrollerview.ResizeSurfaceView;
import com.highgreat.education.videocontrollerview.VideoControllerView;
import com.highgreat.education.widget.NetworkImageView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @FileName：Camera1.5
 * @description：
 * @author：mac-likh
 * @date：2017/1/4 15:11
 */

public class ZOPlayVideoActivity extends BaseActivity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener,
  VideoControllerView.MediaPlayerControlListener, MediaPlayer.OnVideoSizeChangedListener, MediaPlayer.OnCompletionListener,
  MediaPlayer.OnInfoListener, MediaPlayer.OnErrorListener {
  private static final int TYPE_LOCAL = 0;
  private static final int TYPE_NETWORK = 1;

  @BindView(R.id.videoSurface)
  ResizeSurfaceView mVideoSurface;
  @BindView(R.id.loading)
  ProgressBar mLoadingView;
  @BindView(R.id.act_top_back)
  ImageView actTopBack;
  @BindView(R.id.back_layout_top)
  RelativeLayout backLayoutTop;
  @BindView(R.id.videoSurfaceContainer)
  FrameLayout videoSurfaceContainer;
  @BindView(R.id.iv_image_look)
  NetworkImageView imageLook;
  @BindView(R.id.loading_image)
  ImageView mImgeLoading;
  @BindView(R.id.loading_percent)
  TextView loadingPercent;
  @BindView(R.id.loadingView)
  RelativeLayout mLoadingViewPercent;
  @BindView(R.id.video_container)
  RelativeLayout mContentView;

  private Animation mAnimation;

  private String videoPath;
  private String mpid;
  private String thumbPath;
  private String title;
  private int type = 0;//0本地，1网络
  private int mVideoWidth;
  private int mVideoHeight;
  private boolean mIsComplete;
  private boolean isShowNetImage;
  private Handler mHandler;
  private boolean isPlaying;

  private MediaPlayer mMediaPlayer;
  private VideoControllerView controller;

  public static void playLocal(Activity activity, String path, @Nullable String title) {
    playLocal(activity, path, title, null);
  }

  public static void playLocal(Activity activity, String path, @Nullable String title, String rightstr) {
    Intent intent = new Intent(activity, ZOPlayVideoActivity.class);
    intent.putExtra("type", TYPE_LOCAL);
    intent.putExtra("title", title);
    intent.putExtra("path", path);
//    if (!TextUtils.isEmpty(rightstr)) {
//      intent.putExtra("right", rightstr);
//    }
    activity.startActivity(intent);
  }

  public static void playNetwork(Activity activity, String mpid, String thumbPath, @Nullable String title) {
    Intent intent = new Intent(activity, ZOPlayVideoActivity.class);
    intent.putExtra("type", TYPE_NETWORK);
    intent.putExtra("title", title);
    intent.putExtra("mpid", mpid);
    intent.putExtra("thumbPath", thumbPath);
    activity.startActivity(intent);
  }

  @Override
  protected boolean isBindEventBusHere() {
    return false;
  }

  @Override
  protected void onEventComming(EventCenter eventCenter) {

  }

  Unbinder unbinder;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_video_player);
    unbinder=  ButterKnife.bind(this);
    mHandler = new Handler();
    SurfaceHolder mSurfaceHolder = mVideoSurface.getHolder();
    mSurfaceHolder.addCallback(this);

    imageLook.setPlaceholderImage(R.mipmap.icon_default_holder);

    controller = new VideoControllerView.Builder(this, this)
      .withVideoTitle(title)
      .withVideoSurfaceView(mVideoSurface)
      .canControlBrightness(true)
      .canControlVolume(true)
      .canSeekVideo(true)
      .exitIcon(R.mipmap.back_white_icon)
      .pauseIcon(R.mipmap.sp_zhanting)
      .playIcon(R.mipmap.sp_bofang)
      .shrinkIcon(R.mipmap.ic_media_fullscreen_shrink)
      .stretchIcon(R.mipmap.ic_media_fullscreen_stretch)
      .setVideoRightVisible(getIntent().hasExtra("right"))
      .setVideoRightContent(getIntent().getStringExtra("right"))
      .build(videoSurfaceContainer);

    type = getIntent().getIntExtra("type", 0);
    title = getIntent().getStringExtra("title");
    if (type == TYPE_LOCAL) {
      videoPath = getIntent().getStringExtra("path");
      if (TextUtils.isEmpty(title)) {
        title = StringUtil.splitStrForVideo(videoPath);
      }
      mLoadingView.setVisibility(View.VISIBLE);
    } else if (type == TYPE_NETWORK) {//网络
      setAnimation();
      mpid = getIntent().getStringExtra("mpid");
      thumbPath = getIntent().getStringExtra("thumbPath");
    }
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (null != mMediaPlayer) {
      controller.sendViewHideMessage();
      if(mMediaPlayer.isPlaying()) {
        mMediaPlayer.pause();
      }
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    releaseMediaPlayer();

    if (mAnimation != null) {
      mAnimation.reset();
    }
    mHandler.removeCallbacks(hideNetImageRunnable());
    mHandler = null;
    unbinder.unbind();
  }

  private void initMediaDataSource() {
    try {
      if (mMediaPlayer != null) {
        mMediaPlayer.stop();
      }
      mMediaPlayer = new MediaPlayer();
      mMediaPlayer.setOnVideoSizeChangedListener(this);
      mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
      mMediaPlayer.setDisplay(mVideoSurface.getHolder());
      mMediaPlayer.setDataSource(videoPath);

      mMediaPlayer.setOnErrorListener(this);
      mMediaPlayer.setOnPreparedListener(this);
      mMediaPlayer.setOnCompletionListener(this);
      mMediaPlayer.setOnInfoListener(this);
      mMediaPlayer.prepareAsync();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalStateException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    mVideoSurface.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        if (!isShowNetImage) {
          controller.toggleControllerView();
        }
        return false;
      }
    });
  }

  @Override
  public boolean onError(MediaPlayer mp, int what, int extra) {
    return false;
  }

  @Override
  public void onPrepared(MediaPlayer mp) {
    controller.showController();
    mLoadingView.setVisibility(View.GONE);
    mVideoSurface.setVisibility(View.VISIBLE);
    mMediaPlayer.start();
    mIsComplete = false;
  }

  @Override
  public void onCompletion(MediaPlayer mp) {
    mIsComplete = true;
  }

  @Override
  public boolean onInfo(MediaPlayer mp, int what, int extra) {
    if (mp.isPlaying()) {
      if (!isPlaying) {
        isPlaying = true;
        mHandler.postDelayed(hideNetImageRunnable(), 1000L);
      }
    }
    return true;
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    videoDataReset();
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    releaseMediaPlayer();
  }


  private void videoDataReset() {
    if (type == TYPE_LOCAL) {
      initMediaDataSource();
    } else if (type == TYPE_NETWORK) {
      getVideoUrlPlay();
    }
  }

  private void getVideoUrlPlay() {
//    Map<String, String> params = new HashMap<>();
//    params.put("uid", UserUtils.getUserID());
//    params.put("mpid", mpid);
//    params.put("flag", "2");
//    params.put("vway", ""); //视频方式 [0-480、1-720、2-1080]
//    HttpManager.getVideoUrl(this, params, new HttpManager.RequestCallBack<VideoUrlBean>() {
//      @Override
//      public void onFailure(String errorMessage) {
//        UiUtil.showToast(errorMessage);
//        finish();
//      }
//
//      @Override
//      public void onSuccess(VideoUrlBean response) {
//        if (response != null) {
//          if (response.getStatus() == 1) {
//            // 携带url跳转到预览页面
//            videoPath = response.getData().getVurl();
//            if (!TextUtils.isEmpty(thumbPath)) {
//              showNetImage(thumbPath);
//            }
//            if (!TextUtils.isEmpty(videoPath)) {
//              initMediaDataSource();
//            }
//          } else {
//            UiUtil.showToast(response.getTips());
//            finish();
//          }
//        } else {
//          UiUtil.getString(R.string.str_vurl_error);
//          finish();
//        }
//      }
//    });
  }

  public void showNetImage(String mImageUrl) {
    isShowNetImage = true;
    imageLook.setVisibility(View.VISIBLE);
    backLayoutTop.setVisibility(View.VISIBLE);
    imageLook.displayImage(mImageUrl);
  }

  public void hideNetImage() {
    isShowNetImage = false;
    if (mLoadingViewPercent != null) {
      mLoadingViewPercent.setVisibility(View.GONE);
    }
    if (backLayoutTop != null) {
      backLayoutTop.setVisibility(View.GONE);
    }
    if (imageLook != null) {
      imageLook.setVisibility(View.GONE);
    }
  }

  public void setAnimation() {
    mLoadingViewPercent.setVisibility(View.VISIBLE);
    mAnimation = AnimationUtils.loadAnimation(this, R.anim.ptr_loading);
    mAnimation.setDuration(5000);
    mImgeLoading.startAnimation(mAnimation);
  }

  @OnClick(R.id.act_top_back)
  public void setActTopBack() {
    finish();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
//        controller.showController();
    return false;
  }

  @Override
  public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
    mVideoHeight = mp.getVideoHeight();
    mVideoWidth = mp.getVideoWidth();
    if (mVideoHeight > 0 && mVideoWidth > 0)
      mVideoSurface.adjustSize(mContentView.getWidth(), mContentView.getHeight(), mMediaPlayer.getVideoWidth(), mMediaPlayer.getVideoHeight());
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    if (mVideoWidth > 0 && mVideoHeight > 0)
      mVideoSurface.adjustSize(UiUtil.getScreenWidth(), UiUtil.getScreenHeight(), mVideoSurface.getWidth(), mVideoSurface.getHeight());
  }

  private void releaseMediaPlayer() {
    try {
      if (mMediaPlayer != null) {
        if (mMediaPlayer.isPlaying()) {
          mMediaPlayer.stop();
        }

        mMediaPlayer.reset();
        mMediaPlayer.release();
        mMediaPlayer = null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    isPlaying = false;
  }

  /**
   * Implement VideoMediaController.MediaPlayerControl
   */

  @Override
  public int getBufferPercentage() {
    return 0;
  }

  @Override
  public int getCurrentPosition() {
    if (null != mMediaPlayer)
      return mMediaPlayer.getCurrentPosition();
    else
      return 0;
  }

  @Override
  public int getDuration() {
    if (null != mMediaPlayer)
      return mMediaPlayer.getDuration();
    else
      return 0;
  }

  @Override
  public boolean isPlaying() {
    if (null != mMediaPlayer) {
      boolean playing = mMediaPlayer.isPlaying();
      controller.refreshPauseButton(playing);
      return playing;
    } else
      return false;
  }

  @Override
  public boolean isComplete() {
    return mIsComplete;
  }

  @Override
  public void pause() {
    if (null != mMediaPlayer) {
      mMediaPlayer.pause();
    }
  }

  @Override
  public void seekTo(int i) {
    if (null != mMediaPlayer) {
      mMediaPlayer.seekTo(i);
    }
  }

  @Override
  public void start() {
    if (null != mMediaPlayer) {
      mMediaPlayer.start();
      mIsComplete = false;
    }
  }

  @Override
  public boolean isFullScreen() {
    return getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
  }

  @Override
  public void toggleFullScreen() {
    if (isFullScreen()) {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
  }

  @Override
  public void exit() {
    releaseMediaPlayer();
    finish();
  }

  @Override
  public void rightClickEvent() {
  }

  @NonNull
  private Runnable hideNetImageRunnable() {
    return new Runnable() {
      @Override
      public void run() {
        LogUtil.Lee("hideNetImageRunnable");
        hideNetImage();
      }
    };
  }
}
