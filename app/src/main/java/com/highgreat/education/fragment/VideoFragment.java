package com.highgreat.education.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dash.Const;
import com.highgreat.education.R;
import com.highgreat.education.activity.SmallpicActivity;
import com.highgreat.education.activity.ZOPlayVideoActivity;
import com.highgreat.education.adapter.ImageListAdapter;
import com.highgreat.education.bean.DownloadBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.ImagesBean;
import com.highgreat.education.bean.MediaBean;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dao.DBHelper;
import com.highgreat.education.dialog.MaterialDialogBuilderL;
import com.highgreat.education.holder.CountItemViewHolder;
import com.highgreat.education.manager.TasksManager;
import com.highgreat.education.manager.ThreadManager;
import com.highgreat.education.utils.FileUtil;
import com.highgreat.education.utils.ImageUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.PhoneSectorSizeUtils;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.TimeUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.utils.YMComparator;
import com.rtspclient.RTDeviceCmd;
import com.runtop.other.RTDeviceCmdUtils;
import com.runtop.presenter.RtFileSDVideoPresenter;
import com.runtop.presenter.inter.RtFileSDVideoView;
import com.tonicartos.superslim.LayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Do Good App
 * 项目名称：简单的可多选相册
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/2/26 23:07
 * 修改人：mac-likh
 * 修改时间：16/2/26 23:07
 * 修改备注：
 */
public class VideoFragment extends android.support.v4.app.Fragment {
    /**
     * 更新进度条
     */
    private final static int     MSG_UPDATE_PROGRESS           = 99;
    /**
     * 上传切割后的图片
     */
    private final static int     MSG_UPLOAD_DEEP_IMAGE         = 102;
    /**
     * 所有图片上传成功
     */
    private final static int     MSG_UPLOAD_DEEP_IMAGE_SUCCESS = 103;
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
    /**
     * 本地文件扫面成功
     */
    private final static int     SCAN_LOCAL_FILE_FINISH        = 200;
    private final static int     SCAN_LOCAL_EMPTY              = 300;
    private final static int     SCAN_LOCAL_OK                 = 400;
    private final static int     START_FLOW_PHOTO_UPLOAD       = 802;
    public               boolean noDelete                      = false;
    @BindView(R.id.ll_empty)
    LinearLayout llVideoEmpty;
    @BindView(R.id.tv_empty)
    TextView     tvEmpty;
    /**
     * 利用网络请求图片数据，填充数据集合
     *
     * @param pageUrl
     * @param totalPage
     */
    int    pageNum    = 0;
    /**
     * 删除图片的请求
     *
     * @param picNameList
     * @param imagePathLinkedHashMap
     */
    int    deleteNum  = 0;
    String thumbPath  = "";
    String upVideoMD5 = "";
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;
    LinkedHashMap<String,String> videoHashMap=new LinkedHashMap<>();
//    private ArrayList<String>   currentThumbImageList = new ArrayList<>();//当前图片集合
//    private ArrayList<String>   currentBigImageList   = new ArrayList<>();//当前图片集合
//    private ArrayList<String>   currentImageTimeList  = new ArrayList<>();//当前图片集合
//    private ArrayList<String>   currentSizeList       = new ArrayList<>();//当前图片集合

    List<MediaBean> mediaBeanList = new ArrayList<>();
    private Map<String, String> orderSort             = new LinkedHashMap<>();
    private Map<String, String> orderTimeSort         = new LinkedHashMap<>();
    private Map<String, String> orderThumbSort        = new LinkedHashMap<>();
    private Map<String, String> orderSizeSort         = new LinkedHashMap<>();
    private boolean             isLoacl               = true;
    private boolean             isNet                 = true;
    private boolean             isOpenVideo           = false;
    private float               totalSize             = 0;
    private int                 j                     = 0;
    private RecyclerView recyclerView;
    private ImageListAdapter mImageAdapter;
    private SmallpicActivity activity;
    private View             view;
    private LayoutManager mLayoutManager;
    /********
     * debug modle
     **********/
    private boolean isDeleteEnable  = true;
    private String  videoPathSelect = "";
    private File SelectVideo;
    private boolean isDeep;
    private boolean       isCanceled;
    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_LOCAL_FILE_FINISH:
                    initAdapter();
                    break;
                case SCAN_LOCAL_EMPTY:
                    initAdapter();
                    if (llVideoEmpty == null) return;
                    llVideoEmpty.setVisibility(View.VISIBLE);
                    break;
                case SCAN_LOCAL_OK:
                    if (llVideoEmpty == null) return;
                    llVideoEmpty.setVisibility(View.GONE);
                    break;

                    default:

                        break;

            }
        }
    };
    private boolean isStop = false;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            List<File> sortList = new ArrayList<File>();
            File file = new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH);
            File[] subFile = file.listFiles();
            if (subFile != null && subFile.length > 0) {
                for (int i = 0; i < subFile.length; i++) {
                    if (isStop) {
                        return;
                    }
                    String filename = subFile[i].getName();
                    if (filename.trim().toLowerCase().endsWith(".temp")) {
                        FileUtil.delelteItemFile(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + filename);
                    }
                    if (filename.trim().toLowerCase().endsWith(".mp4")) {
                        noDelete = true;
                    }
                }
            }
            //删除脏数据 文件夹没有MP4文件
            if (!noDelete) {
                FileUtil.deleteAllFile(new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH));
            }
            File[] subFileLast = file.listFiles();
            if (subFileLast == null || subFileLast.length == 0) {
                mHandler.sendEmptyMessage(SCAN_LOCAL_EMPTY);
                return;
            }
            mHandler.sendEmptyMessage(SCAN_LOCAL_OK);
            for (File f : subFileLast) {
                if (f.isFile()) {
                    sortList.add(f);
                }
            }
            Collections.sort(sortList, new YMComparator());
            for (int i = 0; i < sortList.size(); i++) {
                if (isStop) {
                    return;
                }
                String filename = sortList.get(i).getName();
                if (filename.trim().toLowerCase().endsWith(".mp4")) {


                    String videoAbPath = sortList.get(i).getAbsolutePath();


                    if (!orderSort.containsValue(videoAbPath)) {
                        long times = sortList.get(i).lastModified();
                        String time = TimeUtil.paserTimeToYM(times);
                        //小数点后保留一位
                        long fileLength = sortList.get(i).length();
                        float localFileSize = (float) (Math.round(fileLength / 1024f / 1024f * 10)) / 10;
//                        File myCaptureFile = new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + filename.replace(".mp4", "_thumb.jpg"));
                        File myCaptureFile = new File(UavConstants.LOCAL_VIDEO_THUMB_ABSOLUTE_PATH+ "/" + filename.replace(".mp4", "_thumb.jpg"));
                       boolean  isOk=false;
                        if (myCaptureFile.exists()) {
                            LogUtil.Lee("缩略图已经存在");
                            orderThumbSort.put(filename, myCaptureFile.getAbsolutePath());
                            isOk=true;
                        } else {
                            //创建缩略图
                            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoAbPath, MediaStore.Video.Thumbnails.MINI_KIND);
                            if (bitmap!=null) {
                                ImageUtil.saveBitmapToFile(bitmap, UavConstants.LOCAL_VIDEO_THUMB_ABSOLUTE_PATH, filename.replace(".mp4", "_thumb.jpg"));
                                ImageUtil.recycleImageSource(bitmap);
                                isOk=true;
                                orderThumbSort.put(filename,  UavConstants.LOCAL_VIDEO_THUMB_ABSOLUTE_PATH+"/"+filename.replace(".mp4", "_thumb.jpg"));
                            }else{
                                isOk=false;
                                FileUtil.deleteFile(new File(videoAbPath),true);
                            }


                        }
                        if (isOk){
                            orderSort.put(filename, videoAbPath);
                            orderTimeSort.put(filename, time);
                            orderSizeSort.put(filename, localFileSize + "");
                        }

                    }
                }
            }

            Map<String, String> stringListMap = StringUtil.sortMapStringByKey(orderSort);
            for (Map.Entry<String, String> entry : stringListMap.entrySet()) {
                if (isStop) {
                    return;
                }

                String  key  =entry.getKey();
                String  time =orderTimeSort.get(key);
                String  bigImage =orderSort.get(key);
                String  thumbImage =orderThumbSort.get(key);
                String  size =orderSizeSort.get(key) + "M";

                MediaBean mediaBean =new MediaBean(true,thumbImage,bigImage,time,size);
                mediaBeanList.add(mediaBean);

            }
            mHandler.sendEmptyMessage(mediaBeanList.size()>0?SCAN_LOCAL_FILE_FINISH:SCAN_LOCAL_EMPTY);
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            int code = eventCenter.getEventCode();
            Object data = eventCenter.getData();
//            switch (code) {
//                case EventBusCode.CODE_UDP_CONN_TIMEOUT:
//                    if (isLoacl) {
//                        isLoacl = false;
//                        isNet = true;
//                        refreshLocal();
//                    }
//                    break;
//                case EventBusCode.CODE_UAVNETCONNECT:
//                    if (isNet) {
//                        initNetData(view, false);
//                        isLoacl = true;
//                        isNet = false;
//                    }

//                    break;
//                case EventBusCode.CODE_LOAD_VIDEO_SUCCESS:
//                    DownloadBean downloadBean = (DownloadBean) data;
////          mImageAdapter.isFinish(downloadBean.ketTime, downloadBean.url);
//                    String result = downloadBean.result;
//                    UavConstants.ISDOWNLOADFINISH = true;
//                    mImageAdapter.notifyDataSetChanged();
//                    if (isOpenVideo) {
//                        boolean added = this.isAdded();
//                        if (added) {
//                            ZOPlayVideoActivity.playLocal(this.getActivity(), result, "", UiUtil.getString(R.string.edit));
//                        }
//                    }
//                    isOpenVideo = false;
//                    break;
//                case EventBusCode.CODE_VIDEO_DOWNLOAD_FINISH:
//                    DownloadBean downloadBeanAll = (DownloadBean) data;
//                    mImageAdapter.removeTitleLinkedHashMap(downloadBeanAll.ketTime, downloadBeanAll.url);
//                    String resultAll = downloadBeanAll.result;
//                    UavConstants.ISDOWNLOADFINISH = true;
//                    mImageAdapter.notifyDataSetChanged();
//                    if (isOpenVideo) {
//                        boolean added = this.isAdded();
//                        if (added) {
//                            ZOPlayVideoActivity.playLocal(this.getActivity(), resultAll, "", UiUtil.getString(R.string.edit));
//                        }
//                    }
//                    isOpenVideo = false;
//                    break;
//             default:
//                 break;
//            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }




    public  void  getVideoList(){
            refreshLocal();
        }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /******************/

    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.small_pic_gallery, null);
        unbinder=  ButterKnife.bind(this, view);
        initSwipeRefresh(view);
        initRecyclerView(view);
        UiUtil.initAutoRefresh(mSwipeLayout);
        activity = (SmallpicActivity) getActivity();
        getVideoList();
        return view;
    }

    private void initRecyclerView(View view) {
        //配置recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_fragment_photo_gallery);
        mLayoutManager = new LayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
        recyclerView.setHasFixedSize(true);//保持固定的大小，如果可以确定每个Item的高度是固定的，设置这个选项可以提高性能
    }

    /**
     * 展示本地数据
     */
    private void initLoaclData() {
        clearOldData();
        getVideoTime();
        noDelete = false;
        ThreadManager.getInstance().addWorkTempPool(mRunnable);
    }

    /**
     * 初始化分页数据
     */
    private void initNetData(final View view, final boolean b) {
        getVideoTime();
//        OkHttpUtils.get()
//                .url(UavConstants.URL_VIDEO)
//                .tag(VideoFragment.this)
//                .build()
//                .execute(new ImangeBeanCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        if (call.isCanceled()) {
//                            return;
//                        }
//                        UiUtil.showToast(UiUtil.getContext().getString(R.string.net_erro));
//                        if(mSwipeLayout!= null) {
//                            mSwipeLayout.setRefreshing(false);
//                        }
//                        EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_ERROR));
//                    }
//
//                    @Override
//                    public void onResponse(ImagesBean imagesBean, int id) {
//                        if (imagesBean == null) {
//                            return;
//                        }
//                        int totalPage = imagesBean.getTotalPage();
//                        if (totalPage != 0) {
//                            EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_OK));
//                            paserData(imagesBean, totalPage, b);
//                            //拼接连续请求地址
//                            for (int i = 2; i < totalPage + 1; i++) {
//                                initImageDataRequest(UavConstants.URL_VIDEO_PAGE + i, totalPage, view, b);
//                            }
//                        } else {
//                            EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_VIDEO_EMPTY));
//                            initAdapter(true, b);
//                        }
//                        if (mSwipeLayout!=null)
//                        mSwipeLayout.setRefreshing(false);
//                    }
//                });



            if (RTDeviceCmd.checkConnectState()) {
//                this.setTimeOut();
//                this.fileSDVideoView.callBack_getVideoListBegin();
//                this.returnVideoListTimes = 0;
//                this.fileSize = 0;
//                this.recvSize = 0;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(1);
                int month = calendar.get(2) + 1;
                int day = calendar.get(5);
                RTDeviceCmd.GetVideoList(year, month, day);
            }
    }

    private void initImageDataRequest(String pageUrl, final int totalPage, final View view,
                                      final boolean b) {
//        OkHttpUtils.get()
//                .url(pageUrl)
//                .tag(VideoFragment.this)
//                .build()
//                .execute(new ImangeBeanCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_ERROR));
//                    }
//
//                    @Override
//                    public void onResponse(ImagesBean imagesBean, int id) {
//                        paserData(imagesBean, totalPage, b);
//                    }
//                });
    }

    private void paserData(ImagesBean imagesBean, int totalPage, boolean b) {
        pageNum++;
        List<ImagesBean.DataEntity> dataList = imagesBean.getData();
        if (dataList == null || dataList.size() <= 0) return;

        for (int i = 0; i < dataList.size(); i++) {
            String thumbPath = dataList.get(i).getThumb();
            String bigPath = dataList.get(i).getPath();
            String time = dataList.get(i).getCreated();
            String size = dataList.get(i).getSize();
            //                    "1970-01-01 00:59:18"
            String times = StringUtil.stringTimePattern(time);

            if (!orderSort.containsValue(bigPath)){//不包含大图才加进去  避免加进去多次
            orderThumbSort.put(time, thumbPath);
            orderSort.put(time, bigPath);
            orderTimeSort.put(time, times);
            orderSizeSort.put(time, size);
            }
        }
        if (pageNum == totalPage) {
            pageNum = 0;
            initAdapter();
        }
    }

    private void getVideoTime() {
        String[] projection = {
                MediaStore.Video.Media.DATA,MediaStore.Video.Media.DURATION};
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        getContentProvider(uri, projection);
    }

    /**
     * 获取ContentProvider
     *
     * @param projection
     */
    public void getContentProvider(Uri uri, String[] projection) {
        if (activity.getContentResolver()==null) return;

        videoHashMap.clear();
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (null == cursor) {
            return;
        }
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap();
            for (int i = 0; i < projection.length; i++) {
                map.put(projection[i], cursor.getString(i));
            }

            videoHashMap.put(map.get(MediaStore.Video.Media.DATA),map.get(MediaStore.Video.Media.DURATION));
        }

    }

    /**
     * 初始化图片列表
     */
    private void initAdapter() {
         mImageAdapter =new ImageListAdapter(getActivity(), true,mediaBeanList,videoHashMap);
        //添加条目点击事件
        mImageAdapter.setOnItemClickLitener(new ImageListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(CountItemViewHolder holder, int position, String thumbPath,
                                    String bigimagePath, List<String> size, List<String> thumbPathForViewPager,
                                    List imagePathForViewPager) {
                if (size == null || size.size() == 0) return;
                float sizeReal = StringUtil.splitSize(size.get(position));// MB/KB/0B
                openPlayVideoView(thumbPath, bigimagePath, sizeReal);
            }

            @Override
            public void onItemLongClick(CountItemViewHolder holder, int position) {

            }
        });
        if (recyclerView == null || mSwipeLayout == null) return;
        mSwipeLayout.setRefreshing(false);
        recyclerView.setAdapter(mImageAdapter);

    }

    /**
     * 调用系统自带的播放器来播放流媒体视频
     */
    private void openPlayVideoView(String thumbPath, String bigimagePath, float size) {
        if (UavConstants.IS_UAV_CONN) {
            //下载缩略图和视频
            String videoname = StringUtil.splitStrForVideo(bigimagePath);
            File file = new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoname);
            if (!file.exists()) {
                isOpenVideo = true;
                LinkedHashMap<String, String> stringStringHashMap = new LinkedHashMap<>();
                stringStringHashMap.put(thumbPath, bigimagePath);
                // 下载前判断手机存储空间
                // 手机存储剩余量
                long phoneInnerSpace = PhoneSectorSizeUtils.getAvailableExternalMemorySize();
                float phoneInnerSize = phoneInnerSpace / 1024f / 1024f;
                if (size > phoneInnerSize - 50) {
                    new MaterialDialogBuilderL(getActivity()).content(R.string.phone_storage_full).
                            cancelable(false).negativeText(R.string.disagree).
                            onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            }).show();
                }
                    downLoad(stringStringHashMap, null, size);

                stringStringHashMap.clear();
            } else {
                String absolutePath = file.getAbsolutePath();
                ZOPlayVideoActivity.playLocal(this.getActivity(), absolutePath, "", UiUtil.getString(R.string.edit));
                isOpenVideo = false;
            }
        } else {
//            Uri uri = Uri.fromFile(new File(bigimagePath));
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(uri, "video/mp4");
//            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            startActivity(intent);

            ZOPlayVideoActivity.playLocal(this.getActivity(), bigimagePath, "", UiUtil.getString(R.string.edit));


        }
    }

    /**
     * 初始化下拉刷新
     */
    private void initSwipeRefresh(final View view) {
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if (UavConstants.IS_UAV_CONN && !UiUtil.isFastRefresh(3000L)) {
////                    OkHttpUtils.getInstance().cancelTag(VideoFragment.this);
//                    clearOldData();
//                    initNetData(view, true);
//                } else {
                    if (!UavConstants.IS_UAV_CONN) {
                        initLoaclData();
                    }
                    mSwipeLayout.setRefreshing(false);
//                }
            }
        });
    }

    public void clearOldData() {
        mediaBeanList.clear();
        orderThumbSort.clear();
        orderSort.clear();
        orderTimeSort.clear();
        orderSizeSort.clear();
    }

    /**
     * 取消选择按钮
     */
    public void select_cancel() {
        if (mImageAdapter == null) return;

        mImageAdapter.setisFlagFromTitle(false);
        mImageAdapter.setisSelectTitle(true);
        mImageAdapter.selectNumMap.clear();
        mImageAdapter.notifyDataSetChanged();
    }

    /**
     * 删除
     */
    public void delete(LinkedHashMap imagePathLinkedHashMap) {
        if (mImageAdapter == null || !isDeleteEnable) return;
        if (imagePathLinkedHashMap.size() > 0) {
            multiChoiceDeleteFromWhere(imagePathLinkedHashMap);
        } else {
            UiUtil.showToast(UiUtil.getString(R.string.delete_info_empty));
        }
    }

    /**
     * 选择在哪里删除
     */
    private void multiChoiceDeleteFromWhere(final LinkedHashMap imagePathLinkedHashMap) {
        new MaterialDialogBuilderL(getActivity()).items(R.array.deletepicfromwhere)
                .itemsCallbackMultiChoice(new Integer[]{0},
                        new MaterialDialog.ListCallbackMultiChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, Integer[] which,
                                                       CharSequence[] text) {
                                if (which.length == 2) {
                                    LogUtil.Lee("全部删除");
                                    deleteSelectItem(imagePathLinkedHashMap, true, true);
                                } else if (which.length == 0) {
                                    LogUtil.Lee("未删除");
                                } else {
                                    for (int i = 0; i < which.length; i++) {
                                        j = which[i];
                                    }
                                    if (j == 0) {
                                        LogUtil.Lee("飞机删除");
                                        deleteSelectItem(imagePathLinkedHashMap, true, false);
                                    } else if (j == 1) {
                                        LogUtil.Lee("手机删除");
                                        deleteSelectItem(imagePathLinkedHashMap, false, true);
                                    }
                                }

                                return true;
                            }
                        })
                .positiveText(R.string.agree)
                .negativeText(R.string.disagree)
                .show();
    }

    /**
     * 删除选中的项目
     */

    private void deleteSelectItem(LinkedHashMap<String, String> imagePathLinkedHashMap, boolean drone,
                                  boolean phone) {
        ArrayList<String> picNameList = new ArrayList<>();
        Iterator iterator = imagePathLinkedHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            //飞机端要传视频MP4的地址去删除
            String name = StringUtil.splitStrForVideo(imagePathLinkedHashMap.get(imagePathKeySet));
            picNameList.add(name);

            TasksManager.getImpl().deleteDownLoadTask(imagePathLinkedHashMap.get(imagePathKeySet));
        }
        initDeleteItemRequest(picNameList, imagePathLinkedHashMap, drone, phone);
    }

    private void initDeleteItemRequest(final ArrayList<String> picNameList,
                                       final LinkedHashMap<String, String> imagePathLinkedHashMap, boolean drone, boolean phone) {
        if (mSwipeLayout!=null){
        mSwipeLayout.setEnabled(true);
        mSwipeLayout.setRefreshing(true);}
        if (drone) {
            isDeleteEnable = false;
            Iterator<String> iterator = picNameList.iterator();
            while (iterator.hasNext()) {
                final String pic_name = iterator.next();
              //  FileUtil.deleteFile(pic_name,true);
            }
        }
    }

    /**
     * 删除成功
     */
    private void deleteOK(LinkedHashMap<String, String> imagePathLinkedHashMap) {
        imagePathLinkedHashMap.clear();
        mImageAdapter.notifyDataSetChanged();
        mImageAdapter.setChangeSelectUI(new ImageListAdapter.ChangeSelectUI() {
            @Override
            public void changeUI(View view1, View download) {
                view1.setSelected(false);
            }

            @Override
            public void changeTitleUI(View view1) {
                ((TextView) view1).setText(R.string.select_cancel);
            }
        });
    }

    /**
     * 刷新
     */
    public void refresh() {
        if (mImageAdapter == null) return;
        isDeleteEnable = true;
        mediaBeanList.clear();
        activity.getSelectedTitleLinkedHashMap().clear();
        activity.getSelectedImagePathLinkedHashMap().clear();
        orderThumbSort.clear();
        orderSort.clear();
        orderTimeSort.clear();
        orderSizeSort.clear();
        mImageAdapter.setisFlagFromTitle(false);
        initNetData(view, true);
        if (mSwipeLayout!=null) {
            mSwipeLayout.setEnabled(false);
            mSwipeLayout.setRefreshing(true);
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSwipeLayout!=null){
                    mSwipeLayout.setRefreshing(false);
                    mSwipeLayout.setEnabled(true);
                }

            }
        }, 3000);
    }

    /**
     * 删除本地文件
     */
    public void deleteLocal(final LinkedHashMap<String, String> imagePathLinkedHashMap) {
        if (mImageAdapter == null) return;
        if (imagePathLinkedHashMap.size() > 0) {
            new MaterialDialogBuilderL(getActivity()).cancelable(false)
                    .content(R.string.delete_info)
                    .positiveText(R.string.agree)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            deleteLocalSelectItem(imagePathLinkedHashMap);
                        }
                    })
                    .negativeText(R.string.disagree)
                    .show();
        } else {
            UiUtil.showToast(UiUtil.getString(R.string.delete_info_empty));
        }
    }

    private void deleteLocalSelectItem(LinkedHashMap<String, String> imagePathLinkedHashMap) {
        ArrayList<String> videoNameList = new ArrayList<>();
        ArrayList<String> picNameList = new ArrayList<>();
        Iterator iterator = imagePathLinkedHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            String videoName = imagePathLinkedHashMap.get(imagePathKeySet);
            String picame = imagePathKeySet;
            picNameList.add(picame);
            videoNameList.add(videoName);
        }

        FileUtil.delelteFile(picNameList, videoNameList, false);
        imagePathLinkedHashMap.clear();
        refreshLocal();
    }

    /**
     * 删除飞机的同时删除本地
     */
    private void deleteLocalWhenNet(LinkedHashMap<String, String> imagePathLinkedHashMap,
                                    boolean drone) {
        ArrayList<String> picNameList = new ArrayList<>();
        ArrayList<String> videoNameList = new ArrayList<>();
        Iterator iterator = imagePathLinkedHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            String videoName = StringUtil.splitStrForVideo(imagePathLinkedHashMap.get(imagePathKeySet));
            String picame = StringUtil.splitStrForVideo(imagePathKeySet);
            picNameList.add(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + picame);
            videoNameList.add(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName);
        }
        FileUtil.delelteFile(picNameList, videoNameList, false);
        imagePathLinkedHashMap.clear();
        if (drone) return;
        refresh();
    }

    /**
     * 刷新
     */
    public void refreshLocal() {
        mediaBeanList.clear();
        orderThumbSort.clear();
        orderSort.clear();
        orderTimeSort.clear();
        orderSizeSort.clear();
        activity.getSelectedTitleLinkedHashMap().clear();
        activity.getSelectedImagePathLinkedHashMap().clear();
        initLoaclData();
        if (mSwipeLayout!=null)
        mSwipeLayout.setEnabled(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mSwipeLayout!=null)
                mSwipeLayout.setRefreshing(false);
            }
        }, 3000);
    }

    /**
     * 下载
     */
    public void downLoad(LinkedHashMap<String,String>  imagePathLinkedHashMap,
                         @Nullable ArrayList<String> fileSizeList, float size) {
        if (mImageAdapter == null) return;
//        List<String> overLists= new ArrayList<>();
        Log.e("size","sizeReal1="+size);

//        Iterator iterator = imagePathLinkedHashMap.keySet().iterator();
//        while (iterator.hasNext()) {
//            String imagePathKeySet = (String) iterator.next();
//            String url = imagePathLinkedHashMap.get(imagePathKeySet);
//
//            float sizeReal = StringUtil.splitSize(currentSizeList.get(currentBigImageList.indexOf(url)));// MB/KB/0B
//            Log.e("size","sizeReal="+sizeReal);
//            totalSize += sizeReal;
//        }
//
//        for (String  key :overLists){
//            imagePathLinkedHashMap.remove(key);
//        }

        // 下载前判断手机存储空间
        // 手机存储剩余量
        long phoneInnerSpace = PhoneSectorSizeUtils.getAvailableExternalMemorySize();
        float phoneInnerSize = phoneInnerSpace / 1024f / 1024f;

        if ((totalSize + size) > phoneInnerSize - 50 && imagePathLinkedHashMap.size() > 0) {
            new MaterialDialogBuilderL(getActivity()).content(R.string.phone_storage_full).
                    cancelable(false).negativeText(R.string.disagree).
                    onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    }).show();
        } else {
            //执行下载动作
            if (imagePathLinkedHashMap.size() > 0) {
                DBHelper db = DBHelper.getInstance(getActivity());
                TasksManager.getImpl().initTaskUrlMap(getActivity(), imagePathLinkedHashMap);

                if (db.getAllTasks().size() <= 0) {
                    UiUtil.showToast(UiUtil.getString(R.string.file_has_downloaded));
                    return;
                }
                mImageAdapter.notifyDataSetChanged();

            } else {
                if (UavConstants.ISDOWNLOADFINISH) {
                    UiUtil.showToast(UiUtil.getString(R.string.load_info_empty));
                    return;
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isStop = true;
        mHandler.removeCallbacksAndMessages(null);
//        EventBus.getDefault().unregister(this);
        unbinder.unbind();
//        OkHttpUtils.getInstance().cancelTag(VideoFragment.this);
//        if (null != task) {
//            task.cancel();
//        }
    }









    public void selectOP() {
        if (mImageAdapter == null) return;
        mImageAdapter.setisSelectTitle(true);
        mImageAdapter.setisFlagFromTitle(false);
        mImageAdapter.selectNumMap.clear();
        mImageAdapter.notifyDataSetChanged();
    }






}
