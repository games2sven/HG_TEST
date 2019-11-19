package com.highgreat.education.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.highgreat.education.R;
import com.highgreat.education.utils.SPUtil;
import com.highgreat.education.utils.UiUtil;
import com.highgreat.education.widget.ClockView;
import com.highgreat.education.widget.NetworkImageView;


/**
 * 媒体库 holder
 * ...
 */
public class CountItemViewHolder extends RecyclerView.ViewHolder {

    public TextView         mp4Duration;
    public ClockView        mProgress;
    public NetworkImageView ivImage;//图片
    public ImageView        imagePlay;//图片
    public ImageView        download_flag;//图片

    public FrameLayout ibSelect;//图片选择图标
    public TextView    select_intitle;//图片选择图标
    public TextView    mTextView;
    public TextView    pic_size;
    /**
     * download id
     */
    public int    id;
    public String url;
    public String key;


    public CountItemViewHolder(View itemView) {
        super(itemView);

        ivImage = (NetworkImageView) itemView.findViewById(R.id.gallery_image);
        if (ivImage != null) {
            ivImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivImage.setPlaceholderImage(R.mipmap.image_placholder);
        }

        imagePlay = (ImageView) itemView.findViewById(R.id.image_play);
        download_flag = (ImageView) itemView.findViewById(R.id.download_flag);
        ibSelect = (FrameLayout) itemView.findViewById(R.id.select_pic);
        mProgress = (ClockView) itemView.findViewById(R.id.gallery_load_progress);
        if (mProgress != null) {
            mProgress.setBackGroundAlpha(0.8f);
        }
        mp4Duration = (TextView) itemView.findViewById(R.id.tv_mp4_duration);

        //头
        mTextView = (TextView) itemView.findViewById(R.id.title);
        select_intitle = (TextView) itemView.findViewById(R.id.select_info);
        pic_size = (TextView) itemView.findViewById(R.id.pic_size);
    }

    public void bindItem(String text) {
        mTextView.setText(text);
    }

    public void update(String url, final int id) {
        this.url = url;
        this.id = id;
    }

    /**
     * 更新下载进度
     *
     * @param status
     * @param sofar
     * @param total
     * @param id
     */
    public void updateDownloading(final int status, final long sofar, final long total, int id) {
        float percent = sofar / (float) total;
        SPUtil.put(UiUtil.getContext(), String.valueOf(id), percent);
        if ((int) mProgress.getTag() == id) {
            mProgress.setMax(100);
            mProgress.updateProgress((int) (percent * 100));
        }
    }

    public void updateDownloaded() {
        if ((int) mProgress.getTag() == id) {
            mProgress.setMax(1);
            mProgress.updateProgress(1);
        }
        SPUtil.remove(UiUtil.getContext(), String.valueOf(id));
    }

    public void updateNotDownloaded(final int status, final long sofar, final long total) {
        if ((int) mProgress.getTag() == id) {
            if (sofar > 0 && total > 0) {
                final float percent = sofar / (float) total;
                mProgress.setMax(100);
                mProgress.updateProgress((int) (percent * 100));
            } else {
                mProgress.setMax(1);
                mProgress.updateProgress(0);
            }
        }
        SPUtil.remove(UiUtil.getContext(), String.valueOf(id));
    }

    public void refreshDownloading() {
        if ((int) mProgress.getTag() == id) {
            float percent = (float) SPUtil.get(UiUtil.getContext(), String.valueOf(id), 0F);
            mProgress.setMax(100);
            mProgress.updateProgress((int) (percent * 100));
        }
    }
    public void update(String url, final int id, String key) {
        this.url = url;
        this.id = id;
        this.key = key;
    }
}
