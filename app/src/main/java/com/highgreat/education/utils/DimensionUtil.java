package com.highgreat.education.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：尺寸工具
 * 创建人：mac-likh
 * 创建时间：16/1/12 15:00
 * 修改人：mac-likh
 * 修改时间：16/1/12 15:00
 * 修改备注：
 */
@SuppressWarnings("unused") public class DimensionUtil {

  /**
   * px转dp
   *
   * @return 转换值
   */
  public static int px2dp(Context context, float px) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (px / scale + 0.5F);
  }

  /**
   * dp转px
   *
   * @return 转换值
   */
  public static int dp2px(Context context, float dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5F);
  }

  /**
   * px转sp
   *
   * @return 转换值
   */
  public static int px2sp(Context context, float px) {
    final float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (px / scale + 0.5F);
  }

  /**
   * sp转px
   *
   * @return 转换值
   */
  public static int sp2px(Context context, float sp) {
    final float scale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (sp * scale + 0.5F);
  }

  /**
   * 使用TypedValue类将dp转px
   *
   * @return 转换值
   */
  public static int dp2px(Context context, int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
        context.getResources().getDisplayMetrics());
  }

  /**
   * 使用TypedValue类将sp转px
   *
   * @return 转换值
   */
  public static int sp2px(Context context, int sp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
        context.getResources().getDisplayMetrics());
  }

  public static int getSystemScreenWidth(Context context) {

    if (context==null) return 0;
    return context.getResources().getDisplayMetrics().widthPixels;

  }

  public static int getSystemScreenHeight(Context context) {
    if (context==null) return 0;
    return context.getResources().getDisplayMetrics().heightPixels;
  }
}
