package com.highgreat.education.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.telecom.Call;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;


import com.highgreat.education.R;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.ImageTasksManagerModel;
import com.highgreat.education.bean.TasksManagerModel;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;
import com.highgreat.education.dao.DBHelper;
import com.highgreat.education.manager.ImageDownLoadCallBackManager;
import com.highgreat.education.manager.ImageTaskManager;
import com.highgreat.education.manager.TaskDownLoadCallBackManager;
import com.highgreat.education.manager.TasksManager;
import com.highgreat.education.widget.NetworkImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/12 15:21
 * 修改人：mac-likh
 * 修改时间：16/1/12 15:21
 * 修改备注：
 */
public class FileUtil {
    /**
     * 媒体库图标 初始化显示 请求图片
     */
    private static Object getPicTag = new Object();
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static final int FILE_UNIT_SIZE_KB = 11;
    public static final int FILE_UNIT_SIZE_MB = 12;
    /**
     * 获取目录下所有文件(按时间排序)
     */
    public static List<File> getFileSort(String path) {

        List<File> list = getAllFiles(path);

        if (list != null && list.size() > 0) {

            Collections.sort(list, new Comparator<File>() {
                public int compare(File file, File newFile) {
                    if (file.lastModified() < newFile.lastModified()) {
                        return 1;
                    } else if (file.lastModified() == newFile.lastModified()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
        }

        return list;
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }
    /**
     * 获取目录下所有文件(按文件名排序)
     */
    public static List<File> getFileSortForFileName(String path) {

        List<File> list = getAllFiles(path);

        if (list != null && list.size() > 0) {
            Collections.sort(list, new YMComparator());
        }

        return list;
    }

    /**
     * 获取目录下所有文件
     */
    public static List<File> getAllFiles(String realpath) {
        List<File> files = new ArrayList<File>();
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles();
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getAllFiles(file.getAbsolutePath());
                } else {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * 读取asset目录下文件。
     *
     * @return content
     */
    public static String readFileForVoiceFunc(Context mContext, String file, String code) {
        int len = 0;
        byte[] buf = null;
        String result = "";
        try {
            AssetManager am = mContext.getAssets();
            InputStream in = am.open(file);
            len = in.available();
            buf = new byte[len];
            in.read(buf, 0, len);

            result = new String(buf, code);
        } catch (Exception e) {
//      e.printStackTrace();
            LogUtil.e(e.getMessage());
        }
        return result;
    }

    /**
     * 删除动作刷新到图库
     */
    public static void FlushImageToGallery(Context context, File file) {
//        MediaScannerConnection.scanFile(context, new String[]{
//                file.toString()
//        }, null, new MediaScannerConnection.OnScanCompletedListener() {
//            @Override
//            public void onScanCompleted(final String path, final Uri uri) {
//
//            }
//        });
        new SingleMediaScanner(context, file);
        MediaScannerConnection.scanFile(context, new String[]{
                file.toString()
        }, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(final String path, final Uri uri) {
            }
        });
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 刷新到图库
     */
    public static void FlushDeleteImageToGallery(Context context, final File file) {

        MediaScannerConnection.scanFile(context, new String[]{
                file.toString()
        }, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(final String path, final Uri uri) {
                file.delete();
            }
        });
    }

    //删除指定文件夹下所有文件
    //param path 文件夹完整绝对路径
    public static void delelteFile(List fileList, List videoNameList, boolean isPicOrVideo) {
        List<File> sortList = new ArrayList<File>();
        String filePath = null;
        if (isPicOrVideo) {
            filePath = UavConstants.BIG_PIC_ABSOLUTE_PATH;
        } else {
            filePath = UavConstants.BIG_VIDEO_ABSOLUTE_PATH;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        File[] subFile = file.listFiles();
        for (File f : subFile) {
            if (f.isFile()) {
                sortList.add(f);
            }
        }
        Collections.sort(sortList, new YMComparator());

        for (int i = 0; i < sortList.size(); i++) {
            String filePathDest = sortList.get(i).toString();
            for (int j = 0; j < fileList.size(); j++) {
                if (filePathDest.equals(fileList.get(j))) {
                    new File(filePathDest).delete();
                    FileUtil.FlushDeleteImageToGallery(UiUtil.getContext(), sortList.get(i));
                }
                if (videoNameList != null && filePathDest.equals(videoNameList.get(j))) {
                    new File(filePathDest).delete();
                    FileUtil.FlushDeleteImageToGallery(UiUtil.getContext(), sortList.get(i));

                }
            }
        }
    }

    //删除指定文件夹下指定文件
    //param path 文件夹完整绝对路径
    public static void delelteItemFile(String deltePicPath) {
        File file = new File(deltePicPath);
        file.delete();
        FileUtil.FlushDeleteImageToGallery(UiUtil.getContext(), file);
        EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_PICINBIG_LOCALDELETE));
    }

    //删除指定文件夹下指定文件
    //param path 文件夹完整绝对路径
    public static void delelteItemFileForNet(String deltePicPath) {
        File file = new File(deltePicPath);
        file.delete();
        FileUtil.FlushDeleteImageToGallery(UiUtil.getContext(), file);
    }

    /**
     * @throws IOException
     */
    public static boolean copyFileFormAssetsToSD(String src, String dest, Context context) {
        FileOutputStream out = null;
        InputStream in = null;
        try {
            in = context.getAssets().open(src);
            out = new FileOutputStream(dest, true);
            int length = -1;
            byte[] buf = new byte[1024];
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
            return true;
        } catch (IOException e) {
//      e.printStackTrace();
            LogUtil.e(e.getMessage());
        } finally {
            try {
                out.flush();
                in.close();
                out.close();
            } catch (Exception e) {
//        e.printStackTrace();
                LogUtil.e(e.getMessage());
            }
        }
        return false;
    }

    public static boolean checkMd5(String md5, String filePath, String fileName) {
        File file = new File(filePath + fileName);
        return md5.equals(MD5Util.getFileMD5(file));
    }

    /**
     * 检查文件路径
     */
    public static File checkFileDir(String filePath) {
        File file = new File(filePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 检查文件路径
     */
    public static void checkFileAndDeleteDir(String filePath) {
        File file = new File(filePath);
        file.delete();
    }

    /**
     * 文件重命名
     *
     * @param path    文件目录
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (!oldfile.exists()) {
                return;//重命名文件不存在
            }
            if (newfile.exists()) {//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                LogUtil.Lee(newname + "已经存在！");
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
            LogUtil.Lee("新文件名和旧文件名相同...");
        }
    }

    /**
     * 删除指定文件
     *
     * @param fileAbsolutePath
     */
    public static void deleteDestPathFile(String fileAbsolutePath) {
        //上传完成删除临时文件
        File file = new File(fileAbsolutePath);
        file.delete();
    }

    /**
     * 删除文件夹下的所有文件
     *
     * @param oldPath
     */
    public static void deleteAllFile(File oldPath) {
        try {
            if (oldPath.isDirectory()) {
                File[] files = oldPath.listFiles();
                for (File file : files) {
                    deleteAllFile(file);
                }
            } else {
                oldPath.delete();
            }
        } catch (Exception e) {

        }
    }






    /**
     * Bitmap对象保存味图片文件
     */
    public static void saveBitmapFile(Bitmap bitmap, String path) {
        File file = new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除动作刷新到图库
     */
    public static void addPicToGallery(Context context, String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        LogUtil.d("addPicToGallery", "Uri  " + contentUri.getPath());
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public static void copyFolder(String oldPath, String newPath) {

        try {
            File fnewpath = new File(newPath);
            if (!fnewpath.exists()) {
                fnewpath.mkdirs();
            }
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {//如果是子文件夹
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    //把一个数组复制到另一个数组
    public static <T> T[] concatArray(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    /**
     * 清除视频下载缓存
     */
    public static void rmDownLoadInfo(Context context) {
        DBHelper instance = DBHelper.getInstance(context);
        //同时删除本地未完成的文件
        Map<String, TasksManagerModel> allTasks = instance.getAllTasks();
        LogUtil.Lee("-----文件---size-- " + allTasks.size());
        TaskDownLoadCallBackManager.getInstance(context).lastPosition = "";

        for (Map.Entry entry : allTasks.entrySet()) {
            TasksManagerModel value = (TasksManagerModel) entry.getValue();
            String urlstr = value.getUrl();
            String videoName = StringUtil.splitStrForVideo(urlstr);
            String picName = StringUtil.splitMp4(videoName) + "_thumb.jpg";
            FileUtil.delelteItemFile(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName);
            FileUtil.delelteItemFile(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + videoName + ".temp");

            new File(UavConstants.BIG_VIDEO_ABSOLUTE_PATH + "/" + picName).delete();
            LogUtil.Lee("-----删除文件---成功-- " + picName);
            instance.deleteDownLoadTask(urlstr);
        }
        TasksManager.getImpl().cancelTaskDownLoadVideo();
    }

    /**
     * 清除图片下载缓存
     */
    public static void rmImageDownLoadInfo(Context context) {
        DBHelper instance = DBHelper.getInstance(context);
        //同时删除本地未完成的文件
        Map<String, ImageTasksManagerModel> allTasks = instance.getAllImageTasks();
        LogUtil.Lee("-----文件---size-- " + allTasks.size());
        ImageDownLoadCallBackManager.getInstance(context).lastPosition = "";
        for (Map.Entry entry : allTasks.entrySet()) {
            ImageTasksManagerModel value = (ImageTasksManagerModel) entry.getValue();
            String urlstr = value.getUrl();
            String picName = StringUtil.splitStrForPic(urlstr);
            FileUtil.delelteItemFile(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + picName);
            FileUtil.delelteItemFile(UavConstants.BIG_PIC_ABSOLUTE_PATH + "/" + picName + ".temp");
            LogUtil.Lee("-----删除图片文件---成功-- " + picName);
            instance.deleteImageDownLoadTask(urlstr);
        }
        ImageTaskManager.getImpl().cancelTaskDownLoadPic();
    }

    public static class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
        private MediaScannerConnection mMs;
        private File mFile;
        public SingleMediaScanner(Context context, File f) {
            mFile = f;
            mMs = new MediaScannerConnection(context, this);
            mMs.connect();
        }
        @Override
        public void onMediaScannerConnected() {
            mMs.scanFile(mFile.getAbsolutePath(), null);
        }
        @Override
        public void onScanCompleted(String path, Uri uri) {
            mMs.disconnect();
        }
    }






    public static String getFileFormatterSize(Context ctx, long size) {
        String strFileSize = Formatter.formatFileSize(ctx, size);
//        switch (unit){
//            case FILE_UNIT_SIZE_KB:
//                strFileSize= Formatter.formatFileSize(ctx,1024);
//                break;
//            case FILE_UNIT_SIZE_MB:
//                strFileSize= Formatter.formatFileSize(ctx,1024*1024);
//                break;
//        }
        return strFileSize;
    }

    /**
     * Create a file Uri for saving an image or video
     */
    public static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public static File getMediaStorageDir() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        return mediaStorageDir;
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        if (Environment.getExternalStorageState() == null) {
            return null;
        }
        File mediaStorageDir = getMediaStorageDir();

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String UriToPath(Context ctx, Uri uri) {
        String fileName = null;
        Uri fileUri = uri;
        if (fileUri.getScheme().compareTo("content") == 0)           //content:开头
        {
            Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileName = cursor.getString(column_index);
            }
        } else if (fileUri.getScheme().compareTo("file") == 0) {         //file:开头
            fileName = fileUri.toString();
            fileName = fileName.replace("file://", "");
        }
        return fileName;
    }

    public static String getPathFromURI(Activity act, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = act.managedQuery(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;
    }

    /**
     * 创建目录
     * @param path
     */
    public static void mkDirs(String path) {
        if(TextUtils.isEmpty(path)) {
            return;
        }
        mkDirs(new File(path));
    }

    /**
     * 创建目录
     * @param file
     */
    public static void mkDirs(File file) {
        if (file == null) {
            return;
        }
        if (!file.isDirectory()) {
            if (file.exists()) {
                file.delete();
            }
            file.mkdirs();
        }
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteDir(File dir,boolean  isNeedDelGallery) {
        if (dir == null) return false;
        // 目录不存在返回true
        if (!dir.exists()) return true;
        // 不是目录返回false
        if (!dir.isDirectory()) return false;
        // 现在文件存在且是文件夹
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file,isNeedDelGallery)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file,isNeedDelGallery)) return false;
                }
            }
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param file 文件
     * @return {@code true}: 删除成功<br>{@code false}: 删除失败
     */
    public static boolean deleteFile(File file,boolean isNeedDelGallery) {
        boolean isDel =file != null && (!file.exists() || file.isFile() && file.delete());
        if (isDel&&isNeedDelGallery){
            FileUtil.FlushDeleteImageToGallery(UiUtil.getContext(), file);

        }

        return isDel;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0.00KB";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public  static String  formatSize(long  size){
        String resource_size;
        DecimalFormat df = new DecimalFormat("0.00");
        if (((double)size/1024/1024)>1024){
            resource_size =df.format(((double)size/1024/1024/1024))+"GB";
        }
        else if(((double)size/1024)>1024)
        {
            resource_size =df.format(((double)size/1024/1024))+"MB";
        }
        else
        {
            resource_size =df.format(((double)size/1024))+"KB";
        }
        return resource_size;
    }

    public static long getFileOrDirSize(File file) {
        if (!file.exists()) return 0;
        if (!file.isDirectory()) return file.length();

        long length = 0;
        File[] list = file.listFiles();
        if (list != null) { // 文件夹被删除时, 子文件正在被写入, 文件属性异常返回null.
            for (File item : list) {
                length += getFileOrDirSize(item);
            }
        }

        return length;
    }

    /**
     * 获取缓存大小
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context)  {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
//            File file =new File(OtherFinals.PATH_APK);
//            if (file.exists()){
//                cacheSize += getFolderSize(file);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFormatSize(cacheSize);
    }


    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 清除缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir(),false);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir(),false);
        }
//        File apkfile =new File(OtherFinals.PATH_APK);
//        if (apkfile.exists()){
//            deleteDir(apkfile);
//        }
    }

    /**
     * 清理指定目录缓存数据
     * @param uid
     * @param dir
     */
    public static void cleanUIDCacheData(String uid,File dir){
        if(null==dir || !dir.exists())
            return;
        deleteFilesByDirectory(dir,uid);
    }

    private static void deleteFilesByDirectory(File directory,String containsStr) {
        boolean useFilter=!TextUtils.isEmpty(containsStr);
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                if(useFilter){
                    if(!item.getName().contains(containsStr)){
                        continue;
                    }
                }
                item.delete();
            }
        }
    }

    public static long getDirFileSize(String containsStr,File directory){
        long cacheSize=0l;
        boolean useFilter=!TextUtils.isEmpty(containsStr);
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                if(useFilter) {
                    if (!item.getName().contains(containsStr)) {
                        continue;
                    }
                }
                cacheSize += item.length();
            }
        }
        return cacheSize;
    }

}
