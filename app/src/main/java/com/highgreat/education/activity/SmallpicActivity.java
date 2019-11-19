package com.highgreat.education.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.Constants;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.fragment.PictureFragment;
import com.highgreat.education.fragment.VideoFragment;
import com.highgreat.education.utils.DimensionUtil;
import com.highgreat.education.utils.UiUtil;
import com.rtspclient.RTDeviceCmd;
import com.runtop.ui.RtBaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/12 15:00
 * 修改人：mac-likh
 * 修改时间：16/1/12 15:00
 * 修改备注：
 */
public class SmallpicActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.back_main)
    ImageView      back_main;
    @BindView(R.id.fl_parent)
    RelativeLayout mParent;
    @BindView(R.id.download)
    ImageView      download_video;
    @BindView(R.id.delete_pic)
    ImageView      delete_pic;
    @BindView(R.id.select_all)
    TextView       selectAll;
    @BindView(R.id.share)
    ImageView      videoShare;
    @BindView(R.id.tv_photo)
    TextView       tvPhoto;
    @BindView(R.id.tv_video)
    TextView       tvVideo;
    @BindView(R.id.tv_gallery_select)
    TextView       tvGallerySelect;
    @BindView(R.id.ll_media_op)
    LinearLayout   llMediaOp;
    @BindView(R.id.fl_download)
    FrameLayout    flDownload;
    @BindView(R.id.fl_share)
    FrameLayout    flShare;
    @BindView(R.id.fl_delete_pic)
    FrameLayout    flDeletePic;
    @BindView(R.id.iv_line1)
    View    iv_line1;
    @BindView(R.id.iv_line2)
    View    iv_line2;

    private LinkedHashMap<String, String> imagePathLinkedHashMap = new LinkedHashMap<>();
    public Map<String, Integer> selectNumMap      = new HashMap<>();//选择数量统计
    //图片路径LinkedHashMap（有序的LinkedHashMap）
    private LinkedHashMap<String, String> titleLinkedHashMap     = new LinkedHashMap<>();
    private ArrayList<String>             fileSizeList           = new ArrayList<String>();//大小

    private boolean isRefleshAllState = false;
    private PictureFragment mPictureFrag;
    private VideoFragment mVideoFrag;
    private boolean         isVideoView;
    private boolean isConnected        = false;
    private boolean isDeleteInbig      = false;
    private boolean isDeleteInLocalbig = false;

//    @Override
//    protected boolean isBindEventBusHere() {
//        return true;
//    }

//    @Override
//    protected void onEventComming(EventCenter eventCenter) {
//        int eventCode = eventCenter.getEventCode();
//        switch (eventCode) {
//            case UavConstants.CODE_UDP_CONN_TIMEOUT:
//                isConnected = false;
//                flDownload.setVisibility(View.GONE);
//                break;
//            case UavConstants.CODE_UAVNETCONNECT:
//                isConnected = true;
//                flDownload.setVisibility(View.VISIBLE);
//                break;
//            case UavConstants.CODE_REQUEST_OK:
//                initButtonClickable(true);
//                break;
//            case UavConstants.CODE_REQUEST_ERROR:
//                initButtonClickable(true);
//                break;
//            case UavConstants.CODE_REQUEST_EMPTY:
//                break;
//            case UavConstants.CODE_REQUEST_VIDEO_EMPTY:
//                break;
//            case UavConstants.CODE_LOAD_PIC_ERRO:
//                break;
//            case UavConstants.CODE_PICINBIG_DELETE:
//                isDeleteInbig = true;
//                break;
//            case UavConstants.CODE_PICINBIG_LOCALDELETE:
//                isDeleteInLocalbig = true;
//
//                break;
//                default:
//
//                    break;
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    Unbinder unbinder;


    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.small_pic_main);
        unbinder= ButterKnife.bind(this);
        setDefaultFragment();
        initButtonEvent();
        UavConstants.GALLERY_WIDTH = DimensionUtil.getSystemScreenWidth(this);
        UavConstants.MARGIN = DimensionUtil.dp2px(this, 1);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (isDeleteInbig && isConnected) {
            isDeleteInbig = false;
            if (mPictureFrag == null) return;
            mPictureFrag.refresh();
        }
        if (isDeleteInLocalbig && !isConnected) {
            isDeleteInLocalbig = false;
            if (mPictureFrag == null) return;
            mPictureFrag.refreshLocal();
        }
    }

    private void initButtonEvent() {
        back_main.setOnClickListener(this);
        download_video.setOnClickListener(this);
        delete_pic.setOnClickListener(this);
        selectAll.setOnClickListener(this);
        videoShare.setOnClickListener(this);
        llMediaOp.setOnClickListener(this);
    }

    /**
     * 控制按钮的点击状态
     */
    private void initButtonClickable(boolean clickable) {
        download_video.setClickable(clickable);
        delete_pic.setClickable(clickable);
    }

    /**
     * 设置默认的页面
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mPictureFrag = new PictureFragment();
        transaction.replace(R.id.fl_parent, mPictureFrag);
        transaction.commit();
        fm.executePendingTransactions();
//        tvVideo.setTextColor(Color.parseColor("#33fefefe"));
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (mPictureFrag != null) {
            transaction.hide(mPictureFrag);
        }

        if (mVideoFrag != null) {
            transaction.hide(mVideoFrag);
        }
    }

    @OnClick(R.id.tv_photo)
    public void switch2Photos() {
        if (!isVideoView) return;

        if (UavConstants.isSelectState) {
            UavConstants.isSelectState = false;
        }
        if (!UavConstants.IS_UAV_CONN) {
            if (mPictureFrag != null) {
                mPictureFrag.refreshLocal();
            }
        }
        changeSelectUi();
        isVideoView = false;
        flShare.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if (mPictureFrag == null) {
            mPictureFrag = new PictureFragment();
            transaction.add(R.id.fl_parent, mPictureFrag);
        } else {
            transaction.show(mPictureFrag);
        }
        titleLinkedHashMap.clear();
        imagePathLinkedHashMap.clear();
        selectNumMap.clear();
        select_cancel();
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
        tvPhoto.setTextColor(Color.parseColor("#fefefe"));
        tvVideo.setTextColor(Color.parseColor("#33fefefe"));
        iv_line1.setVisibility(View.VISIBLE);
        iv_line2.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_video)
    public void switch2Video() {
        if (isVideoView) return;
        if (UavConstants.isSelectState) {
            UavConstants.isSelectState = false;
        }
        if (!UavConstants.IS_UAV_CONN) {
            if (mVideoFrag != null) {
                mVideoFrag.refreshLocal();
            }
        }
        changeSelectUi();
        isVideoView = true;
        flShare.setVisibility(View.GONE);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        if (mVideoFrag == null) {
            mVideoFrag = new VideoFragment();
            transaction.add(R.id.fl_parent, mVideoFrag);
        } else {
            transaction.show(mVideoFrag);
        }
        titleLinkedHashMap.clear();
        imagePathLinkedHashMap.clear();
        selectNumMap.clear();
        select_cancel();
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
        tvVideo.setTextColor(Color.parseColor("#fefefe"));
        tvPhoto.setTextColor(Color.parseColor("#33fefefe"));
        iv_line2.setVisibility(View.VISIBLE);
        iv_line1.setVisibility(View.GONE);
    }

    /**
     * 媒体选择/取消
     */
    @OnClick(R.id.tv_gallery_select)
    public void setTvGallerySelect() {
        changeSelectUi();
        imagePathLinkedHashMap.clear();
        selectNumMap.clear();
        titleLinkedHashMap.clear();
        if (isVideoView) {
            if (mVideoFrag == null) return;
            mVideoFrag.selectOP();
        } else {
            if (mPictureFrag == null) return;
            mPictureFrag.selectOP();
        }
    }

    private void changeSelectUi() {
        if (UavConstants.isSelectState) {
            UavConstants.isSelectState = false;
            tvGallerySelect.setText(R.string.select_cancel);
            llMediaOp.setVisibility(View.VISIBLE);
        } else {
            UavConstants.isSelectState = true;
            tvGallerySelect.setText(R.string.select);
            llMediaOp.setVisibility(View.GONE);
        }
    }

    /**
     * 获取选择的图片路径LinkedHashMap
     *
     * @return LinkedHashMap
     */
    public LinkedHashMap<String, String> getSelectedImagePathLinkedHashMap() {
        if (imagePathLinkedHashMap != null) {
            return imagePathLinkedHashMap;
        }
        return null;
    }

    /**
     * 获取选择的选择图片对应的map集合
     *
     * @return LinkedHashMap
     */
    public Map<String, Integer> getSelectedTitleImagePath() {
        if (selectNumMap != null) {
            return selectNumMap;
        }
        return null;
    }

    /**
     * 获取选择的图片路径LinkedHashMap
     *
     * @return LinkedHashMap
     */
    public ArrayList<String> getfileSizeList() {
        if (fileSizeList != null) {
            return fileSizeList;
        }
        return null;
    }

    /**
     * 获取选择的图片路径LinkedHashMap
     *
     * @return LinkedHashMap
     */
    public LinkedHashMap<String, String> getSelectedTitleLinkedHashMap() {
        if (titleLinkedHashMap != null) {
            return titleLinkedHashMap;
        }
        return null;
    }

    public boolean getisRefleshAllState() {
        return isRefleshAllState;
    }

    @Override
    public void onClick(View v) {
        if (UiUtil.isFastClick()) return;
        switch (v.getId()) {
            case R.id.back_main://返回
                this.finish();
                break;
            case R.id.download://下载
                if(UiUtil.isFastClick()){
                    return;
                }
//                if (isVideoView) {
//                    if (mVideoFrag == null) return;
//                    mVideoFrag.downLoad(imagePathLinkedHashMap, fileSizeList, 0);
//                } else {
//                    if (mPictureFrag == null) return;
//                    mPictureFrag.downLoad(imagePathLinkedHashMap, fileSizeList);
//                }
                break;
            case R.id.delete_pic://删除
                if(UiUtil.isFastClick()){
                    return;
                }
                if (!RTDeviceCmd.checkConnectState()||!UavConstants.isSupportSDCard) {
                    //删除本地
                    if (isVideoView) {
                        if (mVideoFrag == null) return;
                        mVideoFrag.deleteLocal(imagePathLinkedHashMap);
                    } else {
                        if (mPictureFrag == null) return;
                        mPictureFrag.deleteLocal(imagePathLinkedHashMap);
                    }
                } else {
                    if (isVideoView) {
                        if (mVideoFrag == null) return;
                        mVideoFrag.delete(imagePathLinkedHashMap);
                    } else {
                        if (mPictureFrag == null) return;
                        mPictureFrag.delete(imagePathLinkedHashMap);
                    }
                }
                break;
            case R.id.share:
                break;
            case R.id.ll_media_op:
                break;

            default:
                break;
        }
    }

    /**
     * 取消选择状态
     */
    public void select_cancel() {
        if (isVideoView) {
            mVideoFrag.select_cancel();
        } else {
            mPictureFrag.select_cancel();
        }
    }

    @Override
    protected void onDestroy() {
       /* try {
            Wifi4GUtils.bringUpCellularNetwork(this);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        super.onDestroy();
//        FileUtil.rmDownLoadInfo(this);
//        TimeUtil.retriever = null;
        UavConstants.isSelectState = true;
        unbinder.unbind();
    }
}
