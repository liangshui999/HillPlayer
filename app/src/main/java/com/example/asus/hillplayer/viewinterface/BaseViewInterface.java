package com.example.asus.hillplayer.viewinterface;

import android.view.View;

/**
 * Created by asus-cp on 2016-12-20.
 */

public interface BaseViewInterface {

    void showLoading(String loadString);

    void hideLoading();

    void showError(String Error , View.OnClickListener onClickListener);
}
