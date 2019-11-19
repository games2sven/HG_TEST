package com.highgreat.education.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;


import com.highgreat.education.bean.ImageTasksManagerModel;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dao.DBHelper;
import com.highgreat.education.holder.CountItemViewHolder;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.UiUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadConnectListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @FileName：Camera
 * @description：
 * @author：mac-likh
 * @date：16/6/28 14:13
 */
public class ImageTaskManager {

    // 文件保存地址
//    public final String savePath = UavConstants.BIG_PIC_ABSOLUTE_PATH + "/";
    private final DBHelper dbinstance;

    private Context                             context;
    /**保存所有下载的任务*/
    private Map<String, ImageTasksManagerModel> modelList;
    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    private final static class HolderClass {
        private final static ImageTaskManager INSTANCE
                = new ImageTaskManager();
    }

    public static ImageTaskManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private ImageTaskManager() {
        dbinstance = DBHelper.getInstance(UiUtil.getContext());
        modelList = dbinstance.getAllImageTasks();
    }

    /**
     * 初始化任务数据 绑定服务
     *
     * @param context
     * @param pathMap
     */
    public void initTaskUrlMap(Context context, LinkedHashMap<String, String> pathMap) {
        if (pathMap == null) return;
        modelList.clear();

        Iterator iterator = pathMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            String url = pathMap.get(imagePathKeySet);
            String picname = StringUtil.splitStrForPic(url);
            File file = new File(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + picname);
            if (!file.exists()) {
                addTask(picname, url);
            }
        }
        modelList = dbinstance.getAllImageTasks();
        LogUtil.Lee("modelList  初始  " + modelList.size());

        bindDownLoadService(context);
    }


    /**
     * 给每一个Holder添加任务
     * @param task
     * @param holder
     */
    public void addTaskForViewHoder(final BaseDownloadTask task, CountItemViewHolder holder) {
        if (taskSparseArray.get(task.getId()) != null) return;
        taskSparseArray.put(task.getId(), task);
        updateViewHolder(holder.id, holder);
        task.start();

    }

    /**
     * 删除holder的任务
     * @param id
     * @param url
     */
    public void removeTaskForViewHolder(final int id, String url) {
        if(id != -1) {
            taskSparseArray.remove(id);
        }
        removeDBTask(url);
    }
    public void removeDBTask(String url){
        dbinstance.deleteImageDownLoadTask(url);
        modelList = dbinstance.getAllImageTasks();
        if(modelList.size() == 0){
            taskSparseArray.clear();
            // TODO: 2017/7/10 在图片下载完成的回调里，全部任务下载结束后会发送以下消息，不用重复发送
//            EventBus.getDefault().post(new EventCenter(UavConstants.CODE_IMAGE_DOWNLOAD_FINISH));
        }
    }

    /**
     * 给每一个任务标记tag  传入holder对象
     * @param id
     * @param holder
     */
    public void updateViewHolder(final int id, final CountItemViewHolder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }
        task.setTag(holder);
    }

    public void releaseTask() {
        taskSparseArray.clear();
    }

    private FileDownloadConnectListener listener;

    public void bindDownLoadService(final Context context) {
        this.context = context;
        FileDownloader.getImpl().bindService();

        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }
        listener = new FileDownloadConnectListener() {

            @Override
            public void connected() {
            }

            @Override
            public void disconnected() {
            }
        };
        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    public void unBindDownLoadService() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
        releaseTask();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public ImageTasksManagerModel get(String url) {
        return modelList.get(url);
    }

    public ImageTasksManagerModel getbyId(final int id) {
        for (Map.Entry entry : modelList.entrySet()) {
            ImageTasksManagerModel model = (ImageTasksManagerModel) entry.getValue();
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    public void cancelTaskDownLoadPic() {
        try {
            for (Map.Entry entry : modelList.entrySet()) {
                ImageTasksManagerModel model = (ImageTasksManagerModel) entry.getValue();
                FileDownloader.getImpl().clear(model.getId(), "");
            }
        } catch (Exception e) {
            LogUtil.Lee(e.getMessage());
        }
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    private ImageTasksManagerModel addTask(final String name, final String url) {
        String path = createPath(name);
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        if (dbinstance.isExistImageTaskUrl(url)) {
            LogUtil.Lee("数据库url已经存在");
            return null;
        }
        final int id = FileDownloadUtils.generateId(url, path);
        ImageTasksManagerModel model = getbyId(id);
        if (model != null) {
            return model;
        }
        return dbinstance.addImageTask(name, url, path);
    }

    public String createPath(final String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
//        return savePath + name;
        return UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name;
    }
}
