package com.example.asus.hillplayer.receiver;

/**
 * 网络状态的被观察者的接口
 * Created by asus-cp on 2016-12-19.
 */

public interface INetStateReceiver {

    void registerObserver(INetObserver observer);

    void unRegisterObserver(INetObserver observer);

    void notifyObservers();
}
