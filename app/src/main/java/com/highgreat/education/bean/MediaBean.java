package com.highgreat.education.bean;

public class MediaBean {
    public boolean isVideo;
    public String currentThumbImage;
    public String currentOrgPath;
    public String currentImageTime;
    public String currentSize;

    public MediaBean(boolean isVideo, String currentThumbImage, String currentOrgPath, String currentImageTime, String currentSize) {
        this.isVideo = isVideo;
        this.currentThumbImage = currentThumbImage;
        this.currentOrgPath = currentOrgPath;
        this.currentImageTime = currentImageTime;
        this.currentSize = currentSize;
    }
}
