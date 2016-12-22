package com.example.asus.hillplayer.view;

import android.view.View;

/**
 * Created by asus-cp on 2016-12-20.
 */

public interface BaseViewInterface {

    void showLoading(String loadString);

    void showLoading(int resId);

    void hideLoading();

    void showError(String Error , View.OnClickListener onClickListener);

    void showError(int resId, View.OnClickListener onClickListener);
}
