package com.highgreat.education.utils;

import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Do Good App
 * 项目名称：ZeroDroneApp
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/3/3 16:49
 * 修改人：mac-likh
 * 修改时间：16/3/3 16:49
 * 修改备注：
 */
public class MultiDownLoadUtil {
    private static FileDownloadListener downloadListener;
    private static BaseDownloadTask downloadTaskForPic;
    private static List<Integer> downloadIdList = new ArrayList<>();

    /**
     * 图片单任务下载
     */
    public static void startSingleTaskDownLoadPic(String url, String savePath) {
        try {
            String picname = StringUtil.splitStrForPic(url);
            downloadTaskForPic = createDownloadTaskForPic(url, savePath + picname);
            downloadIdList.add(downloadTaskForPic.getId());
            downloadTaskForPic.start();
        } catch (Exception e) {
            //      e.printStackTrace();
            LogUtil.Lee(e.getMessage());
        }
    }


    /**
     * 图片单任务下载
     */
    public static void startSingleTaskDownLoadMaterial(String url, String savePath,FileDownloadListener listener) {
        try {
            downloadTaskForPic = createDownloadTaskForMaterial(url, savePath,listener);
            downloadIdList.add(downloadTaskForPic.getId());
            downloadTaskForPic.start();
        } catch (Exception e) {
            //      e.printStackTrace();
            LogUtil.Lee(e.getMessage());
        }
    }

    public static void cancelSingleTaskDownLoadPic() {
        try {
            if (downloadIdList.size() <= 0) return;
            LogUtil.Lee("下载数量:  " + downloadIdList.size());
            for (Integer id : downloadIdList) {
                FileDownloader.getImpl().clear(id, "");
            }
            downloadIdList.clear();
        } catch (Exception e) {
            //      e.printStackTrace();
            LogUtil.Lee(e.getMessage());
        }
    }

    /**
     * 开启多任务
     */
    public static void startMultiTask(HashMap urlMap, String savePath) {
        downloadListener = createLis();
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        try {
            Iterator iterator = urlMap.keySet().iterator();
            while (iterator.hasNext()) {
                String md5 = (String) iterator.next();
                String url = (String) urlMap.get(md5);
                tasks.add(FileDownloader.getImpl().create(url).setPath(savePath + md5));
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        queueSet.disableCallbackProgressTimes();
        queueSet.setAutoRetryTimes(1);
        // 并行行执行该任务队列
        queueSet.downloadTogether(tasks);

        queueSet.start();
    }

    private static FileDownloadListener createLis() {
        return new FileDownloadListener() {

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                // 之所以加这句判断，是因为有些异步任务在pause以后，会持续回调pause回来，而有些任务在pause之前已经完成，
                // 但是通知消息还在线程池中还未回调回来，这里可以优化
                // 后面所有在回调中加这句都是这个原因
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue,
                                     int soFarBytes, int totalBytes) {
                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                super.retry(task, ex, retryingTimes, soFarBytes);
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                //更改为MD5
                String path = task.getTargetFilePath();
                File file = new File(path);
                String fileMD5 = MD5Util.getFileMD5(file);
                FileUtil.renameFile(file.getParent(),task.getFilename(),fileMD5);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (task.getListener() != downloadListener) {
                    return;
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                if (task.getListener() != downloadListener) {
                    return;
                }
            }
        };
    }

    public static BaseDownloadTask createDownloadTaskForPic(final String url, String savepath) {

        return FileDownloader.getImpl()
                .create(url)
                .setPath(savepath)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue,
                                             int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex,
                                         final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        String name = StringUtil.splitStrForPic(url);
                        File file = new File(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + name);
                        FileUtil.FlushImageToGallery(UiUtil.getContext(), file);
                        EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_DOWN_SINGLE_SUCCESS,url));
                        LogUtil.e("position", "下载" + name);
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_PIC_ERRO));
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                });
    }


    public static BaseDownloadTask createDownloadTaskForMaterial(final String url, String savepath  , FileDownloadListener listener) {

        return FileDownloader.getImpl()
                .create(url)
                .setPath(savepath)
                .setListener(listener);
    }
}
