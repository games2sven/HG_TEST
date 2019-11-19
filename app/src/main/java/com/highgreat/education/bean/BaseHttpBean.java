package com.highgreat.education.bean;

import java.io.Serializable;

/**
 * 网络请求返回数据基类bean
 */
public class BaseHttpBean implements Serializable {
  protected static final long serialVersionUID = 1L;

  protected int status; //1：表示成功, 其他均为错误
  protected String tips; //提示信息

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getTips() {
    return tips;
  }

  public void setTips(String tips) {
    this.tips = tips;
  }

  @Override public String toString() {
    return "BaseHttpBean{" +
        "status=" + status +
        ", tips='" + tips + '\'' +
        '}';
  }
}
