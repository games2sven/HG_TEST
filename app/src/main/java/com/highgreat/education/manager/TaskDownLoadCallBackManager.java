package com.highgreat.education.manager;

import android.content.Context;

import com.highgreat.education.R;
import com.highgreat.education.bean.DownloadBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.TasksManagerModel;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dao.DBHelper;
import com.highgreat.education.holder.CountItemViewHolder;
import com.highgreat.education.utils.BitmapUtil;
import com.highgreat.education.utils.FileUtil;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.StringUtil;
import com.highgreat.education.utils.UiUtil;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.model.FileDownloadStatus;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @description：
 * @author：mac-likh
 * @date：16/6/2 15:41
 */
public class TaskDownLoadCallBackManager {
    private static TaskDownLoadCallBackManager instance;
    private final DBHelper db;
    public String lastPosition = "";
    private Context context;
    private Map<String, String> keyTime = new HashMap<>();
    private FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

        private CountItemViewHolder checkCurrentHolder(final BaseDownloadTask task) {
            final CountItemViewHolder holder = (CountItemViewHolder) task.getTag();
            if (holder != null && holder.id != task.getId()) {
                return null;
            }
            return holder;
        }

        @Override
        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.pending(task, soFarBytes, totalBytes);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }

            holder.updateDownloading(FileDownloadStatus.pending, soFarBytes, totalBytes, task.getId());
        }

        @Override
        protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes,
                                 int totalBytes) {
            super.connected(task, etag, isContinue, soFarBytes, totalBytes);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }

            holder.updateDownloading(FileDownloadStatus.connected, soFarBytes, totalBytes, task.getId());
        }

        @Override
        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.progress(task, soFarBytes, totalBytes);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }
            holder.updateDownloading(FileDownloadStatus.progress, soFarBytes, totalBytes, task.getId());
            UavConstants.ISDOWNLOADFINISH = false;
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }
            LogUtil.Lee("卧槽错误 error" + e);
            UavConstants.ISDOWNLOADFINISH = true;
            holder.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes(),
                    task.getLargeFileTotalBytes());
            FileUtil.rmDownLoadInfo(context);//清除视频下载
            FileUtil.rmDownLoadInfo(context);//清除视频下载
            TasksManager.getImpl().removeTaskForViewHolder(task.getId(), task.getUrl());
            UiUtil.showToast(R.string.video_download_erro);
            FileDownloader.getImpl().clear(task.getId(), task.getTargetFilePath());
        }

        @Override
        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            super.paused(task, soFarBytes, totalBytes);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }

            holder.updateNotDownloaded(FileDownloadStatus.paused, soFarBytes, totalBytes);
            TasksManager.getImpl().removeTaskForViewHolder(task.getId(), task.getUrl());
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }
            holder.updateDownloaded();
            TasksManager.getImpl().removeTaskForViewHolder(task.getId(), task.getUrl());
            LogUtil.Lee("一个视频下载完成 " + task.getUrl());
            final String videoName = StringUtil.splitStrForVideo(task.getUrl());
            //刷新到相册
            FileUtil.FlushImageToGallery(context, new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName));
            //生成缩略图
            BitmapUtil.createThumbnial(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName, videoName.replace(".mp4", "_thumb.jpg"), UavConstants.LOCAL_VIDEO_THUMB_ABSOLUTE_PATH);

            EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_VIDEO_SUCCESS,UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName));
            if (db.getAllTasks().size() == 0) {
                //全部下载完成
                LogUtil.Lee("全部下载完成 ");
                lastPosition = "";
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_VIDEO_DOWNLOAD_FINISH,
                        new DownloadBean(keyTime.get(task.getUrl()), task.getUrl(), UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName)));
            }else{
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_VIDEO_SUCCESS,
                        new DownloadBean(keyTime.get(task.getUrl()), task.getUrl(), UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName)));
            }
        }
    };

    public TaskDownLoadCallBackManager(Context context) {
        this.context = context;
        db = DBHelper.getInstance(context);
    }

    public synchronized static TaskDownLoadCallBackManager getInstance(Context context) {
        if (null == instance) {
            instance = new TaskDownLoadCallBackManager(context);
        }
        return instance;
    }

    /**
     * 开启任务
     */
    public void toStartDownLoadTask(CountItemViewHolder holder) {
        if (!holder.url.equals(lastPosition)) {
            lastPosition = holder.url;

            final TasksManagerModel model = TasksManager.getImpl().get(holder.url);
            final BaseDownloadTask task = FileDownloader.getImpl()
                    .create(model.getUrl())
                    .setPath(model.getPath())
                    .setCallbackProgressTimes(100)
                    .setListener(taskDownloadListener);
            keyTime.put(model.getUrl(), holder.key);
            TasksManager.getImpl().addTaskForViewHoder(task, holder);
        }
    }
}
