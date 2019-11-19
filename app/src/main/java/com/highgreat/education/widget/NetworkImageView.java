package com.highgreat.education.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.common.EventBusCode;
import com.highgreat.education.common.UavConstants;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import me.lxw.dtl.utils.DTLUtils;


/**
 * Created by Din on 2016/10/8.
 */

public class NetworkImageView extends ImageView {
    private Drawable mPlaceHolderDrawable;
    private Drawable mFailureDrawable;
    private ScaleType mScaleType            = ScaleType.FIT_CENTER; //
    private ScaleType mPlaceHolderScaleType = ScaleType.FIT_CENTER;
    private ScaleType mErrorScaleType       = ScaleType.CENTER;
    private boolean             isRoundAsCircle       = false;
    private int                 round                 = -1;
    private int                 mFadeDuration         = 300;
    private ZODiskCacheStrategy mDiskCacheStrategy    = ZODiskCacheStrategy.SOURCE;
    private int resizeWidth;
    private int resizeHeight;
    /**
     * 设置占位图和失败图缩放类型
     */
    private RequestListener<Object, GlideDrawable> mRequestListener       = new RequestListener<Object, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, Object model, Target<GlideDrawable> target, boolean isFirstResource) {
            if (mFailureDrawable != null) {
                setOriginScaleType(mErrorScaleType); //失败图scaleType
            }
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, Object model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            setOriginScaleType(mScaleType); //还原默认scaleType
            EventBus.getDefault().post(new EventCenter(EventBusCode.CODE_LOAD_WELCOME_PICTURE));
            return false;
        }
    };
    private int                                    mPlaceHolderResourceId = 0;
    private int                                    mFailResourceId        = 0;
    private int                                    borderColor            = 0;
    private int                                    borderWidth            = 0;
    private int viewWidth,viewHeight;

    public NetworkImageView(Context context) {
        super(context);
        init(context,null);
    }

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public NetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        DTLUtils.init(getContext());
        mScaleType = getScaleType();
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        mScaleType = scaleType;
    }

    public NetworkImageView setNetImageScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
        mScaleType = scaleType;
        return this;
    }

    public NetworkImageView setRoundAsCircle(boolean isRoundAsCircle) {
        this.isRoundAsCircle = isRoundAsCircle;
        return this;
    }

    public NetworkImageView setDiskCacheStrategy(ZODiskCacheStrategy mDiskCacheStrategy) {
        this.mDiskCacheStrategy = mDiskCacheStrategy;
        return this;
    }

    public NetworkImageView setRoundingParams(int round) {
        this.round = round;
        return this;
    }

    public void setRoundingBorderWidth(int borderColor, int borderWidth) {
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
    }

    public NetworkImageView setFadeDuration(int fadeDuration) {
        this.mFadeDuration = fadeDuration;
        return this;
    }

    public void setAutoPlay(boolean b) {
    }

    public void startPlay() {
    }

    public void stopPlay() {
    }

    public NetworkImageView override(int width, int height) {
        this.resizeWidth = width;
        this.resizeHeight = height;
        return this;
    }

    /**
     * 设置失败图
     *
     * @param drawable
     * @param scaleType
     */
    public NetworkImageView setFailureImage(Drawable drawable, ImageView.ScaleType scaleType) {
        this.mFailureDrawable = drawable;
        this.mErrorScaleType = scaleType;
        return this;
    }

    /**
     * 设置失败图
     *
     * @param drawable
     */
    public NetworkImageView setFailureImage(Drawable drawable) {
        setFailureImage(drawable, ImageView.ScaleType.CENTER);
        return this;
    }

    /**
     * 设置失败图
     *
     * @param resourceId
     */
    public NetworkImageView setFailureImage(int resourceId) {
        return setFailureImage(resourceId, ImageView.ScaleType.CENTER);
    }

    /**
     * 设置失败图
     *
     * @param resourceId
     * @param scaleType
     */
    public NetworkImageView setFailureImage(int resourceId, ImageView.ScaleType scaleType) {
        Drawable drawable;
        if (mFailResourceId == resourceId) {
            drawable = mFailureDrawable;
        } else {
            mFailResourceId = resourceId;
            drawable = getResources().getDrawable(resourceId);
        }
        return setFailureImage(drawable, scaleType);
    }

    /**
     * 设置占位图
     *
     * @param drawable
     * @param scaleType
     */
    public NetworkImageView setPlaceholderImage(Drawable drawable, ImageView.ScaleType scaleType) {
        this.mPlaceHolderDrawable = drawable;
        this.mPlaceHolderScaleType = scaleType;
        return this;
    }

    /**
     * 设置占位图
     *
     * @param drawable
     */
    public NetworkImageView setPlaceholderImage(Drawable drawable) {
        return setPlaceholderImage(drawable, ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 设置占位图
     *
     * @param resourceId
     */
    public NetworkImageView setPlaceholderImage(int resourceId) {
        return setPlaceholderImage(resourceId, ImageView.ScaleType.CENTER_CROP);
    }

    /**
     * 设置占位图
     *
     * @param resourceId
     * @param scaleType
     */
    public NetworkImageView setPlaceholderImage(int resourceId, ImageView.ScaleType scaleType) {
        Drawable drawable;
        if (mPlaceHolderResourceId == resourceId) {
            drawable = mPlaceHolderDrawable;
        } else {
            mPlaceHolderResourceId = resourceId;
            drawable = getResources().getDrawable(resourceId);
        }
        return setPlaceholderImage(drawable, scaleType);
    }

    /**
     * 设置资源id
     *
     * @param resId
     */
    public void displayImage(int resId) {
        setImage(resId);
    }

    /**
     * 设置文件路径、uri或者url
     *
     * @param str
     */
    public void displayImage(String str) {
        setImage(str);
    }

    public void  setImage(Object image) {
        setOriginScaleType(mPlaceHolderScaleType); //占位图scaleType
        if(getContext() instanceof Activity) {
            if(((Activity) getContext()).isFinishing() || ((Activity) getContext()).isDestroyed()) {
                return;
            }
        }
        setOriginScaleType(mPlaceHolderScaleType);
        DrawableTypeRequest<Object> drawableTypeRequest = Glide.with(getContext()).load(image);
        drawableTypeRequest.placeholder(mPlaceHolderDrawable)
                .error(mFailureDrawable)
                .listener(mRequestListener);
        if (resizeWidth != 0 && resizeHeight != 0) {
            drawableTypeRequest.override(resizeWidth, resizeHeight);
        }
        if (isRoundAsCircle) {
            drawableTypeRequest.transform(new GlideCircleTransform(getContext()));
        }
        if (round != -1) {
//            drawableTypeRequest.transform(new GlideRoundTransform(getContext(), round, borderColor, borderWidth));
            drawableTypeRequest.transform(new GlideRoundTransform(getContext(), DTLUtils.getPixels(round), borderColor, DTLUtils.getPixels(borderWidth)));
        }
        DiskCacheStrategy diskCacheStrategy = DiskCacheStrategy.SOURCE;
        if (mDiskCacheStrategy == ZODiskCacheStrategy.ALL) {
            diskCacheStrategy = DiskCacheStrategy.ALL;
        } else if (mDiskCacheStrategy == ZODiskCacheStrategy.NONE || image instanceof Integer || (image instanceof String && new File((String) image).exists())) {
            diskCacheStrategy = DiskCacheStrategy.NONE;
        } else if (mDiskCacheStrategy == ZODiskCacheStrategy.RESULT) {
            diskCacheStrategy = DiskCacheStrategy.RESULT;
        } else if (mDiskCacheStrategy == ZODiskCacheStrategy.SOURCE) {
            diskCacheStrategy = DiskCacheStrategy.SOURCE;
        }
        drawableTypeRequest.diskCacheStrategy(diskCacheStrategy);
        drawableTypeRequest.crossFade(mFadeDuration);
        drawableTypeRequest.into(this);
    }

    private void setOriginScaleType(ScaleType scaleType) {
        super.setScaleType(scaleType);
    }

    public enum ZODiskCacheStrategy {
        /**
         * Caches with both {@link #SOURCE} and {@link #RESULT}.
         */
        ALL,
        /**
         * Saves no data to cache.
         */
        NONE,
        /**
         * Saves just the original data to cache.
         */
        SOURCE,
        /**
         * Saves the media item after all transformations to cache.
         */
        RESULT
    }
}
