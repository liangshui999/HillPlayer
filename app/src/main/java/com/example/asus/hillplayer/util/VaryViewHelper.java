package com.example.asus.hillplayer.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 切换view的帮助类
 * Created by asus-cp on 2016-12-19.
 */

public class VaryViewHelper {

    private Context mContext;

    private LayoutInflater mLayoutInflater;


    /**
     * 需要变换的目标view
     */
    private View mTargetView;

    /**
     * 目标view的父view
     */
    private ViewGroup mParentView;

    /**
     * 目标view的布局参数
     */
    private ViewGroup.LayoutParams mLayoutParams;

    /**
     * 目标view在父view中的索引位置
     */
    private int mIndex;

    public VaryViewHelper(View mTargetView) {
        if(mTargetView==null){
            throw new IllegalArgumentException("目标view为空");
        }

        this.mTargetView = mTargetView;
        mContext=mTargetView.getContext();
        mLayoutInflater=LayoutInflater.from(mContext);

        init(mTargetView);
    }


    /**
     * 获取targetview的各种参数
     * @param mTargetView
     */
    private void init(View mTargetView) {
        mLayoutParams=mTargetView.getLayoutParams();
        ViewGroup parent= (ViewGroup) mTargetView.getParent();
        if(parent != null){
            mParentView=parent;
            int count = mParentView.getChildCount();
            for(int i = 0;i < count ; i++){
                View v = mParentView.getChildAt(i);
                if(v == mTargetView){
                    mIndex = i;
                    break;
                }
            }
        }else {
            mParentView= (ViewGroup) mTargetView.getRootView().findViewById(android.R.id.content);
            mParentView.addView(mTargetView);
            mIndex = 0;
        }
    }

    /**
     * 变换view
     * @param v 准备临时替换目标view的view，比如说正在加载的view或者是加载出错时的view
     */
    public void varyView(View v){
        mParentView.removeViewAt(mIndex);
        mParentView.addView(v,mIndex,mLayoutParams);
    }

    /**
     * 将view替换成目标view
     */
    public void reset(){
        varyView(mTargetView);
    }


    public View inflateView(int resId){
        return mLayoutInflater.inflate(resId,null);
    }
}
