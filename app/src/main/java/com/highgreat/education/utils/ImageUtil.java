package com.highgreat.education.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/3/23 11:33
 * 修改人：mac-likh
 * 修改时间：16/3/23 11:33
 * 修改备注：
 */
public class ImageUtil {

  /**
   * Get bitmap from specified image path
   */
  public static Bitmap getBitmap(String imgPath) {
    // Get bitmap through image path
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    newOpts.inJustDecodeBounds = false;
    newOpts.inPurgeable = true;
    newOpts.inInputShareable = true;
    // Do not compress
    newOpts.inSampleSize = 1;
    newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
    return BitmapFactory.decodeFile(imgPath, newOpts);
  }

  /**
   * Store bitmap into specified image path
   *
   * @throws FileNotFoundException
   */
  public static void storeImage(Bitmap bitmap, String outPath) throws FileNotFoundException {
    FileOutputStream os = new FileOutputStream(outPath);
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
  }

  /**
   * Compress image by pixel, this will modify image width/height.
   * Used to get thumbnail
   *
   * @param imgPath image path
   * @param pixelW target pixel of width
   * @param pixelH target pixel of height
   */
  public static Bitmap ratio(String imgPath, float pixelW, float pixelH) {
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    // 开始读入图片，此时把options.inJustDecodeBounds 设回true，即只读边不读内容
    newOpts.inJustDecodeBounds = true;
    newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
    // Get bitmap info, but notice that bitmap is nul now
    Bitmap bitmap = BitmapFactory.decodeFile(imgPath, newOpts);

    newOpts.inJustDecodeBounds = false;
    int w = newOpts.outWidth;
    int h = newOpts.outHeight;
    // 想要缩放的目标尺寸
    float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
    float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
    // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    int be = 1;//be=1表示不缩放
    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
      be = (int) (newOpts.outWidth / ww);
    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
      be = (int) (newOpts.outHeight / hh);
    }
    if (be <= 0) be = 1;
    newOpts.inSampleSize = be;//设置缩放比例
    // 开始压缩图片，注意此时已经把options.inJustDecodeBounds 设回false了
    bitmap = BitmapFactory.decodeFile(imgPath, newOpts);
    // 压缩好比例大小后再进行质量压缩
    //        return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
    return bitmap;
  }

  /**
   * Compress image by size, this will modify image width/height.
   * Used to get thumbnail
   *
   * @param pixelW target pixel of width
   * @param pixelH target pixel of height
   */
  public static Bitmap ratio(Bitmap image, float pixelW, float pixelH) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    image.compress(Bitmap.CompressFormat.JPEG, 100, os);
    if (os.toByteArray().length / 1024
        > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
      os.reset();//重置baos即清空baos
      image.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
    }
    ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
    BitmapFactory.Options newOpts = new BitmapFactory.Options();
    //开始读入图片，此时把options.inJustDecodeBounds 设回true了
    newOpts.inJustDecodeBounds = true;
    newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
    Bitmap bitmap = BitmapFactory.decodeStream(is, null, newOpts);
    newOpts.inJustDecodeBounds = false;
    int w = newOpts.outWidth;
    int h = newOpts.outHeight;
    float hh = pixelH;// 设置高度为240f时，可以明显看到图片缩小了
    float ww = pixelW;// 设置宽度为120f，可以明显看到图片缩小了
    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
    int be = 1;//be=1表示不缩放
    if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
      be = (int) (newOpts.outWidth / ww);
    } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
      be = (int) (newOpts.outHeight / hh);
    }
    if (be <= 0) be = 1;
    newOpts.inSampleSize = be;//设置缩放比例
    //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
    is = new ByteArrayInputStream(os.toByteArray());
    bitmap = BitmapFactory.decodeStream(is, null, newOpts);
    //压缩好比例大小后再进行质量压缩
    //      return compress(bitmap, maxSize); // 这里再进行质量压缩的意义不大，反而耗资源，删除
    return bitmap;
  }

  /**
   * Compress by quality,  and generate image to the path specified
   *
   * @param maxSize target will be compressed to be smaller than this size.(kb)
   * @throws IOException
   */
  public static void compressAndGenImage(Bitmap image, String outPath, int maxSize)
      throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    // scale
    int options = 100;
    // Store the bitmap into output stream(no compress)
    image.compress(Bitmap.CompressFormat.JPEG, options, os);
    // Compress by loop
    while (os.toByteArray().length / 1024 > maxSize) {
      // Clean up os
      os.reset();
      // interval 10
      options -= 10;
      image.compress(Bitmap.CompressFormat.JPEG, options, os);
    }

    // Generate compressed image file
    FileOutputStream fos = new FileOutputStream(outPath);
    fos.write(os.toByteArray());
    fos.flush();
    fos.close();
  }


  /**
   * 回收图片资源
   */
  public static void recycleImageSource(Bitmap bmp) {
    if (bmp != null && !bmp.isRecycled()) {
      bmp.recycle();   //回收图片所占的内存
      System.gc();  //提醒系统及时回收
    }
  }

  /**
   * 清空图片的内存
   */
  public static void clearImgMemory(View V) {

    if (V instanceof ImageView) {
      Drawable d = ((ImageView) V).getDrawable();
      if (d != null && d instanceof BitmapDrawable) {
        Bitmap bmp = ((BitmapDrawable) d).getBitmap();
        if (bmp != null && !bmp.isRecycled()) {
          bmp.recycle();
          System.gc();
          bmp = null;
        }
      }
      ((ImageView) V).setImageBitmap(null);
      if (d != null) {
        d.setCallback(null);
      }
    }
  }

  /**
   * 保存文件
   *
   * @throws IOException
   */
  public static File saveBitmapToFile(Bitmap bm, String filePath, String fileName) {
    File dirFile = new File(filePath);
    if (!dirFile.exists()) {
      dirFile.mkdirs();
    }
    try {
      File myCaptureFile = new File(filePath +"/"+ fileName);
      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
      bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
      bos.flush();
      bos.close();
      return myCaptureFile;
    } catch (IOException e) {
//      e.printStackTrace();
      LogUtil.e(e.getMessage());
    }
    return null;
  }

  /**
   * 获取图片宽高
   * @param path 图片地址
   * @return
     */
  public static int[] getBitmapWH(String path){
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds  = true;
    BitmapFactory.decodeFile(path, options);
    return new int[]{options.outWidth,options.outHeight};
  }


  public static int getBitmapeDegree(String path){
    int degree = 0;
    try {
      // 从指定路径下读取图片，并获取其EXIF信息
      ExifInterface exifInterface = new ExifInterface(path);
      // 获取图片的旋转信息
      int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
      switch (orientation) {
        case ExifInterface.ORIENTATION_ROTATE_90:
          degree = 90;
          break;
        case ExifInterface.ORIENTATION_ROTATE_180:
          degree = 180;
          break;
        case ExifInterface.ORIENTATION_ROTATE_270:
          degree = 270;
          break;
      }
    } catch (Exception e) {
    }
    return degree;
  }

  public static Bitmap bitmapRotate(Bitmap bitmap, int angle){

    Matrix matrix = new Matrix();
    matrix.postRotate(angle);
    Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    return rotateBitmap;
  }
  public static Bitmap bitmapRotate(String path){

    Bitmap bitmap = BitmapFactory.decodeFile(path);

    Matrix matrix = new Matrix();
    matrix.postRotate(getBitmapeDegree(path));
    Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    bitmap.recycle();
    return rotateBitmap;
  }

}
