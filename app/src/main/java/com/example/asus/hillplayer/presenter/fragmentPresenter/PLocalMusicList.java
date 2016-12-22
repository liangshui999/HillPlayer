package com.example.asus.hillplayer.presenter.fragmentPresenter;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Music;
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

    public PLocalMusicList(IViewLoaclMusicList mVloaclMusicList) {
        this.mVloaclMusicList = mVloaclMusicList;
        mMLocalMusicList = new ModelLocalMusicList();
    }

    public void fetchMusicList(){
        mVloaclMusicList.showLoading(R.string.loading);
        List<Music> musics = mMLocalMusicList.fetchMusicList();

    }
}
