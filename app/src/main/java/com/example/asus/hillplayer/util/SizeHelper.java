package com.example.asus.hillplayer.util;

import android.content.Context;
import android.text.BoringLayout;
import android.util.DisplayMetrics;

/**
 * 主要用于像素间转换
 * Created by asus-cp on 2017-01-09.
 */

public class SizeHelper {

    private static Context sContex = MyAppliacation.getContext();


    public static float convertDp2Px(float dp){
        DisplayMetrics metrics = sContex.getResources().getDisplayMetrics();
        float dpi = metrics.density;
        return dpi * dp;
    }

    public static float convertPx2Dp(float px){
        DisplayMetrics metrics = sContex.getResources().getDisplayMetrics();
        float dpi = metrics.density;
        return px / dpi;
    }
}
