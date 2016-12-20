package com.example.asus.hillplayer.util;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.asus.hillplayer.R;

/**
 * Created by asus-cp on 2016-12-19.
 */

public class VaryViewController implements IVaryViewController {

    private VaryViewHelper mHelper;

    public VaryViewController(VaryViewHelper mHelper) {
        this.mHelper = mHelper;
    }

    @Override
    public void showLoadingView(String loadString) {
        View v=mHelper.inflateView(R.layout.loading);
        TextView textView= (TextView) v.findViewById(R.id.loading_msg);
        textView.setText(loadString);
        mHelper.varyView(v);
    }

    @Override
    public void showErrorView(String error, View.OnClickListener listener) {
        View v=mHelper.inflateView(R.layout.message);
        TextView textView= (TextView) v.findViewById(R.id.message_info);
        textView.setText(error);
        mHelper.varyView(v);

        if(listener != null){
            v.setOnClickListener(listener);
        }
    }

    @Override
    public void showTargetView() {
        mHelper.reset();
    }
}
