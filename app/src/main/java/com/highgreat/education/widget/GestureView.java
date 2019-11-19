package com.highgreat.education.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.highgreat.education.common.FlightControlCode;

/**
 * Created by Din on 2016/9/21.
 */
public class GestureView extends View {
    private Paint paint;

    private float startX;
    private float startY;
    private float endX;
    private float endY;

    private boolean isSendingCmd = false;
    private boolean isOver = false;

    private DrawResultListner listner;

    public GestureView(Context context) {
        this(context, null);
    }

    public GestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void addListner(DrawResultListner listner){
        this.listner = listner;
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#FFFFFF"));
        paint.setStrokeWidth(33);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isSendingCmd){
            return false;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOver=false;
                startX = event.getX();
                startY = event.getY();
                endX = event.getX();
                endY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                endX = event.getX();
                endY = event.getY();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isSendingCmd = true;
                        if (Math.abs(endY - startY) > Math.abs(endX - startX)) {//y轴绝对值大于X轴，所以判断为向前或者向后翻滚
                            if (endY > startY) {//向后翻滚
                                sendCmd(FlightControlCode.MODE_FLIP_BACK);
                            } else {//向前翻滚
                                sendCmd(FlightControlCode.MODE_FLIP_FORWARD);
                            }
                        } else {//左右翻滚
                            if (endX > startX) {//向右翻滚
                                sendCmd(FlightControlCode.MODE_FLIP_RIGHT);
                            } else {//向左翻滚
                                sendCmd(FlightControlCode.MODE_FLIP_LEFT);
                            }
                        }
                        startX = 0;
                        startY = 0;
                        endX = 0;
                        endY = 0;
                    }
                },500);
                invalidate();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!isOver) {
            canvas.drawLine(startX, startY, endX, endY, paint);
        }
    }

    private void sendCmd(short cmd){
        isSendingCmd = false;

        isOver = true;
        if(listner != null){
            listner.over(cmd);
        }
        postInvalidate();
    }

    public interface DrawResultListner{
        void over(short cmd);
    }


}