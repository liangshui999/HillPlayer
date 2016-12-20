package com.example.asus.hillplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus-cp on 2016-12-19.
 */

public class NetStateReceiver extends BroadcastReceiver implements INetStateReceiver{

    private List<INetObserver> mNetObservers;

    public NetStateReceiver() {
        mNetObservers=new ArrayList<>();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager= (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isAvailable()){
            for(INetObserver observer : mNetObservers){
                observer.onNetConnected();
            }
        }else {
            for(INetObserver observer : mNetObservers){
                observer.onNetDisConnected();
            }
        }
    }

    @Override
    public void registerObserver(INetObserver observer) {
        mNetObservers.add(observer);
    }

    @Override
    public void unRegisterObserver(INetObserver observer) {
        mNetObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {

    }
}
