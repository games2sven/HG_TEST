package com.highgreat.education.manager;

import android.content.Context;

import com.highgreat.education.R;
import com.highgreat.education.bean.DownloadBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.ImageTasksManagerModel;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dao.DBHelper;
import com.highgreat.education.holder.CountItemViewHolder;
import com.highgreat.education.utils.FileUtil;
import com.highgreat.education.utils.LogUtil;
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
public class ImageDownLoadCallBackManager {
    private final static String TAG = ImageDownLoadCallBackManager.class.getSimpleName();
    private final DBHelper db;
    private        Context                      context;
    private static ImageDownLoadCallBackManager instance;
    public String lastPosition = "";
    private Map<String, String> keyTime = new HashMap<>();
    public synchronized static ImageDownLoadCallBackManager getInstance(Context context) {
        if (null == instance) {
            instance = new ImageDownLoadCallBackManager(context);
        }
        return instance;
    }

    public ImageDownLoadCallBackManager(Context context) {
        this.context = context;
        db = DBHelper.getInstance(context);
    }

    /**
     * 开启任务
     */
    public void toStartDownLoadTask(CountItemViewHolder holder) {
        if (!holder.url.equals(lastPosition)) {
            lastPosition = holder.url;

            final ImageTasksManagerModel model = ImageTaskManager.getImpl().get(holder.url);
            final BaseDownloadTask task = FileDownloader.getImpl()
                    .create(model.getUrl())
                    .setPath(model.getPath())
                    .setCallbackProgressTimes(100)
                    .setListener(taskDownloadListener);
            keyTime.put(model.getUrl(), holder.key);
            ImageTaskManager.getImpl().addTaskForViewHoder(task, holder);
        }
    }

    private FileDownloadListener taskDownloadListener = new FileDownloadSampleListener() {

        private CountItemViewHolder checkCurrentHolder(final BaseDownloadTask task) {
            final CountItemViewHolder holder = (CountItemViewHolder) task.getTag();
            if (holder != null && holder.id != task.getDownloadId()) {
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
            UavConstants.ISDOWNLOADIMAGEFINISH = false;
        }

        @Override
        protected void error(BaseDownloadTask task, Throwable e) {
            super.error(task, e);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }
            LogUtil.e(TAG, "FileDownloadListener error: " + e);
            UavConstants.ISDOWNLOADIMAGEFINISH = true;
            holder.updateNotDownloaded(FileDownloadStatus.error, task.getLargeFileSoFarBytes(),
                    task.getLargeFileTotalBytes());
            FileUtil.rmImageDownLoadInfo(context);//清除
            ImageTaskManager.getImpl().removeTaskForViewHolder(task.getId(), task.getUrl());
            UiUtil.showToast(R.string.pic_download_erro);
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
            ImageTaskManager.getImpl().removeTaskForViewHolder(task.getId(), task.getUrl());
        }

        @Override
        protected void completed(BaseDownloadTask task) {
            super.completed(task);
            final CountItemViewHolder holder = checkCurrentHolder(task);
            if (holder == null) {
                return;
            }
//            String name = StringUtil.splitStrForPic(task.getUrl());
//            String path = UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name;
//            LogUtil.e(TAG, "xujm completed: " + path + " <==> " + task.getPath());
            File file = new File(task.getPath());
            if (!file.exists()) {
                error(task, null);
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_IMAGE_SUCCESS));
                return;
            }
            holder.updateDownloaded();
            ImageTaskManager.getImpl().removeTaskForViewHolder(task.getId(), task.getUrl());
            LogUtil.Lee("一个图片下载完成 " + task.getUrl());
            FileUtil.FlushImageToGallery(UiUtil.getContext(), file);
            EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_IMAGE_SUCCESS, task.getUrl()));
            if (db.getAllImageTasks().size() == 0) {
                lastPosition = "";
                //全部下载完成
                LogUtil.e(TAG, "全部下载完成 ");
                EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_IMAGE_DOWNLOAD_FINISH,
                        new DownloadBean(keyTime.get(task.getUrl()), task.getUrl(), "")));
            }else{
                if (null != keyTime && keyTime.size() > 0){
                    EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_IMAGE_SUCCESS,
                            new DownloadBean(keyTime.get(task.getUrl()), task.getUrl(), "")));
                }
            }
        }
    };
}
