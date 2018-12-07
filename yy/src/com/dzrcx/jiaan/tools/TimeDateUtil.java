package com.dzrcx.jiaan.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 *
 * @author zhangyu
 */
public class TimeDateUtil {


    /**
     * 将时间秒 转换成 X天X小时X分
     *
     * @param second
     * @return
     */
    public static String DHMTransform(int second) {

        if (second < 0) {
            return null;
        }
        second = second / 1000;

        int d, h, m, s, r;
        d = second / (24 * 3600);
        r = second % (24 * 3600);
        h = r / 3600;
        r = second % 3600;
        m = r / 60;
        s = r % 60;

        StringBuffer buffer = new StringBuffer();
        if (d > 0) {
            buffer.append(d).append("天 ");
        }
        if (h > 0) {
            buffer.append(h).append("小时 ");
        }
        // if (m > 0) {
        buffer.append(m).append("分钟");
        // }

        return buffer.toString();
    }

    /**
     * 将时间微秒 转换成小时
     *
     * @param second
     * @return
     */
    public static long HTransform(long second) {
        if (second < 0) {
            return 0;
        }
        second = second / 1000;
        long h;
        h = second / 3600;
        return h;
    }

    public static String MDHMTransform(long second) {

        if (second == 0) {
            return "";
        }

        String date = new java.text.SimpleDateFormat("MM月dd日 HH:mm")
                .format(new java.util.Date(second));

        return date;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
     */
    public static String dateToStrLong(long second) {
        if (second == 0) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(second);
        return dateString;
    }

    /**
     * 将长时间格式时间转换为字符串 yyyyMM
     */
    public static String dateToYM(long second) {
        if (second == 0) {
            return "";
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String dateString = formatter.format(second);
        return dateString;
    }

    /*
         * 毫秒转化
         */
    public static String formatTime(long ms) {
        long ss = 1000;
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;
        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "" + (hour + day * 24) : "" + (hour + day * 24);// 小时
        String strMinute = minute < 10 ? "" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        return strHour + "小时" + strMinute + "分";
    }

    /*
         * 毫秒转化
         */
    public static String formatTimeMin(long ms) {
        long ss = 1000;
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;
        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "" + hour : "" + hour;// 小时
        String strMinute = minute < 10 ? "" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;
        if ("0".equals(strHour)) {
            return strMinute + "分";
        } else {
            return strHour + "小时" + strMinute + "分";
        }

    }

    /*
         * 毫秒转化
         */
    public static long[] formatTime_HM(long ms) {
        long ss = 1000;
        long mi = ss * 60;
        long hh = mi * 60;
        long dd = hh * 24;
        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second
                * ss;
        String strDay = day < 10 ? "0" + day : "" + day; // 天
        String strHour = hour < 10 ? "" + hour : "" + hour;// 小时
        String strMinute = minute < 10 ? "" + minute : "" + minute;// 分钟
        String strSecond = second < 10 ? "0" + second : "" + second;// 秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
                + milliSecond;// 毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
                + strMilliSecond;

        long[] time = null;
        if (hour != 0) {
            time = new long[]{
                    hour + day * 24, minute
            };
        } else {
            time = new long[]{
                    minute
            };
        }

        return time;
    }


    /**
     * @param ms     时间戳
     * @param format 格式
     * @return
     */

    public static String formatTime(long ms, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(ms));
    }


    public static String getTime(long millis) {
        String todySDF = "今天  HH:mm";
        String yesterDaySDF = "昨天  HH:mm";
        String otherSDF = "MM/dd  HH:mm";
        SimpleDateFormat sfd = null;
        String time = "";
        LG.d("gtgt", dateToStrLong(millis) + "--------------------------------------------------------");
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(millis);
        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(millis);
            return time;
        } else {
            targetCalendar.add(Calendar.DAY_OF_YEAR, -1);
            if (dateCalendar.after(targetCalendar)) {
                sfd = new SimpleDateFormat(yesterDaySDF);
                time = sfd.format(millis);
                return time;
            } else {
                targetCalendar.add(Calendar.DAY_OF_YEAR, -5);
                if (dateCalendar.after(targetCalendar)) {
                    return getWeekHHmm(millis);
                }
//                else {
//                    targetCalendar.add(Calendar.DAY_OF_MONTH, -3);
//                    if (dateCalendar.after(targetCalendar)) {
//                        return getWeekHHmm(millis);
//                    } else {
//                        targetCalendar.add(Calendar.DAY_OF_MONTH, -4);
//                        if (dateCalendar.after(targetCalendar)) {
//                            return getWeekHHmm(millis);
//                        } else {
//                            targetCalendar.add(Calendar.DAY_OF_MONTH, -5);
//                            if (dateCalendar.after(targetCalendar)) {
//                                return getWeekHHmm(millis);
//                            } else {
//                                targetCalendar.add(Calendar.DAY_OF_MONTH, -6);
//                                if (dateCalendar.after(targetCalendar)) {
//                                    return getWeekHHmm(millis);
//                                }
//                            }
//                        }
//                    }
//            }
            }
        }

        sfd = new SimpleDateFormat(otherSDF);
        time = sfd.format(millis);
        return time;
    }

    public static String getWeekHHmm(Long millis) {
        String time = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm");
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                time = "周一  " + sfd.format(millis);
                break;
            case Calendar.TUESDAY:
                time = "周二  " + sfd.format(millis);
                break;
            case Calendar.WEDNESDAY:
                time = "周三  " + sfd.format(millis);
                break;
            case Calendar.THURSDAY:
                time = "周四  " + sfd.format(millis);
                break;
            case Calendar.FRIDAY:
                time = "周五  " + sfd.format(millis);
                break;
            case Calendar.SATURDAY:
                time = "周六  " + sfd.format(millis);
                break;
            case Calendar.SUNDAY:
                time = "周日  " + sfd.format(millis);
                break;
        }
        return time;
    }

}
