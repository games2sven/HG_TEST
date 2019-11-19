package com.highgreat.education.utils;

import android.media.MediaMetadataRetriever;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/25 09:40
 * 修改人：mac-likh
 * 修改时间：16/1/25 09:40
 * 修改备注：
 */
public class TimeUtil {
    /**
     * 获取mp4的时间长度
     *
     * @param filePath
     * @return
     */
    public static MediaMetadataRetriever retriever;
    static StringBuilder mFormatBuilder = new StringBuilder();

    /**
     * 计时到分钟
     *
     * @param start 秒
     * @return
     */
   /* public static String toTimeWithMinute(long start) {
        long minute = start / 60;
        long second = start % 60;
        return String.format("%d" + UiUtil.getContext().getString(R.string.minute) + "%d"
                + UiUtil.getContext().getString(R.string.second), minute, second);
    }*/
    static Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    public static String paserTimeToYM(long time) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (time == 0) {//new Date(time) 传0的话 会格式化成1970年
            return "0";
        }
        return format.format(new Date(time));
    }

    public static String paserTimeToHs(long time) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }

    public static String parseTimeToHm(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(time);
        String str = format.format(curDate);
        return str;
    }

    public static String parseLongTime(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年-MM月-dd日 HH:mm:ss");
        Date curDate = new Date(time);
        String str = format.format(curDate);
        return str;
    }


    /**
     * 格式化系统时间
     */
    public static String formatSystemTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss:SSS ");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 格式化系统当前时间
     */
    public static String stringTimePattern(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        Date curDate = new Date(time);//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    /**
     * 格式化系统当前时间
     */
    public static long stringTimeToLong(String time, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
        }
        return System.currentTimeMillis();
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(long smdate, long bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date smdate1 = sdf.parse(sdf.format(new Date(smdate)));
            Date bdate1 = sdf.parse(sdf.format(new Date(bdate)));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate1);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate1);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 计算两个日期之间相差的小时
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static long minituesBetween(long smdate, long bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date smdate1 = sdf.parse(sdf.format(new Date(smdate)));
            Date bdate1 = sdf.parse(sdf.format(new Date(bdate)));
            LogUtil.Lee("online 下载时间:  " + sdf.format(new Date(smdate)));
            LogUtil.Lee("online 当前时间:  " + sdf.format(new Date(bdate)));

            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            // long ns = 1000;
            // 获得两个时间的毫秒时间差异
            long diff = bdate1.getTime() - smdate1.getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;

            return day * 24 * 60 + hour * 60 + min;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1L;
    }

    /**
     * 当前时间是否在同一个30分钟内
     */
    public static boolean isInSameHalfHour(long smdate, long bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
            SimpleDateFormat sdf3 = new SimpleDateFormat("mm");
            Date smdate1 = sdf.parse(sdf.format(new Date(smdate)));
            Date bdate1 = sdf.parse(sdf.format(new Date(bdate)));

            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;

            // 获得两个时间的毫秒时间差异
            long diff = bdate1.getTime() - smdate1.getTime();
            // 计算差多少天
            long day = diff / nd;
            // 计算差多少小时
            //long hour = diff % nd / nh;
            // 计算差多少分钟
            long min = diff % nd % nh / nm;

            //过期
            if (diff < 0) {
                LogUtil.Lee("online 文件过期否:  超过" + diff + "ms 不在一个时区");
                return true;
            }
            if (day > 0) {
                LogUtil.Lee("online 文件过期否:  超过" + day + "天");
                return true;
            }
            String downLoadTimeHH = sdf2.format(new Date(smdate));
            String downLoadTimeMM = sdf3.format(new Date(smdate));

            String currentTimeHH = sdf2.format(new Date(bdate));
            String currentTimeMM = sdf3.format(new Date(bdate));

            int hour = Integer.parseInt(currentTimeHH) - Integer.parseInt(downLoadTimeHH);
            LogUtil.Lee(
                    "online 文件过期否 下载时间:  " + sdf.format(new Date(smdate)) + "  online 当前时间:  " + sdf.format(
                            new Date(bdate)) + " 相差" + day + "天  相差" + hour + "小时  相差" + min + "分");
            //同一个小时
            if (downLoadTimeHH.equals(currentTimeHH)) {
                //1.他两的差大于30  过期
                if (min - 30 >= 0) {
                    LogUtil.Lee("online 文件过期否:  同一个小时 他两的差大于30  过期");
                    return true;
                }
                // 2.小于30
                if (min - 30 < 0) {
                    //创建时间小于3   现在时间大于等于3  过期
                    if (Integer.parseInt(downLoadTimeMM) - 3 < 0
                            && Integer.parseInt(currentTimeMM) - 3 >= 0) {
                        LogUtil.Lee("online 文件过期否:  同一个小时 他两的差小于30  创建时间小于3   现在时间大于等于3  过期");
                        return true;
                    }
                    //创建时间 如果  大于3并且大与33  不过期
                    if (Integer.parseInt(downLoadTimeMM) - 33 > 0) {
                        LogUtil.Lee("online 文件过期否:  同一个小时 他两的差小于30  创创建时间 如果  大于3并且大与33  不过期");
                        return false;
                    }
                    //创建时间 如果  大于3  但是当前时间大与33  过期
                    if (Integer.parseInt(downLoadTimeMM) - 3 > 0 && Integer.parseInt(currentTimeMM) - 33 > 0) {
                        LogUtil.Lee("online 文件过期否:  同一个小时 他两的差小于30  创创建时间 如果  大于3  但是当前时间大与33  过期");
                        return true;
                    }
                    //创建时间 如果  大于  3 切当前时间未超过33  不过期
                    if (Integer.parseInt(downLoadTimeMM) - 3 > 0
                            && Integer.parseInt(currentTimeMM) - 33 < 0) {
                        LogUtil.Lee("online 文件过期否:  同一个小时 他两的差小于30  创建时间 如果  大于  3 切当前时间未超过 33  不过期");
                        return false;
                    }
                }
            } else {
                //跨大于1个小时
                if (hour >= 2 || hour <= -2) {
                    LogUtil.Lee("online 文件过期否:  跨大于1个小时 过期");
                    return true;
                }
                //创建时间大于33
                if (Integer.parseInt(downLoadTimeMM) - 33 >= 0) {
                    //现在时间大于等于3  过期
                    if (Integer.parseInt(currentTimeMM) - 3 >= 0) {
                        LogUtil.Lee("online 文件过期否: 补一个小时 创建时间大于33  现在时间大于等于3  过期 ");
                        return true;
                    } else {
                        //现在时间小于3  不过期
                        LogUtil.Lee("online 文件过期否: 补一个小时 现在时间小于3  不过期 ");
                        return false;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LogUtil.Lee("online 文件过期否: 默认  不过期 ");
        return false;
    }

    public static String getVideoMp4Time(String filePath) {
        if (retriever == null) {
            retriever = new MediaMetadataRetriever();
        }
        String s = " ";
        try {
            retriever.setDataSource(filePath);
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);
            s = TimeUtil.stringForTime(timeInMillisec);
        } catch (Exception e) {

        }
        return s;
    }

    public static String stringForTime(long timeMs) {

        long totalSeconds = (timeMs + 0) / 1000; //+500是 四舍五入
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static String secondToHours(long times) {
        String hours;
        DecimalFormat decimalFormat = new DecimalFormat("0");
        hours = decimalFormat.format(times / 3600f);
        return hours;
    }
    public static String intToMinutes(int count){
        String time = null;
        int minute = count/60;
        int second = count%60;
        String strMinute = "";
        String strSecond = "";
        if(minute < 10)
            strMinute = "0"+ minute;
        else
            strMinute = ""+minute;
        if(second < 10)
            strSecond =  "0"+second;
        else
            strSecond = ""+second;
        time = strMinute + ":" + strSecond;
        return time;
    }
}
