package com.example.asus.hillplayer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.asus.hillplayer.R;
import com.example.asus.hillplayer.beans.Music;
import com.example.asus.hillplayer.presenter.fragmentPresenter.PLocalMusicList;
import com.example.asus.hillplayer.view.fragmentViewL.IViewLoaclMusicList;

import java.util.List;

/**
 * 本地音乐列表
 * Created by asus-cp on 2016-12-22.
 */

public class LocalMusicListFragment extends BaseFragment<IViewLoaclMusicList, PLocalMusicList>
implements IViewLoaclMusicList{

    private RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_local_music_list,container);
        refreshVaryViewController(v);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyler_local_music_list);
        return v;
    }

    @Override
    protected PLocalMusicList createPresenter() {
        return new PLocalMusicList(this);
    }



    @Override
    public void initData() {
        mPresenter.fetchMusicList();
    }

    @Override
    public void initUI(List<Music> musics) {

    }

    @Override
    public void refreshUI(List<Music> musics) {

    }
}
