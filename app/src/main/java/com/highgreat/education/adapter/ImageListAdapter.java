package com.highgreat.education.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


import com.dash.Const;
import com.highgreat.education.R;
import com.highgreat.education.activity.SmallpicActivity;
import com.highgreat.education.bean.ImageTasksManagerModel;
import com.highgreat.education.bean.MediaBean;
import com.highgreat.education.bean.TasksManagerModel;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.holder.CountItemViewHolder;
import com.highgreat.education.manager.ImageDownLoadCallBackManager;
import com.highgreat.education.manager.ImageTaskManager;
import com.highgreat.education.manager.TaskDownLoadCallBackManager;
import com.highgreat.education.manager.TasksManager;
import com.highgreat.education.utils.FileUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.TimeUtil;
import com.highgreat.education.widget.NetworkImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tonicartos.superslim.GridSLM;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.lxw.dtl.utils.DTLUtils;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/25 09:55
 * 修改人：mac-likh
 * 修改时间：16/1/25 09:55
 * 修改备注：
 */
public class ImageListAdapter extends RecyclerView.Adapter<CountItemViewHolder> {

    private static final int                  VIEW_TYPE_HEADER  = 0x01;
    private static final int                  VIEW_TYPE_CONTENT = 0x00;
    private final LinkedHashMap<String, String> videoHashMap;
    public               Map<String, Integer> selectNumMap      ;//选择数量统计
    public List<LineItem>    mItems;  //列表数据集合
//    public ArrayList<String> imageList;//小图片集合
//    public ArrayList<String> bigImageList;//大图片集合
//    public ArrayList<String> imageTimeList;//大图片时间集合
//    public ArrayList<String> currentSizeList;//大小
    public ArrayList<String> fileSizeList;//大小
    public ArrayList<String> downThumbKey;//大小
    public ChangeSelectUI    changeSelectUI;
    /**
     * 设置单选的UI变化
     *
     * @param holder
     * @param thumbPath
     * @param bigimagePath
     * @param timeKey
     */
    int selectNum = 0;
    private int margin = UavConstants.MARGIN;
    private int width  = UavConstants.GALLERY_WIDTH;//屏幕宽度
    private int j      = 0;
    private Map<String, List<String>> stringListMap;
    private Map<String, List<String>> picMapWithTime = new HashMap<>();//时间集合
    private LinkedHashMap<String, String> imagePathLinkedHashMap;//照片选择的集合
    private LinkedHashMap<String, String> titleLinkedHashMap;//全选标题集合
    private List<String> imagePathForViewPager = new ArrayList<>();//大图地址--滑动
    private List<String> thumbPathForViewPager = new ArrayList<>();//缩略图地址--滑动
    private List<String> sizePathForViewPager  = new ArrayList<>();//图片大小--滑动
    private List<String> picPath;
    private List<String> picPathControlTitle;
    private Context      context;
    private boolean isSelectTitle   = true;
    private boolean isFlagFromTitle = false;
    private boolean isVideo;
    /**
     * 设置条目点击事件
     *
     * @param holder
     */
    private int num          = 0;
    private int itemViewType = 0;
    private OnItemClickLitener mOnItemClickLitener;
    List<MediaBean>  mList;
    public ImageListAdapter(Context context,boolean  isVideo, List<MediaBean> mList, LinkedHashMap<String,String> videoHashMap) {
        this.context = context;
        this.isVideo = isVideo;
//        this.imageList = imageList;
//        this.bigImageList = bigImageList;
//        this.imageTimeList = imageTimeList;
//        this.currentSizeList = currentSizeList;
        this.mList=mList;
        this.videoHashMap=videoHashMap;

        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;

        mItems = new ArrayList<>();
        if (context != null) {
                imagePathLinkedHashMap = ((SmallpicActivity) context).getSelectedImagePathLinkedHashMap();
                 selectNumMap = ((SmallpicActivity) context).getSelectedTitleImagePath();
                titleLinkedHashMap = ((SmallpicActivity) context).getSelectedTitleLinkedHashMap();
                fileSizeList = ((SmallpicActivity) context).getfileSizeList();
            downThumbKey=new ArrayList<>();
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mList.size(); i++) {
            String time = mList.get(i).currentImageTime;
            sb.append(mList.get(i).currentThumbImage)
                    .append("@")
                    .append(mList.get(i).currentOrgPath)
                    .append("@")
                    .append(mList.get(i).currentSize);
//            String path = sb.toString();//thumbPath + "@" + bigPath + "@" + size;//大图加小图
            getListByTime(time).add(sb.toString());
            sb.delete(0, sb.length());
        }
        if (picMapWithTime.size() > 0) {
            stringListMap = StringUtil.sortMapByKey(picMapWithTime);

            for (Map.Entry entry : stringListMap.entrySet()) {
                String key = entry.getKey().toString();
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = j + headerCount;
                headerCount += 1;
                mItems.add(new LineItem(key, null, null, null, true, sectionManager, sectionFirstPosition, false));

                List<String> values = (List) entry.getValue();
                for (String value : values) {
                    j++;
                    String[] split = value.split("@");
                    if (split != null && split.length > 0) { //fixed by yangchao 2016/10/9 :防止数组越界异常
                        mItems.add(new LineItem(key, split[0], split[1], split[2], false, sectionManager,
                                sectionFirstPosition, isVideo));
                    }
                }
            }
        }
        //设置imageview的固定宽高
        if (width <= 0) {
            width = UavConstants.COMMON_WIDTH;
        }
    }

    public void setisFlagFromTitle(boolean isFlagFromTitle) {
        this.isFlagFromTitle = isFlagFromTitle;
    }

    public void setisSelectTitle(boolean isSelectTitle) {
        this.isSelectTitle = isSelectTitle;
    }

    private List<String> getListByTime(String time) {
        if (picMapWithTime.containsKey(time)) {
            return picMapWithTime.get(time);
        } else {
            List<String> list = new ArrayList<>();
            picMapWithTime.put(time, list);
            return list;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems == null || mItems.size() <= 0) //fixed by yangchao 2016/10/9 :防止数组越界异常
            return 0;
        return mItems.get(position).isHeader ? VIEW_TYPE_HEADER : VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public CountItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == VIEW_TYPE_HEADER) {
            view = DTLUtils.inflate(R.layout.small_pic_header_view, parent, false);
        } else {
            view = DTLUtils.inflate(R.layout.small_pic_gallery_item, null);
        }
        return new CountItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CountItemViewHolder holder, int position) {

        if (mItems == null || mItems.size() <= 0) //fixed by yangchao 2016/10/8 :防止数组越界异常
            return;
        final LineItem item = mItems.get(position);
        final String timeKey = item.timeKey;
        final View itemView = holder.itemView;
        int itemViewType = getItemViewType(position);

        initLayout(item, itemView);
        //标题
        if (itemViewType == VIEW_TYPE_HEADER) {

            refreshTitleSelectUI(holder, timeKey);
            holder.bindItem(timeKey);
            timeHeaderSelect(holder, timeKey);

        } else {

            final String imagePath = item.thumbPath;//图片路径
            final String bigimagePath = item.bigPicPath;//图片路径

            initPicSize(holder, item);
            displayImage(holder, imagePath,bigimagePath, holder.ivImage);


            refreshMainUI(holder);
            refreshSelectUI(holder, imagePath, position);
            setItemClickEvent(holder, imagePath, bigimagePath, timeKey);
            refreshShadeClickUI(holder, imagePath, bigimagePath, timeKey);

           // refreshDownloadingProgress(holder, imagePath, bigimagePath, timeKey);
            checkFileExistInPhone(holder, item);

        }
    }

    /**
     * 时间头 全选/取消
     *
     * @param holder
     * @param timeKey
     */
    private void timeHeaderSelect(final CountItemViewHolder holder, final String timeKey) {

        if (changeSelectUI != null) {
            changeSelectUI.changeTitleUI(holder.select_intitle);
        }
        holder.select_intitle.setVisibility(UavConstants.isSelectState ? View.GONE : View.VISIBLE);
        //选择框的状态
        holder.select_intitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleLinkedHashMap.containsKey(timeKey) && picPath != null && picPath.size() > 0) {
                    cancelSelectTimeAll(holder, timeKey);
                } else {
                    selectTimeAll(timeKey, holder);
                }
            }
        });
    }

    /**
     * 下载进度
     *
     * @param holder
     * @param imagePath
     * @param bigimagePath
     * @param timeKey
     */
    private void refreshDownloadingProgress(CountItemViewHolder holder, String imagePath, String bigimagePath, String timeKey) {
        if (isVideo) {
            final TasksManagerModel model = TasksManager.getImpl().get(bigimagePath);
            if (model == null || new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + StringUtil.splitStrForVideo(bigimagePath)).exists()) {
                TasksManager.getImpl().removeTaskForViewHolder(-1, bigimagePath);
                holder.mProgress.setVisibility(View.GONE);
                return;
            }

            holder.update(bigimagePath, model.getId(), timeKey);
            downLoadChangeUi(holder, timeKey, imagePath);
            holder.mProgress.setTag(model.getId());
            holder.mProgress.setVisibility(View.VISIBLE);
            holder.refreshDownloading();
            TasksManager.getImpl().updateViewHolder(holder.id, holder);
            TaskDownLoadCallBackManager.getInstance(context).toStartDownLoadTask(holder); //开始任务

        } else {
            final ImageTasksManagerModel model = ImageTaskManager.getImpl().get(bigimagePath);
            //如果文件已经存在
            if (model == null || new File(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + StringUtil.splitStrForPic(bigimagePath)).exists()) {
                ImageTaskManager.getImpl().removeTaskForViewHolder(-1, bigimagePath);
                holder.mProgress.setVisibility(View.GONE);
                return;
            }

            holder.update(bigimagePath, model.getId(), timeKey);
            downLoadChangeUi(holder, timeKey, imagePath);
            holder.mProgress.setTag(model.getId());
            holder.mProgress.setVisibility(View.VISIBLE);
            holder.refreshDownloading();
            ImageTaskManager.getImpl().updateViewHolder(holder.id, holder);
            ImageDownLoadCallBackManager.getInstance(context).toStartDownLoadTask(holder); //开始任务
        }
    }

    /**
     * 文件大小
     *
     * @param holder
     * @param item
     */
    public void initPicSize(CountItemViewHolder holder, LineItem item) {
        String size = item.size;
        holder.pic_size.setText(size);
        holder.pic_size.setVisibility("0.0M".equals(size) ? View.GONE : View.VISIBLE);
    }

    /**
     * 当天单个取消
     */
    private void cancelSelectTitle(CountItemViewHolder holder, String timeKey) {
        if (holder.mTextView != null) {
            holder.mTextView.setTag(timeKey);
        }
        titleLinkedHashMap.remove(timeKey);
        setisSelectTitle(true);
        setisFlagFromTitle(false);
        notifyDataSetChanged();
    }

    /**
     * 当天全部取消
     */
    private void cancelSelectTimeAll(CountItemViewHolder holder, String timeKey) {

        if (holder.mTextView != null) {
            holder.mTextView.setTag(timeKey);
        }
        titleLinkedHashMap.remove(timeKey);
        picPath = stringListMap.get(timeKey);
        selectNumMap.remove(timeKey);
        //处理这个清理的动作
        for (String value : picPath) {
            String[] split = value.split("@");
            if (split != null && split.length > 0) { //fixed by yangchao 2016/10/9 :防止数组越界异常
                imagePathLinkedHashMap.remove(split[0]);
            }
        }
        setisSelectTitle(true);
        setisFlagFromTitle(false);
        notifyDataSetChanged();
    }

    /**
     * 当天全选
     */
    private void selectTimeAll(String timeKey, CountItemViewHolder holder) {
        setisSelectTitle(false);
        setisFlagFromTitle(true);
        picPath = stringListMap.get(timeKey);
        if (holder.mTextView != null) {
            holder.mTextView.setTag(timeKey);
        }
        titleLinkedHashMap.put(timeKey, timeKey);
        selectNumMap.put(timeKey, picPath.size());
        notifyDataSetChanged();
    }

    /**
     * 更改标题的选择状态
     */

    private void refreshTitleSelectUI(CountItemViewHolder holder, String timeKey) {
        if (((SmallpicActivity) context).getisRefleshAllState()) {
            holder.mTextView.setTag("");
        }   //清除路径，不走下边分支
        if (holder.mTextView.getTag() == timeKey) {
            if (isSelectTitle) {
                holder.mTextView.setTag("");
                holder.select_intitle.setText(R.string.select_all);
            } else {
                holder.select_intitle.setText(R.string.select_cancel);
            }
        }
        //单条滑动冲突解决
        if (titleLinkedHashMap.containsKey(timeKey)) {
            holder.select_intitle.setText(R.string.select_cancel);
        } else {
            holder.select_intitle.setText(R.string.select_all);
        }
    }

    /**
     * 把此日期下的所有图片标为选中，并且把路径加入集合
     */
    private void refreshSelectUI(CountItemViewHolder holder, String imagePath, int position) {
        if (isFlagFromTitle) {
            for (String value : picPath) {
                String[] split = value.split("@");
                if (split != null && split.length > 0) { //fixed by yangchao 2016/10/9 :防止数组越界异常
                    holder.ivImage.setTag(R.string.app_name,split[0]);
                    if (imagePathLinkedHashMap != null) {
                        imagePathLinkedHashMap.put(split[0], split[1]);
                    }
                    if (holder.ivImage.getTag(R.string.app_name).equals(imagePath)) {
                        holder.ibSelect.setVisibility(View.VISIBLE);
                        if (isVideo) {
                            downThumbKey.add(split[0]);
                            calcuSize(holder, position);
                        }
                    }
                }
            }
        } else {
            if (picPath != null && picPath.size() != 0) {
                for (String value : picPath) {
                    String[] split = value.split("@");
                    if (split != null && split.length > 0) { //fixed by yangchao 2016/10/9 :防止数组越界异常
                        holder.ivImage.setTag(R.string.app_name,split[0]);
                        if (holder.ivImage.getTag(R.string.app_name).equals(imagePath)) {
                            holder.ibSelect.setVisibility(View.GONE);
                            if (isVideo) {
                                fileSizeList.clear();
                                downThumbKey.clear();
                            }
                        }
                    }
                }
            }
        }

        //单条滑动冲突解决
        if (imagePathLinkedHashMap != null && imagePathLinkedHashMap.containsKey(imagePath)) {
            holder.ibSelect.setVisibility(View.VISIBLE);
        } else {
            holder.ibSelect.setVisibility(View.GONE);
        }
    }

    /**
     * 计算选择文件的大小
     */
    private void calcuSize(CountItemViewHolder holder, int position) {
        num = 0;
        int pos = holder.getLayoutPosition();
        for (int i = 0; i <= pos; i++) {//遍历一下点击itme之前有多少个header
            itemViewType = getItemViewType(i);
            if (itemViewType == VIEW_TYPE_HEADER) {
                num++;
            }
        }
        if(mList.size() > 0){
            String size = mList.get(position - num).currentSize;
            fileSizeList.add(size);
        }

    }

    /**
     * 设置回调刷新
     */
    private void refreshMainUI(CountItemViewHolder holder) {
        if (changeSelectUI != null) {
            changeSelectUI.changeUI(holder.ibSelect, holder.download_flag);
        }
    }

    private void setItemClickEvent(final CountItemViewHolder holder, final String thumbPath,
                                   final String bigimagePath, final String timeKey) {

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mProgress.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    if (!UavConstants.isSelectState) {
                        Log.i("Sven","selectClick "+thumbPath);
                        selectClick(timeKey, thumbPath, holder, bigimagePath);
                    } else {
                        Log.i("Sven","itemClick"+thumbPath);
                        itemClick(holder, thumbPath, bigimagePath);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder, pos);
                    return false;
                }
            });
        }
    }

    private void itemClick(CountItemViewHolder holder, String thumbPath, String bigimagePath) {
        num = 0;
        int pos = holder.getLayoutPosition();
        for (int i = 0; i <= pos; i++) {//遍历一下点击itme之前有多少个header
            itemViewType = getItemViewType(i);
            if (itemViewType == VIEW_TYPE_HEADER) {
                num++;
            }
        }
        imagePathForViewPager.clear();
        thumbPathForViewPager.clear();
        sizePathForViewPager.clear();
//        for (int i = 0; i < imageList.size(); i++) {
//            imagePathForViewPager.add(bigImageList.get(i));
//            thumbPathForViewPager.add(imageList.get(i));
//            sizePathForViewPager.add(currentSizeList.get(i));
//        }

        for ( LineItem item:mItems){
            if (!item.isHeader) {
                imagePathForViewPager.add(item.bigPicPath);
                if (!TextUtils.isEmpty(thumbPath)&&!thumbPath.startsWith("PIC")&&!thumbPath.startsWith("MOV")){
                    thumbPathForViewPager.add(item.thumbPath);
                }else{
                    String     filename = item.bigPicPath.substring(item.bigPicPath.lastIndexOf("/") + 1);
                    File file = new File(isVideo?Const.PHONE_RECORD_THUMBNAIL_PATH:Const.PHONE_PIC_THUMBNAIL_PATH + "/" + filename);
                    thumbPathForViewPager.add(file.getPath());
                }


                sizePathForViewPager.add(item.size);
            }

        }
        mOnItemClickLitener.onItemClick(holder, pos - num, thumbPath, bigimagePath,
                sizePathForViewPager, thumbPathForViewPager, imagePathForViewPager);
    }

    private void refreshShadeClickUI(final CountItemViewHolder holder, final String imagePath,
                                     final String bigimagePath, final String timeKey) {
        //选择框的状态
        holder.ibSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClick(timeKey, imagePath, holder, bigimagePath);
            }
        });
    }

    private void selectClick(String timeKey, String imagePath, CountItemViewHolder holder, String bigimagePath) {
        if (!selectNumMap.containsKey(timeKey)) {
            selectNum = 0;
            selectNumMap.put(timeKey, selectNum);
        }
        picPathControlTitle = stringListMap.get(timeKey);
        int size = picPathControlTitle.size();

//        LogUtil.d("LinkedHashMap", "selectClick HashMap = " + (imagePathLinkedHashMap != null) + " containkey = " + imagePathLinkedHashMap.containsKey(imagePath) + " key" + imagePath);
        if (imagePathLinkedHashMap != null && imagePathLinkedHashMap.containsKey(
                imagePath)) {//如果LinkedHashMap包含（图片路径）键
            imagePathLinkedHashMap.remove(imagePath);//从LinkedHashMap中移除
            holder.ibSelect.setVisibility(View.GONE);//设置图片选择图标没有选择
            if (selectNumMap.get(timeKey) == size) {
                cancelSelectTitle(holder, timeKey);
            }
            int i = selectNumMap.get(timeKey).intValue();
            i--;
            selectNumMap.put(timeKey, i);
        } else {//如果LinkedHashMap不包含（图片路径）键
            if (imagePathLinkedHashMap != null) {
                imagePathLinkedHashMap.put(imagePath, bigimagePath);//LinkedHashMap存储键值对
            }
//            LogUtil.d("LinkedHashMap------", "HashMap = " + (imagePathLinkedHashMap != null) + " containkey = " + imagePathLinkedHashMap.containsKey(imagePath) + " key" + imagePath + "\n");
            holder.ibSelect.setVisibility(View.VISIBLE);//设置图片选择图标选择
            //判断全选的逻辑 1.知道是哪一天  2.一共几个 picPathControlTitle.size
            for (String value : picPathControlTitle) {
                if (value.contains(imagePath)) {
                    int i = selectNumMap.get(timeKey).intValue();
                    i++;
                    selectNumMap.put(timeKey, i);
                    if (selectNumMap.get(timeKey) == size) {
                        selectTimeAll(timeKey, holder);
                    }
                }
            }
        }
        this.notifyDataSetChanged();
    }

    /**
     * 下载更新ui
     *  @param holder
     * @param timeKey
     * @param imagePath
     */
    public void downLoadChangeUi(CountItemViewHolder holder, String timeKey, String imagePath) {

        if (imagePathLinkedHashMap != null && imagePathLinkedHashMap.containsKey(imagePath)) {
            imagePathLinkedHashMap.remove(imagePath);
            selectNumMap.remove(timeKey);
            holder.ibSelect.setVisibility(View.GONE);
        }
        if (isFlagFromTitle) {
//                titleLinkedHashMap.remove(timeKey);
            holder.ibSelect.setVisibility(View.GONE);
            setisSelectTitle(true);
            setisFlagFromTitle(false);
        }

    }

    /**
     * 检查文件在本地是否已经保存
     */
    private void checkFileExistInPhone(final CountItemViewHolder holder, final LineItem item) {
        File file = new File(item.localPath);

        LogUtil.e("localPath=="+item.localPath);
        if (file.exists()) {
            //下载本地图标显示
            holder.download_flag.setImageLevel(1);
            holder.mProgress.setVisibility(View.GONE);
            if (isVideo) {
                if(TextUtils.isEmpty(item.duration)){//这个是耗时方法2S
                    String time =videoHashMap.get(item.localPath);
                    if (TextUtils.isEmpty(time)){
                      item.duration = TimeUtil.getVideoMp4Time(item.localPath);//需要重新写入数据库文件
                        //刷新到相册
                        FileUtil.FlushImageToGallery(context, new File(item.localPath));
//                        item.duration = "00:00";
                    }else{
                        item.duration =TimeUtil.stringForTime(Long.parseLong(time));
                    }
                }
                holder.mp4Duration.setVisibility(View.VISIBLE);
                holder.mp4Duration.setText(item.duration);
            } else {
                holder.mp4Duration.setVisibility(View.GONE);
            }
        } else {

                //飞机图标显示
           holder.download_flag.setImageLevel(2);
            holder.mp4Duration.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化网格布局
     */
    public void initLayout(LineItem item, View itemView) {
        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        lp.setSlm(GridSLM.ID);
        lp.setMargins(margin, margin, margin, margin);
        lp.setNumColumns(UavConstants.RECYCLERVIEWCOLUMS);
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public void setChangeSelectUI(ChangeSelectUI mChangeSelectUI) {
        this.changeSelectUI = mChangeSelectUI;
    }

    /**
     * 显示图片
     */
    public void displayImage(CountItemViewHolder holder, String imagePath ,String  bigimagePath, NetworkImageView imageView) {
        holder.imagePlay.setVisibility(isVideo ? View.VISIBLE : View.GONE);
        if (!TextUtils.isEmpty(imagePath)&&!imagePath.startsWith("PIC")&&!imagePath.startsWith("MOV")){
            imageView.displayImage(imagePath);
        }else{
         String     filename = bigimagePath.substring(bigimagePath.lastIndexOf("/") + 1);
            File file = new File(isVideo?Const.PHONE_RECORD_THUMBNAIL_PATH:Const.PHONE_PIC_THUMBNAIL_PATH + "/" + filename);
            if ( file.exists()) {
                imageView.displayImage(file.getPath());
            }else {
                imageView.displayImage(R.mipmap.ic_gf_default_photo);
            }

        }

    }

    public String getDownloadThumbPic(int position) {
       return downThumbKey.get(position);
    }

    public interface OnItemClickLitener {
        void onItemClick(CountItemViewHolder holder, int position, String thumbPath,
                         String bigimagePath, List<String> size, List<String> thumbPathForViewPager,
                         List imagePathForViewPager);

        void onItemLongClick(CountItemViewHolder holder, int position);
    }

    public interface ChangeSelectUI {
        void changeUI(View view1, View download);

        void changeTitleUI(View view1);
    }

    private static class LineItem {

        public  String  key;
        public  String  size;
        private int     sectionManager;
        private int     sectionFirstPosition;
        private boolean isHeader;
        private String  timeKey;
        private String  thumbPath;
        private String  bigPicPath;
        private String  localPath;
        public String   duration;

        private LineItem(String timeKey, String thumbPath, String bigPicPath, String size,
                         boolean isHeader, int sectionManager, int sectionFirstPosition, boolean isVideo) {
            this.isHeader = isHeader;
            this.timeKey = timeKey;
            this.thumbPath = thumbPath;
            this.bigPicPath = bigPicPath;
            this.size = size;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
            if (TextUtils.isEmpty(bigPicPath))
                return;

            LogUtil.e("LineItem==="+bigPicPath+"   "+StringUtil.splitStrForPic(bigPicPath));
            if (isVideo) {
                localPath = UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + StringUtil.splitStrForVideo(bigPicPath);
            } else {
                localPath = UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + StringUtil.splitStrForPic(bigPicPath);
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LineItem lineItem = (LineItem) o;

            return thumbPath != null ? thumbPath.equals(lineItem.thumbPath) : lineItem.thumbPath == null;

        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
    /**
     * 根据下载照片集合，判断是否移除全选标题
     * @param key
     */
    public void removeTitleLinkedHashMap(String key, String url){
        String str = url.substring(0, url.lastIndexOf("_"));
        for (String keyTime : imagePathLinkedHashMap.values()) {
            if (keyTime.startsWith(str)){
                return;
            }
        }
        titleLinkedHashMap.remove(key);
    }
}