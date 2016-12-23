package com.example.asus.hillplayer.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by asus-cp on 2016-12-23.
 */

public class MyAppliacation extends Application {

    private static  Context sContex;

    @Override
    public void onCreate() {
        super.onCreate();
        sContex = getApplicationContext();
    }

    public static Context getContext(){
        return sContex;
    }
}
