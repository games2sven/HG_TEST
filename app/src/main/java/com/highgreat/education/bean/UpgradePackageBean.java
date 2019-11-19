package com.highgreat.education.bean;

/**
 * Created by yc on 2017/3/3.
 * 升级包信息bean：目前包括app/固件/遥控器
 */
public class UpgradePackageBean extends BaseHttpBean {
  public VersionInfo data;

  public class VersionInfo {
    public int force; //是否是强制升级 1是 0不是
    public String storeurl; //版本文件下载地址
    public String filename; //文件名
    public long filesize; //文件大小，当前下载apk或者patch的大小
    public String md5; //文件md5
    public int versionCode; //版本Code
    public String versionName; //版本Name
    public String records; //版本提示信息，默认中文
    public int type; //升级方式 1差量包 0整包
    public long fullsize; //整包大小，当前最新app版本的整包大小，整包升级时候和filesize大小相同，差量升级时候大于filesize
    public String forceVname;//强升版本号:当前飞机版本小于强制升级版本就需要强制升级，(固件升级需要,app升级不需要)
    public float patchDobbyVersion;//差量包对应的飞机版本号，只在差量包时候有用(差量包开始升级之前检查当前飞机版本是否对应)
    public int otypeId; //模块ID（0/1-TAKE 2-ROLLCAP 3-DANCE 4-HESPER 5-DOBBY-ZERO 99-无)

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
        ", forceVname='" + forceVname + '\'' +
        ", patchDobbyVersion=" + patchDobbyVersion +
        ", otypeId=" + otypeId +
        '}';
    }
  }

  @Override
  public String toString() {
    return "UpgradePackageBean{" +
      "data=" + data +
      '}';
  }
}