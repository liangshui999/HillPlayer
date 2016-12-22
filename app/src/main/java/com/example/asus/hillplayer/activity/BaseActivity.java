package com.example.asus.hillplayer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.presenter.BasePresenter;
import com.example.asus.hillplayer.receiver.INetObserver;
import com.example.asus.hillplayer.receiver.NetStateReceiver;
import com.example.asus.hillplayer.util.IVaryViewController;
import com.example.asus.hillplayer.util.VaryViewController;
import com.example.asus.hillplayer.util.VaryViewHelper;
import com.example.asus.hillplayer.view.BaseViewInterface;

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

    protected INetObserver mNetObserver;

    protected T mPresenter;

    private Toolbar mToolBar;

    private LinearLayout mContentLinearLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        mPresenter = createPresenter();
        mPresenter.attachView((V) this);
        init();

    }

    private void init() {
        mContentLinearLayout = (LinearLayout) findViewById(R.id.ll_content);
        mToolBar= (Toolbar) findViewById(R.id.title);

        TAG = this.getClass().getSimpleName();
        mContext = this;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        mDpi = metrics.densityDpi;


        mVaryViewController = new VaryViewController(new VaryViewHelper(mContentLinearLayout));
        mNetStateReceiver = new NetStateReceiver();
        IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mNetStateReceiver,intentFilter);


        mToolBar.setTitle("base的toolbar");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setTitleTextColor(getResources().getColor(R.color.white));


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        unregisterReceiver(mNetStateReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置内容的布局
     * @param contentId
     */
    public void setContentLayout(int contentId){
        View v = LayoutInflater.from(this).inflate(contentId, null);
        setContentLayout(v);
    }

    /**
     * 设置内容的布局
     */
    public void setContentLayout(View v){
        mContentLinearLayout.addView(v);
    }

    /**
     * 隐藏头部
     */
    public void hideTitle(){
        mToolBar.setVisibility(View.GONE);
    }

    /**
     * 设置头部
     * @param title
     */
    public void setTitle(String title){
        mToolBar.setTitle(title);
    }

    /**
     * 设置头部
     * @param resId
     */
    public void setTitle(int resId){
        mToolBar.setTitle(resId);
    }

    @Override
    public void showLoading(String loadString) {
        mVaryViewController.showLoadingView(loadString);
    }

    @Override
    public void showLoading(int resId) {
        showLoading(getString(resId));
    }


    @Override
    public void hideLoading() {
        mVaryViewController.showTargetView();
    }

    @Override
    public void showError(String error,View.OnClickListener onClickListener) {
        mVaryViewController.showErrorView(error,onClickListener);
    }

    @Override
    public void showError(int resId, View.OnClickListener onClickListener) {
        showError(getString(resId), onClickListener);
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

    public void setmNetObserver(INetObserver mNetObserver) {
        this.mNetObserver = mNetObserver;
        if(mNetObserver != null){
            mNetStateReceiver.registerObserver(mNetObserver);
        }
    }


    @Deprecated
    private View getTargetView(){
        return mContentLinearLayout;
    };

    /**
     * 手动设置viewcontroller
     */
    protected void refreshVaryViewController(View v){
        mVaryViewController = new VaryViewController(new VaryViewHelper(v));
    }

    public abstract void initView();

    public abstract void initData();

    /**
     * 获取presenter
     * @return
     */
    public abstract T createPresenter() ;



}
