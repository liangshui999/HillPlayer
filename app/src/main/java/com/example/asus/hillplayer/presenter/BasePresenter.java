package com.example.asus.hillplayer.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by asus-cp on 2016-12-20.
 */

public abstract class BasePresenter<V>{

    protected Reference<V> mReference;

    public void attachView(V view){
        mReference = new WeakReference<>(view);
    }

    public void detachView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    public V getView(){
        return mReference.get();
    }

    public boolean isViewAttached(){
        return mReference != null && mReference.get() != null;
    }

}
