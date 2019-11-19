package com.highgreat.education.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by chengbin on 2016/1/5.
 * 属性动画工具类，view 相对于屏幕的左上右下执行属性动画，也可以执行属性动画集，例如，左上、左下、右上、右下
 */
public class ValueAnimUtils {

    /**
     * 属性动画  view相对于屏幕左边执行属性动画
     *
     * @param ofIntstart   动画开始的像素值
     * @param ofFIntEnd     动画结束的像素值
     * @param view           执行动画的view
     * @param viewLeftMargin view距离屏幕左侧的像素值
     * @param duration       动画执行的时间
     */

    public static void leftAnimator(int ofIntstart, int ofFIntEnd, final View view, final int viewLeftMargin, long duration) {

        ValueAnimator mValueAnimator = ValueAnimator.ofInt(ofIntstart, ofFIntEnd);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                Integer animatedValue = (Integer) va.getAnimatedValue();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                marginLayoutParams.leftMargin = animatedValue + viewLeftMargin;
                view.setLayoutParams(marginLayoutParams);
            }
        });
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }

    public static void topAnimator(int ofIntstart, int ofFIntEnd, final View view, final int viewMarginTop, long duration) {

        ValueAnimator mValueAnimator = ValueAnimator.ofInt(ofIntstart, ofFIntEnd);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                Integer animatedValue = (Integer) va.getAnimatedValue();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                marginLayoutParams.topMargin = animatedValue + viewMarginTop;
                view.setLayoutParams(marginLayoutParams);
            }
        });
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }

    public static void rigthAnimator(int ofIntstart, int ofFIntEnd, final View view, final int viewMarginRight, long duration) {

        ValueAnimator mValueAnimator = ValueAnimator.ofInt(ofIntstart, ofFIntEnd);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                Integer animatedValue = (Integer) va.getAnimatedValue();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                marginLayoutParams.rightMargin = animatedValue + viewMarginRight;
                view.setLayoutParams(marginLayoutParams);
            }
        });
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }


    public static void bottomAnimator(int ofIntstart, int ofFIntEnd, final View view, final int viewMarginBottom, long duration) {

        ValueAnimator mValueAnimator = ValueAnimator.ofInt(ofIntstart, ofFIntEnd);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                Integer animatedValue = (Integer) va.getAnimatedValue();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                marginLayoutParams.bottomMargin = animatedValue + viewMarginBottom;
                view.setLayoutParams(marginLayoutParams);
            }
        });
        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }

    public  static  void  alphaAnimator(final View view, float starAlpha, float endAlpha, long duration ){
        ValueAnimator mValueAnimator = ValueAnimator.ofFloat(starAlpha, endAlpha);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator va) {
                float animatedValue = (Float) va.getAnimatedValue();
                view.setAlpha(animatedValue);
            }
        });

        mValueAnimator.setDuration(duration);
        mValueAnimator.start();
    }

}
