package com.example.asus.hillplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.example.asus.hillplayer.presenter.BasePresenter;
import com.example.asus.hillplayer.receiver.INetObserver;
import com.example.asus.hillplayer.receiver.NetStateReceiver;
import com.example.asus.hillplayer.util.IVaryViewController;
import com.example.asus.hillplayer.util.VaryViewController;
import com.example.asus.hillplayer.util.VaryViewHelper;
import com.example.asus.hillplayer.viewinterface.BaseViewInterface;

/**
 * Created by asus-cp on 2016-12-20.
 */

public abstract class BaseActivity<V , T extends BasePresenter<V> > extends AppCompatActivity
implements BaseViewInterface{

    protected String TAG;

    protected Context mContext;

    protected int mScreenWidth;

    private int mScreenHeight;

    protected int mDpi;

    protected IVaryViewController mVaryViewController;

    protected NetStateReceiver mNetStateReceiver;

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        init();

    }

    private void init() {
        TAG = this.getClass().getSimpleName();
        mContext = this;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mDpi = metrics.densityDpi;

        mVaryViewController = new VaryViewController(new VaryViewHelper(getTargetView()));
        mNetStateReceiver = new NetStateReceiver();
        mNetStateReceiver.registerObserver(new INetObserver() {
            @Override
            public void onNetConnected() {

            }

            @Override
            public void onNetDisConnected() {

            }
        });
        IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetStateReceiver,intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        unregisterReceiver(mNetStateReceiver);
    }

    @Override
    public void showLoading(String loadString) {
        mVaryViewController.showLoadingView(loadString);
    }

    @Override
    public void hideLoading() {
        mVaryViewController.showTargetView();
    }

    @Override
    public void showError(String error,View.OnClickListener onClickListener) {
        mVaryViewController.showErrorView(error,onClickListener);
    }


    public void showToast(String msg){
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    public void readyGo(Class<?> clz){
        Intent intent = new Intent(this, clz);
        startActivity(intent);
    }

    public void readyGo(Class<?> clz, Intent intent){
        startActivity(intent);
    }

    public void readyGoThenKill(Class<?> clz, Intent intent){
        readyGo(clz, intent);
        this.finish();
    }

    public void readyGoForResult(Intent intent, int RequestCode){
        startActivityForResult(intent,RequestCode);
    }



    public abstract void initView();

    public abstract void initData();

    /**
     * 获取presenter
     * @return
     */
    public abstract T createPresenter() ;

    /**
     * 获取需要加载的view,这个view在加载出来之前显示loading view
     * 加载出错的时候显示error view
     * @return
     */
    public abstract View getTargetView();

}
