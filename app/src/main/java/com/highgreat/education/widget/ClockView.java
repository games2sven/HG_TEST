package com.highgreat.education.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import me.lxw.dtl.utils.DTLUtils;

/**
 * Created by Din on 2016/9/21.
 */
public class ClockView extends View {
    private float mSweep = 0;
    private int   max    = 10000;
    private Paint paint;
    private Paint paint2;
    private RectF oval;
    private int   center = 0;
    private float alpha  = 11 / 20;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DTLUtils.init(context);
        init();
    }

    private void init() {
        float gap = DTLUtils.getPixels(10);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor((int) (0xff * alpha) << 24);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setStrokeWidth(gap);
        paint2.setColor(0xffffffff);
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setBackGroundAlpha(float alpha) {
        this.alpha = alpha;
        if (paint == null) return;
        paint.setColor((int) (0xff * alpha) << 24);

    }

    public void updateProgress(int progress) {
        mSweep = progress * 360 / max;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (center == 0) {
            center = getWidth() / 2; //获取圆心的x坐标
            int radius = (center - DTLUtils.getPixels(20)); //圆环的半径
            oval = new RectF(center - radius, center - radius, center + radius,
                    center + radius);  //用于定义的圆弧的形状和大小的界限
        }

        canvas.drawCircle(center, center, center, paint);
        canvas.drawArc(oval, -90, mSweep, false, paint2);
    }

}