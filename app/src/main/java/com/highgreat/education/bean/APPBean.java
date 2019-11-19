package com.highgreat.education.bean;

/**
 * Created by chengbin on 2016/8/11.
 * 解析APP相关bean
 */
public class APPBean extends BaseHttpBean  {

    public VersionInfo data;

    public class VersionInfo {
        public int force; //是否是强制升级 1是 0不是
        public String storeurl; //版本文件下载地址
        public String filename; //文件名
        public long   filesize; //文件大小，当前下载apk或者patch的大小
        public String md5; //文件md5
        public int    versionCode; //版本Code
        public String versionName; //版本Name
        public String records; //版本提示信息，默认中文
        public int type; //升级方式 1差量包 0整包
        public long fullsize; //整包大小，当前最新app版本的整包大小，整包升级时候和filesize大小相同，差量升级时候大于filesize

        @Override
        public String toString() {
            return "VersionInfo{" +
                    "force=" + force +
                    ", storeurl='" + storeurl + '\'' +
                    ", filename='" + filename + '\'' +
                    ", filesize=" + filesize +
                    ", md5='" + md5 + '\'' +
                    ", versionCode=" + versionCode +
                    ", versionName='" + versionName + '\'' +
                    ", records='" + records + '\'' +
                    ", type=" + type +
                    ", fullsize=" + fullsize +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "APPBean{" +
                "data=" + data +
                '}';
    }

}