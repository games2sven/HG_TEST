package com.highgreat.education.bean;

/**
 * Created by user on 2017/4/12.
 */

public class DownloadBean {

    public String ketTime;
    public String url;
    public String result; //兼容视频下载特有

    public DownloadBean(String ketTime, String url, String result){
        this.ketTime = ketTime;
        this.url = url;
        this.result = result;
    }
}
