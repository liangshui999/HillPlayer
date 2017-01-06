package com.example.asus.hillplayer.util;

/**
 * 时间转换的工具类
 * Created by asus-cp on 2017-01-05.
 */

public class TimeHelper {

    public static final int MINITUE = 60000;

    public static final int SECONDS = 1000;

    /**
     * 将给定的ms转换成“03：20”这种格式
     * @param duration
     * @return
     */
    public static String convertMS2StanrdTime(int duration){
        int minute = duration/MINITUE;
        int seconds = (duration - minute * MINITUE)/SECONDS;
        return addPrefix(minute)+" : "+addPrefix(seconds);
    }

    /**
     * 如果给定的数字小于10，则加上0，比如8，返回08
     * @param number
     * @return
     */
    private static String addPrefix(int number){
        return number < 10 ? "0"+number : ""+number;
    }
}
