package com.highgreat.education.manager;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;

import com.highgreat.education.bean.TasksManagerModel;
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
 * 下载任务管理
 *
 * @FileName：
 * @description：
 * @author：mac-likh
 * @date：16/5/31 14:25
 */
public class TasksManager {

    // 文件保存地址
//    public static String savePath = UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/";

    private final DBHelper dbinstance;
    private       Context                        context;
    private       Map<String, TasksManagerModel> modelList;
    private SparseArray<BaseDownloadTask> taskSparseArray = new SparseArray<>();

    private final static class HolderClass {
        private final static TasksManager INSTANCE
                = new TasksManager();
    }

    public static TasksManager getImpl() {
        return HolderClass.INSTANCE;
    }


    private TasksManager() {
        dbinstance = DBHelper.getInstance(UiUtil.getContext());
        modelList = dbinstance.getAllTasks();
    }

    public void initTaskUrlMap(Context context, LinkedHashMap<String, String> pathMap) {
        if (pathMap == null) return;
        modelList.clear();

        Iterator iterator = pathMap.keySet().iterator();
        while (iterator.hasNext()) {
            String imagePathKeySet = (String) iterator.next();
            String url = pathMap.get(imagePathKeySet);
            String videoname = StringUtil.splitStrForVideo(url);
            File file = new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoname);
            if (!file.exists()) {
                addTask(videoname, url);
            }
        }
        modelList = dbinstance.getAllTasks();
        LogUtil.Lee("modelList  初始  " + modelList.size());

        bindDownLoadService(context);
    }

    public void addTaskForViewHoder(final BaseDownloadTask task, CountItemViewHolder holder) {
        if (taskSparseArray.get(task.getId()) != null) return;
        taskSparseArray.put(task.getId(), task);
        updateViewHolder(holder.id, holder);
        task.start();

    }

    public void removeTaskForViewHolder(final int id, String url) {
        if(id != -1) {
            taskSparseArray.remove(id);
        }
        dbinstance.deleteDownLoadTask(url);
        modelList = dbinstance.getAllTasks();
        LogUtil.Lee("modelList   之后 " + modelList.size());
    }

    public void deleteDownLoadTask(String url) {
        dbinstance.deleteDownLoadTask(url);
        modelList = dbinstance.getAllTasks();
    }

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

    public TasksManagerModel get(String url) {
        return modelList.get(url);
    }

    public TasksManagerModel getbyId(final int id) {
        for (Map.Entry entry : modelList.entrySet()) {
            TasksManagerModel model = (TasksManagerModel) entry.getValue();
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    public void cancelTaskDownLoadVideo() {
        try {
            for (Map.Entry entry : modelList.entrySet()) {
                TasksManagerModel model = (TasksManagerModel) entry.getValue();
                FileDownloader.getImpl().clear(model.getId(), "");

            }
        } catch (Exception e) {
            LogUtil.Lee(e.getMessage());
        }
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    public TasksManagerModel addTask(final String name, final String url) {
        String path = createPath(name);
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }
        if (dbinstance.isExistTaskUrl(url)) {
            LogUtil.Lee("数据库url已经存在");
            return null;
        }
        final int id = FileDownloadUtils.generateId(url, path);
        TasksManagerModel model = getbyId(id);
        if (model != null) {
            return model;
        }
        return dbinstance.addTask(name, url, path);
    }

    public String createPath(final String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }
//        return savePath + name;
        return UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + name; //add-by-yc-2017/7/31-兼容hesper飞机,区分相册
    }

    public boolean isTaskRunning() {
        return (dbinstance.getAllImageTasks().size() > 0 || dbinstance.getAllTasks().size() > 0) ? true : false;
    }
}
