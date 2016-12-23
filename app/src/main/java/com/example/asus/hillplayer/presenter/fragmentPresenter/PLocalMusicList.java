package com.example.asus.hillplayer.presenter.fragmentPresenter;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.callback.Callback;
import com.example.asus.hillplayer.model.IModel.IModelLocalMusicList;
import com.example.asus.hillplayer.model.modleIml.ModelLocalMusicList;
import com.example.asus.hillplayer.presenter.BasePresenter;
import com.example.asus.hillplayer.view.fragmentViewL.IViewLoaclMusicList;

import java.util.List;

/**
 * Created by asus-cp on 2016-12-22.
 */

public class PLocalMusicList extends BasePresenter<IViewLoaclMusicList>{

    private IViewLoaclMusicList mVloaclMusicList;

    private IModelLocalMusicList mMLocalMusicList;

    public PLocalMusicList() {
        mMLocalMusicList = new ModelLocalMusicList(this);
    }

    public void fetchMusicList(){
        this.mVloaclMusicList = getView();//getView()不能在构造方法中调用,必须在构造方法调用之后才能调用
        mVloaclMusicList.showLoading(R.string.loading);
        mMLocalMusicList.fetchMusicList(new Callback<List<Music>>() {
            @Override
            public void onfetchData(List<Music> result) {
                mVloaclMusicList.hideLoading();
                mVloaclMusicList.refreshUI(result);
            }
        });

    }
}
