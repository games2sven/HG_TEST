package com.highgreat.education.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by yc on 2017/2/9.
 * glide圆角图处理
 */
public class GlideRoundTransform extends BitmapTransformation {

    private float radius      = 0f;
    private float strokeWidth = 0f;
    private Paint borderPaint;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int px) {
        super(context);
        this.radius = px;
    }

    public GlideRoundTransform(Context context, int px, int strokeColor, int strokeWidth) {
        super(context);
        this.radius = px;
        if (strokeWidth > 0) {
            this.strokeWidth = strokeWidth;
            borderPaint = new Paint();
            borderPaint.setDither(true);
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(strokeColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(strokeWidth);
        }
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (borderPaint != null) {
            rectF = new RectF(strokeWidth / 2, strokeWidth / 2, source.getWidth() - strokeWidth / 2, source.getHeight() - strokeWidth / 2);
            canvas.drawRoundRect(rectF, radius - strokeWidth / 2, radius - strokeWidth / 2, borderPaint);
        }
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}
