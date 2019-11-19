package com.highgreat.education.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.highgreat.education.utils.LogUtil;


/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/3/17 10:17
 * 修改人：mac-likh
 * 修改时间：16/3/17 10:17
 * 修改备注：
 */
public class HackyViewPager extends ViewPager {//LazyViewPager {

    private boolean isLocked;

    public HackyViewPager(Context context) {
        super(context);
        isLocked = false;
    }

    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                LogUtil.e("HackyViewPager", e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !isLocked && super.onTouchEvent(event);
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public boolean isLocked() {
        return isLocked;
    }

}
