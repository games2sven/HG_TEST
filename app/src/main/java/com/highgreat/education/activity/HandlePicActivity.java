package com.highgreat.education.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.SerializableList;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dialog.MaterialDialogBuilderL;
import com.highgreat.education.utils.FileUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.MultiDownLoadUtil;
import com.highgreat.education.utils.PhoneSectorSizeUtils;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.utils.ValueAnimUtils;
import com.highgreat.education.widget.HackyViewPager;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/27 15:04
 * 修改人：mac-likh
 * 修改时间：16/1/27 15:04
 * 修改备注：
 */
public class HandlePicActivity extends BaseActivity {

  /**
   * 更新进度条
   */
  private final static int     MSG_UPDATE_PROGRESS           = 99;
  /**
   * 取消弹窗框
   */
  private final static int     MSG_DISMISS                   = 10000;
  /**
   * 上传失败
   */
  private final static int     MSG_UPLOAD_FAIL               = 10001;
  /**
   * 视频上传阿里服务器成功
   */
  private final static int     UPLOAD_TOSERVER_SUCCESS       = 100;


  @BindView(R.id.han_pic_img)
  HackyViewPager hackyViewPager;
  @BindView(R.id.hand_act_back)
  ImageView      handActBack;
  @BindView(R.id.share)
  ImageView      share;
  @BindView(R.id.top_layout)
  RelativeLayout top_layout;
  @BindView(R.id.delete)
  ImageView      delete;
  /**
   * view单击  边缘动画处理
   */
  boolean isTap = true;
//  SharePop sharePop;
  private File               file;
  private List<String>       pathList;
  private List<String>       thumbpathList;
  private String             currentImagePath;
  private String             currentSelectedImagePath;
  private SamplePagerAdapter samplePagerAdapter;
  private int                currentPosition;
  private int                width;
  private List<String>       picSizeList;
  private String             currentSize;
  private int                page;
  private ViewTapListener    viewTapListener;
  private int j = 0;
  private File SelectImage;
  String upImageMD5 = "";
  private boolean       isCanceled;

  @Override
  protected boolean isBindEventBusHere() {
    return true;
  }

  @Override
  protected void onEventComming(EventCenter eventCenter) {
      int  code =eventCenter.getEventCode();
      switch (code){
        case EventBusCode.CODE_DOWN_SINGLE_SUCCESS:

          String  url = (String) eventCenter.getData();
          LogUtil.e("downsuccess","下载++++++"+pathList.indexOf(url));

          if (pathList.indexOf(url)==currentPosition){
          hackyViewPager.setAdapter(samplePagerAdapter);
          hackyViewPager.setCurrentItem(currentPosition);
          }
          break;
        default:
          break;
      }
  }
Unbinder unbinder;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_dehaze_before);
    unbinder= ButterKnife.bind(this);
    width = UavConstants.GALLERY_WIDTH;
    if (width <= 0) {
      width = UavConstants.COMMON_WIDTH;
    }
    Bundle bundle = getIntent().getExtras();
    page = bundle.getInt("page");
    SerializableList serializableList = (SerializableList) bundle.get("pathList");
    SerializableList thumbPath = (SerializableList) bundle.get("thumbpathList");
    SerializableList mySizeList = (SerializableList) bundle.get("mySizeList");
    pathList = serializableList.getPicList();
    thumbpathList = thumbPath.getPicList();
    picSizeList = mySizeList.getPicList();
    samplePagerAdapter = new SamplePagerAdapter();
    hackyViewPager.setAdapter(samplePagerAdapter);
    hackyViewPager.setCurrentItem(page);
    currentPosition = page;
    currentSelectedImagePath = pathList.get(page);
    viewTapListener = new ViewTapListener();
    //选中下载
    downLoadLargePic(page);
    hackyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int i, float f, int j) {
      }

      @Override
      public void onPageSelected(int position) {
        HandlePicActivity.this.currentPosition = position;

        downLoadLargePic(position);
      }

      @Override
      public void onPageScrollStateChanged(int i) {

      }
    });
  }

  /**
   * 下载原图
   */
  private void downLoadLargePic(int position) {
    String currentImagePath = pathList.get(position);
      String name = StringUtil.splitStrForPic(currentImagePath);
      file = new File(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name);
           if (file.exists()) return;
      LogUtil.e("position", "下载原图" + currentImagePath);

      EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_ADD_NEED_DOWNLOAD_PIC,currentImagePath));
  }

  /***************************
   * 界面处理前功能实现          *
   ***************************/

  @OnClick(R.id.hand_act_back)
  public void hand_act_back() {
    finish();
    EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_PICINBIG_DELETE));
  }

  @OnClick(R.id.delete)
  public void delete() {
    if (UiUtil.isFastClick()) return;
    if (UavConstants.IS_UAV_CONN) {
      multiChoiceDeleteFromWhere(currentSelectedImagePath, currentPosition);
    } else {
      deleteLoacl(currentSelectedImagePath, currentPosition);
    }
  }

  /**
   * 删除本地
   */
  private void deleteLoacl(final String deltePicPath, final int currentPosition) {
    if (deltePicPath.isEmpty()) return;
    new MaterialDialogBuilderL(this).cancelable(false)
            .content(R.string.delete_info)
            .positiveText(R.string.agree)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                if (thumbpathList.size() == currentPosition + 1) {//最后一张
                  hackyViewPager.setCurrentItem(0);
                  currentSelectedImagePath = pathList.get(currentPosition);
                  thumbpathList.remove(currentPosition);
                  pathList.remove(currentPosition);
                  hackyViewPager.setAdapter(samplePagerAdapter);
                  if (thumbpathList.size() == 0) {
                    finish();
                  }
                } else {
                  currentSelectedImagePath = pathList.get(currentPosition);
                  thumbpathList.remove(currentPosition);
                  pathList.remove(currentPosition);
                  hackyViewPager.setAdapter(samplePagerAdapter);
                  if (thumbpathList.size() == currentPosition) {
                    hackyViewPager.setCurrentItem(0);
                  } else {
                    hackyViewPager.setCurrentItem(currentPosition);
                  }
                }
                FileUtil.delelteItemFile(currentSelectedImagePath);
              }
            })
            .negativeText(R.string.disagree)
            .show();
  }

  /**
   * 选择在哪里删除
   */
  private void multiChoiceDeleteFromWhere(final String deltePicPath, final int currentPosition) {
    new MaterialDialogBuilderL(this).items(R.array.deletepicfromwhere)
            .itemsCallbackMultiChoice(new Integer[]{0},
                    new MaterialDialog.ListCallbackMultiChoice() {
                      @Override
                      public boolean onSelection(MaterialDialog dialog, Integer[] which,
                                                 CharSequence[] text) {
                        if (which.length == 2) {
                          LogUtil.Lee("全部删除");
                          sureToDeletePic(deltePicPath, currentPosition, true, true);
                        } else if (which.length == 0) {
                          LogUtil.Lee("未删除");
                        } else {
                          for (int i = 0; i < which.length; i++) {
                            j = which[i];
                          }
                          if (j == 0) {
                            LogUtil.Lee("飞机删除");
                            sureToDeletePic(deltePicPath, currentPosition, true, false);
                          } else if (j == 1) {
                            LogUtil.Lee("手机删除");
                            sureToDeletePic(deltePicPath, currentPosition, false, true);
                          }
                        }

                        return true;
                      }
                    })
            .positiveText(R.string.agree)
            .negativeText(R.string.disagree)
            .show();
  }

  private void sureToDeletePic(String deltePicPath, int currentPosition, boolean drone,
                               boolean phone) {
    if (deltePicPath.isEmpty()) return;
    if (pathList == null || pathList.size() == 0) return;
    if (thumbpathList.size() == currentPosition + 1) {//最后一张
      hackyViewPager.setCurrentItem(0);
      currentSelectedImagePath = pathList.get(currentPosition);
      thumbpathList.remove(currentPosition);
      pathList.remove(currentPosition);
      hackyViewPager.setAdapter(samplePagerAdapter);
    } else {
      currentSelectedImagePath = pathList.get(currentPosition);
      thumbpathList.remove(currentPosition);
      pathList.remove(currentPosition);
      hackyViewPager.setAdapter(samplePagerAdapter);
      if (thumbpathList.size() == currentPosition) {
        hackyViewPager.setCurrentItem(0);
      } else {
        hackyViewPager.setCurrentItem(currentPosition);
      }
    }
    deleteSelectItem(currentSelectedImagePath, drone, phone);
  }

  /**
   * 删除选中的项目
   */
  private void deleteSelectItem(String deltePicPath, boolean drone, boolean phone) {
    //飞机端要传大图的地址去删除
    String name = StringUtil.splitStrForPic(deltePicPath);
    if (drone) {
//      OkHttpUtils.get()
//              .url(UavConstants.PIC_DELETE + name)
//              .build()
//              .execute(new ImangeBeanCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//
//                }
//
//                @Override
//                public void onResponse(ImagesBean imagesBean, int id) {
//                  LogUtil.e("test", "删除成功");
//                  EventBus.getDefault().post(new EventCenter(UavConstants.CODE_PICINBIG_DELETE));
//                  if (thumbpathList.size() == 0) {
//                    finish();
//                  }
//                }
//              });
    }
    if (phone) {
      deleteLocalWhenNet(name);
    }
  }

  /**
   * 删除飞机也删除本地
   */
  private void deleteLocalWhenNet(String name) {
    FileUtil.delelteItemFileForNet(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name);
  }

  /**
   * 加载网络显示图片
   */
  public void displayImage(String imagePath, final ImageView imageView) {
    Glide.with(this).load(imagePath)
            .placeholder(R.mipmap.ic_gf_default_photo)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView);

  }

  /**
   * 加载本地显示图片
   */
  public void displayLocalImage(File file, ImageView imageView) {
    displayImage(file.getAbsolutePath(), imageView);
  }

  public String getBitmapPath() {
    currentSelectedImagePath = pathList.get(currentPosition);
    if (currentSelectedImagePath.contains("http")) {
      String name = StringUtil.splitStrForPic(currentSelectedImagePath);
      currentSelectedImagePath = UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name;
    }
    LogUtil.Lee("share; " + currentSelectedImagePath);
    return currentSelectedImagePath;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
//    UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
  }

  /**
   * 优化返回键
   */
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
      this.finish();
      EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_PICINBIG_DELETE));
    }
    return false;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (hackyViewPager != null) {
      hackyViewPager.removeAllViews();
      hackyViewPager = null;
    }
    unbinder.unbind();
  }















  /**
   * viewpager适配器
   * http://192.168.1.1:80/internal/pic/kongying-88-2016-03-10-13-46-18-861614.jpg
   */
  class SamplePagerAdapter extends PagerAdapter {
    @Override
    public int getCount() {
      return pathList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
//      NetworkImageView photoView = views.get(position);
      PhotoView photoView = new PhotoView(container.getContext());
      currentImagePath = pathList.get(position);
      currentSize = picSizeList.get(position);
//      if (UavConstants.IS_UAV_CONN) {
//        float sizeReal = StringUtil.splitSize(currentSize);// MB/KB/0B
        String name = StringUtil.splitStrForPic(currentImagePath);
        file = new File(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name);
//        float l = file.length() / 1024f / 1024f;
//        float localFileSize = (float) (Math.round(l * 10)) / 10;
        if (!file.exists()) {
          LogUtil.e("position", "加载预览图");
          displayImage(thumbpathList.get(position), photoView);
        } else {
          LogUtil.e("position", "加载本地");
          displayLocalImage(file, photoView);
        }
//      } else {
//        file = new File(currentImagePath);
//        displayLocalImage(file, photoView);
//      }
      photoView.setOnViewTapListener(viewTapListener);
      container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT);
      return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }
  }

  private class ViewTapListener implements PhotoViewAttacher.OnViewTapListener {

    @Override
    public void onViewTap(View view, float v, float v1) {
      if (isTap) {
        isTap = false;
        ValueAnimUtils.topAnimator(0, -top_layout.getWidth(), top_layout, 0, 400);
      } else {
        isTap = true;
        ValueAnimUtils.topAnimator(-top_layout.getWidth(), 0, top_layout, 0, 400);
      }
    }
  }
}
