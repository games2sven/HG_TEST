package com.highgreat.education.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dash.Const;
import com.highgreat.education.R;
import com.highgreat.education.activity.HandlePicActivity;
import com.highgreat.education.activity.SmallpicActivity;
import com.highgreat.education.adapter.ImageListAdapter;
import com.highgreat.education.bean.DownloadBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.ImagesBean;
import com.highgreat.education.bean.MediaBean;
import com.highgreat.education.bean.SerializableList;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dao.DBHelper;
import com.highgreat.education.dialog.MaterialDialogBuilderL;
import com.highgreat.education.holder.CountItemViewHolder;
import com.highgreat.education.manager.ImageTaskManager;
import com.highgreat.education.manager.ThreadManager;
import com.highgreat.education.utils.FileUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.MultiDownLoadUtil;
import com.highgreat.education.utils.PhoneSectorSizeUtils;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.TimeUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.utils.YMComparator;
import com.runtop.other.FileUtils;
import com.runtop.other.RTDeviceCmdUtils;
import com.runtop.presenter.RtFileSDImagePresenter;
import com.runtop.presenter.RtFileSDVideoPresenter;
import com.runtop.presenter.inter.RtFileSDImageView;
import com.tonicartos.superslim.LayoutManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Do Good App
 * 项目名称：简单的可多选相册
 * 类描述： 创建人：mac-likh
 * 创建时间：16/2/26 22:49
 * 修改人：mac-likh
 * 修改时间：16/2/26 22:49
 * 修改备注：
 */
public class PictureFragment extends  android.support.v4.app.Fragment  {
    /**
     * 本地文件扫面成功
     */
    private final static int SCAN_LOCAL_FILE_FINISH = 300;
    private final static int SCAN_LOCAL_EMPTY       = 400;
    private final static int SCAN_LOCAL_OK          = 500;


    Stack<String> needDownloadName = new Stack<>();

    private static int lastPosition = 0;
    @BindView(R.id.ll_empty)
    LinearLayout       llEmpty;
    @BindView(R.id.tv_empty)
    TextView           tvEmpty;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    int pageNum = 0;
    int deleteNum;

//    private ArrayList<String> currentThumbImageList = new ArrayList<>();//当前图片集合
//    private ArrayList<String> currentBigImageList   = new ArrayList<>();//当前图片集合
//    private ArrayList<String> currentImageTimeList  = new ArrayList<>();//当前图片集合
//    private ArrayList<String> currentSizeList       = new ArrayList<>();//当前图片集合

    List<MediaBean> mediaBeanList = new ArrayList<>();
    private Map<String, String> orderSort      = new LinkedHashMap<>();
    private Map<String, String> orderTimeSort  = new LinkedHashMap<>();
    private Map<String, String> orderThumbSort = new LinkedHashMap<>();
    private Map<String, String> orderSizeSort  = new LinkedHashMap<>();
    private SmallpicActivity activity;
    private RecyclerView recyclerView;
    private ImageListAdapter mImageAdapter;
    private View             view;
    private boolean isLoacl       = true;
    private boolean isNet         = true;
    private float   totalSize     = 0;
    private Integer j             = 0;
    private int     clickPosition = 0;
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
                    if (llEmpty == null) return;
                    llEmpty.setVisibility(View.VISIBLE);
                    break;
                case SCAN_LOCAL_OK:
                    if (llEmpty == null) return;
                    llEmpty.setVisibility(View.GONE);
                    break;
                 default:
                     break;
            }
        }
    };
    Runnable mRunnable = new Runnable() {
        public boolean isStop = false;

        @Override
        public void run() {
            List<File> sortList = new ArrayList<File>();
            String filePath = UavConstants.BIG_PIC_ABSOLUTE_PATH;
            File file = new File(filePath);
            File[] subFile = file.listFiles();
            if (subFile != null && subFile.length > 0) {
                for (int i = 0; i < subFile.length; i++) {
                    String filename = subFile[i].getName();
                    if (filename.trim().toLowerCase().endsWith(".temp")) {
                        FileUtil.delelteItemFile(filePath + "/" + filename);
                    }
                }
            }
            if (subFile == null || subFile.length == 0) {
                mHandler.sendEmptyMessage(SCAN_LOCAL_EMPTY);
                return;
            } else{
                mHandler.sendEmptyMessage(SCAN_LOCAL_OK);
            }
            for (File f : subFile) {
                if (isStop) {
                    return;
                }
                sortList.add(f);
            }
            Collections.sort(sortList, new YMComparator());

            for (int i = 0; i < sortList.size(); i++) {
                if (isStop) {
                    return;
                }
                String filename = sortList.get(i).getName();
                // 判断是否为jpg结尾
                if (filename.trim().toLowerCase().endsWith(".jpg")) {
                    String path = sortList.get(i).getAbsolutePath();
                    long times = sortList.get(i).lastModified();
                    String time = TimeUtil.paserTimeToYM(times);
                    long fileLength = sortList.get(i).length();
//                    if (!currentBigImageList.contains(path)) {
                        //小数点后保留一位
                        float localFileSize = (float) (Math.round(fileLength / 1024f / 1024f * 10)) / 10;
                    MediaBean mediaBean =new MediaBean(false,path,path,time,localFileSize + "M");
                    mediaBeanList.add(mediaBean);
//                    }
                }
            }
            if (isStop) {
                return;
            }
            mHandler.sendEmptyMessage(mediaBeanList.size()>0?SCAN_LOCAL_FILE_FINISH:SCAN_LOCAL_EMPTY);
        }
    };
    private LayoutManager mLayoutManager;
    private int     lastOffset     = 0;
    private boolean isDeleteEnable = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            if (llEmpty == null) return;
            int code = eventCenter.getEventCode();
            Object data = eventCenter.getData();
            switch (code) {
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
//
//                    break;
                case EventBusCode.CODE_ADD_NEED_DOWNLOAD_PIC:
                    String  pic = (String) data;
                    if (!needDownloadName.contains(pic))
                    needDownloadName.push(pic);

                    if (!needDownloadName.empty()) {
                        startDonwnLoadPic();
                    }

                    break;
                 default:
                     break;
            }
        }
    }


    boolean isDownLoadPic =false;



    public  void  startDonwnLoadPic(){
        if (isDownLoadPic) return;
        isDownLoadPic=true;
        String picName =needDownloadName.peek();
        LogUtil.e("startDonwnLoadPic"+needDownloadName.peek());
        File file = new File(Const.PHONE_SNAPSHOT_PATH + "/" + picName);
        if (file.exists()){
            needDownloadName.pop();
            if (!needDownloadName.empty()){
                isDownLoadPic=false;
                startDonwnLoadPic();
            }

        }else{
//            rtPresenter.downLoadImageFile(picName);
        }


    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.small_pic_gallery, null);
        unbinder=   ButterKnife.bind(this, view);
        initSwipeRefresh(view);
        initRecyclerView(view);
        UiUtil.initAutoRefresh(mSwipeLayout);
        activity = (SmallpicActivity) getActivity();
//        if (!UavConstants.IS_UAV_CONN) {
        if (!UavConstants.isSupportSDCard)
        refreshLocal();
//        }
        return view;
    }

    private void initRecyclerView(View view) {
        //配置recyclerView
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_fragment_photo_gallery);
        mLayoutManager = new LayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);//设置布局管理器
        recyclerView.setHasFixedSize(true);//保持固定的大小，如果可以确定每个Item的高度是固定的，设置这个选项可以提高性能
        recyclerView.addOnScrollListener(new RecyclerViewListener(mLayoutManager));
    }



    /**
     * 展示本地数据
     */
    private void initLoaclData() {
        clearOldData();
        ThreadManager.getInstance().addWorkTempPool(mRunnable);
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    /**
     * 初始化分页数据
     */
    private void initNetData(final View view, final boolean b) {
//        synchronized (this) {
//            clearOldData();
//            OkHttpUtils.get().url(UavConstants.URL_PIC).tag(PictureFragment.this).build().execute(new ImangeBeanCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//                    if (call.isCanceled()) {
//                        return;
//                    }
//                    LogUtil.Lee("http onError ");
//
//                    UiUtil.showToast(R.string.net_erro);
//                    mSwipeLayout.setRefreshing(false);
//                    EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_ERROR));
//                }
//
//                @Override
//                public void onResponse(ImagesBean imagesBean, int id) {
//                    if (imagesBean == null) {
//                        return;
//                    }
//                    int totalPage = imagesBean.getTotalPage();
//                    LogUtil.Lee("http onResponse = " + totalPage);
//                    if (totalPage != 0) {
//                        EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_OK));
//                        pasePic(imagesBean, totalPage, b);
//                        //拼接连续请求地址
//                        for (int i = 2; i < totalPage + 1; i++) {
//
//                            initImageDataRequest(UavConstants.URL_PIC_PAGE + i, totalPage, view, b);
//                        }
//                    } else {
//                        EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_EMPTY));
//                        initAdapter(true, b);
//                        mSwipeLayout.setRefreshing(false);
//                    }
//                }
//            });
//        }
    }

    private void initImageDataRequest(String pageUrl, final int totalPage, final View view,
                                      final boolean b) {
//        OkHttpUtils.get()
//                .url(pageUrl)
//                .tag(PictureFragment.this)
//                .build()
//                .execute(new ImangeBeanCallback() {
//                    @Override
//                    public void onError(Call call, Exception e, int id) {
//                        if(getActivity() != null && !getActivity().isFinishing()){
//                            EventBus.getDefault().post(new EventCenter(UavConstants.CODE_REQUEST_ERROR));
//                            mSwipeLayout.setRefreshing(false);
//                        }
//                    }
//
//                    @Override
//                    public void onResponse(ImagesBean imagesBean, int id) {
//                        if(getActivity() != null && !getActivity().isFinishing()){
//                            pasePic(imagesBean, totalPage, b);
//                        }
//                    }
//                });
    }

    private void pasePic(ImagesBean imagesBean, int totalPage, boolean b) {
        pageNum++;
        List<ImagesBean.DataEntity> dataList = imagesBean.getData();
        if (dataList == null || dataList.size() <= 0) return;
        for (int i = 0; i < dataList.size(); i++) {
            String thumbPath = dataList.get(i).getThumb();
            String bigPath = dataList.get(i).getPath();
            String time = dataList.get(i).getCreated();
            String size = dataList.get(i).getSize();
            String title = dataList.get(i).getTitle();

            //"1970-01-01 00:59:18"
            String times = StringUtil.stringTimePattern(time);
            if (!orderSort.containsValue(bigPath)){
            orderThumbSort.put(title, thumbPath);
            orderSort.put(title, bigPath);
            orderTimeSort.put(title, times);
            orderSizeSort.put(title, size);
            }
        }
        if (pageNum == totalPage) {
            pageNum = 0;
//            Map<String, String> stringTimebMap = StringUtil.sortMapStringByKey(orderTimeSort);
//            for (Map.Entry<String, String> entry : orderTimeSort.entrySet()) {
//                currentImageTimeList.add(entry.getValue());
//            }
////            Map<String, String> stringThumbMap = StringUtil.sortMapStringByKey(orderThumbSort);
//            for (Map.Entry<String, String> entry : orderThumbSort.entrySet()) {
//                currentThumbImageList.add(entry.getValue());
//            }
////            Map<String, String> stringListMap = StringUtil.sortMapStringByKey(orderSort);
//            for (Map.Entry<String, String> entry : orderSort.entrySet()) {
//                currentBigImageList.add(entry.getValue());
//            }
////            Map<String, String> stringSizeMap = StringUtil.sortMapStringByKey(orderSizeSort);
//            for (Map.Entry<String, String> entry : orderSizeSort.entrySet()) {
//                currentSizeList.add(entry.getValue());
//            }
            initAdapter();
        }
    }

    /**
     * 初始化图片列表
     */
    private void initAdapter() {
       mImageAdapter = new ImageListAdapter(getActivity(), false,mediaBeanList,null);
        //添加条目点击事件
        mImageAdapter.setOnItemClickLitener(new ImageListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(CountItemViewHolder holder, int position, String thumbPath,
                                    String bigimagePath, List<String> sizeList, List<String> thumbPathForViewPager,
                                    List imagePathForViewPager) {
                clickPosition = position;
                //进入图片处理
                //传递数据
                LogUtil.d("setOnItemClickLitener", "sizeList.size() = " + bigimagePath);
                if (sizeList.size() == 0) return;//防止刷新的时候点击crash
                openHandleView(position, bigimagePath, sizeList, thumbPathForViewPager,
                        imagePathForViewPager);
//                rtPresenter.downLoadImageFile(bigimagePath);
            }

            @Override
            public void onItemLongClick(CountItemViewHolder holder, int position) {
                //长按事件
            }
        });

        if (recyclerView == null || mSwipeLayout == null) return;
        recyclerView.setAdapter(mImageAdapter);
        if (lastPosition == 1) {
            recyclerView.scrollToPosition(clickPosition);
        } else {
            recyclerView.scrollToPosition(lastPosition);
        }
        clickPosition = 0;
        mSwipeLayout.setRefreshing(false);
        LogUtil.Lee("lastPosition " + lastPosition + "  clicklposition " + clickPosition);
    }

    /**
     * 查看大图
     */
    private void openHandleView(int position, String bigimagePath, List<String> sizeList,
                                List<String> thumbPathForViewPager, List imagePathForViewPager) {
        final SerializableList myList = new SerializableList();
        final SerializableList mythumbList = new SerializableList();
        final SerializableList mySizeList = new SerializableList();
        myList.setPicList(imagePathForViewPager);
        mythumbList.setPicList(thumbPathForViewPager);
        mySizeList.setPicList(sizeList);
        Bundle bundle = new Bundle();
        bundle.putString("path", bigimagePath);
        bundle.putInt("page", position);
        bundle.putSerializable("pathList", myList);
        bundle.putSerializable("thumbpathList", mythumbList);
        bundle.putSerializable("mySizeList", mySizeList);
        Intent intent = new Intent(activity, HandlePicActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
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
//                if (UavConstants.IS_UAV_CONN && !UiUtil.isFastRefresh(4000L)) {
//                    OkHttpUtils.getInstance().cancelTag(PictureFragment.this);
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

    private void clearOldData() {
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
    public void delete(final LinkedHashMap imagePathLinkedHashMap) {
        if (mImageAdapter == null || !isDeleteEnable) return;
        if (imagePathLinkedHashMap.size() > 0) {
            multiChoiceDeleteFromWhere(imagePathLinkedHashMap);
        } else {
            UiUtil.showToast(R.string.delete_info_empty);
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
     * 确定删除么
     *
     * @param drone 飞机删除
     * @param phone 手机删除
     */
    private void sureTodeletePic(final LinkedHashMap imagePathLinkedHashMap, final boolean drone,
                                 final boolean phone) {
        new MaterialDialogBuilderL(getActivity()).cancelable(false)
                .content(R.string.delete_info)
                .positiveText(R.string.agree)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                        deleteSelectItem(imagePathLinkedHashMap, drone, phone);
                    }
                })
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
            //飞机端要传大图的地址去删除
            String name = StringUtil.splitStrForPic(imagePathLinkedHashMap.get(imagePathKeySet));

            picNameList.add(name);
        }
        initDeleteItemRequest(picNameList, imagePathLinkedHashMap, drone, phone);
    }

    ArrayList<String> deleteName =new ArrayList<>();

    private void initDeleteItemRequest(ArrayList<String> picNameList,
                                       final LinkedHashMap<String, String> imagePathLinkedHashMap, boolean drone, boolean phone) {
        mSwipeLayout.setEnabled(true);
        mSwipeLayout.setRefreshing(true);
        if (drone) {
            isDeleteEnable = false;
            deleteName.clear();
            deleteName.addAll(picNameList);
           if (deleteName.size()>0) {
               String pic_name = deleteName.get(0);
//               rtPresenter.deleteFile(pic_name);
               deleteName.remove(0);
           }
        }
        if (phone) {
            deleteLocalWhenNet(imagePathLinkedHashMap, drone);
        }
    }

    /**
     * 刷新
     */
    public void refresh() {
        if (mImageAdapter == null) return;
        isDeleteEnable = true;
        activity.getSelectedTitleLinkedHashMap().clear();
        activity.getSelectedImagePathLinkedHashMap().clear();
        initNetData(view, true);
        mSwipeLayout.setEnabled(false);
        mSwipeLayout.setRefreshing(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                mSwipeLayout.setEnabled(true);
            }
        }, 3000);
    }

    /**
     * 删除本地文件
     */
    public void deleteLocal(final LinkedHashMap<String, String> imagePathLinkedHashMap) {

        if (mImageAdapter == null) return;
        Activity activity = getActivity();
//    LanguageUtils.getCurrentLanguage(activity);
        if (imagePathLinkedHashMap.size() > 0) {
            new MaterialDialogBuilderL(activity).cancelable(false)
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
            UiUtil.showToast(R.string.delete_info_empty);
        }
    }

    private void deleteLocalSelectItem(LinkedHashMap<String, String> imagePathLinkedHashMap) {
        ArrayList<String> picNameList = new ArrayList<>();
        Iterator iterator = imagePathLinkedHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            String name = imagePathKeySet;
            picNameList.add(name);
        }

        FileUtil.delelteFile(picNameList, null, true);
        imagePathLinkedHashMap.clear();
        refreshLocal();
    }

    /**
     * 删除飞机的同时删除本地
     */
    private void deleteLocalWhenNet(LinkedHashMap<String, String> imagePathLinkedHashMap,
                                    boolean drone) {
        ArrayList<String> picNameList = new ArrayList<>();
        Iterator iterator = imagePathLinkedHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            //飞机端要传大图的地址去删除
            String name = StringUtil.splitStrForPic(imagePathLinkedHashMap.get(imagePathKeySet));
            picNameList.add(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name);
        }
        FileUtil.delelteFile(picNameList, null, true);
        imagePathLinkedHashMap.clear();
        if (drone) return;
        refresh();
    }

    /**
     * 刷新
     */
    public void refreshLocal() {
        activity.getSelectedTitleLinkedHashMap().clear();
        activity.getSelectedImagePathLinkedHashMap().clear();
        mediaBeanList.clear();
        orderThumbSort.clear();
        orderSizeSort.clear();
        orderSort.clear();
        orderTimeSort.clear();
        initLoaclData();
        mSwipeLayout.setEnabled(true);
        mSwipeLayout.setRefreshing(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
//        OkHttpUtils.getInstance().cancelTag(PictureFragment.this);
    }

    /**
     * 下载照片文件
     */
    public void downLoad(LinkedHashMap<String, String> imagePathLinkedHashMap,
                         ArrayList<String> fileSizeList) {
        if (mImageAdapter == null) return;
        if (fileSizeList != null) {
            MultiDownLoadUtil.cancelSingleTaskDownLoadPic();
            totalSize = 0;
            for (String singleSize : fileSizeList) {
                float sizeReal = StringUtil.splitSize(singleSize);// MB/KB/0B
                totalSize += sizeReal;
            }
        }
        // 下载前判断手机存储空间
        // 手机存储剩余量
        long phoneInnerSpace = PhoneSectorSizeUtils.getAvailableExternalMemorySize();
        float phoneInnerSize = phoneInnerSpace / 1024f / 1024f;
        if (totalSize > phoneInnerSize - 50 && imagePathLinkedHashMap.size() > 0) {
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
                ImageTaskManager.getImpl().initTaskUrlMap(getActivity(), imagePathLinkedHashMap);

                if (db.getAllImageTasks().size() <= 0) {
                    UiUtil.showToast(UiUtil.getString(R.string.file_has_downloaded));
                    return;
                }
                mImageAdapter.notifyDataSetChanged();

            } else {
                if (UavConstants.ISDOWNLOADIMAGEFINISH) {
                    UiUtil.showToast(R.string.load_info_empty);
                    return;
                }
            }
        }
    }

    /**
     * 选择操作
     */
    public void selectOP() {
        if (mImageAdapter == null) return;
        mImageAdapter.setisSelectTitle(true);
        mImageAdapter.setisFlagFromTitle(false);
        mImageAdapter.selectNumMap.clear();
        mImageAdapter.notifyDataSetChanged();
    }












    int  firstVisibleItem ;
    int  visibleItemCount;
     class RecyclerViewListener extends RecyclerView.OnScrollListener {

        private LayoutManager mLayoutManager;

        public RecyclerViewListener(LayoutManager layoutManager) {
            mLayoutManager = layoutManager;
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //在这里进行第二次滚动（最后的100米！）
            View topView = recyclerView.getChildAt(0);

            //获取可视的第一个view
            lastPosition = mLayoutManager.getPosition(topView);  //得到该View的数组位置
            LogUtil.e("onScrolled===  "+lastPosition);
            firstVisibleItem=lastPosition;
            visibleItemCount=recyclerView.getChildCount();
//            if (rtPresenter!=null){
//
//                rtPresenter.setResetStack(0,lastPosition);
//            }
        }

         @Override
         public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
             super.onScrollStateChanged(recyclerView, newState);
             LogUtil.e("onScrollStateChanged===   "+newState);

         }
     }
}
