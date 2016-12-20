package com.example.asus.hillplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.hillplayer.presenter.BasePresenter;
import com.example.asus.hillplayer.receiver.INetObserver;
import com.example.asus.hillplayer.receiver.NetStateReceiver;
import com.example.asus.hillplayer.util.IVaryViewController;
import com.example.asus.hillplayer.util.VaryViewController;
import com.example.asus.hillplayer.util.VaryViewHelper;
import com.example.asus.hillplayer.viewinterface.BaseViewInterface;

/**
 * Created by asus-cp on 2016-12-19.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends Fragment
implements BaseViewInterface{

    protected String TAG;

    protected Context mContext;

    protected int mScreenWidth;

    protected int mScrrenHeight;

    protected int mDpi;

    protected IVaryViewController mVaryViewController;

    protected NetStateReceiver mNetStateReceiver;

    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter=createPresenter();
        mPresenter.attachView((V) this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        init();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        getActivity().unregisterReceiver(mNetStateReceiver);
    }

    private void init(){
        mContext = getActivity();
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth=metrics.widthPixels;
        mScrrenHeight=metrics.heightPixels;
        mDpi=metrics.densityDpi;
        mVaryViewController=new VaryViewController(new VaryViewHelper(getTargetView()));
        mNetStateReceiver=new NetStateReceiver();
        mNetStateReceiver.registerObserver(new INetObserver() {
            @Override
            public void onNetConnected() {

            }

            @Override
            public void onNetDisConnected() {

            }
        });
        IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(mNetStateReceiver,intentFilter);
    };

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
        Intent intent = new Intent(mContext, clz);
        startActivity(intent);
    }

    public void readyGo(Class<?> clz, Intent intent){
        startActivity(intent);
    }

    public void readyGoThenKill(Class<?> clz, Intent intent){
        readyGo(clz, intent);
        getActivity().finish();
    }

    public void readyGoForResult(Intent intent, int RequestCode){
        startActivityForResult(intent,RequestCode);
    }


    /**
     * 创建presenter
     * @return
     */
    protected abstract T createPresenter();

    /**
     * 获取需要加载的view,这个view在加载出来之前显示loading view
     * 加载出错的时候显示error view
     * @return
     */
    public abstract View getTargetView();

    public abstract void initView();

    public abstract void initData();
}
