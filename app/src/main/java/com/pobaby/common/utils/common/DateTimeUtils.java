package com.pobaby.common.utils.common;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenqh on 2016/8/29.
 * 时间工具类
 */
public class DateTimeUtils {

    // 一天的毫秒数
    private static final long DAY_OF_MILLIS = 24 * 60 * 60 * 1000;

    /**
     * 时间格式类型类型
     * ALL(0), YEAR_MONTH_DAY_HOURS_MINS(1), YEAR_MONTH_DAY(2), HOURS_MINS(3), HOURS_MINS_SECOND(4);
     */
    public enum DateTimeType {

        ALL(0),
        YEAR_MONTH_DAY_HOURS_MINS(1),
        YEAR_MONTH_DAY(2),
        MONTH_DAY(3),
        HOURS_MINS(4),
        HOURS_MINS_SECOND(5);
        public int value;

        DateTimeType(int value) {
            this.value = value;
        }
    }

    /**
     * 根据时间字符串获取Calendar对象
     *
     * @param dateTime
     * @return Calendar
     */
    public static Calendar getCalendar(String dateTime) {
        return getCalendar(dateTime, DateTimeType.ALL);
    }

    /**
     * 根据时间字符串获取Calendar对象
     *
     * @param dateTime
     * @param type
     * @return Calendar
     */
    public static Calendar getCalendar(String dateTime, DateTimeType type) {

        Calendar calendar = Calendar.getInstance();
        if (TextUtils.isEmpty(dateTime))
            return calendar;
        calendar.setTime(getDate(dateTime, type));

        return calendar;
    }

    /**
     * 获取当前时间
     *
     * @author chenqh
     * @email 403167386@qq.com
     * @created 2018/6/15 11:24
     */
    public static class Now {

        public static String toString(DateTimeType type) {
            return getFormatString(Calendar.getInstance(), type);
        }
    }

    /**
     * 根据时间字符串获取Date对象
     *
     * @param dateTime
     * @return Date
     */
    public static Date getDate(String dateTime, DateTimeType type) {

        Date date = new Date();
        if (TextUtils.isEmpty(dateTime))
            return date;

        try {
            date = getFormat(type).parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    /**
     * 根据时间字符串获取Date对象
     *
     * @param dateTime
     * @param type     获取格式的类型
     * @return DateFormatString
     */
    public static String getFormatString(String dateTime, DateTimeType type) {

        SimpleDateFormat mFormat = getFormat(type);
        return mFormat.format(getDate(dateTime, DateTimeType.ALL));
    }

    /**
     * 根据时间字符串获取Date对象
     *
     * @param dateTime
     * @param type     获取格式的类型
     * @return DateFormatString
     */
    public static String getFormatString(Date dateTime, DateTimeType type) {

        SimpleDateFormat mFormat = getFormat(type);
        return mFormat.format(dateTime);
    }


    /**
     * 根据时间字符串获取Date对象
     *
     * @param dateTime
     * @param type     获取格式的类型
     * @return DateFormatString
     */
    public static String getFormatString(Calendar dateTime, DateTimeType type) {

        SimpleDateFormat mFormat = getFormat(type);
        return mFormat.format(dateTime.getTime());
    }

    /**
     * 根据格式的类型获取时间格式对象
     *
     * @param type 格式的类型
     * @return SimpleDateFormat
     */
    public static SimpleDateFormat getFormat(DateTimeType type) {
        SimpleDateFormat mFormat;

        switch (type) {
            case ALL:
                mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;

            case YEAR_MONTH_DAY_HOURS_MINS:
                mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                break;

            case YEAR_MONTH_DAY:
                mFormat = new SimpleDateFormat("yyyy-MM-dd");
                break;

            case MONTH_DAY:
                mFormat = new SimpleDateFormat("MM-dd");
                break;

            case HOURS_MINS:
                mFormat = new SimpleDateFormat("HH:mm");
                break;

            case HOURS_MINS_SECOND:
                mFormat = new SimpleDateFormat("HH:mm:ss");
                break;

            default:
                mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                break;
        }
        return mFormat;
    }

    /**
     * 设置每个阶段时间
     */
    private static final int seconds_of_1minute = 60;
    private static final int seconds_of_30minutes = 30 * 60;
    private static final int seconds_of_1hour = 60 * 60;
    private static final int seconds_of_1day = 24 * 60 * 60;
    private static final int seconds_of_15days = seconds_of_1day * 15;
    private static final int seconds_of_30days = seconds_of_1day * 30;
    private static final int seconds_of_6months = seconds_of_30days * 6;
    private static final int seconds_of_1year = seconds_of_30days * 12;

    /**
     * 格式化时间
     *
     * @param mTime
     * @return
     */
    public static String getTimeRange(String mTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /**获取当前时间*/
        Date curDate = new Date(System.currentTimeMillis());
        String dataStrNew = sdf.format(curDate);
        Date startTime = null;
        try {
            /**将时间转化成Date*/
            curDate = sdf.parse(dataStrNew);
            startTime = sdf.parse(mTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**除以1000是为了转换成秒*/
        long between = (curDate.getTime() - startTime.getTime()) / 1000;
        int elapsedTime = (int) (between);
        if (elapsedTime < seconds_of_1minute) {
            return "刚刚";
        }
        if (elapsedTime < seconds_of_30minutes) {
            return elapsedTime / seconds_of_1minute + "分钟前";
        }
        if (elapsedTime < seconds_of_1hour) {
            return "半小时前";
        }
        if (elapsedTime < seconds_of_1day) {
            return elapsedTime / seconds_of_1hour + "小时前";
        }
        if (elapsedTime < seconds_of_15days) {
            return elapsedTime / seconds_of_1day + "天前";
        }
        if (elapsedTime < seconds_of_30days) {
            return "半个月前";
        }
        if (elapsedTime < seconds_of_6months) {
            return elapsedTime / seconds_of_30days + "月前";
        }
        if (elapsedTime < seconds_of_1year) {
            return "半年前";
        }
        if (elapsedTime >= seconds_of_1year) {
            return elapsedTime / seconds_of_1year + "年前";
        }
        return "";
    }

    /**
     * 根据毫秒返回时分秒
     *
     * @param time
     * @return
     */
    public static String getFormatHMS(long time) {
        time = time / 1000;//总秒数
        int s = (int) (time % 60);//秒
        int m = (int) (time / 60);//分
        int h = (int) (time / 3600);//秒
        return String.format("%02d:%02d:%02d", h, m, s);
    }

    /**
     * 根据时间 返回毫秒  注意int最大值问题（这里处理录音问题 不做判断）
     *
     * @param hsm
     * @param split
     * @return
     */
    public static int getHMSToInt(String hsm, String split) {
        int result = 0;
        String[] arr = hsm.split(split);
        if (arr.length == 1) {
            result = Integer.valueOf(arr[0]) * 1000;
        } else if (arr.length == 2) {
            result = Integer.valueOf(arr[0]) * 60 * 1000 + Integer.valueOf(arr[0]) * 1000;
        } else if (arr.length == 3) {
            result = Integer.valueOf(arr[0]) * 60 * 60 * 1000 + Integer.valueOf(arr[1]) * 60 * 1000 + Integer.valueOf(arr[2]) * 1000;
        }
        return result;
    }

    /**
     * 根据传递时间增加（减少 负数）
     *
     * @param format "HH:mm:ss"
     * @param type   Calendar.SECOND
     * @param date   "00:00:00"
     * @param count  -1
     */
    public static String addTime(String format, int type, String date, int count) {
        String result = "";
        try {
            DateFormat f = new SimpleDateFormat(format);
            Date today = f.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(type, count);
            Date tomorrow = c.getTime();
            result = f.format(tomorrow);
        } catch (ParseException e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算2个时间相差的天数
     *
     * @param begin 年份
     * @param end   月份
     * @return 相差天数
     */
    public static int getDifferDays(Calendar begin, Calendar end) {
        return (int) ((end.getTimeInMillis() - begin.getTimeInMillis()) / DAY_OF_MILLIS);
    }

}
