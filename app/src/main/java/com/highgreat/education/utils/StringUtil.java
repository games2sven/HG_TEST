package com.highgreat.education.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StringUtil {

  /**
   * 字符串是否为空
   *
   * @param string
   * @return
   */
  public static boolean isEmpty(String string) {
    if (null == string || "null".equalsIgnoreCase(string)) {
      return true;
    }
    if (0 == string.trim().length() || 0 == string.replace(" ", "").length()) {
      return true;
    }

    return false;
  }

  /**
   * 判断字符串是否是整数
   */
  public static boolean isInteger(String value) {
    try {
      Integer.parseInt(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * 判断字符串是否是浮点数
   */
  public static boolean isDouble(String value) {
    try {
      Double.parseDouble(value);
      if (value.contains("."))
        return true;
      return false;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * 判断字符串是否是数字
   */
  public static boolean isNumber(String value) {
    return isInteger(value) || isDouble(value);
  }

  /**
   * 格式化日期 yyyy-MM-dd
   *
   * @param milliseconds
   *            日期的毫秒数
   * @return
   */
  public static String getDate(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return simpleDateFormat.format(date);
  }

  /**
   * 格式化日期 yyyy-MM-dd
   *
   * @param milliseconds
   *            日期的毫秒数
   * @return
   */
  public static String getAnotherDate(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy/MM/dd HH:mm:ss
   *
   * @param milliseconds
   * @return
   */
  public static String getDetailDate(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy-MM-dd HH:mm
   *
   * @param milliseconds
   * @return
   */
  public static String getDetailDateNoM(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * HH:mm:ss
   *
   * @param milliseconds
   * @return
   */
  public static String getDetailDate1(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy-MM-dd HH:mm:ss
   *
   * @param milliseconds
   * @return
   */
  public static String getDetailViolationDate(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * HH:mm
   *
   * @param milliseconds
   * @return
   */
  public static String getDetailViolationTime(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy/MM/dd E
   *
   * @param milliseconds
   * @return
   */
  public static String getDetailWeekDate(long milliseconds) {
    Date date = new Date(milliseconds);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd E");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy/MM/dd HH:mm
   *
   * @return
   */
  public static String getCurrentDetailDate() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy/MM/dd HH:mm:ss
   *
   * @return
   */
  public static String getCurrentDetailDateSecond() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * E
   * @return
   */
  public static String getCurrentDetailYear() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy/
   *
   * @return
   */
  public static String getCurrentWeek() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * yyyy/MM/dd HH:mm
   *
   * @return
   */
  public static String getCurrentMonthDayDate() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * MM/dd
   *
   * @return
   */
  public static String getCurrentMonthDate() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * MM/dd
   *
   * @return
   */
  public static String getYearMonthDate() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * MM/dd
   *
   * @return
   */
  public static String getCurrentYearMonthDate() {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return simpleDateFormat.format(date);
  }

  /**
   *
   * MM/dd
   *
   * @return
   */
  public static int getWeekOfYear() {
    int week = 0;
    Calendar calendar = Calendar.getInstance();
    try {
      week = calendar.get(Calendar.WEEK_OF_YEAR);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return week;
  }

  /**
   *
   * yyyy/MM/dd HH:mm
   *
   * @param date
   * @return
   */
  public static long getMillisecondsBydate(String date) {
    // 注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
    Date millDate = new Date();
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
      // 必须捕获异常
      try {
        if (date != null && !date.equals(""))
          millDate = simpleDateFormat.parse(date);
      } catch (java.text.ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    return millDate.getTime();
  }

  /**
   *
   * yyyy-MM-dd HH:mm
   *
   * @param date
   * @return
   */
  public static long getMillisecondsBydateA(String date) {
    // 注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
    Date millDate = new Date();
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
      // 必须捕获异常
      try {
        if (date != null && !date.equals(""))
          millDate = simpleDateFormat.parse(date);
      } catch (java.text.ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    return millDate.getTime();
  }

  /**
   *
   * yyyy/MM/dd
   *
   * @param date
   * @return
   */
  public static long getMillisecondsByDateDay(String date) {
    // 注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    // 必须捕获异常
    Date millDate = new Date();
    try {
      if (date != null && !date.equals(""))
        millDate = simpleDateFormat.parse(date);
    } catch (java.text.ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return millDate.getTime();
  }

  /**
   * 判断数据库数据是否超过3天
   *
   * @param sqlTime
   * @param sysTime
   * @return
   */
  public static boolean getThreeDay(long sqlTime, long sysTime) {
    long time = sysTime - sqlTime;
    if (time >= 24 * 60 * 60 * 1000 * 3)
      return true;
    else
      return false;
  }

  /**
   * @param date
   * @param formatStr
   * @return
   */
  public static long getMillisecondsByFormatDate(String date, String formatStr) {
    // 注意：SimpleDateFormat构造函数的样式与strDate的样式必须相符
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);
    // 必须捕获异常
    Date millDate = new Date();
    try {
      if (date != null && !date.equals(""))
        millDate = simpleDateFormat.parse(date);
    } catch (java.text.ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return millDate.getTime();
  }


  /**
   * 仅适用于图片删除时的字符串切割
   */
  public static String splitStrForPic(String url) {
    String substring = null;
    try {
        if (!url.contains("/")) return url;

      int indexOf = url.lastIndexOf("/");
      //            int indexOf = url.indexOf("/pic/");
      if (indexOf != -1) {
        //                substring = url.substring(indexOf + 5, url.length());
        substring = url.substring(indexOf + 1, url.length());
      }
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }

    return substring;
  }

  /**
   * 仅适用于视频
   * 删除时的字符串切割
   */
  public static String splitStrForVideo(String url) {
    String substring = url;
    try {
        if (!url.contains("/")) return url;
      int indexOf = url.lastIndexOf("/");
      if (indexOf != -1) {
        substring = url.substring(indexOf + 1, url.length());
      }
      //            int indexOf = url.indexOf("/video/");
      //            substring = url.substring(indexOf + 7, url.length());
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }
    return substring;
  }

  /**
   * 将String型格式化,比如想要将2011-11-11格式化成2011年11月11日,就StringPattern("2011-11-11","yyyy-MM-dd","yyyy年MM月dd日").
   *
   * @param date String 想要格式化的日期
   * 想要格式化的日期的现有格式
   * String 想要格式化成什么格式
   * @return String
   */
  static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");        // 实例化模板对象
  static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");        // 实例化模板对象
  public static final String stringTimePattern(String date) {
    if (date == null ) return "";
    Date d = null;
    try {
      d = sdf1.parse(date);   // 将给定的字符串中的日期提取出来
    } catch (Exception e) {            // 如果提供的字符串格式有错误，则进行异常处理
//      e.printStackTrace();       // 打印异常信息
      LogUtil.e(e.getMessage());
    }
    return sdf2.format(d);
  }

  /**
   * 比较日期大小
   */
  public static int compare_date(String DATE1, String DATE2) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    try {
      Date dt1 = df.parse(DATE1);
      Date dt2 = df.parse(DATE2);
      if (dt1.getTime() > dt2.getTime()) {
        return 1;
      } else if (dt1.getTime() < dt2.getTime()) {
        return -1;
      } else {
        return 0;
      }
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }
    return 0;
  }

  /**
   * 集合按Key排序
   */
  public static Map<String, List<String>> sortMapByKey(Map<String, List<String>> map) {
    if (map == null || map.isEmpty()) {
      return null;
    }

    Map<String, List<String>> sortMap = new TreeMap<String, List<String>>(new MapKeyComparator());
    sortMap.putAll(map);
    return sortMap;
  }

  /**
   * 集合按Key排序
   */
  public static Map<String, String> sortMapStringByKey(Map<String, String> map) {

    Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
    sortMap.putAll(map);
    return sortMap;
  }

  public static String splitMp4(String url) {
    String substring = null;
    try {
      int indexOf = url.indexOf(".mp4");
      substring = url.substring(0, indexOf);
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }
    return substring;
  }

  public static String splitUnicodeForPic(String url) {
    String substring = null;
    try {
      int indexOf = url.indexOf(".jpg");
      substring = url.substring(0, indexOf + 4);
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }

    return substring;
  }

  public static float splitSize(String currentSize) {
    int indexOf = 0;
    float dimen=1  ;
    String substring = null;
    float result = 0;
    try {
      if (currentSize.contains("KB")) {
        indexOf = currentSize.indexOf("KB");
        dimen=0.001f;
      } else if (currentSize.contains("MB")) {
        indexOf = currentSize.indexOf("MB");
        dimen=1;
      } else if (currentSize.contains("GB")){
        indexOf =currentSize.indexOf("GB");
        dimen=1000;
      }
      substring = currentSize.substring(0, indexOf);
      result = Float.parseFloat(substring)*dimen;
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }
    return result;
  }

  /**
   * 字符串切割
   */
  public static String splitStr(String str, String splitFlg) {
    String substring = null;
    try {
      int indexOf = str.lastIndexOf(splitFlg);
      if (indexOf != -1) {
        substring = str.substring(0, indexOf);
      }
    } catch (Exception exception) {
//      exception.printStackTrace();
      LogUtil.e(exception.getMessage());
    }

    return substring;
  }
}
