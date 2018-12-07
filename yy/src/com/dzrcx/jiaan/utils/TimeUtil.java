package com.dzrcx.jiaan.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by chenh on 2017/1/5.
 */

public class TimeUtil {
    public static String getData(int dayDifference){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dayDifference); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return  df.format(date);
    }

    public static String getHistryData(int dayDifference){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, dayDifference); //得到前一天
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return  df.format(date);
    }

    public static String getSpecifiedDay(Long time,int days) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        c.setTime(new Date(time));
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + days);

        String dayBefore = new SimpleDateFormat("MM月dd日 HH:mm").format(c
                .getTime());
        return dayBefore;
    }

    public static String getSimpleData(Long time) {//可以用new Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        String strData = "";
        try {
            date = new Date(time);
            strData = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (Exception e) {

        }

        return strData;
    }

    /*
* 毫秒转化
*/
    public static String formatTime(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strDay + " 天 " + strHour + " 时 " + strMinute + " 分 " ;
    }

    /*
* 毫秒转化
*/
    public static String formatTimeMinute(long ms) {

        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;//毫秒
        strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;

        return strMinute;
    }
}
