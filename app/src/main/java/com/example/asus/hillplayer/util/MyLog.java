package com.example.asus.hillplayer.util;

import android.util.Log;

/**
 * 自己的打印类
 * Created by asus-cp on 2016-12-23.
 */

public class MyLog{

    private static final int V = 1;

    private static final int D = 2;

    private static final int I = 3;

    private static final int W = 4;

    private static final int E = 5;

    public static final int NOTHING = 6;

    /**
     * 当前水平
     */
    private static int LEVEL = V;


    private MyLog(){}


    public static void v(String tag, String msg) {
        if (LEVEL <= V) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (LEVEL <= D) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL <= I) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL <= W) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL <= E) {
            Log.e(tag, msg);
        }
    }

}
