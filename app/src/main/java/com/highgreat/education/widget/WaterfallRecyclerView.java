package com.highgreat.education.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by app_hg on 2017/9/14.
 */

public class WaterfallRecyclerView extends RecyclerView {
    public WaterfallRecyclerView(Context context) {
        super(context);
    }

    public WaterfallRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WaterfallRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        //return super.onTouchEvent(e);
        return false;
    }
}
