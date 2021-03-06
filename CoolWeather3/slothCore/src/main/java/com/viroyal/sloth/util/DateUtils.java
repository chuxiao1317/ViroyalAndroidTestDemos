package com.viroyal.sloth.util;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sloth.core.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by LiGang on 2016/6/1.
 */
@SuppressWarnings("ALL")
@SuppressLint("SimpleDateFormat")
public class DateUtils {
    /**
     * 获取当前时间
     *
     * @return
     */
    public static String currentDateTime() {
        return currentDateTime(DateStyle.YYYY_MM_DD);
    }

    public static long toTodayDate(long time) {

        String day = currentDateTime();
        String h = formatSecondTimestamp(time, DateStyle.HH_MM_SS);

        Date date = format(day + " " + h, DateStyle.YYYY_MM_DD_HH_MM_SS);
        if (null != date) {
            return date.getTime();
        }
        return 0;
    }

    public static long toTodayDate(String time) {

        String day = currentDateTime();

        Date date = format(day + " " + time, DateStyle.YYYY_MM_DD_HH_MM);
        if (null != date) {
            return date.getTime();
        }
        return 0;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     */
    public static int daysBetween(Date smdate, Date bdate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateStyle.YYYY_MM_DD_HH_MM_SS);
        try {
            smdate = sdf.parse(sdf.format(smdate));
            bdate = sdf.parse(sdf.format(bdate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(smdate);
            long time1 = cal.getTimeInMillis();
            cal.setTime(bdate);
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateStyle.YYYY_MM_DD_HH_MM_SS);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);

            return Integer.parseInt(String.valueOf(between_days));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当前时间
     *
     * @param dateStyle
     * @return
     */
    public static long getTime(String date, String dateStyle) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateStyle);
        try {
            return formatter.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getWeek() {
        Calendar cal = Calendar.getInstance();
//   cal.setTime(new Date(System.currentTimeMillis()));
        cal.setTime(new Date(System.currentTimeMillis()));
        int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        return week;
    }

    /**
     * 获取当前时间
     *
     * @param dateStyle
     * @return
     */
    public static String currentDateTime(String dateStyle) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateStyle);
        return formatter.format(new Date());
    }

    /**
     * 格式化时间
     *
     * @param date  时间
     * @param style 格式
     * @return 格式化结果
     */
    public static Date format(String date, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style);
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 格式化时间
     *
     * @param date  时间
     * @param style 格式
     * @return 格式化结果
     */
    public static String format(Date date, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style);
        return format.format(date);
    }

    public static long startDate(long date) {
        long time = date / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();// 今天零点零分零秒的毫秒数
        return time;
    }

    public static Date startOfDate(Date date) {
        long time = date.getTime() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset();// 今天零点零分零秒的毫秒数
        return new Date(time);
    }

    public static boolean before2(Date date, Date now) {
        if (date.before(now)) {
            return true;
        }
        return false;
    }

    public static boolean before(Date date, Date now) {
        if (date.before(now)) {
            return true;
        } else {
            long between = date.getTime() - now.getTime();

            if (between > (24 * 3600000)) {
                return false;
            }
            return true;
        }
    }

    public static Date endOfDate(Date date) {
        long time = date.getTime() / (1000 * 3600 * 24) * (1000 * 3600 * 24) - TimeZone.getDefault().getRawOffset() + 24 * 60 * 60 * 1000 - 1;
        return new Date(time);
    }

    /**
     * 格式化timestamp格式的时间，以秒为单位
     *
     * @param timestamp 秒
     * @param dateStyle 格式
     * @return 格式化结果
     */
    public static String formatSecondTimestamp(long timestamp, String dateStyle) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp * 1000);
        return format(c.getTime(), dateStyle);
    }

    /**
     * 根据时间戳，格式化日期，当天只返回时间，其他返回日期
     *
     * @param timestamp 秒
     * @return 格式化的时间
     */
    public static String getCurrentTimeIFCurrentDayOrDate(long timestamp) {
        Calendar now = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timestamp * 1000);
        if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
                && c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
            return format(c.getTime(), DateStyle.HH_MM);
        }
        return format(c.getTime(), DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取yyyy-MM-dd格式日期
     *
     * @param context
     * @param date
     * @return
     */
    public static String getDateFormatYYYYMMDD(Context context, Date date) {
        if (context == null) return null;
        String formatter = context.getResources().getString(R.string.request_date_formatter);
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        return format.format(date);
    }

    /**
     * 把一个日期格式的字符串转化为日期
     *
     * @param context
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parseStringToDate(Context context, String dateStr) throws ParseException {
        if (context == null || dateStr == null) return null;
        String formatter = context.getResources().getString(R.string.request_date_formatter);
        SimpleDateFormat format = new SimpleDateFormat(formatter);
        return format.parse(dateStr);
    }

    public static String formatDateTime(Date date) {
        String text;
        long dateTime = date.getTime();
        if (isSameDay(dateTime)) {
            Calendar calendar = GregorianCalendar.getInstance();
            if (inOneMinute(dateTime, calendar.getTimeInMillis())) {
                return "刚刚";
            } else if (inOneHour(dateTime, calendar.getTimeInMillis())) {
                return String.format("%d分钟之前", Math.abs(dateTime - calendar.getTimeInMillis()) / 60000);
            } else {
                calendar.setTime(date);
                text = "HH:mm";
//                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//                if (hourOfDay > 17) {
//                    text = "晚上 hh:mm";
//                } else if (hourOfDay >= 0 && hourOfDay <= 6) {
//                    text = "凌晨 hh:mm";
//                } else if (hourOfDay > 11 && hourOfDay <= 17) {
//                    text = "下午 hh:mm";
//                } else {
//                    text = "上午 hh:mm";
//                }
            }
        } else if (isYesterday(dateTime)) {
            text = "昨天 HH:mm";
        } else if (isSameYear(dateTime)) {
            text = "M月d日 HH:mm";
        } else {
            text = "yyyy-M-d HH:mm";
        }

        // 注意，如果使用android.text.format.DateFormat这个工具类，在API 17之前它只支持adEhkMmszy
        return new SimpleDateFormat(text, Locale.CHINA).format(date);
    }

    private static boolean inOneMinute(long time1, long time2) {
        return Math.abs(time1 - time2) < 60000;
    }

    private static boolean inOneHour(long time1, long time2) {
        return Math.abs(time1 - time2) < 3600000;
    }

    private static boolean isSameDay(long time) {
        long startTime = floorDay(Calendar.getInstance()).getTimeInMillis();
        long endTime = ceilDay(Calendar.getInstance()).getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isYesterday(long time) {
        Calendar startCal;
        startCal = floorDay(Calendar.getInstance());
        startCal.add(Calendar.DAY_OF_MONTH, -1);
        long startTime = startCal.getTimeInMillis();

        Calendar endCal;
        endCal = ceilDay(Calendar.getInstance());
        endCal.add(Calendar.DAY_OF_MONTH, -1);
        long endTime = endCal.getTimeInMillis();
        return time > startTime && time < endTime;
    }

    private static boolean isSameYear(long time) {
        Calendar startCal;
        startCal = floorDay(Calendar.getInstance());
        startCal.set(Calendar.MONTH, Calendar.JANUARY);
        startCal.set(Calendar.DAY_OF_MONTH, 1);
        return time >= startCal.getTimeInMillis();
    }

    private static Calendar floorDay(Calendar startCal) {
        startCal.set(Calendar.HOUR_OF_DAY, 0);
        startCal.set(Calendar.MINUTE, 0);
        startCal.set(Calendar.SECOND, 0);
        startCal.set(Calendar.MILLISECOND, 0);
        return startCal;
    }

    private static Calendar ceilDay(Calendar endCal) {
        endCal.set(Calendar.HOUR_OF_DAY, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 999);
        return endCal;
    }
}
