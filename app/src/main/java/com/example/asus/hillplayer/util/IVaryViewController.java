package com.example.asus.hillplayer.util;

import android.view.View;

/**
 * 切换view控制的接口
 * Created by asus-cp on 2016-12-19.
 */

public interface IVaryViewController {

    void showLoadingView(String loadString);

    void showErrorView(String error, View.OnClickListener listener);

    void showTargetView();
}
